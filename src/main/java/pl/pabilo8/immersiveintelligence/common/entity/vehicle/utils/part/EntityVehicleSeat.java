package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part;

import blusunrize.immersiveengineering.api.tool.ZoomHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;
import pl.pabilo8.immersiveintelligence.client.util.CameraHandler;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.entity.ISyncNBTEntity;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 06.08.2020
 * <p>
 * Just a marker for seats, doesn't do collision but it's an actual world handled entity on all sides
 * Seats are just one-sided and independent on client and server
 * <p>
 * Riding interaction must be handled by the vehicle by an additional part
 */
public class EntityVehicleSeat extends Entity implements ISyncNBTEntity<EntityVehicleSeat>
{
	@SyncNBT(events = SyncEvents.ENTITY_PASSENGER)
	public String seatID = "";

	public EntityVehicleSeat(World worldIn)
	{
		super(worldIn);

		//requires size so it's bounding box is visible
		setSize(0.25f, 0.25f);
	}

	/**
	 * @param vehicle that requests the seat
	 * @param id      of the seat
	 */
	public static EntityVehicleSeat getOrCreateSeat(Entity vehicle, String id)
	{
		Optional<Entity> probableSeat = vehicle.getPassengers().stream()
				.filter(entity -> entity instanceof EntityVehicleSeat&&((EntityVehicleSeat)entity).seatID.equals(id))
				.findFirst();
		if(!probableSeat.isPresent()||!(probableSeat.get() instanceof EntityVehicleSeat))
		{
			EntityVehicleSeat seat = new EntityVehicleSeat(vehicle.world);
			seat.setSeatID(id);
			seat.setPosition(vehicle.posX, vehicle.posY, vehicle.posZ);
			vehicle.world.spawnEntity(seat);
			seat.startRiding(vehicle);
			return seat;
		}
		else
			return (EntityVehicleSeat)probableSeat.get();
	}

	/**
	 * Checks if the player is currently sitting on the specified seat of the specified vehicle
	 *
	 * @param vehicle the vehicle
	 * @param id      the seat id
	 * @return true if the player is sitting on the seat
	 */
	@SideOnly(Side.CLIENT)
	public static boolean isPlayerOnSeat(Entity vehicle, String id)
	{
		EntityPlayerSP player = ClientUtils.mc().player;
		Entity ridingEntity = player.getRidingEntity();
		if(!(ridingEntity instanceof EntityVehicleSeat))
			return false;
		if(!((EntityVehicleSeat)ridingEntity).seatID.equals(id))
			return false;
		return vehicle.getPassengers().contains(ridingEntity);
	}

	@Override
	protected void entityInit()
	{

	}

	@Override
	public boolean canRenderOnFire()
	{
		return false;
	}

	public void setSeatID(String seatID)
	{
		this.seatID = seatID;
		updateEntityForEvent(SyncEvents.ENTITY_PASSENGER);
	}

	@Override
	public boolean shouldRiderSit()
	{
		if(getRidingEntity() instanceof IVehicleMultiPart&&!this.getPassengers().isEmpty())
			return ((IVehicleMultiPart<?>)getRidingEntity()).shouldSeatPassengerSit(seatID, this.getPassengers().get(0));
		return super.shouldRiderSit();
	}

	@Override
	public void applyOrientationToEntity(@Nonnull Entity passenger)
	{
		if(getRidingEntity() instanceof IVehicleMultiPart)
		{
			((IVehicleMultiPart<?>)getRidingEntity()).getSeatRidingAngle(seatID, passenger);
		}
		else
			super.applyOrientationToEntity(passenger);
	}

	@Override
	public void updatePassenger(@Nonnull Entity passenger)
	{
		if(getRidingEntity() instanceof IVehicleMultiPart)
		{
			((IVehicleMultiPart<?>)getRidingEntity()).getSeatRidingPosition(seatID, passenger);
			((IVehicleMultiPart<?>)getRidingEntity()).getSeatRidingAngle(seatID, passenger);

		}
		else
			super.updatePassenger(passenger);
	}

	@Override
	public void dismountRidingEntity()
	{

	}

	@Override
	protected void removePassenger(@Nonnull Entity passenger)
	{
		if(world.isRemote&&passenger instanceof EntityPlayerSP)
		{
			CameraHandler.setEnabled(false);
			ZoomHandler.isZooming = false;
		}
		super.removePassenger(passenger);
		if(getRidingEntity() instanceof IVehicleMultiPart)
			((IVehicleMultiPart<?>)getRidingEntity()).onSeatDismount(seatID, passenger);
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
}
