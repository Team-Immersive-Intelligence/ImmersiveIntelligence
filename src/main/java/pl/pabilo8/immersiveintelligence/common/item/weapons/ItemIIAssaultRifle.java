package pl.pabilo8.immersiveintelligence.common.item.weapons;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEEnergyItem;
import blusunrize.immersiveengineering.common.util.IESounds;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IItemScrollable;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedZoomTool;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.AssaultRifle;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrades;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandler;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandlerMagazine;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandlerSingle;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.RangedSound;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 17-09-2022
 */
public class ItemIIAssaultRifle extends ItemIIGunBase implements IItemScrollable, IAdvancedZoomTool, IIEEnergyItem
{
	//--- NBT Values Reference ---//
	public static final String FIRE_MODE = "fire_mode";
	public static final String LAST_FIRE_MODE = "last_mode";
	public static final String FIRE_MODE_TIMER = "mode_switch";
	public static final String LOADED_GRENADE = "grenade";
	public static final String ENERGY_UPGRADED = "energy";

	//--- Scope Overlay Textures ---//
	public static final ResourceLocation OVERLAY_SCOPE = new ResourceLocation(ImmersiveIntelligence.MODID,
			"textures/gui/item/machinegun/scope.png");
	public static final ResourceLocation OVERLAY_SCOPE_IR = new ResourceLocation(ImmersiveIntelligence.MODID,
			"textures/gui/item/machinegun/scope_infrared.png");

	//--- Ammunition Handlers ---//
	private final AmmoHandlerMagazine stgAmmoHandler;
	private final AmmoHandlerSingle railgunAmmoHandler;

	public ItemIIAssaultRifle()
	{
		super("assault_rifle");
		stgAmmoHandler = new AmmoHandlerMagazine(this, MAGAZINE, IIContent.itemAmmoAssaultRifle)
		{
			@Nullable
			@Override
			protected SoundEvent getUnloadSound(ItemStack weapon, EasyNBT nbt)
			{
				return IISounds.assaultRifleUnload;
			}

			@Nullable
			@Override
			protected SoundEvent getReloadSound(ItemStack weapon, EasyNBT nbt)
			{
				return IISounds.assaultRifleReload;
			}
		};

		railgunAmmoHandler = new AmmoHandlerSingle(this, LOADED_GRENADE, IIContent.itemRailgunGrenade)
		{
			@Nullable
			@Override
			protected SoundEvent getUnloadSound(ItemStack weapon, EasyNBT nbt)
			{
				return null;
			}

			@Nullable
			@Override
			protected SoundEvent getReloadSound(ItemStack weapon, EasyNBT nbt)
			{
				return IISounds.assaultRifleLoadGrenade;
			}
		};
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, world, entity, itemSlot, isSelected);

