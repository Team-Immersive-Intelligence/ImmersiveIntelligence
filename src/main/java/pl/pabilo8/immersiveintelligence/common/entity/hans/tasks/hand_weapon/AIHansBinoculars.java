package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.hand_weapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansAnimations.HansArmAnimation;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansAnimations.HansLegAnimation;
import pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.idle.AIHansSalute;

/**
 * @author Pabilo8
 * @since 06.02.2022
 */
public class AIHansBinoculars extends AIHansHandWeapon
{

	public AIHansBinoculars(EntityHans hans)
	{
		super(hans, 10, 90, 1.15f);
		this.setMutexBits(3);
	}

	@Override
	protected boolean hasAnyAmmo()
	{
		return true;
	}

	@Override
	protected boolean isValidWeapon()
	{
		return getWeapon().getItem()==IIContent.itemBinoculars;
	}

	@Override
	public void executeTask()
	{
		assert attackTarget!=null;

		if(motionState==MotionState.FALLBACK||motionState==MotionState.COME_TOWARDS)
			return;

		lookOnTarget();

		if(this.rangedAttackTime < 0)
		{
			hans.resetActiveHand();
			hans.setSneaking(false);
			rangedAttackTime += 1;
		}

		if(this.rangedAttackTime < 20)
		{
			for(EntityHans anotherHans : this.hans.world.getEntitiesWithinAABB(EntityHans.class, hans.getEntityBoundingBox().grow(20), input -> input!=null&&input!=this.hans))
				if(anotherHans.getAttackTarget()!=attackTarget&&anotherHans.isValidTarget(attackTarget))
				{
					anotherHans.tasks.addTask(2, new AIHansSalute(anotherHans, this.hans));
					anotherHans.setAttackTarget(attackTarget);
				}
			hans.setActiveHand(EnumHand.MAIN_HAND);
		}
	}

	@Override
	protected float calculateBallisticAngle(ItemStack ammo, EntityLivingBase attackTarget)
	{
		return 0f;
	}

	@Override
	public void setRequiredAnimation()
	{
		if(motionState==MotionState.IN_POSITION||motionState==MotionState.SET_UP)
		{
			if(attackTarget!=null&&!attackTarget.isDead&&seeTime > 10)
			{
				hans.setSneaking(true);
				hans.legAnimation = HansLegAnimation.KNEELING;
				hans.armAnimation = HansArmAnimation.SQUAD_ORDER_ONWARDS;
			}
		}
		else
		{
			hans.legAnimation = HansLegAnimation.STANDING;
		}
	}
}