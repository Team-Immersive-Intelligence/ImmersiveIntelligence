package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.hand_weapon;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIRailgunOverride;

import java.util.List;

/**
 * @author Pabilo8
 * @since 23.04.2021
 */
public class AIHansRailgun extends EntityAIBase
{
	/**
	 * The entity (as a RangedAttackMob) the AI instance has been applied to.
	 */
	private final EntityHans hans;
	private EntityLivingBase attackTarget;
	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
	 * maxRangedAttackTime.
	 */
	private int rangedAttackTime;
	private final double entityMoveSpeed;
	private int seeTime;
	private final int burstTime;
	/**
	 * The maximum time the AI has to wait before peforming another ranged attack.
	 */
	private final float maxAttackDistance;

	public AIHansRailgun(EntityHans attacker, double movespeed, int burstTime, float maxAttackDistanceIn)
	{
		this.rangedAttackTime = -1;

		this.hans = attacker;
		this.entityMoveSpeed = movespeed;
		this.maxAttackDistance = maxAttackDistanceIn*maxAttackDistanceIn;
		this.burstTime = burstTime;
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
		return !ItemIIRailgunOverride.findAmmo(hans).isEmpty();
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting()
	{
		return this.shouldExecute()||!this.hans.getNavigator().noPath()||!(this.attackTarget==null||hans.getPositionVector().distanceTo(attackTarget.getPositionVector()) < 1.25f);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void resetTask()
	{
		if(attackTarget!=null)
			hans.setSneaking(false);

		this.attackTarget = null;
		this.seeTime = 0;
		this.rangedAttackTime = -1;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask()
	{
		if(attackTarget==null)
			return;
		if(attackTarget.isDead)
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

		if(d0 <= (double)this.maxAttackDistance&&this.seeTime >= 20)
		{
			this.hans.getNavigator().clearPath();
		}
		else
		{
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
			EnergyHelper.insertFlux(railgun,99999,false);
			//EnergyHelper.insertFlux(backpack,extracted,false);
		}

		if(this.rangedAttackTime < 0)
		{
			hans.resetActiveHand();
			rangedAttackTime += 1;
		}
		if(this.rangedAttackTime < burstTime)
		{
			hans.setActiveHand(EnumHand.MAIN_HAND);
			if(railgun.getItem() instanceof ItemIIRailgunOverride)
			{
				if(ItemIIRailgunOverride.isAmmo(ItemIIRailgunOverride.findAmmo(hans)))
				{
					hans.hasAmmo = true;
					hans.setSneaking(true);
					hans.faceEntity(attackTarget, 30, 0);
					IEContent.itemRailgun.onUsingTick(railgun, this.hans, this.rangedAttackTime++);
				}
				IEContent.itemRailgun.onUpdate(railgun, this.hans.world, this.hans, 0, true);

				if(rangedAttackTime >= burstTime)
				{
					// TODO: 13.05.2021 add balistic calculation
					IEContent.itemRailgun.onPlayerStoppedUsing(railgun, this.hans.world, this.hans,IEContent.itemRailgun.getMaxItemUseDuration(railgun)-rangedAttackTime);
					rangedAttackTime = -10;
				}
			}
		}
	}

	private boolean canShootEntity(EntityLivingBase entity)
	{
		Vec3d start = hans.getPositionEyes(0);
		Vec3d end = entity.getPositionVector();

		//Don't shoot non-targeted entities between the turret and the target
		AxisAlignedBB potentialCollateralArea = entity.getEntityBoundingBox().union(hans.getEntityBoundingBox());
		List<EntityLivingBase> potentialCollateral = hans.world.getEntitiesWithinAABB(EntityLivingBase.class, potentialCollateralArea);
		for(EntityLivingBase coll : potentialCollateral)
		{
			AxisAlignedBB entityBB = coll.getEntityBoundingBox().grow(.125f/2+.4);//Add the range of a revolver bullet in all directions
			if(coll!=hans&&!hans.isValidTarget(coll)&&entityBB.calculateIntercept(start, end)!=null)
				return false;
		}
		return true;
	}
}