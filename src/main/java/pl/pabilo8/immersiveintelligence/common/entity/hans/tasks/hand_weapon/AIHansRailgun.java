package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.hand_weapon;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansAnimations.HansLegAnimation;
import pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.AIHansBase;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIRailgunOverride;

/**
 * @author Pabilo8
 * @since 23.04.2021
 */
public class AIHansRailgun extends AIHansBase
{
	private EntityLivingBase attackTarget;
	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
	 * maxRangedAttackTime.
	 */
	private int rangedAttackTime;
	private final double entityMoveSpeed;
	/**
	 * Determines for how many ticks an AI has to see its target to attack it
	 */
	private int seeTime;
	/**
	 * The maximum time the AI has to wait before peforming another ranged attack.
	 */
	private final int holdFireTime;
	/**
	 * Snipers lie down instead of kneeling
	 */
	boolean sniper = false;
	/**
	 * The maximum distance the AI will attack enemies from
	 * Will walk towards if necessary.
	 */
	private final float maxAttackDistance;
	/**
	 * The minimum distance the AI will attack enemies from
	 * Will change position if closer.
	 */
	private final float minAttackDistance;
	/**
	 * The distance AI will run if it's too close to an enemy
	 */
	private final float safeAttackDistance;

	public AIHansRailgun(EntityHans hans, double movespeed, int holdFireTime, float minAttackDistanceIn, float maxAttackDistanceIn)
	{
		super(hans);
		this.rangedAttackTime = -1;
		this.entityMoveSpeed = movespeed;
		this.minAttackDistance = minAttackDistanceIn*minAttackDistanceIn;
		this.maxAttackDistance = maxAttackDistanceIn*maxAttackDistanceIn;
		this.safeAttackDistance = (float)MathHelper.clampedLerp(minAttackDistance, maxAttackDistance, 0.35);
		this.holdFireTime = holdFireTime;
		this.setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		EntityLivingBase entitylivingbase = this.hans.getAttackTarget();

		if(entitylivingbase==null)
		{
			return false;
		}
		else
		{
			this.attackTarget = entitylivingbase;
			return hans.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() instanceof ItemIIRailgunOverride&&hasAnyAmmo();
		}
	}

	private boolean hasAnyAmmo()
	{
		return hans.hasAmmo = (!ItemIIRailgunOverride.findAmmo(hans).isEmpty());
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting()
	{
		return this.shouldExecute()||!this.hans.getNavigator().noPath()||!(this.attackTarget==null||hans.getPositionVector().distanceTo(attackTarget.getPositionVector()) < EntityHans.MELEE_DISTANCE);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void resetTask()
	{
		if(attackTarget!=null)
		{
			hans.setSneaking(false);
		}

		this.attackTarget = null;
		this.seeTime = 0;
		this.rangedAttackTime = -1;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask()
	{
		if(attackTarget==null||attackTarget.isDead||!hasAnyAmmo())
		{
			hans.setSneaking(false);
			resetTask();
			return;
		}
		double d0 = this.hans.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
		boolean flag = this.hans.getEntitySenses().canSee(this.attackTarget)&&canShootEntity(this.attackTarget);

		if(flag)
		{
			++this.seeTime;
		}
		else
		{
			this.seeTime = 0;
		}

		if((d0 <= 60||(!hans.getNavigator().noPath()&&d0<=200))&&this.seeTime >= 20)
		{
			if(hans.getNavigator().noPath())
			{
				Vec3d away = RandomPositionGenerator.findRandomTargetBlockAwayFrom(hans, 40, 10, this.attackTarget.getPositionVector());
				if(away!=null)
				{
					this.hans.getNavigator().tryMoveToXYZ(away.x, away.y, away.z, this.entityMoveSpeed*1.125f);
					this.hans.setSprinting(true);

				}
			}
			return;
		}
		else if(d0 <= (double)this.maxAttackDistance&&this.seeTime >= 5)
		{
			this.hans.setSprinting(false);
			this.hans.getNavigator().clearPath();
		}
		else
		{
			this.hans.setSprinting(false);
			this.hans.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
		}

		//this.hans.getPositionVector().add(this.hans.getLookVec()).subtract();
		final Vec3d add = this.attackTarget.getPositionVector().add(this.attackTarget.getLookVec());
		this.hans.getLookHelper().setLookPosition(add.x, add.y, add.z, 30, 30);

		if(!flag)
		{
			return;
		}
		final ItemStack backpack = hans.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		final ItemStack railgun = hans.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		if(EnergyHelper.isFluxItem(backpack)&&EnergyHelper.isFluxItem(railgun))
		{
			//int extracted = EnergyHelper.extractFlux(backpack,4000,false);
			//extracted =
			EnergyHelper.insertFlux(railgun, 99999, false);
			//EnergyHelper.insertFlux(backpack,extracted,false);
		}

		if(this.rangedAttackTime < 0)
		{
			hans.resetActiveHand();
			rangedAttackTime += 1;
		}
		if(this.rangedAttackTime < holdFireTime)
		{
			hans.setActiveHand(EnumHand.MAIN_HAND);
			if(railgun.getItem() instanceof ItemIIRailgunOverride)
			{
				ItemStack ammo = ItemIIRailgunOverride.findAmmo(hans);
				if(ItemIIRailgunOverride.isAmmo(ammo))
				{
					hans.hasAmmo = true;
					hans.faceEntity(attackTarget, 30, 0);
					IEContent.itemRailgun.onUsingTick(railgun, this.hans, this.rangedAttackTime++);
				}
				hans.rotationPitch = calculateBallisticAngle(ammo, attackTarget);
				IEContent.itemRailgun.onUpdate(railgun, this.hans.world, this.hans, 0, true);
				sniper = ((ItemIIRailgunOverride)railgun.getItem()).getUpgrades(railgun).getBoolean("scope");

				if(rangedAttackTime >= holdFireTime)
				{

					IEContent.itemRailgun.onPlayerStoppedUsing(railgun, this.hans.world, this.hans, IEContent.itemRailgun.getMaxItemUseDuration(railgun)-rangedAttackTime);
					rangedAttackTime = -10;
				}
			}
		}
	}

	private float calculateBallisticAngle(ItemStack ammo, EntityLivingBase attackTarget)
	{
		if(ammo.getItem()==IIContent.itemRailgunGrenade)
		{
			return Utils.getDirectFireAngle(IIContent.itemRailgunGrenade.getDefaultVelocity(), IIContent.itemRailgunGrenade.getMass(ammo),
					hans.getPositionVector().addVector(0, hans.getEyeHeight()-0.10000000149011612D, 0).subtract(Utils.getEntityCenter(attackTarget)));
		}
		else
		{
			return Utils.getIEDirectRailgunAngle(ammo, hans.getPositionVector().addVector(0, hans.getEyeHeight(), 0).subtract(Utils.getEntityCenter(attackTarget)));
		}
	}

	@Override
	public void setRequiredAnimation()
	{
		if(hans.getNavigator().noPath())
		{
			if(attackTarget!=null&&!attackTarget.isDead&&seeTime > 10)
				hans.legAnimation = (sniper&&!hans.enemyContact)?HansLegAnimation.LYING: HansLegAnimation.KNEELING;
		}
		else
			hans.legAnimation = HansLegAnimation.STANDING;
	}
}