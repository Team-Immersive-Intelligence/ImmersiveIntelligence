package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part;

import net.minecraft.util.math.Vec3d;

/**
 * Helper class for storing wheel force calculations.
 * Contains both linear force vector and rotational torque.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 24.10.2025
 */
public class WheelForces
{
	public final Vec3d force;
	public final double torque;
	public final boolean isGrounded;

	public WheelForces(Vec3d force, double torque, boolean isGrounded)
	{
		this.force = force;
		this.torque = torque;
		this.isGrounded = isGrounded;
	}
}
