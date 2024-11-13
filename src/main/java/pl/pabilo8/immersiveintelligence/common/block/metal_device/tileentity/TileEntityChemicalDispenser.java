package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.util.IESounds;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.NumericDataType;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ChemicalDispenser;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityIIChemthrowerShot;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.wire.IIDataWireType;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * @author Pabilo8
 * @since 15-07-2019
 */
public class TileEntityChemicalDispenser extends TileEntityImmersiveConnectable implements ITickable, IDirectionalTile, IBlockBounds, IDataConnector
{
	public int energyStorage = 0, plannedAmount = 0, scatter = 0;
	public float pitch = 0, plannedPitch = 0, yaw = 0, plannedYaw = 0;
	public boolean shouldIgnite = false;
	public FluidTank tank = new FluidTank(12000);
	public EnumFacing facing = EnumFacing.DOWN;

	protected Set<String> acceptablePowerWires = ImmutableSet.of(WireType.LV_CATEGORY, WireType.MV_CATEGORY);
	protected DataWireNetwork wireNetwork = new DataWireNetwork().add(this);

	WireType secondCable;
	SidedFluidHandler fluidHandler = new SidedFluidHandler(this, null);
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
		if(amount > 0&&energyStorage < ChemicalDispenser.energyCapacity)
		{
			if(!simulate)
			{
				int rec = Math.min(ChemicalDispenser.energyCapacity-energyStorage, ChemicalDispenser.energyUsage);
				energyStorage += rec;
				return rec;
			}
			return Math.min(ChemicalDispenser.energyCapacity-energyStorage, ChemicalDispenser.energyUsage);
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
			return attachCat.equals(IIDataWireType.DATA_CATEGORY)&&limitType==null;
		else if(conn==1)
			return acceptablePowerWires.contains(attachCat)&&secondCable==null;

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
			switch(facing)
			{
				default:
				case UP:
				{
					return new Vec3d(0.875f, 0.5f, 0.125f);
				}
				case DOWN:
					return new Vec3d(0.125f, 0.75f, 0.125f);
				case SOUTH:
					return new Vec3d(0.125, 0.125, 0.5);
				case NORTH:
					return new Vec3d(0.125, 0.875, 0.5);
				case EAST:
					return new Vec3d(0.5, 0.875, 0.125);
				case WEST:
					return new Vec3d(0.5, 0.875, 0.875);
			}
		else
			switch(facing)
			{
				default:
				case UP:
				{
					return new Vec3d(0.125f, 0.525f, 0.875f);
				}
				case DOWN:
					return new Vec3d(0.875f, 0.525f, 0.875f);
				case SOUTH:
					return new Vec3d(0.875, 0.875, 0.5);
				case NORTH:
					return new Vec3d(0.875, 0.125, 0.5);
				case EAST:
					return new Vec3d(0.5, 0.125, 0.875);
				case WEST:
					return new Vec3d(0.5, 0.125, 0.125);
			}
	}

