package pl.pabilo8.immersiveintelligence.common.util.gun;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;

/**
 * Stores yaw and pitch coordinates of a gun and provides utility methods for targetting and rendering.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 16.08.2026
 * @ii-approved 0.3.1
 * @implNote only pitch, yaw, targetPitch, targetYaw and centerYaw are synced to NBT, the rest is for runtime use only and not saved
 * @since 24.02.2026
 */
public class GunAimCoordinate implements INBTSerializable<NBTTagCompound>
{
	private static final float MINIMAL = 0.05f;
	protected float pitch = 0, yaw = 0;
	//Target
	protected float targetPitch = 0, targetYaw = 0;
	protected float centerYaw = 0;

	@Nonnull
	protected AimCorrectionFunction aimCorrectionFunction = GunAimCoordinate::getTargetLead;
	protected Vec3d target = Vec3d.ZERO;
	//Rotation speeds
	protected float aimSpeedPitch = 1, aimSpeedYaw = 1;
	//Angle limits relative to centerYaw
	protected float yawLimitMin = -180, yawLimitMax = 180;
	protected float pitchLimitMin = -90, pitchLimitMax = 90;

	//--- Setters --- //

	public GunAimCoordinate withAimSpeed(float aimSpeedYaw, float aimSpeedPitch)
	{
		this.aimSpeedYaw = Math.max(0, aimSpeedYaw);
		this.aimSpeedPitch = Math.max(0, aimSpeedPitch);
		return this;
	}

	public GunAimCoordinate withAimCorrectionFunction(@Nonnull AimCorrectionFunction aimCorrectionFunction)
	{
		this.aimCorrectionFunction = aimCorrectionFunction;
		return this;
	}

	/**
	 * Sets the current absolute yaw/pitch and also initialises the target to the same values.
	 * The angles are clamped to the yaw limits (relative to {@link #centerYaw}) and the pitch limits.
	 */
	public GunAimCoordinate withCurrentAngles(float yaw, float pitch)
	{
		this.yaw = this.targetYaw = clampYawToRange(yaw);
		this.pitch = this.targetPitch = MathHelper.clamp(pitch, this.pitchLimitMin, this.pitchLimitMax);
		return this;
	}

	/**
	 * Sets the hull (center) yaw. All stored absolute yaw values are re‑clamped to the new center.
	 */
	public GunAimCoordinate withCenterYaw(float centerYaw)
	{
		this.centerYaw = MathHelper.wrapDegrees(centerYaw);
		this.yaw = clampYawToRange(this.yaw);
		this.targetYaw = clampYawToRange(this.targetYaw);
		return this;
	}

	/**
	 * Sets the yaw limits (relative to {@link #centerYaw}).
	 *
	 * @param minYawLimit minimum relative yaw (degrees, in range -180…180)
	 * @param maxYawLimit maximum relative yaw (degrees, in range -180…180)
	 *                    If {@code minYawLimit <= maxYawLimit} the allowed range is the closed interval [min, max].
	 *                    If {@code minYawLimit > maxYawLimit} the allowed range is the complement,
	 *                    i.e. [min, 180) ∪ (-180, max].
	 */
	public GunAimCoordinate withYawLimit(float minYawLimit, float maxYawLimit)
	{
		this.yawLimitMin = MathHelper.clamp(minYawLimit, -180f, 180f);
		this.yawLimitMax = MathHelper.clamp(maxYawLimit, -180f, 180f);
		//Re‑clamp current and target values to the new limits
		this.targetYaw = clampYawToRange(this.targetYaw);
		this.yaw = clampYawToRange(this.yaw);
		return this;
	}

	public GunAimCoordinate withPitchLimit(float minPitchLimit, float maxPitchLimit)
	{
		this.pitchLimitMin = Math.min(minPitchLimit, maxPitchLimit);
		this.pitchLimitMax = Math.max(minPitchLimit, maxPitchLimit);
		this.targetPitch = MathHelper.clamp(this.targetPitch, this.pitchLimitMin, this.pitchLimitMax);
		this.pitch = MathHelper.clamp(this.pitch, this.pitchLimitMin, this.pitchLimitMax);
		return this;
	}

	//--- Targeting --- //

	/**
	 * Sets the target absolute yaw and pitch. The target will be rejected if outside limits.
	 *
	 * @param targetYaw   absolute yaw in degrees
	 * @param targetPitch absolute pitch in degrees
	 * @return whether the yaw (relative to {@link #centerYaw}) is within the yaw limits
	 * and the pitch within its absolute limits.
	 */
	public boolean setTarget(float targetYaw, float targetPitch)
	{
		targetYaw = MathHelper.wrapDegrees(targetYaw);
		targetPitch = MathHelper.wrapDegrees(targetPitch);

		if(isWithinLimits(targetYaw, targetPitch))
		{
			this.targetYaw = targetYaw;
			this.targetPitch = targetPitch;
			this.target = IIMath.offsetPosDirection(1, Math.toRadians(-this.targetYaw), Math.toRadians(-this.targetPitch));
			return true;
		}
		return false;
	}

