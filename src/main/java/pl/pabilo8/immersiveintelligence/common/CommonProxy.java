package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IGuiItem;
import blusunrize.immersiveengineering.common.world.IEWorldGen;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Ores;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIFluid;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.BlockIIWoodenMultiblock;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.MultiblockSkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySkyCrateStation.TileEntitySkyCrateStationParent;
import pl.pabilo8.immersiveintelligence.common.blocks.stone.BlockIIStoneDecoration;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDecoration;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_Ore;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_StoneDecoration;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMinecartCrateReinforced;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMinecartCrateSteel;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMinecartCrateWooden;
import pl.pabilo8.immersiveintelligence.common.entity.EntitySkyCrate;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerMetalCrate;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerPrintingPress;
import pl.pabilo8.immersiveintelligence.common.gui.arithmetic_logic_machine.*;
import pl.pabilo8.immersiveintelligence.common.gui.data_input_machine.ContainerDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.gui.data_input_machine.ContainerDataInputMachineVariables;
import pl.pabilo8.immersiveintelligence.common.items.*;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.wire.IIWireType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static blusunrize.immersiveengineering.api.energy.wires.WireApi.registerFeedthroughForWiretype;

/**
 * Created by Pabilo8 on 2019-05-07.
 */
@Mod.EventBusSubscriber(modid = ImmersiveIntelligence.MODID)
public class CommonProxy implements IGuiHandler
{
	public static final List<Block> blocks = new ArrayList<>();
	public static final List<Item> items = new ArrayList<>();

	public static final String description_key = "desc."+ImmersiveIntelligence.MODID+".";
	public static final String info_key = "info."+ImmersiveIntelligence.MODID+".";
	public static final String data_key = "datasystem."+ImmersiveIntelligence.MODID+".";

	public static final List<Item> creativetab_items = new ArrayList<>();

	public static ItemIIMaterial item_material;
	public static ItemFunctionalCircuit item_circuit;

	public static ItemIILighter item_lighter;
	public static ItemIIElectricHammer item_hammer;
	public static ItemIIElectricWirecutter item_wirecutter;
	public static ItemIIDataWireCoil item_data_wire_coil;
	public static ItemIIMinecart item_minecart;
	public static ItemIIRadioConfigurator item_radio_configurator;
	public static ItemIIMeasuringCup item_measuring_cup;

	public static ItemIIPunchtape item_punchtape;
	public static ItemIIPrintedPage item_printed_page;

	public static BlockIIBase<IIBlockTypes_Ore> block_ore, block_sheetmetal, block_metal_storage;
	public static BlockIIBase<IIBlockTypes_StoneDecoration> block_stone_decoration;
	public static BlockIIBase<IIBlockTypes_MetalDecoration> block_metal_decoration;

	public static BlockIIMetalDevice block_metal_device;
	public static BlockIIDataConnector block_data_connector;

	public static BlockIIWoodenMultiblock block_wooden_multiblock;
	public static BlockIIMetalMultiblock block_metal_multiblock;

	public static BlockIIFluid block_fluid_ink_black;
	public static BlockIIFluid block_fluid_ink_cyan;
	public static BlockIIFluid block_fluid_ink_magenta;
	public static BlockIIFluid block_fluid_ink_yellow;

	public static Fluid fluid_ink_black;
	public static Fluid fluid_ink_cyan;
	public static Fluid fluid_ink_magenta;
	public static Fluid fluid_ink_yellow;

	public void preInit()
	{
		IIWireType.init();
		IIPacketHandler.preInit();

		fluid_ink_black = new Fluid(
				"ink",
				new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/fluid/ink_still"),
				new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/fluid/ink_flow")
		).setDensity(1500).setViscosity(2250);
		if(!FluidRegistry.registerFluid(fluid_ink_black))
			fluid_ink_black = FluidRegistry.getFluid("ink");
		FluidRegistry.addBucketForFluid(fluid_ink_black);

		fluid_ink_cyan = new Fluid(
				"ink_cyan",
				new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/fluid/ink_cyan_still"),
				new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/fluid/ink_cyan_flow")
		).setDensity(1500).setViscosity(2250);
		if(!FluidRegistry.registerFluid(fluid_ink_cyan))
			fluid_ink_cyan = FluidRegistry.getFluid("ink_cyan");
		FluidRegistry.addBucketForFluid(fluid_ink_cyan);

		fluid_ink_magenta = new Fluid(
				"ink_magenta",
				new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/fluid/ink_magenta_still"),
				new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/fluid/ink_magenta_flow")
		).setDensity(1500).setViscosity(2250);
		if(!FluidRegistry.registerFluid(fluid_ink_magenta))
			fluid_ink_magenta = FluidRegistry.getFluid("ink_magenta");
		FluidRegistry.addBucketForFluid(fluid_ink_magenta);

		fluid_ink_yellow = new Fluid(
				"ink_yellow",
				new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/fluid/ink_yellow_still"),
				new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/fluid/ink_yellow_flow")
		).setDensity(1500).setViscosity(2250);
		if(!FluidRegistry.registerFluid(fluid_ink_yellow))
			fluid_ink_yellow = FluidRegistry.getFluid("ink_yellow");
		FluidRegistry.addBucketForFluid(fluid_ink_yellow);

	}

