package pl.pabilo8.immersiveintelligence.common;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.client.gui.block.*;
import pl.pabilo8.immersiveintelligence.client.gui.block.ammunition_production.GuiAmmunitionWorkshop;
import pl.pabilo8.immersiveintelligence.client.gui.block.ammunition_production.GuiProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine.GuiArithmeticLogicMachineEdit;
import pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine.GuiArithmeticLogicMachineStorage;
import pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine.GuiArithmeticMachineVariables;
import pl.pabilo8.immersiveintelligence.client.gui.block.data_input_machine.GuiDataInputMachineEdit;
import pl.pabilo8.immersiveintelligence.client.gui.block.data_input_machine.GuiDataInputMachineStorage;
import pl.pabilo8.immersiveintelligence.client.gui.block.data_input_machine.GuiDataInputMachineVariables;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageStatus;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageStorage;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageTasks;
import pl.pabilo8.immersiveintelligence.client.gui.item.GuiCasingPouch;
import pl.pabilo8.immersiveintelligence.client.gui.item.GuiPrintedPage;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMerger;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityMetalCrate;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityMedicalCrate;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityRepairCrate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySawmill;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCartStation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityGearbox;
import pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.TileEntitySmallCrate;
import pl.pabilo8.immersiveintelligence.common.gui.*;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerEmplacement.ContainerEmplacementStorage;
import pl.pabilo8.immersiveintelligence.common.util.lambda.TriFunction;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;

/**
 * Created by Pabilo8 on 2019-05-17.
 * Major update on 2020-06-08
 */

public enum IIGuiList
{
	GUI_METAL_CRATE(TileEntityMetalCrate.class,
			ContainerMetalCrate::new
	),
	GUI_AMMUNITION_CRATE(TileEntityAmmunitionCrate.class,
			ContainerAmmunitionCrate::new
	),
	GUI_MEDICRATE(TileEntityMedicalCrate.class,
			ContainerMedicalCrate::new
	),
	GUI_REPAIR_CRATE(TileEntityRepairCrate.class,
			ContainerRepairCrate::new
	),

	GUI_SMALL_CRATE(TileEntitySmallCrate.class,
			ContainerSmallCrate::new
	),

	GUI_SKYCRATE_STATION(TileEntitySkyCrateStation.class,
			ContainerSkycrateStation::new
	),
	GUI_SKYCART_STATION(TileEntitySkyCartStation.class,
			ContainerSkycartStation::new
	),

	GUI_DATA_INPUT_MACHINE_STORAGE(TileEntityDataInputMachine.class,
			(player, te) -> new ContainerDataInputMachine(player, te, true)
	),
	GUI_DATA_INPUT_MACHINE_VARIABLES(TileEntityDataInputMachine.class,
			(player, te) -> new ContainerDataInputMachine(player, te, false)
	),
	GUI_DATA_INPUT_MACHINE_EDIT(TileEntityDataInputMachine.class,
			(player, te) -> new ContainerDataInputMachine(player, te, false)
	),

