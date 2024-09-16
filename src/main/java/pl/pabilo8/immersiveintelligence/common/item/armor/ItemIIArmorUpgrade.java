package pl.pabilo8.immersiveintelligence.common.item.armor;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.tool.IUpgrade;
import blusunrize.immersiveengineering.api.tool.IUpgradeableTool;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.item.armor.ItemIIArmorUpgrade.ArmorUpgrades;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @since 21.04.2021
 */
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIArmorUpgrade extends ItemIISubItemsBase<ArmorUpgrades> implements IUpgrade
{
	public ItemIIArmorUpgrade()
	{
		super("armor_upgrade", 1, ArmorUpgrades.values());
	}

	/**
	 * Describes armors and their types
	 */
	public enum ArmorTypes implements ISerializableEnum
	{
		LIGHT_ENGINEER_HELMET(0xff8840, '\u24be'),
		LIGHT_ENGINEER_CHESTPLATE(0xcc6c33, '\u24bf'),
		LIGHT_ENGINEER_LEGGINGS(0xff6440, '\u24c0'),
		LIGHT_ENGINEER_BOOTS(0xcc5033, '\u24c1');

		private final IIColor color;
		private final char symbol;

		ArmorTypes(int color, char symbol)
		{
			this.color = IIColor.fromPackedRGB(color);
			this.symbol = symbol;
		}
	}

	/**
	 * Contains upgrades
	 */
	@GeneratedItemModels(itemName = "armor_upgrade")
	public enum ArmorUpgrades implements IIItemEnum
	{
		//--- Helmet ---//

		//Protects from gasses
		GASMASK(ImmutableSet.of(ArmorTypes.LIGHT_ENGINEER_HELMET)),
		INFILTRATOR_GEAR(ImmutableSet.of(ArmorTypes.LIGHT_ENGINEER_HELMET),
				"infiltrator_gear", "engineer_gear"),
		TECHNICIAN_GEAR(ImmutableSet.of(ArmorTypes.LIGHT_ENGINEER_HELMET),
				"technician_gear", "engineer_gear"),
		ENGINEER_GEAR(ImmutableSet.of(ArmorTypes.LIGHT_ENGINEER_HELMET),
				"technician_gear", "infiltrator_gear"),

		//--- All Parts ---//

		//Adds 1/3 armor thickness point, Deflects arrows, -5% speed per part
		@IIItemProperties(stackSize = 3)
		STEEL_ARMOR_PLATES(
				ImmutableSet.of(ArmorTypes.LIGHT_ENGINEER_HELMET, ArmorTypes.LIGHT_ENGINEER_CHESTPLATE, ArmorTypes.LIGHT_ENGINEER_LEGGINGS),
				(upgrade, modifications) -> {
					modifications.setBoolean("steel_plates", true);
					modifications.setDouble("toughness_increase", upgrade.getCount()*0.5);
					modifications.setDouble("armor_increase", upgrade.getCount());
				},
				"composite_plates"),

		//Adds 1 armor thickness point, Deflects arrows, blocks damage with base <8+((i-1)*(amount*0.75)), makes the weapon break, -2.5% speed per part
		@IIItemProperties(stackSize = 3)
		COMPOSITE_ARMOR_PLATES(
				ImmutableSet.of(ArmorTypes.LIGHT_ENGINEER_HELMET, ArmorTypes.LIGHT_ENGINEER_CHESTPLATE, ArmorTypes.LIGHT_ENGINEER_LEGGINGS),
				(upgrade, modifications) -> {
					modifications.setBoolean("composite_plates", true);
					modifications.setDouble("toughness_increase", upgrade.getCount());
					modifications.setDouble("armor_increase", upgrade.getCount());
				},
				"steel_plates"),

		//Protects from radiation
		HAZMAT_COATING(
				ImmutableSet.of(ArmorTypes.LIGHT_ENGINEER_HELMET, ArmorTypes.LIGHT_ENGINEER_CHESTPLATE, ArmorTypes.LIGHT_ENGINEER_LEGGINGS, ArmorTypes.LIGHT_ENGINEER_BOOTS),
				(upgrade, modifications) -> modifications.setBoolean("hazmat", true)),

		//--- Chestplate ---//

		HEAT_RESISTANT_COATING(
				ImmutableSet.of(ArmorTypes.LIGHT_ENGINEER_CHESTPLATE),
				(upgrade, modifications) -> modifications.setBoolean("heatcoat", true)),

		ANTI_STATIC_MESH(
				ImmutableSet.of(ArmorTypes.LIGHT_ENGINEER_CHESTPLATE),
				"camo_mesh", "ir_mesh"),

		CAMOUFLAGE_MESH(
				ImmutableSet.of(ArmorTypes.LIGHT_ENGINEER_CHESTPLATE),
				(upgrade, modifications) -> modifications.setBoolean("camo_mesh", true),
				"anti_static_mesh", "ir_mesh"),

		INFRARED_ABSORBING_MESH(
				ImmutableSet.of(ArmorTypes.LIGHT_ENGINEER_CHESTPLATE),
				(upgrade, modifications) -> modifications.setBoolean("ir_mesh", true),
				"anti_static_mesh", "camo_mesh"),

		SCUBA_TANK(ImmutableSet.of(ArmorTypes.LIGHT_ENGINEER_CHESTPLATE),
				(upgrade, modifications) -> modifications.setBoolean("scuba", true)
		),

		@IIItemProperties(hidden = true, category = IICategory.WARFARE)
		HELIPACK(ImmutableSet.of(ArmorTypes.LIGHT_ENGINEER_CHESTPLATE)),

		//--- Leggings ---//

		EXOSKELETON(ImmutableSet.of(ArmorTypes.LIGHT_ENGINEER_LEGGINGS)),

		//--- Boots ---//

		BOOT_REINFORCEMENT(ImmutableSet.of(ArmorTypes.LIGHT_ENGINEER_BOOTS), "boot_reinforcement"),
		SNOW_RACKETS(ImmutableSet.of(ArmorTypes.LIGHT_ENGINEER_BOOTS), "snow_rackets"),
		FLIPPERS(ImmutableSet.of(ArmorTypes.LIGHT_ENGINEER_BOOTS), "flippers"),
		INTERNAL_SPRINGS(ImmutableSet.of(ArmorTypes.LIGHT_ENGINEER_BOOTS), "internal_springs");

		private final ImmutableSet<ArmorTypes> toolset;
		private final BiPredicate<ItemStack, ItemStack> check;
		private final BiConsumer<ItemStack, NBTTagCompound> function;

		ArmorUpgrades(ImmutableSet<ArmorTypes> types, final String... incompatible)
		{
			this.toolset = types;
			this.check = createCheck(incompatible);
			//due to getName() method, this cannot overload the other constructor
			this.function = (upgrade, modifications) -> modifications.setBoolean(getName(), true);
		}

		ArmorUpgrades(ImmutableSet<ArmorTypes> types, BiConsumer<ItemStack, NBTTagCompound> function, final String... incompatible)
		{
			this.toolset = types;
			this.check = createCheck(incompatible);
			this.function = function;
		}

		/**
		 * Utility method, so no code is copied
		 */
		private BiPredicate<ItemStack, ItemStack> createCheck(final String... incompatible)
		{
			return (target, upgrade) -> {
				//check for incompatible upgrades
				NBTTagCompound upgrades = ((IUpgradeableTool)target.getItem()).getUpgrades(target);

				//one upgrade can't be installed 2 times
				if(upgrades.hasKey(getName()))
					return false;

				for(String s : incompatible)
					if(upgrades.hasKey(s))
						return false; //found
				//not found
				return true;
			};
		}
	}

	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> list, @Nonnull ITooltipFlag flag)
	{
		ArmorUpgrades sub = stackToSub(stack);
		//add valid weapon types
		for(ArmorTypes type : sub.toolset)
			list.add(type.color.getHexCol(type.symbol+" "+I18n.format(IIReference.DESC_TOOLUPGRADE+"item."+type.getName())));

		//add description
		String[] flavour = ImmersiveEngineering.proxy.splitStringOnWidth(
				I18n.format(IIReference.DESC_TOOLUPGRADE+sub.getName()), 200);
		Arrays.stream(flavour).map(IIStringUtil::getItalicString).forEach(list::add);
	}

	@Override
	public Set<String> getUpgradeTypes(ItemStack stack)
	{
		return stackToSub(stack).toolset.stream()
				.map(ISerializableEnum::getName)
				.map(String::toUpperCase)
				.collect(Collectors.toSet());
	}

	@Override
	public boolean canApplyUpgrades(ItemStack target, ItemStack upgrade)
	{
		if(target.getItem() instanceof IUpgradeableTool)
			return stackToSub(upgrade).check.test(target, upgrade);
		return false;
	}

	@Override
	public void applyUpgrades(ItemStack target, ItemStack upgrade, NBTTagCompound modifications)
	{
		stackToSub(upgrade).function.accept(upgrade, modifications);
	}
}
