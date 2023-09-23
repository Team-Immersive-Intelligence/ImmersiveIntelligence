package pl.pabilo8.immersiveintelligence.common.item.weapons;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Submachinegun;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrades;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandler;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandlerMagazine;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.RangedSound;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 01-11-2019
 */
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
				return magazine!=Magazines.SUBMACHINEGUN_DRUM||hasIIUpgrade(weapon, WeaponUpgrades.BOTTOM_LOADING);
			}

			@SideOnly(Side.CLIENT)
			@Nullable
			@Override
			protected SoundEvent getUnloadSound(ItemStack weapon, EasyNBT nbt)
			{
				return IISounds.submachinegunUnload;
			}

			@SideOnly(Side.CLIENT)
			@Nullable
			@Override
			protected SoundEvent getReloadSound(ItemStack weapon, EasyNBT nbt)
			{
				return IISounds.submachinegunReload;
			}
		};
	}

	@Override
	public int getSlotCount(ItemStack stack)
	{
		return 3;
	}

	@Override
	public void removeFromWorkbench(EntityPlayer player, ItemStack stack)
	{
		//NBTTagCompound upgrades = getUpgrades(stack);
		// TODO: 09.08.2020 advancements
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
		return nbt.hasKey(WeaponUpgrades.FOLDING_STOCK)?
				0.35: 0.65;
	}

	@Override
	public int getFireDelay(ItemStack weapon, EasyNBT nbt)
	{
		return Submachinegun.bulletFireTime;
	}

	@SideOnly(Side.CLIENT)
	@Nullable
	@Override
	protected SoundEvent getDryfireSound(ItemStack weapon, EasyNBT easyNBT)
	{
		return IISounds.submachinegunShotDry;
	}

	@SideOnly(Side.CLIENT)
	@Nullable
	@Override
	protected RangedSound getFireSound(ItemStack weapon, EasyNBT easyNBT)
	{
		return IISounds.submachinegunShot;
	}

	@Override
	protected int getEnemyAttractRange(ItemStack weapon, EasyNBT nbt)
	{
		return nbt.hasKey(WeaponUpgrades.SUPPRESSOR)?
				Submachinegun.enemyAttractRangeSuppressor: Submachinegun.enemyAttractRange;
	}

	@Override
	public int getAimingTime(ItemStack weapon, EasyNBT nbt)
	{
		return nbt.hasKey(WeaponUpgrades.FOLDING_STOCK)?
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
		return Submachinegun.recoilHorizontal;
	}

	@Override
	public float getVerticalRecoil(ItemStack weapon, EasyNBT nbt, boolean isAimed)
	{
		return Submachinegun.recoilVertical;
	}

	@Override
	public float getMaxHorizontalRecoil(ItemStack weapon, EasyNBT nbt)
	{
		return Submachinegun.maxRecoilHorizontal;
	}

	@Override
	public float getMaxVerticalRecoil(ItemStack weapon, EasyNBT nbt)
	{
		return Submachinegun.maxRecoilVertical;
	}

	@Override
	protected float getGunfireParticleSize(ItemStack weapon, EasyNBT nbt)
	{
		return nbt.hasKey(WeaponUpgrades.SUPPRESSOR)?0.5f: 1.5f;
	}

	@Override
	protected float getVelocityModifier(ItemStack weapon, EasyNBT nbt, ItemStack ammo)
	{
		return nbt.hasKey(WeaponUpgrades.STURDY_BARREL)?Submachinegun.sturdyBarrelVelocityMod: 1f;
	}
}