	public void init()
	{
		IEApi.forbiddenInCrates.add((stack) ->
		{
			if(OreDictionary.itemMatches(new ItemStack(block_metal_device, 1, 0), stack, true))
				return true;
			return OreDictionary.itemMatches(new ItemStack(block_metal_device, 1, 1), stack, true);
		});

		ImmersiveIntelligence.logger.info("Adding oregen");
		addConfiguredWorldgen(block_ore.getStateFromMeta(0), "platinum", Ores.ore_platinum);

		ImmersiveIntelligence.logger.info("Adding TileEntities");
		registerTile(TileEntityMetalCrate.class);
		registerTile(TileEntityAmmunitionCrate.class);
		registerTile(TileEntityAlarmSiren.class);

		registerTile(TileEntityInserter.class);

		registerTile(TileEntityTimedBuffer.class);
		registerTile(TileEntityRedstoneBuffer.class);

		//Only for debug purposes
		registerTile(TileEntityDataDebugger.class);

		registerTile(TileEntityDataConnector.class);
		registerTile(TileEntityDataRelay.class);

		registerTile(TileEntitySkyCrateStationParent.class);
		registerTile(TileEntitySkyCrateStation.class);

		registerTile(TileEntityRadioStation.class);
		registerTile(TileEntityDataInputMachine.class);
		registerTile(TileEntityArithmeticLogicMachine.class);
		registerTile(TileEntityPrintingPress.class);

		MultiblockHandler.registerMultiblock(MultiblockSkyCrateStation.instance);
		MultiblockHandler.registerMultiblock(MultiblockRadioStation.instance);
		MultiblockHandler.registerMultiblock(MultiblockDataInputMachine.instance);
		MultiblockHandler.registerMultiblock(MultiblockArithmeticLogicMachine.instance);
		MultiblockHandler.registerMultiblock(MultiblockPrintingPress.instance);

		int i = -1;
		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "minecart_wooden_crate"),
				EntityMinecartCrateWooden.class, "minecart_wooden_crate", i++, ImmersiveIntelligence.INSTANCE, 64, 1,
				true);
		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "minecart_reinforced_crate"),
				EntityMinecartCrateReinforced.class, "minecart_reinforced_crate", i++, ImmersiveIntelligence.INSTANCE,
				64, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "minecart_steel_crate"),
				EntityMinecartCrateSteel.class, "minecart_steel_crate", i++, ImmersiveIntelligence.INSTANCE, 64, 1,
				true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "skycrate"),
				EntitySkyCrate.class, "skycrate", i++, ImmersiveIntelligence.INSTANCE, 64, 1, true);

		LighterFuelHandler.addFuel(FluidRegistry.getFluid("creosote"), 100);
		LighterFuelHandler.addFuel(FluidRegistry.getFluid("ethanol"), 20);
		// LighterFuelHandler.addFuel(FluidRegistry.getFluid("creosote"),100);

	}

	public void postInit()
	{

	}

	private static ResourceLocation createRegistryName(String unlocalized)
	{
		unlocalized = unlocalized.substring(unlocalized.indexOf(ImmersiveIntelligence.MODID));
		unlocalized = unlocalized.replaceFirst("\\.", ":");
		return new ResourceLocation(unlocalized);
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		// block_conn = new BlockConnector();

		block_ore = (BlockIIBase)new BlockIIBase("ore", Material.ROCK,
				PropertyEnum.create("type", IIBlockTypes_Ore.class), ItemBlockIEBase.class, false).setOpaque(true)
				.setHardness(4.0F).setResistance(5.0F);
		block_sheetmetal = (BlockIIBase)new BlockIIBase("sheetmetal", Material.IRON,
				PropertyEnum.create("type", IIBlockTypes_Ore.class), ItemBlockIEBase.class, false).setOpaque(true)
				.setHardness(4.0F).setResistance(6.0F);
		block_metal_storage = (BlockIIBase)new BlockIIBase("storage", Material.IRON,
				PropertyEnum.create("type", IIBlockTypes_Ore.class), ItemBlockIEBase.class, false).setOpaque(true)
				.setHardness(4.0F).setResistance(5.0F);

		block_metal_decoration = (BlockIIBase)new BlockIIBase("metal_decoration", Material.IRON,
				PropertyEnum.create("type", IIBlockTypes_MetalDecoration.class), ItemBlockIEBase.class, false)
				.setBlockLayer(BlockRenderLayer.CUTOUT).setHardness(3.0F).setResistance(15.0F);

		block_stone_decoration = new BlockIIStoneDecoration();

		block_metal_device = new BlockIIMetalDevice();

		block_data_connector = new BlockIIDataConnector();

		block_wooden_multiblock = new BlockIIWoodenMultiblock();

		block_metal_multiblock = new BlockIIMetalMultiblock();

		block_fluid_ink_black = new BlockIIFluid("ink", fluid_ink_black, Material.WATER);
		block_fluid_ink_cyan = new BlockIIFluid("ink_cyan", fluid_ink_cyan, Material.WATER);
		block_fluid_ink_magenta = new BlockIIFluid("ink_magenta", fluid_ink_magenta, Material.WATER);
		block_fluid_ink_yellow = new BlockIIFluid("ink_yellow", fluid_ink_yellow, Material.WATER);

		for(Block block : blocks)
			event.getRegistry().register(block.setRegistryName(createRegistryName(block.getUnlocalizedName())));

		registerFeedthroughForWiretype(IIWireType.DATA, new ResourceLocation(ImmersiveIntelligence.MODID, "block/empty.obj"),
				new ResourceLocation(ImmersiveIntelligence.MODID, "blocks/data_connector_feedtrough"), new float[]{0, 4, 8, 12},
				0.09375, ImmersiveIntelligence.proxy.block_data_connector.getStateFromMeta(IIBlockTypes_Connector.DATA_RELAY.getMeta()),
				0, 0, (f) -> f);

	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		ImmersiveIntelligence.logger.info("Registering Items");

		item_material = new ItemIIMaterial();
		item_circuit = new ItemFunctionalCircuit();

		item_data_wire_coil = new ItemIIDataWireCoil();

		item_minecart = new ItemIIMinecart();

		item_hammer = new ItemIIElectricHammer();
		item_wirecutter = new ItemIIElectricWirecutter();
		item_lighter = new ItemIILighter();
		item_radio_configurator = new ItemIIRadioConfigurator();
		item_measuring_cup = new ItemIIMeasuringCup();

		item_punchtape = new ItemIIPunchtape();
		item_printed_page = new ItemIIPrintedPage();

		for(Item item : items)
			event.getRegistry().register(item.setRegistryName(createRegistryName(item.getUnlocalizedName())));

		registerOreDict();
	}

	public static <T extends TileEntity & IGuiTile> void openGuiForTile(@Nonnull EntityPlayer player, @Nonnull T tile)
	{
		player.openGui(ImmersiveIntelligence.INSTANCE, tile.getGuiID(), tile.getWorld(), tile.getPos().getX(),
				tile.getPos().getY(), tile.getPos().getZ());
	}

	public static <T extends TileEntity & IGuiTile> void openSpecificGuiForEvenMoreSpecificTile(@Nonnull EntityPlayer player, @Nonnull T tile, int gui)
	{
		player.openGui(ImmersiveIntelligence.INSTANCE, gui, tile.getWorld(), tile.getPos().getX(),
				tile.getPos().getY(), tile.getPos().getZ());
	}

	public static void openGuiForItem(@Nonnull EntityPlayer player, @Nonnull EntityEquipmentSlot slot)
	{
		ItemStack stack = player.getItemStackFromSlot(slot);
		if(stack.isEmpty()||!(stack.getItem() instanceof IGuiItem))
			return;
		ImmersiveIntelligence.logger.info("guiitem1");
		IGuiItem gui = (IGuiItem)stack.getItem();
		player.openGui(ImmersiveIntelligence.INSTANCE, gui.getGuiID(stack), player.world, (int)player.posX, (int)player.posY, (int)player.posZ);
	}

	public void reInitGui()
	{
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		ItemStack stack = player.getActiveItemStack();
		if(te instanceof IGuiTile)
		{
			Object gui = null;
			if(ID==IIGuiList.GUI_METAL_CRATE&&te instanceof TileEntityMetalCrate)
				gui = new ContainerMetalCrate(player.inventory, (TileEntityMetalCrate)te);
			else if(ID==IIGuiList.GUI_AMMUNITION_CRATE&&te instanceof TileEntityAmmunitionCrate)
				gui = new ContainerAmmunitionCrate(player.inventory, (TileEntityAmmunitionCrate)te);

			else if(ID==IIGuiList.GUI_DATA_INPUT_MACHINE_STORAGE&&te instanceof TileEntityDataInputMachine)
				gui = new ContainerDataInputMachine(player.inventory, (TileEntityDataInputMachine)te);
			else if(ID==IIGuiList.GUI_DATA_INPUT_MACHINE_VARIABLES&&te instanceof TileEntityDataInputMachine)
				gui = new ContainerDataInputMachineVariables(player.inventory, (TileEntityDataInputMachine)te);
			else if(ID==IIGuiList.GUI_DATA_INPUT_MACHINE_EDIT&&te instanceof TileEntityDataInputMachine)
				gui = new ContainerDataInputMachineVariables(player.inventory, (TileEntityDataInputMachine)te);

			else if(ID==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_STORAGE&&te instanceof TileEntityArithmeticLogicMachine)
				gui = new ContainerArithmeticLogicMachineStorage(player.inventory, (TileEntityArithmeticLogicMachine)te);
			else if(ID==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_1&&te instanceof TileEntityArithmeticLogicMachine)
				gui = new ContainerArithmeticLogicMachineVariables0(player.inventory, (TileEntityArithmeticLogicMachine)te);
			else if(ID==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_2&&te instanceof TileEntityArithmeticLogicMachine)
				gui = new ContainerArithmeticLogicMachineVariables1(player.inventory, (TileEntityArithmeticLogicMachine)te);
			else if(ID==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_3&&te instanceof TileEntityArithmeticLogicMachine)
				gui = new ContainerArithmeticLogicMachineVariables2(player.inventory, (TileEntityArithmeticLogicMachine)te);
			else if(ID==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_4&&te instanceof TileEntityArithmeticLogicMachine)
				gui = new ContainerArithmeticLogicMachineVariables3(player.inventory, (TileEntityArithmeticLogicMachine)te);

			else if(ID==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_1&&te instanceof TileEntityArithmeticLogicMachine)
				gui = new ContainerArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine)te);
			else if(ID==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_2&&te instanceof TileEntityArithmeticLogicMachine)
				gui = new ContainerArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine)te);
			else if(ID==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_3&&te instanceof TileEntityArithmeticLogicMachine)
				gui = new ContainerArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine)te);
			else if(ID==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_4&&te instanceof TileEntityArithmeticLogicMachine)
				gui = new ContainerArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine)te);

			else if(ID==IIGuiList.GUI_PRINTING_PRESS&&te instanceof TileEntityPrintingPress)
				gui = new ContainerPrintingPress(player.inventory, (TileEntityPrintingPress)te);

			((IGuiTile)te).onGuiOpened(player, false);

			ImmersiveIntelligence.logger.info(world.isRemote+" / "+gui);

			return gui;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		ImmersiveIntelligence.logger.info("getClientGUI (on server???)");
		return getServerGuiElement(ID, player, world, x, y, z);
	}

	public static void addConfiguredWorldgen(IBlockState state, String name, int[] config)
	{
		if(config!=null&&config.length >= 5&&config[0] > 0)
			IEWorldGen.addOreGen(name, state, config[0], config[1], config[2], config[3], config[4]);
	}

	public static void registerTile(Class<? extends TileEntity> tile)
	{
		String s = tile.getSimpleName();
		s = s.substring(s.indexOf("TileEntity")+"TileEntity".length());
		GameRegistry.registerTileEntity(tile, ImmersiveIntelligence.MODID+":"+s);
	}

	public static void registerOreDict()
	{
		OreDictionary.registerOre("dustAdvancedElectronicAlloy", new ItemStack(item_material, 1, item_material.getMetaBySubname("advanced_plate_blend")));
		OreDictionary.registerOre("plateAdvancedElectronicAlloy", new ItemStack(item_material, 1, item_material.getMetaBySubname("advanced_plate")));
		OreDictionary.registerOre("electronTubeAdvanced", new ItemStack(item_material, 1, item_material.getMetaBySubname("advanced_electron_tube")));

		//Basic Circuit Board
		OreDictionary.registerOre("circuitBasicRaw", new ItemStack(item_material, 1, item_material.getMetaBySubname("basic_circuit_board_raw")));

		//Advanced Circuit Board
		OreDictionary.registerOre("circuitAdvancedRaw", new ItemStack(item_material, 1, item_material.getMetaBySubname("advanced_circuit_board_raw")));
		OreDictionary.registerOre("circuitAdvanced", new ItemStack(item_material, 1, item_material.getMetaBySubname("advanced_circuit_board")));

		//Processor Circuit Board
		OreDictionary.registerOre("circuitProcessorRaw", new ItemStack(item_material, 1, item_material.getMetaBySubname("processor_circuit_board_raw")));
		OreDictionary.registerOre("circuitProcessor", new ItemStack(item_material, 1, item_material.getMetaBySubname("processor_circuit_board")));
		OreDictionary.registerOre("circuitEliteRaw", new ItemStack(item_material, 1, item_material.getMetaBySubname("processor_circuit_board_raw")));
		OreDictionary.registerOre("circuitElite", new ItemStack(item_material, 1, item_material.getMetaBySubname("processor_circuit_board")));

		//Platinum
		OreDictionary.registerOre("ingotPlatinum", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_ingot_platinum")));
		OreDictionary.registerOre("dustPlatinum", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_dust_platinum")));
		OreDictionary.registerOre("platePlatinum", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_plate_platinum")));
		OreDictionary.registerOre("nuggetPlatinum", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_nugget_platinum")));

		//Punchtapes
		OreDictionary.registerOre("punchtapeEmpty", new ItemStack(item_material, 1, item_material.getMetaBySubname("punchtape_empty")));
		OreDictionary.registerOre("punchtape", new ItemStack(item_punchtape, 1, 0));


		OreDictionary.registerOre("orePlatinum", new ItemStack(block_ore, 1, 0));
		OreDictionary.registerOre("blockPlatinum", new ItemStack(block_metal_storage, 1, 0));
		OreDictionary.registerOre("sheetmetalPlatinum", new ItemStack(block_sheetmetal, 1, 0));

		OreDictionary.registerOre("pageEmpty", new ItemStack(item_printed_page, 1, 0));
		OreDictionary.registerOre("pageText", new ItemStack(item_printed_page, 1, 1));
		OreDictionary.registerOre("pageWritten", new ItemStack(item_printed_page, 1, 1));

		OreDictionary.registerOre("pageCode", new ItemStack(item_printed_page, 1, 2));
		OreDictionary.registerOre("pageWritten", new ItemStack(item_printed_page, 1, 2));
		OreDictionary.registerOre("pageBlueprint", new ItemStack(item_printed_page, 1, 3));
		OreDictionary.registerOre("pageWritten", new ItemStack(item_printed_page, 1, 3));
	}

	public void renderTile(TileEntity te)
	{
	}

	@SubscribeEvent
	public static void onSave(WorldEvent.Save event)
	{
		IISaveData.setDirty(0);
	}

	@SubscribeEvent
	public static void onUnload(WorldEvent.Unload event)
	{
		IISaveData.setDirty(0);
	}

	public void startSkyhookSound(EntitySkyCrate entitySkyCrate)
	{
	}

	public void onServerGuiChangeRequest(TileEntity tile, int gui, EntityPlayer player)
	{
		if(!(tile instanceof IGuiTile)||((IGuiTile)tile).getGuiMaster()==null)
			return;

		//I like casting things
		IGuiTile te = ((IGuiTile)((IGuiTile)tile).getGuiMaster());
		if(!((TileEntity)te).getWorld().isRemote&&te.canOpenGui(player))
		{
			openSpecificGuiForEvenMoreSpecificTile(player, (TileEntity & IGuiTile)te, gui);
		}
	}
}