	/**
	 * Sets the target absolute yaw and pitch, clamping the target to allowed limits
	 *
	 * @param targetYaw   absolute yaw in degrees
	 * @param targetPitch absolute pitch in degrees
	 * @return always true (the target is set to the clamped values)
	 */
	public boolean setTargetClamped(float targetYaw, float targetPitch)
	{
		return setTarget(clampYawToRange(targetYaw), clampPitchToRange(targetPitch));
	}

	public boolean setTarget(Vec3d shooterPos, Vec3d shooterMotion, Vec3d targetPos, Vec3d targetMotion)
	{
		float[] angles = aimCorrectionFunction.getAnglePrediction(shooterPos, shooterMotion, targetPos, targetMotion);
		return setTarget(angles[0], angles[1]);
	}

	/**
	 * Checks if the given absolute yaw/pitch are within limits.
	 * Yaw is considered relative to {@link #centerYaw} and tested against the yaw limits
	 * (normal or wrapped). Pitch uses absolute limits.
	 */
	public boolean isWithinLimits(float yaw, float pitch)
	{
		if(pitch < pitchLimitMin||pitch > pitchLimitMax)
			return false;

		float relYaw = MathHelper.wrapDegrees(yaw-centerYaw);
		return isRelativeYawWithinLimits(relYaw);
	}

	private boolean isRelativeYawWithinLimits(float relYaw)
	{
		if(yawLimitMin <= yawLimitMax) //Normal range
			return relYaw >= yawLimitMin&&relYaw <= yawLimitMax;
		else //Wrapped range: allowed if outside (max, min)
			return relYaw >= yawLimitMin||relYaw <= yawLimitMax;
	}

	//--- Update --- //

	public void update()
	{
		this.yaw = getYaw(1f);
		this.pitch = getPitch(1f);
	}

