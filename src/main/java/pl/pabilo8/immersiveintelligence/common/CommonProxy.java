package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MixerRecipe;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IGuiItem;
import blusunrize.immersiveengineering.common.world.IEWorldGen;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Ores;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.LighterFuelHandler;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.api.crafting.BathingRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.ElectrolyzerRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecissionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIFluid;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.BlockIIWoodenMultiblock;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.MultiblockSkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySkyCrateStation.TileEntitySkyCrateStationParent;
import pl.pabilo8.immersiveintelligence.common.blocks.stone.BlockIIStoneDecoration;
import pl.pabilo8.immersiveintelligence.common.blocks.stone.TileEntitySandbags;
import pl.pabilo8.immersiveintelligence.common.blocks.types.*;
import pl.pabilo8.immersiveintelligence.common.bullets.*;
import pl.pabilo8.immersiveintelligence.common.entity.*;
import pl.pabilo8.immersiveintelligence.common.gui.*;
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
	public static final String block_key = "tile."+ImmersiveIntelligence.MODID+".";

	public static ItemIIMaterial item_material = new ItemIIMaterial();
	public static ItemIIFunctionalCircuit item_circuit = new ItemIIFunctionalCircuit();

	public static ItemIILighter item_lighter = new ItemIILighter();
	public static ItemIIElectricHammer item_hammer = new ItemIIElectricHammer();
	public static ItemIIElectricWirecutter item_wirecutter = new ItemIIElectricWirecutter();
	public static ItemIIDataWireCoil item_data_wire_coil = new ItemIIDataWireCoil();
	public static ItemIIMinecart item_minecart = new ItemIIMinecart();
	public static ItemIIRadioConfigurator item_radio_configurator = new ItemIIRadioConfigurator();
	public static ItemIIMeasuringCup item_measuring_cup = new ItemIIMeasuringCup();
	public static ItemIIPrecissionTool item_precission_tool = new ItemIIPrecissionTool();
	public static ItemIIAssemblyScheme item_assembly_scheme = new ItemIIAssemblyScheme();
	public static ItemIIBinoculars item_binoculars = new ItemIIBinoculars();

	public static ItemIIBullet item_bullet = new ItemIIBullet();
	public static ItemIICasingArtillery item_casing_artillery = new ItemIICasingArtillery();
	public static ItemIICasingGrenade item_casing_grenade = new ItemIICasingGrenade();

	public static ItemIIPunchtape item_punchtape = new ItemIIPunchtape();
	public static ItemIIPrintedPage item_printed_page = new ItemIIPrintedPage();

	public static BlockIIBase<IIBlockTypes_Ore> block_ore;
	public static BlockIIBase<IIBlockTypes_Metal> block_sheetmetal, block_metal_storage;
	public static BlockIIBase<IIBlockTypes_StoneDecoration> block_stone_decoration;
	public static BlockIIBase<IIBlockTypes_MetalDecoration> block_metal_decoration;

	public static BlockIIMetalDevice block_metal_device;
	public static BlockIIDataConnector block_data_connector;
	public static BlockIISmallCrate block_small_crate;

	public static BlockIIWoodenMultiblock block_wooden_multiblock;
	public static BlockIIMetalMultiblock block_metal_multiblock;

	public static BlockIIFluid block_fluid_ink_black;
	public static BlockIIFluid block_fluid_ink_cyan;
	public static BlockIIFluid block_fluid_ink_magenta;
	public static BlockIIFluid block_fluid_ink_yellow;
	public static BlockIIFluid block_fluid_etching_acid;

	public static BlockIIFluid block_fluid_brine;
	public static BlockIIFluid block_gas_hydrogen;
	public static BlockIIFluid block_gas_oxygen;
	public static BlockIIFluid block_gas_chlorine;

	public static Fluid fluid_ink_black;
	public static Fluid fluid_ink_cyan;
	public static Fluid fluid_ink_magenta;
	public static Fluid fluid_ink_yellow;

	public static Fluid fluid_etching_acid;

	public static Fluid fluid_brine;
	public static Fluid gas_hydrogen;
	public static Fluid gas_oxygen;
	public static Fluid gas_chlorine;

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
				PropertyEnum.create("type", IIBlockTypes_Metal.class), ItemBlockIEBase.class, false).setOpaque(true)
				.setHardness(4.0F).setResistance(6.0F);
		block_metal_storage = (BlockIIBase)new BlockIIBase("storage", Material.IRON,
				PropertyEnum.create("type", IIBlockTypes_Metal.class), ItemBlockIEBase.class, false).setOpaque(true)
				.setHardness(4.0F).setResistance(5.0F);

		block_metal_decoration = (BlockIIBase)new BlockIIBase("metal_decoration", Material.IRON,
				PropertyEnum.create("type", IIBlockTypes_MetalDecoration.class), ItemBlockIEBase.class, false)
				.setBlockLayer(BlockRenderLayer.CUTOUT).setHardness(3.0F).setResistance(15.0F);

		block_stone_decoration = new BlockIIStoneDecoration();

		block_metal_device = new BlockIIMetalDevice();

		block_data_connector = new BlockIIDataConnector();

		block_small_crate = new BlockIISmallCrate();

		block_wooden_multiblock = new BlockIIWoodenMultiblock();

		block_metal_multiblock = new BlockIIMetalMultiblock();

		block_fluid_ink_black = new BlockIIFluid("ink", fluid_ink_black, Material.WATER);
		block_fluid_ink_cyan = new BlockIIFluid("ink_cyan", fluid_ink_cyan, Material.WATER);
		block_fluid_ink_magenta = new BlockIIFluid("ink_magenta", fluid_ink_magenta, Material.WATER);
		block_fluid_ink_yellow = new BlockIIFluid("ink_yellow", fluid_ink_yellow, Material.WATER);
		block_fluid_etching_acid = new BlockIIFluid("etching_acid", fluid_etching_acid, Material.WATER);

		block_fluid_brine = new BlockIIFluid("brine", fluid_brine, Material.WATER);
		block_gas_hydrogen = new BlockIIFluid("hydrogen", gas_hydrogen, Material.WATER);
		block_gas_oxygen = new BlockIIFluid("oxygen", gas_oxygen, Material.WATER);
		block_gas_chlorine = new BlockIIFluid("chlorine", gas_chlorine, Material.WATER);

		for(Block block : blocks)
			event.getRegistry().register(block.setRegistryName(createRegistryName(block.getTranslationKey())));

		registerFeedthroughForWiretype(IIWireType.DATA, new ResourceLocation(ImmersiveIntelligence.MODID, "block/empty.obj"),
				new ResourceLocation(ImmersiveIntelligence.MODID, "blocks/data_connector_feedtrough"), new float[]{0, 4, 8, 12},
				0.09375, ImmersiveIntelligence.proxy.block_data_connector.getStateFromMeta(IIBlockTypes_Connector.DATA_RELAY.getMeta()),
				0, 0, (f) -> f);

	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		ImmersiveIntelligence.logger.info("Registering Items");

		for(Item item : items)
			event.getRegistry().register(item.setRegistryName(createRegistryName(item.getTranslationKey())));

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
		IGuiItem gui = (IGuiItem)stack.getItem();
		player.openGui(ImmersiveIntelligence.INSTANCE, gui.getGuiID(stack), player.world, (int)player.posX, (int)player.posY, (int)player.posZ);
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
		OreDictionary.registerOre("circuitBasicEtched", new ItemStack(item_material, 1, item_material.getMetaBySubname("basic_circuit_board_etched")));
		OreDictionary.registerOre("chipBasic", new ItemStack(item_material, 1, item_material.getMetaBySubname("basic_electronic_element")));

		//Advanced Circuit Board
		OreDictionary.registerOre("circuitAdvancedRaw", new ItemStack(item_material, 1, item_material.getMetaBySubname("advanced_circuit_board_raw")));
		OreDictionary.registerOre("circuitAdvancedEtched", new ItemStack(item_material, 1, item_material.getMetaBySubname("advanced_circuit_board_etched")));
		OreDictionary.registerOre("chipAdvanced", new ItemStack(item_material, 1, item_material.getMetaBySubname("advanced_electronic_element")));
		OreDictionary.registerOre("circuitAdvanced", new ItemStack(item_material, 1, item_material.getMetaBySubname("advanced_circuit_board")));

		//Processor Circuit Board
		OreDictionary.registerOre("circuitProcessorEtched", new ItemStack(item_material, 1, item_material.getMetaBySubname("processor_circuit_board_etched")));
		OreDictionary.registerOre("circuitProcessorRaw", new ItemStack(item_material, 1, item_material.getMetaBySubname("processor_circuit_board_raw")));
		OreDictionary.registerOre("chipProcessor", new ItemStack(item_material, 1, item_material.getMetaBySubname("processor_electronic_element")));
		OreDictionary.registerOre("circuitProcessor", new ItemStack(item_material, 1, item_material.getMetaBySubname("processor_circuit_board")));

		OreDictionary.registerOre("circuitEliteEtched", new ItemStack(item_material, 1, item_material.getMetaBySubname("processor_circuit_board_etched")));
		OreDictionary.registerOre("circuitEliteRaw", new ItemStack(item_material, 1, item_material.getMetaBySubname("processor_circuit_board_raw")));
		OreDictionary.registerOre("circuitElite", new ItemStack(item_material, 1, item_material.getMetaBySubname("processor_circuit_board")));
		OreDictionary.registerOre("chipElite", new ItemStack(item_material, 1, item_material.getMetaBySubname("processor_electronic_element")));

		OreDictionary.registerOre("electricEngineSmall", new ItemStack(item_material, 1, item_material.getMetaBySubname("compact_electric_engine")));
		OreDictionary.registerOre("electricEngineCompact", new ItemStack(item_material, 1, item_material.getMetaBySubname("compact_electric_engine")));

		//Platinum
		OreDictionary.registerOre("ingotPlatinum", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_ingot_platinum")));
		OreDictionary.registerOre("dustPlatinum", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_dust_platinum")));
		OreDictionary.registerOre("platePlatinum", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_plate_platinum")));
		OreDictionary.registerOre("nuggetPlatinum", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_nugget_platinum")));
		OreDictionary.registerOre("orePlatinum", new ItemStack(block_ore, 1, IIBlockTypes_Ore.PLATINUM.getMeta()));
		OreDictionary.registerOre("blockPlatinum", new ItemStack(block_metal_storage, 1, IIBlockTypes_Metal.PLATINUM.getMeta()));
		OreDictionary.registerOre("sheetmetalPlatinum", new ItemStack(block_sheetmetal, 1, IIBlockTypes_Metal.PLATINUM.getMeta()));

		//Zinc

		OreDictionary.registerOre("ingotZinc", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_ingot_zinc")));
		OreDictionary.registerOre("dustZinc", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_dust_zinc")));
		OreDictionary.registerOre("plateZinc", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_plate_zinc")));
		OreDictionary.registerOre("nuggetZinc", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_nugget_zinc")));
		OreDictionary.registerOre("oreZinc", new ItemStack(block_ore, 1, IIBlockTypes_Ore.ZINC.getMeta()));
		OreDictionary.registerOre("blockZinc", new ItemStack(block_metal_storage, 1, IIBlockTypes_Metal.ZINC.getMeta()));
		OreDictionary.registerOre("sheetmetalZinc", new ItemStack(block_sheetmetal, 1, IIBlockTypes_Metal.ZINC.getMeta()));

		//Tungsten

		OreDictionary.registerOre("ingotTungsten", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_ingot_tungsten")));
		OreDictionary.registerOre("dustTungsten", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_dust_tungsten")));
		OreDictionary.registerOre("plateTungsten", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_plate_tungsten")));
		OreDictionary.registerOre("nuggetTungsten", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_nugget_tungsten")));
		OreDictionary.registerOre("oreTungsten", new ItemStack(block_ore, 1, IIBlockTypes_Ore.TUNGSTEN.getMeta()));
		OreDictionary.registerOre("blockTungsten", new ItemStack(block_metal_storage, 1, IIBlockTypes_Metal.TUNGSTEN.getMeta()));
		OreDictionary.registerOre("sheetmetalTungsten", new ItemStack(block_sheetmetal, 1, IIBlockTypes_Metal.TUNGSTEN.getMeta()));

		OreDictionary.registerOre("ingotWolfram", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_ingot_tungsten")));
		OreDictionary.registerOre("dustWolfram", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_dust_tungsten")));
		OreDictionary.registerOre("plateWolfram", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_plate_tungsten")));
		OreDictionary.registerOre("nuggetWolfram", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_nugget_tungsten")));
		OreDictionary.registerOre("oreWolfram", new ItemStack(block_ore, 1, IIBlockTypes_Ore.TUNGSTEN.getMeta()));
		OreDictionary.registerOre("blockWolfram", new ItemStack(block_metal_storage, 1, IIBlockTypes_Metal.TUNGSTEN.getMeta()));
		OreDictionary.registerOre("sheetmetalWolfram", new ItemStack(block_sheetmetal, 1, IIBlockTypes_Metal.TUNGSTEN.getMeta()));

		//Brass

		OreDictionary.registerOre("ingotBrass", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_ingot_brass")));
		OreDictionary.registerOre("dustBrass", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_dust_brass")));
		OreDictionary.registerOre("plateBrass", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_plate_brass")));
		OreDictionary.registerOre("nuggetBrass", new ItemStack(item_material, 1, item_material.getMetaBySubname("metal_nugget_brass")));
		//Not this time ^^
		//OreDictionary.registerOre("oreBrass", new ItemStack(block_ore, 1, IIBlockTypes_Ore.BRASS.getMeta()));
		OreDictionary.registerOre("blockBrass", new ItemStack(block_metal_storage, 1, IIBlockTypes_Metal.BRASS.getMeta()));
		OreDictionary.registerOre("sheetmetalBrass", new ItemStack(block_sheetmetal, 1, IIBlockTypes_Metal.BRASS.getMeta()));

		//Punchtapes
		OreDictionary.registerOre("punchtapeEmpty", new ItemStack(item_material, 1, item_material.getMetaBySubname("punchtape_empty")));
		OreDictionary.registerOre("punchtape", new ItemStack(item_punchtape, 1, 0));

		OreDictionary.registerOre("pageEmpty", new ItemStack(item_printed_page, 1, 0));
		OreDictionary.registerOre("pageText", new ItemStack(item_printed_page, 1, 1));
		OreDictionary.registerOre("pageWritten", new ItemStack(item_printed_page, 1, 1));

		OreDictionary.registerOre("pageCode", new ItemStack(item_printed_page, 1, 2));
		OreDictionary.registerOre("pageWritten", new ItemStack(item_printed_page, 1, 2));
		OreDictionary.registerOre("pageBlueprint", new ItemStack(item_printed_page, 1, 3));
		OreDictionary.registerOre("pageWritten", new ItemStack(item_printed_page, 1, 3));

		OreDictionary.registerOre("materialTNT", new ItemStack(Blocks.TNT, 1, 0));
		OreDictionary.registerOre("materialRDX", new ItemStack(item_material, 1, item_material.getMetaBySubname("dust_rdx")));
		OreDictionary.registerOre("materialHexogen", new ItemStack(item_material, 1, item_material.getMetaBySubname("dust_rdx")));
		OreDictionary.registerOre("materialHMX", new ItemStack(item_material, 1, item_material.getMetaBySubname("dust_hmx")));

		OreDictionary.registerOre("dustWhitePhosphorus", new ItemStack(item_material, 1, item_material.getMetaBySubname("white_phosphorus")));
		OreDictionary.registerOre("whitePhosphorus", new ItemStack(item_material, 1, item_material.getMetaBySubname("white_phosphorus")));

		OreDictionary.registerOre("dustSalt", new ItemStack(item_material, 1, item_material.getMetaBySubname("dust_salt")));
		OreDictionary.registerOre("oreSalt", new ItemStack(block_ore, 1, IIBlockTypes_Ore.SALT.getMeta()));
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

	public void preInit()
	{
		IIWireType.init();
		IIPacketHandler.preInit();

		fluid_ink_black = makeFluid("ink", 1500, 2250);
		fluid_ink_cyan = makeFluid("ink_cyan", 1500, 2250);
		fluid_ink_magenta = makeFluid("ink_magenta", 1500, 2250);
		fluid_ink_yellow = makeFluid("ink_yellow", 1500, 2250);

		fluid_etching_acid = makeFluid("etching_acid", 1500, 2250);

		fluid_brine = makeFluid("brine", 1500, 2250);
		gas_hydrogen = makeFluid("hydrogen", 0, 2250).setGaseous(true);
		gas_oxygen = makeFluid("oxygen", 0, 2250).setGaseous(true);
		gas_chlorine = makeFluid("chlorine", 0, 2250).setGaseous(true);

		//ALWAYS REGISTER BULLETS IN PRE-INIT! (so they get their texture registered before TextureStitchEvent.Pre)
		//Bullets

		BulletRegistry.INSTANCE.registerCasing(this.item_casing_artillery, "artillery_8bCal");
		BulletRegistry.INSTANCE.registerCasing(this.item_casing_grenade, "grenade_4bCal");

		BulletRegistry.INSTANCE.registerComponent(new BulletComponentTNT(), "TNT");
		BulletRegistry.INSTANCE.registerComponent(new BulletComponentRDX(), "RDX");
		BulletRegistry.INSTANCE.registerComponent(new BulletComponentHMX(), "HMX");
		BulletRegistry.INSTANCE.registerComponent(new BulletComponentWhitePhosphorus(), "white_phosphorus");

		BulletRegistry.INSTANCE.registerBulletCore(new BulletCoreSteel(), "CoreSteel");
		BulletRegistry.INSTANCE.registerBulletCore(new BulletCoreTungsten(), "CoreTungsten");
		BulletRegistry.INSTANCE.registerBulletCore(new BulletCoreBrass(), "CoreBrass");

	}

	public void init()
	{
		IEApi.forbiddenInCrates.add((stack) ->
		{
			if(OreDictionary.itemMatches(new ItemStack(block_metal_device, 1, 0), stack, true))
				return true;
			return OreDictionary.itemMatches(new ItemStack(block_metal_device, 1, 1), stack, true);
		});

		IEApi.forbiddenInCrates.add((stack) -> stack.getItem() instanceof ItemBlockIEBase&&((ItemBlockIEBase)stack.getItem()).getBlock() instanceof BlockIISmallCrate);

		ImmersiveIntelligence.logger.info("Adding oregen");
		addConfiguredWorldgen(block_ore.getStateFromMeta(IIBlockTypes_Ore.PLATINUM.getMeta()), "platinum", Ores.ore_platinum);
		addConfiguredWorldgen(block_ore.getStateFromMeta(IIBlockTypes_Ore.ZINC.getMeta()), "zinc", Ores.ore_zinc);
		addConfiguredWorldgen(block_ore.getStateFromMeta(IIBlockTypes_Ore.TUNGSTEN.getMeta()), "tungsten", Ores.ore_tungsten);
		addConfiguredWorldgen(block_ore.getStateFromMeta(IIBlockTypes_Ore.SALT.getMeta()), "salt", Ores.ore_salt);

		ImmersiveIntelligence.logger.info("Adding TileEntities");
		registerTile(TileEntityMetalCrate.class);
		registerTile(TileEntityAmmunitionCrate.class);
		registerTile(TileEntitySmallCrate.class);
		registerTile(TileEntityAlarmSiren.class);

		registerTile(TileEntityInserter.class);
		registerTile(TileEntityAdvancedInserter.class);
		registerTile(TileEntityFluidInserter.class);

		registerTile(TileEntityTimedBuffer.class);
		registerTile(TileEntityRedstoneBuffer.class);
		registerTile(TileEntitySmallDataBuffer.class);

		//Only for debug purposes
		registerTile(TileEntityDataDebugger.class);

		registerTile(TileEntityDataConnector.class);
		registerTile(TileEntityDataRelay.class);

		registerTile(TileEntitySandbags.class);

		registerTile(TileEntitySkyCrateStationParent.class);
		registerTile(TileEntitySkyCrateStation.class);

		registerTile(TileEntityRadioStation.class);
		registerTile(TileEntityDataInputMachine.class);
		registerTile(TileEntityArithmeticLogicMachine.class);
		registerTile(TileEntityPrintingPress.class);
		registerTile(TileEntityChemicalBath.class);
		registerTile(TileEntityElectrolyzer.class);
		registerTile(TileEntityConveyorScanner.class);
		registerTile(TileEntityPrecissionAssembler.class);
		registerTile(TileEntityArtilleryHowitzer.class);
		registerTile(TileEntityAmmunitionFactory.class);

		MultiblockHandler.registerMultiblock(MultiblockSkyCrateStation.instance);
		MultiblockHandler.registerMultiblock(MultiblockRadioStation.instance);
		MultiblockHandler.registerMultiblock(MultiblockDataInputMachine.instance);
		MultiblockHandler.registerMultiblock(MultiblockArithmeticLogicMachine.instance);
		MultiblockHandler.registerMultiblock(MultiblockPrintingPress.instance);
		MultiblockHandler.registerMultiblock(MultiblockChemicalBath.instance);
		MultiblockHandler.registerMultiblock(MultiblockElectrolyzer.instance);
		MultiblockHandler.registerMultiblock(MultiblockConveyorScanner.instance);
		MultiblockHandler.registerMultiblock(MultiblockPrecissionAssembler.instance);
		MultiblockHandler.registerMultiblock(MultiblockArtilleryHowitzer.instance);
		MultiblockHandler.registerMultiblock(MultiblockAmmunitionFactory.instance);

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

		//Entities

		//TODO:Skycrates, maybe someday they will be a thing ^^
		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "skycrate"),
				EntitySkyCrate.class, "skycrate", i++, ImmersiveIntelligence.INSTANCE, 64, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "bullet"),
				EntityBullet.class, "bullet", i++, ImmersiveIntelligence.INSTANCE, 64, 1, true);

		//Crafting

		LighterFuelHandler.addFuel(FluidRegistry.getFluid("creosote"), 100);
		LighterFuelHandler.addFuel(FluidRegistry.getFluid("ethanol"), 20);
		// LighterFuelHandler.addFuel(FluidRegistry.getFluid("creosote"),100);

		ArcFurnaceRecipe.addRecipe(new ItemStack(item_material, 1, item_material.getMetaBySubname("advanced_plate_blend")), "dustAdvancedPlate", ItemStack.EMPTY, 600, 640);

		//Remove all circuit blueprint recipes
		BlueprintCraftingRecipe.recipeList.removeAll("components");
		BlueprintCraftingRecipe.addRecipe("components", new ItemStack(IEContent.itemMaterial, 1, 8), "plateIron", "plateIron", "ingotCopper");
		BlueprintCraftingRecipe.addRecipe("components", new ItemStack(IEContent.itemMaterial, 1, 9), "plateSteel", "plateSteel", "ingotCopper");

		BlueprintCraftingRecipe.addRecipe("basic_circuits", new ItemStack(item_material, 1, item_material.getMetaBySubname("basic_circuit_board_raw")), new ItemStack(IEContent.blockStoneDecoration, 2, BlockTypes_StoneDecoration.INSULATING_GLASS.getMeta()), "plateCopper");
		BlueprintCraftingRecipe.addRecipe("basic_circuits", new ItemStack(IEContent.itemMaterial, 1, 27), "circuitBasicEtched", new IngredientStack("chipBasic", 2));

		BlueprintCraftingRecipe.addRecipe("advanced_circuits", new ItemStack(item_material, 1, item_material.getMetaBySubname("advanced_circuit_board_raw")), new IngredientStack("circuitBasicRaw", 2), "plateAdvancedElectronicAlloy");
		BlueprintCraftingRecipe.addRecipe("advanced_circuits", new ItemStack(item_material, 1, item_material.getMetaBySubname("advanced_circuit_board")), "circuitAdvancedEtched", new IngredientStack("chipAdvanced", 3));

		BlueprintCraftingRecipe.addRecipe("processors", new ItemStack(item_material, 1, item_material.getMetaBySubname("processor_circuit_board_raw")), new IngredientStack("circuitAdvancedRaw", 4), new IngredientStack("plateAdvancedElectronicAlloy", 2));
		BlueprintCraftingRecipe.addRecipe("processors", new ItemStack(item_material, 1, item_material.getMetaBySubname("processor_circuit_board")), "circuitProcessorEtched", new IngredientStack("chipProcessor", 4));
//((IForgeRegistryModifiable)CraftingManager.REGISTRY).remove(new ResourceLocation(""));

		//It's cheap, believe me
		BathingRecipe.addRecipe(new ItemStack(item_material, 1, item_material.getMetaBySubname("basic_circuit_board_etched")), new IngredientStack("circuitBasicRaw"), FluidRegistry.getFluidStack("etching_acid", 1000), 15000, 360);
		BathingRecipe.addRecipe(new ItemStack(item_material, 1, item_material.getMetaBySubname("advanced_circuit_board_etched")), new IngredientStack("circuitAdvancedRaw"), FluidRegistry.getFluidStack("etching_acid", 2000), 150000, 560);
		BathingRecipe.addRecipe(new ItemStack(item_material, 1, item_material.getMetaBySubname("processor_circuit_board_etched")), new IngredientStack("circuitProcessorRaw"), FluidRegistry.getFluidStack("etching_acid", 4000), 1500000, 720);

		//Immersive Engineering can into space???
		ElectrolyzerRecipe.addRecipe(FluidRegistry.getFluidStack("water", 3000), FluidRegistry.getFluidStack("oxygen", 1000), FluidRegistry.getFluidStack("hydrogen", 2000), 640, 320);
		ElectrolyzerRecipe.addRecipe(FluidRegistry.getFluidStack("brine", 3000), FluidRegistry.getFluidStack("chlorine", 1500), FluidRegistry.getFluidStack("hydrogen", 1500), 640, 320);

		MixerRecipe.addRecipe(new FluidStack(fluid_etching_acid, 1000), new FluidStack(gas_chlorine, 500), new Object[]{"dustIron"}, 4800);
		MixerRecipe.addRecipe(new FluidStack(fluid_brine, 750), new FluidStack(FluidRegistry.WATER, 750), new Object[]{"dustSalt"}, 3200);

		//2x Vacuum tube + 3 x copper nugget = 2 x copper wire, 1 x iron plate, 1 x glass block
		PrecissionAssemblerRecipe.addRecipe(
				new ItemStack(IEContent.itemMaterial, 2, 26),
				new ItemStack(IEContent.itemMetal, 1, 20),

				new IngredientStack[]{new IngredientStack("plateIron"), new IngredientStack("wireCopper", 2), new IngredientStack("blockGlass")},

				new String[]{"inserter", "solderer", "drill"},
				new String[]{"drill work main", "solderer work first", "inserter pick first", "inserter drop main", "solderer work main", "drill work second", "inserter pick second", "inserter drop main"},

				12000,
				1.0f
		);

		//Blu, pls add getting subitem stacks by name ^^ (not relevant to 1.13, i won't be porting my mod there anyway)
		//1x Basic Electronic Component =  2x vacuum tube + nickel plate + 4 x redstone dust
		PrecissionAssemblerRecipe.addRecipe(
				new ItemStack(item_material, 1, item_material.getMetaBySubname("basic_electronic_element")),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("electronTube"), new IngredientStack("plateNickel"), new IngredientStack("dustRedstone", 4)},

				new String[]{"inserter", "solderer", "drill"},
				new String[]{"inserter pick second", "drill work main", "inserter drop main", "solderer work main", "inserter pick first", "inserter drop main"},

				18000,
				1.0f
		);

		//CrusherRecipe.addRecipe(ItemStack(item_material,1,item_material.getMetaBySubname("dust_salt")),new IngredientStack("oreSalt"),3200);


	}

	public void postInit()
	{

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
			else if(ID==IIGuiList.GUI_SMALL_CRATE&&te instanceof TileEntitySmallCrate)
				gui = new ContainerSmallCrate(player.inventory, (TileEntitySmallCrate)te);

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
			else if(ID==IIGuiList.GUI_CHEMICAL_BATH&&te instanceof TileEntityChemicalBath)
				gui = new ContainerChemicalBath(player.inventory, (TileEntityChemicalBath)te);
			else if(ID==IIGuiList.GUI_ELECTROLYZER&&te instanceof TileEntityElectrolyzer)
				gui = new ContainerElectrolyzer(player.inventory, (TileEntityElectrolyzer)te);
			else if(ID==IIGuiList.GUI_PRECISSION_ASSEMBLER&&te instanceof TileEntityPrecissionAssembler)
				gui = new ContainerPrecissionAssembler(player.inventory, (TileEntityPrecissionAssembler)te);
			else if(ID==IIGuiList.GUI_AMMUNITION_FACTORY&&te instanceof TileEntityAmmunitionFactory)
				gui = new ContainerAmmunitionFactory(player.inventory, (TileEntityAmmunitionFactory)te);

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

	public void renderTile(TileEntity te)
	{
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

	public Fluid makeFluid(String name, int density, int viscosity)
	{
		Fluid fl = new Fluid(
				name,
				new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/fluid/"+name+"_still"),
				new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/fluid/"+name+"_flow")
		).setDensity(density).setViscosity(viscosity);
		if(!FluidRegistry.registerFluid(fl))
			fl = FluidRegistry.getFluid(name);
		FluidRegistry.addBucketForFluid(fl);
		ImmersiveIntelligence.creativeTab.fluidBucketMap.add(fl);
		return fl;
	}
}
