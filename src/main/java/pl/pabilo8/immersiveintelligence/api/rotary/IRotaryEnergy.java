package pl.pabilo8.immersiveintelligence.api.rotary;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

/**
 * Created by Pabilo8 on 06-01-2020.
 * It's definitely more advanced than the forge energy system, but it reflects at least a bit how it works irl
 * Used multiple sources to make this:
 * -Used physics equations and general info about how rotary energy works from https://www.engineeringtoolbox.com/
 * -Many ideas and implementations from RotaryCraft by Reika Kalseki https://sites.google.com/site/reikasminecraft/rotarycraft
 * -and RotaryCraft Wiki https://rotarycraft.fandom.com/wiki/RotaryCraft_Wiki
 * -General info from Wikipedia
 */
public interface IRotaryEnergy
{
	/**
	 * @return Torque in Newton metres
	 */
	float getTorque();

	/**
	 * @param torque in Newton metres
	 */
	void setTorque(float torque);

	/**
	 * Used by connectors, as they need to have local and network energy
	 *
	 * @return output torque
	 */
	default float getOutputTorque()
	{
		return getTorque();
	}

	/**
	 * @return Revelations per minute
	 */
	float getRotationSpeed();

	/**
	 * Sets revelations per minute value
	 *
	 * @param rpm revelations per minute
	 */
	void setRotationSpeed(float rpm);

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
	 * Energy in Rotary Flux (1 RoF = 1 IF/tick = 0.00134 MHp = 0.25 EU/tick = 0.36 RU)
	 * Energy can also be measured in Minecraft-Horse Power (746 RoF = 1 MHp)
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
	 * Checks if RMP is equal, so the torque can be merged
	 *
	 * @param other device with which you want to merge torque
	 * @return RPM is equal
	 */
	default boolean canBeMerged(IRotaryEnergy other)
	{
		return other.getOutputRotationSpeed()==getRotationSpeed();
	}

	/**
	 * A harsh system, which will force player to maintain the machines
	 *
	 * @param other device with which you want to merge torque
	 * @return Summed torque if RPM is equal or lowest of the torques
	 */
	default float getCombinedTorque(IRotaryEnergy other)
	{
		return canBeMerged(other)?(getTorque()+other.getOutputTorque()): (Math.min(getTorque(), other.getOutputTorque()));
	}

	/**
	 * RPM and Torque cannot exceed 0.98f of the other's speed
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
	 * @return whether the rpm/torque has changed
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
