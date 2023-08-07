package pl.pabilo8.immersiveintelligence.common.entity.vehicle.drone;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityDrone;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 19.12.2022
 */
public abstract class AIDroneWeapon extends AIDroneBase
{
	/**
	 * The target currently attacked by the Hans
	 */
	@Nullable
	protected EntityLivingBase attackTarget;
	/**
	 * The maximum distance the drone will attack enemies from
	 * Will walk towards if necessary.
	 */
	protected final float maxAttackDistance;
	/**
	 * The minimum distance the drone will attack enemies from
	 * Will change position if closer.
	 */
	protected final float minAttackDistance;
	/**
	 * The distance drone will maintain when attacking an enemy
	 */
	protected final int safeAttackDistance;
	/**
	 * Determines for how many ticks an AI has to see its target to attack it
	 */
	protected int minSeeTime;
	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
	 * maxRangedAttackTime.
	 */
	protected int rangedAttackTime;
	/**
	 * Determines for how many ticks an AI has seen its target
	 */
	protected int seeTime;


	protected AIDroneWeapon(EntityDrone drone, float minAttackDistance, float maxAttackDistance, double moveSpeed)
	{
		super(drone);

		this.rangedAttackTime = -1;

		this.minAttackDistance = minAttackDistance;
		this.maxAttackDistance = maxAttackDistance;
		this.safeAttackDistance = (int)MathHelper.clampedLerp(this.minAttackDistance, this.maxAttackDistance, 0.65);

		this.setMutexBits(3);
	}


	@Override
	public boolean shouldExecute()
	{
		return false;
	}

	public enum DroneWeaponType
	{
		GUN,
		ROCKET,
		BOMB
	}
}
