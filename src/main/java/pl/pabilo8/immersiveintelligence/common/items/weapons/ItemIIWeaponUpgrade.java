package pl.pabilo8.immersiveintelligence.common.items.weapons;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.tool.IUpgrade;
import blusunrize.immersiveengineering.api.tool.IUpgradeableTool;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.render.MachinegunRenderer;
import pl.pabilo8.immersiveintelligence.client.render.item.SubmachinegunItemStackRenderer;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * @author Pabilo8
 * @since 01-11-2019
 */
public class ItemIIWeaponUpgrade extends ItemIIBase implements IUpgrade
{
	public ItemIIWeaponUpgrade()
	{
		super("weapon_upgrade", 1, WeaponUpgrades.parse());
	}

	public static TextFormatting getFormattingForWeapon(String weapon)
	{
		switch(weapon)
		{
			case "MACHINEGUN":
				return TextFormatting.YELLOW;
			case "SUBMACHINEGUN":
				return TextFormatting.GOLD;
			case "RAILGUN":
				return TextFormatting.DARK_GREEN;
			case "REVOLVER":
				return TextFormatting.BLUE;
			case "AUTOREVOLVER":
				return TextFormatting.DARK_BLUE;
			case "ASSAULT_RIFLE":
				return TextFormatting.RED;
			case "SPIGOT_MORTAR":
				return TextFormatting.DARK_PURPLE;
		}
		return TextFormatting.RESET;
	}

	public enum WeaponUpgrades
	{
		///Machinegun

