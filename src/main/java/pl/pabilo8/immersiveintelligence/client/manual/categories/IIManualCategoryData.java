package pl.pabilo8.immersiveintelligence.client.manual.categories;

import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualCategory;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualEntry;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.crafting.IIRecipes;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIMaterial.Materials;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit.Circuits;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

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
				.addSource("basic_circuit_blueprints", getSourceForItems(
						BlueprintCraftingRecipe.getTypedBlueprint("basic_circuits")

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
				.addSource("advanced_general", getSourceForItems(
						IIContent.itemMaterial.getStack(Materials.ADVANCED_CIRCUIT_BOARD_RAW),
						IIContent.itemMaterial.getStack(Materials.ADVANCED_CIRCUIT_BOARD_ETCHED),
						IIContent.itemMaterial.getStack(Materials.ADVANCED_CIRCUIT_BOARD),
						IIContent.itemMaterial.getStack(Materials.ADVANCED_ELECTRONIC_ELEMENT)
				))
				.addSource("processor_general", getSourceForItems(
						IIContent.itemMaterial.getStack(Materials.PROCESSOR_CIRCUIT_BOARD_RAW),
						IIContent.itemMaterial.getStack(Materials.PROCESSOR_CIRCUIT_BOARD_ETCHED),
						IIContent.itemMaterial.getStack(Materials.PROCESSOR_CIRCUIT_BOARD),
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
				.addSource("wireless_connection", EasyNBT.newNBT()
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
				);

		addEntry("data_input_machine")
				.addSource("punchtape", getSourceForItem(IIContent.itemPunchtape.getStack(1)))
				.addSource("data_coil", getSourceForItem(IIContent.itemDataWireCoil.getStack(1)));
		addEntry("punchtapes");
		addEntry("arithmetic_logic_machine");
		IIManualEntry functionalCircuits = addEntry("functional_circuits")
				.addSource("circuit_blueprints", getSourceForItems(
						BlueprintCraftingRecipe.getTypedBlueprint("basic_functional_circuits"),
						BlueprintCraftingRecipe.getTypedBlueprint("advanced_functional_circuits"),
						BlueprintCraftingRecipe.getTypedBlueprint("processor_functional_circuits")
				));
		for(Circuits circuit : Circuits.values())
			functionalCircuits.addSource(circuit.getName(), getSourceForItem(IIContent.itemCircuit.getStack(circuit)));


		addEntry("redstone_interface");

		addEntry("small_data_devices");

		addEntry("radio_station");
		addEntry("printing_press");
		addEntry("scanning_conveyor");

		addEntry("chemical_bath");
		addEntry("chemical_painter");
		addEntry("electrolyzer");
		addEntry("precision_assembler");
		addEntry("functions/arithmetic");
		addEntry("folder/device/sekritdokuments/data_pol");

		/*ManualHelper.addEntry("data_main", getCategory(),
				new ManualPages.Text(ManualHelper.getManual(), "data_main0"),
				new ManualPages.Text(ManualHelper.getManual(), "data_main1")
		);

		IIManualPageDataType[] dataPages = DataPacket.varTypes.values().stream()
				.map(DataPacket::getVarInstance)
				.map(data -> new IIManualPageDataType(ManualHelper.getManual(), data))
				.toArray(IIManualPageDataType[]::new);

		ManualHelper.addEntry("data_variable_types", getCategory(), dataPages);

		ManualHelper.addEntry("data_wires", getCategory(),
				new ManualPages.Crafting(ManualHelper.getManual(), "data_wires0", new ItemStack(IIContent.itemDataWireCoil)),
				new ManualPages.CraftingMulti(ManualHelper.getManual(), "data_wires1",
						new ItemStack(IIContent.blockDataConnector, 1, IIBlockTypes_Connector.DATA_CONNECTOR.getMeta()),
						new ItemStack(IIContent.blockDataConnector, 1, IIBlockTypes_Connector.DATA_RELAY.getMeta())
				)
		);

		ManualHelper.addEntry("circuit_production", getCategory(),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "circuit_production0",
						new ItemStack(IEContent.itemMaterial, 1, 27),
						IIContent.itemMaterial.getStack(Materials.ADVANCED_CIRCUIT_BOARD),
						IIContent.itemMaterial.getStack(Materials.PROCESSOR_CIRCUIT_BOARD)
				),

				new ManualPages.ItemDisplay(ManualHelper.getManual(), "circuit_production_basic",
						IIContent.itemMaterial.getStack(Materials.BASIC_CIRCUIT_BOARD_RAW),
						IIContent.itemMaterial.getStack(Materials.BASIC_CIRCUIT_BOARD_ETCHED),
						IIContent.itemMaterial.getStack(Materials.ADVANCED_ELECTRONIC_ELEMENT),
						new ItemStack(IEContent.itemMaterial, 1, 27)
				),
				new ManualPages.Text(ManualHelper.getManual(), "circuit_production_basic_usages"),

				new ManualPages.ItemDisplay(ManualHelper.getManual(), "circuit_production_advanced",
						IIContent.itemMaterial.getStack(Materials.ADVANCED_CIRCUIT_BOARD_RAW),
						IIContent.itemMaterial.getStack(Materials.ADVANCED_CIRCUIT_BOARD_ETCHED),
						IIContent.itemMaterial.getStack(Materials.ADVANCED_ELECTRONIC_ELEMENT),
						IIContent.itemMaterial.getStack(Materials.ADVANCED_CIRCUIT_BOARD)
				),
				new ManualPages.Text(ManualHelper.getManual(), "circuit_production_advanced_usages"),

				new ManualPages.ItemDisplay(ManualHelper.getManual(), "circuit_production_processor",
						IIContent.itemMaterial.getStack(Materials.PROCESSOR_CIRCUIT_BOARD_RAW),
						IIContent.itemMaterial.getStack(Materials.PROCESSOR_CIRCUIT_BOARD_ETCHED),
						IIContent.itemMaterial.getStack(Materials.PROCESSOR_ELECTRONIC_ELEMENT),
						IIContent.itemMaterial.getStack(Materials.PROCESSOR_CIRCUIT_BOARD)
				),
				new ManualPages.Text(ManualHelper.getManual(), "circuit_production_processor_usages")
		);

		ManualHelper.addEntry("data_input_machine", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "data_input_machine0", MultiblockDataInputMachine.INSTANCE),
				new ManualPages.Text(ManualHelper.getManual(), "data_input_machine1"),
				new ManualPages.Text(ManualHelper.getManual(), "data_input_machine2")
		);
		ManualHelper.addEntry("arithmetic_logic_machine", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "arithmetic_logic_machine0", MultiblockArithmeticLogicMachine.INSTANCE),
				new ManualPages.Text(ManualHelper.getManual(), "arithmetic_logic_machine1"),
				new ManualPages.Text(ManualHelper.getManual(), "arithmetic_logic_machine2"),

				new ManualPages.ItemDisplay(ManualHelper.getManual(), "arithmetic_logic_machine_fcircuit_arithmetic", new ItemStack(IIContent.itemCircuit, 1, 0)),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "arithmetic_logic_machine_fcircuit_advanced_arithmetic", new ItemStack(IIContent.itemCircuit, 1, 1)),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "arithmetic_logic_machine_fcircuit_logic", new ItemStack(IIContent.itemCircuit, 1, 2)),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "arithmetic_logic_machine_fcircuit_comparator", new ItemStack(IIContent.itemCircuit, 1, 3)),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "arithmetic_logic_machine_fcircuit_advanced_logic", new ItemStack(IIContent.itemCircuit, 1, 4)),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "arithmetic_logic_machine_fcircuit_advanced_text", new ItemStack(IIContent.itemCircuit, 1, 5)),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "arithmetic_logic_machine_fcircuit_advanced_itemstack", new ItemStack(IIContent.itemCircuit, 1, 6))
		);
		ManualHelper.addEntry("redstone_interface", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "redstone_interface0", MultiblockRedstoneInterface.INSTANCE),
				new ManualPages.Text(ManualHelper.getManual(), "redstone_interface1")
		);
		ManualHelper.addEntry("scanning_conveyor", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "scanning_conveyor0", MultiblockConveyorScanner.INSTANCE),
				new ManualPages.Text(ManualHelper.getManual(), "scanning_conveyor1"),
				new IIManualPageDataVariables(ManualHelper.getManual(), "scanning_conveyor", false)
						.addEntry(new DataTypeItemStack(), 's')
		);

		ManualHelper.addEntry("printing_press", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "printing_press0", MultiblockPrintingPress.INSTANCE),
				new ManualPages.Text(ManualHelper.getManual(), "printing_press1"),
				new ManualPages.Text(ManualHelper.getManual(), "printing_press2"),
				new IIManualPageDataVariables(ManualHelper.getManual(), "printing_press", true)
						.addEntry(new DataTypeInteger(), 'a')
						.addEntry(new DataTypeString(), 'm')
						.addEntry(new DataTypeString(), 't'),
				new IIManualPageDataVariablesCallback(ManualHelper.getManual(), "printing_press")
						.addEntry(new DataTypeInteger(), "get_ink", "get_ink_black")
						.addEntry(new DataTypeInteger(), "get_ink_cyan")
						.addEntry(new DataTypeInteger(), "get_ink_magenta")
						.addEntry(new DataTypeInteger(), "get_ink_yellow")
						.addEntry(new DataTypeInteger(), "get_energy")
						.addEntry(new DataTypeInteger(), "get_paper")

		);

		ManualHelper.addEntry("radio_station", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "radio_station0", MultiblockRadioStation.INSTANCE),
				new ManualPages.Text(ManualHelper.getManual(), "radio_station1"),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "radio_station2",
						new ItemStack(IIContent.itemRadioTuner, 1, 0),
						new ItemStack(IIContent.itemRadioTuner, 1, 1)
				)
		);

		ManualHelper.addEntry("small_data_devices", getCategory(),
				new ManualPages.Crafting(ManualHelper.getManual(), "data_merger", new ItemStack(IIContent.blockMetalDevice, 1, IIBlockTypes_MetalDevice.DATA_MERGER.getMeta())),
				new ManualPages.Crafting(ManualHelper.getManual(), "data_router", new ItemStack(IIContent.blockMetalDevice, 1, IIBlockTypes_MetalDevice.DATA_ROUTER.getMeta())),
				new ManualPages.Crafting(ManualHelper.getManual(), "redstone_buffer", new ItemStack(IIContent.blockMetalDevice, 1, IIBlockTypes_MetalDevice.REDSTONE_BUFFER.getMeta())),
				new ManualPages.Crafting(ManualHelper.getManual(), "timed_buffer", new ItemStack(IIContent.blockMetalDevice, 1, IIBlockTypes_MetalDevice.TIMED_BUFFER.getMeta())),
				new ManualPages.Crafting(ManualHelper.getManual(), "small_data_buffer", new ItemStack(IIContent.blockMetalDevice, 1, IIBlockTypes_MetalDevice.SMALL_DATA_BUFFER.getMeta()))

		);

		ManualHelper.addEntry("programmable_speaker", getCategory(),
				new ManualPages.Crafting(ManualHelper.getManual(), "programmable_speaker0", new ItemStack(IIContent.blockDataConnector, 1, IIBlockTypes_Connector.PROGRAMMABLE_SPEAKER.getMeta())),
				new IIManualPageDataVariables(ManualHelper.getManual(), "programmable_speaker", true)
						.addEntry(new DataTypeBoolean(), 'o')
						.addEntry(new DataTypeString(), 's')
						.addEntry(new DataTypeInteger(), 'v')
						.addEntry(new DataTypeInteger(), 't')
		);*/
	}
}
