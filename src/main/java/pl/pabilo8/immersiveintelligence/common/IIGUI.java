package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import mezz.jei.api.IModRegistry;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.client.gui.block.*;
import pl.pabilo8.immersiveintelligence.client.gui.block.ammunition_production.GuiAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.client.gui.block.ammunition_production.GuiProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine.GuiArithmeticLogicMachineEdit;
import pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine.GuiArithmeticLogicMachineStorage;
import pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine.GuiArithmeticMachineVariables;
import pl.pabilo8.immersiveintelligence.client.gui.block.data_input_machine.GuiDataInputMachine;
import pl.pabilo8.immersiveintelligence.client.gui.block.data_input_machine.GuiDataInputMachineEdit;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageStatus;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageStorage;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageTasks;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui.DecoResourcesLoader;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoResource;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.item.GuiCasingPouch;
import pl.pabilo8.immersiveintelligence.client.gui.item.GuiPrintedPage;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMerger;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityMetalCrate;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityMedicalCrate;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityRepairCrate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySawmill;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCartStation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityGearbox;
import pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.TileEntitySmallCrate;
import pl.pabilo8.immersiveintelligence.common.compat.jei.gui_handlers.JeiDecoGuiHandler;
import pl.pabilo8.immersiveintelligence.common.gui.*;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerEmplacement.ContainerEmplacementStorage;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;
import pl.pabilo8.immersiveintelligence.common.util.lambda.TriFunction;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.06.2020
 * @updated 09.06.2025
 * @ii-approved 0.3.1
 * @since 17.05.2019
 */
public enum IIGUI implements ISerializableEnum
{
	METAL_CRATE(TileEntityMetalCrate.class, ContainerIICrate::new),
	AMMUNITION_CRATE(TileEntityAmmunitionCrate.class, ContainerAmmunitionCrate::new),
	MEDIC_CRATE(TileEntityMedicalCrate.class, ContainerMedicalCrate::new),
	REPAIR_CRATE(TileEntityRepairCrate.class, ContainerRepairCrate::new),
	SMALL_CRATE(TileEntitySmallCrate.class, ContainerIICrate::new),

	SKYCRATE_STATION(TileEntitySkyCrateStation.class, ContainerSkycrateStation::new),
	SKYCART_STATION(TileEntitySkyCartStation.class, ContainerSkycartStation::new),

	DATA_INPUT_MACHINE_STORAGE(TileEntityDataInputMachine.class,
			(player, te) -> new ContainerDataInputMachine(player, te, true)
	),
	DATA_INPUT_MACHINE_VARIABLES(TileEntityDataInputMachine.class,
			(player, te) -> new ContainerDataInputMachine(player, te, false)
	),
	DATA_INPUT_MACHINE_EDIT(TileEntityDataInputMachine.class, ContainerDataInputMachineEditing::new),

