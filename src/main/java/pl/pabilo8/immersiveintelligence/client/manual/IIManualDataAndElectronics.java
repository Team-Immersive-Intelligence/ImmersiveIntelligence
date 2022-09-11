package pl.pabilo8.immersiveintelligence.client.manual;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageDataType;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageDataVariables;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageDataVariablesCallback;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockRedstoneInterface;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIMaterial.Materials;
import pl.pabilo8.immersiveintelligence.common.util.IILib;

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
		return IILib.CAT_DATA;
	}

	@Override
	public void addPages()
	{
		ManualHelper.addEntry("data_main", getCategory(),
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
		ManualHelper.addEntry("conveyor_scanner", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "conveyor_scanner0", MultiblockConveyorScanner.INSTANCE),
				new ManualPages.Text(ManualHelper.getManual(), "conveyor_scanner1"),
				new IIManualPageDataVariables(ManualHelper.getManual(), "conveyor_scanner", false)
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
		);
	}
}
