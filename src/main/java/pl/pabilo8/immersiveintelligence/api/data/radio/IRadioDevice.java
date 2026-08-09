package pl.pabilo8.immersiveintelligence.api.data.radio;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;

/**
 * Defines a node that can send, receive, or relay radio packets.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 06.08.2026
 * @ii-approved 0.3.1
 * @since 23.06.2019
 */
public interface IRadioDevice
{
	int MAX_USABILITY_COOLDOWN = 200;

	void onRadioSend(DataPacket packet);

	boolean onRadioReceive(DataPacket packet);

	int getFrequency();

	void setFrequency(int value);

	boolean isBasicRadio();

	float getRange();

	DimensionBlockPos getDevicePosition();

	int getRadioCooldown();

	void setRadioCooldown(int ticks);

	/**
	 * Returns true when this node can participate in radio communication.
	 */
	default boolean isRadioAvailable()
	{
		return getRadioCooldown() <= 0;
	}

	/**
	 * Adds radio downtime without exceeding the common safety limit.
	 */
	default void addRadioCooldown(int ticks)
	{
		if(ticks > 0)
			setRadioCooldown(Math.min(MAX_USABILITY_COOLDOWN, Math.max(0, getRadioCooldown())+ticks));
	}

	/**
	 * Decreases the radio cooldown by one server tick.
	 */
	default void tickRadioCooldown()
	{
		if(getRadioCooldown() > 0)
			setRadioCooldown(getRadioCooldown()-1);
	}
}
