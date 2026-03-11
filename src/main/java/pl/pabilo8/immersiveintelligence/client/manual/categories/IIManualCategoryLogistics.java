package pl.pabilo8.immersiveintelligence.client.manual.categories;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualCategory;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.block.fortification.BlockIIMetalChainFence.MetalFortifications;
import pl.pabilo8.immersiveintelligence.common.block.fortification.BlockIIWoodenChainFence.WoodenFortifications;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIGearbox.IIBlockTypes_Gearbox;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIMechanicalConnector.IIBlockTypes_MechanicalConnector;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIMechanicalDevice.IIBlockTypes_MechanicalDevice;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIMechanicalDevice1.IIBlockTypes_MechanicalDevice1;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIISmallCrate.IIBlockTypes_SmallCrate;
import pl.pabilo8.immersiveintelligence.common.item.ItemIIMinecart.Minecarts;
import pl.pabilo8.immersiveintelligence.common.item.ItemIISkycrateMount.SkycrateMounts;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIISawBlade.SawBlades;
import pl.pabilo8.immersiveintelligence.common.item.mechanical.ItemIIMotorBelt.MotorBelt;
import pl.pabilo8.immersiveintelligence.common.item.mechanical.ItemIIMotorGear.MotorGear;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 18.01.2020
 * @since 8-8-2024
 */
public class IIManualCategoryLogistics extends IIManualCategory
{
	public static IIManualCategoryLogistics INSTANCE = new IIManualCategoryLogistics();

	@Override
	public String getCategory()
	{
		return IIReference.CAT_LOGISTICS;
	}

