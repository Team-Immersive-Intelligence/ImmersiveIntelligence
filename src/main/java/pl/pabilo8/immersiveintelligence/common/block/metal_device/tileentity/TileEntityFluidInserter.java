package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity;

import blusunrize.immersiveengineering.api.energy.wires.WireType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.FluidInserter;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityInserterBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
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

			if(in)
			{
				if(self.buffer.getFluidAmount() >= self.buffer.getCapacity())
					return false;

				TileEntity te = world.getTileEntity(posIn);
				if(te==null||!te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facingIn.getOpposite()))
					return false;

				IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facingIn.getOpposite());
				if(handler==null)
					return false;

				int toMove = Math.min(tile.takeAmount, self.buffer.getCapacity()-self.buffer.getFluidAmount());
				FluidStack simulated = handler.drain(toMove, false);
				return simulated!=null&&simulated.amount > 0;
			}
			else
			{
				if(self.buffer.getFluidAmount() <= 0)
					return false;

				TileEntity te = world.getTileEntity(posOut);
				if(te==null||!te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facingOut.getOpposite()))
					return false;

				IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facingOut.getOpposite());
				if(handler==null)
					return false;

				FluidStack inTank = self.buffer.getFluid();
				if(inTank==null||inTank.amount <= 0)
					return false;

				int toFill = Math.min(tile.takeAmount, inTank.amount);
				FluidStack offer = inTank.copy();
				offer.amount = toFill;

				return handler.fill(offer, false) > 0;
			}
		}

		@Override
		public boolean execute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in)
		{
			TileEntityFluidInserter self = (TileEntityFluidInserter)tile;

			if(in)
			{
				TileEntity te = world.getTileEntity(posIn);
				if(te==null)
					return false;

				IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facingIn.getOpposite());
				if(handler==null)
					return false;

				int toMove = Math.min(tile.takeAmount, self.buffer.getCapacity()-self.buffer.getFluidAmount());
				FluidStack drainedSim = handler.drain(toMove, false);
				if(drainedSim==null||drainedSim.amount <= 0)
					return false;

				int accepted = self.buffer.fill(drainedSim, true);
				if(accepted <= 0)
					return false;

				FluidStack actuallyDrain = drainedSim.copy();
				actuallyDrain.amount = accepted;
				handler.drain(actuallyDrain, true);
				return true;
			}
			else
			{
				TileEntity te = world.getTileEntity(posOut);
				if(te==null)
					return false;

				IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facingOut.getOpposite());
				if(handler==null)
					return false;

				FluidStack inTank = self.buffer.getFluid();
				if(inTank==null||inTank.amount <= 0)
					return false;

				int toFill = Math.min(tile.takeAmount, inTank.amount);
				FluidStack offer = inTank.copy();
				offer.amount = toFill;

				int filled = handler.fill(offer, true);
				if(filled <= 0)
					return false;

				self.buffer.drain(filled, true);
				return self.buffer.getFluidAmount()==0; // finish phase when buffer emptied
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
}
