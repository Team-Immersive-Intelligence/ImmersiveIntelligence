package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.propulsion;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleDurability;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.IVehicleComponent;

import javax.annotation.Nullable;

/**
 * Abstract engine class providing rotations for a {@link VehicleTransmission transmission system}, which then can power a group of {@link pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleWheel wheels}.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 01.10.2025
 */
public abstract class VehicleEngineBase<T extends VehicleEngineBase<T>> implements IVehicleComponent, INBTSerializable<NBTTagCompound>, IRotaryEnergy
{
	@Nullable
	protected VehicleDurability durability;
	protected boolean active;
	protected float acceleration = 0f;
	protected RotaryStorage rotaryStorage = new RotaryStorage(0, 0);

	public boolean start()
	{
		if(active)
			return false;
		return active = canBeStarted();
	}

	public boolean stop()
	{
		if(!active)
			return false;
		return active = canBeStopped();
	}

	protected abstract boolean canBeStarted();

	protected boolean canBeStopped()
	{
		return true;
	}

	public abstract void onUpdate();

	//--- Setters ---//

	@SuppressWarnings("unchecked")
	public T withDurability(@Nullable VehicleDurability durability)
	{
		this.durability = durability;
		return (T)this;
	}

	//--- Getters ---//

	public boolean isActive()
	{
		return active;
	}

	public float getAcceleration()
	{
		return acceleration;
	}

	@Override
	public float getTorque()
	{
		return rotaryStorage.getTorque();
	}

	@Override
	public void setTorque(float torque)
	{
		rotaryStorage.setTorque(torque);
	}

	@Override
	public float getRotationSpeed()
	{
		return rotaryStorage.getRotationSpeed();
	}

	@Override
	public void setRotationSpeed(float speed)
	{
		rotaryStorage.setRotationSpeed(speed);
	}

	@Override
	public RotationSide getSide(@Nullable EnumFacing facing)
	{
		return null;
	}

	//--- IVehicleComponent ---//

	@Nullable
	@Override
	public VehicleDurability getDurability()
	{
		return durability;
	}

	//--- INBTSerializable ---//

	@Override
	public NBTTagCompound serializeNBT()
	{
		return new NBTTagCompound();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{

	}
}
