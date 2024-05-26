package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.hand_weapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.item.ammo.grenade.ItemIIAmmoGrenade;

/**
 * @author Pabilo8
 * @since 23.04.2021
 */
public class AIHansGrenade extends AIHansHandWeapon
{
	/**
	 * How many ticks does it take to throw a grenade
	 */
	private final int throwTime;
	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
	 * maxRangedAttackTime.
	 */
	private int rangedAttackTime;

	public AIHansGrenade(EntityHans hans)
	{
		super(hans, 4, 22, 1f);
		this.rangedAttackTime = -1;
		this.throwTime = 40;
	}

	@Override
	protected boolean hasAnyAmmo()
	{
		return hans.hasAmmo;
	}

	@Override
	protected boolean isValidWeapon()
	{
		return getWeapon().getItem() instanceof ItemIIAmmoGrenade;
	}

	@Override
	protected boolean hasToSeeEnemy()
	{
		return false;
	}

	@Override
	protected void executeTask()
	{
		assert attackTarget!=null;
		final ItemStack grenade = getWeapon();

		//hans can not shoot while running away
		if(motionState==MotionState.FALLBACK||motionState==MotionState.COME_TOWARDS)
		{
			hans.setSneaking(false);
			return;
		}

		//use weapon
		hans.setActiveHand(EnumHand.MAIN_HAND);

		lookOnTarget();
		hans.cameraPitch = hans.rotationPitch = -calculateBallisticAngle(grenade, attackTarget);

		if(this.rangedAttackTime < 0)
		{
			hans.resetActiveHand();
			rangedAttackTime += 1;
		}
		else
		{
			rangedAttackTime++;

			hans.setActiveHand(EnumHand.MAIN_HAND);

			if(rangedAttackTime >= throwTime)
			{
				IIContent.itemGrenade.onPlayerStoppedUsing(grenade, this.hans.world, this.hans, IIContent.itemGrenade.getMaxItemUseDuration(grenade)-rangedAttackTime);
//				hans.stopActiveHand();
				lookOnTarget();
				hans.cameraPitch = hans.rotationPitch = -calculateBallisticAngle(grenade, attackTarget);
				rangedAttackTime = -10;

				Vec3d away = null;
				int tries = 0;
				while(away==null&&tries < 10)
				{
					away = RandomPositionGenerator.findRandomTargetBlockAwayFrom(hans, 40, 10, attackTarget.getPositionVector());
					tries++;
				}


				if(away!=null)
				{
					this.hans.getNavigator().tryMoveToXYZ(away.x, away.y, away.z, this.moveSpeed*1.125f);
					this.hans.setSprinting(true);
					resetTask();
					motionState = MotionState.FALLBACK;
				}

			}
		}


	}

	@Override
	public void setRequiredAnimation()
	{

	}

	@Override
	protected float calculateBallisticAngle(ItemStack ammo, EntityLivingBase attackTarget)
	{
		return IIAmmoUtils.calculateBallisticAngle(hans.getPositionVector(), attackTarget.getPositionVector(), ammo, 0.04f);
	}
}