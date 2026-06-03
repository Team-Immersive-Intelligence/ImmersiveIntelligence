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
		this.aimSpeedYaw = aimSpeedYaw;
		this.aimSpeedPitch = aimSpeedPitch;
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
		this.centerYaw = centerYaw;
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
		this.yawLimitMin = minYawLimit;
		this.yawLimitMax = maxYawLimit;
		//Re‑clamp current and target values to the new limits
		this.targetYaw = clampYawToRange(this.targetYaw);
		this.yaw = clampYawToRange(this.yaw);
		return this;
	}

	public GunAimCoordinate withPitchLimit(float minPitchLimit, float maxPitchLimit)
	{
		this.pitchLimitMin = Math.min(minPitchLimit, maxPitchLimit);
		this.pitchLimitMax = Math.max(minPitchLimit, maxPitchLimit);
		this.targetPitch = MathHelper.clamp(this.pitch, this.pitchLimitMin, this.pitchLimitMax);
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
			this.target = IIMath.offsetPosDirection(1, this.targetYaw, this.targetPitch);
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
		return setTarget(clampYawToRange(targetYaw), clampYawToRange(targetPitch));
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
		yaw = nbt.getFloat("yaw");
		targetPitch = nbt.getFloat("target_pitch");
		targetYaw = nbt.getFloat("target_yaw");
		centerYaw = nbt.getFloat("center_yaw");
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
		if(Math.abs(yaw-targetYaw) <= MINIMAL)
			return targetYaw;

		float step = aimSpeedYaw*partialTicks;
		float newYaw = moveYawTowards(yaw, targetYaw, step);
		//Snap if very close
		if(Math.abs(newYaw-targetYaw) <= MINIMAL)
			newYaw = targetYaw;
		return clampYawToRange(newYaw);
	}

	/**
	 * Moves {@code current} towards {@code target} by at most {@code maxStep} degrees,
	 * staying inside the allowed yaw region defined by {@link #yawLimitMin} and {@link #yawLimitMax}
	 * (relative to {@link #centerYaw}). Handles both normal and wrapped ranges, and always chooses
	 * the direction (shortest or longest) that keeps the movement within limits.
	 *
	 * @param current current absolute yaw (in -180..180)
	 * @param target  target absolute yaw (in -180..180, guaranteed to be within limits)
	 * @param maxStep maximum angular change (positive)
	 * @return new absolute yaw after moving
	 */
	private float moveYawTowards(float current, float target, float maxStep)
	{
		if(current==target||maxStep <= 0)
			return current;
		if(Math.abs(current-target) <= MINIMAL)
			return target;

		//Convert to [0,360) for easier circle arithmetic
		float curr = (current%360+360)%360;
		float targ = (target%360+360)%360;

		//Convert limits to [0,360)
		float a = (yawLimitMin%360+360)%360;
		float b = (yawLimitMax%360+360)%360;

		//Full circle allowed? (forbidden length == 0)
		float forbiddenLen = (a-b+360)%360;
		boolean fullCircle = (forbiddenLen==0);

		float newAngle;
		if(fullCircle)
		{
			//Simple shortest path
			float diff = (targ-curr+360)%360;
			if(diff > 180) diff -= 360; //signed delta
			float move = Math.copySign(Math.min(maxStep, Math.abs(diff)), diff);
			newAngle = curr+move;
		}
		else
		{
			//Map allowed region to a contiguous interval
			float currU = curr < a?curr+360: curr;
			float targU = targ < a?targ+360: targ;

			//Linear movement in unwrapped space
			float diffU = targU-currU;
			float moveU = Math.copySign(Math.min(maxStep, Math.abs(diffU)), diffU);
			float newU = currU+moveU;

			//Map back to [0,360)
			newAngle = newU%360;
			if(newAngle < 0) newAngle += 360;
		}

		//Convert back to -180..180 range
		float result = newAngle;
		if(result > 180) result -= 360;
		return result;
	}

	/**
	 * @return a value in [0,1] representing how far the current relative yaw is
	 * between the yaw limits, linearly interpolated even across the wrap point.
	 * For a wrapped range (min > max), 0 corresponds to {@code yawLimitMin},
	 * 1 corresponds to {@code yawLimitMax} (going through +180).
	 */
	public float getYawNormalized(float partialTicks)
	{
		float relYaw = MathHelper.wrapDegrees(getYaw(partialTicks)-centerYaw);

		if(yawLimitMin <= yawLimitMax) //Normal range
		{
			if(yawLimitMin==yawLimitMax)
				return 0.5f;
			return (relYaw-yawLimitMin)/(yawLimitMax-yawLimitMin);
		}
		else //Wrapped range: map the arc [min, max+360] linearly
		{
			float effectiveMin = yawLimitMin;
			float effectiveMax = yawLimitMax+360;
			float relYaw2 = relYaw;
			if(relYaw2 < yawLimitMin)
				relYaw2 += 360;
			return (relYaw2-effectiveMin)/(effectiveMax-effectiveMin);
		}
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
		return Math.abs(yaw-targetYaw) <= allowedInaccuracy&&Math.abs(pitch-targetPitch) <= allowedInaccuracy;
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
				return distToMin <= distToMin?min: max;
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
		double yawRad = Math.toRadians(MathHelper.wrapDegrees(getYaw(partialTicks)+centerYaw));
		double pitchRad = Math.toRadians(-getPitch(partialTicks));
		return IIMath.offsetPosDirection(1, -yawRad, pitchRad);
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
		 * @return array of pitch and yaw in degrees (absolute)
		 */
		float[] getAnglePrediction(Vec3d shooterPos, Vec3d shooterMotion, Vec3d targetPos, Vec3d targetMotion);
	}

	private static float[] getTargetLead(Vec3d shooterPos, Vec3d shooterMotion, Vec3d targetPos, Vec3d targetMotion)
	{
		Vec3d vv = shooterPos.subtract(targetPos).add(targetMotion).normalize();
		float yy = (float)((Math.atan2(vv.x, vv.z)*180D)/Math.PI);
		float pp = (float)Math.toDegrees(Math.atan2(vv.y, vv.distanceTo(new Vec3d(0, vv.y, 0))));
		return new float[]{yy, pp};
	}
}
