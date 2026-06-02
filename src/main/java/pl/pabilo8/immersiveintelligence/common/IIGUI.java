package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import mezz.jei.api.IModRegistry;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.gui.block.*;
import pl.pabilo8.immersiveintelligence.client.gui.block.ammunition_production.GuiAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine.GuiArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine.GuiArithmeticLogicMachineEdit;
import pl.pabilo8.immersiveintelligence.client.gui.block.data_input_machine.GuiDataInputMachine;
import pl.pabilo8.immersiveintelligence.client.gui.block.data_input_machine.GuiDataInputMachineEdit;
import pl.pabilo8.immersiveintelligence.client.gui.block.data_router.GuiDataRouter;
import pl.pabilo8.immersiveintelligence.client.gui.block.data_router.GuiDataRouterEdit;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageConfig;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageFireMissions;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageStorage;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageTargetFilters;
import pl.pabilo8.immersiveintelligence.client.gui.block.flagpole.GuiFlagpole;
import pl.pabilo8.immersiveintelligence.client.gui.block.flagpole.GuiFlagpoleFaction;
import pl.pabilo8.immersiveintelligence.client.gui.block.inserter.GuiInserter;
import pl.pabilo8.immersiveintelligence.client.gui.block.packer.GuiPacker;
import pl.pabilo8.immersiveintelligence.client.gui.block.packer.GuiPackerLabeler;
import pl.pabilo8.immersiveintelligence.client.gui.block.radar.GuiRadar;
import pl.pabilo8.immersiveintelligence.client.gui.block.radar.GuiRadarConfig;
import pl.pabilo8.immersiveintelligence.client.gui.block.radar.GuiRadarTargets;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui.DecoResourcesLoader;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoResource;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.entity.GuiEntityUpgrade;
import pl.pabilo8.immersiveintelligence.client.gui.item.GuiCasingPouch;
import pl.pabilo8.immersiveintelligence.client.gui.item.GuiPrintedPage;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMerger;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataRouter;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityMetalCrate;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityMedicalCrate;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityRepairCrate;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityInserterBase;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySawmill;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCartStation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityGearbox;
import pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.TileEntitySmallCrate;
import pl.pabilo8.immersiveintelligence.common.compat.jei.DecoGuiJEIHandler;
import pl.pabilo8.immersiveintelligence.common.gui.*;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIEntityBase;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;
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
	ARITHMETIC_LOGIC_MACHINE_VARIABLES(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player, te, 1)
	),
	ARITHMETIC_LOGIC_MACHINE_EDIT(TileEntityArithmeticLogicMachine.class,
			(player, te) -> new ContainerArithmeticLogicMachine(player, te, 2)
	),

	PRINTED_PAGE_BLANK(),
	PRINTED_PAGE_TEXT(),
	PRINTED_PAGE_CODE(),
	PRINTED_PAGE_BLUEPRINT(),
	PRINTED_PAGE_NEWSPAPER(),
	PRINTED_PAGE_BOOK(),
	PRINTED_PAGE_BOUND(),
	PRINTED_PAGE_LOGISTIC_TAG(),

	CASING_POUCH(ContainerCasingPouch::new),

	DATA_REDSTONE_INTERFACE_DATA(TileEntityRedstoneDataInterface.class, ContainerRedstoneDataInterface::getDataGUI),
	DATA_REDSTONE_INTERFACE_REDSTONE(TileEntityRedstoneDataInterface.class, ContainerRedstoneDataInterface::getRedstoneGUI),
	PRINTING_PRESS(TileEntityPrintingPress.class, ContainerPrintingPress::new),
	CHEMICAL_BATH(TileEntityChemicalBath.class, ContainerChemicalBath::new),
	ELECTROLYZER(TileEntityElectrolyzer.class, ContainerElectrolyzer::new),
	PRECISION_ASSEMBLER(TileEntityPrecisionAssembler.class, ContainerPrecisionAssembler::new),
	FUEL_STATION(TileEntityFuelStation.class, ContainerFuelStation::new),

	DATA_MERGER(TileEntityDataMerger.class, ContainerDataMerger::new),
	DATA_ROUTER(TileEntityDataRouter.class, ContainerDataRouter::getMainGui),
	DATA_ROUTER_EDIT(TileEntityDataRouter.class, ContainerDataRouter::getEditGui),
	INSERTER(TileEntityInserterBase.class, ContainerInserter::new),

	GEARBOX(TileEntityGearbox.class, ContainerGearbox::new),
	PACKER(TileEntityPacker.class, ContainerPacker::new),
	PACKER_LABELER(TileEntityPacker.class, ContainerPacker::new),
	SAWMILL(TileEntitySawmill.class, ContainerSawmill::new),

	@SuppressWarnings({"rawtypes", "unchecked"})
	UPGRADE_TILE(TileEntityIEBase.class,
			(player, te) -> new ContainerTileUpgrade(player, te)
	),
	@SuppressWarnings({"rawtypes", "unchecked"})
	UPGRADE_ENTITY(Entity.class,
			(player, entity) -> new ContainerEntityUpgrade(player, entity)
	),
	VULCANIZER(TileEntityVulcanizer.class, ContainerVulcanizer::new),

	FLAGPOLE(TileEntityFlagpole.class, ContainerFlagpole::getContainerForFlagpolePage),
	FLAGPOLE_FACTION(TileEntityFlagpole.class, ContainerFlagpole::getContainerForFactionPage),
	EMPLACEMENT_STORAGE(TileEntityEmplacement.class, ContainerEmplacement::getContainerForStoragePage),
	EMPLACEMENT_CONFIG(TileEntityEmplacement.class, ContainerEmplacement::new),
	EMPLACEMENT_TARGET_FILTERS(TileEntityEmplacement.class, ContainerEmplacement::new),
	EMPLACEMENT_FIRE_MISSIONS(TileEntityEmplacement.class, ContainerEmplacement::new),

	FILLER(TileEntityFiller.class, ContainerFiller::new),
	CHEMICAL_PAINTER(TileEntityChemicalPainter.class, ContainerChemicalPainter::new),
	COAGULATOR(TileEntityCoagulator.class, ContainerCoagulator::new),

	PROJECTILE_WORKSHOP(TileEntityProjectileWorkshop.class, ContainerProjectileWorkshop::new),
	AMMUNITION_ASSEMBLER(TileEntityAmmunitionAssembler.class, ContainerAmmunitionAssembler::new),

	RADAR(TileEntityRadar.class, ContainerRadar::new),
	RADAR_CONFIG(TileEntityRadar.class, ContainerRadar::new),
	RADAR_TARGETS(TileEntityRadar.class, ContainerRadar::new);

	public final Class<? extends TileEntity> teClass;
	public final Class<? extends Entity> entityClass;
	public final BiFunction<EntityPlayer, TileEntity, Container> containerFromTile;
	public final BiFunction<EntityPlayer, Entity, Container> containerFromEntity;
	public final TriFunction<EntityPlayer, ItemStack, EnumHand, Container> containerFromStack;
	public boolean item;

	@SideOnly(Side.CLIENT)
	public BiFunction<EntityPlayer, TileEntity, GuiScreen> guiFromTile;
	@SideOnly(Side.CLIENT)
	public TriFunction<EntityPlayer, ItemStack, EnumHand, GuiScreen> guiFromStack;
	@SideOnly(Side.CLIENT)
	private BiFunction<EntityPlayer, Entity, GuiScreen> guiFromEntity;
	//Required for JEI
	@SideOnly(Side.CLIENT)
	public Class<? extends DecoGui<?, ?>> guiClass;

	/**
	 * TileEntity GUI constructor
	 */
	<T> IIGUI(@Nonnull Class<T> teClass, @Nonnull BiFunction<EntityPlayer, T, Container> containerFunction)
	{
		if(TileEntity.class.isAssignableFrom(teClass))
		{
			//noinspection unchecked
			this.teClass = (Class<? extends TileEntity>)teClass;
			this.entityClass = null;
			//noinspection unchecked
			this.containerFromTile = (player, tileEntity) -> containerFunction.apply(player, (T)tileEntity);
			this.containerFromEntity = null;
		}
		else if(Entity.class.isAssignableFrom(teClass))
		{
			this.teClass = null;
			//noinspection unchecked
			this.entityClass = (Class<? extends Entity>)teClass;
			this.containerFromTile = null;
			this.containerFromEntity = (player, entity) -> containerFunction.apply(player, (T)entity);
		}
		else
			throw new IllegalArgumentException("Invalid GUI subject class: "+teClass);
		this.containerFromStack = null;
		this.item = false;
	}

	/**
	 * ItemStack GUI constructor
	 */
	IIGUI(@Nonnull TriFunction<EntityPlayer, ItemStack, EnumHand, Container> containerFromStack)
	{
		this.teClass = null;
		this.entityClass = null;
		this.containerFromTile = null;
		this.containerFromEntity = null;
		this.containerFromStack = containerFromStack;
		this.item = true;
	}

	/**
	 * Container-less Item GUI constructor
	 */
	IIGUI()
	{
		this.teClass = null;
		this.entityClass = null;
		this.containerFromTile = null;
		this.containerFromEntity = null;
		this.containerFromStack = null;
		this.item = true;
	}

	@SideOnly(Side.CLIENT)
	public static void initClientGUIs()
	{
		IIGUI.SAWMILL.setClientTileGui(GuiSawmill::new);
		IIGUI.PACKER.setClientTileGui(GuiPacker::new);
		IIGUI.PACKER_LABELER.setClientTileGui(GuiPackerLabeler::new);
		IIGUI.GEARBOX.setClientTileGui(GuiGearbox::new);

		IIGUI.DATA_REDSTONE_INTERFACE_DATA
				.setClientTileGui(GuiDataRedstoneInterface::getDataGUI);
		IIGUI.DATA_REDSTONE_INTERFACE_REDSTONE
				.setClientTileGui(GuiDataRedstoneInterface::getRedstoneGUI);

		IIGUI.PRINTING_PRESS.setClientTileGui(GuiPrintingPress::new);
		IIGUI.CHEMICAL_BATH.setClientTileGui(GuiChemicalBath::new);
		IIGUI.ELECTROLYZER.setClientTileGui(GuiElectrolyzer::new);
		IIGUI.PRECISION_ASSEMBLER.setClientTileGui(GuiPrecisionAssembler::new);
		IIGUI.FUEL_STATION.setClientTileGui(GuiFuelStation::new);
		IIGUI.DATA_MERGER.setClientGui(GuiDataMerger::new);
		IIGUI.DATA_ROUTER.setClientTileGui(GuiDataRouter::new);
		IIGUI.DATA_ROUTER_EDIT.setClientTileGui(GuiDataRouterEdit::new);
		IIGUI.INSERTER.setClientTileGui(GuiInserter::new);
		//Crates
		IIGUI.METAL_CRATE.setClientTileGui(GuiMetalCrate::new);
		IIGUI.SMALL_CRATE.setClientTileGui(GuiSmallCrate::new);
		//Effect Crates
		IIGUI.AMMUNITION_CRATE.setClientTileGui(GuiAmmunitionCrate::new);
		IIGUI.MEDIC_CRATE.setClientTileGui(GuiMedicalCrate::new);
		IIGUI.REPAIR_CRATE.setClientTileGui(GuiRepairCrate::new);
		//Skycrate
		IIGUI.SKYCRATE_STATION.setClientTileGui(GuiSkycrateStation::new);
		IIGUI.SKYCART_STATION.setClientTileGui(GuiSkycartStation::new);
		//DIM
		IIGUI.DATA_INPUT_MACHINE_STORAGE.setClientTileGui(GuiDataInputMachine::getStorageGui);
		IIGUI.DATA_INPUT_MACHINE_VARIABLES.setClientTileGui(GuiDataInputMachine::getVariablesGui);
		IIGUI.DATA_INPUT_MACHINE_EDIT.setClientTileGui(GuiDataInputMachineEdit::new);
		//ALM
		IIGUI.ARITHMETIC_LOGIC_MACHINE_STORAGE.setClientTileGui(GuiArithmeticLogicMachine::getStorageGui);
		IIGUI.ARITHMETIC_LOGIC_MACHINE_VARIABLES.setClientTileGui(GuiArithmeticLogicMachine::getVariablesGui);
		IIGUI.ARITHMETIC_LOGIC_MACHINE_EDIT.setClientTileGui(GuiArithmeticLogicMachineEdit::new);

		//Printed Page
		IIGUI.PRINTED_PAGE_BLANK.setClientStackGui(GuiPrintedPage::new);
		IIGUI.PRINTED_PAGE_TEXT.setClientStackGui(GuiPrintedPage::new);
		IIGUI.PRINTED_PAGE_CODE.setClientStackGui(GuiPrintedPage::new);
		IIGUI.PRINTED_PAGE_BLUEPRINT.setClientStackGui(GuiPrintedPage::new);
		IIGUI.PRINTED_PAGE_NEWSPAPER.setClientStackGui(GuiPrintedPage::new);
		IIGUI.PRINTED_PAGE_BOUND.setClientStackGui(GuiPrintedPage::new);
		IIGUI.PRINTED_PAGE_BOOK.setClientStackGui(GuiPrintedPage::new);

		IIGUI.CASING_POUCH.setClientStackGui(GuiCasingPouch::new);

		//noinspection rawtypes,unchecked
		IIGUI.UPGRADE_TILE.setClientTileGui((player, tile) -> new GuiTileUpgrade(player, tile));
		//noinspection rawtypes,unchecked
		IIGUI.UPGRADE_ENTITY.setClientEntityGui((player, tile) -> new GuiEntityUpgrade(player, tile));

		IIGUI.FLAGPOLE.setClientTileGui(GuiFlagpole::new);
		IIGUI.FLAGPOLE_FACTION.setClientTileGui(GuiFlagpoleFaction::new);
		IIGUI.EMPLACEMENT_STORAGE.setClientTileGui(GuiEmplacementPageStorage::new);
		IIGUI.EMPLACEMENT_CONFIG.setClientTileGui(GuiEmplacementPageConfig::new);
		IIGUI.EMPLACEMENT_TARGET_FILTERS.setClientTileGui(GuiEmplacementPageTargetFilters::new);
		IIGUI.EMPLACEMENT_FIRE_MISSIONS.setClientTileGui(GuiEmplacementPageFireMissions::new);

		IIGUI.FILLER.setClientTileGui(GuiFiller::new);
		IIGUI.CHEMICAL_PAINTER.setClientTileGui(GuiChemicalPainter::new);

		IIGUI.AMMUNITION_ASSEMBLER.setClientTileGui(GuiAmmunitionAssembler::new);
		IIGUI.PROJECTILE_WORKSHOP.setClientTileGui(GuiProjectileWorkshop::new);

		IIGUI.RADAR.setClientTileGui(GuiRadar::new);
		IIGUI.RADAR_CONFIG.setClientTileGui(GuiRadarConfig::new);
		IIGUI.RADAR_TARGETS.setClientTileGui(GuiRadarTargets::new);
		IIGUI.COAGULATOR.setClientTileGui(GuiCoagulator::new);
		IIGUI.VULCANIZER.setClientTileGui(GuiVulcanizer::new);
	}

	@SideOnly(Side.CLIENT)
	@Method(modid = "jei")
	public static void registerDecoJEICompat(IModRegistry registry)
	{
		for(IIGUI gui : values())
			if(gui.guiClass!=null)
				registry.addAdvancedGuiHandlers(new DecoGuiJEIHandler<>(gui));
	}

	@SideOnly(Side.CLIENT)
	@Deprecated
	public <T extends TileEntity> void setClientGui(BiFunction<EntityPlayer, T, GuiScreen> guiFromTile)
	{
		this.guiFromTile = (player, tileEntity) -> guiFromTile.apply(player, (T)tileEntity);
	}

	@SideOnly(Side.CLIENT)
	@SuppressWarnings("unchecked")
	public <T extends TileEntityIEBase & IIEInventory, C extends ContainerIITileBase<T>> void setClientTileGui(BiFunction<EntityPlayer, T, DecoGui<T, C>> guiFromTile)
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
			if(field.isAnnotationPresent(DecoResource.class)&&Modifier.isStatic(field.getModifiers()))
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
		if(!resources.isEmpty())
			new DecoResourcesLoader(this.getName().replace("gui_", ""), resources);

	}

	@SideOnly(Side.CLIENT)
	public void setClientStackGui(TriFunction<EntityPlayer, ItemStack, EnumHand, GuiScreen> guiFromStack)
	{
		this.guiFromStack = guiFromStack;
		this.item = true;
	}

	@SideOnly(Side.CLIENT)
	public <T extends Entity & IIEInventory, C extends ContainerIIEntityBase<T>> void setClientEntityGui(BiFunction<EntityPlayer, T, DecoGui<T, C>> guiFromEntity)
	{
		this.guiFromEntity = (player, entity) -> guiFromEntity.apply(player, (T)entity);
		this.item = false;
	}
}
