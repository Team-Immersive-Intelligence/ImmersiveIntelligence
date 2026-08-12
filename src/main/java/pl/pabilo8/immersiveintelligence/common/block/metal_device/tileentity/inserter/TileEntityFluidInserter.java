package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter;

import blusunrize.immersiveengineering.api.energy.wires.WireType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
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
import pl.pabilo8.immersiveintelligence.api.crafting.IngredientReference;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.FluidInserter;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityLatexCollector;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Fluid variant of {@link TileEntityInserter}.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 12.08.2026
 * @ii-approved 0.3.1
 * @since 15.07.2019
 */
public class TileEntityFluidInserter extends TileEntityInserterBase
{
	public static final LinkedHashMap<String, Supplier<InserterTask>> TASKS = new LinkedHashMap<>();
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
	@SyncNBT(name = "buffer", events = {SyncEvents.TILE_CUSTOM1, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_GUI_OPENED})
	public FluidTank buffer;

	private final IFluidHandler fluidWrapper = new IFluidHandler()
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

	public TileEntityFluidInserter()
	{
		buffer = new FluidTank(getMaxTakeAmount());
	}

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
	public LinkedHashMap<String, Supplier<InserterTask>> getAvailableTasks()
	{
		return TASKS;
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
			return (T)fluidWrapper;
		return super.getCapability(capability, facing);
	}

	@Override
	public void onPacketReceive(DataPacket packet)
	{
		taskRetryDelay = 0;
		final boolean[] changed = {false};

		IIDataHandlingUtils.expectingStringParam('c', packet, command -> {
			DataType a = packet.get('a');

			switch(command)
			{
				case "add":
				{
					if(packet.has('a')&&"fluid".equals(a.toString()))
					{
						tasks.add(new InserterTaskFluid());
						changed[0] = true;
					}
				}
				break;
				case "remove":
				{
					int before = tasks.size();
					IIDataHandlingUtils.expectingIntegerParam('a', packet, idx -> {
						if(idx >= 0&&idx < tasks.size())
							tasks.remove(idx);
					});
					changed[0] = tasks.size()!=before;
				}
				break;
				case "clear":
				{
					changed[0] = !tasks.isEmpty();
					tasks.clear();
					current = null;
					currentTaskIndex = -1;
				}
				break;
			}
		});

		refreshCurrentTask();
		if(changed[0])
		{
			markDirty();
			if(!world.isRemote)
				updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
		}
	}

	@Nonnull
	@Override
	public ItemStack getTileDrop(@Nullable EntityPlayer player, IBlockState state)
	{
		return withDropData(new ItemStack(state.getBlock(), 1, IIBlockTypes_Connector.FLUID_INSERTER.getMeta()));
	}

	/**
	 * Single supported task: move fluid from input handler to internal buffer, then from buffer to output handler.
	 */
	public static class InserterTaskFluid extends InserterTask
	{
		@Nullable
		private transient FluidStack plannedFluid;

		public InserterTaskFluid()
		{
			super();
		}

		@Override
		public boolean canExecute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			TileEntityFluidInserter self = (TileEntityFluidInserter)tile;
			int requested = getAmountToBeTaken(self);
			if(requested <= 0)
				return false;

			EnumFacing facing = in?facingIn: facingOut;
			IFluidHandler handler = getTargetCapability(world, in?posIn: posOut,
					CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite(), in);
			if(handler==null)
				return false;

			if(in)
			{
				int room = self.buffer.getCapacity()-self.buffer.getFluidAmount();
				if(room <= 0||strictAmount&&room < requested)
					return false;

				FluidStack drained = drain(handler, Math.min(requested, room), false);
				if(!matches(drained))
					return false;
				int accepted = self.buffer.fill(drained, false);
				if(accepted <= 0||strictAmount&&accepted < requested)
					return false;

				plannedFluid = drained.copy();
				plannedFluid.amount = accepted;
				return true;
			}

			FluidStack buffered = self.buffer.getFluid();
			FluidStack offer = buffered!=null&&buffered.amount > 0?buffered.copy(): copyPlannedFluid();
			if(!matches(offer))
				return false;

			int filled = handler.fill(offer, false);
			if(buffered!=null&&buffered.amount > 0)
				return filled >= offer.amount;
			if(filled <= 0||strictAmount&&filled < offer.amount)
				return false;
			if(filled < offer.amount)
				plannedFluid.amount = filled;
			return true;
		}

		@Override
		public boolean execute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			TileEntityFluidInserter self = (TileEntityFluidInserter)tile;
			EnumFacing facing = in?facingIn: facingOut;
			IFluidHandler handler = getTargetCapability(world, in?posIn: posOut,
					CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite(), in);
			if(handler==null)
				return false;

			if(in)
			{
				FluidStack planned = copyPlannedFluid();
				if(!matches(planned))
					return false;

				FluidStack simulated = handler.drain(planned, false);
				if(!matches(simulated)||simulated.amount < planned.amount)
					return false;
				FluidStack drained = handler.drain(planned, true);
				return matches(drained)&&drained.amount==planned.amount
						&&self.buffer.fill(drained, true)==drained.amount;
			}

			FluidStack buffered = self.buffer.getFluid();
			if(!matches(buffered))
				return false;
			FluidStack offer = buffered.copy();
			if(handler.fill(offer, false) < offer.amount)
				return false;

