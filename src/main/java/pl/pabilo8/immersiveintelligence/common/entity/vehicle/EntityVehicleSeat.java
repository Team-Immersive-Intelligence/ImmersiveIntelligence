package pl.pabilo8.immersiveintelligence.common.entity.vehicle;

import blusunrize.immersiveengineering.api.tool.ZoomHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;
import pl.pabilo8.immersiveintelligence.client.util.CameraHandler;

import java.util.Optional;

/**
 * @author Pabilo8
 * @since 06.08.2020
 * <p>
 * Just a marker for seats, doesn't do collision but it's an actual world handled entity on all sides
 * Seats are just one-sided and independent on client and server
 * <p>
 * Riding interaction must be handled by the vehicle by an additional part
 */
public class EntityVehicleSeat extends Entity
{
	public int seatID;

	private static final DataParameter<Integer> dataMarkerSeatID = EntityDataManager.createKey(EntityVehicleSeat.class, DataSerializers.VARINT);

	public EntityVehicleSeat(World worldIn)
	{
		super(worldIn);

		//requires size so it's bounding box is visible
		setSize(0.25f, 0.25f);
	}

	@Override
	protected void entityInit()
	{
		dataManager.register(dataMarkerSeatID, 0);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		seatID = compound.getInteger("seatID");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger("seatID", seatID);
	}

	@Override
	protected boolean canFitPassenger(Entity passenger)
	{
		return this.getPassengers().size() < 1;
	}

	@Override
	public boolean canRenderOnFire()
	{
		return false;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}

	@Override
	public void onUpdate()
	{
		if(world.isRemote)
			seatID = dataManager.get(dataMarkerSeatID);
		else
			dataManager.set(dataMarkerSeatID, seatID);
	}

	public void setSeatID(int seatID)
	{
		this.seatID = seatID;
	}

	@Override
	public boolean shouldRiderSit()
	{
		if(getRidingEntity() instanceof IVehicleMultiPart&&this.getPassengers().size() > 0)
			return ((IVehicleMultiPart)getRidingEntity()).shouldSeatPassengerSit(seatID, this.getPassengers().get(0));
		return super.shouldRiderSit();
	}

	@Override
	public void applyOrientationToEntity(Entity passenger)
	{
		if(getRidingEntity() instanceof IVehicleMultiPart)
		{
			((IVehicleMultiPart)getRidingEntity()).getSeatRidingAngle(seatID, passenger);
		}
		else
			super.applyOrientationToEntity(passenger);
	}

	@Override
	public void updatePassenger(Entity passenger)
	{
		if(getRidingEntity() instanceof IVehicleMultiPart)
		{
			((IVehicleMultiPart)getRidingEntity()).getSeatRidingPosition(seatID, passenger);
			((IVehicleMultiPart)getRidingEntity()).getSeatRidingAngle(seatID, passenger);

		}
		else
			super.updatePassenger(passenger);
	}

	/**
	 * @param vehicle that requests the seat
	 * @param id      of the seat
	 */
	public static EntityVehicleSeat getOrCreateSeat(Entity vehicle, int id)
	{
		Optional<Entity> probableSeat = vehicle.getPassengers().stream().filter(entity -> entity instanceof EntityVehicleSeat&&((EntityVehicleSeat)entity).seatID==id).findFirst();
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

	@Override
	public void dismountRidingEntity()
	{

	}

	@Override
	protected void removePassenger(Entity passenger)
	{
		if(world.isRemote&&passenger instanceof EntityPlayerSP)
		{
			CameraHandler.setEnabled(false);
			ZoomHandler.isZooming = false;
		}
		super.removePassenger(passenger);
		if(getRidingEntity() instanceof IVehicleMultiPart)
		{
			((IVehicleMultiPart)getRidingEntity()).onSeatDismount(seatID, passenger);
		}
	}
}
