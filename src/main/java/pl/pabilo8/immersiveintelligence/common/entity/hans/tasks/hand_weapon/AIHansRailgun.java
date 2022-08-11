package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.hand_weapon;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansAnimations.HansLegAnimation;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIRailgunOverride;

/**
 * @author Pabilo8
 * @since 23.04.2021
 */
public class AIHansRailgun extends AIHansHandWeapon
{
	/**
	 * Snipers lie down instead of kneeling
	 */
	boolean sniper = false;
	/**
	 * The maximum time the Hans has to wait before peforming another ranged attack.
	 */
	protected final int holdFireTime;

	public AIHansRailgun(EntityHans hans)
	{
		super(hans, 5, 64, 0.9f);
		this.setMutexBits(3);
		this.holdFireTime = 40;
	}

	@Override
	protected boolean hasAnyAmmo()
	{
		return hans.hasAmmo = (!ItemIIRailgunOverride.findAmmo(hans).isEmpty());
	}

	@Override
	protected boolean isValidWeapon()
	{
		return getWeapon().getItem()==IEContent.itemRailgun;
	}

	@Override
	public void executeTask()
	{
		assert attackTarget!=null;

		if(motionState==MotionState.FALLBACK||motionState==MotionState.COME_TOWARDS)
			return;

		lookOnTarget();

		final ItemStack backpack = hans.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		final ItemStack railgun = getWeapon();
		//int extracted = EnergyHelper.extractFlux(backpack,4000,false);
		//extracted =
		//EnergyHelper.insertFlux(backpack,extracted,false);
		if(EnergyHelper.isFluxItem(backpack)&&EnergyHelper.isFluxItem(railgun))
			EnergyHelper.insertFlux(railgun, 99999, false);

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
				sniper = ((ItemIIRailgunOverride)railgun.getItem()).getUpgrades(railgun).getBoolean("scope");

				if(rangedAttackTime >= holdFireTime)
				{
					IEContent.itemRailgun.onPlayerStoppedUsing(railgun, this.hans.world, this.hans, IEContent.itemRailgun.getMaxItemUseDuration(railgun)-rangedAttackTime);
					rangedAttackTime = -10;
				}
			}
		}
	}

	@Override
	protected float calculateBallisticAngle(ItemStack ammo, EntityLivingBase attackTarget)
	{
		if(ammo.getItem()==IIContent.itemRailgunGrenade)
			return Utils.getDirectFireAngle(IIContent.itemRailgunGrenade.getDefaultVelocity(), IIContent.itemRailgunGrenade.getMass(ammo),
					hans.getPositionVector().addVector(0, hans.getEyeHeight()-0.10000000149011612D, 0).subtract(Utils.getEntityCenter(attackTarget)));
		else
			return Utils.getIEDirectRailgunAngle(ammo, hans.getPositionVector().addVector(0, hans.getEyeHeight(), 0).subtract(Utils.getEntityCenter(attackTarget)));
	}

	@Override
	public void setRequiredAnimation()
	{
		if(motionState==MotionState.IN_POSITION||motionState==MotionState.SET_UP)
		{
			if(attackTarget!=null&&canFire)
				hans.legAnimation = (sniper&&!hans.enemyContact)?HansLegAnimation.LYING: HansLegAnimation.KNEELING;
		}
		else
			hans.legAnimation = HansLegAnimation.STANDING;
	}
}