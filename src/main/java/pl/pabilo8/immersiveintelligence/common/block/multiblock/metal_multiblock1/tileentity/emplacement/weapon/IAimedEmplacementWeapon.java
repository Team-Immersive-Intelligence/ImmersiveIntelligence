package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;


import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 31.12.2025
 */
public interface IAimedEmplacementWeapon
{
	float getYaw();

	float getPitch();

	/**
	 * @param yaw   destination
	 * @param pitch destination
	 * @return whether the gun is pointing to the pitch and yaw given
	 */
	default boolean isAimedAt(float yaw, float pitch)
	{
		return pitch==getPitch()&&MathHelper.wrapDegrees(yaw)==MathHelper.wrapDegrees(getYaw());
	}

	/**
	 * Calculates final aiming angle of the weapon
	 *
	 * @param posTurret of the emplacement
	 * @param posTarget to be attacked
	 * @param motion    for moving entities, {@link Vec3d#ZERO} for other cases
	 * @return the final aiming angle
	 */
	default float[] getAnglePrediction(Vec3d posTurret, Vec3d posTarget, Vec3d motion)
	{
		Vec3d vv = posTurret.subtract(posTarget).add(motion).normalize();
		float yy = (float)((Math.atan2(vv.x, vv.z)*180D)/3.1415927410125732D);
		float pp = (float)Math.toDegrees((Math.atan2(vv.y, vv.distanceTo(new Vec3d(0, vv.y, 0)))));
		return new float[]{yy, pp};
	}
}
