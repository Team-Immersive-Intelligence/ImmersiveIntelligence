package pl.pabilo8.immersiveintelligence.api.rotary;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 10.10.2025
 * @ii-approved 0.3.1
 * @since 26.12.2019
 */
public interface IRotaryConnector
{
	/**
	 * @return the MotorBeltNetwork this rotary connector is connected to.
	 */
	@Nonnull
	MotorBeltNetwork getNetwork();

	/**
	 * Sets the MotorBeltNetwork this rotary connector is connected to.
	 *
	 * @param net the new network for this rotary connector.
	 */
	void setNetwork(@Nonnull MotorBeltNetwork net);

	/**
	 * Called whenever the {@link MotorBeltNetwork} is changed in some way (both adding/removing connectors and changes in speed or torque value).
	 */
	void onChange();

	/**
	 * @return the world that this rotary connector is in
	 */
	World getConnectorWorld();

	/**
	 * @return D/t speed the rotary connector is outputting
	 */
	double getOutputSpeed();

	/**
	 * @return the rotational energy storage object
	 */
	RotaryStorage getRotaryStorage();

	/**
	 * @return the axis this rotary connector connects along
	 */
	EnumFacing.Axis getConnectionAxis();

	/**
	 * @return rotary connector wheel radius
	 */
	float getRadius();
}