	ARITHMETIC_LOGIC_MACHINE_STORAGE(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player, te, 0)
	),
	ARITHMETIC_LOGIC_MACHINE_VARIABLES_0(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player, te, 1, 0)
	),
	ARITHMETIC_LOGIC_MACHINE_VARIABLES_1(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player, te, 1, 1)
	),
	ARITHMETIC_LOGIC_MACHINE_VARIABLES_2(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player, te, 1, 2)
	),
	ARITHMETIC_LOGIC_MACHINE_VARIABLES_3(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player, te, 1, 3)
	),
	ARITHMETIC_LOGIC_MACHINE_EDIT(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player, te, 2)
	),

	PRINTED_PAGE_BLANK(),
	PRINTED_PAGE_TEXT(),
	PRINTED_PAGE_CODE(),
	PRINTED_PAGE_BLUEPRINT(),

	CASING_POUCH(ContainerCasingPouch::new),

	DATA_REDSTONE_INTERFACE_DATA(TileEntityRedstoneInterface.class, ContainerRedstoneDataInterface::new),
	DATA_REDSTONE_INTERFACE_REDSTONE(TileEntityRedstoneInterface.class, ContainerRedstoneDataInterface::new),
	PRINTING_PRESS(TileEntityPrintingPress.class, ContainerPrintingPress::new),
	CHEMICAL_BATH(TileEntityChemicalBath.class, ContainerChemicalBath::new),
	ELECTROLYZER(TileEntityElectrolyzer.class, ContainerElectrolyzer::new),
	PRECISION_ASSEMBLER(TileEntityPrecisionAssembler.class, ContainerPrecisionAssembler::new),
	FUEL_STATION(TileEntityFuelStation.class, ContainerFuelStation::new),
	DATA_MERGER(TileEntityDataMerger.class, ContainerDataMerger::new),
	GEARBOX(TileEntityGearbox.class, ContainerGearbox::new),
	PACKER(TileEntityPacker.class, ContainerPacker::new),
	SAWMILL(TileEntitySawmill.class, ContainerSawmill::new),
	UPGRADE(TileEntity.class,
			(player, te) -> new ContainerUpgrade(player, (TileEntity & IUpgradableMachine)te)
	),
	VULCANIZER(TileEntityVulcanizer.class, ContainerVulcanizer::new),

	EMPLACEMENT_STORAGE(TileEntityEmplacement.class, ContainerEmplacementStorage::new),
	EMPLACEMENT_TASKS(TileEntityEmplacement.class, ContainerEmplacement::new),
	EMPLACEMENT_STATUS(TileEntityEmplacement.class, ContainerEmplacement::new),

	FILLER(TileEntityFiller.class, ContainerFiller::new),
	CHEMICAL_PAINTER(TileEntityChemicalPainter.class, ContainerChemicalPainter::new),
	COAGULATOR(TileEntityCoagulator.class, ContainerCoagulator::new),

	PROJECTILE_WORKSHOP(TileEntityProjectileWorkshop.class, ContainerProjectileWorkshop::new),
	AMMUNITION_ASSEMBLER(TileEntityAmmunitionAssembler.class, ContainerAmmunitionAssembler::new),

	RADAR(TileEntityRadar.class, ContainerRadar::new);

	//GUI_PERISCOPE,

	public boolean item;
	public final Class<? extends TileEntity> teClass;
	public final BiFunction<EntityPlayer, TileEntity, Container> containerFromTile;
	public final TriFunction<EntityPlayer, ItemStack, EnumHand, Container> containerFromStack;
	@SideOnly(Side.CLIENT)
	public BiFunction<EntityPlayer, TileEntity, GuiScreen> guiFromTile;
	@SideOnly(Side.CLIENT)
	public TriFunction<EntityPlayer, ItemStack, EnumHand, GuiScreen> guiFromStack;
	//Required for JEI
	@SideOnly(Side.CLIENT)
	public Class<? extends DecoGui<?, ?>> guiClass;

	/**
	 * TileEntity GUI constructor
	 */
	<T extends TileEntity> IIGUI(@Nonnull Class<T> teClass, BiFunction<EntityPlayer, T, Container> containerFromTile)
	{
		this.teClass = teClass;
		this.containerFromTile = (player, tileEntity) -> containerFromTile.apply(player, (T)tileEntity);
		this.containerFromStack = null;
		this.item = false;
	}

	/**
	 * ItemStack GUI constructor
	 */
	IIGUI(TriFunction<EntityPlayer, ItemStack, EnumHand, Container> containerFromStack)
	{
		this.teClass = null;
		this.containerFromTile = null;
		this.containerFromStack = containerFromStack;
		this.item = true;
	}

	/**
	 * Container-less Item GUI constructor
	 */
	IIGUI()
	{
		this.teClass = null;
		this.containerFromTile = null;
		this.containerFromStack = null;
		this.item = true;
	}

	@SideOnly(Side.CLIENT)
	public static void initClientGUIs()
	{
		IIGUI.SAWMILL.setClientDecoGui(GuiSawmill::new);
		IIGUI.PACKER.setClientGui(GuiPacker::new);
		IIGUI.GEARBOX.setClientDecoGui(GuiGearbox::new);

		IIGUI.DATA_REDSTONE_INTERFACE_DATA
				.setClientDecoGui(GuiDataRedstoneInterfaceData::new);
		IIGUI.DATA_REDSTONE_INTERFACE_REDSTONE
				.setClientGui(GuiDataRedstoneInterfaceRedstone::new);

		IIGUI.PRINTING_PRESS.setClientDecoGui(GuiPrintingPress::new);
		IIGUI.CHEMICAL_BATH.setClientGui(GuiChemicalBath::new);
		IIGUI.ELECTROLYZER.setClientDecoGui(GuiElectrolyzer::new);
		IIGUI.PRECISION_ASSEMBLER.setClientGui(GuiPrecisionAssembler::new);
		IIGUI.FUEL_STATION.setClientGui(GuiFuelStation::new);
		IIGUI.DATA_MERGER.setClientGui(GuiDataMerger::new);
		//Crates
		IIGUI.METAL_CRATE.setClientDecoGui(GuiMetalCrate::new);
		IIGUI.SMALL_CRATE.setClientDecoGui(GuiSmallCrate::new);
		//Effect Crates
		IIGUI.AMMUNITION_CRATE.setClientGui(GuiAmmunitionCrate::new);
		IIGUI.MEDIC_CRATE.setClientGui(GuiMedicalCrate::new);
		IIGUI.REPAIR_CRATE.setClientGui(GuiRepairCrate::new);
		//Skycrate
		IIGUI.SKYCRATE_STATION.setClientGui(GuiSkycrateStation::new);
		IIGUI.SKYCART_STATION.setClientGui(GuiSkycartStation::new);
		//DIM
		IIGUI.DATA_INPUT_MACHINE_STORAGE.setClientDecoGui(GuiDataInputMachine::getStorageGui);
		IIGUI.DATA_INPUT_MACHINE_VARIABLES.setClientDecoGui(GuiDataInputMachine::getVariablesGui);
		IIGUI.DATA_INPUT_MACHINE_EDIT.setClientDecoGui(GuiDataInputMachineEdit::new);
		//ALM
		IIGUI.ARITHMETIC_LOGIC_MACHINE_STORAGE.setClientGui(GuiArithmeticLogicMachineStorage::new);
		IIGUI.ARITHMETIC_LOGIC_MACHINE_VARIABLES_0.setClientGui((player, te) ->
				new GuiArithmeticMachineVariables(player, (TileEntityArithmeticLogicMachine)te, 0));
		IIGUI.ARITHMETIC_LOGIC_MACHINE_VARIABLES_1.setClientGui((player, te) ->
				new GuiArithmeticMachineVariables(player, (TileEntityArithmeticLogicMachine)te, 1));
		IIGUI.ARITHMETIC_LOGIC_MACHINE_VARIABLES_2.setClientGui((player, te) ->
				new GuiArithmeticMachineVariables(player, (TileEntityArithmeticLogicMachine)te, 2));
		IIGUI.ARITHMETIC_LOGIC_MACHINE_VARIABLES_3.setClientGui((player, te) ->
				new GuiArithmeticMachineVariables(player, (TileEntityArithmeticLogicMachine)te, 3));
		IIGUI.ARITHMETIC_LOGIC_MACHINE_EDIT.setClientGui(GuiArithmeticLogicMachineEdit::new);
		//Printed Page
		IIGUI.PRINTED_PAGE_BLANK.setClientStackGui(GuiPrintedPage::new);
		IIGUI.PRINTED_PAGE_TEXT.setClientStackGui(GuiPrintedPage::new);
		IIGUI.PRINTED_PAGE_CODE.setClientStackGui(GuiPrintedPage::new);
		IIGUI.PRINTED_PAGE_BLUEPRINT.setClientStackGui(GuiPrintedPage::new);

		IIGUI.CASING_POUCH.setClientStackGui(GuiCasingPouch::new);

		IIGUI.UPGRADE.setClientGui((player, te) -> new GuiUpgrade(player, ((TileEntity & IUpgradableMachine)te)));

		IIGUI.VULCANIZER.setClientGui(GuiVulcanizer::new);
		IIGUI.EMPLACEMENT_STORAGE.setClientGui(GuiEmplacementPageStorage::new);
		IIGUI.EMPLACEMENT_TASKS.setClientGui(GuiEmplacementPageTasks::new);
		IIGUI.EMPLACEMENT_STATUS.setClientGui(GuiEmplacementPageStatus::new);

		IIGUI.FILLER.setClientGui(GuiFiller::new);
		IIGUI.CHEMICAL_PAINTER.setClientGui(GuiChemicalPainter::new);

		IIGUI.AMMUNITION_ASSEMBLER.setClientGui(GuiAmmunitionAssembler::new);
		IIGUI.PROJECTILE_WORKSHOP.setClientGui(GuiProjectileWorkshop::new);

		IIGUI.RADAR.setClientGui(GuiRadar::new);
	}

	@SideOnly(Side.CLIENT)
	@Method(modid = "jei")
	public static void registerDecoJEICompat(IModRegistry registry)
	{
		for(IIGUI gui : values())
			if(gui.guiClass!=null)
				registry.addAdvancedGuiHandlers(new JeiDecoGuiHandler<>(gui));
	}

	@SideOnly(Side.CLIENT)
	@Deprecated
	public <T extends TileEntity> void setClientGui(BiFunction<EntityPlayer, T, GuiScreen> guiFromTile)
	{
		this.guiFromTile = (player, tileEntity) -> guiFromTile.apply(player, (T)tileEntity);
	}

	@SideOnly(Side.CLIENT)
	public <T extends TileEntityIEBase & IIEInventory, C extends ContainerIIBase<T>> void setClientDecoGui(BiFunction<EntityPlayer, T, DecoGui<T, C>> guiFromTile)
	{
		Class<DecoGui<T, C>> klass = (Class<DecoGui<T, C>>)guiFromTile.apply(null, null).getClass();
		this.guiFromTile = (player, tileEntity) -> guiFromTile.apply(player, (T)tileEntity);
		this.guiClass = klass;
		DecoTemplate annotation = klass.getAnnotation(DecoTemplate.class);
		if(annotation==null)
		{
			IILogger.error("GUI class "+klass.getName()+" is missing @DecoTemplate annotation!");
			return;
		}

		List<ResLoc> resources = new ArrayList<>();
		for(Field field : klass.getFields())
		{
			if(field.isAnnotationPresent(DecoResource.class)&&Modifier.isStatic(field.getModifiers()))
			{
				try
				{
					Object value = field.get(null);
					if(value instanceof ResLoc)
						resources.add(((ResLoc)value));
					else if(value instanceof ResourceLocation)
						resources.add(ResLoc.of((ResourceLocation)value));
					else if(value instanceof String)
						resources.add(ResLoc.of((String)value));
				} catch(IllegalAccessException e)
				{
					IILogger.error("Failed to access field "+field.getName()+" in class "+klass.getName(), e);
				}
			}
		}
		if(!resources.isEmpty())
			new DecoResourcesLoader(this.getName().replace("gui_", ""), resources);

	}

	@SideOnly(Side.CLIENT)
	public void setClientStackGui(TriFunction<EntityPlayer, ItemStack, EnumHand, GuiScreen> guiFromStack)
	{
		this.guiFromStack = guiFromStack;
		item = true;
	}
}