		//Increases fire rate
		HEAVY_BARREL(ImmutableSet.of("MACHINEGUN"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("water_cooling"),
				(upgrade, modifications) -> modifications.setBoolean("heavy_barrel", true)),

		//Uses water to speed up gun cooling
		WATER_COOLING(ImmutableSet.of("MACHINEGUN"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("heavy_barrel"),
				(upgrade, modifications) -> modifications.setBoolean("water_cooling", true)),

		//Allows to load ammo from ammo crate below the player
		BELT_FED_LOADER(ImmutableSet.of("MACHINEGUN"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("second_magazine"),
				(upgrade, modifications) -> modifications.setBoolean("belt_fed_loader", true)),

		//Adds a second magazine, increases reload time a little bit
		SECOND_MAGAZINE(ImmutableSet.of("MACHINEGUN"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("belt_fed_loader"),
				(upgrade, modifications) -> modifications.setBoolean("second_magazine", true)),

		//Speeds up mg setting up time, but increases recoil
		HASTY_BIPOD(ImmutableSet.of("MACHINEGUN"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("precise_bipod"),
				(upgrade, modifications) -> modifications.setBoolean("hasty_bipod", true)),

		//Slows down mg setting up time, but decreases recoil
		PRECISE_BIPOD(ImmutableSet.of("MACHINEGUN"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("hasty_bipod"),
				(upgrade, modifications) -> modifications.setBoolean("precise_bipod", true)),

		//3 x Magnification
		SCOPE(ImmutableSet.of("MACHINEGUN", "AUTOREVOLVER", "ASSAULT_RIFLE"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("infrared_scope"),
				(upgrade, modifications) -> modifications.setBoolean("scope", true)),

		//Allows nightvision + 2 x magnification, uses energy from player's backpack
		INFRARED_SCOPE(ImmutableSet.of("MACHINEGUN", "ASSAULT_RIFLE"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("scope"),
				(upgrade, modifications) -> modifications.setBoolean("infrared_scope", true)),

		//Deflects projectiles
		SHIELD(ImmutableSet.of("MACHINEGUN"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("shield"),
				(upgrade, modifications) -> modifications.setBoolean("shield", true)),

		//Slows down mg setting up time, almost eliminates recoil, increases yaw and pitch angles
		TRIPOD(ImmutableSet.of("MACHINEGUN"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("tripod"),
				(upgrade, modifications) -> modifications.setBoolean("tripod", true)),

		///Submachinegun

		//Adds a velocity, penetration and suppression boost, lowers the firerate
		STURDY_BARREL(ImmutableSet.of("SUBMACHINEGUN"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("sturdy_barrel"),
				(upgrade, modifications) -> modifications.setBoolean("sturdy_barrel", true)),

		//Makes gunshots (almost) silent
		SUPPRESSOR(ImmutableSet.of("SUBMACHINEGUN"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("suppressor"),
				(upgrade, modifications) -> modifications.setBoolean("suppressor", true)),

		//Allows using drum magazines
		BOTTOM_LOADING(ImmutableSet.of("SUBMACHINEGUN"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("bottom_loading"),
				(upgrade, modifications) -> modifications.setBoolean("bottom_loading", true)),

		//Reduces aiming time
		FOLDING_STOCK(ImmutableSet.of("SUBMACHINEGUN"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("folding_stock"),
				(upgrade, modifications) -> modifications.setBoolean("folding_stock", true));

		/*

		///Autorevolver

		//Increases velocity, penetration and suppression
		HEAVY_SPRINGBOX(ImmutableSet.of("AUTOREVOLVER"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("heavy_springbox"),
				(upgrade, modifications) -> modifications.setBoolean("heavy_springbox", true)),

		//Storm Rifle

		//Shows yaw and pitch, allows to send a packet with player's yaw and pitch (+distance if a rangefinder is installed, +position data if player has a radio backpack)
		RADIO_MARKER(ImmutableSet.of("ASSAULT_RIFLE"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("radio_marker"),
				(upgrade, modifications) -> modifications.setBoolean("radio_marker", true)),

		//Allows shooting railgun grenades at a lower range, requires energy
		RIFLE_GRENADE_LAUNCHER(ImmutableSet.of("ASSAULT_RIFLE"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("stereoscopic_rangefinder"),
				(upgrade, modifications) -> modifications.setBoolean("rifle_grenade_launcher", true)),

		//Shows distance to target
		STEREOSCOPIC_RANGEFINDER(ImmutableSet.of("ASSAULT_RIFLE"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("rifle_grenade_launcher"),
				(upgrade, modifications) -> modifications.setBoolean("stereoscopic_rangefinder", true));

		 */

		private ImmutableSet<String> toolset;
		private int stackSize = 1;
		private BiPredicate<ItemStack, ItemStack> applyCheck;
		private BiConsumer<ItemStack, NBTTagCompound> function;

		WeaponUpgrades(ImmutableSet<String> toolset, BiConsumer<ItemStack, NBTTagCompound> function)
		{
			this(toolset, 1, function);
		}

		WeaponUpgrades(ImmutableSet<String> toolset, int stackSize, BiConsumer<ItemStack, NBTTagCompound> function)
		{
			this(toolset, stackSize, null, function);
		}

		WeaponUpgrades(ImmutableSet<String> toolset, int stackSize, BiPredicate<ItemStack, ItemStack> applyCheck, BiConsumer<ItemStack, NBTTagCompound> function)
		{
			this.toolset = toolset;
			this.stackSize = stackSize;
			this.applyCheck = applyCheck;
			this.function = function;
		}

		static String[] parse()
		{
			String[] ret = new String[values().length];
			for(int i = 0; i < ret.length; i++)
				ret[i] = values()[i].toString().toLowerCase(Locale.US);
			return ret;
		}

		static WeaponUpgrades get(int meta)
		{
			if(meta >= 0&&meta < values().length)
				return values()[meta];
			return HEAVY_BARREL;
		}
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		if(stack.getItemDamage() < getSubNames().length)
		{
			for(String upgradeType : this.getUpgradeTypes(stack))
				list.add(getFormattingForWeapon(upgradeType)+I18n.format(CommonProxy.DESCRIPTION_KEY+"toolupgrade.item."+upgradeType.toLowerCase()));
			String[] flavour = ImmersiveEngineering.proxy.splitStringOnWidth(I18n.format(CommonProxy.DESCRIPTION_KEY+"toolupgrade."+this.getSubNames()[stack.getItemDamage()]), 200);
			list.addAll(Arrays.asList(flavour));
		}
	}

	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return WeaponUpgrades.get(stack.getMetadata()).stackSize;
	}

	@Override
	public Set<String> getUpgradeTypes(ItemStack upgrade)
	{
		return WeaponUpgrades.get(upgrade.getMetadata()).toolset;
	}

	@Override
	public boolean canApplyUpgrades(ItemStack target, ItemStack upgrade)
	{
		BiPredicate<ItemStack, ItemStack> check = WeaponUpgrades.get(upgrade.getMetadata()).applyCheck;
		if(check!=null&&target.getItem() instanceof IUpgradeableTool)
			return check.test(target, upgrade);
		return true;
	}

	@Override
	public void applyUpgrades(ItemStack target, ItemStack upgrade, NBTTagCompound modifications)
	{
		WeaponUpgrades.get(upgrade.getMetadata()).function.accept(upgrade, modifications);
	}

	public static void addUpgradesToRender()
	{
		//mg

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("water_cooling")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(MachinegunRenderer.model.barrelBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.waterCoolingBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("heavy_barrel")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(MachinegunRenderer.model.barrelBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.heavyBarrelBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("second_magazine")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.add(MachinegunRenderer.model.secondMagazineMainBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.secondMagazineMagBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("belt_fed_loader")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(MachinegunRenderer.model.ammoBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.beltFedLoaderBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("scope")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.add(MachinegunRenderer.model.scopeBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("infrared_scope")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.add(MachinegunRenderer.model.infraredScopeBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("hasty_bipod")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(MachinegunRenderer.model.bipodBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.hastyBipodBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("precise_bipod")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(MachinegunRenderer.model.bipodBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.preciseBipodBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("tripod")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(MachinegunRenderer.model.bipodBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.tripodBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (IIContent.itemMachinegun.getUpgrades(stack).getBoolean("shield")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.add(MachinegunRenderer.model.shieldBox);
				}
		);

		//smg

		SubmachinegunItemStackRenderer.upgrades.put(
				stack -> (IIContent.itemSubmachinegun.getUpgrades(stack).getBoolean("sturdy_barrel")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(SubmachinegunItemStackRenderer.model.barrelBox);
					tmtNamedBoxGroups.add(SubmachinegunItemStackRenderer.model.sturdyBarrelBox);
				}
		);

		SubmachinegunItemStackRenderer.upgrades.put(
				stack -> (IIContent.itemSubmachinegun.getUpgrades(stack).getBoolean("suppressor")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.add(SubmachinegunItemStackRenderer.model.silencerBox);
				}
		);

		SubmachinegunItemStackRenderer.upgrades.put(
				stack -> (IIContent.itemSubmachinegun.getUpgrades(stack).getBoolean("bottom_loading")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(SubmachinegunItemStackRenderer.model.ammoBox);
					tmtNamedBoxGroups.remove(SubmachinegunItemStackRenderer.model.gripBox);
					tmtNamedBoxGroups.remove(SubmachinegunItemStackRenderer.model.loaderBox);

					tmtNamedBoxGroups.add(SubmachinegunItemStackRenderer.model.bottomLoaderBox);
				}
		);
	}
}
