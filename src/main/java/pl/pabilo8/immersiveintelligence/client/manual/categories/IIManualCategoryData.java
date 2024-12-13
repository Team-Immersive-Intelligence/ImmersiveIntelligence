package pl.pabilo8.immersiveintelligence.client.manual.categories;

import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualCategory;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualEntry;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.crafting.IIRecipes;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIMaterial.Materials;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIPrecisionTool.PrecisionTools;
import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialDust.MaterialsDust;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit.Circuits;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 18-01-2020
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
						BlueprintCraftingRecipe.getTypedBlueprint("processors"),
						BlueprintCraftingRecipe.getTypedBlueprint("cryptography_circuits")
				))
				.addSource("basic_circuit_blueprints", getSourceForBlueprint("basic_circuits"))
				.addSource("advanced_electronic_alloy", getSourceForItem(
						IIContent.itemMaterialDust.getStack(MaterialsDust.ADVANCED_ELECTRONIC_ALLOY)
				))
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
				));
		addEntry("data_types");
		addEntry("data_wiring")
				.addSource("data_cable", getSourceForItem(IIContent.itemDataWireCoil.getStack(1)))
				.addSource("connector", getSourceForItem(IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.DATA_CONNECTOR)))
				.addSource("relay", getSourceForItem(IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.DATA_RELAY)))
				.addSource("duplex_connector", getSourceForItem(IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.DATA_DUPLEX_CONNECTOR)))
				.addSource("debugger", getSourceForItem(IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.DATA_DEBUGGER)))
				.addSource("wired_connection", EasyNBT.newNBT()
						.withString("animation", ImmersiveIntelligence.MODID+":manual/wired_connection")
						.withInt("duration", 120)
						.withList("elements",
								EasyNBT.parseEasyNBT("{name:\"input\",rot:[0.0,-90.0,0.0],pos:[-2.5,0.0,-1.25],model:\"immersiveintelligence:models/block/metal_device/punchtape_reader.obj\"}"),
								EasyNBT.parseEasyNBT("{name:\"conn\",rot:[0.0,0.0,90.0],pos:[-1.5,0.0,-1.25],model:\"immersiveintelligence:models/block/connector/data_connector.obj.ie\"}"),
								EasyNBT.parseEasyNBT("{name:\"wire0\",start:[1.0,0.9,-1.25], end:[0.5,1.0,0.5], diameter: 0.125, type:\"wire\"}"),
								EasyNBT.parseEasyNBT("{name:\"relay\",pos:[0.0,0.0,0.0],model:\"immersiveintelligence:models/block/connector/data_relay.obj\"}"),
								EasyNBT.parseEasyNBT("{name:\"debugger\",pos:[1.25,0.0,1.25],model:\"immersiveintelligence:models/block/metal_device/data_debugger/data_debugger.obj\"}"),
								EasyNBT.parseEasyNBT("{name:\"wire0\",start:[0.5,1.0,0.5], end:[-0.75,1.1385,1.25], diameter: 0.125, type:\"wire\"}")
						)
						.withList("overlay",
								EasyNBT.parseEasyNBT("{name:\"punchtape\",pos:[0.0,0.0,0.0], type:\"item\", stack:%s}", IIContent.itemPunchtape.getStack(1).serializeNBT()),
								EasyNBT.parseEasyNBT("{name:\"data\",pos:[0.0,0.0,0.0], type:\"text\", text:\"{...}\"}"),
								EasyNBT.parseEasyNBT("{name:\"particle\",pos:[0.0,0.0,0.0], type:\"text\", text:\"{...}\"}")
						)
						.withList("hovers",
								EasyNBT.parseEasyNBT("{x:%s,y:%s,w:%s,h:%s,item:%s}", 76, 10, 32, 32,
										IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.DATA_DEBUGGER).serializeNBT().toString()),
								EasyNBT.parseEasyNBT("{x:%s,y:%s,w:%s,h:%s,item:%s}", 48, 24, 24, 27,
										IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.DATA_RELAY).serializeNBT().toString()),
								EasyNBT.parseEasyNBT("{x:%s,y:%s,w:%s,h:%s,item:%s}", 28, 38, 16, 16,
										IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.DATA_CONNECTOR).serializeNBT().toString()),
								EasyNBT.parseEasyNBT("{x:%s,y:%s,w:%s,h:%s,item:%s}", 2, 32, 32, 32,
										IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.PUNCHTAPE_READER).serializeNBT().toString())
						)
				)
				.addSource("wireless_connection", EasyNBT.newNBT());
		addEntry("data_callback");

		addEntry("data_input_machine")
				.addSource("punchtape", getSourceForItem(IIContent.itemPunchtape.getStack(1)))
				.addSource("data_coil", getSourceForItem(IIContent.itemDataWireCoil.getStack(1)));
		addEntry("punchtapes")
				.addSource("punchtape", getSourceForItem(IIContent.itemMaterial.getStack(Materials.PUNCHTAPE_EMPTY)))
				.addSource("reader", getSourceForItem(IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.PUNCHTAPE_READER)))
				.addSource("writer", getSourceForItem(IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.PUNCHTAPE_READER)));
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

		addEntry("small_data_devices")
				.addSource("data_combiner", getSourceForItem(IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.DATA_MERGER)))
				.addSource("data_router", getSourceForItem(IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.DATA_ROUTER)))
				.addSource("small_data_buffer", getSourceForItem(IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.SMALL_DATA_BUFFER)))
				.addSource("timed_buffer", getSourceForItem(IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.TIMED_BUFFER)))
				.addSource("redstone_buffer", getSourceForItem(IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.REDSTONE_BUFFER)

				));

		addEntry("radio_station");
		addEntry("printing_press");
		addEntry("scanning_conveyor");
		addEntry("programmable_speaker");

		addEntry("chemical_bath");
		addEntry("chemical_painter");
		addEntry("electrolyzer");
		addEntry("precision_assembler")
				.addSource("assembly_scheme", getSourceForItem(IIContent.itemAssemblyScheme.getStack(1)))
				.addSource("precision_tools", getSourceForItems(
						Arrays.stream(PrecisionTools.values())
								.map(IIContent.itemPrecisionTool::getStack)
								.toArray(ItemStack[]::new)
				));

	}
}
