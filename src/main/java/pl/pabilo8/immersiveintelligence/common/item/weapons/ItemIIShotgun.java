package pl.pabilo8.immersiveintelligence.common.item.weapons;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Shotgun;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandler;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandlerList;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.RangedSound;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;

import javax.annotation.Nullable;

@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIShotgun extends ItemIIGunBase
{
	//--- NBT Values Reference ---//
	public static final String HANDMADE = "handmade";

	//--- Ammunition Handler ---//
	public static final int MAG_SIZE = Shotgun.clipSize;
	private final AmmoHandlerList ammoHandler;
	//private final AmmoHandlerMagazine ammoHandlerSemiAuto;

	public ItemIIShotgun()
	{
		super("shotgun");
		ammoHandler = new AmmoHandlerList(this, BULLETS, IIContent.itemAmmoShotgun, MAG_SIZE)
		{
			@Nullable
			@Override
			protected SoundEvent getStartLoadingSound(ItemStack weapon, EasyNBT nbt)
			{
				return IISounds.rifleLoadStart;
			}

			@Nullable
			@Override
			protected SoundEvent getReloadSound(ItemStack weapon, EasyNBT nbt)
			{
				return IISounds.rifleLoad;
			}

			@Nullable
			@Override
			protected SoundEvent getFinishLoadingSound(ItemStack weapon, EasyNBT nbt)
			{
				return IISounds.rifleLoadEnd;
			}
		};
		/*
		ammoHandlerSemiAuto = new AmmoHandlerMagazine(this, MAGAZINE, IIContent.itemAmmoShotgun)
		{
			@Override
			protected boolean isValidType(ItemStack weapon, Magazines magazine)
			{
				return magazine==Magazines.SHOTGUN;
			}

			@Nullable
			@Override
			protected SoundEvent getUnloadSound(ItemStack weapon, EasyNBT nbt)
			{
				return IISounds.rifleUnloadMagazine;
			}

			@Nullable
			@Override
			protected SoundEvent getReloadSound(ItemStack weapon, EasyNBT nbt)
			{
				return IISounds.rifleReloadMagazine;
			}
		};
		*/
	}

	@Override
	public int getSlotCount(ItemStack stack)
	{
		return 2;
	}

	@Override
	public void removeFromWorkbench(EntityPlayer player, ItemStack stack)
	{
		//NBTTagCompound upgrades = getUpgrades(stack);
		// TODO: 31.01.2023 advancements
	}

	@Override
	public AmmoHandler getAmmoHandler(ItemStack weapon)
	{
		//return hasIIUpgrade(weapon, WeaponUpgrade.SEMI_AUTOMATIC)?ammoHandlerSemiAuto: ammoHandler;
		return ammoHandler;
	}

	@Override
	protected FireModeType getFireMode(ItemStack weapon)
	{
		//return hasIIUpgrade(weapon, WeaponUpgrade.SEMI_AUTOMATIC)?FireModeType.AUTOMATIC: FireModeType.SINGULAR;
		return FireModeType.SINGULAR;
	}

	@Override
	protected double getEquipSpeed(ItemStack weapon, EasyNBT nbt)
	{
		//return hasIIUpgrade(weapon, WeaponUpgrade.SEMI_AUTOMATIC)?
				//1.0625: 0.9;
		return 0.9;
	}

	@Override
	public int getFireDelay(ItemStack weapon, EasyNBT nbt)
	{
		//return hasIIUpgrade(weapon, WeaponUpgrade.SEMI_AUTOMATIC)?Shotgun.bulletFireTimeSemiAuto: Shotgun.bulletFireTime;
		return Shotgun.bulletFireTime;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		if(this.isInCreativeTab(tab))
		{
			list.add(new ItemStack(this, 1));

			ItemStack handmade = new ItemStack(this, 1, 0);
			handmade.setTagCompound(EasyNBT.newNBT().withBoolean(HANDMADE, true).unwrap());
			list.add(handmade);
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		if(ItemNBTHelper.hasKey(stack, HANDMADE))
			return "item.immersiveintelligence.shotgun_handmade";
		return super.getUnlocalizedName(stack);
	}

	@Nullable
	@Override
	protected SoundEvent getDryfireSound(ItemStack weapon, EasyNBT easyNBT)
	{
		return IISounds.rifleShotDry;
	}

	@Nullable
	@Override
	protected RangedSound getFireSound(ItemStack weapon, EasyNBT easyNBT)
	{
		//return hasIIUpgrade(weapon, WeaponUpgrade.SEMI_AUTOMATIC)?IISounds.rifleShot: IISounds.rifleBoltShot;
		return IISounds.rifleShot;
	}

	@Override
	protected int getEnemyAttractRange(ItemStack weapon, EasyNBT nbt)
	{
		return Shotgun.enemyAttractRange;
	}

	@Override
	public int getAimingTime(ItemStack weapon, EasyNBT nbt)
	{
		return Shotgun.aimTime;
	}

	@Override
	public int getReloadTime(ItemStack weapon, ItemStack loaded, EasyNBT nbt)
	{
		//return hasIIUpgrades(weapon, WeaponUpgrade.SEMI_AUTOMATIC)?Shotgun.magazineReloadTime: Shotgun.bulletReloadTime;
		return Shotgun.bulletReloadTime;
	}

	@Override
	public float getHorizontalRecoil(ItemStack weapon, EasyNBT nbt, boolean isAimed)
	{
		return (isAimed?0.5f: 1f)*Shotgun.recoilHorizontal;
	}

	@Override
	public float getVerticalRecoil(ItemStack weapon, EasyNBT nbt, boolean isAimed)
	{
		//if(nbt.hasKey(WeaponUpgrade.SEMI_AUTOMATIC))
			//return (isAimed?0.75f: 1f)*1.55f;
		return (isAimed?0.5f: 1f)*Shotgun.recoilVertical;
	}

	@Override
	public float getMaxHorizontalRecoil(ItemStack weapon, EasyNBT nbt)
	{
		return Shotgun.maxRecoilHorizontal;
	}

	@Override
	public float getMaxVerticalRecoil(ItemStack weapon, EasyNBT nbt)
	{
		return Shotgun.maxRecoilVertical;
	}

	@Override
	protected float getGunfireParticleSize(ItemStack weapon, EasyNBT nbt)
	{
		return 1.5f;
	}

	@Override
	protected float getVelocityModifier(ItemStack weapon, EasyNBT nbt, ItemStack ammo)
	{
		//if(nbt.hasKey(WeaponUpgrade.SEMI_AUTOMATIC))
			//return 1.5f;
		return 1.75f;
	}
}
