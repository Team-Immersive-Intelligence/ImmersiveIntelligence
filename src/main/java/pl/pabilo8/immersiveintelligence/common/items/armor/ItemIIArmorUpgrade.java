package pl.pabilo8.immersiveintelligence.common.items.armor;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.tool.IUpgrade;
import blusunrize.immersiveengineering.api.tool.IUpgradeableTool;
import blusunrize.immersiveengineering.common.gui.IESlot.Upgrades;
import blusunrize.immersiveengineering.common.items.ItemUpgradeableTool;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
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
 * @since 21.04.2021
 */
public class ItemIIArmorUpgrade extends ItemIIBase implements IUpgrade
{
	public ItemIIArmorUpgrade()
	{
		super("armor_upgrade", 1, ArmorUpgrades.parse());
		setMetaHidden(ArmorUpgrades.HELIPACK.ordinal());
	}

	public enum ArmorUpgrades
	{
		/**
		 * HELMET ONLY
		 */

		//Protects from gases
		GASMASK(ImmutableSet.of("LIGHT_ENGINEER_HELMET"), 1,
				(target, upgrade) -> true,
				(upgrade, modifications) -> modifications.setBoolean("gasmask", true)),

		INFILTRATOR_GEAR(ImmutableSet.of("LIGHT_ENGINEER_HELMET"), 1,
				(target, upgrade) -> !IIContent.itemLightEngineerHelmet.getUpgrades(target).hasKey("headgear"),
				(upgrade, modifications) -> {
					modifications.setBoolean("infiltrator_gear", true);
					modifications.setBoolean("headgear", true);
				}),

		TECHNICIAN_GEAR(ImmutableSet.of("LIGHT_ENGINEER_HELMET"), 1,
				(target, upgrade) -> !IIContent.itemLightEngineerHelmet.getUpgrades(target).hasKey("headgear"),
				(upgrade, modifications) -> {
					modifications.setBoolean("technician_gear", true);
					modifications.setBoolean("headgear", true);
				}),

		ENGINEER_GEAR(ImmutableSet.of("LIGHT_ENGINEER_HELMET"), 1,
				(target, upgrade) -> !IIContent.itemLightEngineerHelmet.getUpgrades(target).hasKey("headgear"),
				(upgrade, modifications) -> {
					modifications.setBoolean("engineer_gear", true);
					modifications.setBoolean("headgear", true);
				}),

		/**
		 * ALL PARTS
		 */

		//Adds 1/3 armor thickness point, Deflects arrows, -5% speed per part
		STEEL_ARMOR_PLATES(ImmutableSet.of("LIGHT_ENGINEER_HELMET", "LIGHT_ENGINEER_CHESTPLATE", "LIGHT_ENGINEER_LEGGINGS"), 3,
				(target, upgrade) -> !IIContent.itemLightEngineerHelmet.getUpgrades(target).hasKey("composite_plates"),
				(upgrade, modifications) -> {
					modifications.setBoolean("steel_plates", true);
					modifications.setDouble("toughness_increase", upgrade.getCount()*0.5);
					modifications.setDouble("armor_increase", upgrade.getCount());
				}),

		//Adds 1 armor thickness point, Deflects arrows, blocks damage with base <8+((i-1)*(amount*0.75)), makes the weapon break, -2.5% speed per part
		COMPOSITE_ARMOR_PLATES(ImmutableSet.of("LIGHT_ENGINEER_HELMET", "LIGHT_ENGINEER_CHESTPLATE", "LIGHT_ENGINEER_LEGGINGS"), 3,
				(target, upgrade) -> !IIContent.itemLightEngineerHelmet.getUpgrades(target).hasKey("steel_plates"),
				(upgrade, modifications) -> {
					modifications.setBoolean("composite_plates", true);
					modifications.setDouble("toughness_increase", upgrade.getCount());
					modifications.setDouble("armor_increase", upgrade.getCount());
				}),

		//Protects from radiation
		HAZMAT_COATING(ImmutableSet.of("LIGHT_ENGINEER_HELMET", "LIGHT_ENGINEER_CHESTPLATE", "LIGHT_ENGINEER_LEGGINGS", "LIGHT_ENGINEER_BOOTS"), 1,
				(target, upgrade) -> true,
				(upgrade, modifications) -> modifications.setBoolean("hazmat", true)),

		/**
		 * CHESTPLATE ONLY
		 */

		HEAT_RESISTANT_COATING(ImmutableSet.of("LIGHT_ENGINEER_CHESTPLATE"), 1,
				(target, upgrade) -> true,
				(upgrade, modifications) -> modifications.setBoolean("heat_coating", true)),

		ANTI_STATIC_MESH(ImmutableSet.of("LIGHT_ENGINEER_CHESTPLATE"), 1,
				(target, upgrade) -> !IIContent.itemLightEngineerHelmet.getUpgrades(target).hasKey("mesh"),
				(upgrade, modifications) -> {
					modifications.setBoolean("anti_static_mesh", true);
					modifications.setBoolean("mesh", true);
				}),