	@Override
	public void addPages()
	{
		super.addPages();

		addEntry("logistics");
		addEntry("packer");
		addEntry("task_system");

		addEntry("inserters")
				.addSource("inserter_basic", getSourceForItem(IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.INSERTER)))
				.addSource("inserter_advanced", getSourceForItem(IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.ADVANCED_INSERTER)))
				.addSource("inserter_fluid", getSourceForItem(IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.FLUID_INSERTER)))
				.addSource("inserters_full", getSourceForItems(
						IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.INSERTER),
						IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.ADVANCED_INSERTER),
						IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.FLUID_INSERTER)

				));

		addEntry("skycrate_system")
				.addSource("minecart_general", getSourceForItems(
						IIContent.itemMinecart.getStack(Minecarts.CAPACITOR_HV),
						IIContent.itemMinecart.getStack(Minecarts.METAL_BARREL),
						IIContent.itemMinecart.getStack(Minecarts.WOODEN_CRATE),
						IIContent.itemMinecart.getStack(Minecarts.REINFORCED_CRATE),
						IIContent.itemMinecart.getStack(Minecarts.STEEL_CRATE)
				))
				.addSource("mount", getSourceForItems(
						IIContent.itemSkycrateMount.getStack(SkycrateMounts.MECHANICAL),
						IIContent.itemSkycrateMount.getStack(SkycrateMounts.ELECTRIC)
				));

		addEntry("chain_fences_and_gates")
				.addSource("fence_blocks", getSourceForItems(
						IIContent.blockMetalFortification.getStack(MetalFortifications.STEEL_CHAIN_FENCE),
						IIContent.blockMetalFortification.getStack(MetalFortifications.BRASS_CHAIN_FENCE),
						IIContent.blockMetalFortification.getStack(MetalFortifications.ALUMINIUM_CHAIN_FENCE),
						IIContent.blockWoodenFortification.getStack(WoodenFortifications.WOODEN_STEEL_CHAIN_FENCE),
						IIContent.blockWoodenFortification.getStack(WoodenFortifications.WOODEN_BRASS_CHAIN_FENCE)
				));

		addEntry("rotary_power")
				.addSource("gearbox", getSourceForItem(
						IIContent.blockGearbox.getStack(IIBlockTypes_Gearbox.WOODEN_GEARBOX)
				))

				.addSource("transmission", getSourceForItem(
						IIContent.blockMechanicalDevice.getStack(IIBlockTypes_MechanicalDevice.WOODEN_TRANSMISSION_BOX)
				))

				.addSource("wheels", getSourceForItems(
						IIContent.blockMechanicalConnector.getStack(IIBlockTypes_MechanicalConnector.IRON_WHEEL),
						IIContent.blockMechanicalConnector.getStack(IIBlockTypes_MechanicalConnector.STEEL_WHEEL)
				))

				.addSource("belt", getSourceForItem(
						new ItemStack(IIContent.itemMotorBelt)))
				.addSource("belt_cloth", getSourceForItem(IIContent.itemMotorBelt.getStack(MotorBelt.CLOTH)))
				.addSource("belt_steel", getSourceForItem(IIContent.itemMotorBelt.getStack(MotorBelt.STEEL)))
				.addSource("belt_rubber", getSourceForItem(IIContent.itemMotorBelt.getStack(MotorBelt.RUBBER)))
				.addSource("gears", getSourceForItems(
						IIContent.itemMotorGear.getStack(MotorGear.COPPER),
						IIContent.itemMotorGear.getStack(MotorGear.BRASS),
						IIContent.itemMotorGear.getStack(MotorGear.IRON),
						IIContent.itemMotorGear.getStack(MotorGear.STEEL),
						IIContent.itemMotorGear.getStack(MotorGear.TUNGSTEN)
				));

		addEntry("medical_crate")
				.addSource("medical", getSourceForItem(
						IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.MEDIC_CRATE))
				);

		addEntry("repair_crate")
				.addSource("repair", getSourceForItem(
						IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.REPAIR_CRATE))
				);
		addEntry("engineers_crates")
				.addSource("metalbox", getSourceForItem(IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.METAL_CRATE)))
				.addSource("multicrates1", getSourceForItems(
						IIContent.blockSmallCrate.getStack(IIBlockTypes_SmallCrate.WOODEN_CRATE_BOX),
						IIContent.blockSmallCrate.getStack(IIBlockTypes_SmallCrate.WOODEN_CRATE_CUBE),
						IIContent.blockSmallCrate.getStack(IIBlockTypes_SmallCrate.WOODEN_CRATE_WIDE)))
				.addSource("multicrates2", getSourceForItems(
						IIContent.blockSmallCrate.getStack(IIBlockTypes_SmallCrate.REINFORCED_CRATE_BOX),
						IIContent.blockSmallCrate.getStack(IIBlockTypes_SmallCrate.REINFORCED_CRATE_CUBE),
						IIContent.blockSmallCrate.getStack(IIBlockTypes_SmallCrate.REINFORCED_CRATE_WIDE)))
				.addSource("multicrates3", getSourceForItems(
						IIContent.blockSmallCrate.getStack(IIBlockTypes_SmallCrate.METAL_CRATE_BOX),
						IIContent.blockSmallCrate.getStack(IIBlockTypes_SmallCrate.METAL_CRATE_CUBE),
						IIContent.blockSmallCrate.getStack(IIBlockTypes_SmallCrate.METAL_CRATE_WIDE)
				));
		addEntry("improved_capacitor_backpack")
				.addSource("improved_capacitor", getSourceForItem(
						new ItemStack(IIContent.itemAdvancedPowerPack))
				);
		addEntry("mechanical_pump")
				.addSource("pump", getSourceForItem(
						IIContent.blockMechanicalDevice1.getStack(IIBlockTypes_MechanicalDevice1.MECHANICAL_PUMP))
				);

		addEntry("sawmill")
				.addSource("blades", getSourceForItems(
						IIContent.itemSawblade.getStack(SawBlades.IRON),
						IIContent.itemSawblade.getStack(SawBlades.STEEL),
						IIContent.itemSawblade.getStack(SawBlades.TUNGSTEN)
				))
				.addSource("recipe_planks", getSourceForRecipe(SawmillRecipe.class, new ItemStack(Blocks.LOG)))
				.addSource("recipe_sticks", getSourceForRecipe(SawmillRecipe.class, new ItemStack(Blocks.PLANKS)));
		addEntry("chemical_bath");
		addEntry("chemical_painter");
		addEntry("electrolyzer");
	}
}
