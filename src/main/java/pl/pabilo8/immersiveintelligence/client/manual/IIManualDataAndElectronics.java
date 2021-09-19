package pl.pabilo8.immersiveintelligence.client.manual;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeString;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageDataVariablesCallback;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageDataVariables;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.MultiblockRedstoneInterface;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDevice;

/**
 * @author Pabilo8
 * @since 18-01-2020
 */
public class IIManualDataAndElectronics extends IIManual
{
	public static IIManualDataAndElectronics INSTANCE = new IIManualDataAndElectronics();

	@Override
	public String getCategory()
	{
		return ClientProxy.CAT_DATA;
	}

	@Override
	public void addPages()
	{
		ManualHelper.addEntry("data_main", getCategory(),
				new ManualPages.Text(ManualHelper.getManual(), "data_main0"),
				new ManualPages.Text(ManualHelper.getManual(), "data_main1")
		);

		String[][] intInfoTable = {{"ie.manual.entry.def_value", "0"}};
		String[][] stringInfoTable = {{"ie.manual.entry.def_value", "''"}, {"ie.manual.entry.max_length", "512"}};
		String[][] boolInfoTable = {{"ie.manual.entry.def_value", "ie.manual.entry.false"}, {"ie.manual.entry.accepted_values", "ie.manual.entry.tf"}};
		String[][] itemstackInfoTable = {{"ie.manual.entry.def_value", "ie.manual.entry.empty"}};
		String[][] arrayInfoTable = {{"ie.manual.entry.def_value", "ie.manual.entry.empty"}, {"ie.manual.entry.min_index", "0"}, {"ie.manual.entry.max_index", "255"}};
		String[][] pairInfoTable = {{"ie.manual.entry.def_value", "ie.manual.entry.empty"}};
		String[][] rangeInfoTable = {{"ie.manual.entry.def_value", "0-0"}};

		ManualHelper.addEntry("data_variable_types", getCategory(),
				new ManualPages.Text(ManualHelper.getManual(), "data_variable_types"),
				new ManualPages.Text(ManualHelper.getManual(), "data_variable_types_null"),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_integer", intInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_string", stringInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_boolean", boolInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_itemstack", itemstackInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_array", arrayInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_pair", pairInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_range", rangeInfoTable, true)
		);

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
						Utils.getStackWithMetaName(IIContent.itemMaterial, "advanced_circuit_board"),
						Utils.getStackWithMetaName(IIContent.itemMaterial, "processor_circuit_board")
				),

				new ManualPages.ItemDisplay(ManualHelper.getManual(), "circuit_production_basic",
						Utils.getStackWithMetaName(IIContent.itemMaterial, "basic_circuit_board_raw"),
						Utils.getStackWithMetaName(IIContent.itemMaterial, "basic_circuit_board_etched"),
						Utils.getStackWithMetaName(IIContent.itemMaterial, "advanced_electronic_element"),
						new ItemStack(IEContent.itemMaterial, 1, 27)
				),
				new ManualPages.Text(ManualHelper.getManual(), "circuit_production_basic_usages"),

				new ManualPages.ItemDisplay(ManualHelper.getManual(), "circuit_production_advanced",
						Utils.getStackWithMetaName(IIContent.itemMaterial, "advanced_circuit_board_raw"),
						Utils.getStackWithMetaName(IIContent.itemMaterial, "advanced_circuit_board_etched"),
						Utils.getStackWithMetaName(IIContent.itemMaterial, "advanced_electronic_element"),
						Utils.getStackWithMetaName(IIContent.itemMaterial, "advanced_circuit_board")
				),
				new ManualPages.Text(ManualHelper.getManual(), "circuit_production_advanced_usages"),

				new ManualPages.ItemDisplay(ManualHelper.getManual(), "circuit_production_processor",
						Utils.getStackWithMetaName(IIContent.itemMaterial, "processor_circuit_board_raw"),
						Utils.getStackWithMetaName(IIContent.itemMaterial, "processor_circuit_board_etched"),
						Utils.getStackWithMetaName(IIContent.itemMaterial, "processor_electronic_element"),
						Utils.getStackWithMetaName(IIContent.itemMaterial, "processor_circuit_board")
				),
				new ManualPages.Text(ManualHelper.getManual(), "circuit_production_processor_usages")
		);

		ManualHelper.addEntry("data_input_machine", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "data_input_machine0", MultiblockDataInputMachine.instance),
				new ManualPages.Text(ManualHelper.getManual(), "data_input_machine1"),
				new ManualPages.Text(ManualHelper.getManual(), "data_input_machine2")
		);
		ManualHelper.addEntry("arithmetic_logic_machine", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "arithmetic_logic_machine0", MultiblockArithmeticLogicMachine.instance),
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
				new ManualPageMultiblock(ManualHelper.getManual(), "redstone_interface0", MultiblockRedstoneInterface.instance),
				new ManualPages.Text(ManualHelper.getManual(), "redstone_interface1")
		);
		ManualHelper.addEntry("conveyor_scanner", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "conveyor_scanner0", MultiblockConveyorScanner.instance),
				new IIManualPageDataVariables(ManualHelper.getManual(), "conveyor_scanner", false)
						.addEntry(new DataPacketTypeItemStack(), 's')
		);

		ManualHelper.addEntry("printing_press", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "printing_press0", MultiblockPrintingPress.instance),
				new ManualPages.Text(ManualHelper.getManual(), "printing_press1"),
				new ManualPages.Text(ManualHelper.getManual(), "printing_press2"),
				new IIManualPageDataVariables(ManualHelper.getManual(), "printing_press", true)
						.addEntry(new DataPacketTypeInteger(), 'a')
						.addEntry(new DataPacketTypeString(), 'm')
						.addEntry(new DataPacketTypeString(), 't'),
				new IIManualPageDataVariablesCallback(ManualHelper.getManual(), "printing_press")
						.addEntry(new DataPacketTypeInteger(), "get_ink", "get_ink_black")
						.addEntry(new DataPacketTypeInteger(), "get_ink_cyan")
						.addEntry(new DataPacketTypeInteger(), "get_ink_magenta")
						.addEntry(new DataPacketTypeInteger(), "get_ink_yellow")
						.addEntry(new DataPacketTypeInteger(), "get_energy")
						.addEntry(new DataPacketTypeInteger(), "get_paper")

		);

		ManualHelper.addEntry("radio_station", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "radio_station0", MultiblockRadioStation.instance),
				new ManualPages.Text(ManualHelper.getManual(), "radio_station1"),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "radio_station2",
						new ItemStack(IIContent.itemRadioConfigurator, 1, 0),
						new ItemStack(IIContent.itemRadioConfigurator, 1, 1)
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
						.addEntry(new DataPacketTypeBoolean(), 'o')
						.addEntry(new DataPacketTypeString(), 's')
						.addEntry(new DataPacketTypeInteger(), 'v')
						.addEntry(new DataPacketTypeInteger(), 't')
		);
	}
}
