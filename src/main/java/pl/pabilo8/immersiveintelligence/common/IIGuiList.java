package pl.pabilo8.immersiveintelligence.common;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import pl.pabilo8.immersiveintelligence.client.gui.*;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticLogicMachineEdit;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticLogicMachineStorage;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticMachineVariables;
import pl.pabilo8.immersiveintelligence.client.gui.data_input_machine.GuiDataInputMachineEdit;
import pl.pabilo8.immersiveintelligence.client.gui.data_input_machine.GuiDataInputMachineStorage;
import pl.pabilo8.immersiveintelligence.client.gui.data_input_machine.GuiDataInputMachineVariables;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityRedstoneInterface;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySawmill;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySkyCartStation;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.TileEntityGearbox;
import pl.pabilo8.immersiveintelligence.common.gui.*;
import pl.pabilo8.immersiveintelligence.common.gui.arithmetic_logic_machine.ContainerArithmeticLogicMachineEdit;
import pl.pabilo8.immersiveintelligence.common.gui.arithmetic_logic_machine.ContainerArithmeticLogicMachineStorage;
import pl.pabilo8.immersiveintelligence.common.gui.arithmetic_logic_machine.ContainerArithmeticLogicMachineVariables;
import pl.pabilo8.immersiveintelligence.common.gui.data_input_machine.ContainerDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.gui.data_input_machine.ContainerDataInputMachineVariables;

import java.util.function.BiFunction;

/**
 * Created by Pabilo8 on 2019-05-17.
 * Major update on 2020-06-08
 */

public enum IIGuiList
{
	GUI_METAL_CRATE(TileEntityMetalCrate.class,
			(player, te) -> new ContainerMetalCrate(player.inventory, (TileEntityMetalCrate)te),
			(player, te) -> new GuiMetalCrate(player.inventory, (TileEntityMetalCrate)te)
	),
	GUI_AMMUNITION_CRATE(TileEntityAmmunitionCrate.class,
			(player, te) -> new ContainerAmmunitionCrate(player.inventory, (TileEntityAmmunitionCrate)te),
			(player, te) -> new GuiAmmunitionCrate(player.inventory, (TileEntityAmmunitionCrate)te)
	),
	GUI_MEDICRATE(TileEntityMedicalCrate.class,
			(player, te) -> new ContainerMedicalCrate(player.inventory, (TileEntityMedicalCrate)te),
			(player, te) -> new GuiMedicalCrate(player.inventory, (TileEntityMedicalCrate)te)
	),
	GUI_REPAIR_CRATE(TileEntityRepairCrate.class,
			(player, te) -> new ContainerRepairCrate(player.inventory, (TileEntityRepairCrate)te),
			(player, te) -> new GuiRepairCrate(player.inventory, (TileEntityRepairCrate)te)
	),

	GUI_SMALL_CRATE(TileEntitySmallCrate.class,
			(player, te) -> new ContainerSmallCrate(player.inventory, (TileEntitySmallCrate)te),
			(player, te) -> new GuiSmallCrate(player.inventory, (TileEntitySmallCrate)te)
	),

	GUI_SKYCRATE_STATION(TileEntitySkyCrateStation.class,
			(player, te) -> new ContainerSkycrateStation(player.inventory, (TileEntitySkyCrateStation)te),
			(player, te) -> new GuiSkycrateStation(player.inventory, (TileEntitySkyCrateStation)te)
	),
	GUI_SKYCART_STATION(TileEntitySkyCartStation.class,
			(player, te) -> new ContainerSkycartStation(player.inventory, (TileEntitySkyCartStation)te),
			(player, te) -> new GuiSkycartStation(player.inventory, (TileEntitySkyCartStation)te)
	),

	GUI_DATA_INPUT_MACHINE_STORAGE(TileEntityDataInputMachine.class,
			(player, te) -> new ContainerDataInputMachine(player.inventory, (TileEntityDataInputMachine)te),
			(player, te) -> new GuiDataInputMachineStorage(player.inventory, (TileEntityDataInputMachine)te)
	),
	GUI_DATA_INPUT_MACHINE_VARIABLES(TileEntityDataInputMachine.class,
			(player, te) -> new ContainerDataInputMachineVariables(player.inventory, (TileEntityDataInputMachine)te),
			(player, te) -> new GuiDataInputMachineVariables(player.inventory, (TileEntityDataInputMachine)te)
	),
	GUI_DATA_INPUT_MACHINE_EDIT(TileEntityDataInputMachine.class,
			(player, te) -> new ContainerDataInputMachineVariables(player.inventory, (TileEntityDataInputMachine)te),
			(player, te) -> new GuiDataInputMachineEdit(player.inventory, (TileEntityDataInputMachine)te)
	),

