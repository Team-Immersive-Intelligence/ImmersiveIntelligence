package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.MultiblockFormEvent;
import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MixerRecipe;
import blusunrize.immersiveengineering.api.tool.BulletHandler;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralMix;
import blusunrize.immersiveengineering.api.tool.RailgunHandler;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice0;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityChargingStation;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDevice0;
import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityWatermill;
import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityWindmill;
import blusunrize.immersiveengineering.common.crafting.RecipeRGBColouration;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IGuiItem;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.network.MessageNoSpamChatComponents;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
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
import pl.pabilo8.immersiveintelligence.api.*;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.Shrapnel;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.DamageBlockPos;
import pl.pabilo8.immersiveintelligence.api.bullets.IBullet;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.crafting.ElectrolyzerRecipe;
import pl.pabilo8.immersiveintelligence.api.rotary.CapabilityRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils;
import pl.pabilo8.immersiveintelligence.api.utils.IAdvancedMultiblock;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.api.utils.MinecartBlockHelper;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.common.ammunition_system.*;
import pl.pabilo8.immersiveintelligence.common.ammunition_system.cores.*;
import pl.pabilo8.immersiveintelligence.common.ammunition_system.explosives.BulletComponentHMX;
import pl.pabilo8.immersiveintelligence.common.ammunition_system.explosives.BulletComponentNuke;
import pl.pabilo8.immersiveintelligence.common.ammunition_system.explosives.BulletComponentRDX;
import pl.pabilo8.immersiveintelligence.common.ammunition_system.explosives.BulletComponentTNT;
import pl.pabilo8.immersiveintelligence.common.ammunition_system.factory.BulletComponentFluid;
import pl.pabilo8.immersiveintelligence.common.ammunition_system.factory.BulletComponentShrapnel;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIFluid;
import pl.pabilo8.immersiveintelligence.common.blocks.fortification.TileEntityChainFence;
import pl.pabilo8.immersiveintelligence.common.blocks.fortification.TileEntityTankTrap;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.*;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.*;
import pl.pabilo8.immersiveintelligence.common.blocks.stone.TileEntitySandbags;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_Ore;
import pl.pabilo8.immersiveintelligence.common.blocks.wooden.TileEntityMineSign;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.entity.*;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.*;
import pl.pabilo8.immersiveintelligence.common.entity.minecarts.*;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerUpgrade;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIMinecart;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIRailgunOverride;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageBlockDamageSync;
import pl.pabilo8.immersiveintelligence.common.wire.IIDataWireType;
import pl.pabilo8.immersiveintelligence.common.world.IIWorldGen;
import pl.pabilo8.immersiveintelligence.common.world.IIWorldGen.EnumOreType;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map.Entry;

import static blusunrize.immersiveengineering.api.energy.wires.WireApi.registerFeedthroughForWiretype;

/**
 * Created by Pabilo8 on 2019-05-07.
 * 2 days of headache, pain, lack of sleep and addict-like coffee drinking, finally i got a fix!
 * the problem why the server couldn't load was the modifier "abstract" in this class
 * why? i don't know why it was here in the first place
 * for how long? ask github
 * how did you not notice that? ... that was really unexpected, didn't even consider such a thing being there
 */
@Mod.EventBusSubscriber(modid = ImmersiveIntelligence.MODID)
public class CommonProxy implements IGuiHandler, LoadingCallback
{
	public static final String DESCRIPTION_KEY = "desc.immersiveintelligence.";
	public static final String INFO_KEY = "info.immersiveintelligence.";
	public static final String DATA_KEY = "datasystem.immersiveintelligence.";
	public static final String ROTARY_KEY = "rotary.immersiveintelligence.";
	public static final String BLOCK_KEY = "tile.immersiveintelligence.";

	public static final String SKIN_LOCATION = "immersiveintelligence:textures/skins/";

	public static final String TOOL_ADVANCED_HAMMER = "II_ADVANCED_HAMMER";
	public static final String TOOL_WRENCH = "II_WRENCH";
	public static final String TOOL_ADVANCED_WRENCH = "II_ADVANCED_WRENCH";
	public static final String TOOL_CROWBAR = "II_CROWBAR";
	public static final String TOOL_TACHOMETER = "TOOL_TACHOMETER";

	public CommonProxy()
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
		for(Block block : IIContent.BLOCKS)
			event.getRegistry().register(block.setRegistryName(createRegistryName(block.getUnlocalizedName())));

