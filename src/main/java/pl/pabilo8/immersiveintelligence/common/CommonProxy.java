package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MixerRecipe;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice1;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDevice0;
import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityWatermill;
import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityWindmill;
import blusunrize.immersiveengineering.common.crafting.RecipeRGBColouration;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IGuiItem;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.MechanicalDevices;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Ores;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.LighterFuelHandler;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.Shrapnel;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.crafting.ElectrolyzerRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecissionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.api.rotary.CapabilityRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils;
import pl.pabilo8.immersiveintelligence.api.utils.MinecartBlockHelper;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIFluid;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIISlab;
import pl.pabilo8.immersiveintelligence.common.blocks.cloth.BlockIIClothDecoration;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.BlockIIMetalMultiblock0;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.BlockIIMetalMultiblock1;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.MultiblockRedstoneInterface;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityRedstoneInterface;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.*;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.*;
import pl.pabilo8.immersiveintelligence.common.blocks.stone.BlockIIStoneDecoration;
import pl.pabilo8.immersiveintelligence.common.blocks.stone.TileEntitySandbags;
import pl.pabilo8.immersiveintelligence.common.blocks.types.*;
import pl.pabilo8.immersiveintelligence.common.bullets.BulletComponentFirework;
import pl.pabilo8.immersiveintelligence.common.bullets.BulletComponentTracerPowder;
import pl.pabilo8.immersiveintelligence.common.bullets.BulletComponentWhitePhosphorus;
import pl.pabilo8.immersiveintelligence.common.bullets.cores.*;
import pl.pabilo8.immersiveintelligence.common.bullets.explosives.BulletComponentHMX;
import pl.pabilo8.immersiveintelligence.common.bullets.explosives.BulletComponentRDX;
import pl.pabilo8.immersiveintelligence.common.bullets.explosives.BulletComponentTNT;
import pl.pabilo8.immersiveintelligence.common.bullets.shrapnel.BulletComponentShrapnel;
import pl.pabilo8.immersiveintelligence.common.entity.EntityCamera;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.entity.EntitySkyCrate;
import pl.pabilo8.immersiveintelligence.common.entity.EntitySkycrateInternal;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityShrapnel;
import pl.pabilo8.immersiveintelligence.common.entity.minecarts.*;
import pl.pabilo8.immersiveintelligence.common.gui.*;
import pl.pabilo8.immersiveintelligence.common.gui.arithmetic_logic_machine.*;
import pl.pabilo8.immersiveintelligence.common.gui.data_input_machine.ContainerDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.gui.data_input_machine.ContainerDataInputMachineVariables;
import pl.pabilo8.immersiveintelligence.common.items.*;
import pl.pabilo8.immersiveintelligence.common.items.bullet_casings.*;
import pl.pabilo8.immersiveintelligence.common.items.material.*;
import pl.pabilo8.immersiveintelligence.common.items.mechanical.ItemIIMotorBelt;
import pl.pabilo8.immersiveintelligence.common.items.mechanical.ItemIIMotorGear;
import pl.pabilo8.immersiveintelligence.common.items.tools.*;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIMachinegun;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIWeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageBlockDamageSync;
import pl.pabilo8.immersiveintelligence.common.wire.IIDataWireType;
import pl.pabilo8.immersiveintelligence.common.world.IIWorldGen;
import pl.pabilo8.immersiveintelligence.common.world.IIWorldGen.EnumOreType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Predicate;

import static blusunrize.immersiveengineering.api.energy.wires.WireApi.registerFeedthroughForWiretype;

/**
 * Created by Pabilo8 on 2019-05-07.
 */
@Mod.EventBusSubscriber(modid = ImmersiveIntelligence.MODID)
public abstract class CommonProxy implements IGuiHandler
{
	public static final List<Block> blocks = new ArrayList<>();
	public static final List<Item> items = new ArrayList<>();

	public static final String description_key = "desc."+ImmersiveIntelligence.MODID+".";
	public static final String info_key = "info."+ImmersiveIntelligence.MODID+".";
	public static final String data_key = "datasystem."+ImmersiveIntelligence.MODID+".";
	public static final String rotary_key = "rotary."+ImmersiveIntelligence.MODID+".";
	public static final String block_key = "tile."+ImmersiveIntelligence.MODID+".";

	public static final String TOOL_ADVANCED_HAMMER = "II_ADVANCED_HAMMER";
	public static final String TOOL_WRENCH = "II_WRENCH";
	public static final String TOOL_ADVANCED_WRENCH = "II_ADVANCED_WRENCH";
	public static final String TOOL_CROWBAR = "II_CROWBAR";
	public static final String TOOL_TACHOMETER = "TOOL_TACHOMETER";

	public static ItemIIMaterial item_material = new ItemIIMaterial();

