package pl.pabilo8.immersiveintelligence.common.item.weapons;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IItemScrollable;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Pistol;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandler;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ammohandler.AmmoHandlerMagazine;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.RangedSound;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 17-09-2022
 */
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIPistol extends ItemIIGunBase implements IItemScrollable
{
	//--- NBT Values Reference ---//
	public static final String FIRE_MODE = "fire_mode";
	public static final String LAST_FIRE_MODE = "last_mode";
	public static final String FIRE_MODE_TIMER = "mode_switch";

	//--- Ammunition Handlers ---//
	private final AmmoHandlerMagazine ammoHandler, ammoHandler2;

	public ItemIIPistol()
	{
		super("pistol");
		ammoHandler = new AmmoHandlerMagazine(this, MAGAZINE, IIContent.itemAmmoSubmachinegun)
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

		ammoHandler2 = new AmmoHandlerMagazine(this, MAGAZINE, IIContent.itemAmmoPistol)
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
		}
	}

	@Override
	public AmmoHandler getAmmoHandler(ItemStack weapon)
	{
		return hasIIUpgrade(weapon, WeaponUpgrade.SMG_MAG_CONVERTER_KIT) ? ammoHandler : ammoHandler2;
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

	//@Override
	//protected float getVelocityModifier(ItemStack weapon, EasyNBT nbt, ItemStack ammo)
	//{
	//	return 1f;
	//}

	@Override
	public int getFireDelay(ItemStack weapon, EasyNBT nbt)
	{
		switch(ItemNBTHelper.getInt(weapon, FIRE_MODE))
		{
			case 0:
			{
				return Pistol.bulletFireTimeAuto;
			}
			case 1:
			{
				return Pistol.bulletFireTimeSemiAuto;
			}
			case 2:
				return 40;
		}
		return 0;
	}

	@Override
	public float getRecoilDecay(ItemStack weapon, EasyNBT nbt, boolean isAimed)
	{
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
		return Pistol.enemyAttractRange;
	}

	@Override
	public int getAimingTime(ItemStack weapon, EasyNBT nbt)
	{
		return Pistol.aimTime;
	}

	@Override
	public int getReloadTime(ItemStack weapon, ItemStack loaded, EasyNBT upgrades)
	{
		return Pistol.clipReloadTime;

	}

	@Override
	public float getHorizontalRecoil(ItemStack weapon, EasyNBT nbt, boolean isAimed)
	{
		return Pistol.recoilHorizontal;
	}

	@Override
	public float getVerticalRecoil(ItemStack weapon, EasyNBT nbt, boolean isAimed)
	{
		return Pistol.recoilVertical;
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

	//--- IItemScrollable ---//

	@Override
	public void onScroll(ItemStack stack, boolean forward, EntityPlayerMP player)
	{
		if(ItemNBTHelper.getInt(stack, RELOADING)==0&&ItemNBTHelper.getInt(stack, FIRE_MODE_TIMER)==0)
		{
			int mode = ItemNBTHelper.getInt(stack, FIRE_MODE);
			int switched = MathHelper.clamp(mode+(forward?1: -1), 0,
					(getUpgrades(stack).hasKey(WeaponUpgrade.RIFLE_GRENADE_LAUNCHER.getName())?2: 1)
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

	public boolean isScoped(ItemStack stack)
	{
		return hasIIUpgrade(stack, WeaponUpgrade.SCOPE);
	}
}

