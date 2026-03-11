package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 16.11.2025
 */
public class VerticalForces
{
	public final boolean canClimb;
	public final double climbHeight;
	public final Vec3d obstacleNormal;
	public final AxisAlignedBB climbedBox;
	public final double gravityForce;
	public final double climbForce;
	public final double verticalSum;
	public final boolean isGrounded;

	public VerticalForces(boolean canClimb, double climbHeight,
						  Vec3d obstacleNormal, AxisAlignedBB climbedBox,
						  double gravityForce, double climbForce, double verticalSum, boolean isGrounded)
	{
		this.canClimb = canClimb;
		this.climbHeight = climbHeight;
		this.obstacleNormal = obstacleNormal;
		this.climbedBox = climbedBox;
		this.gravityForce = gravityForce;
		this.climbForce = climbForce;
		this.verticalSum = verticalSum;
		this.isGrounded = isGrounded;
	}
}
