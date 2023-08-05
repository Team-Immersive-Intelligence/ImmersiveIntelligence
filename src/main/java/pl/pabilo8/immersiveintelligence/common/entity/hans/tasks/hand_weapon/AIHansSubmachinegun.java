package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.hand_weapon;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIISubmachinegun;

/**
 * @author Pabilo8
 * @since 23.04.2021
 */
public class AIHansSubmachinegun extends AIHansHandWeapon
{
	/**
	 * For how many ticks should the Hans shoot the SMG in a burst.
	 */
	private final int burstTime;
	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
	 * maxRangedAttackTime.
	 */
	private int rangedAttackTime;

	public AIHansSubmachinegun(EntityHans hans)
	{
		super(hans, 4, 18, 1f);
		this.rangedAttackTime = -1;
		this.burstTime = 10;
	}

	@Override
	protected boolean hasAnyAmmo()
	{
		final ItemStack smg = getWeapon();
		final ItemStack magazine = ItemNBTHelper.getItemStack(smg, "magazine");
		return hans.hasAmmo||IIContent.itemSubmachinegun.isAmmo(magazine, smg)||!IIContent.itemSubmachinegun.findAmmo(hans, smg).isEmpty();
	}

	@Override
	protected boolean isValidWeapon()
	{
		return getWeapon().getItem() instanceof ItemIISubmachinegun;
	}

	@Override
	protected void executeTask()
	{
		assert attackTarget!=null;
		final ItemStack smg = getWeapon();
		ItemStack magazine = ItemNBTHelper.getItemStack(smg, ItemIISubmachinegun.SHOULD_RELOAD);
		boolean isAmmoLoaded = IIContent.itemSubmachinegun.isAmmo(magazine, smg);

		//use weapon
		hans.setActiveHand(EnumHand.MAIN_HAND);

		//reload if magazine empty
		if(!isAmmoLoaded)
		{
			if(!ItemNBTHelper.getBoolean(smg, ItemIISubmachinegun.SHOULD_RELOAD))
			{
				final ItemStack ammo = IIContent.itemSubmachinegun.findAmmo(hans, smg);
				hans.hasAmmo = !ammo.isEmpty();
				if(hans.hasAmmo)
					ItemNBTHelper.setBoolean(smg, ItemIISubmachinegun.SHOULD_RELOAD, true);
				hans.setSneaking(false);
			}
		}

		//hans can not shoot while running away
		if(motionState==MotionState.FALLBACK)
		{
			if(!ItemNBTHelper.getBoolean(smg, ItemIISubmachinegun.SHOULD_RELOAD))
				hans.resetActiveHand();
			hans.setSneaking(false);
			return;
		}

		lookOnTarget();


		if(this.rangedAttackTime < 0)
		{
			hans.resetActiveHand();
			rangedAttackTime += 1;
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
		return IIUtils.getDirectFireAngle(IIContent.itemAmmoSubmachinegun.getDefaultVelocity(), IIContent.itemAmmoSubmachinegun.getMass(ammo), hans.getPositionVector().addVector(0, (double)hans.getEyeHeight()-0.10000000149011612D, 0).subtract(IIUtils.getEntityCenter(attackTarget)));
	}
}