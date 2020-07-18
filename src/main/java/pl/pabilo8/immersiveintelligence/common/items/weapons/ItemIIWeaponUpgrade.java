package pl.pabilo8.immersiveintelligence.common.items.weapons;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.tool.IUpgrade;
import blusunrize.immersiveengineering.api.tool.IUpgradeableTool;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.render.MachinegunRenderer;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;

import javax.annotation.Nullable;
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

	public enum WeaponUpgrades
	{
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
		SCOPE(ImmutableSet.of("MACHINEGUN"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("infrared_scope"),
				(upgrade, modifications) -> modifications.setBoolean("scope", true)),

		//Allows nightvision + 2 x magnification, uses energy from player's backpack
		INFRARED_SCOPE(ImmutableSet.of("MACHINEGUN"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("scope"),
				(upgrade, modifications) -> modifications.setBoolean("infrared_scope", true)),

		//Allows nightvision + 2 x magnification, uses energy from player's backpack
		SHIELD(ImmutableSet.of("MACHINEGUN"), 1,
				(target, upgrade) -> !((IUpgradeableTool)target.getItem()).getUpgrades(target).hasKey("shield"),
				(upgrade, modifications) -> modifications.setBoolean("shield", true));


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
			String[] flavour = ImmersiveEngineering.proxy.splitStringOnWidth(I18n.format(ImmersiveIntelligence.proxy.DESCRIPTION_KEY+"toolupgrade."+this.getSubNames()[stack.getItemDamage()]), 200);
			for(String s : flavour)
				list.add(s);
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
		MachinegunRenderer.upgrades.put(
				stack -> (ImmersiveIntelligence.proxy.item_machinegun.getUpgrades(stack).getBoolean("water_cooling")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(MachinegunRenderer.model.barrelBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.waterCoolingBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (ImmersiveIntelligence.proxy.item_machinegun.getUpgrades(stack).getBoolean("heavy_barrel")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(MachinegunRenderer.model.barrelBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.heavyBarrelBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (ImmersiveIntelligence.proxy.item_machinegun.getUpgrades(stack).getBoolean("second_magazine")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.add(MachinegunRenderer.model.secondMagazineMainBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.secondMagazineMagBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (ImmersiveIntelligence.proxy.item_machinegun.getUpgrades(stack).getBoolean("belt_fed_loader")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(MachinegunRenderer.model.ammoBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.beltFedLoaderBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (ImmersiveIntelligence.proxy.item_machinegun.getUpgrades(stack).getBoolean("scope")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.add(MachinegunRenderer.model.scopeBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (ImmersiveIntelligence.proxy.item_machinegun.getUpgrades(stack).getBoolean("infrared_scope")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.add(MachinegunRenderer.model.infraredScopeBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (ImmersiveIntelligence.proxy.item_machinegun.getUpgrades(stack).getBoolean("hasty_bipod")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(MachinegunRenderer.model.bipodBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.hastyBipodBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (ImmersiveIntelligence.proxy.item_machinegun.getUpgrades(stack).getBoolean("precise_bipod")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.remove(MachinegunRenderer.model.bipodBox);
					tmtNamedBoxGroups.add(MachinegunRenderer.model.preciseBipodBox);
				}
		);

		MachinegunRenderer.upgrades.put(
				stack -> (ImmersiveIntelligence.proxy.item_machinegun.getUpgrades(stack).getBoolean("shield")),
				(stack, tmtNamedBoxGroups) ->
				{
					tmtNamedBoxGroups.add(MachinegunRenderer.model.shieldBox);
				}
		);
	}
}
