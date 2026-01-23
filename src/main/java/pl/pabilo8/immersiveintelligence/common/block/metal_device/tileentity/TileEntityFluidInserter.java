package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.FluidInserter;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityInserterBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Fluid variant of the inserter.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 19.01.2026
 * @ii-approved 0.3.1
 * @since 15.07.2019
 */
public class TileEntityFluidInserter extends TileEntityInserterBase
{
	public static final HashMap<String, Function<NBTTagCompound, InserterTask>> TASKS = new HashMap<>();
	private static final Set<String> WIRES = ImmutableSet.of(WireType.LV_CATEGORY, WireType.MV_CATEGORY);

	static
	{
		TASKS.put("fluid", InserterTaskFluid::new);
		TASKS.put("fluid_milk_cow", InserterTaskMilkCow::new);
		TASKS.put("fluid_latex_collector", InserterTaskLatexCollectorDrain::new);
	}

	/**
	 * Temporary internal buffer between input drain and output fill.
	 */
	private FluidTank buffer = new FluidTank(FluidInserter.maxTake);

	@Nonnull
	@Override
	protected Set<String> getAcceptedPowerWires()
	{
		return WIRES;
	}

	@Override
	public int getPickupSpeed()
	{
		return FluidInserter.taskTime;
	}

	@Override
	public int getEnergyUsage()
	{
		return FluidInserter.energyUsage;
	}

	@Override
	public int getEnergyCapacity()
	{
		return FluidInserter.energyCapacity;
	}

	@Override
	public int getMaxTakeAmount()
	{
		return FluidInserter.maxTake;
	}

	@Nonnull
	@Override
	public HashMap<String, Function<NBTTagCompound, InserterTask>> getAvailableTasks()
	{
		return TASKS;
	}

