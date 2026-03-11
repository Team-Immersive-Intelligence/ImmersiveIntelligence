package pl.pabilo8.immersiveintelligence.api.rotary;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

/**
 * An interface for an Immersive Intelligence rotary powered device. Uses a torque (IT - Immersive Torque) and speed (D/T - Degrees per Tick) value to represent ongoing rotations.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 10.10.2025
 * @since 06.01.2020
 */
public interface IRotaryEnergy
{
	/**
	 * @return Torque in IT
	 */
	float getTorque();

	/**
	 * @param torque in IT
	 */
	void setTorque(float torque);

	/**
	 * Used by connectors, as they need to have local and network energy
	 *
	 * @return output torque in IT
	 */
	default float getOutputTorque()
	{
		return getTorque();
	}

	/**
	 * @return Degrees per tick
	 */
	float getRotationSpeed();

	/**
	 * Sets degrees per tick value
	 *
	 * @param speed degrees per tick
	 */
	void setRotationSpeed(float speed);

	/**
	 * Used by connectors, as they need to have local and network energy
	 *
	 * @return output speed
	 */
	default float getOutputRotationSpeed()
	{
		return getRotationSpeed();
	}

	/**
	 * Energy in Immersive Flux / Tick (1 IF/tick = 0.00134 MHp = 0.25 EU/tick = 0.36 RU)
	 * In Gregtech: 1 RU -> 0.6875 EU, the conversion RoF to IF to EU to RU gives result of 0.36,
	 *
	 * @return the amount of energy in RoF
	 */
	default float getEnergy()
	{
		return getTorque()*getRotationSpeed();
	}

	/**
	 * @param facing of the TileEntity, null when not important
	 * @return side type
	 */
	RotationSide getSide(@Nullable EnumFacing facing);


	/**
	 * D/t and Torque cannot exceed 0.98f of the other's speed
	 *
	 * @param other   rotary device's energy storage
	 * @param percent of growth
	 */
	default boolean grow(IRotaryEnergy other, float percent)
	{
		return grow(other.getOutputRotationSpeed(), other.getOutputTorque(), percent);
	}

	default boolean grow(float other_speed, float other_torque, float percent)
	{
		float t = this.getTorque(), r = this.getRotationSpeed();
		float minT = Math.min(other_torque, t), maxT = Math.max(other_torque, t);
		float minR = Math.min(other_speed, r), maxR = Math.max(other_speed, r);

		this.setTorque(Math.min(minT+Math.max(percent*maxT, 0.1f), maxT));
		this.setRotationSpeed(Math.min(minR+Math.max(percent*maxR, 0.1f), maxR));

		return getTorque()!=t||getRotationSpeed()!=r;
	}

	/**
	 * @param other  rotational energy storage
	 * @param facing of the other storage being accessed
	 * @return whether the D/t/torque has changed
	 */
	default boolean handleRotation(IRotaryEnergy other, EnumFacing facing)
	{
		if(other.getSide(facing).canOutput)
			return grow(other, 0.01f);
		return false;
	}

	default NBTTagCompound toNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat("speed", getRotationSpeed());
		nbt.setFloat("torque", getTorque());
		return nbt;
	}

	default void fromNBT(NBTTagCompound nbt)
	{
		setRotationSpeed(nbt.getFloat("speed"));
		setTorque(nbt.getFloat("torque"));
	}

	enum RotationSide
	{
		INPUT(true, false),
		OUTPUT(false, true),
		BOTH(true, true),
		NONE(false, false);

		private final boolean canInput;

		private final boolean canOutput;

		RotationSide(boolean canInput, boolean canOutput)
		{
			this.canInput = canInput;
			this.canOutput = canOutput;
		}

		public boolean canInput()
		{
			return canInput;
		}

		public boolean canOutput()
		{
			return canOutput;
		}
	}
}