	GUI_ARITHMETIC_LOGIC_MACHINE_STORAGE(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player, te, 0)
	),
	GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_0(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player, te, 1, 0)
	),
	GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_1(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player, te, 1, 1)
	),
	GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_2(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player, te, 1, 2)
	),
	GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_3(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player, te, 1, 3)
	),
	GUI_ARITHMETIC_LOGIC_MACHINE_EDIT(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player, te, 2)
	),

	GUI_PRINTED_PAGE_BLANK(),
	GUI_PRINTED_PAGE_TEXT(),
	GUI_PRINTED_PAGE_CODE(),
	GUI_PRINTED_PAGE_BLUEPRINT(),

	GUI_CASING_POUCH(ContainerCasingPouch::new),

	GUI_DATA_REDSTONE_INTERFACE_DATA(TileEntityRedstoneInterface.class,
			ContainerRedstoneDataInterface::new
	),
	GUI_DATA_REDSTONE_INTERFACE_REDSTONE(TileEntityRedstoneInterface.class,
			ContainerRedstoneDataInterface::new
	),

	GUI_PRINTING_PRESS(TileEntityPrintingPress.class,
			ContainerPrintingPress::new
	),

	GUI_CHEMICAL_BATH(TileEntityChemicalBath.class,
			ContainerChemicalBath::new
	),
	GUI_ELECTROLYZER(TileEntityElectrolyzer.class,
			ContainerElectrolyzer::new
	),

	GUI_PRECISSION_ASSEMBLER(TileEntityPrecisionAssembler.class,
			ContainerPrecissionAssembler::new
	),

	GUI_FUEL_STATION(TileEntityFuelStation.class,
			ContainerFuelStation::new
	),

	GUI_DATA_MERGER(TileEntityDataMerger.class,
			ContainerDataMerger::new
	),

	GUI_GEARBOX(TileEntityGearbox.class,
			ContainerGearbox::new
	),

	GUI_PACKER(TileEntityPacker.class,
			ContainerPacker::new
	),

	GUI_SAWMILL(TileEntitySawmill.class,
			ContainerSawmill::new
	),

	GUI_UPGRADE(TileEntity.class,
			(player, te) -> new ContainerUpgrade(player, (TileEntity & IUpgradableMachine)te)
	),

	GUI_VULCANIZER(TileEntityVulcanizer.class,
			ContainerVulcanizer::new
	),

	GUI_EMPLACEMENT_STORAGE(TileEntityEmplacement.class,
			ContainerEmplacementStorage::new
	),
	GUI_EMPLACEMENT_TASKS(TileEntityEmplacement.class,
			ContainerEmplacement::new
	),
	GUI_EMPLACEMENT_STATUS(TileEntityEmplacement.class,
			ContainerEmplacement::new
	),

	GUI_FILLER(TileEntityFiller.class,
			ContainerFiller::new
	),

	GUI_CHEMICAL_PAINTER(TileEntityChemicalPainter.class,
			ContainerChemicalPainter::new
	),

	GUI_COAGULATOR(TileEntityCoagulator.class,
			ContainerCoagulator::new
	),

	GUI_PROJECTILE_WORKSHOP(TileEntityProjectileWorkshop.class,
			ContainerProjectileWorkshop::new
	),
	GUI_AMMUNITION_WORKSHOP(TileEntityAmmunitionWorkshop.class,
			ContainerAmmunitionWorkshop::new
	),

	RADAR(TileEntityRadar.class,
			ContainerRadar::new
	);

	//GUI_PERISCOPE,

	public boolean item;
	public final Class<? extends TileEntity> teClass;
	public final BiFunction<EntityPlayer, TileEntity, Container> containerFromTile;
	public final TriFunction<EntityPlayer, ItemStack, EnumHand, Container> containerFromStack;
	@SideOnly(Side.CLIENT)
	public BiFunction<EntityPlayer, TileEntity, GuiScreen> guiFromTile;
	@SideOnly(Side.CLIENT)
	public TriFunction<EntityPlayer, ItemStack, EnumHand, GuiScreen> guiFromStack;

	/**
	 * TileEntity GUI constructor
	 */
	<T extends TileEntity> IIGuiList(@Nonnull Class<T> teClass, BiFunction<EntityPlayer, T, Container> containerFromTile)
	{
		this.teClass = teClass;
		this.containerFromTile = (player, tileEntity) -> containerFromTile.apply(player, (T)tileEntity);
		this.containerFromStack = null;
		this.item = false;
	}

	/**
	 * ItemStack GUI constructor
	 */
	IIGuiList(TriFunction<EntityPlayer, ItemStack, EnumHand, Container> containerFromStack)
	{
		this.teClass = null;
		this.containerFromTile = null;
		this.containerFromStack = containerFromStack;
		this.item = true;
	}

	/**
	 * Container-less Item GUI constructor
	 */
	IIGuiList()
	{
		this.teClass = null;
		this.containerFromTile = null;
		this.containerFromStack = null;
		this.item = true;
	}

	@SideOnly(Side.CLIENT)
	public static void initClientGUIs()
	{
		IIGuiList.GUI_SAWMILL.setClientGui(GuiSawmill::new);
		IIGuiList.GUI_PACKER.setClientGui(GuiPacker::new);
		IIGuiList.GUI_GEARBOX.setClientGui(GuiGearbox::new);

		IIGuiList.GUI_DATA_REDSTONE_INTERFACE_DATA.setClientGui(GuiDataRedstoneInterfaceData::new);
		IIGuiList.GUI_DATA_REDSTONE_INTERFACE_REDSTONE.setClientGui(GuiDataRedstoneInterfaceRedstone::new);
		IIGuiList.GUI_PRINTING_PRESS.setClientGui(GuiPrintingPress::new);
		IIGuiList.GUI_CHEMICAL_BATH.setClientGui(GuiChemicalBath::new);
		IIGuiList.GUI_ELECTROLYZER.setClientGui(GuiElectrolyzer::new);
		IIGuiList.GUI_PRECISSION_ASSEMBLER.setClientGui(GuiPrecissionAssembler::new);
		IIGuiList.GUI_FUEL_STATION.setClientGui(GuiFuelStation::new);
		IIGuiList.GUI_DATA_MERGER.setClientGui(GuiDataMerger::new);

		IIGuiList.GUI_METAL_CRATE.setClientGui(GuiMetalCrate::new);
		IIGuiList.GUI_AMMUNITION_CRATE.setClientGui(GuiAmmunitionCrate::new);
		IIGuiList.GUI_MEDICRATE.setClientGui(GuiMedicalCrate::new);
		IIGuiList.GUI_REPAIR_CRATE.setClientGui(GuiRepairCrate::new);
		IIGuiList.GUI_SMALL_CRATE.setClientGui(GuiSmallCrate::new);
		IIGuiList.GUI_SKYCRATE_STATION.setClientGui(GuiSkycrateStation::new);
		IIGuiList.GUI_SKYCART_STATION.setClientGui(GuiSkycartStation::new);
		IIGuiList.GUI_DATA_INPUT_MACHINE_STORAGE.setClientGui(GuiDataInputMachineStorage::new);
		IIGuiList.GUI_DATA_INPUT_MACHINE_VARIABLES.setClientGui(GuiDataInputMachineVariables::new);
		IIGuiList.GUI_DATA_INPUT_MACHINE_EDIT.setClientGui(GuiDataInputMachineEdit::new);
		IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_STORAGE.setClientGui(GuiArithmeticLogicMachineStorage::new);
		IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_0.setClientGui((player, te) -> new GuiArithmeticMachineVariables(player, (TileEntityArithmeticLogicMachine)te, 0));
		IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_1.setClientGui((player, te) -> new GuiArithmeticMachineVariables(player, (TileEntityArithmeticLogicMachine)te, 1));
		IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_2.setClientGui((player, te) -> new GuiArithmeticMachineVariables(player, (TileEntityArithmeticLogicMachine)te, 2));
		IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_3.setClientGui((player, te) -> new GuiArithmeticMachineVariables(player, (TileEntityArithmeticLogicMachine)te, 3));
		IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_EDIT.setClientGui(GuiArithmeticLogicMachineEdit::new);

		IIGuiList.GUI_PRINTED_PAGE_BLANK.setClientStackGui(GuiPrintedPage::new);
		IIGuiList.GUI_PRINTED_PAGE_TEXT.setClientStackGui(GuiPrintedPage::new);
		IIGuiList.GUI_PRINTED_PAGE_CODE.setClientStackGui(GuiPrintedPage::new);
		IIGuiList.GUI_PRINTED_PAGE_BLUEPRINT.setClientStackGui(GuiPrintedPage::new);

		IIGuiList.GUI_CASING_POUCH.setClientStackGui(GuiCasingPouch::new);

		IIGuiList.GUI_UPGRADE.setClientGui((player, te) -> new GuiUpgrade(player, ((TileEntity & IUpgradableMachine)te)));

		IIGuiList.GUI_VULCANIZER.setClientGui(GuiVulcanizer::new);
		IIGuiList.GUI_EMPLACEMENT_STORAGE.setClientGui(GuiEmplacementPageStorage::new);
		IIGuiList.GUI_EMPLACEMENT_TASKS.setClientGui(GuiEmplacementPageTasks::new);
		IIGuiList.GUI_EMPLACEMENT_STATUS.setClientGui(GuiEmplacementPageStatus::new);

		IIGuiList.GUI_FILLER.setClientGui(GuiFiller::new);
		IIGuiList.GUI_CHEMICAL_PAINTER.setClientGui(GuiChemicalPainter::new);

		IIGuiList.GUI_AMMUNITION_WORKSHOP.setClientGui(GuiAmmunitionWorkshop::new);
		IIGuiList.GUI_PROJECTILE_WORKSHOP.setClientGui(GuiProjectileWorkshop::new);

		IIGuiList.RADAR.setClientGui(GuiRadar::new);
	}

	@SideOnly(Side.CLIENT)
	public <T extends TileEntity> void setClientGui(BiFunction<EntityPlayer, T, GuiScreen> guiFromTile)
	{
		this.guiFromTile = (player, tileEntity) -> guiFromTile.apply(player, (T)tileEntity);
	}

	@SideOnly(Side.CLIENT)
	public void setClientStackGui(TriFunction<EntityPlayer, ItemStack, EnumHand, GuiScreen> guiFromStack)
	{
		this.guiFromStack = guiFromStack;
		item = true;
	}
}
