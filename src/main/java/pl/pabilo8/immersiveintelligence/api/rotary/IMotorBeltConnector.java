package pl.pabilo8.immersiveintelligence.api.rotary;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * @author Pabilo8
 * @since 26-12-2019
 */
public interface IMotorBeltConnector
{
	/**
	 * Sets the MotorBeltNetwork this connector is connected to.
	 *
	 * @param net the new network for this connector.
	 */
	void setNetwork(MotorBeltNetwork net);

	/**
	 * @return the MotorBeltNetwork this connector is connected to.
	 */
	MotorBeltNetwork getNetwork();

	/**
	 * Called whenever the DataWireNetwork is changed in some way (both adding/removing connectors and changes in RPM value).
	 */
	void onChange();

	/**
	 * @return the world that this connector is in
	 */
	World getConnectorWorld();

	/**
	 * @return RPM speed/power the connector is outputting
	 */
	double getOutputRPM();

	/**
	 * @return the rotational energy storage object
	 */
	RotaryStorage getRotaryStorage();

	//Axis around which the connector (wheel) rotates
	EnumFacing.Axis getConnectionAxis();

	//Radius of the conveyor in 1/16 of a block
	float getRadius();
}