		CAMOUFLAGE_MESH(ImmutableSet.of("LIGHT_ENGINEER_CHESTPLATE"), 1,
				(target, upgrade) -> !IIContent.itemLightEngineerHelmet.getUpgrades(target).hasKey("mesh"),
				(upgrade, modifications) -> {
					modifications.setBoolean("camo_mesh", true);
					modifications.setBoolean("mesh", true);
				}),

		INFRARED_ABSORBING_MESH(ImmutableSet.of("LIGHT_ENGINEER_CHESTPLATE"), 1,
				(target, upgrade) -> !IIContent.itemLightEngineerHelmet.getUpgrades(target).hasKey("mesh"),
				(upgrade, modifications) -> {
					modifications.setBoolean("ir_mesh", true);
					modifications.setBoolean("mesh", true);
				}),

		SCUBA_TANK(ImmutableSet.of("LIGHT_ENGINEER_CHESTPLATE"), 1,
				(target, upgrade) -> true,
				(upgrade, modifications) -> {
					modifications.setBoolean("scuba", true);
				}),

		HELIPACK(ImmutableSet.of("LIGHT_ENGINEER_CHESTPLATE"), 1,
				(target, upgrade) -> true,
				(upgrade, modifications) -> {
					modifications.setBoolean("helipack", true);
				}),

		/**
		 * LEGGINGS ONLY
		 */

		EXOSKELETON(ImmutableSet.of("LIGHT_ENGINEER_LEGGINGS"), 1,
				(target, upgrade) -> true,
				(upgrade, modifications) -> modifications.setBoolean("exoskeleton", true)),

		/**
		 * BOOTS ONLY
		 */

		BOOT_REINFORCEMENT(ImmutableSet.of("LIGHT_ENGINEER_BOOTS"), 1,
				(target, upgrade) -> true,
				(upgrade, modifications) -> {
					modifications.setBoolean("reinforced", true);
					modifications.setDouble("toughness_increase", 1);
					modifications.setDouble("armor_increase", 1);
				}),

		SNOW_RACKETS(ImmutableSet.of("LIGHT_ENGINEER_BOOTS"), 1,
				(target, upgrade) -> !IIContent.itemLightEngineerHelmet.getUpgrades(target).hasKey("flippers"),
				(upgrade, modifications) -> modifications.setBoolean("snow_rackets", true)),

		FLIPPERS(ImmutableSet.of("LIGHT_ENGINEER_BOOTS"), 1,
				(target, upgrade) -> !IIContent.itemLightEngineerHelmet.getUpgrades(target).hasKey("snow_rackets"),
				(upgrade, modifications) -> modifications.setBoolean("flippers", true)),

		INTERNAL_SPRINGS(ImmutableSet.of("LIGHT_ENGINEER_BOOTS"), 1,
				(target, upgrade) -> true,
				(upgrade, modifications) -> modifications.setBoolean("springs", true));

		private ImmutableSet<String> toolset;
		private int stackSize = 1;
		private BiPredicate<ItemStack, ItemStack> applyCheck;
		private BiConsumer<ItemStack, NBTTagCompound> function;

		ArmorUpgrades(ImmutableSet<String> toolset, BiConsumer<ItemStack, NBTTagCompound> function)
		{
			this(toolset, 1, function);
		}

		ArmorUpgrades(ImmutableSet<String> toolset, int stackSize, BiConsumer<ItemStack, NBTTagCompound> function)
		{
			this(toolset, stackSize, null, function);
		}

		ArmorUpgrades(ImmutableSet<String> toolset, int stackSize, BiPredicate<ItemStack, ItemStack> applyCheck, BiConsumer<ItemStack, NBTTagCompound> function)
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

		static ArmorUpgrades get(int meta)
		{
			if(meta >= 0&&meta < values().length)
				return values()[meta];
			return GASMASK;
		}

		public boolean isValidFor(String weapon)
		{
			return toolset.contains(weapon);
		}
	}

	public static TextFormatting getFormattingForWeapon(String weapon)
	{
		switch(weapon)
		{
			case "LIGHT_ENGINEER_HELMET":
				return TextFormatting.GOLD;
			case "LIGHT_ENGINEER_CHESTPLATE":
				return TextFormatting.YELLOW;
			case "LIGHT_ENGINEER_LEGGINGS":
				return TextFormatting.GREEN;
			case "LIGHT_ENGINEER_BOOTS":
				return TextFormatting.DARK_GREEN;
		}
		return TextFormatting.RESET;
	}

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
		return ArmorUpgrades.get(stack.getMetadata()).stackSize;
	}

	@Override
	public Set<String> getUpgradeTypes(ItemStack upgrade)
	{
		return ArmorUpgrades.get(upgrade.getMetadata()).toolset;
	}

	@Override
	public boolean canApplyUpgrades(ItemStack target, ItemStack upgrade)
	{
		BiPredicate<ItemStack, ItemStack> check = ArmorUpgrades.get(upgrade.getMetadata()).applyCheck;
		if(check!=null&&target.getItem() instanceof IUpgradeableTool)
			return check.test(target, upgrade);
		return true;
	}

	@Override
	public void applyUpgrades(ItemStack target, ItemStack upgrade, NBTTagCompound modifications)
	{
		ArmorUpgrades.get(upgrade.getMetadata()).function.accept(upgrade, modifications);
	}
}