	public int getTargetedConnector(TargetingInfo target)
	{
		switch(facing)
		{
			case UP:
			{
				if(target.hitX <= 1f&&target.hitX >= 0.75f&&target.hitZ <= 0.25f&&target.hitZ >= 0f)
					return 0;
				if(target.hitX <= 0.25&&target.hitX >= 0&&target.hitZ <= 1&&target.hitZ >= 0.75)
					return 1;
			}
			break;
			case DOWN:
			{
				if(target.hitX <= 0.25&&target.hitX >= 0&&target.hitZ <= 0.25f&&target.hitZ >= 0f)
					return 0;
				if(target.hitX <= 1f&&target.hitX >= 0.75f&&target.hitZ <= 1&&target.hitZ >= 0.75)
					return 1;
			}
			break;
			case NORTH:
			{
				// 0.125, 0.875, 0.5
				if(target.hitX <= 0.25&&target.hitX >= 0&&target.hitY <= 1&&target.hitY >= 0.75)
					return 0;
				// 0.875, 0.125, 0.5
				if(target.hitX <= 1f&&target.hitX >= 0.75f&&target.hitY <= 0.25f&&target.hitY >= 0f)
					return 1;
			}
			break;
			case SOUTH:
			{
				// 0.125, 0.125, 0.5
				if(target.hitX <= 0.25&&target.hitX >= 0&&target.hitY <= 0.25f&&target.hitY >= 0f)
					return 0;
				// 0.875, 0.875, 0.5
				if(target.hitX <= 1f&&target.hitX >= 0.75f&&target.hitY <= 1&&target.hitY >= 0.75)
					return 1;
			}
			break;
			case WEST:
			{
				// 0.5, 0.875, 0.875
				if(target.hitZ <= 1f&&target.hitZ >= 0.75f&&target.hitY <= 1&&target.hitY >= 0.75)
					return 0;
				// 0.5, 0.125, 0.125
				if(target.hitZ <= 0.25&&target.hitZ >= 0&&target.hitY <= 0.25f&&target.hitY >= 0f)
					return 1;
			}
			break;
			case EAST:
			{
				// 0.5, 0.875, 0.125
				if(target.hitZ <= 0.25&&target.hitZ >= 0&&target.hitY <= 1&&target.hitY >= 0.75)
					return 0;
				// 0.5, 0.875, 0.875
				if(target.hitZ <= 1f&&target.hitZ >= 0.75f&&target.hitY <= 0.25f&&target.hitY >= 0f)
					return 1;
			}
			break;
		}
		return -1;
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
		if(message.hasKey("plannedAmount"))
			plannedAmount = message.getInteger("plannedAmount");
		if(message.hasKey("scatter"))
			scatter = message.getInteger("scatter");
		if(message.hasKey("pitch"))
			pitch = message.getFloat("pitch");
		if(message.hasKey("yaw"))
			yaw = message.getFloat("yaw");
		if(message.hasKey("plannedYaw"))
			plannedYaw = message.getFloat("plannedYaw");
		if(message.hasKey("plannedPitch"))
			plannedPitch = message.getFloat("plannedPitch");
		if(message.hasKey("tank"))
			tank.readFromNBT(message.getCompoundTag("tank"));
		if(message.hasKey("shouldIgnite"))
			shouldIgnite = message.getBoolean("shouldIgnite");
		if(message.hasKey("facing"))
			facing = EnumFacing.getFront(message.getInteger("facing"));

	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(nbt.hasKey("secondCable"))
			secondCable = ApiUtils.getWireTypeFromNBT(nbt, "secondCable");
		else
			secondCable = null;
		energyStorage = nbt.getInteger("energyStorage");
		plannedAmount = nbt.getInteger("plannedAmount");
		scatter = nbt.getInteger("scatter");
		shouldIgnite = nbt.getBoolean("shouldIgnite");
		pitch = nbt.getInteger("pitch");
		yaw = nbt.getInteger("yaw");
		if(nbt.hasKey("facing"))
			facing = EnumFacing.getFront(nbt.getInteger("facing"));
		if(nbt.hasKey("tank"))
			tank.readFromNBT(nbt.getCompoundTag("tank"));
		if(nbt.hasKey("plannedYaw"))
			plannedYaw = nbt.getFloat("plannedYaw");
		if(nbt.hasKey("plannedPitch"))
			plannedPitch = nbt.getFloat("plannedPitch");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		if(secondCable!=null)
			nbt.setString("secondCable", secondCable.getUniqueName());
		nbt.setInteger("energyStorage", energyStorage);
		nbt.setInteger("plannedAmount", plannedAmount);
		nbt.setInteger("scatter", scatter);
		nbt.setBoolean("shouldIgnite", shouldIgnite);
		nbt.setFloat("pitch", pitch);
		nbt.setFloat("yaw", yaw);
		nbt.setInteger("facing", facing.ordinal());
		nbt.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
		nbt.setFloat("plannedYaw", plannedYaw);
		nbt.setFloat("plannedPitch", plannedPitch);
	}

