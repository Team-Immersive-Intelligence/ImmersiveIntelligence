package pl.pabilo8.immersiveintelligence.common;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySawmill;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySkyCartStation;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.TileEntityGearbox;
import pl.pabilo8.immersiveintelligence.common.gui.*;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerEmplacement.ContainerEmplacementStorage;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;

/**
 * Created by Pabilo8 on 2019-05-17.
 * Major update on 2020-06-08
 */

public enum IIGuiList
{
	GUI_METAL_CRATE(TileEntityMetalCrate.class,
			(player, te) -> new ContainerMetalCrate(player.inventory, (TileEntityMetalCrate)te)
	),
	GUI_AMMUNITION_CRATE(TileEntityAmmunitionCrate.class,
			(player, te) -> new ContainerAmmunitionCrate(player.inventory, (TileEntityAmmunitionCrate)te)
	),
	GUI_MEDICRATE(TileEntityMedicalCrate.class,
			(player, te) -> new ContainerMedicalCrate(player.inventory, (TileEntityMedicalCrate)te)
	),
	GUI_REPAIR_CRATE(TileEntityRepairCrate.class,
			(player, te) -> new ContainerRepairCrate(player.inventory, (TileEntityRepairCrate)te)
	),

	GUI_SMALL_CRATE(TileEntitySmallCrate.class,
			(player, te) -> new ContainerSmallCrate(player.inventory, (TileEntitySmallCrate)te)
	),

	GUI_SKYCRATE_STATION(TileEntitySkyCrateStation.class,
			(player, te) -> new ContainerSkycrateStation(player.inventory, (TileEntitySkyCrateStation)te)
	),
	GUI_SKYCART_STATION(TileEntitySkyCartStation.class,
			(player, te) -> new ContainerSkycartStation(player.inventory, (TileEntitySkyCartStation)te)
	),

	GUI_DATA_INPUT_MACHINE_STORAGE(TileEntityDataInputMachine.class,
			(player, te) -> new ContainerDataInputMachine(player.inventory, (TileEntityDataInputMachine)te, true)
	),
	GUI_DATA_INPUT_MACHINE_VARIABLES(TileEntityDataInputMachine.class,
			(player, te) -> new ContainerDataInputMachine(player.inventory, (TileEntityDataInputMachine)te, false)
	),
	GUI_DATA_INPUT_MACHINE_EDIT(TileEntityDataInputMachine.class,
			(player, te) -> new ContainerDataInputMachine(player.inventory, (TileEntityDataInputMachine)te, false)
	),

