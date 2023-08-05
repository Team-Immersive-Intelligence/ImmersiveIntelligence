package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.hand_weapon;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIAssaultRifle;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIGunBase;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandler;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8
 * @since 23.04.2021
 */
public abstract class AIHansAbstractGun extends AIHansHandWeapon
{
	/**
	 * For how many ticks should the Hans shoot the stg in a burst.
	 */
	protected final int burstTime;
	protected final ItemIIGunBase item;
	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
	 * maxRangedAttackTime.
	 */
	protected int rangedAttackTime;

	public AIHansAbstractGun(EntityHans hans, ItemIIGunBase item, float minAttackDistance, float maxAttackDistance, int burstTime)
	{
		super(hans, minAttackDistance, maxAttackDistance, 1f);
		this.item = item;
		this.burstTime = burstTime;
		this.rangedAttackTime = -1;
	}

	@Override
	protected boolean hasAnyAmmo()
	{
		final ItemStack weapon = getWeapon();
		return hans.hasAmmo||
				item.getAmmoHandler(weapon).canFire(weapon, EasyNBT.wrapNBT(weapon))||
				(hans.hasAmmo = !item.findAmmo(hans, weapon).isEmpty());
	}

	@Override
	protected boolean isValidWeapon()
	{
		return getWeapon().getItem()==item;
	}

	@Override
	protected void executeTask()
	{
		assert attackTarget!=null;
		final ItemStack stg = getWeapon();
		final EasyNBT nbt = EasyNBT.wrapNBT(stg);
		final AmmoHandler ammoHandler = item.getAmmoHandler(stg);
		final boolean canFire = ammoHandler.canFire(getWeapon(), nbt);

		//use weapon
		hans.setActiveHand(EnumHand.MAIN_HAND);

		//reload if magazine empty
		if(!canFire)
			nbt.withBoolean(ItemIIGunBase.SHOULD_RELOAD, true);

		//hans can not shoot while running away
		if(motionState==MotionState.FALLBACK)
		{
			if(!ItemNBTHelper.getBoolean(stg, ItemIIGunBase.SHOULD_RELOAD))
				hans.resetActiveHand();
			hans.setSneaking(false);
			return;
		}

		lookOnTarget();

		//attempt attacking the enemy
		if(this.rangedAttackTime < 0)
		{
			hans.resetActiveHand();
			rangedAttackTime++;
		}

		if(canFire&&aimingAtTarget)
		{
			if(this.rangedAttackTime < burstTime)
			{
				hans.hasAmmo = true;
				hans.setSneaking(true);
				hans.rotationPitch = calculateBallisticAngle(ammoHandler.getNextAmmo(stg, nbt, false), attackTarget);
			}
			else
				hans.resetActiveHand();
			rangedAttackTime++;

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
		IAmmo ammoType = (IAmmo)ammo.getItem();
		return IIUtils.getDirectFireAngle(ammoType.getDefaultVelocity(), ammoType.getMass(ammo),
				hans.getPositionVector().addVector(0, (double)hans.getEyeHeight()-0.10000000149011612D, 0).subtract(IIUtils.getEntityCenter(attackTarget))
		);
	}
}