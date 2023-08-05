package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.hand_weapon;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.AIHansBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Decided to make weapon tasks Hans-only<br>
 * Hans Hand Weapon task is a task used for dealing with ranged item weapons, such as bows, revolvers, railguns, etc.
 *
 * @author Pabilo8
 * @since 04.02.2022
 */
public abstract class AIHansHandWeapon extends AIHansBase
{
	/**
	 * The target currently attacked by the Hans
	 */
	@Nullable
	protected EntityLivingBase attackTarget;
	/**
	 * The maximum distance the Hans will attack enemies from
	 * Will walk towards if necessary.
	 */
	protected final float maxAttackDistance;
	/**
	 * The minimum distance the Hans will attack enemies from
	 * Will change position if closer.
	 */
	protected final float minAttackDistance;
	/**
	 * The distance Hans will run if it's too close to an enemy
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
	/**
	 * The movement speed modifier of the Hans
	 */
	protected final double moveSpeed;
	/**
	 * The current combat motion state
	 */
	@Nonnull
	public MotionState motionState = MotionState.IN_POSITION;
	/**
	 * Determines whether the Hans can fire at the target
	 */
	protected boolean canFire;
	/**
	 * Whether the Hans is looking at the target
	 */
	protected boolean aimingAtTarget;

	protected AIHansHandWeapon(EntityHans hans, float minAttackDistance, float maxAttackDistance, double moveSpeed)
	{
		super(hans);

		this.rangedAttackTime = -1;
		this.moveSpeed = moveSpeed;

		this.minAttackDistance = minAttackDistance;
		this.maxAttackDistance = maxAttackDistance;
		this.safeAttackDistance = (int)MathHelper.clampedLerp(this.minAttackDistance, this.maxAttackDistance, 0.65);

		this.setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		EntityLivingBase entitylivingbase = this.hans.getAttackTarget();

		if(entitylivingbase==null)
			return false;
		else
		{
			this.attackTarget = entitylivingbase;
			this.hans.hasAmmo = hasAnyAmmo();

			return isValidWeapon()&&hans.hasAmmo;
		}
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void resetTask()
	{
		super.resetTask();
		hans.setSneaking(false);

		if(motionState==MotionState.FALLBACK)
			hans.setSneaking(false);

		this.attackTarget = null;
		this.seeTime = 0;
		this.rangedAttackTime = -1;
		canFire = false;
		aimingAtTarget = false;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	@Override
	public final void updateTask()
	{
		super.updateTask();
		this.hans.hasAmmo = hasAnyAmmo();

		MotionState prevMotionState = motionState;
		this.motionState = getMotionState();

		if(motionState==MotionState.FALLBACK&&motionState!=prevMotionState)
		{
			int radius = 15;
			List<EntityHans> hanses = this.hans.world.getEntitiesWithinAABB(EntityHans.class,
					new AxisAlignedBB(new BlockPos(hans.posX, hans.posY, hans.posZ)).grow(radius, radius, radius),
					input -> input!=null&&input!=hans&&input.getTeam()==hans.getTeam());
			for(EntityHans anotherHans : hanses)
			{
				if(anotherHans.getAttackTarget()==null)
					anotherHans.setAttackTarget(this.hans.getAttackTarget());
			}
			hans.playSound(SoundEvents.ENTITY_VILLAGER_YES, 1f, 1f);
		}

		if(this.attackTarget==null||this.attackTarget.isDead||!this.hans.hasAmmo)
		{
			resetTask();
			return;
		}

		if(canFire)
			executeTask();
	}

	/**
	 * Determines the combat motion state for the Hans.<br>
	 * Some weapons can be fired on the move, like the SMG, but others like the Railgun require taking a position (due to charge time).<br>
	 * Handling of the motion state is
	 *
	 * @return the preferred motion state
	 */
	protected MotionState getMotionState()
	{
		//distance, not counting Y
		List<EntityLiving> enemiesWayTooClose = hans.world.getEntitiesWithinAABB(EntityLiving.class, hans.getEntityBoundingBox().grow(minAttackDistance), input -> input.getAttackTarget()==hans);
		EntityLivingBase enemy = enemiesWayTooClose.size() > 0?enemiesWayTooClose.get(0): this.attackTarget;

		//no enemy around
		if(enemy==null)
			return MotionState.IN_POSITION;

		double targetDist = this.hans.getPositionVector().distanceTo(new Vec3d(enemy.posX, enemy.posY, enemy.posZ));
		canFire = this.attackTarget!=null&&
				(!hasToSeeEnemy()||this.hans.getEntitySenses().canSee(this.attackTarget))&&
				canShootEntity(this.attackTarget);

		this.seeTime = canFire?(seeTime+1): 0;

		double d0 = this.attackTarget.posX-this.hans.posX;
		double d2 = this.attackTarget.posZ-this.hans.posZ;
		float f = (float)(MathHelper.atan2(d2, d0)*(180D/Math.PI))-90.0F;
		aimingAtTarget = Math.abs(MathHelper.wrapDegrees(hans.rotationYawHead)-MathHelper.wrapDegrees(f)) < 2;

		if(targetDist <= minAttackDistance||(!hans.getNavigator().noPath()&&targetDist < safeAttackDistance))
		{
			//enemy is dangerously close, run away
			if(hans.getNavigator().noPath())
			{
				Vec3d away = RandomPositionGenerator.findRandomTargetBlockAwayFrom(hans, safeAttackDistance, 0, enemy.getPositionVector());

				if(away!=null)
				{
					List<EntityLiving> enemies = hans.world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(new BlockPos(away)).grow(minAttackDistance), input -> input.getAttackTarget()==hans);
					if(enemies.size()==0)
					{
						float v = enemies.stream().map(EntityLiving::getAIMoveSpeed).max(Float::compareTo).orElse(1f)*1.5f;

						this.hans.getNavigator().tryMoveToXYZ(away.x, away.y, away.z, v);
						this.hans.setSprinting(true);
						resetTask();
						return MotionState.FALLBACK;
					}
				}
			}
			//already in fallback state
			this.hans.setSprinting(true);
			return MotionState.FALLBACK;
		}

		if(targetDist >= minAttackDistance&&targetDist <= maxAttackDistance&&seeTime > 0)
		{
			//enemy approached, fire
			this.hans.setSprinting(false);
			this.hans.getNavigator().clearPath();
			return seeTime > minSeeTime?MotionState.SET_UP: MotionState.IN_POSITION;
		}

		//come towards enemy
		this.hans.setSprinting(false);
		Vec3d towards = RandomPositionGenerator.findRandomTargetBlockTowards(hans, (int)Math.abs(minAttackDistance-targetDist), 10, this.attackTarget.getPositionVector());
		if(towards!=null)
			this.hans.getNavigator().tryMoveToXYZ(towards.x, towards.y, towards.z, this.moveSpeed);
		this.hans.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.moveSpeed);
		return MotionState.COME_TOWARDS;

	}

	/**
	 * The motion state returned by {@link #getMotionState()}
	 */
	protected enum MotionState
	{
		COME_TOWARDS,
		IN_POSITION,
		SET_UP,
		FALLBACK
	}

	protected void lookOnTarget()
	{
		assert attackTarget!=null;
		//final Vec3d add = this.attackTarget.getPositionVector().add(this.attackTarget.getLookVec());
		this.hans.getLookHelper().setLookPositionWithEntity(attackTarget, 40f, hans.getVerticalFaceSpeed());
	}

	protected abstract void executeTask();

	protected abstract float calculateBallisticAngle(ItemStack ammo, EntityLivingBase attackTarget);

	protected ItemStack getWeapon()
	{
		return hans.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
	}

	protected abstract boolean hasAnyAmmo();

	protected abstract boolean isValidWeapon();

	protected boolean hasToSeeEnemy()
	{
		return true;
	}

}
