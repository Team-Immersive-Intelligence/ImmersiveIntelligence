package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.hand_weapon;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIAssaultRifle;

/**
 * @author Pabilo8
 * @since 23.04.2021
 */
public class AIHansAssaultRifle extends AIHansHandWeapon
{
	/**
	 * For how many ticks should the Hans shoot the stg in a burst.
	 */
	private final int burstTime;
	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
	 * maxRangedAttackTime.
	 */
	private int rangedAttackTime;

	public AIHansAssaultRifle(EntityHans hans)
	{
		super(hans, 4, 25, 1f);
		this.rangedAttackTime = -1;
		this.burstTime = 4;
	}

	@Override
	protected boolean hasAnyAmmo()
	{
		final ItemStack stg = getWeapon();
		final ItemStack magazine = ItemNBTHelper.getItemStack(stg, "magazine");
		return hans.hasAmmo||IIContent.itemAssaultRifle.isAmmo(magazine, stg)||!IIContent.itemAssaultRifle.findAmmo(hans, stg).isEmpty();
	}

	@Override
	protected boolean isValidWeapon()
	{
		return getWeapon().getItem() instanceof ItemIIAssaultRifle;
	}

	@Override
	protected void executeTask()
	{
		assert attackTarget!=null;
		final ItemStack stg = getWeapon();
		ItemStack magazine = ItemNBTHelper.getItemStack(stg, "magazine");
		boolean isAmmoLoaded = IIContent.itemAssaultRifle.isAmmo(magazine, stg);

		//use weapon
		hans.setActiveHand(EnumHand.MAIN_HAND);

		//reload if magazine empty
		if(!isAmmoLoaded)
		{
			if(!ItemNBTHelper.getBoolean(stg, "shouldReload"))
			{
				final ItemStack ammo = IIContent.itemAssaultRifle.findAmmo(hans, stg);
				hans.hasAmmo = !ammo.isEmpty();
				if(hans.hasAmmo)
					ItemNBTHelper.setBoolean(stg, "shouldReload", true);
				hans.setSneaking(false);
			}
		}

		//hans can not shoot while running away
		if(motionState==MotionState.FALLBACK)
		{
			if(!ItemNBTHelper.getBoolean(stg, "shouldReload"))
				hans.resetActiveHand();
			hans.setSneaking(false);
			return;
		}

		lookOnTarget();


		if(this.rangedAttackTime < 0)
		{
			hans.resetActiveHand();
			rangedAttackTime ++;
		}
		if(canFire&&aimingAtTarget&&this.rangedAttackTime < burstTime)
		{
			if(isAmmoLoaded)
			{
				hans.hasAmmo = true;
				hans.setSneaking(true);
				hans.rotationPitch = calculateBallisticAngle(IIContent.itemBulletMagazine.takeBullet(magazine, false), attackTarget);
			}
			else
				hans.resetActiveHand();
			rangedAttackTime ++;

			if(rangedAttackTime >= burstTime)
				rangedAttackTime = -(int)(-burstTime*0.75);
		}
		else
			hans.resetActiveHand();
	}

	@Override
	public void setRequiredAnimation()
	{

	}

	@Override
	protected float calculateBallisticAngle(ItemStack ammo, EntityLivingBase attackTarget)
	{
		return IIUtils.getDirectFireAngle(IIContent.itemAmmoAssaultRifle.getDefaultVelocity(), IIContent.itemAmmoAssaultRifle.getMass(ammo),
				hans.getPositionVector().addVector(0, (double)hans.getEyeHeight()-0.10000000149011612D, 0).subtract(IIUtils.getEntityCenter(attackTarget))
		);
	}
}