	GUI_ARITHMETIC_LOGIC_MACHINE_STORAGE(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player.inventory, (TileEntityArithmeticLogicMachine)te, 0)
	),
	GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_0(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player.inventory, (TileEntityArithmeticLogicMachine)te, 1, 0)
	),
	GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_1(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player.inventory, (TileEntityArithmeticLogicMachine)te, 1, 1)
	),
	GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_2(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player.inventory, (TileEntityArithmeticLogicMachine)te, 1, 2)
	),
	GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_3(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player.inventory, (TileEntityArithmeticLogicMachine)te, 1, 3)
	),
	GUI_ARITHMETIC_LOGIC_MACHINE_EDIT(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player.inventory, (TileEntityArithmeticLogicMachine)te, 2)
	),

	GUI_PRINTED_PAGE_BLANK(),
	GUI_PRINTED_PAGE_TEXT(),
	GUI_PRINTED_PAGE_CODE(),
	GUI_PRINTED_PAGE_BLUEPRINT(),

	GUI_DATA_REDSTONE_INTERFACE_DATA(TileEntityRedstoneInterface.class,
			(player, te) -> new ContainerRedstoneDataInterface(player.inventory, (TileEntityRedstoneInterface)te)
	),
	GUI_DATA_REDSTONE_INTERFACE_REDSTONE(TileEntityRedstoneInterface.class,
			(player, te) -> new ContainerRedstoneDataInterface(player.inventory, (TileEntityRedstoneInterface)te)
	),

	GUI_PRINTING_PRESS(TileEntityPrintingPress.class,
			(player, te) -> new ContainerPrintingPress(player.inventory, (TileEntityPrintingPress)te)
	),

	GUI_CHEMICAL_BATH(TileEntityChemicalBath.class,
			(player, te) -> new ContainerChemicalBath(player.inventory, (TileEntityChemicalBath)te)
	),
	GUI_ELECTROLYZER(TileEntityElectrolyzer.class,
			(player, te) -> new ContainerElectrolyzer(player.inventory, (TileEntityElectrolyzer)te)
	),

	GUI_PRECISSION_ASSEMBLER(TileEntityPrecissionAssembler.class,
			(player, te) -> new ContainerPrecissionAssembler(player.inventory, (TileEntityPrecissionAssembler)te)
	),

	GUI_FUEL_STATION(TileEntityFuelStation.class,
			(player, te) -> new ContainerFuelStation(player.inventory, (TileEntityFuelStation)te)
	),

	GUI_DATA_MERGER(TileEntityDataMerger.class,
			(player, te) -> new ContainerDataMerger(player.inventory, (TileEntityDataMerger)te)
	),

	GUI_GEARBOX(TileEntityGearbox.class,
			(player, te) -> new ContainerGearbox(player.inventory, (TileEntityGearbox)te)
	),

	GUI_PACKER(TileEntityPacker.class,
			(player, te) -> new ContainerPacker(player.inventory, (TileEntityPacker)te)
	),
	//GUI_UNPACKER,

	GUI_SAWMILL(TileEntitySawmill.class,
			(player, te) -> new ContainerSawmill(player.inventory, (TileEntitySawmill)te)
	),

	GUI_UPGRADE(TileEntity.class,
			(player, te) -> new ContainerUpgrade(player.inventory, (TileEntity & IUpgradableMachine)te)
	),

	GUI_VULCANIZER(TileEntityVulcanizer.class,
			(player, te) -> new ContainerVulcanizer(player.inventory, (TileEntityVulcanizer)te)
	),

	GUI_EMPLACEMENT_STORAGE(TileEntityEmplacement.class,
			(player, te) -> new ContainerEmplacementStorage(player.inventory, (TileEntityEmplacement)te)
	),
	GUI_EMPLACEMENT_TASKS(TileEntityEmplacement.class,
			(player, te) -> new ContainerEmplacement(player.inventory, (TileEntityEmplacement)te)
	),
	GUI_EMPLACEMENT_STATUS(TileEntityEmplacement.class,
			(player, te) -> new ContainerEmplacement(player.inventory, (TileEntityEmplacement)te)
	),

	GUI_FILLER(TileEntityFiller.class,
			(player, te) -> new ContainerFiller(player.inventory, (TileEntityFiller)te)
	),

	GUI_CHEMICAL_PAINTER(TileEntityChemicalPainter.class,
			(player, te) -> new ContainerChemicalPainter(player.inventory, (TileEntityChemicalPainter)te)
	),

	GUI_COAGULATOR(TileEntityCoagulator.class,
			(player, te) -> new ContainerCoagulator(player.inventory, (TileEntityCoagulator)te)
	),

	GUI_PROJECTILE_WORKSHOP(TileEntityProjectileWorkshop.class,
			(player, te) -> new ContainerProjectileWorkshop(player.inventory, (TileEntityProjectileWorkshop)te)
	),
	GUI_AMMUNITION_WORKSHOP(TileEntityAmmunitionWorkshop.class,
			(player, te) -> new ContainerAmmunitionWorkshop(player.inventory, (TileEntityAmmunitionWorkshop)te)
	);

	//GUI_PERISCOPE,

	public boolean item;
	public final Class<? extends TileEntity> teClass;
	public final BiFunction<EntityPlayer, TileEntity, Container> container;
	@SideOnly(Side.CLIENT)
	public BiFunction<EntityPlayer, TileEntity, GuiScreen> guiFromTile;
	@SideOnly(Side.CLIENT)
	public BiFunction<EntityPlayer, ItemStack, GuiScreen> guiFromStack;

	IIGuiList(@Nonnull Class<? extends TileEntity> teClass, BiFunction<EntityPlayer, TileEntity, Container> container)
	{
		this.teClass = teClass;
		this.container = container;
		this.item = false;
	}

	IIGuiList()
	{
		this.teClass = null;
		this.container = null;
		this.item = true;
	}

	@SideOnly(Side.CLIENT)
	public void setClientGui(BiFunction<EntityPlayer, TileEntity, GuiScreen> guiFromTile)
	{
		this.guiFromTile = guiFromTile;
	}

	@SideOnly(Side.CLIENT)
	public void setClientStackGui(BiFunction<EntityPlayer, ItemStack, GuiScreen> guiFromStack)
	{
		this.guiFromStack = guiFromStack;
		item = true;
	}
}