	public static ItemIIMaterialIngot item_material_ingot = new ItemIIMaterialIngot();
	public static ItemIIMaterialPlate item_material_plate = new ItemIIMaterialPlate();
	public static ItemIIMaterialDust item_material_dust = new ItemIIMaterialDust();
	public static ItemIIMaterialNugget item_material_nugget = new ItemIIMaterialNugget();
	public static ItemIIMaterialWire item_material_wire = new ItemIIMaterialWire();
	public static ItemIIMaterialSpring item_material_spring = new ItemIIMaterialSpring();
	public static ItemIIMaterialGem item_material_gem = new ItemIIMaterialGem();

	public static ItemIIMetalPressMold item_mold = new ItemIIMetalPressMold();

	public static ItemIIFunctionalCircuit item_circuit = new ItemIIFunctionalCircuit();
	public static ItemIIMotorBelt item_motor_belt = new ItemIIMotorBelt();
	public static ItemIIMotorGear item_motor_gear = new ItemIIMotorGear();
	public static ItemIISkycrateMount item_skycrate_mount = new ItemIISkycrateMount();

	public static ItemIILighter item_lighter = new ItemIILighter();
	public static ItemIIElectricHammer item_hammer = new ItemIIElectricHammer();

	public static List<Predicate<IBlockState>> hammer_blacklist = new ArrayList<>();

	public static ItemIIElectricWirecutter item_wirecutter = new ItemIIElectricWirecutter();

	public static ItemIIWrench item_wrench = new ItemIIWrench();
	public static ItemIIElectricWrench item_electric_wrench = new ItemIIElectricWrench();

	//Don't know if i should make a seperate item for a torque meter
	public static ItemIITachometer item_tachometer = new ItemIITachometer();

	public static ItemIIDataWireCoil item_data_wire_coil = new ItemIIDataWireCoil();
	public static ItemIIMinecart item_minecart = new ItemIIMinecart();
	public static ItemIIRadioConfigurator item_radio_configurator = new ItemIIRadioConfigurator();
	public static ItemIIMeasuringCup item_measuring_cup = new ItemIIMeasuringCup();
	public static ItemIIPrecissionTool item_precission_tool = new ItemIIPrecissionTool();
	public static ItemIIAssemblyScheme item_assembly_scheme = new ItemIIAssemblyScheme();
	public static ItemIIBinoculars item_binoculars = new ItemIIBinoculars();

	public static ItemIIMachinegun item_machinegun = new ItemIIMachinegun();
	public static ItemIIWeaponUpgrade item_machinegun_upgrade = new ItemIIWeaponUpgrade();

	public static ItemIIBullet item_bullet = new ItemIIBullet();
	public static ItemIICasingArtillery item_casing_artillery = new ItemIICasingArtillery();
	public static ItemIICasingGrenade item_casing_grenade = new ItemIICasingGrenade();

	public static ItemIICasingMachinegun item_casing_machinegun = new ItemIICasingMachinegun();
	public static ItemIICasingSubmachinegun item_casing_submachinegun = new ItemIICasingSubmachinegun();
	public static ItemIICasingRevolver item_casing_revolver = new ItemIICasingRevolver();

	public static ItemIIBulletMagazine item_bullet_magazine = new ItemIIBulletMagazine();

	public static ItemIIPunchtape item_punchtape = new ItemIIPunchtape();
	public static ItemIIPrintedPage item_printed_page = new ItemIIPrintedPage();
	public static ItemIITracerPowder item_tracer_powder = new ItemIITracerPowder();

	public static BlockIIBase<IIBlockTypes_Ore> block_ore = (BlockIIBase)new BlockIIBase("ore", Material.ROCK,
			PropertyEnum.create("type", IIBlockTypes_Ore.class), ItemBlockIEBase.class, false).setOpaque(true)
			.setHardness(3.0F).setResistance(10.0F);
	public static BlockIIBase<IIBlockTypes_Metal> block_sheetmetal = (BlockIIBase)new BlockIIBase("sheetmetal", Material.IRON,
			PropertyEnum.create("type", IIBlockTypes_Metal.class), ItemBlockIEBase.class, false).setOpaque(true)
			.setHardness(3.0F).setResistance(10.0F), block_metal_storage = (BlockIIBase)new BlockIIBase("storage", Material.IRON,
			PropertyEnum.create("type", IIBlockTypes_Metal.class), ItemBlockIEBase.class, false).setOpaque(true)
			.setHardness(3.0F).setResistance(10.0F);

	public static BlockIIBase<IIBlockTypes_Metal> block_metal_slabs = (BlockIIBase<IIBlockTypes_Metal>)new BlockIISlab("storage_slab", Material.IRON, PropertyEnum.create("type", IIBlockTypes_Metal.class)).setHardness(3.0F).setResistance(10.0F);
	public static BlockIIBase<IIBlockTypes_Metal> block_sheetmetal_slabs = (BlockIIBase<IIBlockTypes_Metal>)new BlockIISlab("sheetmetal_slab", Material.IRON, PropertyEnum.create("type", IIBlockTypes_Metal.class)).setHardness(3.0F).setResistance(10.0F);

