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
	 * Returns the interpolated visual rotation progress.
	 *
	 * @param belt         whether the progress is intended for a connected motor belt
	 * @param partialTicks partial tick time used for interpolation
	 * @return normalized visual rotation progress
	 */
	default float getDisplayedRotationProgress(boolean belt, float partialTicks)
	{
		return 0;
	}

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
