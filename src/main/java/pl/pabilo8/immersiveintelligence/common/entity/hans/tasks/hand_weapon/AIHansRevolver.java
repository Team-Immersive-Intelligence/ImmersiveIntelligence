package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.hand_weapon;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.items.ItemRevolver;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.AIHansBase;

/**
 * @author Pabilo8
 * @since 23.04.2021
 */
public class AIHansRevolver extends AIHansBase
{
	private EntityLivingBase attackTarget;
	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
	 * maxRangedAttackTime.
	 */
	private int rangedAttackTime;
	private final double entityMoveSpeed;
	private int seeTime;
	/**
	 * The maximum time the AI has to wait before peforming another ranged attack.
	 */
	private final float maxAttackDistance;
	private static final ItemRevolver ITEM_REVOLVER = (ItemRevolver)IEContent.itemRevolver;

	public AIHansRevolver(EntityHans hans, double movespeed, float maxAttackDistanceIn)
	{
		super(hans);
		this.rangedAttackTime = -1;
		this.entityMoveSpeed = movespeed;
		this.maxAttackDistance = maxAttackDistanceIn*maxAttackDistanceIn;
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

		this.attackTarget = entitylivingbase;
		return hans.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() instanceof ItemRevolver&&hasAnyAmmo();
	}

	private boolean hasAnyAmmo()
	{
		final ItemStack smg = hans.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		final ItemStack magazine = ItemNBTHelper.getItemStack(smg, "magazine");
		return hans.hasAmmo||IIContent.itemSubmachinegun.isAmmo(magazine, smg)||!IIContent.itemSubmachinegun.findMagazine(hans, smg).isEmpty();
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
		this.attackTarget = null;
		this.seeTime = 0;
		this.rangedAttackTime = -1;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask()
	{
		if(attackTarget==null||attackTarget.isDead)
		{
			resetTask();
			return;
		}
		double d0 = this.hans.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
		boolean flag = this.hans.getEntitySenses().canSee(this.attackTarget)&&canShootEntity(this.attackTarget);

		if(flag)
			++this.seeTime;
		else
			this.seeTime = 0;

		if(d0 <= (double)this.maxAttackDistance&&this.seeTime >= 20)
			this.hans.getNavigator().clearPath();
		else
			this.hans.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);

		//this.hans.getPositionVector().add(this.hans.getLookVec()).subtract();
		final Vec3d add = this.attackTarget.getPositionVector().add(this.attackTarget.getLookVec());
		this.hans.getLookHelper().setLookPosition(add.x, add.y, add.z, 30, 30);

		if(!flag)
			return;

		if(this.rangedAttackTime < 0)
		{
			hans.resetActiveHand();
			rangedAttackTime += 1;
		}

		final ItemStack smg = hans.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		if(smg.getItem() instanceof ItemRevolver)
		{
			if(ITEM_REVOLVER.getShootCooldown(smg)==0)
			{
				hans.setActiveHand(EnumHand.MAIN_HAND);

				/*
				ItemStack magazine = ItemNBTHelper.getItemStack(smg, "magazine");
				if(!IIContent.itemSubmachinegun.isAmmo(magazine, smg))
				{
					if(!ItemNBTHelper.getBoolean(smg, "shouldReload"))
					{
						final ItemStack ammo = IIContent.itemSubmachinegun.findMagazine(hans, smg);
						hans.hasAmmo = !ammo.isEmpty();
						if(hans.hasAmmo)
							ItemNBTHelper.setBoolean(smg, "shouldReload", true);

					}
				}
				else

				{
					ItemStack s1 =
					hans.hasAmmo = true;
					hans.faceEntity(attackTarget, 30, 0);
					hans.rotationPitch = calculateBallisticAngle(s1, attackTarget);
					ITEM_REVOLVER.onUsingTick(smg, this.hans, this.rangedAttackTime++);
				}
				ITEM_REVOLVER.onUpdate(smg, this.hans.world, this.hans, 0, true);
				 */

			}
		}

	}

	@Override
	public void setRequiredAnimation()
	{

	}

	private float calculateBallisticAngle(ItemStack ammo, EntityLivingBase attackTarget)
	{
		return Utils.getDirectFireAngle(IIContent.itemAmmoRevolver.getDefaultVelocity(), IIContent.itemAmmoRevolver.getMass(ammo), hans.getPositionVector().addVector(0, (double)hans.getEyeHeight()-0.10000000149011612D, 0).subtract(attackTarget.getPositionVector()));
	}
}