	public static BlockIIBase<IIBlockTypes_StoneDecoration> block_stone_decoration = new BlockIIStoneDecoration();

	public static BlockIIBase<IIBlockTypes_ClothDecoration> block_cloth_decoration = new BlockIIClothDecoration();

	public static BlockIIBase<IIBlockTypes_MetalDecoration> block_metal_decoration = new BlockIIMetalDecoration();

	public static BlockIIMetalDevice block_metal_device = new BlockIIMetalDevice();
	public static BlockIIDataConnector block_data_connector = new BlockIIDataConnector();
	public static BlockIISmallCrate block_small_crate = new BlockIISmallCrate();

	public static BlockIIMechanicalDevice block_mechanical_device = new BlockIIMechanicalDevice();
	public static BlockIIGearbox block_gearbox = new BlockIIGearbox();
	public static BlockIIMechanicalConnector block_mechanical_connector = new BlockIIMechanicalConnector();

	public static BlockIIWoodenMultiblock block_wooden_multiblock = new BlockIIWoodenMultiblock();
	public static BlockIIMetalMultiblock0 block_metal_multiblock0 = new BlockIIMetalMultiblock0();
	public static BlockIIMetalMultiblock1 block_metal_multiblock1 = new BlockIIMetalMultiblock1();

	public static BlockIIFluid block_fluid_ink_black;
	public static BlockIIFluid block_fluid_ink_cyan;
	public static BlockIIFluid block_fluid_ink_magenta;
	public static BlockIIFluid block_fluid_ink_yellow;
	public static BlockIIFluid block_fluid_etching_acid;
	public static BlockIIFluid block_fluid_sulfuric_acid;
	public static BlockIIFluid block_fluid_hydrofluoric_acid;

	public static BlockIIFluid block_fluid_brine;
	public static BlockIIFluid block_gas_hydrogen;
	public static BlockIIFluid block_gas_oxygen;
	public static BlockIIFluid block_gas_chlorine;

	public static Fluid fluid_ink_black;
	public static Fluid fluid_ink_cyan;
	public static Fluid fluid_ink_magenta;
	public static Fluid fluid_ink_yellow;

	public static Fluid fluid_etching_acid;
	public static Fluid fluid_sulfuric_acid;
	public static Fluid fluid_hydrofluoric_acid;

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
		block_fluid_ink_black = new BlockIIFluid("ink", fluid_ink_black, Material.WATER);
		block_fluid_ink_cyan = new BlockIIFluid("ink_cyan", fluid_ink_cyan, Material.WATER);
		block_fluid_ink_magenta = new BlockIIFluid("ink_magenta", fluid_ink_magenta, Material.WATER);
		block_fluid_ink_yellow = new BlockIIFluid("ink_yellow", fluid_ink_yellow, Material.WATER);
		block_fluid_etching_acid = new BlockIIFluid("etching_acid", fluid_etching_acid, Material.WATER);
		block_fluid_sulfuric_acid = new BlockIIFluid("sulfuric_acid", fluid_sulfuric_acid, Material.WATER);
		block_fluid_hydrofluoric_acid = new BlockIIFluid("hydrofluoric_acid", fluid_hydrofluoric_acid, Material.WATER);

		block_fluid_brine = new BlockIIFluid("brine", fluid_brine, Material.WATER);
		block_gas_hydrogen = new BlockIIFluid("hydrogen", gas_hydrogen, Material.WATER);
		block_gas_oxygen = new BlockIIFluid("oxygen", gas_oxygen, Material.WATER);
		block_gas_chlorine = new BlockIIFluid("chlorine", gas_chlorine, Material.WATER);

		for(Block block : blocks)
			event.getRegistry().register(block.setRegistryName(createRegistryName(block.getTranslationKey())));

