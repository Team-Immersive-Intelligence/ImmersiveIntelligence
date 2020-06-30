package pl.pabilo8.immersiveintelligence.client.manual;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeString;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPages.DataVariablesDisplay;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.MultiblockRedstoneInterface;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDevice;

import static pl.pabilo8.immersiveintelligence.api.Utils.getItemWithMetaName;

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
		String[][] stringInfoTable = {{"ie.manual.entry.def_value", "\'\'"}, {"ie.manual.entry.max_length", "512"}};
		String[][] boolInfoTable = {{"ie.manual.entry.def_value", "ie.manual.entry.false"}, {"ie.manual.entry.accepted_values", "ie.manual.entry.tf"}};
		String[][] itemstackInfoTable = {{"ie.manual.entry.def_value", "ie.manual.entry.empty"}};
		String[][] arrayInfoTable = {{"ie.manual.entry.def_value", "ie.manual.entry.empty"}, {"ie.manual.entry.min_index", "0"}, {"ie.manual.entry.max_index", "255"}};
		String[][] pairInfoTable = {{"ie.manual.entry.def_value", "ie.manual.entry.empty"}};
		String[][] rangeInfoTable = {{"ie.manual.entry.def_value", "0-0"}};

		ManualHelper.addEntry("data_variable_types", getCategory(),
				new ManualPages.Text(ManualHelper.getManual(), "data_variable_types"),
				new ManualPages.Text(ManualHelper.getManual(), "data_variable_types_null"),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_int", intInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_string", stringInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_bool", boolInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_itemstack", itemstackInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_array", arrayInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_pair", pairInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_range", rangeInfoTable, true)
		);

		ManualHelper.addEntry("data_wires", getCategory(),
				new ManualPages.Crafting(ManualHelper.getManual(), "data_wires0", new ItemStack(CommonProxy.item_data_wire_coil)),
				new ManualPages.CraftingMulti(ManualHelper.getManual(), "data_wires1",
						new ItemStack(CommonProxy.block_data_connector, 1, IIBlockTypes_Connector.DATA_CONNECTOR.getMeta()),
						new ItemStack(CommonProxy.block_data_connector, 1, IIBlockTypes_Connector.DATA_RELAY.getMeta())
				)
		);

		ManualHelper.addEntry("circuit_production", getCategory(),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "circuit_production0",
						new ItemStack(IEContent.itemMaterial, 1, 27),
						getItemWithMetaName(CommonProxy.item_material, "advanced_circuit_board"),
						getItemWithMetaName(CommonProxy.item_material, "processor_circuit_board")
				),

				new ManualPages.ItemDisplay(ManualHelper.getManual(), "circuit_production_basic",
						getItemWithMetaName(CommonProxy.item_material, "basic_circuit_board_raw"),
						getItemWithMetaName(CommonProxy.item_material, "basic_circuit_board_etched"),
						getItemWithMetaName(CommonProxy.item_material, "advanced_electronic_element"),
						new ItemStack(IEContent.itemMaterial, 1, 27)
				),
				new ManualPages.Text(ManualHelper.getManual(), "circuit_production_basic_usages"),

				new ManualPages.ItemDisplay(ManualHelper.getManual(), "circuit_production_advanced",
						getItemWithMetaName(CommonProxy.item_material, "advanced_circuit_board_raw"),
						getItemWithMetaName(CommonProxy.item_material, "advanced_circuit_board_etched"),
						getItemWithMetaName(CommonProxy.item_material, "advanced_electronic_element"),
						getItemWithMetaName(CommonProxy.item_material, "advanced_circuit_board")
				),
				new ManualPages.Text(ManualHelper.getManual(), "circuit_production_advanced_usages"),

				new ManualPages.ItemDisplay(ManualHelper.getManual(), "circuit_production_processor",
						getItemWithMetaName(CommonProxy.item_material, "processor_circuit_board_raw"),
						getItemWithMetaName(CommonProxy.item_material, "processor_circuit_board_etched"),
						getItemWithMetaName(CommonProxy.item_material, "processor_electronic_element"),
						getItemWithMetaName(CommonProxy.item_material, "processor_circuit_board")
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

				new ManualPages.ItemDisplay(ManualHelper.getManual(), "arithmetic_logic_machine_fcircuit_arithmetic", new ItemStack(CommonProxy.item_circuit, 1, 0)),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "arithmetic_logic_machine_fcircuit_advanced_arithmetic", new ItemStack(CommonProxy.item_circuit, 1, 1)),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "arithmetic_logic_machine_fcircuit_logic", new ItemStack(CommonProxy.item_circuit, 1, 2)),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "arithmetic_logic_machine_fcircuit_comparator", new ItemStack(CommonProxy.item_circuit, 1, 3)),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "arithmetic_logic_machine_fcircuit_advanced_logic", new ItemStack(CommonProxy.item_circuit, 1, 4)),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "arithmetic_logic_machine_fcircuit_advanced_text", new ItemStack(CommonProxy.item_circuit, 1, 5)),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "arithmetic_logic_machine_fcircuit_advanced_itemstack", new ItemStack(CommonProxy.item_circuit, 1, 6))
		);
		ManualHelper.addEntry("redstone_interface", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "redstone_interface0", MultiblockRedstoneInterface.instance),
				new ManualPages.Text(ManualHelper.getManual(), "redstone_interface1")
		);
		ManualHelper.addEntry("conveyor_scanner", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "conveyor_scanner0", MultiblockConveyorScanner.instance),
				new DataVariablesDisplay(ManualHelper.getManual(), "conveyor_scanner", false)
						.addEntry(new DataPacketTypeItemStack(), 's')
		);

		ManualHelper.addEntry("printing_press", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "printing_press0", MultiblockPrintingPress.instance),
				new ManualPages.Text(ManualHelper.getManual(), "printing_press1"),
				new ManualPages.Text(ManualHelper.getManual(), "printing_press2"),
				new DataVariablesDisplay(ManualHelper.getManual(), "printing_press", true)
						.addEntry(new DataPacketTypeString(), 't')
						.addEntry(new DataPacketTypeInteger(), 'c')
						.addEntry(new DataPacketTypeString(), 's')

		);

		ManualHelper.addEntry("radio_station", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "radio_station0", MultiblockRadioStation.instance),
				new ManualPages.Text(ManualHelper.getManual(), "radio_station1"),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "radio_station2",
						new ItemStack(CommonProxy.item_radio_configurator, 1, 0),
						new ItemStack(CommonProxy.item_radio_configurator, 1, 1)
				)
		);

		ManualHelper.addEntry("small_data_devices", getCategory(),
				new ManualPages.Crafting(ManualHelper.getManual(), "data_merger", new ItemStack(CommonProxy.block_metal_device, 1, IIBlockTypes_MetalDevice.DATA_MERGER.getMeta())),
				new ManualPages.Crafting(ManualHelper.getManual(), "data_router", new ItemStack(CommonProxy.block_metal_device, 1, IIBlockTypes_MetalDevice.DATA_ROUTER.getMeta())),
				new ManualPages.Crafting(ManualHelper.getManual(), "redstone_buffer", new ItemStack(CommonProxy.block_metal_device, 1, IIBlockTypes_MetalDevice.REDSTONE_BUFFER.getMeta())),
				new ManualPages.Crafting(ManualHelper.getManual(), "timed_buffer", new ItemStack(CommonProxy.block_metal_device, 1, IIBlockTypes_MetalDevice.TIMED_BUFFER.getMeta())),
				new ManualPages.Crafting(ManualHelper.getManual(), "small_data_buffer", new ItemStack(CommonProxy.block_metal_device, 1, IIBlockTypes_MetalDevice.SMALL_DATA_BUFFER.getMeta()))

		);

		ManualHelper.addEntry("programmable_speaker", getCategory(),
				new ManualPages.Crafting(ManualHelper.getManual(), "programmable_speaker0", new ItemStack(CommonProxy.block_data_connector, 1, IIBlockTypes_Connector.PROGRAMMABLE_SPEAKER.getMeta())),
				new DataVariablesDisplay(ManualHelper.getManual(), "programmable_speaker", true)
						.addEntry(new DataPacketTypeString(), 's')
						.addEntry(new DataPacketTypeInteger(), 't')
		);
	}
}