		if(isSelected)
		{
			//decrease mode switch timer value
			EasyNBT nbt = EasyNBT.wrapNBT(ItemNBTHelper.getTag(stack));
			nbt.checkSetInt(FIRE_MODE_TIMER, i -> nbt.withInt(FIRE_MODE_TIMER, Math.max(i-1, 0)));

			if(hasIIUpgrade(stack, WeaponUpgrades.GYROSCOPIC_STABILIZER)&&getEnergyStored(stack) >= AssaultRifle.upgradeStabilizerEnergy)
			{
				if(ItemNBTHelper.getFloat(stack, RECOIL_H) > 0||ItemNBTHelper.getFloat(stack, RECOIL_V) > 0)
					extractEnergy(stack, AssaultRifle.upgradeStabilizerEnergy, false);
			}

			if(entity instanceof EntityLivingBase&&isAimed(stack)&&hasIIUpgrade(stack, WeaponUpgrades.INFRARED_SCOPE))
			{
				if(extractEnergy(stack, AssaultRifle.upgradeIRScopeEnergy, false) > 0)
					((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 4, 1, true, false));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> list, @Nonnull ITooltipFlag flag)
	{
		if(isEnergyUpgraded(stack))
		{
			list.add(IIUtils.getItalicString(I18n.format(IIReference.INFO_KEY+"charge_with_if")));
			list.add(I18n.format(Lib.DESC+"info.energyStored", TextFormatting.GOLD+String.valueOf(getEnergyStored(stack))+TextFormatting.RESET));
			list.add("");
		}

		super.addInformation(stack, world, list, flag);
	}

	@Override
	public AmmoHandler getAmmoHandler(ItemStack weapon)
	{
		return ItemNBTHelper.getInt(weapon, FIRE_MODE) < 2?stgAmmoHandler: railgunAmmoHandler;
	}

	@Override
	protected FireModeType getFireMode(ItemStack weapon)
	{
		switch(ItemNBTHelper.getInt(weapon, FIRE_MODE))
		{
			case 2:
				return FireModeType.SINGULAR_CHARGED;
			case 1:
				return FireModeType.SINGULAR;
			default:
				return FireModeType.AUTOMATIC;
		}
	}

	//--- ItemIIGunBase ---//

	@Override
	public int getSlotCount(ItemStack stack)
	{
		return 3;
	}

	@Override
	protected double getEquipSpeed(ItemStack weapon, EasyNBT nbt)
	{
		return 0.75;
	}

	@Override
	protected void createProjectile(EntityLivingBase user, World world, Vec3d dir, Vec3d pos, ItemStack weapon, EasyNBT nbt, ItemStack ammo)
	{
		int stored = getEnergyStored(weapon);

		if(hasIIUpgrade(weapon, WeaponUpgrades.ELECTRIC_FIRING_MOTOR)&&stored >= AssaultRifle.upgradeFiringMotorEnergy)
		{
			world.playSound(null, pos.x, pos.y, pos.z, IESounds.spark, SoundCategory.PLAYERS, 0.5f, 1.5f);
			extractEnergy(weapon, AssaultRifle.upgradeFiringMotorEnergy, false);
		}
		if(hasIIUpgrade(weapon, WeaponUpgrades.RAILGUN_ASSISTED_CHAMBER)&&stored >= AssaultRifle.upgradeRailgunChamberEnergy)
		{
			world.playSound(null, pos.x, pos.y, pos.z, IESounds.railgunFire, SoundCategory.PLAYERS, 0.5f, 1f);
			extractEnergy(weapon, AssaultRifle.upgradeRailgunChamberEnergy, false);
		}

		super.createProjectile(user, world, dir, pos, weapon, nbt, ammo);
	}

	@Override
	protected float getVelocityModifier(ItemStack weapon, EasyNBT nbt, ItemStack ammo)
	{
		switch(ItemNBTHelper.getInt(weapon, FIRE_MODE))
		{
			case 0:
				return hasIIUpgrade(weapon, WeaponUpgrades.ELECTRIC_FIRING_MOTOR)
						&&getEnergyStored(weapon) >= AssaultRifle.upgradeFiringMotorEnergy?0.75f: 1f;
			case 1:
				return hasIIUpgrade(weapon, WeaponUpgrades.RAILGUN_ASSISTED_CHAMBER)
						&&getEnergyStored(weapon) >= AssaultRifle.upgradeRailgunChamberEnergy?1.5f: 1.25f;
			case 2:
				return 0.55f;
		}
		return 1f;
	}

	@Override
	public int getFireDelay(ItemStack weapon, EasyNBT nbt)
	{
		switch(ItemNBTHelper.getInt(weapon, FIRE_MODE))
		{
			case 0:
			{
				if(hasIIUpgrade(weapon, WeaponUpgrades.ELECTRIC_FIRING_MOTOR)&&getEnergyStored(weapon) >= AssaultRifle.upgradeFiringMotorEnergy)
					return 1;
				return AssaultRifle.bulletFireTimeAuto;
			}
			case 1:
			{
				if(hasIIUpgrade(weapon, WeaponUpgrades.RAILGUN_ASSISTED_CHAMBER)) //penalty still applied
					return 10;
				return AssaultRifle.bulletFireTimeSemiAuto;
			}
			case 2:
				return 40;
		}
		return 0;
	}

	@Override
	public float getRecoilDecay(ItemStack weapon, EasyNBT nbt, boolean isAimed)
	{
		if(hasIIUpgrade(weapon, WeaponUpgrades.GYROSCOPIC_STABILIZER)&&getEnergyStored(weapon) >= AssaultRifle.upgradeStabilizerEnergy)
			return isAimed?0.65f: 0.2f;
		return super.getRecoilDecay(weapon, nbt, isAimed);
	}

	@Nullable
	@Override
	protected SoundEvent getDryfireSound(ItemStack weapon, EasyNBT nbt)
	{
		return IISounds.assaultRifleShotDry;
	}

	@Nullable
	@Override
	protected RangedSound getFireSound(ItemStack weapon, EasyNBT nbt)
	{
		return ItemNBTHelper.getInt(weapon, FIRE_MODE)==2?IISounds.assaultRifleRailgunShot: IISounds.assaultRifleShot;
	}

	@Nullable
	@Override
	protected SoundEvent getChargeFireSound(ItemStack weapon, EasyNBT nbt)
	{
		return IISounds.assaultRifleRailgunCharge;
	}

	@Override
	protected int getEnemyAttractRange(ItemStack weapon, EasyNBT nbt)
	{
		return AssaultRifle.enemyAttractRange;
	}

	@Override
	public int getAimingTime(ItemStack weapon, EasyNBT nbt)
	{
		return AssaultRifle.aimTime;
	}

	@Override
	public int getReloadTime(ItemStack weapon, ItemStack loaded, EasyNBT upgrades)
	{
		if(ItemNBTHelper.getInt(weapon, FIRE_MODE)==2)
			return AssaultRifle.grenadeReloadTime;
		return AssaultRifle.clipReloadTime;

	}

	@Override
	public float getHorizontalRecoil(ItemStack weapon, EasyNBT nbt, boolean isAimed)
	{
		return AssaultRifle.recoilHorizontal;
	}

	@Override
	public float getVerticalRecoil(ItemStack weapon, EasyNBT nbt, boolean isAimed)
	{
		return AssaultRifle.recoilVertical;
	}

	@Override
	public float getMaxHorizontalRecoil(ItemStack weapon, EasyNBT nbt)
	{
		return 10F;
	}

	@Override
	public float getMaxVerticalRecoil(ItemStack weapon, EasyNBT nbt)
	{
		return 10F;
	}

	@Override
	protected float getGunfireParticleSize(ItemStack weapon, EasyNBT nbt)
	{
		return 1.5f;
	}

	//--- Custom ---//

	@Override
	public void removeFromWorkbench(EntityPlayer player, ItemStack stack)
	{
		if(hasIIUpgrade(stack, WeaponUpgrades.ELECTRIC_FIRING_MOTOR)&&
				hasIIUpgrade(stack, WeaponUpgrades.SCOPE, WeaponUpgrades.INFRARED_SCOPE))
			IIUtils.unlockIIAdvancement(player, "main/weapon_of_war");
		if(hasIIUpgrades(stack, WeaponUpgrades.RAILGUN_ASSISTED_CHAMBER, WeaponUpgrades.RIFLE_GRENADE_LAUNCHER))
			IIUtils.unlockIIAdvancement(player, "main/the_accelerator");
		if(hasIIUpgrade(stack, WeaponUpgrades.STEREOSCOPIC_RANGEFINDER))
			IIUtils.unlockIIAdvancement(player, "main/special_operations_initiative");
	}

	//--- IItemScrollable ---//

	@Override
	public void onScroll(ItemStack stack, boolean forward, EntityPlayerMP player)
	{
		if(ItemNBTHelper.getInt(stack, RELOADING)==0&&ItemNBTHelper.getInt(stack, FIRE_MODE_TIMER)==0)
		{
			int mode = ItemNBTHelper.getInt(stack, FIRE_MODE);
			int switched = MathHelper.clamp(mode+(forward?1: -1), 0,
					(getUpgrades(stack).hasKey(WeaponUpgrades.RIFLE_GRENADE_LAUNCHER.getName())?2: 1)
			);

			if(switched!=mode)
			{
				ItemNBTHelper.setInt(stack, LAST_FIRE_MODE, mode);
				ItemNBTHelper.setInt(stack, FIRE_MODE, switched);
				ItemNBTHelper.setInt(stack, FIRE_MODE_TIMER, 6);
				player.world.playSound(null, player.posX, player.posY, player.posZ, IISounds.assaultRifleModeChange, SoundCategory.PLAYERS, 0.25f, 1f);
			}
		}
	}

	//--- IAdvancedZoomTool ---//

	@Override
	public boolean shouldZoom(ItemStack stack, EntityPlayer player)
	{
		boolean isAimed = ItemNBTHelper.getInt(stack, AIMING) > getAimingTime(stack, EasyNBT.wrapNBT(getUpgrades(stack)))*0.75;
		return isAimed&&isScoped(stack);
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
		return new float[]{0.45f};
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ResourceLocation getZoomOverlayTexture(ItemStack stack, EntityPlayer player)
	{
		return hasIIUpgrade(stack, WeaponUpgrades.SCOPE)?OVERLAY_SCOPE: OVERLAY_SCOPE_IR;
	}

	//--- Utility Methods ---//

	public boolean isScoped(ItemStack stack)
	{
		return hasIIUpgrade(stack, WeaponUpgrades.SCOPE, WeaponUpgrades.INFRARED_SCOPE);
	}

	private boolean isEnergyUpgraded(ItemStack stack)
	{
		return getUpgrades(stack).hasKey(ENERGY_UPGRADED);
	}

	@Override
	public int getMaxEnergyStored(ItemStack stack)
	{
		return isEnergyUpgraded(stack)?4000: 0;
	}
}
