package pl.pabilo8.immersiveintelligence.common.item.weapons;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Submachinegun;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandler;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandlerMagazine;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.sound.AdvancedSounds.RangedSound;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 01.11.2019
 */
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIISubmachinegun extends ItemIIGunBase
{
	//--- Ammunition Handler ---//
	private final AmmoHandlerMagazine ammoHandler;

	public ItemIISubmachinegun()
	{
		super("submachinegun");
		ammoHandler = new AmmoHandlerMagazine(this, MAGAZINE, IIContent.itemAmmoSubmachinegun)
		{
			@Override
			protected boolean isValidType(ItemStack weapon, Magazines magazine)
			{
				return magazine!=Magazines.SUBMACHINEGUN_DRUM||hasIIUpgrade(weapon, WeaponUpgrade.BOTTOM_LOADING);
			}

			@Override
			public void markLoadedAmmo(EasyNBT nbt, ItemStack ammo)
			{
				if(IIContent.itemBulletMagazine.stackToSub(ammo)==Magazines.SUBMACHINEGUN_DRUM)
					nbt.withBoolean("isDrum", true);
				else
					nbt.without("isDrum");
			}

			@Nullable
			@Override
			protected SoundEvent getUnloadSound(ItemStack weapon, EasyNBT nbt)
			{
				return IISounds.submachinegunUnload;
			}

			@Nullable
			@Override
			protected SoundEvent getReloadSound(ItemStack weapon, EasyNBT nbt)
			{
				return IISounds.submachinegunReload;
			}
		};
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity user, int itemSlot, boolean isSelected)
	{

		
		super.onUpdate(stack, world, user, itemSlot, isSelected);

		//server check
		if(world.isRemote || !isSelected || !(user instanceof EntityLivingBase))
			return;

		EntityLivingBase livingUser = (EntityLivingBase) user;

		//check NBT for drum
		EasyNBT nbt = EasyNBT.wrapNBT(stack.getTagCompound());
		ItemStack magazineStack = nbt.getItemStack(MAGAZINE);
		if(magazineStack.isEmpty())
			return;

		//check if drum is loaded
		if(IIContent.itemBulletMagazine.stackToSub(magazineStack) != Magazines.SUBMACHINEGUN_DRUM)
			return;

		//check if bottom loader/ drum thing is NOT installed
		if(!hasIIUpgrade(stack, WeaponUpgrade.BOTTOM_LOADING))
		{
			//clear NBT for drum from gun
			nbt.without(MAGAZINE);
			nbt.without("isDrum");
			stack.setTagCompound(nbt.unwrap());

			//drops drum
			world.spawnEntity(new EntityItem(
					world,
					livingUser.posX,
					livingUser.posY + livingUser.getEyeHeight(),
					livingUser.posZ,
					magazineStack
			));
		}
	}


	@Override
	public int getSlotCount(ItemStack stack)
	{
		return 3;
	}

	@Override
	public void removeFromWorkbench(EntityPlayer player, ItemStack stack)
	{
		if(hasIIUpgrade(stack, WeaponUpgrade.STURDY_BARREL, WeaponUpgrade.BOTTOM_LOADING))
			IIUtils.unlockIIAdvancement(player, "main/infinite_power");
		if(hasIIUpgrades(stack, WeaponUpgrade.SUPPRESSOR, WeaponUpgrade.FOLDING_STOCK))
			IIUtils.unlockIIAdvancement(player, "main/the_silent_unseen");
	}

	@Override
	public AmmoHandler getAmmoHandler(ItemStack weapon)
	{
		return ammoHandler;
	}

	@Override
	protected FireModeType getFireMode(ItemStack weapon)
	{
		return FireModeType.AUTOMATIC;
	}

	@Override
	protected double getEquipSpeed(ItemStack weapon, EasyNBT nbt)
	{
		return nbt.hasKey(WeaponUpgrade.FOLDING_STOCK)?
				0.35: 0.65;
	}

	@Override
	public int getFireDelay(ItemStack weapon, EasyNBT nbt)
	{
		return Submachinegun.bulletFireTime;
	}

	@Nullable
	@Override
	protected SoundEvent getDryfireSound(ItemStack weapon, EasyNBT easyNBT)
	{
		return IISounds.submachinegunShotDry;
	}

	@Nullable
	@Override
	protected RangedSound getFireSound(ItemStack weapon, EasyNBT easyNBT)
	{
		return IISounds.submachinegunShot;
	}

	@Override
	protected int getEnemyAttractRange(ItemStack weapon, EasyNBT nbt)
	{
		return nbt.hasKey(WeaponUpgrade.SUPPRESSOR)?
				Submachinegun.enemyAttractRangeSuppressor: Submachinegun.enemyAttractRange;
	}

	@Override
	public int getAimingTime(ItemStack weapon, EasyNBT nbt)
	{
		return nbt.hasKey(WeaponUpgrade.FOLDING_STOCK)?
				Submachinegun.aimTimeFoldedStock: Submachinegun.aimTime;
	}

	@Override
	public int getReloadTime(ItemStack weapon, ItemStack loaded, EasyNBT nbt)
	{
		if(IIContent.itemBulletMagazine.stackToSub(weapon)==Magazines.SUBMACHINEGUN_DRUM)
			return Submachinegun.drumReloadTime;
		return Submachinegun.clipReloadTime;
	}

	@Override
	public float getHorizontalRecoil(ItemStack weapon, EasyNBT nbt, boolean isAimed)
	{
		return Submachinegun.newRecoilHorizontal;
	}

	@Override
	public float getVerticalRecoil(ItemStack weapon, EasyNBT nbt, boolean isAimed)
	{
		return Submachinegun.newRecoilVertical;
	}

	@Override
	public float getMaxHorizontalRecoil(ItemStack weapon, EasyNBT nbt)
	{
		return Submachinegun.newMaxRecoilHorizontal;
	}

	@Override
	public float getMaxVerticalRecoil(ItemStack weapon, EasyNBT nbt)
	{
		return Submachinegun.newMaxRecoilVertical;
	}

	@Override
	protected float getGunfireParticleSize(ItemStack weapon, EasyNBT nbt)
	{
		return nbt.hasKey(WeaponUpgrade.SUPPRESSOR)?0.5f: 1.5f;
	}

	@Override
	protected float getVelocityModifier(ItemStack weapon, EasyNBT nbt, ItemStack ammo)
	{
		return nbt.hasKey(WeaponUpgrade.STURDY_BARREL)?Submachinegun.sturdyBarrelVelocityMod: 1f;
	}
}