	GUI_ARITHMETIC_LOGIC_MACHINE_STORAGE(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachineStorage(player.inventory, (TileEntityArithmeticLogicMachine)te),
			(player, te) -> new GuiArithmeticLogicMachineStorage(player.inventory, (TileEntityArithmeticLogicMachine)te)
	),
	GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_1(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine)te, 0),
			(player, te) -> new GuiArithmeticMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine)te, 0)
	),
	GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_2(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine)te, 1),
			(player, te) -> new GuiArithmeticMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine)te, 1)
	),
	GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_3(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine)te, 2),
			(player, te) -> new GuiArithmeticMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine)te, 2)
	),
	GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_4(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine)te, 3),
			(player, te) -> new GuiArithmeticMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine)te, 3)
	),

	GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_1(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine)te),
			(player, te) -> new GuiArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine)te, 0)
	),
	GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_2(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine)te),
			(player, te) -> new GuiArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine)te, 1)
	),
	GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_3(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine)te),
			(player, te) -> new GuiArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine)te, 2)
	),
	GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_4(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine)te),
			(player, te) -> new GuiArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine)te, 3)
	),

	GUI_PRINTED_PAGE_BLANK(GuiPrintedPage::new),
	GUI_PRINTED_PAGE_TEXT(GuiPrintedPage::new),
	GUI_PRINTED_PAGE_CODE(GuiPrintedPage::new),
	GUI_PRINTED_PAGE_BLUEPRINT(GuiPrintedPage::new),

	GUI_DATA_REDSTONE_INTERFACE_DATA(TileEntityRedstoneInterface.class,
			(player, te) -> new ContainerRedstoneDataInterface(player.inventory, (TileEntityRedstoneInterface)te),
			(player, te) -> new GuiDataRedstoneInterfaceData(player.inventory, (TileEntityRedstoneInterface)te)
	),
	GUI_DATA_REDSTONE_INTERFACE_REDSTONE(TileEntityRedstoneInterface.class,
			(player, te) -> new ContainerRedstoneDataInterface(player.inventory, (TileEntityRedstoneInterface)te),
			(player, te) -> new GuiDataRedstoneInterfaceRedstone(player.inventory, (TileEntityRedstoneInterface)te)
	),

	GUI_PRINTING_PRESS(TileEntityPrintingPress.class,
			(player, te) -> new ContainerPrintingPress(player.inventory, (TileEntityPrintingPress)te),
			(player, te) -> new GuiPrintingPress(player.inventory, (TileEntityPrintingPress)te)
	),

	GUI_CHEMICAL_BATH(TileEntityChemicalBath.class,
			(player, te) -> new ContainerChemicalBath(player.inventory, (TileEntityChemicalBath)te),
			(player, te) -> new GuiChemicalBath(player.inventory, (TileEntityChemicalBath)te)
	),
	GUI_ELECTROLYZER(TileEntityElectrolyzer.class,
			(player, te) -> new ContainerElectrolyzer(player.inventory, (TileEntityElectrolyzer)te),
			(player, te) -> new GuiElectrolyzer(player.inventory, (TileEntityElectrolyzer)te)
	),

	GUI_PRECISSION_ASSEMBLER(TileEntityPrecissionAssembler.class,
			(player, te) -> new ContainerPrecissionAssembler(player.inventory, (TileEntityPrecissionAssembler)te),
			(player, te) -> new GuiPrecissionAssembler(player.inventory, (TileEntityPrecissionAssembler)te)
	),

	GUI_AMMUNITION_FACTORY(TileEntityAmmunitionFactory.class,
			(player, te) -> new ContainerAmmunitionFactory(player.inventory, (TileEntityAmmunitionFactory)te),
			(player, te) -> new GuiAmmunitionFactory(player.inventory, (TileEntityAmmunitionFactory)te)
	),


	GUI_DATA_MERGER(TileEntityDataMerger.class,
			(player, te) -> new ContainerDataMerger(player.inventory, (TileEntityDataMerger)te),
			(player, te) -> new GuiDataMerger(player.inventory, (TileEntityDataMerger)te)
	),

	GUI_GEARBOX(TileEntityGearbox.class,
			(player, te) -> new ContainerGearbox(player.inventory, (TileEntityGearbox)te),
			(player, te) -> new GuiGearbox(player.inventory, (TileEntityGearbox)te)
	),

	GUI_PACKER(TileEntityPacker.class,
			(player, te) -> new ContainerPacker(player.inventory, (TileEntityPacker)te),
			(player, te) -> new GuiPacker(player.inventory, (TileEntityPacker)te)
	),
	//GUI_UNPACKER,

	GUI_SAWMILL(TileEntitySawmill.class,
			(player, te) -> new ContainerSawmill(player.inventory, (TileEntitySawmill)te),
			(player, te) -> new GuiSawmill(player.inventory, (TileEntitySawmill)te)
	);

	//GUI_PERISCOPE,

	public boolean item;
	public Class<? extends TileEntity> teClass;
	public BiFunction<EntityPlayer, TileEntity, Container> container;
	public BiFunction<EntityPlayer, TileEntity, GuiScreen> guiFromTile;
	public BiFunction<EntityPlayer, ItemStack, GuiScreen> guiFromStack;

	IIGuiList(Class<? extends TileEntity> teClass, BiFunction<EntityPlayer, TileEntity, Container> container, BiFunction<EntityPlayer, TileEntity, GuiScreen> guiFromTile)
	{
		this.teClass = teClass;
		this.container = container;
		this.guiFromTile = guiFromTile;
		item = false;
	}

	IIGuiList(BiFunction<EntityPlayer, ItemStack, GuiScreen> guiFromStack)
	{
		this.guiFromStack = guiFromStack;
		item = true;
	}
}
