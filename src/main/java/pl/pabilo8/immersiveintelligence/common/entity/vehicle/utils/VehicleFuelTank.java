package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils;

import blusunrize.immersiveengineering.api.energy.DieselHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.IVehicleComponent;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.entity.SyncedDurability;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 01.10.2025
 */
public class VehicleFuelTank<T extends EntityVehicleBase<T>> implements IFluidHandler, INBTSerializable<NBTTagCompound>, IVehicleComponent
{
	private SyncedDurability durability;
	private final T vehicle;
	private final FluidTank tank;

	public VehicleFuelTank(T vehicle, int capacity)
	{
		this.vehicle = vehicle;
		this.tank = new FluidTank(capacity);
	}

	public VehicleFuelTank<T> withDurability(SyncedDurability durability)
	{
		this.durability = durability;
		return this;
	}

	public FluidStack getFluid()
	{
		return tank.getFluid();
	}

	public float getFuelPercentage()
	{
		return (tank.getFluidAmount()/(float)tank.getCapacity());
	}

	@Override
	public void onUpdate()
	{
		//Leak fuel if the tank is destroyed
		if(durability!=null&&durability.isDead())
			this.tank.drain(4, true);
	}

	@Override
	public int fill(FluidStack resource, boolean doFill)
	{
		if(resource==null)
			return 0;
		if(!DieselHandler.isValidFuel(resource.getFluid()))
			return 0;

		int i = tank.fill(resource, doFill);
		if(i > 0)
			vehicle.updateEntityForEvent(SyncEvents.ENTITY_VEHICLE_FUEL);
		return i;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain)
	{
		if(resource==null)
			return null;
		return this.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		FluidStack f = tank.drain(maxDrain, doDrain);
		if(f!=null&&f.amount > 0)
			vehicle.updateEntityForEvent(SyncEvents.ENTITY_VEHICLE_FUEL);
		return f;
	}

	@Override
	public IFluidTankProperties[] getTankProperties()
	{
		return tank.getTankProperties();
	}

	//--- INBTSerializable ---//

	@Override
	public NBTTagCompound serializeNBT()
	{
		return tank.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		tank.readFromNBT(nbt);
	}

	//--- IVehicleComponent ---//

	@Nullable
	@Override
	public SyncedDurability getDurability()
	{
		return null;
	}
}