		registerFeedthroughForWiretype(IIDataWireType.DATA, new ResourceLocation(ImmersiveIntelligence.MODID, "block/empty.obj"),
				new ResourceLocation(ImmersiveIntelligence.MODID, "blocks/data_connector_feedtrough"), new float[]{0, 4, 8, 12},
				0.09375, block_data_connector.getStateFromMeta(IIBlockTypes_Connector.DATA_RELAY.getMeta()),
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

	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> event)
	{
		/*POTIONS*/
		IIPotions.init();
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

	public static void addConfiguredWorldgen(IBlockState state, String name, int[] config, EnumOreType type)
	{
		if(config!=null&&config.length >= 5&&config[0] > 0)
			IIWorldGen.addOreGen(name, state, config[0], config[1], config[2], config[3], config[4], type);
	}

	public static void registerTile(Class<? extends TileEntity> tile)
	{
		String s = tile.getSimpleName();
		s = s.substring(s.indexOf("TileEntity")+"TileEntity".length());
		GameRegistry.registerTileEntity(tile, new ResourceLocation(ImmersiveIntelligence.MODID+":"+s));
	}

	public static void registerOreDict()
	{
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

		registerItemOredict(item_material, "compact_electric_engine", "electricEngineSmall", "electricEngineCompact");
		registerItemOredict(item_material, "compact_electric_engine_advanced", "electricEngineSmallAdvanced", "electricEngineCompactAdvanced");

		registerMetalOredict(item_material_ingot, "ingot");
		registerMetalOredict(item_material_plate, "plate");
		registerMetalOredict(item_material_dust, "dust");
		registerMetalOredict(item_material_nugget, "nugget");
		registerMetalOredict(item_material_wire, "wire");
		registerMetalOredict(item_material_spring, "spring");

		registerMetalOredictBlock(block_ore, "ore");
		registerMetalOredictBlock(block_sheetmetal, "sheetmetal");
		registerMetalOredictBlock(block_sheetmetal_slabs, "slabSheetmetal");
		registerMetalOredictBlock(block_metal_storage, "block");
		registerMetalOredictBlock(block_metal_slabs, "slab");


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

		OreDictionary.registerOre("brushCarbon", new ItemStack(item_material, 1, item_material.getMetaBySubname("carbon_brush")));


		registerMetalOredict(item_motor_gear, "gear");
		registerMetalOredict(item_motor_belt, "belt");
	}

	private static void registerMetalOredictBlock(BlockIIBase block, String dict)
	{
		for(int i = 0; i < block.enumValues.length; i += 1)
			OreDictionary.registerOre(Utils.toCamelCase(dict+"_"+block.enumValues[i].name().toLowerCase(), true), new ItemStack(block, 1, i));
	}

	private static void registerItemOredict(ItemIIBase item, String subname, String... dicts)
	{
		for(int i = 0; i < dicts.length; i += 1)
			OreDictionary.registerOre(Utils.toCamelCase(dicts[i], true), new ItemStack(item, 1, item.getMetaBySubname(subname)));
	}

	private static void registerMetalOredict(ItemIIBase item, String dict)
	{
		for(int i = 0; i < item.getSubNames().length; i += 1)
			OreDictionary.registerOre(Utils.toCamelCase(dict+"_"+item.getSubNames()[i].toLowerCase(), true), new ItemStack(item, 1, i));
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

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
	{
		LighterFuelHandler.addFuel(FluidRegistry.getFluid("creosote"), 100);
		LighterFuelHandler.addFuel(FluidRegistry.getFluid("ethanol"), 20);
		// LighterFuelHandler.addFuel(FluidRegistry.getFluid("creosote"),100);

		final ItemStack powder = new ItemStack(item_tracer_powder, 1, 0);
		event.getRegistry().register(new RecipeRGBColouration((s) -> (OreDictionary.itemMatches(powder, s, true)), (s) -> (ItemNBTHelper.hasKey(s, "colour")?ItemNBTHelper.getInt(s, "colour"): 0xffffff), (s, i) -> ItemNBTHelper.setInt(s, "colour", i)).setRegistryName(ImmersiveEngineering.MODID, "tracer_powder_colour"));

		ArcFurnaceRecipe.addRecipe(new ItemStack(item_material, 1, item_material.getMetaBySubname("advanced_plate_blend")), "dustAdvancedPlate", ItemStack.EMPTY, 600, 640);


		for(int i = 0; i < item_mold.getSubNames().length; i++)
			BlueprintCraftingRecipe.addRecipe("molds", new ItemStack(item_mold, 1, i), "plateSteel", "plateSteel", "plateSteel", "plateSteel", "plateSteel", new ItemStack(IEContent.itemTool, 1, 1));

//((IForgeRegistryModifiable)CraftingManager.REGISTRY).remove(new ResourceLocation(""));

		IIRecipes.addCircuitRecipes();

		//Immersive Engineering can into space???
		ElectrolyzerRecipe.addRecipe(FluidRegistry.getFluidStack("water", 3000), FluidRegistry.getFluidStack("oxygen", 1000), FluidRegistry.getFluidStack("hydrogen", 2000), 640, 320);
		ElectrolyzerRecipe.addRecipe(FluidRegistry.getFluidStack("brine", 3000), FluidRegistry.getFluidStack("chlorine", 1500), FluidRegistry.getFluidStack("hydrogen", 1500), 640, 320);


		IIRecipes.addInkRecipes();

		MixerRecipe.addRecipe(new FluidStack(fluid_etching_acid, 1000), new FluidStack(gas_chlorine, 500), new Object[]{"dustIron"}, 4800);
		MixerRecipe.addRecipe(new FluidStack(fluid_sulfuric_acid, 500), new FluidStack(FluidRegistry.WATER, 1000), new Object[]{"dustSulfur"}, 4800);
		MixerRecipe.addRecipe(new FluidStack(fluid_hydrofluoric_acid, 500), new FluidStack(FluidRegistry.WATER, 1000), new Object[]{"dustFluorite"}, 5600);
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

		PrecissionAssemblerRecipe.addRecipe(
				new ItemStack(item_material, 2, item_material.getMetaBySubname("advanced_electron_tube")),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("plateSteel"), new IngredientStack("wireTungsten", 2), new IngredientStack("electronTube", 2)},

				new String[]{"inserter", "solderer", "drill"},
				new String[]{"drill work main", "inserter pick second", "inserter drop main", "inserter pick first", "inserter drop main", "solderer work main"},

				24000,
				1.25f
		);

		PrecissionAssemblerRecipe.addRecipe(
				new ItemStack(item_material, 1, item_material.getMetaBySubname("advanced_electronic_element")),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("advancedElectronTube"), new IngredientStack("chipAdvanced", 2)},

				new String[]{"inserter", "solderer", "drill"},
				new String[]{"inserter pick second", "drill work main", "inserter drop main", "solderer work main", "inserter pick first", "inserter drop main"},

				32000,
				1.25f
		);

		PrecissionAssemblerRecipe.addRecipe(
				new ItemStack(item_material, 2, item_material.getMetaBySubname("transistor")),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("plateSteel"), new IngredientStack("wireTungsten", 2), new IngredientStack("electronTube", 2)},

				new String[]{"inserter", "solderer", "drill"},
				new String[]{"drill work main", "inserter pick second", "inserter drop main", "inserter pick first", "inserter drop main", "solderer work main"},

				50000,
				1.25f
		);

