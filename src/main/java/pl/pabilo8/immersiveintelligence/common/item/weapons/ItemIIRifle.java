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
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedZoomTool;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Rifle;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandler;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandlerList;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandlerMagazine;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.RangedSound;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 01-11-2019
 */
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIRifle extends ItemIIGunBase implements IAdvancedZoomTool
{
	//--- NBT Values Reference ---//
	public static final String HANDMADE = "handmade";

	//--- Scope Overlay Textures ---//
	public static final ResourceLocation OVERLAY_SCOPE = new ResourceLocation(ImmersiveIntelligence.MODID,
			"textures/gui/item/machinegun/scope.png");

	//--- Ammunition Handler ---//
	public static final int MAG_SIZE = Rifle.clipSize;
	private final AmmoHandlerList ammoHandler;
	private final AmmoHandlerMagazine ammoHandlerSemiAuto;

	public ItemIIRifle()
	{
		super("rifle");
		ammoHandler = new AmmoHandlerList(this, BULLETS, IIContent.itemAmmoMachinegun, MAG_SIZE)
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
		ammoHandlerSemiAuto = new AmmoHandlerMagazine(this, MAGAZINE, IIContent.itemAmmoMachinegun)
		{
			@Override
			protected boolean isValidType(ItemStack weapon, Magazines magazine)
			{
				return magazine==Magazines.RIFLE;
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
		return hasIIUpgrade(weapon, WeaponUpgrade.SEMI_AUTOMATIC)?ammoHandlerSemiAuto: ammoHandler;
	}

	@Override
	protected FireModeType getFireMode(ItemStack weapon)
	{
		return hasIIUpgrade(weapon, WeaponUpgrade.SEMI_AUTOMATIC)?FireModeType.AUTOMATIC: FireModeType.SINGULAR;
	}

	@Override
	protected double getEquipSpeed(ItemStack weapon, EasyNBT nbt)
	{
		return hasIIUpgrade(weapon, WeaponUpgrade.EXTENDED_BARREL, WeaponUpgrade.SEMI_AUTOMATIC)?
				1.0625: 0.9;
	}

	@Override
	public int getFireDelay(ItemStack weapon, EasyNBT nbt)
	{
		return hasIIUpgrade(weapon, WeaponUpgrade.SEMI_AUTOMATIC)?Rifle.bulletFireTimeSemiAuto: Rifle.bulletFireTime;
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
			return "item.immersiveintelligence.rifle_handmade";
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
		return hasIIUpgrade(weapon, WeaponUpgrade.SEMI_AUTOMATIC)?IISounds.rifleShot: IISounds.rifleBoltShot;
	}

	@Override
	protected int getEnemyAttractRange(ItemStack weapon, EasyNBT nbt)
	{
		return Rifle.enemyAttractRange;
	}

	@Override
	public int getAimingTime(ItemStack weapon, EasyNBT nbt)
	{
		return Rifle.aimTime;
	}

	@Override
	public int getReloadTime(ItemStack weapon, ItemStack loaded, EasyNBT nbt)
	{
		return hasIIUpgrades(weapon, WeaponUpgrade.SEMI_AUTOMATIC)?Rifle.magazineReloadTime: Rifle.bulletReloadTime;
	}

	@Override
	public float getHorizontalRecoil(ItemStack weapon, EasyNBT nbt, boolean isAimed)
	{
		return (isAimed?0.5f: 1f)*Rifle.recoilHorizontal;
	}

	@Override
	public float getVerticalRecoil(ItemStack weapon, EasyNBT nbt, boolean isAimed)
	{
		if(nbt.hasKey(WeaponUpgrade.SEMI_AUTOMATIC))
			return (isAimed?0.75f: 1f)*1.55f;
		return (isAimed?0.5f: 1f)*Rifle.recoilVertical;
	}

	@Override
	public float getMaxHorizontalRecoil(ItemStack weapon, EasyNBT nbt)
	{
		return Rifle.maxRecoilHorizontal;
	}

	@Override
	public float getMaxVerticalRecoil(ItemStack weapon, EasyNBT nbt)
	{
		return Rifle.maxRecoilVertical;
	}

	@Override
	protected float getGunfireParticleSize(ItemStack weapon, EasyNBT nbt)
	{
		return 1.5f;
	}

	@Override
	protected float getVelocityModifier(ItemStack weapon, EasyNBT nbt, ItemStack ammo)
	{
		if(nbt.hasKey(WeaponUpgrade.EXTENDED_BARREL))
			return Rifle.longBarrelVelocityMod;
		else if(nbt.hasKey(WeaponUpgrade.SEMI_AUTOMATIC))
			return 1.5f;
		return 1.75f;
	}

	//--- IAdvancedZoomTool ---//

	@Override
	public boolean shouldZoom(ItemStack stack, EntityPlayer player)
	{
		boolean isAimed = ItemNBTHelper.getInt(stack, AIMING) > getAimingTime(stack, EasyNBT.wrapNBT(getUpgrades(stack)))*0.75;
		return isAimed&&hasIIUpgrade(stack, WeaponUpgrade.SCOPE);
	}

	@Override
	public float getZoomProgress(ItemStack stack, EntityPlayer player)
	{
		int aiming = ItemNBTHelper.getInt(stack, AIMING);
		int fullTime = getAimingTime(stack, EasyNBT.wrapNBT(getUpgrades(stack)));

		return MathHelper.clamp(((aiming/(float)fullTime)-0.75f), 0, 0.25f)/0.25f;
	}

	@Override
	public float[] getZoomSteps(ItemStack stack, EntityPlayer player)
	{
		return new float[]{0.125f, 0.25f};
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ResourceLocation getZoomOverlayTexture(ItemStack stack, EntityPlayer player)
	{
		return OVERLAY_SCOPE;
	}
}
