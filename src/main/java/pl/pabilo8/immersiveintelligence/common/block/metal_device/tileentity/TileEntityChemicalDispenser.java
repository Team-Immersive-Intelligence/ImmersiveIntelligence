package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.util.IESounds;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.device.DataWireNetwork;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.NumericDataType;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ChemicalDispenser;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityIIChemthrowerShot;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional.FacingLimitation;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional.FacingSettings;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectionalConnectable;
import pl.pabilo8.immersiveintelligence.common.wire.IIDataWireType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * Dispenses aimed chemthrower fluid using data and power wire connections.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 14.08.2026
 * @since 15.07.2019
 */
public class TileEntityChemicalDispenser extends TileEntityIIDirectionalConnectable implements
		ITickable, IBlockBounds, IDataConnector, IAdvancedTextOverlay
{
	private static final FacingSettings FACING_SETTINGS = new FacingSettings(FacingLimitation.SIDE_CLICKED)
			.withMirroringOnPlacement(true);
	private static final Set<String> ACCEPTABLE_POWER_WIRES = ImmutableSet.of(WireType.LV_CATEGORY, WireType.MV_CATEGORY);
	private static final double CONNECTOR_OFFSET = 0.375;
	private static final double CONNECTOR_HIT_RADIUS = 0.125;

	@SyncNBT(events = SyncEvents.TILE_ENERGY_CHANGED)
	public int energyStorage = 0;
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public int plannedAmount = 0, scatter = 0;
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public float pitch = 0, plannedPitch = 0, yaw = 0, plannedYaw = 0;
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public boolean shouldIgnite = false;
	@SyncNBT(events = SyncEvents.TILE_CUSTOM2)
	public FluidTank tank = new FluidTank(12000);
	@SyncNBT(nullable = true)
	public WireType secondCable = null;

	protected DataWireNetwork wireNetwork = new DataWireNetwork().add(this);
	private final SidedFluidHandler fluidHandler = new SidedFluidHandler(this);
	private boolean refreshWireNetwork = false;

	public TileEntityChemicalDispenser()
	{
		facing = EnumFacing.DOWN;
	}

	@Nonnull
	@Override
	protected FacingSettings getFacingSettings()
	{
		return FACING_SETTINGS;
	}

	@Override
	public boolean acceptsWireType(WireType wireType)
	{
		String category = wireType.getCategory();
		return IIDataWireType.DATA_CATEGORY.equals(category)||ACCEPTABLE_POWER_WIRES.contains(category);
	}

	@Override
	public boolean isRelay()
	{
		return false;
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
		if(amount <= 0||energyStorage >= ChemicalDispenser.energyCapacity)
			return 0;

		int received = Math.min(amount, Math.min(ChemicalDispenser.energyCapacity-energyStorage, ChemicalDispenser.energyUsage));
		if(!simulate)
			this.energyStorage += received;
		return received;
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
			return ACCEPTABLE_POWER_WIRES.contains(attachCat)&&secondCable==null;

		return false;
	}

	@Override
	public void connectCable(WireType cableType, TargetingInfo target, IImmersiveConnectable other)
	{
		int connector = getTargetedConnector(target);
		if(connector==0&&limitType==null)
		{
			limitType = cableType;
			DataWireNetwork.updateConnectors(pos, world, wireNetwork);
		}
		else if(connector==1&&secondCable==null)
			secondCable = cableType;
		else
			return;

		markDirty();
		markContainingBlockForUpdate(getWorld().getBlockState(pos));
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
			default:
				return null;
		}
	}

	@Override
	public void removeCable(@Nullable Connection connection)
	{
		WireType type = connection!=null?connection.cableType: null;
		String category = type!=null?type.getCategory(): null;

		if(type==null||IIDataWireType.DATA_CATEGORY.equals(category))
		{
			wireNetwork.removeFromNetwork(this);
			limitType = null;
		}
		if(type==null||ACCEPTABLE_POWER_WIRES.contains(category))
			secondCable = null;

		markDirty();
		markContainingBlockForUpdate(getWorld().getBlockState(pos));
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return getConnectorOffset(con!=null&&IIDataWireType.DATA_CATEGORY.equals(con.cableType.getCategory()));
	}

	@Override
	public Vec3d getConnectionOffset(Connection con, TargetingInfo target, Vec3i offsetLink)
	{
		return getConnectorOffset(getTargetedConnector(target)==0);
	}

	private Vec3d getConnectorOffset(boolean data)
	{
		double multiplier = data?CONNECTOR_OFFSET: -CONNECTOR_OFFSET;
		if(facing.getAxis()!=EnumFacing.Axis.Y)
		{
			Vec3i side = facing.rotateYCCW().getDirectionVec();
			return new Vec3d(
					0.5+side.getX()*multiplier,
					0.5+multiplier,
					0.5+side.getZ()*multiplier
			);
		}

		Vec3i direction = facing.getDirectionVec();
		Vec3d diagonal = new Vec3d(direction.getY(), 0, -1);
		Vec3d result = new Vec3d(0.5, 0.5, 0.5).add(diagonal.scale(multiplier));

		//Keep the connector depth aligned with the existing model for vertical placement.
		if(facing==EnumFacing.DOWN&&data)
			result = result.addVector(0, 0.25, 0);
		else if(!data)
			result = result.addVector(0, 0.025, 0);
		return result;
	}

	public int getTargetedConnector(TargetingInfo target)
	{
		Vec3d hit = new Vec3d(target.hitX, target.hitY, target.hitZ);
		if(isConnectorHit(hit, getConnectorOffset(true)))
			return 0;
		if(isConnectorHit(hit, getConnectorOffset(false)))
			return 1;
		return -1;
	}

	private boolean isConnectorHit(Vec3d hit, Vec3d connector)
	{
		Vec3i normal = facing.getDirectionVec();
		return (normal.getX()!=0||Math.abs(hit.x-connector.x) <= CONNECTOR_HIT_RADIUS)
				&&(normal.getY()!=0||Math.abs(hit.y-connector.y) <= CONNECTOR_HIT_RADIUS)
				&&(normal.getZ()!=0||Math.abs(hit.z-connector.z) <= CONNECTOR_HIT_RADIUS);
	}

	@Nonnull
	@Override
	public float[] getBlockBounds()
	{
		Vec3i direction = facing.getDirectionVec();
		return new float[]{
				getMinBound(direction.getX()), getMinBound(direction.getY()), getMinBound(direction.getZ()),
				getMaxBound(direction.getX()), getMaxBound(direction.getY()), getMaxBound(direction.getZ())
		};
	}

	private static float getMinBound(int direction)
	{
		return direction > 0?0.5f: 0f;
	}

	private static float getMaxBound(int direction)
	{
		return direction < 0?0.5f: 1f;
	}

	@Override
	public void update()
	{
		//Move the nozzle to the commanded angles.
		pitch = approachAngle(pitch, plannedPitch, ChemicalDispenser.rotateVTime);
		yaw = approachAngle(yaw, plannedYaw, ChemicalDispenser.rotateHTime);

		//Refresh the data network once after the tile loads.
		if(hasWorld()&&!world.isRemote&&!refreshWireNetwork)
		{
			refreshWireNetwork = true;
			wireNetwork.removeFromNetwork(null);
		}

		//A redstone signal requests one standard chemthrower operation.
		if(world.isBlockPowered(pos))
			plannedAmount = 20;

		//Spray only on the server and only after the nozzle reaches its target.
		if(world.isRemote||plannedAmount <= 0||tank.getFluid()==null||plannedYaw!=yaw||plannedPitch!=pitch)
			return;

		sprayFluid();
	}

	private static float approachAngle(float current, float target, int rotationTime)
	{
		float step = rotationTime <= 0?Float.MAX_VALUE: 900f/rotationTime;
		return MathHelper.clamp(IIMath.progressValue(current, target, step, 1), -45f, 45f);
	}

	private void sprayFluid()
	{
		FluidStack fluidStack = tank.getFluid();
		if(fluidStack==null||fluidStack.getFluid()==null)
			return;

		int consumed = Math.min(IEConfig.Tools.chemthrower_consumption, plannedAmount);
		if(consumed > fluidStack.amount||energyStorage < ChemicalDispenser.energyUsage)
			return;

		//The scatter command changes the spray from a wide, short cone to a narrow, long stream.
		float focus = MathHelper.clamp(scatter/100f, 0f, 1f);
		boolean gas = fluidStack.getFluid().isGaseous(fluidStack)||ChemthrowerHandler.isGas(fluidStack.getFluid());
		float baseScatter = gas?ChemicalDispenser.sprayScatterGas: ChemicalDispenser.sprayScatterFluid;
		float baseRange = gas?ChemicalDispenser.sprayRangeGas: ChemicalDispenser.sprayRangeFluid;
		float shotScatter = MathHelper.clamp(
				(float)MathHelper.clampedLerp(baseScatter*3f, 0f, focus),
				0.05f, 10f
		);
		float shotRange = MathHelper.clamp(
				(float)MathHelper.clampedLerp(0f, baseRange*0.5f, focus),
				0.25f, 10f
		);

		Vec3d direction = getSprayDirection();
		Vec3d origin = new Vec3d(pos).addVector(0.5, 0.5, 0.5).add(direction.scale(0.5));

		//Create the configured number of chemthrower shots for this operation.
		for(int i = 0; i < ChemicalDispenser.sprayShotsPerTick; i++)
		{
			Vec3d shotDirection = direction.addVector(
					Utils.RAND.nextGaussian()*shotScatter,
					Utils.RAND.nextGaussian()*shotScatter,
					Utils.RAND.nextGaussian()*shotScatter
			);
			EntityIIChemthrowerShot shot = new EntityIIChemthrowerShot(world,
					origin.x, origin.y, origin.z,
					shotDirection.x*0.5, shotDirection.y*0.5, shotDirection.z*0.5,
					fluidStack
			).withMotion(shotDirection.scale(shotRange));

			if(shouldIgnite)
				shot.setFire(10);
			world.spawnEntity(shot);
		}

		//Play the spray loop at the same cadence as the handheld chemthrower.
		if(world.getTotalWorldTime()%4==0)
			world.playSound(null, pos.getX()+0.5f, pos.getY()-0.5f, pos.getZ()+0.5f,
					shouldIgnite?IESounds.sprayFire: IESounds.spray,
					SoundCategory.PLAYERS, 0.5f, shouldIgnite?1.5f: 0.75f);

		energyStorage -= ChemicalDispenser.energyUsage;
		tank.drain(consumed, true);
		plannedAmount -= consumed;
	}

	private Vec3d getSprayDirection()
	{
		EnumFacing output = facing.getOpposite();
		Vec3i direction = output.getDirectionVec();

		if(output.getHorizontalIndex()!=-1)
		{
			float baseYaw = (float)Math.toDegrees(Math.atan2(-direction.getX(), direction.getZ()));
			float yawDirection = facing.getAxisDirection()==AxisDirection.POSITIVE?1f: -1f;
			return IIMath.offsetPosDirectionXZ(1, 0, baseYaw+yaw*yawDirection, pitch);
		}

		//For vertical placement, yaw and pitch are independent tilts on the horizontal plane.
		Vec3d horizontal = IIMath.offsetPosDirectionXZ(
				Math.tan(Math.toRadians(yaw)),
				Math.tan(Math.toRadians(pitch)),
				0, 0
		);
		return new Vec3d(horizontal.x, direction.getY(), horizontal.z).normalize();
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
	public void onPacketReceive(DataPacket packet)
	{
		if(packet.get('p') instanceof NumericDataType)
			this.plannedPitch = MathHelper.clamp(
					packet.getVarInType(NumericDataType.class, packet.get('p')).floatValue(),
					-45, 45);

		if(packet.get('y') instanceof NumericDataType)
			this.plannedYaw = MathHelper.clamp(
					packet.getVarInType(NumericDataType.class, packet.get('y')).floatValue(),
					-45, 45);

		if(packet.get('a') instanceof DataTypeInteger)
			this.plannedAmount = ((DataTypeInteger)packet.get('a')).value;
		if(packet.get('s') instanceof DataTypeInteger)
			this.scatter = MathHelper.clamp(((DataTypeInteger)packet.get('s')).value, 0, 100);
		if(packet.get('i') instanceof DataTypeBoolean)
			this.shouldIgnite = ((DataTypeBoolean)packet.get('i')).value;

		updateTileForEvent(SyncEvents.TILE_CUSTOM1);
	}

	@Override
	public void sendPacket(DataPacket packet)
	{
		//Nope
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY&&facing==this.facing)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY&&facing==this.facing)
			return (T)fluidHandler;
		return super.getCapability(capability, facing);
	}

	private static class SidedFluidHandler implements IFluidHandler
	{
		private final TileEntityChemicalDispenser tile;

		private SidedFluidHandler(TileEntityChemicalDispenser tile)
		{
			this.tile = tile;
		}

		@Override
		public int fill(FluidStack resource, boolean doFill)
		{
			int i = tile.tank.fill(resource, doFill);
			if(doFill&&i > 0)
				tile.updateTileForEvent(SyncEvents.TILE_CUSTOM2);
			return i;
		}

		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain)
		{
			return drain(resource.amount, doDrain);
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain)
		{
			FluidStack drained = tile.tank.drain(maxDrain, doDrain);
			if(doDrain&&drained!=null&&drained.amount > 0)
				tile.updateTileForEvent(SyncEvents.TILE_CUSTOM2);
			return drained;
		}

		@Override
		public IFluidTankProperties[] getTankProperties()
		{
			return tile.tank.getTankProperties();
		}
	}

	//--- IAdvancedTextOverlay ---//

	@SideOnly(Side.CLIENT)
	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		return new String[]{IIUtils.getFluidNameOverlayText(tank.getFluid())};
	}
}
