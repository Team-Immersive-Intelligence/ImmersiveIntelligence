package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Stores yaw and pitch coordinates of a gun and provides utility methods for targetting and rendering.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @implNote only pitch, yaw, targetPitch and targetYaw are synced to NBT, the rest is for runtime use only and not saved
 * @since 24.02.2026
 */
public class GunAimCoordinate implements INBTSerializable<NBTTagCompound>
{
	//Current angles
	protected float pitch = 0, yaw = 0;
	//Target
	protected float targetPitch = 0, targetYaw = 0;
	@Nonnull
	protected AimCorrectionFunction aimCorrectionFunction = GunAimCoordinate::getDefaultAnglePrediction;
	protected Vec3d target = Vec3d.ZERO;
	//Rotation speeds
	protected float aimSpeedPitch = 1, aimSpeedYaw = 1;
	//Angle limits
	protected float yawLimitMin = -180, yawLimitMax = 180;
	protected float pitchLimitMin = -90, pitchLimitMax = 90;

	//--- Setters ---//

	public GunAimCoordinate withAimSpeed(float aimSpeedYaw, float aimSpeedPitch)
	{
		this.aimSpeedYaw = aimSpeedYaw;
		this.aimSpeedPitch = aimSpeedPitch;
		return this;
	}

	public GunAimCoordinate withAimCorrectionFunction(@Nullable AimCorrectionFunction aimCorrectionFunction)
	{
		this.aimCorrectionFunction = aimCorrectionFunction;
		return this;
	}

	public GunAimCoordinate withCurrentAngles(float yaw, float pitch)
	{
		this.yaw = yaw;
		this.pitch = pitch;
		return this;
	}

	public GunAimCoordinate withYawLimit(float minYawLimit, float maxYawLimit)
	{
		this.yawLimitMin = minYawLimit;
		this.yawLimitMax = maxYawLimit;
		return this;
	}

	public GunAimCoordinate withPitchLimit(float minPitchLimit, float maxPitchLimit)
	{
		this.pitchLimitMin = minPitchLimit;
		this.pitchLimitMax = maxPitchLimit;
		return this;
	}

	//--- Targetting ---//

	/**
	 * Sets the target angles
	 *
	 * @param targetYaw   in degrees
	 * @param targetPitch in degrees
	 * @return whether the angle is within the limits
	 */
	public boolean setTarget(float targetYaw, float targetPitch)
	{
		//Normalize
		targetYaw = MathHelper.wrapDegrees(targetYaw);
		targetPitch = MathHelper.wrapDegrees(targetPitch);
		//Check and only set if within the limits
		if(isWithinLimits(targetYaw, targetPitch))
		{
			this.targetYaw = targetYaw;
			this.targetPitch = targetPitch;
			this.target = IIMath.offsetPosDirection(1, this.targetYaw, this.targetPitch);
		}
		return false;
	}

	public boolean setTarget(Vec3d shooterPos, Vec3d shooterMotion, Vec3d targetPos, Vec3d targetMotion)
	{
		float[] angles = aimCorrectionFunction.getAnglePrediction(shooterPos, shooterMotion, targetPos, targetMotion);
		return setTarget(angles[0], angles[1]);
	}

	/**
	 * @param yaw   in degrees
	 * @param pitch in degrees
	 * @return if the given angles are within the limits
	 */
	public boolean isWithinLimits(float yaw, float pitch)
	{
		return yaw >= yawLimitMin&&yaw <= yawLimitMax&&pitch >= pitchLimitMin&&pitch <= pitchLimitMax;
	}

	//--- Update ---//

	public void update()
	{
		//Lerp towards the target angles
		this.yaw = getPitch(1f);
		this.pitch = getYaw(1f);
	}

	//--- NBT ---//

	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT()
				.withFloat("pitch", pitch)
				.withFloat("yaw", yaw)
				.withFloat("targetPitch", targetPitch)
				.withFloat("targetYaw", targetYaw)
				.unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		pitch = nbt.getFloat("pitch");
		yaw = nbt.getFloat("yaw");
		targetPitch = nbt.getFloat("targetPitch");
		targetYaw = nbt.getFloat("targetYaw");
	}

	//--- Utils ---//

	public float getYaw(float partialTicks)
	{
		if(partialTicks==0)
			return yaw;
		if(yaw==targetYaw)
			return yaw;
		return MathHelper.clamp(yaw+(partialTicks*aimSpeedYaw*Math.signum(yaw-targetYaw)), yawLimitMin, yawLimitMax);
	}

	public float getYawNormalized(float partialTicks)
	{
		if(yawLimitMin==yawLimitMax)
			return 0.5f;
		return (getYaw(partialTicks)-yawLimitMin)/(yawLimitMax-yawLimitMin);
	}

	public float getPitch(float partialTicks)
	{
		if(partialTicks==0)
			return pitch;
		if(pitch==targetPitch)
			return pitch;
		return MathHelper.clamp(pitch+(partialTicks*aimSpeedPitch*Math.signum(pitch-targetPitch)), pitchLimitMin, pitchLimitMax);
	}

	public float getPitchNormalized(float partialTicks)
	{
		if(pitchLimitMin==pitchLimitMax)
			return 0.5f;
		return (getPitch(partialTicks)-pitchLimitMin)/(pitchLimitMax-pitchLimitMin);
	}

	public boolean isAimed()
	{
		return yaw==targetYaw&&pitch==targetPitch;
	}

	public boolean isAimed(float allowedInaccuracy)
	{
		return Math.abs(MathHelper.wrapDegrees(yaw-targetYaw)) <= allowedInaccuracy
				&&Math.abs(MathHelper.wrapDegrees(pitch-targetPitch)) <= allowedInaccuracy;
	}

	@FunctionalInterface
	public interface AimCorrectionFunction
	{
		/**
		 * @param shooterPos    position of the shooter/gun
		 * @param shooterMotion motion of the shooter/gun
		 * @param targetPos     position of the target
		 * @param targetMotion  motion of the target
		 * @return array of pitch and yaw in degrees
		 */
		float[] getAnglePrediction(Vec3d shooterPos, Vec3d shooterMotion, Vec3d targetPos, Vec3d targetMotion);
	}

	private static float[] getDefaultAnglePrediction(Vec3d shooterPos, Vec3d shooterMotion, Vec3d targetPos, Vec3d targetMotion)
	{
		Vec3d vv = shooterPos.subtract(targetPos).add(targetMotion).normalize();
		float yy = (float)((Math.atan2(vv.x, vv.z)*180D)/3.1415927410125732D);
		float pp = (float)Math.toDegrees((Math.atan2(vv.y, vv.distanceTo(new Vec3d(0, vv.y, 0)))));
		return new float[]{yy, pp};
	}
}
