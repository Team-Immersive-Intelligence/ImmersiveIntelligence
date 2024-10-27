package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IComparatorOverride;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ITileDrop;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.device.DataWireNetwork;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.FluidInserter;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.wire.IIDataWireType;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * @author Pabilo8
 * @since 15-07-2019
 */
// TODO: 26.07.2022 rework
public class TileEntityFluidInserter extends TileEntityImmersiveConnectable implements ITileDrop, IComparatorOverride, IHammerInteraction, ITickable, IBlockBounds, IDataConnector
{
	public int energyStorage = 0;
	public EnumFacing outputFacing = EnumFacing.NORTH;
	public EnumFacing inputFacing = EnumFacing.SOUTH;
	public int fluidToTake = 0;
	protected Set<String> acceptablePowerWires = ImmutableSet.of(WireType.LV_CATEGORY, WireType.MV_CATEGORY);
	protected DataWireNetwork wireNetwork = new DataWireNetwork().add(this);
	WireType secondCable;
	String fluidTakeMode = "set";
	SidedFluidHandler outputFluidHandler = new SidedFluidHandler(this, outputFacing);
	SidedFluidHandler inputFluidHandler = new SidedFluidHandler(this, inputFacing);
	private boolean refreshWireNetwork = false;

	@Override
	protected boolean canTakeLV()
	{
		return true;
	}

	@Override
	protected boolean canTakeMV()
	{
		return true;
	}

	@Override
	public boolean canConnect()
	{
		return true;
	}

	@Override
	public boolean isEnergyOutput()
	{
		return true;
	}