	@Override
	public float[] getBlockBounds()
	{
		switch(this.facing)
		{
			case UP:
			{
				return new float[]{0, 0.5f, 0, 1, 1, 1};
			}
			case DOWN:
			{
				return new float[]{0, 0, 0, 1, 0.5f, 1};
			}
			case SOUTH:
			{
				return new float[]{0, 0, 0.5f, 1, 1, 1};
			}
			case NORTH:
			{
				return new float[]{0, 0, 0, 1, 1, 0.5f};
			}
			case EAST:
			{
				return new float[]{0.5f, 0, 0, 1, 1, 1};
			}
			case WEST:
			{
				return new float[]{0, 0, 0, 0.5f, 1, 1};
			}
		}
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	@Override
	public void update()
	{
		if(plannedPitch > pitch)
			pitch += 45f/(ChemicalDispenser.rotateVTime/20f);
		if(plannedPitch < pitch)
			pitch -= 45f/(ChemicalDispenser.rotateVTime/20f);

		if(Math.abs(plannedPitch-pitch) < (45f/(ChemicalDispenser.rotateVTime/20f)))
			pitch = plannedPitch;
		pitch = MathHelper.clamp(pitch, -45, 45);

		if(plannedYaw > yaw)
			yaw += 45f/(ChemicalDispenser.rotateHTime/20f);
		if(plannedYaw < yaw)
			yaw -= 45f/(ChemicalDispenser.rotateHTime/20f);

		if(Math.abs(plannedYaw-yaw) < (45f/(ChemicalDispenser.rotateHTime/20f)))
			yaw = plannedYaw;
		yaw = MathHelper.clamp(yaw, -45, 45);

		if(hasWorld()&&!world.isRemote&&!refreshWireNetwork)
		{
			refreshWireNetwork = true;
			wireNetwork.removeFromNetwork(null);
		}

		if(world.isBlockPowered(this.getPos()))
			plannedAmount = 20;

		if(!world.isRemote&&plannedAmount > 0&&tank.getFluid()!=null&&plannedYaw==yaw&&plannedPitch==pitch)
		{
			int consumed = IEConfig.Tools.chemthrower_consumption;
			FluidStack fs = tank.getFluid();
			if(energyStorage >= ChemicalDispenser.energyUsage&&Math.min(consumed, plannedAmount) <= fs.amount)
			{
				energyStorage -= ChemicalDispenser.energyUsage;
				Vec3i vi = facing.getOpposite().getDirectionVec();
				Vec3d v = new Vec3d(vi.getX(), vi.getY(), vi.getZ()).rotateYaw((float)Math.toRadians(yaw)*((facing.getAxisDirection()==AxisDirection.POSITIVE)?-1: 1));

				if(facing.getHorizontalIndex()!=-1)
					v = v.addVector(0, Math.toRadians(pitch), 0);
				else
				{
					v = new Vec3d(vi.getX(), vi.getY(), vi.getZ());
					v = v.addVector(Math.toRadians(-pitch), 0, Math.toRadians(yaw));
				}

				int split = 8;
				boolean isGas = fs.getFluid().isGaseous()||ChemthrowerHandler.isGas(fs.getFluid());

				float scatter = isGas?.05f: .025f;
				float range = isGas?1f: 1.25f;

				scatter *= (1f-((float)this.scatter/100f))*3f;
				range *= (((float)this.scatter/100f))*0.5;

				scatter = MathHelper.clamp(scatter, 0.05f, 10f);
				range = MathHelper.clamp(range, 0.25f, 10f);

				for(int i = 0; i < split; i++)
				{
					Vec3d vecDir = v.addVector(Utils.RAND.nextGaussian()*scatter, Utils.RAND.nextGaussian()*scatter, Utils.RAND.nextGaussian()*scatter);
					EntityIIChemthrowerShot chem = new EntityIIChemthrowerShot(world, (float)pos.getX()+0.5f+(v.x/2f), (float)pos.getY()+0.5f+(v.y/2f), (float)pos.getZ()+0.5f+(v.z/2f), vecDir.x*0.5, vecDir.y*0.5, vecDir.z*0.5, fs);

					// Apply momentum from the player.
					chem.motionX = vecDir.x*range;
					chem.motionY = vecDir.y*range;
					chem.motionZ = vecDir.z*range;

					if(shouldIgnite)
						chem.setFire(10);
					world.spawnEntity(chem);
				}
				if(world.getTotalWorldTime()%4==0)
					if(shouldIgnite)
						world.playSound(null, pos.getX()+0.5f, pos.getY()-0.5f, pos.getZ()+0.5f, IESounds.sprayFire, SoundCategory.PLAYERS, .5f, 1.5f);
					else
						world.playSound(null, pos.getX()+0.5f, pos.getY()-0.5f, pos.getZ()+0.5f, IESounds.spray, SoundCategory.PLAYERS, .5f, .75f);
			}
			tank.drain(Math.min(plannedAmount, consumed), true);
			plannedAmount -= consumed;
			plannedAmount = Math.max(plannedAmount, 0);
		}
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
		if(packet.getPacketVariable('p') instanceof NumericDataType)
			this.plannedPitch = MathHelper.clamp(
					packet.getVarInType(NumericDataType.class, packet.getPacketVariable('p')).floatValue(),
					-45, 45);

		if(packet.getPacketVariable('y') instanceof NumericDataType)
			this.plannedYaw = MathHelper.clamp(
					packet.getVarInType(NumericDataType.class, packet.getPacketVariable('y')).floatValue(),
					-45, 45);

		if(packet.getPacketVariable('a') instanceof DataTypeInteger)
			this.plannedAmount = ((DataTypeInteger)packet.getPacketVariable('a')).value;
		if(packet.getPacketVariable('s') instanceof DataTypeInteger)
			this.scatter = MathHelper.clamp(((DataTypeInteger)packet.getPacketVariable('s')).value, 0, 100);
		if(packet.getPacketVariable('i') instanceof DataTypeBoolean)
			this.shouldIgnite = ((DataTypeBoolean)packet.getPacketVariable('i')).value;

		IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT()
				.withFloat("plannedPitch", plannedPitch)
				.withFloat("pitch", pitch)
				.withFloat("plannedYaw", plannedYaw)
				.withFloat("yaw", yaw)
				.withInt("plannedAmount", plannedAmount)
				.withInt("scatter", scatter)
				.withBoolean("shouldIgnite", shouldIgnite)
		));
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
	public EnumFacing getFacing()
	{
		return facing;
	}