			FluidStack drained = self.buffer.drain(offer, true);
			if(drained==null||drained.amount <= 0)
				return false;
			int filled = handler.fill(drained, true);
			if(filled < drained.amount)
			{
				FluidStack remainder = drained.copy();
				remainder.amount -= filled;
				self.buffer.fill(remainder, true);
				return false;
			}
			if(!isJob)
				stack.inputSize = Math.max(0, stack.inputSize-filled);
			return true;
		}

		@Nullable
		private FluidStack drain(IFluidHandler handler, int amount, boolean doDrain)
		{
			if(stack.isWildcard())
				return handler.drain(amount, doDrain);
			if(stack.fluid==null)
				return null;

			FluidStack requested = stack.useNBT?stack.fluid.copy(): findMatchingFluid(handler);
			if(requested==null)
				return null;
			requested.amount = amount;
			return handler.drain(requested, doDrain);
		}

		@Nullable
		private FluidStack findMatchingFluid(IFluidHandler handler)
		{
			for(IFluidTankProperties properties : handler.getTankProperties())
			{
				FluidStack contents = properties.getContents();
				if(contents!=null&&contents.amount > 0&&stack.matchesFluidStackIgnoringSize(contents))
					return contents.copy();
			}
			return null;
		}

		protected final void setPlannedFluid(@Nullable FluidStack fluid)
		{
			plannedFluid = fluid==null?null: fluid.copy();
		}

		@Nullable
		protected final FluidStack copyPlannedFluid()
		{
			return plannedFluid==null?null: plannedFluid.copy();
		}

		protected boolean matches(@Nullable FluidStack fluid)
		{
			return fluid!=null&&fluid.amount > 0&&stack.matchesFluidStackIgnoringSize(fluid);
		}

		@Override
		protected void clearCachedTargets()
		{
			super.clearCachedTargets();
			plannedFluid = null;
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
		@Nullable
		private transient EntityCow cachedCow;

		public InserterTaskMilkCow()
		{
			configureStack();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			super.deserializeNBT(nbt);
			configureStack();
		}

		private void configureStack()
		{
			FluidStack milk = FluidRegistry.getFluidStack("milk", FluidInserter.cowMilkAmount);
			if(milk!=null)
			{
				stack = new IngredientReference(milk);
				stack.inputSize = FluidInserter.cowMilkAmount;
			}
		}

		@Override
		public boolean canExecute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(!in)
				return super.canExecute(tile, world, posIn, posOut, facingIn, facingOut, false);

			TileEntityFluidInserter self = (TileEntityFluidInserter)tile;
			FluidStack milk = FluidRegistry.getFluidStack("milk", FluidInserter.cowMilkAmount);
			if(milk==null||self.buffer.getCapacity()-self.buffer.getFluidAmount() < milk.amount)
				return false;

			if(cachedCow==null||cachedCow.isDead)
				cachedCow = world.getEntitiesWithinAABB(EntityCow.class, new AxisAlignedBB(posIn))
						.stream().findFirst().orElse(null);
			if(cachedCow==null)
				return false;

			setPlannedFluid(milk);
			return true;
		}

		@Override
		public boolean execute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(!in)
				return super.execute(tile, world, posIn, posOut, facingIn, facingOut, false);

			TileEntityFluidInserter self = (TileEntityFluidInserter)tile;
			FluidStack milk = copyPlannedFluid();
			if(cachedCow==null||cachedCow.isDead||milk==null)
				return false;
			return self.buffer.fill(milk, true)==milk.amount;
		}

		@Override
		protected void clearCachedTargets()
		{
			super.clearCachedTargets();
			cachedCow = null;
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
		@Nullable
		private transient TileEntityLatexCollector cachedCollector;

		public InserterTaskLatexCollectorDrain()
		{
			configureStack();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			super.deserializeNBT(nbt);
			configureStack();
		}

		private void configureStack()
		{
			stack = new IngredientReference(new FluidStack(IIContent.fluidLatex, 10));
			stack.inputSize = 10;
		}

		@Override
		public boolean canExecute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(!in)
				return super.canExecute(tile, world, posIn, posOut, facingIn, facingOut, false);

			TileEntityFluidInserter self = (TileEntityFluidInserter)tile;
			int room = self.buffer.getCapacity()-self.buffer.getFluidAmount();
			int requested = getAmountToBeTaken(self);
			if(room <= 0||requested <= 0)
				return false;

			TileEntity target = world.getTileEntity(posIn);
			cachedCollector = target instanceof TileEntityLatexCollector?(TileEntityLatexCollector)target: null;
			if(cachedCollector==null)
				return false;

			int available = cachedCollector.getAvailableLatexMilliBuckets();
			int amount = Math.min(requested, Math.min(room, available));
			if(amount <= 0||strictAmount&&amount < requested)
				return false;

			setPlannedFluid(new FluidStack(IIContent.fluidLatex, amount));
			return true;
		}

		@Override
		public boolean execute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			if(!in)
				return super.execute(tile, world, posIn, posOut, facingIn, facingOut, false);

			TileEntityFluidInserter self = (TileEntityFluidInserter)tile;
			FluidStack planned = copyPlannedFluid();
			if(cachedCollector==null||planned==null||planned.amount <= 0)
				return false;

			int drained = cachedCollector.drainLatexMilliBuckets(planned.amount, true);
			if(drained <= 0)
				return false;
			int filled = self.buffer.fill(new FluidStack(IIContent.fluidLatex, drained), true);
			if(!isJob&&filled > 0)
				stack.inputSize = Math.max(0, stack.inputSize-filled);
			return filled==drained;
		}

		@Override
		protected void clearCachedTargets()
		{
			super.clearCachedTargets();
			cachedCollector = null;
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
