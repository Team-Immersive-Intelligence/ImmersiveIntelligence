package pl.pabilo8.immersiveintelligence.client.manual.categories;

import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualCategory;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualEntry;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.compat.ie.recipe.MetalPressRecipeAdapter;
import pl.pabilo8.immersiveintelligence.common.crafting.IIRecipes;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIMaterial.Materials;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIPrecisionTool.PrecisionTools;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit.Circuits;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.Arrays;

/**
 * Registers manual entries and data sources for data devices.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.10.2026
 * @since 18.01.2020
 */
public class IIManualCategoryData extends IIManualCategory
{
	public static IIManualCategoryData INSTANCE = new IIManualCategoryData();

	@Override
	public String getCategory()
	{
		return IIReference.CAT_DATA;
	}

	@Override
	public void addPages()
	{
		super.addPages();
		addEntry("data_main");
		addEntry("electronic_components")
				.addSource("circuit_template", getSourceForItems(
						BlueprintCraftingRecipe.getTypedBlueprint("basic_circuits"),
						BlueprintCraftingRecipe.getTypedBlueprint("advanced_circuits"),
						BlueprintCraftingRecipe.getTypedBlueprint("processors")
				))
				.addSource("circuit_blueprints", getSourceForItems(
						BlueprintCraftingRecipe.getTypedBlueprint("basic_circuits"),
						BlueprintCraftingRecipe.getTypedBlueprint("advanced_circuits"),
						BlueprintCraftingRecipe.getTypedBlueprint("processors")
				))
				.addSource("basic_circuit_blueprints", getSourceForBlueprint("basic_circuits"))
				/*.addSource("advanced_electronic_alloy", getSourceForItem(
						IIContent.itemMaterialDust.getStack(MaterialsDust.ADVANCED_ELECTRONIC_ALLOY)
				))*/
				.addSource("all_circuits", getSourceForItems(
						IIRecipes.BASIC_CIRCUIT,
						IIContent.itemMaterial.getStack(Materials.ADVANCED_CIRCUIT_BOARD),
						IIContent.itemMaterial.getStack(Materials.CRYPTOGRAPHIC_CIRCUIT_BOARD),
						IIContent.itemMaterial.getStack(Materials.PROCESSOR_CIRCUIT_BOARD)
				))
				.addSource("basic_general", getSourceForItems(
						IIContent.itemMaterial.getStack(Materials.BASIC_CIRCUIT_BOARD_RAW),
						IIContent.itemMaterial.getStack(Materials.BASIC_CIRCUIT_BOARD_ETCHED),
						IIRecipes.BASIC_CIRCUIT,
						IIContent.itemMaterial.getStack(Materials.BASIC_ELECTRONIC_ELEMENT)
				))
				.addSource("basic_electronic_element", getSourceForItems(
						IIContent.itemMaterial.getStack(Materials.BASIC_ELECTRONIC_ELEMENT)
				))
				.addSource("cryptography_circuit", getSourceForBlueprint("cryptography_circuits"))
				.addSource("advanced_circuit_blueprints", getSourceForBlueprint("advanced_circuits"))
				.addSource("advanced_general", getSourceForItems(
						IIContent.itemMaterial.getStack(Materials.ADVANCED_CIRCUIT_BOARD_RAW),
						IIContent.itemMaterial.getStack(Materials.ADVANCED_CIRCUIT_BOARD_ETCHED),
						IIContent.itemMaterial.getStack(Materials.ADVANCED_CIRCUIT_BOARD),
						IIContent.itemMaterial.getStack(Materials.ADVANCED_ELECTRONIC_ELEMENT)
				))
				.addSource("advanced_electronic_element", getSourceForItems(

						IIContent.itemMaterial.getStack(Materials.ADVANCED_ELECTRONIC_ELEMENT)
				))
				.addSource("processors_blueprints", getSourceForBlueprint("processors"))
				.addSource("processor_general", getSourceForItems(
						IIContent.itemMaterial.getStack(Materials.PROCESSOR_CIRCUIT_BOARD_RAW),
						IIContent.itemMaterial.getStack(Materials.PROCESSOR_CIRCUIT_BOARD_ETCHED),
						IIContent.itemMaterial.getStack(Materials.PROCESSOR_CIRCUIT_BOARD),
						IIContent.itemMaterial.getStack(Materials.PROCESSOR_ELECTRONIC_ELEMENT)
				))
				.addSource("processor_electronic_element", getSourceForItems(
						IIContent.itemMaterial.getStack(Materials.PROCESSOR_ELECTRONIC_ELEMENT)
				))
				.addSource("cryptographic_circuit_board", getSourceForItem(IIContent.itemMaterial.getStack(Materials.CRYPTOGRAPHIC_CIRCUIT_BOARD))
				);
		addEntry("data_types");
		addEntry("data_wiring")
				.addSource("data_cable", getSourceForItem(IIContent.itemDataWireCoil.getStack(1)))
				.addSource("connector", getSourceForItem(IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.DATA_CONNECTOR)))
				.addSource("relay", getSourceForItem(IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.DATA_RELAY)))
				.addSource("duplex_connector", getSourceForItem(IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.DATA_DUPLEX_CONNECTOR)))
				.addSource("debugger", getSourceForItem(IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.DATA_DEBUGGER)))
				.addSource("wired_connection", getScenarioSource("wired_connection", 120, 1f)
						.withList("hovers",
								EasyNBT.parseEasyNBT("{x:76,y:10,w:32,h:32,item:%s}",
										IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.DATA_DEBUGGER).serializeNBT()),
								EasyNBT.parseEasyNBT("{x:48,y:24,w:24,h:27,item:%s}",
										IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.DATA_RELAY).serializeNBT()),
								EasyNBT.parseEasyNBT("{x:28,y:38,w:16,h:16,item:%s}",
										IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.DATA_CONNECTOR).serializeNBT()),
								EasyNBT.parseEasyNBT("{x:2,y:32,w:32,h:32,item:%s}",
										IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.PUNCHTAPE_READER).serializeNBT())
						))
				.addSource("wireless_connection", getScenarioSource("wireless_connection", 160, 0.3f));
		addEntry("data_callback")
				.addSource("data_callback_scenario", getScenarioSource("data_callback", 200, 0.5f));
		addEntry("inventory_interface")
				.addSource("inventory_interface", getSourceForItem(
						IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.INVENTORY_INTERFACE)));

		addEntry("data_input_machine")
				.addSource("punchtape", getSourceForItem(IIContent.itemPunchtape.getStack(1)))
				.addSource("data_coil", getSourceForItem(IIContent.itemDataWireCoil.getStack(1)));
		addEntry("punchtapes")
				.addSource("punchtape", getSourceForItem(IIContent.itemMaterial.getStack(Materials.PUNCHTAPE_EMPTY)))
				.addSource("reader", getSourceForItem(IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.PUNCHTAPE_READER)))
				.addSource("writer", getSourceForItem(IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.PUNCHTAPE_READER)))
				.addSource("punchtape_scenario", EasyNBT.newNBT());
		addEntry("arithmetic_logic_machine");
		IIManualEntry functionalCircuits = addEntry("functions/_functional_circuits")
				.addSource("circuit_blueprints", getSourceForItems(
						BlueprintCraftingRecipe.getTypedBlueprint("basic_functional_circuits"),
						BlueprintCraftingRecipe.getTypedBlueprint("advanced_functional_circuits"),
						BlueprintCraftingRecipe.getTypedBlueprint("processor_functional_circuits")
				));
		for(Circuits circuit : Circuits.values())
		{
			functionalCircuits.addSource(circuit.getName(), getSourceForItem(IIContent.itemCircuit.getStack(circuit)));
			addEntry("functions/"+circuit.getName());
		}
		addEntry("computers/data_pol");

		addEntry("redstone_interface");

		addEntry("small_data_devices/data_buffer")
				.addSource("device", getSourceForItem(IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.DATA_BUFFER)));
		addEntry("small_data_devices/data_mapper")
				.addSource("device", getSourceForItem(IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.DATA_MAPPER)));
		addEntry("small_data_devices/redstone_sequencer")
				.addSource("device", getSourceForItem(IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.REDSTONE_SEQUENCER)));
		addEntry("small_data_devices/data_appender")
				.addSource("device", getSourceForItem(IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.DATA_APPENDER)));
		addEntry("small_data_devices/data_router")
				.addSource("device", getSourceForItem(IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.DATA_ROUTER)));
		addEntry("small_data_devices/data_merger")
				.addSource("device", getSourceForItem(IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.DATA_MERGER)));

		addEntry("small_data_devices/nixie_displays")
				.addSource("crafting", getSourceForItems(
						IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.DOUBLE_NIXIE_DISPLAY),
						IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.QUAD_NIXIE_DISPLAY)
				));
		addEntry("radio_station")
				.addSource("wireless_connection", getScenarioSource("wireless_connection", 160, 0.3f))
				.addSource("basic_radio_tuner", getSourceForItem(IIContent.itemRadioTuner.getStack(1)))
				.addSource("advanced_radio_tuner", getSourceForItem(new ItemStack(IIContent.itemRadioTuner, 1, 1)
				));
		addEntry("radio_backpack")
				.addSource("radio_backpack", getSourceForItem(new ItemStack(IIContent.itemRadioPack)));
		addEntry("printing_press")
				.addSource("paper_page", getSourceForRecipe(MetalPressRecipeAdapter.class,
						new IngredientStack("paper"), new ItemStack(IEContent.itemMold, 1, 0)));
		addEntry("scanning_conveyor");
		addEntry("programmable_speaker")
				.addSource("programmable_spkr", getSourceForItem(IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.PROGRAMMABLE_SPEAKER)
				));

		addEntry("precision_assembler")
				.addSource("assembly_scheme", getSourceForItem(IIContent.itemAssemblyScheme.getStack(1)))
				.addSource("precision_tools", getSourceForItems(
						Arrays.stream(PrecisionTools.values())
								.map(IIContent.itemPrecisionTool::getStack)
								.toArray(ItemStack[]::new)
				));

	}

	private static EasyNBT getScenarioSource(String name, int duration, float scale)
	{
		return EasyNBT.newNBT()
				.withString("header", "immersiveintelligence:models/manual/"+name+".obj.amt")
				.withString("animation", "immersiveintelligence:manual/"+name)
				.withInt("duration", duration)
				.withFloat("scale", scale)
				.withFloat("offset_y", name.equals("wired_connection")?15: 7);
	}

}