	@Override
	protected void handleSounds()
	{

	}

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(nbt.hasKey("buffer"))
			buffer.readFromNBT(nbt.getCompoundTag("buffer"));
	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setTag("buffer", buffer.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T)new IFluidHandler()
			{
				@Override
				public IFluidTankProperties[] getTankProperties()
				{
					return buffer.getTankProperties();
				}

				@Override
				public int fill(FluidStack resource, boolean doFill)
				{
					return buffer.fill(resource, doFill);
				}

				@Nullable
				@Override
				public FluidStack drain(FluidStack resource, boolean doDrain)
				{
					return buffer.drain(resource, doDrain);
				}

				@Nullable
				@Override
				public FluidStack drain(int maxDrain, boolean doDrain)
				{
					return buffer.drain(maxDrain, doDrain);
				}
			};
		return super.getCapability(capability, facing);
	}

	@Override
	public void onPacketReceive(DataPacket packet)
	{
		// minimal, "only fluid task" command surface
		IIDataHandlingUtils.expectingStringParam('c', packet, command -> {
			DataType a = packet.get('a'); // action name ("fluid") or index for remove

			switch(command)
			{
				case "add":
				{
					if(packet.has('a')&&"fluid".equals(a.toString()))
						tasks.add(new InserterTaskFluid(new NBTTagCompound()));
				}
				break;
				case "remove":
				{
					// allow removing by index only, keeps this tiny
					IIDataHandlingUtils.expectingIntegerParam('a', packet, idx -> {
						if(idx >= 0&&idx < tasks.size())
							tasks.remove(idx);
					});
				}
				break;
				case "clear":
				{
					tasks.clear();
					current = null;
				}
				break;
			}
		});

		sendUpdate();
	}

	@Nonnull
	@Override
	public ItemStack getTileDrop(@Nullable EntityPlayer player, IBlockState state)
	{
		// keep same block/meta, but ensure correct connector type for fluid inserter
		ItemStack stack = new ItemStack(state.getBlock(), 1, IIBlockTypes_Connector.FLUID_INSERTER.getMeta());
		// ...existing code from base uses defaultOutputFacing/defaultInputFacing via NBT in stack...
		return stack;
	}

	/**
	 * Single supported task: move fluid from input handler to internal buffer, then from buffer to output handler.
	 */
	public static class InserterTaskFluid extends InserterTask
	{
		public InserterTaskFluid(NBTTagCompound nbt)
		{
			super(nbt);
		}

		@Override
		public boolean canExecute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			TileEntityFluidInserter self = (TileEntityFluidInserter)tile;
			if(this.stack.inputSize <= 0)
				return false;

			//Determine which side to interact with
			EnumFacing facing = (in?facingIn: facingOut);
			BlockPos pos = (in?posIn: posOut);
			//Find the capability from the side
			IFluidHandler handler = IIUtils.getTileCapability(world, pos,
					CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
			if(handler==null)
				//Or from above (f.e. barrels)
				handler = IIUtils.getTileCapability(world, pos,
						CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
			if(handler==null) //Or an entity
			{
				Optional<Entity> first = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos),
								input -> input.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()))
						.stream()
						.findFirst();
				if(first.isPresent())
					handler = first.get().getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
			}
			if(handler==null)
				return false;

			//Perform the action
			if(in)
			{
				//Any fluid
				int toMove = getAmountToBeTaken(self);
				FluidStack drained;
				if("*".equals(stack.oreName))
					drained = handler.drain(toMove, false);
				else
				{
					FluidStack actuallyDrain = this.stack.fluid.copy();
					actuallyDrain.amount = toMove;
					drained = handler.drain(toMove, false);
				}
				return drained!=null&&drained.amount > 0&&(!this.strictAmount||drained.amount==toMove);
			}
			else
			{
				FluidStack offer = this.stack.fluid.copy();
				offer.amount = getAmountToBeTaken(self);
				int filled = handler.fill(offer, false);
				return filled > 0&&(!this.strictAmount||filled==offer.amount);
			}
		}

		@Override
		public boolean execute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			//Determine which side to interact with
			EnumFacing facing = (in?facingIn: facingOut);
			BlockPos pos = (in?posIn: posOut);
			//Find the capability from the side
			IFluidHandler handler = IIUtils.getTileCapability(world, pos,
					CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
			if(handler==null)
				//Or from above (f.e. barrels)
				handler = IIUtils.getTileCapability(world, pos,
						CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
			if(handler==null) //Or an entity
			{
				Optional<Entity> first = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos),
								input -> input.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()))
						.stream()
						.findFirst();
				if(first.isPresent())
					handler = first.get().getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
			}
			if(handler==null)
				return false;

			if(in)
			{
				//Any fluid
				int toMove = getAmountToBeTaken(tile);
				FluidStack drained;
				if("*".equals(stack.oreName))
					drained = handler.drain(toMove, true);
				else
				{
					FluidStack actuallyDrain = this.stack.fluid.copy();
					actuallyDrain.amount = toMove;
					drained = handler.drain(toMove, true);
				}
				if(drained!=null&&drained.amount > 0)
				{
					((TileEntityFluidInserter)tile).buffer.fill(drained, true);
					return true;
				}
				return false;
			}
			else
			{
				FluidStack offer = this.stack.fluid.copy();
				offer.amount = getAmountToBeTaken(tile);
				FluidStack drained = ((TileEntityFluidInserter)tile).buffer.drain(offer, true);
				if(drained!=null&&drained.amount > 0)
				{
					int filled = handler.fill(drained, true);
					//If we couldn't fill all, put back the rest
					if(filled < drained.amount)
					{
						FluidStack toReturn = drained.copy();
						toReturn.amount = drained.amount-filled;
						((TileEntityFluidInserter)tile).buffer.fill(toReturn, true);
					}
					if(!isJob)
						this.stack.inputSize -= filled;
					return true;
				}
				return false;
			}
		}

		@Override
		public String getName()
		{
			return "fluid";
		}

		@Override
		public float getTimeModifier()
		{
			return 0f;
		}
	}

	/**
	 * Milk a cow in front of the input position.
	 * Puts FluidInserter.maxTake mB of milk into the internal buffer per execution (config-driven).
	 */
	public static class InserterTaskMilkCow extends InserterTaskFluid
	{
		public InserterTaskMilkCow(NBTTagCompound nbt)
		{
			super(nbt);
			this.stack = new IngredientStack(FluidRegistry.getFluidStack("milk", FluidInserter.cowMilkAmount));
			this.stack.inputSize = FluidInserter.cowMilkAmount;
		}

		@Override
		public boolean canExecute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(!in)
				return super.canExecute(tile, world, posIn, posOut, facingIn, facingOut, false);
			return !world.getEntitiesWithinAABB(EntityCow.class, new AxisAlignedBB(posIn)).isEmpty();
		}

		@Override
		public boolean execute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(!in)
				return super.execute(tile, world, posIn, posOut, facingIn, facingOut, false);

			TileEntityFluidInserter self = (TileEntityFluidInserter)tile;
			if(world.getEntitiesWithinAABB(EntityCow.class, new AxisAlignedBB(posIn)).isEmpty())
				return false;

			FluidStack milk = new FluidStack(FluidRegistry.getFluid("milk"), FluidInserter.cowMilkAmount);
			if(milk.getFluid()==null)
				return false;

			int filled = self.buffer.fill(milk, true);
			return filled > 0;
		}

		@Override
		public String getName()
		{
			return "fluid_milk_cow";
		}

		@Override
		public float getTimeModifier()
		{
			return 1f;
		}

		@Override
		public boolean areDetailsEditable()
		{
			return false;
		}
	}

	/**
	 * Drain partial latex from a Latex Collector even when its full 1000mB bucket isn't finished.
	 * Uses collector progress fraction to compute available mB: floor((timer/collectTime)*1000).
	 */
	public static class InserterTaskLatexCollectorDrain extends InserterTaskFluid
	{
		public InserterTaskLatexCollectorDrain(NBTTagCompound nbt)
		{
			super(nbt);
			this.stack = new IngredientStack(new FluidStack(IIContent.fluidLatex, 10));
			this.stack.inputSize = 10;
		}

		@Override
		public boolean canExecute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(!in)
				return super.canExecute(tile, world, posIn, posOut, facingIn, facingOut, false);

			TileEntityFluidInserter self = (TileEntityFluidInserter)tile;
			final int room = self.buffer.getCapacity()-self.buffer.getFluidAmount();
			if(room <= 0)
				return false;

			TileEntity lc = world.getTileEntity(posIn);
			return lc instanceof TileEntityLatexCollector&&((TileEntityLatexCollector)lc).getAvailableLatexMilliBuckets() > 0;
		}

		@Override
		public boolean execute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(!in)
				return super.execute(tile, world, posIn, posOut, facingIn, facingOut, false);

			TileEntityFluidInserter self = (TileEntityFluidInserter)tile;

			TileEntity lc = world.getTileEntity(posIn);
			if(!(lc instanceof TileEntityLatexCollector)||((TileEntityLatexCollector)lc).getAvailableLatexMilliBuckets() <= 0)
				return false;
			int amountToBeTaken = getAmountToBeTaken(self);
			int drained = ((TileEntityLatexCollector)lc).drainLatexMilliBuckets(amountToBeTaken, true);
			if(drained <= 0)
				return false;
			self.buffer.fill(new FluidStack(IIContent.fluidLatex, drained), true);
			if(!isJob)
				this.stack.inputSize -= drained;
			return true;
		}

		@Override
		public String getName()
		{
			return "fluid_latex_collector";
		}

		@Override
		public float getTimeModifier()
		{
			return 2f;
		}

		@Override
		public boolean areDetailsEditable()
		{
			return false;
		}
	}
}