	@Override
	public void setFacing(EnumFacing facing)
	{
		this.facing = facing;
	}

	@Override
	public int getFacingLimitation()
	{
		return 0;
	}

	@Override
	public boolean mirrorFacingOnPlacement(EntityLivingBase placer)
	{
		return true;
	}

	@Override
	public boolean canHammerRotate(EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase entity)
	{
		return false;
	}

	@Override
	public boolean canRotate(EnumFacing axis)
	{
		return false;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY&&facing==this.facing)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY&&facing==this.facing)
			return (T)fluidHandler;
		return super.getCapability(capability, facing);
	}

	static class SidedFluidHandler implements IFluidHandler
	{
		TileEntityChemicalDispenser tile;
		EnumFacing facing;

		SidedFluidHandler(TileEntityChemicalDispenser tile, EnumFacing facing)
		{
			this.tile = tile;
			this.facing = facing;
		}

		@Override
		public int fill(FluidStack resource, boolean doFill)
		{
			int i = tile.tank.fill(resource, doFill);
			if(i > 0)
			{
				tile.markDirty();
				tile.markContainingBlockForUpdate(null);
			}
			return i;
		}

		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain)
		{
			return tile.tank.drain(resource.amount, doDrain);
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain)
		{
			return tile.tank.drain(maxDrain, doDrain);
		}

		@Override
		public IFluidTankProperties[] getTankProperties()
		{
			return tile.tank.getTankProperties();
		}
	}

}
