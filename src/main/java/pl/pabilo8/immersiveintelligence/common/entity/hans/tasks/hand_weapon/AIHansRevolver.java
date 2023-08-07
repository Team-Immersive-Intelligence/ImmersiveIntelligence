package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.hand_weapon;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.items.ItemRevolver;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;

/**
 * @author Pabilo8
 * @since 23.04.2021
 */
public class AIHansRevolver extends AIHansHandWeapon
{
	private static final ItemRevolver ITEM_REVOLVER = (ItemRevolver)IEContent.itemRevolver;

	public AIHansRevolver(EntityHans hans)
	{
		super(hans, 2f, 16f, 1f);
		this.rangedAttackTime = -1;
	}

	@Override
	protected boolean isValidWeapon()
	{
		return getWeapon().getItem()==ITEM_REVOLVER;
	}


	protected boolean hasAnyAmmo()
	{
		final ItemStack smg = getWeapon();
		final ItemStack magazine = ItemNBTHelper.getItemStack(smg, "magazine");
		return hans.hasAmmo||IIContent.itemSubmachinegun.isAmmo(magazine, smg)||!IIContent.itemSubmachinegun.findAmmo(hans, smg).isEmpty();
	}

	@Override
	protected void executeTask()
	{
		assert attackTarget!=null;

		if(this.rangedAttackTime < 0)
		{
			hans.resetActiveHand();
			rangedAttackTime += 1;
		}

		final ItemStack smg = getWeapon();

		lookOnTarget();
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
				 */

		}

	}

	@Override
	public void setRequiredAnimation()
	{

	}

	@Override
	protected float calculateBallisticAngle(ItemStack ammo, EntityLivingBase attackTarget)
	{
		return IIUtils.getDirectFireAngle(IIContent.itemAmmoRevolver.getDefaultVelocity(), IIContent.itemAmmoRevolver.getMass(ammo), hans.getPositionVector().addVector(0, (double)hans.getEyeHeight()-0.10000000149011612D, 0).subtract(attackTarget.getPositionVector()));
	}
}