	@Override
	public int outputEnergy(int amount, boolean simulate, int energyType)
	{
		if(amount > 0&&energyStorage < FluidInserter.energyCapacity)
		{
			if(!simulate)
			{
				int rec = Math.min(FluidInserter.energyCapacity-energyStorage, FluidInserter.energyUsage);
				energyStorage += rec;
				return rec;
			}
			return Math.min(FluidInserter.energyCapacity-energyStorage, FluidInserter.energyUsage);
		}
		return 0;
	}

	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset)
	{
		int tc = getTargetedConnector(target);
		return canAttach(cableType, tc);
	}

	private boolean canAttach(WireType toAttach, int conn)
	{
		String attachCat = toAttach.getCategory();

		if(attachCat==null)
			return false;

		if(conn==0)
		{
			return attachCat.equals(IIDataWireType.DATA_CATEGORY)&&limitType==null;
		}
		else if(conn==1)
		{
			return acceptablePowerWires.contains(attachCat)&&secondCable==null;
		}

		return false;
	}

	@Override
	public void connectCable(WireType cableType, TargetingInfo target, IImmersiveConnectable other)
	{
		switch(getTargetedConnector(target))
		{
			case 0:
				if(this.limitType==null)
				{
					DataWireNetwork.updateConnectors(pos, world, wireNetwork);
					this.limitType = cableType;
				}
				break;
			case 1:
				if(secondCable==null)
					this.secondCable = cableType;
				break;
		}
		this.markContainingBlockForUpdate(null);
	}

	@Override
	public WireType getCableLimiter(TargetingInfo target)
	{
		switch(getTargetedConnector(target))
		{
			case 0:
				return limitType;
			case 1:
				return secondCable;
		}
		return null;
	}

	@Override
	public void removeCable(Connection connection)
	{
		WireType type = connection!=null?connection.cableType: null;
		if(type==null)
		{
			limitType = null;
			secondCable = null;
		}
		if(type==limitType)
		{
			wireNetwork.removeFromNetwork(this);
			this.limitType = null;
		}
		if(type==secondCable)
			this.secondCable = null;
		this.markContainingBlockForUpdate(null);
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		boolean right = con.cableType==limitType;
		return getConnectionOffset(con, right);
	}

	@Override
	public Vec3d getConnectionOffset(Connection con, TargetingInfo target, Vec3i offsetLink)
	{
		return getConnectionOffset(con, getTargetedConnector(target)==0);
	}

	private Vec3d getConnectionOffset(Connection con, boolean data)
	{
		if(data)
			return new Vec3d(0.875f, 0.5f, 0.875f);
		else
			return new Vec3d(0.125f, 0.475f, 0.125f);
	}

	public int getTargetedConnector(TargetingInfo target)
	{
		if(target.hitX < 1&&target.hitX > 0.75&&target.hitZ < 1&&target.hitZ > 0.75)
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}

	public WireType getLimiter(int side)
	{
		if(side==0)
			return limitType;
		return secondCable;
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);

		if(message.hasKey("energyStorage"))
			energyStorage = message.getInteger("energyStorage");

		if(message.hasKey("outputFacing"))
			outputFacing = EnumFacing.getFront(message.getInteger("outputFacing"));
		if(message.hasKey("inputFacing"))
			inputFacing = EnumFacing.getFront(message.getInteger("inputFacing"));

	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);

		fluidToTake = nbt.getInteger("fluidToTake");
		fluidTakeMode = nbt.getString("fluidTakeMode");

		outputFacing = EnumFacing.getFront(nbt.getInteger("outputFacing"));
		inputFacing = EnumFacing.getFront(nbt.getInteger("inputFacing"));
		if(nbt.hasKey("secondCable"))
			secondCable = ApiUtils.getWireTypeFromNBT(nbt, "secondCable");
		else
			secondCable = null;
		energyStorage = nbt.getInteger("energyStorage");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);

		nbt.setInteger("fluidToTake", fluidToTake);
		nbt.setString("fluidTakeMode", fluidTakeMode);

		nbt.setInteger("outputFacing", outputFacing.ordinal());
		nbt.setInteger("inputFacing", inputFacing.ordinal());
		if(secondCable!=null)
			nbt.setString("secondCable", secondCable.getUniqueName());
		nbt.setInteger("energyStorage", energyStorage);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY&&(facing==inputFacing||facing==outputFacing))
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			if(facing==this.inputFacing)
				return (T)inputFluidHandler;
			else if(facing==this.outputFacing)
				return (T)outputFluidHandler;

		}
		return super.getCapability(capability, facing);
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0f, 0, 0f, 1f, 0.1875f, 1f};
	}

	@Override
	public int getComparatorInputOverride()
	{
		return 0;
	}

	@Override
	public void readOnPlacement(@Nullable EntityLivingBase placer, ItemStack stack)
	{

		if(stack.hasTagCompound())
		{
			receiveMessageFromServer(stack.getTagCompound());
		}

	}

	@Override
	public void update()
	{
		if(hasWorld()&&!world.isRemote&&!refreshWireNetwork)
		{
			refreshWireNetwork = true;
			wireNetwork.removeFromNetwork(null);
		}

	}

	@Override
	public boolean hammerUseSide(EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		if(world.isRemote)
			return true;

		/*if (side==EnumFacing.UP || side==EnumFacing.DOWN)
			side=player.getHorizontalFacing().getOpposite();*/

		EnumFacing hitside = null;

		if(IIMath.isPointInRectangle(0.25, 0.75, 0.75, 1, hitX, hitZ))
			hitside = EnumFacing.SOUTH;
		else if(IIMath.isPointInRectangle(0.25, 0, 0.75, 0.25, hitX, hitZ))
			hitside = EnumFacing.NORTH;
		else if(IIMath.isPointInRectangle(0.75, 0.25, 1, 0.75, hitX, hitZ))
			hitside = EnumFacing.EAST;
		else if(IIMath.isPointInRectangle(0, 0.25, 0.25, 0.75, hitX, hitZ))
			hitside = EnumFacing.WEST;


		if(hitside!=null)
		{
			if(player.isSneaking())
			{
				if(inputFacing==hitside)
					inputFacing = EnumFacing.UP;
				outputFacing = hitside;
			}
			else
			{
				if(outputFacing==hitside)
					outputFacing = EnumFacing.UP;
				inputFacing = hitside;
			}
		}

		outputFluidHandler.facing = outputFacing;
		inputFluidHandler.facing = inputFacing;

		markContainingBlockForUpdate(null);

		IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT()
				.withInt("inputFacing", inputFacing.ordinal())
				.withInt("outputFacing", outputFacing.ordinal())
		));

		return true;
	}

	@Override
	public DataWireNetwork getDataNetwork()
	{
		return wireNetwork;
	}

	@Override
	public void setDataNetwork(DataWireNetwork net)
	{
		wireNetwork = net;
	}

	@Override
	public void onDataChange()
	{
		if(!isInvalid())
		{
			markDirty();
			IBlockState stateHere = world.getBlockState(pos);
			markContainingBlockForUpdate(stateHere);
		}
	}

	@Override
	public World getConnectorWorld()
	{
		return getWorld();
	}

	@Override
	public void onPacketReceive(DataPacket packet)
	{
		if(packet.getPacketVariable('m').getName().equals("string"))
		{
			fluidTakeMode = ((DataTypeString)packet.getPacketVariable('m')).value;
		}

		if(packet.getPacketVariable('c').getName().equals("integer"))
		{
			int items = ((DataTypeInteger)packet.getPacketVariable('c')).value;
			fluidToTake = (fluidTakeMode.equals("add"))?fluidToTake+items: items;
		}
	}

	@Override
	public void sendPacket(DataPacket packet)
	{
		//Nope
	}

	@Override
	public boolean moveConnectionTo(Connection c, BlockPos newEnd)
	{
		return true;
	}

	@Override
	public ItemStack getTileDrop(@Nullable EntityPlayer player, IBlockState state)
	{
		ItemStack stack = new ItemStack(state.getBlock(), 1, IIBlockTypes_Connector.FLUID_INSERTER.getMeta());
		ItemNBTHelper.setInt(stack, "outputFacing", outputFacing.ordinal());
		ItemNBTHelper.setInt(stack, "inputFacing", inputFacing.ordinal());
		return stack;
	}

	//Copied from TileEntityWoodenBarrel
	static class SidedFluidHandler implements IFluidHandler
	{
		TileEntityFluidInserter internal;
		EnumFacing facing;

		SidedFluidHandler(TileEntityFluidInserter internal, EnumFacing facing)
		{
			this.internal = internal;
			this.facing = facing;
		}

		@Override
		public int fill(FluidStack resource, boolean doFill)
		{
			/*
			if (doFill && barrel.fluidToTake<=0)
			{
				IILogger.info("res.scatter");
				return 0;
			}

			if (barrel.world.getTileEntity(barrel.pos.offset(facing))!=null && barrel.world.getTileEntity(barrel.pos.offset(barrel.outputFacing)).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,barrel.outputFacing.getOpposite()))
			{
				TileEntity te = barrel.world.getTileEntity(barrel.pos.offset(barrel.outputFacing));
				IFluidHandler handler = ((IFluidHandler)te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,barrel.outputFacing.getOpposite()));

				if (doFill && barrel.energyStorage<fluid_inserter.energyUsage)
					return 0;
				if (doFill)
				{
					barrel.energyStorage-=fluid_inserter.energyUsage;
				}
				FluidStack res2 = resource.copy();

				//Max scatter to output
				res2.scatter=Math.min(resource.scatter,Math.min(fluid_inserter.maxOutput,barrel.fluidToTake));
				int maxfill =Math.min(resource.scatter,Math.min(fluid_inserter.maxOutput,barrel.fluidToTake));
				int left = handler.fill(res2,doFill);

				if (doFill)
				{
					IILogger.info(maxfill-left);
					barrel.fluidToTake-=(maxfill-left);
				}

				return resource.scatter-(maxfill-left);
			}
			 */

			//IILogger.info(barrel.world.getTileEntity(barrel.pos.offset(barrel.outputFacing)).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, barrel.outputFacing.getOpposite()));

			if(internal.energyStorage >= FluidInserter.energyUsage&&internal.fluidToTake > 0&&internal.world.getTileEntity(internal.pos.offset(internal.outputFacing))!=null&&internal.world.getTileEntity(internal.pos.offset(internal.outputFacing)).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, internal.outputFacing.getOpposite()))
			{
				TileEntity te = internal.world.getTileEntity(internal.pos.offset(internal.outputFacing));
				IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, internal.outputFacing.getOpposite());

				BlockPos pos = internal.pos.offset(internal.outputFacing);

				FluidStack res2 = resource.copy();
				res2.amount = Math.min(resource.amount, Math.min(FluidInserter.maxOutput, internal.fluidToTake));
				int maxfill = Math.min(resource.amount, Math.min(FluidInserter.maxOutput, internal.fluidToTake));
				int left = handler.fill(res2, doFill);

				if(doFill)
				{
					internal.fluidToTake -= left;
					internal.energyStorage -= FluidInserter.energyUsage;
				}

				return left;

			}

			return 0;
		}

		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain)
		{
			return null;
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain)
		{
			return null;
		}

		@Override
		public IFluidTankProperties[] getTankProperties()
		{
			return new FluidTank(0).getTankProperties();
		}
	}
}