		registerFeedthroughForWiretype(IIDataWireType.DATA, new ResourceLocation(ImmersiveIntelligence.MODID, "block/data_connector.obj"),
				new ResourceLocation(ImmersiveIntelligence.MODID, "blocks/data_connector_feedtrough"), new float[]{4, 4, 12, 12},
				0.09375, IIContent.blockDataConnector.getStateFromMeta(IIBlockTypes_Connector.DATA_CONNECTOR.getMeta()),
				0, 0, (f) -> f);

	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		ImmersiveIntelligence.logger.info("Registering Items");

		for(Item item : IIContent.ITEMS)
			event.getRegistry().register(item.setRegistryName(createRegistryName(item.getUnlocalizedName())));

		registerOreDict();
	}

	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> event)
	{
		/*POTIONS*/
		IIPotions.init();
		for(Block block : IIContent.BLOCKS)
		{
			if(block instanceof BlockIIFluid)
			{
				BlockIIFluid b = (BlockIIFluid)block;
				if(b.isAcid)
					b.setPotionEffects(new PotionEffect(IIPotions.corrosion, 40, 1));
				b.addToChemthrower();
			}
		}
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

	public static <T extends TileEntity & IUpgradableMachine> void openUpgradeGuiForTile(@Nonnull EntityPlayer player, @Nonnull T tile)
	{
		player.openGui(ImmersiveIntelligence.INSTANCE, IIGuiList.GUI_UPGRADE.ordinal(), tile.getWorld(), tile.getPos().getX(),
				tile.getPos().getY(), tile.getPos().getZ());
	}

	public static <T extends Entity & IGuiTile> void openGuiForEntity(@Nonnull EntityPlayer player, @Nonnull T entity, int gui)
	{
		player.openGui(ImmersiveIntelligence.INSTANCE, gui, entity.world, entity.getPosition().getX(),
				entity.getPosition().getY(), entity.getPosition().getZ());
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
		OreDictionary.registerOre("electronTubeAdvanced", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("advanced_electron_tube")));

		OreDictionary.registerOre("transistor", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("transistor")));
		OreDictionary.registerOre("oc:materialTransistor", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("transistor")));

		//Basic Circuit Board
		OreDictionary.registerOre("circuitBasic", new ItemStack(IEContent.itemMaterial, 1, 27));
		OreDictionary.registerOre("circuitBasicRaw", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("basic_circuit_board_raw")));
		OreDictionary.registerOre("oc:materialCircuitBoardRaw", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("basic_circuit_board_raw")));
		OreDictionary.registerOre("circuitBasicEtched", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("basic_circuit_board_etched")));
		OreDictionary.registerOre("oc:materialCircuitBoardPrinted", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("basic_circuit_board_etched")));
		OreDictionary.registerOre("chipBasic", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("basic_electronic_element")));
		OreDictionary.registerOre("oc:circuitChip1", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("basic_electronic_element")));

		//Advanced Circuit Board
		OreDictionary.registerOre("circuitAdvancedRaw", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("advanced_circuit_board_raw")));
		OreDictionary.registerOre("circuitAdvancedEtched", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("advanced_circuit_board_etched")));
		OreDictionary.registerOre("oc:materialCircuitBoardPrinted", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("advanced_circuit_board_raw")));
		OreDictionary.registerOre("chipAdvanced", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("advanced_electronic_element")));
		OreDictionary.registerOre("oc:circuitChip2", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("advanced_electronic_element")));
		OreDictionary.registerOre("circuitAdvanced", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("advanced_circuit_board")));

		//Processor Circuit Board
		OreDictionary.registerOre("circuitProcessorEtched", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("processor_circuit_board_etched")));
		OreDictionary.registerOre("oc:materialCircuitBoardPrinted", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("processor_circuit_board_etched")));
		OreDictionary.registerOre("circuitProcessorRaw", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("processor_circuit_board_raw")));
		OreDictionary.registerOre("chipProcessor", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("processor_electronic_element")));
		OreDictionary.registerOre("oc:circuitChip3", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("processor_electronic_element")));
		OreDictionary.registerOre("circuitProcessor", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("processor_circuit_board")));

		OreDictionary.registerOre("circuitEliteEtched", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("processor_circuit_board_etched")));
		OreDictionary.registerOre("circuitEliteRaw", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("processor_circuit_board_raw")));
		OreDictionary.registerOre("circuitElite", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("processor_circuit_board")));
		OreDictionary.registerOre("chipElite", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("processor_electronic_element")));

		registerItemOredict(IIContent.itemMaterial, "compact_electric_engine", "engineElectricSmall", "engineElectricCompact");
		registerItemOredict(IIContent.itemMaterial, "compact_electric_engine_advanced", "engineElectricSmallAdvanced", "engineElectricCompactAdvanced");

		registerMetalOredict(IIContent.itemMaterialIngot, "ingot");
		registerMetalOredict(IIContent.itemMaterialPlate, "plate");
		registerMetalOredict(IIContent.itemMaterialDust, "dust");
		registerMetalOredict(IIContent.itemMaterialNugget, "nugget");
		registerMetalOredict(IIContent.itemMaterialWire, "wire");
		registerMetalOredict(IIContent.itemMaterialSpring, "spring");
		registerMetalOredict(IIContent.itemMaterialGem, "gem");
		registerMetalOredict(IIContent.itemMaterialBoule, "boule");

		registerMetalOredictBlock(IIContent.blockOre, "ore");
		registerMetalOredictBlock(IIContent.blockSheetmetal, "sheetmetal");
		registerMetalOredictBlock(IIContent.blockSheetmetalSlabs, "slabSheetmetal");
		registerMetalOredictBlock(IIContent.blockMetalStorage, "block");
		registerMetalOredictBlock(IIContent.blockMetalSlabs, "slab");


		//Punchtapes
		OreDictionary.registerOre("punchtapeEmpty", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("punchtape_empty")));
		OreDictionary.registerOre("punchtape", new ItemStack(IIContent.itemPunchtape, 1, 0));

		OreDictionary.registerOre("pageEmpty", new ItemStack(IIContent.itemPrintedPage, 1, 0));
		OreDictionary.registerOre("pageText", new ItemStack(IIContent.itemPrintedPage, 1, 1));
		OreDictionary.registerOre("pageWritten", new ItemStack(IIContent.itemPrintedPage, 1, 1));

		OreDictionary.registerOre("pageCode", new ItemStack(IIContent.itemPrintedPage, 1, 2));
		OreDictionary.registerOre("pageWritten", new ItemStack(IIContent.itemPrintedPage, 1, 2));
		OreDictionary.registerOre("pageBlueprint", new ItemStack(IIContent.itemPrintedPage, 1, 3));
		OreDictionary.registerOre("pageWritten", new ItemStack(IIContent.itemPrintedPage, 1, 3));

		OreDictionary.registerOre("materialTNT", new ItemStack(Blocks.TNT, 1, 0));
		OreDictionary.registerOre("materialRDX", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("dust_rdx")));
		OreDictionary.registerOre("materialHexogen", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("dust_rdx")));
		OreDictionary.registerOre("materialHMX", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("dust_hmx")));

		OreDictionary.registerOre("dustWhitePhosphorus", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("white_phosphorus")));
		OreDictionary.registerOre("whitePhosphorus", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("white_phosphorus")));

		OreDictionary.registerOre("dustSalt", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("dust_salt")));
		OreDictionary.registerOre("dustWood", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("dust_wood")));
		OreDictionary.registerOre("pulpWood", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("pulp_wood")));
		OreDictionary.registerOre("pulpWoodTreated", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("pulp_wood_treated")));
		OreDictionary.registerOre("dustHexamine", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("dust_hexamine")));
		OreDictionary.registerOre("dustFormaldehyde", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("dust_formaldehyde")));

		OreDictionary.registerOre("leatherArtificial", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("artificial_leather")));
		OreDictionary.registerOre("leather", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("artificial_leather")));

		OreDictionary.registerOre("brushCarbon", new ItemStack(IIContent.itemMaterial, 1, IIContent.itemMaterial.getMetaBySubname("carbon_brush")));

		OreDictionary.registerOre("listAllMeatRaw", Items.PORKCHOP);
		OreDictionary.registerOre("listAllMeatRaw", Items.BEEF);
		OreDictionary.registerOre("listAllMeatRaw", Items.FISH);
		OreDictionary.registerOre("listAllMeatRaw", Items.CHICKEN);
		OreDictionary.registerOre("listAllMeatRaw", Items.RABBIT);
		OreDictionary.registerOre("listAllMeatRaw", Items.MUTTON);

		registerMetalOredict(IIContent.itemMotorGear, "gear");
		registerMetalOredict(IIContent.itemMotorBelt, "belt");
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
		String sulfur = OreDictionary.doesOreNameExist("oreSulfur")?"oreSulfur": "dustSulfur";

		MineralMix mineralFluorite = ExcavatorHandler.addMineral("Fluorite", 25, .65f, new String[]{"oreFluorite", "oreQuartz"}, new float[]{.5f, .25f});
		MineralMix mineralPhosphorite = ExcavatorHandler.addMineral("Phosphorite", 30, .45f, new String[]{"orePhosphorus", sulfur, "oreIron", "oreAluminum"}, new float[]{.65f, .125f, 0.0625f, 0.0125f});
		mineralFluorite.dimensionWhitelist = new int[]{-1};
		mineralPhosphorite.dimensionWhitelist = new int[]{-1};

		ExcavatorHandler.addMineral("Wolframite", 15, .15f, new String[]{"oreTungsten", "oreIron"}, new float[]{.25f, .75f});
		ExcavatorHandler.addMineral("Ferberite", 10, .2f, new String[]{"oreTungsten", "oreIron", "oreTin"}, new float[]{.2f, .4f, .3f});

		LighterFuelHandler.addFuel(FluidRegistry.getFluid("creosote"), 100);
		LighterFuelHandler.addFuel(FluidRegistry.getFluid("ethanol"), 20);

		MachinegunCoolantHandler.addCoolant(FluidRegistry.WATER, 1);
		// LighterFuelHandler.addFuel(FluidRegistry.getFluid("creosote"),100);

		CrusherRecipe.addRecipe(Utils.getStackWithMetaName(IIContent.itemMaterialDust, "silicon"), new IngredientStack("plateSilicon"), 12000);

		final ItemStack tracer_powder = new ItemStack(IIContent.itemTracerPowder, 1, 0);
		event.getRegistry().register(new RecipeRGBColouration((s) -> (OreDictionary.itemMatches(tracer_powder, s, true)), (s) -> (ItemNBTHelper.hasKey(s, "colour")?ItemNBTHelper.getInt(s, "colour"): 0xffffff), (s, i) -> ItemNBTHelper.setInt(s, "colour", i)).setRegistryName(ImmersiveEngineering.MODID, "tracer_powder_colour"));
		final ItemStack flare_powder = new ItemStack(IIContent.itemTracerPowder, 1, 1);
		event.getRegistry().register(new RecipeRGBColouration((s) -> (OreDictionary.itemMatches(flare_powder, s, true)), (s) -> (ItemNBTHelper.hasKey(s, "colour")?ItemNBTHelper.getInt(s, "colour"): 0xffffff), (s, i) -> ItemNBTHelper.setInt(s, "colour", i)).setRegistryName(ImmersiveEngineering.MODID, "flare_powder_colour"));

		IIRecipes.addMinecartRecipes(event.getRegistry());
		IIRecipes.addSmallCrateRecipes(event.getRegistry());

		for(int i = 0; i < IIContent.itemPressMold.getSubNames().length; i++)
			BlueprintCraftingRecipe.addRecipe("molds", new ItemStack(IIContent.itemPressMold, 1, i), "plateSteel", "plateSteel", "plateSteel", "plateSteel", "plateSteel", new ItemStack(IEContent.itemTool, 1, 1));

		//((IForgeRegistryModifiable)CraftingManager.REGISTRY).remove(new ResourceLocation(""));

		IIRecipes.addBulletPressRecipes();

		IIRecipes.addSiliconProcessingRecipes();
		IIRecipes.addCircuitRecipes();

		IIRecipes.addFunctionalCircuits();
		IIRecipes.addSpringRecipes();
		IIRecipes.addMiscIERecipes();

		IIRecipes.addRotaryPowerRecipes();
		IIRecipes.addUpgradeRecipes();

		IIRecipes.addRDXProductionRecipes();
		IIRecipes.addHMXProductionRecipes();

		IIRecipes.addConcreteRecipes();
		IIRecipes.addChemicalBathCleaningRecipes();

		//Immersive Engineering can into space???
		ElectrolyzerRecipe.addRecipe(FluidRegistry.getFluidStack("water", 750), FluidRegistry.getFluidStack("oxygen", 250), FluidRegistry.getFluidStack("hydrogen", 500), 160, 80);
		ElectrolyzerRecipe.addRecipe(FluidRegistry.getFluidStack("brine", 750), FluidRegistry.getFluidStack("chlorine", 375), FluidRegistry.getFluidStack("hydrogen", 375), 160, 80);


		IIRecipes.addInkRecipes();

		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidEtchingAcid, 1000), new FluidStack(IIContent.gasChlorine, 500), new Object[]{"dustIron"}, 4800);
		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidSulfuricAcid, 500), new FluidStack(FluidRegistry.WATER, 1000), new Object[]{"dustSulfur"}, 4800);
		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidHydrofluoricAcid, 500), new FluidStack(IIContent.fluidSulfuricAcid, 1000), new Object[]{"dustFluorite"}, 5600);
		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidNitricAcid, 250), new FluidStack(IIContent.fluidSulfuricAcid, 1000), new Object[]{"dustSaltpeter"}, 5600);
		MixerRecipe.addRecipe(new FluidStack(IIContent.fluidBrine, 750), new FluidStack(FluidRegistry.WATER, 750), new Object[]{"dustSalt"}, 3200);
	}

	public static Fluid makeFluid(String name, int density, int viscosity)
	{
		return makeFluid(name, density, viscosity, "");
	}

	public static Fluid makeFluid(String name, int density, int viscosity, String prefix)
	{
		Fluid fl = new Fluid(
				name,
				new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/fluid/"+prefix+name+"_still"),
				new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/fluid/"+prefix+name+"_flow")
		).setDensity(density).setViscosity(viscosity);
		FluidRegistry.addBucketForFluid(fl);
		if(!FluidRegistry.registerFluid(fl))
			fl = FluidRegistry.getFluid(fl.getName());

		IICreativeTab.fluidBucketMap.add(fl);
		return fl;
	}

	public static void refreshFluidReferences()
	{
		IIContent.fluidInkBlack = FluidRegistry.getFluid("ink");
		IIContent.fluidInkCyan = FluidRegistry.getFluid("ink_cyan");
		IIContent.fluidInkMagenta = FluidRegistry.getFluid("ink_magenta");
		IIContent.fluidInkYellow = FluidRegistry.getFluid("ink_yellow");

		IIContent.fluidBrine = FluidRegistry.getFluid("brine");
		IIContent.fluidEtchingAcid = FluidRegistry.getFluid("etching_acid");
		IIContent.fluidHydrofluoricAcid = FluidRegistry.getFluid("hydrofluoric_acid");
		IIContent.fluidSulfuricAcid = FluidRegistry.getFluid("sulfuric_acid");

		IIContent.fluidAmmonia = FluidRegistry.getFluid("ammonia");
		IIContent.fluidMethanol = FluidRegistry.getFluid("methanol");

		IIContent.gasChlorine = FluidRegistry.getFluid("chlorine");
		IIContent.gasHydrogen = FluidRegistry.getFluid("hydrogen");
		IIContent.gasOxygen = FluidRegistry.getFluid("oxygen");
	}


	public void preInit()
	{
		IIDataWireType.init();
		IIPacketHandler.preInit();
		CapabilityRotaryEnergy.register();
		IICompatModule.doModulesPreInit();
		IEContent.itemRailgun = new ItemIIRailgunOverride();

		//ALWAYS REGISTER BULLETS IN PRE-INIT! (so they get their texture registered before TextureStitchEvent.Pre)
		//Bullets

		BulletRegistry.INSTANCE.registerCasing(IIContent.itemAmmoArtillery);
		BulletRegistry.INSTANCE.registerCasing(IIContent.itemAmmoAutocannon);
		BulletRegistry.INSTANCE.registerCasing(IIContent.itemGrenade);
		BulletRegistry.INSTANCE.registerCasing(IIContent.itemRailgunGrenade);

		BulletRegistry.INSTANCE.registerCasing(IIContent.itemAmmoMachinegun);
		BulletRegistry.INSTANCE.registerCasing(IIContent.itemAmmoSubmachinegun);
		BulletRegistry.INSTANCE.registerCasing(IIContent.itemAmmoStormRifle);
		BulletRegistry.INSTANCE.registerCasing(IIContent.itemAmmoRevolver);

		BulletRegistry.INSTANCE.registerCasing((IBullet)IIContent.blockTripmine.itemBlock);
		//BulletRegistry.INSTANCE.registerCasing((IBullet)IIContent.blockTellermine.itemBlock);
		BulletRegistry.INSTANCE.registerCasing(IIContent.itemNavalMine);

		BulletRegistry.INSTANCE.registerComponent(new BulletComponentTNT());
		BulletRegistry.INSTANCE.registerComponent(new BulletComponentRDX());
		BulletRegistry.INSTANCE.registerComponent(new BulletComponentHMX());
		BulletRegistry.INSTANCE.registerComponent(new BulletComponentNuke());
		BulletRegistry.INSTANCE.registerComponent(new BulletComponentWhitePhosphorus());
		BulletRegistry.INSTANCE.registerComponent(new BulletComponentFirework());
		BulletRegistry.INSTANCE.registerComponent(new BulletComponentTracerPowder());
		BulletRegistry.INSTANCE.registerComponent(new BulletComponentFlarePowder());

		BulletRegistry.INSTANCE.registerBulletCore(new BulletCoreSteel());
		BulletRegistry.INSTANCE.registerBulletCore(new BulletCoreTungsten());
		BulletRegistry.INSTANCE.registerBulletCore(new BulletCoreBrass());
		BulletRegistry.INSTANCE.registerBulletCore(new BulletCoreLead());
		BulletRegistry.INSTANCE.registerBulletCore(new BulletCoreUranium());

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

		//easter eggs
		BulletRegistry.INSTANCE.registerComponent(new BulletComponentFish());
		BulletRegistry.INSTANCE.registerBulletCore(new BulletCorePabilium());

		for(Entry<String, Shrapnel> s : ShrapnelHandler.registry.entrySet())
		{
			BulletComponentShrapnel shrapnel = new BulletComponentShrapnel(s.getKey());
			BulletRegistry.INSTANCE.registerComponent(shrapnel);
		}

		BulletHandler.registerBullet("ii_bullet", IIContent.itemAmmoRevolver);
	}

	public void init()
	{
		IICompatModule.doModulesInit();
		reInitGui();

		for(Fluid f : FluidRegistry.getRegisteredFluids().values())
		{
			BulletComponentFluid comp = new BulletComponentFluid(f);
			BulletRegistry.INSTANCE.registerComponent(comp);
		}

		//Blocks config
		IIContent.blockOre.setMiningLevels();

		//Worldgen registration
		IIWorldGen iiWorldGen = new IIWorldGen();
		GameRegistry.registerWorldGenerator(iiWorldGen, 0);
		MinecraftForge.EVENT_BUS.register(iiWorldGen);

		ImmersiveIntelligence.logger.info("Adding oregen");
		addConfiguredWorldgen(IIContent.blockOre.getStateFromMeta(IIBlockTypes_Ore.PLATINUM.getMeta()), "platinum", Ores.ore_platinum, EnumOreType.OVERWORLD);
		addConfiguredWorldgen(IIContent.blockOre.getStateFromMeta(IIBlockTypes_Ore.ZINC.getMeta()), "zinc", Ores.ore_zinc, EnumOreType.OVERWORLD);
		addConfiguredWorldgen(IIContent.blockOre.getStateFromMeta(IIBlockTypes_Ore.TUNGSTEN.getMeta()), "tungsten", Ores.ore_tungsten, EnumOreType.OVERWORLD);
		addConfiguredWorldgen(IIContent.blockOre.getStateFromMeta(IIBlockTypes_Ore.SALT.getMeta()), "salt", Ores.ore_salt, EnumOreType.OVERWORLD);
		addConfiguredWorldgen(IIContent.blockOre.getStateFromMeta(IIBlockTypes_Ore.FLUORITE.getMeta()), "fluorite", Ores.ore_fluorite, EnumOreType.NETHER);
		addConfiguredWorldgen(IIContent.blockOre.getStateFromMeta(IIBlockTypes_Ore.PHOSPHORUS.getMeta()), "phosphorus", Ores.ore_phosphorus, EnumOreType.NETHER);

		RailgunHandler.registerProjectileProperties(new IngredientStack("stickTungsten"), 32, 1.3).setColourMap(new int[][]{{0xCBD1D6, 0xCBD1D6, 0xCBD1D6, 0xCBD1D6, 0x9EA2A7, 0x9EA2A7}});

		//Disallow crates in crates
		IEApi.forbiddenInCrates.add((stack) ->
		{
			if(OreDictionary.itemMatches(new ItemStack(IIContent.blockMetalDevice, 1, 0), stack, true))
				return true;
			if(stack.getItem()==IIContent.itemMinecart&&stack.getMetadata() < ItemIIMinecart.META_MINECART_WOODEN_BARREL)
				return true;
			return OreDictionary.itemMatches(new ItemStack(IIContent.blockMetalDevice, 1, 1), stack, true);
		});

		IIContent.tileEntitiesWeDontLike.add(tileEntity -> tileEntity instanceof TileEntityChargingStation);

		IEApi.forbiddenInCrates.add((stack) -> stack.getItem() instanceof ItemBlockIEBase&&((ItemBlockIEBase)stack.getItem()).getBlock() instanceof BlockIISmallCrate);


		ImmersiveIntelligence.logger.info("Adding TileEntities");
		registerTile(TileEntityMetalCrate.class);
		registerTile(TileEntityAmmunitionCrate.class);
		registerTile(TileEntitySmallCrate.class);
		registerTile(TileEntityAlarmSiren.class);
		registerTile(TileEntityProgrammableSpeaker.class);
		registerTile(TileEntityMedicalCrate.class);
		registerTile(TileEntityRepairCrate.class);

		registerTile(TileEntityInserter.class);
		registerTile(TileEntityAdvancedInserter.class);
		registerTile(TileEntityFluidInserter.class);

		registerTile(TileEntityTimedBuffer.class);
		registerTile(TileEntityRedstoneBuffer.class);
		registerTile(TileEntitySmallDataBuffer.class);
		registerTile(TileEntityDataMerger.class);
		registerTile(TileEntityDataRouter.class);
		registerTile(TileEntityDataDebugger.class);
		registerTile(TileEntityPunchtapeReader.class);
		registerTile(TileEntityChemicalDispenser.class);

		registerTile(TileEntityDataConnector.class);
		registerTile(TileEntityDataRelay.class);
		registerTile(TileEntityDataCallbackConnector.class);

		registerTile(TileEntitySandbags.class);
		registerTile(TileEntityMineSign.class);

		registerTile(TileEntityMechanicalWheel.class);
		registerTile(TileEntityGearbox.class);
		registerTile(TileEntityTransmissionBoxCreative.class);
		registerTile(TileEntityTransmissionBox.class);
		registerTile(TileEntityMechanicalPump.class);

		registerTile(TileEntityTankTrap.class);
		registerTile(TileEntityChainFence.class);
		registerTile(TileEntityTripMine.class);
		registerTile(TileEntityTellermine.class);

		registerTile(TileEntitySkyCratePost.class);
		registerTile(TileEntitySkyCrateStation.class);
		registerTile(TileEntitySkyCartStation.class);

		registerTile(TileEntitySawmill.class);

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
		registerTile(TileEntityEmplacement.class);
		registerTile(TileEntityFlagpole.class);

		//Wooden
		MultiblockHandler.registerMultiblock(MultiblockSkyCratePost.instance);
		MultiblockHandler.registerMultiblock(MultiblockSkyCrateStation.instance);
		MultiblockHandler.registerMultiblock(MultiblockSkyCartStation.instance);
		MultiblockHandler.registerMultiblock(MultiblockSawmill.instance);

		//Metal0
		MultiblockHandler.registerMultiblock(MultiblockRadioStation.instance);
		MultiblockHandler.registerMultiblock(MultiblockDataInputMachine.instance);
		MultiblockHandler.registerMultiblock(MultiblockArithmeticLogicMachine.instance);
		MultiblockHandler.registerMultiblock(MultiblockPrintingPress.instance);
		MultiblockHandler.registerMultiblock(MultiblockChemicalBath.instance);
		MultiblockHandler.registerMultiblock(MultiblockElectrolyzer.instance);
		MultiblockHandler.registerMultiblock(MultiblockConveyorScanner.instance);
		MultiblockHandler.registerMultiblock(MultiblockPrecissionAssembler.instance);
		MultiblockHandler.registerMultiblock(MultiblockArtilleryHowitzer.instance);
		//MultiblockHandler.registerMultiblock(MultiblockAmmunitionFactory.instance);
		MultiblockHandler.registerMultiblock(MultiblockBallisticComputer.instance);
		MultiblockHandler.registerMultiblock(MultiblockPacker.instance);

		//Metal1
		MultiblockHandler.registerMultiblock(MultiblockRedstoneInterface.instance);
		MultiblockHandler.registerMultiblock(MultiblockEmplacement.instance);
		MultiblockHandler.registerMultiblock(MultiblockFlagpole.instance);

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

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "naval_mine"),
				EntityNavalMine.class, "naval_mine", i++, ImmersiveIntelligence.INSTANCE, 64, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "naval_mine_anchor"),
				EntityNavalMineAnchor.class, "naval_mine_anchor", i++, ImmersiveIntelligence.INSTANCE, 64, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "shrapnel"),
				EntityShrapnel.class, "shrapnel", i++, ImmersiveIntelligence.INSTANCE, 16, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "white_phosphorus"),
				EntityWhitePhosphorus.class, "white_phosphorus", i++, ImmersiveIntelligence.INSTANCE, 16, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "machinegun"),
				EntityMachinegun.class, "machinegun", i++, ImmersiveIntelligence.INSTANCE, 64, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "camera"),
				EntityCamera.class, "camera", i++, ImmersiveIntelligence.INSTANCE, 1, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "skycrate_internal"),
				EntitySkycrateInternal.class, "skycrate_internal", i++, ImmersiveIntelligence.INSTANCE, 64, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "motorbike"),
				EntityMotorbike.class, "motorbike", i++, ImmersiveIntelligence.INSTANCE, 64, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "field_howitzer"),
				EntityFieldHowitzer.class, "field_howitzer", i++, ImmersiveIntelligence.INSTANCE, 64, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "seat"),
				EntityVehicleSeat.class, "seat", i++, ImmersiveIntelligence.INSTANCE, 64, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "tripod_periscope"),
				EntityTripodPeriscope.class, "tripod_periscope", i++, ImmersiveIntelligence.INSTANCE, 64, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "atomic_boom"),
				EntityAtomicBoom.class, "atomic_boom", i++, ImmersiveIntelligence.INSTANCE, 64, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "gas_cloud"),
				EntityGasCloud.class, "gas_cloud", i++, ImmersiveIntelligence.INSTANCE, 64, 1, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "hans"),
				EntityHans.class, "hans", i++, ImmersiveIntelligence.INSTANCE, 64, 4, true);

		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "flare"),
				EntityFlare.class, "flare", i++, ImmersiveIntelligence.INSTANCE, 64, 4, true);
	}

	public void postInit()
	{
		IICompatModule.doModulesPostInit();

		MinecartBlockHelper.blocks.put((stack -> blusunrize.immersiveengineering.common.util.Utils.getBlockFromItem(stack.getItem())==IEContent.blockWoodenDevice0&&stack.getMetadata()==BlockTypes_WoodenDevice0.CRATE.getMeta()),
				EntityMinecartCrateWooden::new);
		MinecartBlockHelper.blocks.put((stack -> blusunrize.immersiveengineering.common.util.Utils.getBlockFromItem(stack.getItem())==IEContent.blockWoodenDevice0&&stack.getMetadata()==BlockTypes_WoodenDevice0.REINFORCED_CRATE.getMeta()),
				EntityMinecartCrateReinforced::new);
		MinecartBlockHelper.blocks.put((stack -> blusunrize.immersiveengineering.common.util.Utils.getBlockFromItem(stack.getItem())==IIContent.blockMetalDevice&&stack.getMetadata()==IIBlockTypes_MetalDevice.METAL_CRATE.getMeta()),
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

		IIRecipes.addWoodTableSawRecipes();

		CorrosionHandler.addItemToBlacklist(new ItemStack(Items.DIAMOND_HELMET));
		CorrosionHandler.addItemToBlacklist(new ItemStack(Items.DIAMOND_CHESTPLATE));
		CorrosionHandler.addItemToBlacklist(new ItemStack(Items.DIAMOND_LEGGINGS));
		CorrosionHandler.addItemToBlacklist(new ItemStack(Items.DIAMOND_BOOTS));
	}

	public void reInitGui()
	{

	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		ItemStack stack = player.getHeldItem(player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IGuiItem?EnumHand.MAIN_HAND: EnumHand.OFF_HAND);

		if(ID==IIGuiList.GUI_UPGRADE.ordinal())
		{
			if(te instanceof IUpgradableMachine)
			{
				TileEntity upgradeMaster = ((IUpgradableMachine)te).getUpgradeMaster();
				if(upgradeMaster!=null)
					return new ContainerUpgrade(player.inventory, (TileEntity & IUpgradableMachine)upgradeMaster);
			}
		}

		Container gui;
		if(IIGuiList.values().length > ID)
		{
			IIGuiList guiBuilder = IIGuiList.values()[ID];
			// TODO: 03.10.2020 items with server side container inventory
			if(guiBuilder.item)
				return null;
			else if(te instanceof IGuiTile&&guiBuilder.teClass.isInstance(te))
			{
				gui = guiBuilder.container.apply(player, te);
				if(gui!=null)
				{
					((IGuiTile)te).onGuiOpened(player, true);
					return gui;
				}
			}
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
		DamageBlockPos dpos = null;
		for(DamageBlockPos g : PenetrationRegistry.blockDamage)
		{
			if(g.dimension==event.getWorld().provider.getDimension()&&event.getPos().equals(g)) ;
			{
				dpos = g;
				break;
			}
		}
		if(dpos!=null)
		{
			PenetrationRegistry.blockDamage.remove(dpos);
			dpos.damage = 0;
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageBlockDamageSync(dpos), Utils.targetPointFromPos(dpos, event.getWorld(), 32));
		}
	}

	@SubscribeEvent
	public void onMultiblockForm(MultiblockFormEvent.Post event)
	{
		if(event.isCancelable()&&!event.isCanceled()&&event.getMultiblock().getClass().isAnnotationPresent(IAdvancedMultiblock.class))
		{
			//Required by Advanced Structures!
			if(!pl.pabilo8.immersiveintelligence.api.Utils.isAdvancedHammer(event.getHammer()))
			{
				if(!event.getEntityPlayer().getEntityWorld().isRemote)
					ImmersiveEngineering.packetHandler.sendTo(new MessageNoSpamChatComponents(new TextComponentTranslation("info.immersiveintelligence.requires_advanced_hammer")), (EntityPlayerMP)event.getEntityPlayer());
				event.setCanceled(true);
			}
		}
	}

	public void reloadModels()
	{

	}

	public static MachineUpgrade createMachineUpgrade(String name)
	{
		return new MachineUpgrade(name, new ResourceLocation(ImmersiveIntelligence.MODID, "textures/gui/upgrade/"+name+".png"));
	}

	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world)
	{
		for(Ticket ticket : tickets)
		{
			if(ticket.getType()==Type.NORMAL)
			{
				for(ChunkPos chunkPos : ticket.getChunkList())
				{
					ForgeChunkManager.forceChunk(ticket, chunkPos);
				}
				final MinecraftServer minecraftServer = world.getMinecraftServer();
				if(minecraftServer!=null)
					minecraftServer.addScheduledTask(() -> ForgeChunkManager.releaseTicket(ticket));
			}
		}
	}
}
