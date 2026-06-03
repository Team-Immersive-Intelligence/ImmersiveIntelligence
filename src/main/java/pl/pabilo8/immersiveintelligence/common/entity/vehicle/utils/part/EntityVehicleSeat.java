package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part;

import blusunrize.immersiveengineering.api.tool.ZoomHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;
import pl.pabilo8.immersiveintelligence.client.util.CameraHandler;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleControls;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.entity.ISyncNBTEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * Just a marker for seats, doesn't do collision but it's an actual world handled entity on all sides
 * Seats are just one-sided and independent on client and server
 * <p>
 * Riding interaction must be handled by the vehicle by an additional part
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 06.08.2020
 */
public class EntityVehicleSeat extends Entity implements ISyncNBTEntity<EntityVehicleSeat>
{
	@SyncNBT(events = SyncEvents.ENTITY_PASSENGER)
	public String seatID = "";
	public SeatInfo<?> info;

	public EntityVehicleSeat(World worldIn)
	{
		super(worldIn);

		//requires size so it's bounding box is visible
		setSize(0.25f, 0.25f);
	}

	/**
	 * Makes the player attempt to enter the seat
	 *
	 * @param player the player
	 * @param info   seat info
	 * @return true if the player successfully entered the seat
	 */
	public static boolean enterSeat(EntityPlayer player, SeatInfo<?> info)
	{
		EntityVehicleSeat seat = getOrCreateSeat(info);
		if(seat==null)
			return false;
		return player.startRiding(seat);
	}

	/**
	 * Creates or returns an existing vehicle seat
	 *
	 * @param seatInfo Seat info
	 */
	@Nullable
	public static EntityVehicleSeat getOrCreateSeat(SeatInfo<?> seatInfo)
	{
		//Try to find existing seat
		Optional<Entity> probableSeat = seatInfo.vehicle.getPassengers().stream()
				.filter(entity -> entity instanceof EntityVehicleSeat&&((EntityVehicleSeat)entity).seatID.equals(seatInfo.seatID))
				.findFirst();
		if(!probableSeat.isPresent()||!(probableSeat.get() instanceof EntityVehicleSeat))
		{
			//Do not create seats on client side
			if(seatInfo.vehicle.world.isRemote)
				return null;
			//Create a new seat
			EntityVehicleSeat seat = new EntityVehicleSeat(seatInfo.vehicle.world);
			seat.info = seatInfo;
			seat.seatID = seatInfo.seatID;
			seat.setPosition(seatInfo.vehicle.posX, seatInfo.vehicle.posY, seatInfo.vehicle.posZ);
			seatInfo.vehicle.world.spawnEntity(seat);
			seat.startRiding(seatInfo.vehicle);
			seat.updateEntityForEvent(SyncEvents.ENTITY_PASSENGER);
			return seat;
		}
		else
		{
			//Return existing seat
			EntityVehicleSeat seat = (EntityVehicleSeat)probableSeat.get();
			seat.info = seatInfo;
			return seat;
		}
	}

	public static Entity getPassengerOnSeat(SeatInfo<?> seatInfo)
	{
		//Try to find the seat
		Optional<Entity> probableSeat = seatInfo.vehicle.getPassengers().stream()
				.filter(entity -> entity instanceof EntityVehicleSeat&&((EntityVehicleSeat)entity).seatID.equals(seatInfo.seatID))
				.findFirst();
		//Return passenger on seat if present
		if(probableSeat.isPresent())
		{
			List<Entity> passengers = probableSeat.get().getPassengers();
			return passengers.isEmpty()?null: passengers.get(0);
		}
		return null;
	}

	@Override
	protected void entityInit()
	{

	}

	@Override
	public void onUpdate()
	{
		//Update seatInfo when not present
		if(this.info==null&&!seatID.isEmpty()&&this.isRiding())
		{
			Entity vehicle = this.getRidingEntity();
			if(vehicle instanceof IVehicleMultiPart)
			{
				this.info = ((IVehicleMultiPart<?>)vehicle).getSeatInfo(seatID);
				if(!world.isRemote)
					updateEntityForEvent(SyncEvents.ENTITY_PASSENGER);
			}
		}

		if(world.isRemote)
		{
			//Try to find seat info on client
			if(!seatID.isEmpty()&&info==null&&getRidingEntity() instanceof IVehicleMultiPart)
			{
				IVehicleMultiPart<?> vehicle = (IVehicleMultiPart<?>)getRidingEntity();
				this.info = vehicle.getSeatInfo(seatID);
			}
			else if(info!=null)
			{
				//Update Controls
				if(this.info.controls!=null&&this.info.isClientPlayerOnSeat()&&this.info.controls.clientUpdate())
					this.info.vehicle.sendServerUpdateForEvent(SyncEvents.ENTITY_VEHICLE_CONTROLS);
			}
		}
		//Remove entity when data is invalid
		else if(this.ticksExisted > 20&&(!this.isRiding()||seatID.isEmpty()))
			setDead();

		super.onUpdate();
	}

	@Override
	public boolean canRenderOnFire()
	{
		return false;
	}

	@Override
	public boolean shouldRiderSit()
	{
		if(info!=null&&getRidingEntity()==info.vehicle)
			return info.shouldSeatPassengerSit;
		return super.shouldRiderSit();
	}

