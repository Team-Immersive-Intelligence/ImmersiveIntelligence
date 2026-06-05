package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.propulsion;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.IVehicleComponent;
import pl.pabilo8.immersiveintelligence.common.util.entity.SyncedDurability;

import javax.annotation.Nullable;

/**
 * Abstract engine class providing rotations for a {@link VehicleTransmission transmission system}, which then can power a group of {@link pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleWheel wheels}.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 01.10.2025
 */
public abstract class VehicleEngineBase<T extends VehicleEngineBase<T, V>, V extends EntityVehicleBase<V>>
		implements IVehicleComponent, INBTSerializable<NBTTagCompound>, IRotaryEnergy
{
	@Nullable
	protected SyncedDurability durability;
	protected boolean nextState, active;
	protected int activeTicks, activationTicks = 40, animationTicks = 8;
	protected float acceleration = 0f;
	protected RotaryStorage rotaryStorage = new RotaryStorage(0, 0);
	protected V vehicle;

	public VehicleEngineBase(V vehicle)
	{
		this.vehicle = vehicle;
	}

	public boolean start()
	{
		if(active)
			return false;
		return nextState = canBeStarted();
	}

	public boolean stop()
	{
		if(!active)
			return false;
		return nextState = active^canBeStopped();
	}

	public boolean toggle()
	{
		return active?stop(): start();
	}

	/**
	 * Immediately stalls the engine and clears rotary output. Used by semi-automatic gearboxes when an unsafe
	 * downshift shocks the drivetrain. The player must start the engine again afterwards.
	 */
	public void stall()
	{
		this.active = false;
		this.nextState = false;
		this.activeTicks = 0;
		this.acceleration = 0;
		this.rotaryStorage.setTorque(0);
		this.rotaryStorage.setRotationSpeed(0);
	}

	public float getStartingProgress(float partialTicks)
	{
		return MathHelper.clamp((activeTicks+(nextState?partialTicks: -partialTicks))/activationTicks, 0f, 1f);
	}

	protected abstract boolean canBeStarted();

	protected boolean canBeStopped()
	{
		return true;
	}

	public void onUpdate()
	{
		if(!active)
			acceleration = 0;
		activeTicks = MathHelper.clamp(activeTicks+(nextState?1: -1), 0, activationTicks);
		active = activeTicks==activationTicks;
	}

	//--- Setters ---//

	@SuppressWarnings("unchecked")
	public T withDurability(@Nullable SyncedDurability durability)
	{
		this.durability = durability;
		return (T)this;
	}

	public void accelerate(boolean accelerate)
	{
		this.acceleration = MathHelper.clamp(this.acceleration+(accelerate?0.05f: -0.1f), 0f, 1f);
	}

	//--- Getters ---//

	public boolean isActive()
	{
		return active;
	}

	public boolean isStarting()
	{
		return nextState&&!active;
	}

	public int getAnimationTicks()
	{
		return animationTicks;
	}

	public float getAcceleration()
	{
		return acceleration;
	}

	/**
	 * Convenience output for vehicle balancing. A wheel group can use this as a single power-like
	 * value instead of making the vehicle author reason about speed and torque separately.
	 */
	public float getDrivePower()
	{
		return getOutputRotationSpeed()*getOutputTorque();
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
		return RotationSide.OUTPUT;
	}

	//--- IVehicleComponent ---//

	@Nullable
	@Override
	public SyncedDurability getDurability()
	{
		return durability;
	}

	//--- INBTSerializable ---//

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("active", active);
		nbt.setBoolean("nextState", nextState);
		nbt.setInteger("activeTicks", activeTicks);
		nbt.setFloat("acceleration", acceleration);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		active = nbt.getBoolean("active");
		nextState = nbt.getBoolean("nextState");
		activeTicks = nbt.getInteger("activeTicks");
		acceleration = nbt.getFloat("acceleration");
	}
}