	//--- NBT --- //

	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT()
				.withFloat("pitch", pitch)
				.withFloat("yaw", yaw)
				.withFloat("target_pitch", targetPitch)
				.withFloat("target_yaw", targetYaw)
				.withFloat("center_yaw", centerYaw)
				.unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		pitch = nbt.getFloat("pitch");
		yaw = MathHelper.wrapDegrees(nbt.getFloat("yaw"));
		targetPitch = nbt.getFloat("target_pitch");
		targetYaw = MathHelper.wrapDegrees(nbt.getFloat("target_yaw"));
		centerYaw = MathHelper.wrapDegrees(nbt.getFloat("center_yaw"));
	}

	//--- Utility methods --- //

	/**
	 * @return absolute yaw (global) at the given partial tick, moving from current towards target
	 * along the allowed angular path (respecting limits and choosing the correct direction).
	 */
	public float getYaw(float partialTicks)
	{
		if(partialTicks==0)
			return yaw;

		float newYaw = moveYawTowards(yaw, targetYaw, aimSpeedYaw*partialTicks);
		if(getYawDistance(newYaw, targetYaw) <= MINIMAL)
			return targetYaw;
		return clampYawToRange(newYaw);
	}

	/**
	 * Moves the absolute yaw towards the target inside the configured relative yaw range.
	 */
	private float moveYawTowards(float current, float target, float maxStep)
	{
		if(maxStep <= 0)
			return current;

		float currentRelative = MathHelper.wrapDegrees(current-centerYaw);
		float targetRelative = MathHelper.wrapDegrees(target-centerYaw);
		float difference;

		if(isFullYawRange())
			difference = MathHelper.wrapDegrees(targetRelative-currentRelative);
		else if(yawLimitMin <= yawLimitMax)
			difference = targetRelative-currentRelative;
		else
		{
			if(currentRelative < yawLimitMin)
				currentRelative += 360f;
			if(targetRelative < yawLimitMin)
				targetRelative += 360f;
			difference = targetRelative-currentRelative;
		}

		float movement = Math.copySign(Math.min(maxStep, Math.abs(difference)), difference);
		return MathHelper.wrapDegrees(centerYaw+currentRelative+movement);
	}

	private float getYawDistance(float current, float target)
	{
		float currentRelative = MathHelper.wrapDegrees(current-centerYaw);
		float targetRelative = MathHelper.wrapDegrees(target-centerYaw);

		if(isFullYawRange())
			return Math.abs(MathHelper.wrapDegrees(targetRelative-currentRelative));
		if(yawLimitMin <= yawLimitMax)
			return Math.abs(targetRelative-currentRelative);

		if(currentRelative < yawLimitMin)
			currentRelative += 360f;
		if(targetRelative < yawLimitMin)
			targetRelative += 360f;
		return Math.abs(targetRelative-currentRelative);
	}

	private boolean isFullYawRange()
	{
		return yawLimitMin <= -180f&&yawLimitMax >= 180f;
	}


	/**
	 * @return a value in [0,1] representing how far the current relative yaw is
	 * between the yaw limits, linearly interpolated even across the wrap point.
	 * For a wrapped range (min > max), 0 corresponds to {@code yawLimitMin},
	 * 1 corresponds to {@code yawLimitMax} (going through +180).
	 */
	public float getYawNormalized(float partialTicks)
	{
		float relYaw = getRelativeYaw(partialTicks);

		if(isFullYawRange())
			return (relYaw+180f)/360f;
		if(yawLimitMin <= yawLimitMax)
		{
			if(yawLimitMin==yawLimitMax)
				return 0.5f;
			return MathHelper.clamp((relYaw-yawLimitMin)/(yawLimitMax-yawLimitMin), 0f, 1f);
		}

		if(relYaw < yawLimitMin)
			relYaw += 360f;
		return MathHelper.clamp((relYaw-yawLimitMin)/(yawLimitMax+360f-yawLimitMin), 0f, 1f);
	}

	/**
	 * @return current yaw relative to the gun centre in the -180 to 180 degree range.
	 */
	public float getRelativeYaw(float partialTicks)
	{
		return MathHelper.wrapDegrees(getYaw(partialTicks)-centerYaw);
	}

	public float getPitch(float partialTicks)
	{
		if(partialTicks==0)
			return pitch;
		if(Math.abs(pitch-targetPitch) <= MINIMAL)
			return targetPitch;
		float speed = Math.min(aimSpeedPitch, Math.abs(pitch-targetPitch));
		float newPitch = pitch+(partialTicks*speed*Math.signum(targetPitch-pitch));
		newPitch = MathHelper.clamp(newPitch, pitchLimitMin, pitchLimitMax);
		if(Math.abs(newPitch-targetPitch) <= MINIMAL)
			newPitch = targetPitch;
		return newPitch;
	}

	public float getPitchNormalized(float partialTicks)
	{
		if(pitchLimitMin==pitchLimitMax)
			return 0.5f;
		return (getPitch(partialTicks)-pitchLimitMin)/(pitchLimitMax-pitchLimitMin);
	}

	public boolean isAimed()
	{
		return isAimed(MINIMAL);
	}

	public boolean isAimed(float allowedInaccuracy)
	{
		return getYawDistance(yaw, targetYaw) <= allowedInaccuracy&&Math.abs(pitch-targetPitch) <= allowedInaccuracy;
	}

	/**
	 * Clamps an absolute yaw value to the absolute yaw range defined by
	 * {@link #centerYaw} and the (possibly wrapped) relative yaw limits.
	 *
	 * @param yaw absolute yaw to clamp
	 * @return clamped absolute yaw (nearest allowed value)
	 */
	public float clampYawToRange(float yaw)
	{
		float relYaw = MathHelper.wrapDegrees(yaw-centerYaw);
		float clampedRel = clampRelativeYaw(relYaw);
		return MathHelper.wrapDegrees(centerYaw+clampedRel);
	}

	private float clampRelativeYaw(float relYaw)
	{
		//Normal range
		if(yawLimitMin <= yawLimitMax)
			return MathHelper.clamp(relYaw, yawLimitMin, yawLimitMax);
			//Wrapped range
		else
		{
			float min = yawLimitMin;
			float max = yawLimitMax;
			//If relYaw is inside forbidden interval (max, min), map to nearest endpoint
			if(relYaw > max&&relYaw < min)
			{
				float distToMin = Math.abs(relYaw-min);
				float distToMax = Math.abs(relYaw-max);
				return distToMin <= distToMax?min: max;
			}
			return relYaw;
		}
	}

	public float clampPitchToRange(float pitch)
	{
		return MathHelper.clamp(pitch, pitchLimitMin, pitchLimitMax);
	}

	public Vec3d getTarget(float partialTicks)
	{
		double yawRad = Math.toRadians(-getYaw(partialTicks));
		double pitchRad = Math.toRadians(-getPitch(partialTicks));
		return IIMath.offsetPosDirection(1, yawRad, pitchRad);
	}

	public float getCenterYaw()
	{
		return centerYaw;
	}

	public float getTargetYaw()
	{
		return targetYaw;
	}

	public float getTargetPitch()
	{
		return targetPitch;
	}

	@FunctionalInterface
	public interface AimCorrectionFunction
	{
		/**
		 * @param shooterPos    position of the shooter/gun
		 * @param shooterMotion motion of the shooter/gun
		 * @param targetPos     position of the target
		 * @param targetMotion  motion of the target
		 * @return array of yaw and pitch in degrees (absolute)
		 */
		float[] getAnglePrediction(Vec3d shooterPos, Vec3d shooterMotion, Vec3d targetPos, Vec3d targetMotion);
	}

	private static float[] getTargetLead(Vec3d shooterPos, Vec3d shooterMotion, Vec3d targetPos, Vec3d targetMotion)
	{
		Vec3d direction = targetPos.subtract(shooterPos).add(targetMotion.subtract(shooterMotion)).normalize();
		float yaw = (float)Math.toDegrees(Math.atan2(-direction.x, direction.z));
		float pitch = (float)-Math.toDegrees(Math.atan2(direction.y, direction.distanceTo(new Vec3d(0, direction.y, 0))));
		return new float[]{yaw, pitch};
	}
}