		PrecissionAssemblerRecipe.addRecipe(
				new ItemStack(item_material, 1, item_material.getMetaBySubname("processor_electronic_element")),
				ItemStack.EMPTY,

				new IngredientStack[]{new IngredientStack("advancedElectronTube"), new IngredientStack("chipAdvanced", 2)},

				new String[]{"inserter", "solderer", "drill"},
				new String[]{"inserter pick second", "drill work main", "inserter drop main", "solderer work main", "inserter pick first", "inserter drop main"},

				500000,
				3f
		);
	}

	public void preInit()
	{
		IIDataWireType.init();
		IIPacketHandler.preInit();
		CapabilityRotaryEnergy.register();

		fluid_ink_black = makeFluid("ink", 1500, 2250);
		fluid_ink_cyan = makeFluid("ink_cyan", 1500, 2250);
		fluid_ink_magenta = makeFluid("ink_magenta", 1500, 2250);
		fluid_ink_yellow = makeFluid("ink_yellow", 1500, 2250);

		fluid_etching_acid = makeFluid("etching_acid", 1500, 2250);
		fluid_sulfuric_acid = makeFluid("sulfuric_acid", 1500, 2250);
		fluid_hydrofluoric_acid = makeFluid("hydrofluoric_acid", 1500, 2250);

		fluid_brine = makeFluid("brine", 1500, 2250);
		gas_hydrogen = makeFluid("hydrogen", 0, 2250).setGaseous(true);
		gas_oxygen = makeFluid("oxygen", 0, 2250).setGaseous(true);
		gas_chlorine = makeFluid("chlorine", 0, 2250).setGaseous(true);

		//ALWAYS REGISTER BULLETS IN PRE-INIT! (so they get their texture registered before TextureStitchEvent.Pre)
		//Bullets

		BulletRegistry.INSTANCE.registerCasing(item_casing_artillery, "artillery_8bCal");
		BulletRegistry.INSTANCE.registerCasing(item_casing_grenade, "grenade_4bCal");

		BulletRegistry.INSTANCE.registerCasing(item_casing_machinegun, "machinegun_2bCal");
		BulletRegistry.INSTANCE.registerCasing(item_casing_submachinegun, "submachinegun_1bCal");
		BulletRegistry.INSTANCE.registerCasing(item_casing_revolver, "revolver_1bCal");

		BulletRegistry.INSTANCE.registerComponent(new BulletComponentTNT(), "TNT");
		BulletRegistry.INSTANCE.registerComponent(new BulletComponentRDX(), "RDX");
		BulletRegistry.INSTANCE.registerComponent(new BulletComponentHMX(), "HMX");
		BulletRegistry.INSTANCE.registerComponent(new BulletComponentWhitePhosphorus(), "white_phosphorus");
		BulletRegistry.INSTANCE.registerComponent(new BulletComponentFirework(), "firework");
		BulletRegistry.INSTANCE.registerComponent(new BulletComponentTracerPowder(), "tracer_powder");

		BulletRegistry.INSTANCE.registerBulletCore(new BulletCoreSteel(), "CoreSteel");
		BulletRegistry.INSTANCE.registerBulletCore(new BulletCoreTungsten(), "CoreTungsten");
		BulletRegistry.INSTANCE.registerBulletCore(new BulletCoreBrass(), "CoreBrass");
		BulletRegistry.INSTANCE.registerBulletCore(new BulletCoreLead(), "CoreLead");
		BulletRegistry.INSTANCE.registerBulletCore(new BulletCoreUranium(), "CoreUranium");
		BulletRegistry.INSTANCE.registerBulletCore(new BulletCorePabilium(), "CorePabilium");

		//ShrapnelHandler.addShrapnel("wood","",1,0.25f,0f,true);

		ShrapnelHandler.addShrapnel("aluminum", 0xd9ecea, "immersiveengineering:textures/blocks/sheetmetal_aluminum", 1, 0.05f, 0f);
		ShrapnelHandler.addShrapnel("zinc", 0xdee3dc, "immersiveintelligence:textures/blocks/metal/sheetmetal_zinc", 1, 0.15f, 0f);
		ShrapnelHandler.addShrapnel("copper", 0xe37c26, "immersiveengineering:textures/blocks/sheetmetal_copper", 2, 0.25f, 0f);
		ShrapnelHandler.addShrapnel("platinum", 0xd8e1e1, "immersiveintelligence:textures/blocks/metal/sheetmetal_platinum", 2, 0.05f, 0f);
		ShrapnelHandler.addShrapnel("gold", 0xd1b039, "minecraft:textures/blocks/gold_block", 2, 0.25f, 0f);
		ShrapnelHandler.addShrapnel("nickel", 0x838877, "immersiveengineering:textures/blocks/sheetmetal_nickel", 2, 0.25f, 0f);
		ShrapnelHandler.addShrapnel("silver", 0xa7cac8, "immersiveengineering:textures/blocks/sheetmetal_silver", 2, 0.25f, 0f);
		ShrapnelHandler.addShrapnel("electrum", 0xf6ad59, "immersiveengineering:textures/blocks/sheetmetal_electrum", 2, 0.25f, 0f);
		ShrapnelHandler.addShrapnel("constantan", 0xf97456, "immersiveengineering:textures/blocks/sheetmetal_constantan", 3, 0.25f, 0f);
		ShrapnelHandler.addShrapnel("iron", 0xc7c7c7, "minecraft:textures/blocks/iron_block", 4, 0.25f, 0f);
		ShrapnelHandler.addShrapnel("lead", 0x3a3e44, "immersiveengineering:textures/blocks/sheetmetal_lead", 5, 0.75f, 0f);
		ShrapnelHandler.addShrapnel("steel", 0x4d4d4d, "immersiveengineering:textures/blocks/sheetmetal_steel", 6, 0.35f, 0f);
		ShrapnelHandler.addShrapnel("brass", 0x957743, "immersiveintelligence:textures/blocks/metal/sheetmetal_brass", 6, 0.35f, 0f);
		ShrapnelHandler.addShrapnel("tungsten", 0x3b3e43, "immersiveintelligence:textures/blocks/metal/sheetmetal_tungsten", 8, 0.45f, 0f);
		ShrapnelHandler.addShrapnel("HOPGraphite", 0x282828, "immersiveengineering:textures/blocks/stone_decoration_coke", 8, 0.45f, 0f);
		ShrapnelHandler.addShrapnel("uranium", 0x659269, "immersiveengineering:textures/blocks/sheetmetal_uranium", 12, 0.45f, 8f);

		for(Entry<String, Shrapnel> s : ShrapnelHandler.registry.entrySet())
		{
			BulletComponentShrapnel shrapnel = new BulletComponentShrapnel(s.getKey());
			BulletRegistry.INSTANCE.registerComponent(shrapnel, shrapnel.getName());
		}
	}

	public void init()
	{
		//Worldgen registration
		IIWorldGen iiWorldGen = new IIWorldGen();
		GameRegistry.registerWorldGenerator(iiWorldGen, 0);
		MinecraftForge.EVENT_BUS.register(iiWorldGen);

		ImmersiveIntelligence.logger.info("Adding oregen");
		addConfiguredWorldgen(block_ore.getStateFromMeta(IIBlockTypes_Ore.PLATINUM.getMeta()), "platinum", Ores.ore_platinum, EnumOreType.OVERWORLD);
		addConfiguredWorldgen(block_ore.getStateFromMeta(IIBlockTypes_Ore.ZINC.getMeta()), "zinc", Ores.ore_zinc, EnumOreType.OVERWORLD);
		addConfiguredWorldgen(block_ore.getStateFromMeta(IIBlockTypes_Ore.TUNGSTEN.getMeta()), "tungsten", Ores.ore_tungsten, EnumOreType.OVERWORLD);
		addConfiguredWorldgen(block_ore.getStateFromMeta(IIBlockTypes_Ore.SALT.getMeta()), "salt", Ores.ore_salt, EnumOreType.OVERWORLD);
		addConfiguredWorldgen(block_ore.getStateFromMeta(IIBlockTypes_Ore.FLUORITE.getMeta()), "fluorite", Ores.ore_fluorite, EnumOreType.NETHER);

		//Disallow crates in crates
		IEApi.forbiddenInCrates.add((stack) ->
		{
			if(OreDictionary.itemMatches(new ItemStack(block_metal_device, 1, 0), stack, true))
				return true;
			if(stack.getItem()==item_minecart&&stack.getMetadata() < ItemIIMinecart.META_MINECART_WOODEN_BARREL)
				return true;
			return OreDictionary.itemMatches(new ItemStack(block_metal_device, 1, 1), stack, true);
		});

		hammer_blacklist.add(
				iBlockState -> (iBlockState.getBlock().equals(IEContent.blockMetalDevice1)&&iBlockState.getBlock().getMetaFromState(iBlockState)==BlockTypes_MetalDevice1.CHARGING_STATION.getMeta())
		);

		IEApi.forbiddenInCrates.add((stack) -> stack.getItem() instanceof ItemBlockIEBase&&((ItemBlockIEBase)stack.getItem()).getBlock() instanceof BlockIISmallCrate);


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
		registerTile(TileEntityDataMerger.class);
		registerTile(TileEntityDataRouter.class);
		registerTile(TileEntityDataDebugger.class);
		registerTile(TileEntityChemicalDispenser.class);

		registerTile(TileEntityDataConnector.class);
		registerTile(TileEntityDataRelay.class);

		registerTile(TileEntitySandbags.class);

		registerTile(TileEntityMechanicalWheel.class);
		registerTile(TileEntityGearbox.class);
		registerTile(TileEntityTransmissionBoxCreative.class);
		registerTile(TileEntityTransmissionBox.class);

		registerTile(TileEntitySkyCratePost.class);
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
		registerTile(TileEntityBallisticComputer.class);
		registerTile(TileEntityPacker.class);
		registerTile(TileEntityRedstoneInterface.class);

		MultiblockHandler.registerMultiblock(MultiblockSkyCratePost.instance);
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
		MultiblockHandler.registerMultiblock(MultiblockBallisticComputer.instance);
		MultiblockHandler.registerMultiblock(MultiblockPacker.instance);
		MultiblockHandler.registerMultiblock(MultiblockRedstoneInterface.instance);

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
		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "minecart_wooden_barrel"),
				EntityMinecartBarrelWooden.class, "minecart_wooden_barrel", i++, ImmersiveIntelligence.INSTANCE, 64, 1,
				true);
		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "minecart_metal_barrel"),
				EntityMinecartBarrelSteel.class, "minecart_metal_barrel", i++, ImmersiveIntelligence.INSTANCE, 64, 1,
				true);

		//Entities

		//Finally Skycrates are a thing! ^^
		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "skycrate"),
				EntitySkyCrate.class, "skycrate", i++, ImmersiveIntelligence.INSTANCE, 64, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "bullet"),
				EntityBullet.class, "bullet", i++, ImmersiveIntelligence.INSTANCE, 32, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "shrapnel"),
				EntityShrapnel.class, "shrapnel", i++, ImmersiveIntelligence.INSTANCE, 16, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "machinegun"),
				EntityMachinegun.class, "machinegun", i++, ImmersiveIntelligence.INSTANCE, 64, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "camera"),
				EntityCamera.class, "camera", i++, ImmersiveIntelligence.INSTANCE, 1, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "skycrate_internal"),
				EntitySkycrateInternal.class, "skycrate_internal", i++, ImmersiveIntelligence.INSTANCE, 64, 1, true);
	}

	public void postInit()
	{
		MinecartBlockHelper.blocks.put((stack -> blusunrize.immersiveengineering.common.util.Utils.getBlockFromItem(stack.getItem())==IEContent.blockWoodenDevice0&&stack.getMetadata()==BlockTypes_WoodenDevice0.CRATE.getMeta()),
				EntityMinecartCrateWooden::new);
		MinecartBlockHelper.blocks.put((stack -> blusunrize.immersiveengineering.common.util.Utils.getBlockFromItem(stack.getItem())==IEContent.blockWoodenDevice0&&stack.getMetadata()==BlockTypes_WoodenDevice0.REINFORCED_CRATE.getMeta()),
				EntityMinecartCrateReinforced::new);
		MinecartBlockHelper.blocks.put((stack -> blusunrize.immersiveengineering.common.util.Utils.getBlockFromItem(stack.getItem())==block_metal_device&&stack.getMetadata()==IIBlockTypes_MetalDevice.METAL_CRATE.getMeta()),
				EntityMinecartCrateSteel::new);
		MinecartBlockHelper.blocks.put((stack -> blusunrize.immersiveengineering.common.util.Utils.getBlockFromItem(stack.getItem())==IEContent.blockWoodenDevice0&&stack.getMetadata()==BlockTypes_WoodenDevice0.BARREL.getMeta()),
				EntityMinecartBarrelWooden::new);
		MinecartBlockHelper.blocks.put((stack -> blusunrize.immersiveengineering.common.util.Utils.getBlockFromItem(stack.getItem())==IEContent.blockMetalDevice0&&stack.getMetadata()==BlockTypes_MetalDevice0.BARREL.getMeta()),
				EntityMinecartBarrelSteel::new);

		RotaryUtils.ie_rotational_blocks_torque.put(tileEntity -> tileEntity instanceof TileEntityWindmill,
				aFloat -> aFloat*MechanicalDevices.dynamo_windmill_torque
		);

		RotaryUtils.ie_rotational_blocks_torque.put(tileEntity -> tileEntity instanceof TileEntityWatermill,
				aFloat -> aFloat*MechanicalDevices.dynamo_watermill_torque
		);
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

			else if(ID==IIGuiList.GUI_PACKER&&te instanceof TileEntityPacker)
				gui = new ContainerPacker(player.inventory, (TileEntityPacker)te);
			/*else if(ID==IIGuiList.GUI_UNPACKER&&te instanceof TileEntityUnpacker)
				gui = new ContainerUnpacker(player.inventory, (TileEntityUnpacker)te);*/

			else if(ID==IIGuiList.GUI_DATA_MERGER&&te instanceof TileEntityDataMerger)
				gui = new ContainerDataMerger(player.inventory, (TileEntityDataMerger)te);
			else if(ID==IIGuiList.GUI_DATA_REDSTONE_INTERFACE_DATA&&te instanceof TileEntityRedstoneInterface)
				gui = new ContainerRedstoneDataInterface(player.inventory, (TileEntityRedstoneInterface)te);
			else if(ID==IIGuiList.GUI_DATA_REDSTONE_INTERFACE_REDSTONE&&te instanceof TileEntityRedstoneInterface)
				gui = new ContainerRedstoneDataInterface(player.inventory, (TileEntityRedstoneInterface)te);

			else if(ID==IIGuiList.GUI_SKYCRATE_STATION&&te instanceof TileEntitySkyCrateStation)
				gui = new ContainerSkycrateStation(player.inventory, (TileEntitySkyCrateStation)te);
			else if(ID==IIGuiList.GUI_GEARBOX&&te instanceof TileEntityGearbox)
				gui = new ContainerGearbox(player.inventory, (TileEntityGearbox)te);

			((IGuiTile)te).onGuiOpened(player, false);

			return gui;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
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
		IICreativeTab.fluidBucketMap.add(fl);
		return fl;
	}

	//Cancel when using a machinegun
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onItemUse(PlayerInteractEvent.RightClickBlock event)
	{
		if(event.getEntity().isRiding()&&event.getEntity().getRidingEntity() instanceof EntityMachinegun)
		{
			event.setResult(Result.DENY);
			event.setCanceled(true);
		}
	}

	//Cancel when using a machinegun
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onBlockUse(PlayerInteractEvent.RightClickItem event)
	{
		if(event.getEntity().isRiding()&&event.getEntity().getRidingEntity() instanceof EntityMachinegun)
		{
			event.setResult(Result.DENY);
			event.setCanceled(true);
		}
	}

	//Shooting
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onEmptyRightclick(PlayerInteractEvent.RightClickEmpty event)
	{
		if(event.getEntity().isRiding()&&event.getEntity().getRidingEntity() instanceof EntityMachinegun)
		{
			event.setResult(Result.DENY);
		}
	}

	@SubscribeEvent
	public void onBreakBlock(BreakEvent event)
	{
		DimensionBlockPos dpos = null;
		for(Entry<DimensionBlockPos, Float> g : PenetrationRegistry.blockDamage.entrySet())
		{
			if(g.getKey().dimension==event.getWorld().provider.getDimension()&&event.getPos().equals(g.getKey())) ;
			{
				dpos = g.getKey();
				break;
			}
		}
		if(dpos!=null)
		{
			PenetrationRegistry.blockDamage.remove(dpos);
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageBlockDamageSync(-1f, dpos), Utils.targetPointFromPos(dpos, event.getWorld(), 32));
		}
	}


	public void spawnGunfireFX(World world, double x, double y, double z, double mx, double my, double mz, float size)
	{

	}
}