	@Override
	public void applyOrientationToEntity(@Nonnull Entity passenger)
	{
		Entity riding = getRidingEntity();
		if(info==null||riding!=info.vehicle)
			return;
		EntityVehicleBase<?> vehicle = info.vehicle;

		//Apply yaw angle restrictions
		passenger.setRenderYawOffset(vehicle.rotationYaw+info.yawAngleOffset);
		float f = MathHelper.wrapDegrees(passenger.rotationYaw-(vehicle.rotationYaw+info.yawAngleOffset));
		float f1 = MathHelper.clamp(f, info.minYawAngle, info.maxYawAngle);
		passenger.prevRotationYaw += f1-f;
		passenger.rotationYaw += f1-f;
		passenger.setRotationYawHead(passenger.rotationYaw);

		//Fix player cape
		if(passenger instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)passenger;
			player.prevRenderYawOffset = player.renderYawOffset;
		}
	}

	@Override
	public void updatePassenger(@Nonnull Entity passenger)
	{
		Entity riding = getRidingEntity();
		if(info==null||riding!=info.vehicle)
			return;
		EntityVehicleBase<?> vehicle = info.vehicle;

		//Set position
		Vec3d pos = IIMath.offsetPosDirectionXYZ(info.offset, vehicle.rotationYaw, vehicle.rotationPitch, vehicle.rotationRoll)
				.addVector(vehicle.posX, vehicle.posY, vehicle.posZ);
		passenger.setPosition(pos.x, pos.y, pos.z);
		passenger.motionX = vehicle.motionX;
		passenger.motionY = vehicle.motionY;
		passenger.motionZ = vehicle.motionZ;

		//Set angle
		applyOrientationToEntity(passenger);
	}

	@Override
	public void dismountRidingEntity()
	{
		super.dismountRidingEntity();
	}

	@Override
	protected void removePassenger(@Nonnull Entity passenger)
	{
		//Reset camera system
		if(world.isRemote&&passenger instanceof EntityPlayerSP)
		{
			CameraHandler.setEnabled(false);
			ZoomHandler.isZooming = false;
		}
		super.removePassenger(passenger);
		//Notify vehicle (apply damage if exiting when the vehicle is moving, etc.)
		if(getRidingEntity() instanceof IVehicleMultiPart)
		{
			updatePassenger(passenger);
			((IVehicleMultiPart<?>)getRidingEntity()).onSeatDismount(seatID, passenger);
		}
		updateEntityForEvent(SyncEvents.ENTITY_PASSENGER);
	}

	@Override
	public void readEntityFromNBT(@Nonnull NBTTagCompound compound)
	{
		ISyncNBTEntity.super.readEntityFromNBT(compound);
	}

	@Override
	public void writeEntityToNBT(@Nonnull NBTTagCompound compound)
	{
		ISyncNBTEntity.super.writeEntityToNBT(compound);
	}

	@ParametersAreNonnullByDefault
	public static class SeatInfo<T extends EntityVehicleBase<T>>
	{
		private String seatID;
		private EntityVehicleBase<T> vehicle;
		private boolean shouldSeatPassengerSit = true;
		private Vec3d offset = Vec3d.ZERO;

		private float yawAngleOffset = 0, minYawAngle = -180, maxYawAngle = 180;
		@Nullable
		private VehicleControls controls;

		public SeatInfo(EntityVehicleBase<T> vehicle, String seatID)
		{
			this.seatID = seatID;
			this.vehicle = vehicle;
		}

		//--- Setters ---//

		public SeatInfo<T> withSettings(boolean shouldSeatPassengerSit, Vec3d offset)
		{
			this.shouldSeatPassengerSit = shouldSeatPassengerSit;
			this.offset = offset;
			return this;
		}

		public SeatInfo<T> withYawAngleLimits(float yawAngleOffset, float minYawAngle, float maxYawAngle)
		{
			this.yawAngleOffset = yawAngleOffset;
			this.minYawAngle = minYawAngle;
			this.maxYawAngle = maxYawAngle;
			return this;
		}

		public SeatInfo<T> withControls(VehicleControls controls)
		{
			this.controls = controls;
			return this;
		}

		//--- Getters ---//

		public String getSeatID()
		{
			return seatID;
		}

		public float getYawAngleOffset()
		{
			return yawAngleOffset;
		}

		public float getMinYawAngle()
		{
			return minYawAngle;
		}

		public float getMaxYawAngle()
		{
			return maxYawAngle;
		}

		@Nullable
		public VehicleControls getControls()
		{
			return controls;
		}

		/**
		 * Checks if the client-side player is currently sitting on this seat
		 *
		 * @return true if the player is sitting on the seat
		 */
		@SideOnly(Side.CLIENT)
		public boolean isClientPlayerOnSeat()
		{
			EntityPlayerSP player = ClientUtils.mc().player;
			Entity ridingEntity = player.getRidingEntity();
			if(!(ridingEntity instanceof EntityVehicleSeat))
				return false;
			if(!((EntityVehicleSeat)ridingEntity).seatID.equals(seatID))
				return false;
			return vehicle.getPassengers().contains(ridingEntity);
		}

		@SideOnly(Side.CLIENT)
		public boolean passMouseButtonEvent(MouseEvent event)
		{
			if(controls==null)
				return false;
			controls.passMouseButtonEvent(event);
			return true;
		}
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}
}
