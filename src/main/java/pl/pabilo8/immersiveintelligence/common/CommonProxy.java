package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.MultiblockHandler.MultiblockFormEvent;
import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.BulletHandler;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorTile;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralMix;
import blusunrize.immersiveengineering.api.tool.RailgunHandler;
import blusunrize.immersiveengineering.common.Config.IEConfig.Tools;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityChargingStation;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityWatermill;
import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityWindmill;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IGuiItem;
import blusunrize.immersiveengineering.common.items.ItemToolUpgrade.ToolUpgrades;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.MechanicalDevices;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.Railgun;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.*;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.Shrapnel;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.DamageBlockPos;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.crafting.DustUtils;
import pl.pabilo8.immersiveintelligence.api.rotary.CapabilityRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils;
import pl.pabilo8.immersiveintelligence.api.utils.IAdvancedMultiblock;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.api.utils.MinecartBlockHelper;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.common.ammo.*;
import pl.pabilo8.immersiveintelligence.common.ammo.cores.*;
import pl.pabilo8.immersiveintelligence.common.ammo.explosives.AmmoComponentHMX;
import pl.pabilo8.immersiveintelligence.common.ammo.explosives.AmmoComponentNuke;
import pl.pabilo8.immersiveintelligence.common.ammo.explosives.AmmoComponentRDX;
import pl.pabilo8.immersiveintelligence.common.ammo.explosives.AmmoComponentTNT;
import pl.pabilo8.immersiveintelligence.common.ammo.factory.AmmoComponentFluid;
import pl.pabilo8.immersiveintelligence.common.ammo.factory.AmmoComponentShrapnel;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.conveyors.*;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIOre.Ores;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIISmallCrate;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.crafting.IIRecipes;
import pl.pabilo8.immersiveintelligence.common.crafting.RecipePowerpackAdvanced;
import pl.pabilo8.immersiveintelligence.common.crafting.RecipeSkinCraftingHandler;
import pl.pabilo8.immersiveintelligence.common.entity.*;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.*;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansUtils;
import pl.pabilo8.immersiveintelligence.common.entity.minecart.barrel.EntityMinecartBarrelSteel;
import pl.pabilo8.immersiveintelligence.common.entity.minecart.barrel.EntityMinecartBarrelWooden;
import pl.pabilo8.immersiveintelligence.common.entity.minecart.capacitor.EntityMinecartCapacitorCreative;
import pl.pabilo8.immersiveintelligence.common.entity.minecart.capacitor.EntityMinecartCapacitorHV;
import pl.pabilo8.immersiveintelligence.common.entity.minecart.capacitor.EntityMinecartCapacitorLV;
import pl.pabilo8.immersiveintelligence.common.entity.minecart.capacitor.EntityMinecartCapacitorMV;
import pl.pabilo8.immersiveintelligence.common.entity.minecart.crate.EntityMinecartCrateReinforced;
import pl.pabilo8.immersiveintelligence.common.entity.minecart.crate.EntityMinecartCrateSteel;
import pl.pabilo8.immersiveintelligence.common.entity.minecart.crate.EntityMinecartCrateWooden;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityDrone;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityFieldHowitzer;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityMotorbike;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleSeat;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerUpgrade;
import pl.pabilo8.immersiveintelligence.common.item.ItemIIMinecart.Minecarts;
import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialDust.MaterialsDust;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIRailgunOverride;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBlockDamageSync;
import pl.pabilo8.immersiveintelligence.common.util.IBatchOredictRegister;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIFluid;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIUpgradeableArmor;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.wire.IIDataWireType;
import pl.pabilo8.immersiveintelligence.common.world.IIWorldGen;
import pl.pabilo8.immersiveintelligence.common.world.IIWorldGen.EnumOreType;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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
		IILogger.info("Registering Blocks");

		for(Block block : IIContent.BLOCKS)
			event.getRegistry().register(block.setRegistryName(createRegistryName(block.getUnlocalizedName())));

		registerFeedthroughForWiretype(IIDataWireType.DATA, new ResourceLocation(ImmersiveIntelligence.MODID, "block/connector/data_connector.obj.ie"),
				new ResourceLocation(ImmersiveIntelligence.MODID, "blocks/data_connector_feedtrough"), new float[]{4, 4, 12, 12},
				0.75, IIContent.blockDataConnector.getStateFromMeta(IIBlockTypes_Connector.DATA_CONNECTOR.getMeta()),
				0, 0, (f) -> f);

	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		IILogger.info("Registering Items");

		for(Item item : IIContent.ITEMS)
			event.getRegistry().register(item.setRegistryName(createRegistryName(item.getUnlocalizedName())));

		registerOreDict();
	}

	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> event)
	{
		IILogger.info("Registering Potions");

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

	@SubscribeEvent
	public static void registerBiomes(RegistryEvent.Register<Biome> event)
	{
		IILogger.info("Registering Biomes");
		event.getRegistry().register(IIContent.biomeWasteland);
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

	public static void registerEntity(int id, Class<? extends Entity> entity, String name, int trackingRange, int updateFrequency, boolean sendVelocityUpdates)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, name),
				entity, name, id, ImmersiveIntelligence.INSTANCE, trackingRange, updateFrequency, sendVelocityUpdates);
	}

	public static void registerOreDict()
	{
		//IE Circuit Board
		OreDictionary.registerOre("circuitBasic", new ItemStack(IEContent.itemMaterial, 1, 27));

		//Catch them all!
		for(Item item : IIContent.ITEMS)
			if(item instanceof ItemIISubItemsBase<?>)
			{
				String[] ores = getAnnotatedOreDict(item);
				for(IIItemEnum subItem : ((ItemIISubItemsBase<?>)item).getSubItems())
				{
					//get SubItem id
					int meta = subItem.getMeta();

					//only register visible items
					if(((ItemIISubItemsBase<?>)item).isMetaHidden(meta))
						continue;

					//virgin batch registered OreDict
					if(ores!=null)
						for(String ore : ores)
							OreDictionary.registerOre(IIUtils.toCamelCase(ore+"_"+subItem.getName(), true), new ItemStack(item, 1, meta));

					//chad subtype dependent OreDict
					for(String ore : subItem.getOreDict())
						OreDictionary.registerOre(ore, new ItemStack(item, 1, meta));
				}
			}
			else if(item instanceof ItemIIBase)
			{
				String[] ores = getAnnotatedOreDict(item);
				if(ores!=null)
					for(String ore : ores)
						OreDictionary.registerOre(IIUtils.toCamelCase(ore, true), new ItemStack(item));
			}

		for(Block block : IIContent.BLOCKS)
		{
			if(block instanceof BlockIIBase<?>)
			{
				String[] ores = getAnnotatedOreDict(block);

				for(IIBlockEnum enumValue : ((BlockIIBase<?>)block).enumValues)
				{
					int meta = enumValue.getMeta();

					//batch registered OreDict
					if(ores!=null)
						for(String ore : ores)
							OreDictionary.registerOre(IIUtils.toCamelCase(ore+"_"+enumValue.getName(), true), new ItemStack(block, 1, meta));

					//subtype dependent OreDict
					IIBlockProperties properties = enumValue.getProperties();
					if(properties!=null)
						for(String ore : properties.oreDict())
							OreDictionary.registerOre(ore, new ItemStack(block, 1, meta));
				}
			}
		}

		//for fields only
		for(Field field : IIContent.class.getFields())
		{
			if(field.isAnnotationPresent(IBatchOredictRegister.class))
			{
				IBatchOredictRegister annotation = field.getAnnotation(IBatchOredictRegister.class);
				String[] ores = annotation.oreDict();
				try
				{
					Object o = field.get(null);

					if(o instanceof BlockIIBase)
					{
						//separate name for each meta
						BlockIIBase<?> block = (BlockIIBase<?>)field.get(null);
						for(IIBlockEnum enumValue : block.enumValues)
							for(String ore : ores)
								OreDictionary.registerOre(IIUtils.toCamelCase(ore+"_"+enumValue.getName(), true),
										new ItemStack(block, 1, enumValue.getMeta()));
					}
					else if(o instanceof ItemIIBase)
					{
						//meta insensitive
						ItemIIBase item = (ItemIIBase)field.get(null);
						for(String ore : ores)
							OreDictionary.registerOre(IIUtils.toCamelCase(ore, true), new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
					}

				} catch(IllegalAccessException ignored) {}
			}
		}

		OreDictionary.registerOre("listAllMeatRaw", Items.PORKCHOP);
		OreDictionary.registerOre("listAllMeatRaw", Items.BEEF);
		OreDictionary.registerOre("listAllMeatRaw", Items.FISH);
		OreDictionary.registerOre("listAllMeatRaw", Items.CHICKEN);
		OreDictionary.registerOre("listAllMeatRaw", Items.RABBIT);
		OreDictionary.registerOre("listAllMeatRaw", Items.MUTTON);

		OreDictionary.registerOre("logWood", new ItemStack(IIContent.blockRubberLog));
		OreDictionary.registerOre("woodRubber", new ItemStack(IIContent.blockRubberLog));
		OreDictionary.registerOre("blockLeaves", new ItemStack(IIContent.blockRubberLeaves));

		OreDictionary.registerOre("tnt", new ItemStack(Blocks.TNT));
		OreDictionary.registerOre("materialTNT", new ItemStack(Blocks.TNT));

		OreDictionary.registerOre("leadedConcrete", new ItemStack(IEContent.blockStoneDecoration, 1, BlockTypes_StoneDecoration.CONCRETE_LEADED.getMeta()));
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

		CrusherRecipe.addRecipe(IIContent.itemMaterialDust.getStack(MaterialsDust.SILICON, 1),
				new IngredientStack("plateSilicon"), 12000);

		IIRecipes.doRecipes(event.getRegistry());
		IICompatModule.doModulesRecipes();


		event.getRegistry().register(new RecipeSkinCraftingHandler().setRegistryName(ImmersiveIntelligence.MODID, "contributor_skin"));

		event.getRegistry().register(new RecipePowerpackAdvanced().setRegistryName(ImmersiveIntelligence.MODID, "powerpack_advanced"));
		assert IIContent.itemAdvancedPowerPack.getRegistryName()!=null;
		if(Arrays.stream(Tools.powerpack_blacklist).noneMatch(s -> s.equals(IIContent.itemAdvancedPowerPack.getRegistryName().toString())))
		{
			List<String> collect = new ArrayList<>(Arrays.asList(Tools.powerpack_blacklist));
			collect.add(IIContent.itemAdvancedPowerPack.getRegistryName().toString());
			Tools.powerpack_blacklist = collect.toArray(new String[0]);
		}

		VehicleFuelHandler.addVehicle(EntityMotorbike.class,
				FluidRegistry.getFluid("diesel"),
				FluidRegistry.getFluid("biodiesel")
		);
	}

	/**
	 * Works only for annotated CLASSES, not fields
	 */
	static String[] getAnnotatedOreDict(Object o)
	{
		String[] ores = null;
		if(o.getClass().isAnnotationPresent(IBatchOredictRegister.class))
			ores = o.getClass().getAnnotation(IBatchOredictRegister.class).oreDict();
		return ores;
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
		if(Railgun.enableRailgunOverride)
			IEContent.itemRailgun = new ItemIIRailgunOverride();
		ReflectionHelper.setPrivateValue(ToolUpgrades.class, ToolUpgrades.REVOLVER_BAYONET, ImmutableSet.of("REVOLVER", "SUBMACHINEGUN", "RIFLE"), "toolset");

		IEApi.prefixToIngotMap.put("spring", new Integer[]{2, 1});

		IIContent.init();

		//ALWAYS REGISTER BULLETS IN PRE-INIT! (so they get their texture registered before TextureStitchEvent.Pre)
		//Bullets

		AmmoRegistry.INSTANCE.registerBulletItem(IIContent.itemAmmoArtillery);
		AmmoRegistry.INSTANCE.registerBulletItem(IIContent.itemAmmoMortar);
		AmmoRegistry.INSTANCE.registerBulletItem(IIContent.itemAmmoLightArtillery);
		AmmoRegistry.INSTANCE.registerBulletItem(IIContent.itemAmmoAutocannon);
		AmmoRegistry.INSTANCE.registerBulletItem(IIContent.itemGrenade);
		AmmoRegistry.INSTANCE.registerBulletItem(IIContent.itemRailgunGrenade);

		AmmoRegistry.INSTANCE.registerBulletItem(IIContent.itemAmmoMachinegun);
		AmmoRegistry.INSTANCE.registerBulletItem(IIContent.itemAmmoSubmachinegun);
		AmmoRegistry.INSTANCE.registerBulletItem(IIContent.itemAmmoAssaultRifle);
		AmmoRegistry.INSTANCE.registerBulletItem(IIContent.itemAmmoRevolver);

		AmmoRegistry.INSTANCE.registerBulletItem((IAmmo)IIContent.blockTripmine.itemBlock);
		AmmoRegistry.INSTANCE.registerBulletItem((IAmmo)IIContent.blockTellermine.itemBlock);
		AmmoRegistry.INSTANCE.registerBulletItem((IAmmo)IIContent.blockRadioExplosives.itemBlock);
		AmmoRegistry.INSTANCE.registerBulletItem(IIContent.itemNavalMine);

		AmmoRegistry.INSTANCE.registerComponent(new AmmoComponentTNT());
		AmmoRegistry.INSTANCE.registerComponent(new AmmoComponentRDX());
		AmmoRegistry.INSTANCE.registerComponent(new AmmoComponentHMX());
		AmmoRegistry.INSTANCE.registerComponent(new AmmoComponentNuke());
		AmmoRegistry.INSTANCE.registerComponent(new AmmoComponentWhitePhosphorus());
		AmmoRegistry.INSTANCE.registerComponent(new AmmoComponentFirework());
		AmmoRegistry.INSTANCE.registerComponent(new AmmoComponentTracerPowder());
		AmmoRegistry.INSTANCE.registerComponent(new AmmoComponentFlarePowder());
		AmmoRegistry.INSTANCE.registerComponent(new AmmoComponentPropaganda());
		AmmoRegistry.INSTANCE.registerComponent(new AmmoComponentTesla());

		AmmoRegistry.INSTANCE.registerBulletCore(new AmmoCoreSteel());
		AmmoRegistry.INSTANCE.registerBulletCore(new AmmoCoreTungsten());
		AmmoRegistry.INSTANCE.registerBulletCore(new AmmoCoreBrass());
		AmmoRegistry.INSTANCE.registerBulletCore(new AmmoCoreLead());
		AmmoRegistry.INSTANCE.registerBulletCore(new AmmoCoreUranium());

		//ShrapnelHandler.addShrapnel("wood","",1,0.25f,0f,true);

		//Tiny dusts (1 -> 9) from GregTech are a bit too much :P
		DustUtils.registerDust(new IngredientStack("gunpowder", 100), "gunpowder", 0x242424);
		DustUtils.registerDust(new IngredientStack("smallGunpowder", 25), "gunpowder");
		DustUtils.registerDust(new IngredientStack("dustSulfur", 100), "sulfur", 0xbba31d);
		DustUtils.registerDust(new IngredientStack("dustSmallSulfur", 25), "sulfur");

		DustUtils.registerDust(new IngredientStack("dustWood", 100), "sawdust", 0x8c8269);
		DustUtils.registerDust(new IngredientStack("dustSmallWood", 25), "sawdust", 0x8c8269);
		DustUtils.registerDust(new IngredientStack("sand", 100), "sand", 0xaca37b);
		DustUtils.registerDust(new IngredientStack("gravel", 100), "gravel", 0x383937);

		ShrapnelHandler.addShrapnel("aluminum", 0xd9ecea, "immersiveengineering:textures/blocks/sheetmetal_aluminum", 1, 0.05f, 0f);
		ShrapnelHandler.addShrapnel("zinc", 0xdee3dc, "immersiveintelligence:textures/blocks/metal/sheetmetal_zinc", 1, 0.15f, 0f);
		ShrapnelHandler.addShrapnel("copper", 0xe37c26, "immersiveengineering:textures/blocks/sheetmetal_copper", 2, 0.25f, 0f);
		ShrapnelHandler.addShrapnel("platinum", 0xd8e1e1, "immersiveintelligence:textures/blocks/metal/sheetmetal_platinum", 2, 0.05f, 0f);
		ShrapnelHandler.addShrapnel("gold", 0xd1b039, "minecraft:textures/blocks/gold_block", 2, 0.25f, 0f);
		ShrapnelHandler.addShrapnel("nickel", 0x838877, "immersiveengineering:textures/blocks/sheetmetal_nickel", 2, 0.25f, 0f);
		ShrapnelHandler.addShrapnel("silver", 0xa7cac8, "immersiveengineering:textures/blocks/sheetmetal_silver", 2, 0.25f, 0f);
		ShrapnelHandler.addShrapnel("electrum", 0xf6ad59, "immersiveengineering:textures/blocks/sheetmetal_electrum", 2, 0.25f, 0f);
		ShrapnelHandler.addShrapnel("constantan", 0xf97456, "immersiveengineering:textures/blocks/sheetmetal_constantan", 3, 0.25f, 0f);
		ShrapnelHandler.addShrapnel("brass", 0x957743, "immersiveintelligence:textures/blocks/metal/sheetmetal_brass", 3, 0.35f, 0f);
		ShrapnelHandler.addShrapnel("iron", 0xc7c7c7, "minecraft:textures/blocks/iron_block", 4, 0.25f, 0f);
		ShrapnelHandler.addShrapnel("lead", 0x3a3e44, "immersiveengineering:textures/blocks/sheetmetal_lead", 4, 0.75f, 0f);
		ShrapnelHandler.addShrapnel("steel", 0x4d4d4d, "immersiveengineering:textures/blocks/sheetmetal_steel", 6, 0.35f, 0f);
		ShrapnelHandler.addShrapnel("tungsten", 0x3b3e43, "immersiveintelligence:textures/blocks/metal/sheetmetal_tungsten", 7, 0.45f, 0f);
		ShrapnelHandler.addShrapnel("HOPGraphite", 0x282828, "immersiveengineering:textures/blocks/stone_decoration_coke", 7, 0.45f, 0f);
		ShrapnelHandler.addShrapnel("uranium", 0x659269, "immersiveengineering:textures/blocks/sheetmetal_uranium", 8, 0.45f, 8f);

		//easter eggs
		AmmoRegistry.INSTANCE.registerComponent(new AmmoComponentFish());
		AmmoRegistry.INSTANCE.registerBulletCore(new AmmoCorePabilium());

		for(Entry<String, Shrapnel> s : ShrapnelHandler.registry.entrySet())
		{
			AmmoComponentShrapnel shrapnel = new AmmoComponentShrapnel(s.getKey());
			AmmoRegistry.INSTANCE.registerComponent(shrapnel);
		}

		BulletHandler.registerBullet("ii_bullet", IIContent.itemAmmoRevolver);

		ConveyorHandler.registerConveyorHandler(new ResourceLocation(ImmersiveIntelligence.MODID, "rubber_conveyor"), ConveyorRubber.class, (tileEntity) -> new ConveyorRubber());
		ConveyorHandler.registerConveyorHandler(new ResourceLocation(ImmersiveIntelligence.MODID, "rubber_uncontrolled"), ConveyorRubberUncontrolled.class, (tileEntity) -> new ConveyorRubberUncontrolled());
		ConveyorHandler.registerConveyorHandler(new ResourceLocation(ImmersiveIntelligence.MODID, "rubber_dropper"), ConveyorRubberDropper.class, (tileEntity) -> new ConveyorRubberDropper());
		ConveyorHandler.registerConveyorHandler(new ResourceLocation(ImmersiveIntelligence.MODID, "rubber_droppercovered"), ConveyorRubberCoveredDropper.class, (tileEntity) -> new ConveyorRubberCoveredDropper());
		ConveyorHandler.registerConveyorHandler(new ResourceLocation(ImmersiveIntelligence.MODID, "rubber_vertical"), ConveyorRubberVertical.class, (tileEntity) -> new ConveyorRubberVertical());
		ConveyorHandler.registerConveyorHandler(new ResourceLocation(ImmersiveIntelligence.MODID, "rubber_splitter"), ConveyorRubberSplitter.class, (tileEntity) -> new ConveyorRubberSplitter(tileEntity instanceof IConveyorTile?((IConveyorTile)tileEntity).getFacing(): EnumFacing.NORTH));
		ConveyorHandler.registerConveyorHandler(new ResourceLocation(ImmersiveIntelligence.MODID, "rubber_covered"), ConveyorRubberCovered.class, (tileEntity) -> new ConveyorRubberCovered());
		ConveyorHandler.registerConveyorHandler(new ResourceLocation(ImmersiveIntelligence.MODID, "rubber_verticalcovered"), ConveyorRubberCoveredVertical.class, (tileEntity) -> new ConveyorRubberCoveredVertical());
		ConveyorHandler.registerConveyorHandler(new ResourceLocation(ImmersiveIntelligence.MODID, "rubber_extract"), ConveyorRubberExtract.class, (tileEntity) -> new ConveyorRubberExtract(tileEntity instanceof IConveyorTile?((IConveyorTile)tileEntity).getFacing(): EnumFacing.NORTH));
		ConveyorHandler.registerConveyorHandler(new ResourceLocation(ImmersiveIntelligence.MODID, "rubber_extractcovered"), ConveyorRubberCoveredExtract.class, (tileEntity) -> new ConveyorRubberCoveredExtract(tileEntity instanceof IConveyorTile?((IConveyorTile)tileEntity).getFacing(): EnumFacing.NORTH));

		IICompatModule.doModulesPreInit();
	}

	public void init()
	{
		IICompatModule.doModulesInit();

		for(Fluid f : FluidRegistry.getRegisteredFluids().values())
		{
			AmmoComponentFluid comp = new AmmoComponentFluid(f);
			AmmoRegistry.INSTANCE.registerComponent(comp);
		}

		IIContent.blockFluidInkBlack.setPotionEffects(new PotionEffect(IEPotions.sticky, 60, 0));
		IIContent.blockFluidInkCyan.setPotionEffects(new PotionEffect(IEPotions.sticky, 60, 0));
		IIContent.blockFluidInkMagenta.setPotionEffects(new PotionEffect(IEPotions.sticky, 60, 0));
		IIContent.blockFluidInkYellow.setPotionEffects(new PotionEffect(IEPotions.sticky, 60, 0));
		IIContent.blockFluidLatex.setPotionEffects(new PotionEffect(IEPotions.sticky, 60, 0));

		//Blocks config
		for(Block block : IIContent.BLOCKS)
			if(block instanceof BlockIIBase)
				((BlockIIBase<?>)block).parseSubBlocks();

		//Worldgen registration
		IILogger.info("Registering Worldgen");
		IIWorldGen iiWorldGen = new IIWorldGen();
		GameRegistry.registerWorldGenerator(iiWorldGen, 0);
		MinecraftForge.EVENT_BUS.register(iiWorldGen);

		IILogger.info("Adding oregen");
		addConfiguredWorldgen(IIContent.blockOre.getStateFromMeta(Ores.PLATINUM.getMeta()), "platinum", pl.pabilo8.immersiveintelligence.Config.IIConfig.Ores.orePlatinum, EnumOreType.OVERWORLD);
		addConfiguredWorldgen(IIContent.blockOre.getStateFromMeta(Ores.ZINC.getMeta()), "zinc", pl.pabilo8.immersiveintelligence.Config.IIConfig.Ores.oreZinc, EnumOreType.OVERWORLD);
		addConfiguredWorldgen(IIContent.blockOre.getStateFromMeta(Ores.TUNGSTEN.getMeta()), "tungsten", pl.pabilo8.immersiveintelligence.Config.IIConfig.Ores.oreTungsten, EnumOreType.OVERWORLD);
		addConfiguredWorldgen(IIContent.blockOre.getStateFromMeta(Ores.SALT.getMeta()), "salt", pl.pabilo8.immersiveintelligence.Config.IIConfig.Ores.oreSalt, EnumOreType.OVERWORLD);
		addConfiguredWorldgen(IIContent.blockOre.getStateFromMeta(Ores.FLUORITE.getMeta()), "fluorite", pl.pabilo8.immersiveintelligence.Config.IIConfig.Ores.oreFluorite, EnumOreType.NETHER);
		addConfiguredWorldgen(IIContent.blockOre.getStateFromMeta(Ores.PHOSPHORUS.getMeta()), "phosphorus", pl.pabilo8.immersiveintelligence.Config.IIConfig.Ores.orePhosphorus, EnumOreType.NETHER);

		IILogger.info("Adding Railgun Projectiles");
		RailgunHandler.registerProjectileProperties(new IngredientStack("stickTungsten"), 32, 1.3).setColourMap(new int[][]{{0xCBD1D6, 0xCBD1D6, 0xCBD1D6, 0xCBD1D6, 0x9EA2A7, 0x9EA2A7}});

		//Disallow crates in crates
		IEApi.forbiddenInCrates.add((stack) ->
		{
			if(OreDictionary.itemMatches(new ItemStack(IIContent.blockMetalDevice, 1, 0), stack, true))
				return true;
			if(stack.getItem()==IIContent.itemMinecart)
				return true;
			return OreDictionary.itemMatches(new ItemStack(IIContent.blockMetalDevice, 1, 1), stack, true);
		});

		IIContent.tileEntitiesWeDontLike.add(tileEntity -> tileEntity instanceof TileEntityChargingStation);

		IEApi.forbiddenInCrates.add((stack) -> stack.getItem() instanceof ItemBlockIEBase&&((ItemBlockIEBase)stack.getItem()).getBlock() instanceof BlockIISmallCrate);

		IILogger.info("Adding TileEntities");
		IIContent.TILE_ENTITIES.stream().distinct().forEach(CommonProxy::registerTile);
		IILogger.info("Registering Multiblocks");
		IIContent.MULTIBLOCKS.forEach(MultiblockHandler::registerMultiblock);


		//Entities
		int i = -1;

		//Minecarts
		registerEntity(i++, EntityMinecartCrateWooden.class, "minecart_wooden_crate", 64, 1, true);
		registerEntity(i++, EntityMinecartCrateReinforced.class, "minecart_reinforced_crate", 64, 1, true);
		registerEntity(i++, EntityMinecartCrateSteel.class, "minecart_steel_crate", 64, 1, true);
		registerEntity(i++, EntityMinecartBarrelWooden.class, "minecart_wooden_barrel", 64, 1, true);
		registerEntity(i++, EntityMinecartBarrelSteel.class, "minecart_metal_barrel", 64, 1, true);

		//Finally Skycrates are a thing! ^^
		registerEntity(i++, EntitySkyCrate.class, "skycrate", 64, 1, true);
		registerEntity(i++, EntityBullet.class, "bullet", 32, 1, true);
		registerEntity(i++, EntityNavalMine.class, "naval_mine", 64, 1, true);
		registerEntity(i++, EntityNavalMineAnchor.class, "naval_mine_anchor", 64, 1, true);
		registerEntity(i++, EntityShrapnel.class, "shrapnel", 16, 1, true);
		registerEntity(i++, EntityWhitePhosphorus.class, "white_phosphorus", 16, 1, true);

		registerEntity(i++, EntityMachinegun.class, "machinegun", 64, 1, true);
		registerEntity(i++, EntitySkycrateInternal.class, "skycrate_internal", 64, 1, true);

		registerEntity(i++, EntityMotorbike.class, "motorbike", 64, 20, true);
		registerEntity(i++, EntityFieldHowitzer.class, "field_howitzer", 64, 20, true);
		registerEntity(i++, EntityVehicleSeat.class, "seat", 64, 1, true);

		registerEntity(i++, EntityTripodPeriscope.class, "tripod_periscope", 64, 1, true);
		registerEntity(i++, EntityAtomicBoom.class, "atomic_boom", 64, 1, true);
		registerEntity(i++, EntityGasCloud.class, "gas_cloud", 64, 1, true);

		registerEntity(i++, EntityHans.class, "hans", 64, 4, true);

		registerEntity(i++, EntityFlare.class, "flare", 64, 4, true);
		registerEntity(i++, EntityParachute.class, "parachute", 64, 4, true);
		registerEntity(i++, EntityEmplacementWeapon.class, "emplacement_weapon", 64, 4, false);
		registerEntity(i++, EntityMortar.class, "mortar", 64, 1, false);

		registerEntity(i++, EntityMinecartCapacitorLV.class, "minecart_capacitor_lv", 64, 1, true);
		registerEntity(i++, EntityMinecartCapacitorMV.class, "minecart_capacitor_mv", 64, 1, true);
		registerEntity(i++, EntityMinecartCapacitorHV.class, "minecart_capacitor", 64, 1, true);
		registerEntity(i++, EntityMinecartCapacitorCreative.class, "minecart_capacitor_creative", 64, 1, true);

		registerEntity(i++, EntityDrone.class, "drone", 64, 1, true);

		registerEntity(i++, EntityIIChemthrowerShot.class, "chemthrower_shot", 64, 1, true);

		/*
		Soon™
		EntityRegistry.registerModEntity(new ResourceLocation(ImmersiveIntelligence.MODID, "panzer"),
				EntityMotorbike.class, "panzer", i++, ImmersiveIntelligence.INSTANCE, 64, 1, true);
		 */
	}

	public void postInit()
	{
		IICompatModule.doModulesPostInit();
		//Init Hans Weapons
		HansUtils.init();

		for(Minecarts value : Minecarts.values())
			MinecartBlockHelper.blocks.put(stack -> OreDictionary.itemMatches(stack, value.stack.get(), false), world -> value.minecart.apply(world, Vec3d.ZERO));

		// TODO: 07.11.2021 register

		RotaryUtils.ie_rotational_blocks_torque.put(tileEntity -> tileEntity instanceof TileEntityWindmill,
				aFloat -> aFloat*MechanicalDevices.dynamoWindmillTorque
		);

		RotaryUtils.ie_rotational_blocks_torque.put(tileEntity -> tileEntity instanceof TileEntityWatermill,
				aFloat -> aFloat*MechanicalDevices.dynamoWatermillTorque
		);


		CorrosionHandler.addItemToBlacklist(new ItemStack(Items.DIAMOND_HELMET));
		CorrosionHandler.addItemToBlacklist(new ItemStack(Items.DIAMOND_CHESTPLATE));
		CorrosionHandler.addItemToBlacklist(new ItemStack(Items.DIAMOND_LEGGINGS));
		CorrosionHandler.addItemToBlacklist(new ItemStack(Items.DIAMOND_BOOTS));

		for(IMultiblock mb : MultiblockHandler.getMultiblocks())
		{
			if(mb instanceof MultiblockStuctureBase)
				((MultiblockStuctureBase<?>)mb).updateStructure();
		}
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		ItemStack stack = player.getHeldItem(player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IGuiItem?EnumHand.MAIN_HAND: EnumHand.OFF_HAND);

		if(ID==IIGuiList.GUI_UPGRADE.ordinal()&&te instanceof IUpgradableMachine)
		{
			TileEntity upgradeMaster = ((IUpgradableMachine)te).getUpgradeMaster();
			if(upgradeMaster!=null)
				return new ContainerUpgrade(player, (TileEntity & IUpgradableMachine)upgradeMaster);
		}

		if(IIGuiList.values().length > ID)
		{
			IIGuiList gui = IIGuiList.values()[ID];

			if(gui.teClass==null||gui.container==null||gui.guiFromTile==null)
				return null;
			else if(te instanceof IGuiTile&&gui.teClass.isInstance(te))
			{
				Container opened = gui.container.apply(player, te);
				if(opened!=null)
				{
					((IGuiTile)te).onGuiOpened(player, false);
					return opened;
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
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageBlockDamageSync(dpos), IIPacketHandler.targetPointFromPos(dpos, event.getWorld(), 32));
		}
	}

	@SubscribeEvent
	public void onMultiblockForm(MultiblockFormEvent.Post event)
	{
		if(event.isCancelable()&&!event.isCanceled()&&event.getMultiblock().getClass().isAnnotationPresent(IAdvancedMultiblock.class))
		{
			//Required by Advanced Structures!
			if(!IIUtils.isAdvancedHammer(event.getHammer()))
			{
				if(!event.getEntityPlayer().getEntityWorld().isRemote)
					IIPacketHandler.sendChatTranslation(event.getEntityPlayer(), "info.immersiveintelligence.requires_advanced_hammer");
				event.setCanceled(true);
			}
		}
	}

	public void reloadModels()
	{

	}

	public void reloadManual()
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

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onLivingUpdate(LivingUpdateEvent event)
	{
		if(!(event.getEntityLiving() instanceof EntityPlayer&&((EntityPlayer)event.getEntityLiving()).isCreative())&&event.getEntityLiving().world.getTotalWorldTime()%20==0&&event.getEntityLiving().world.getBiome(event.getEntityLiving().getPosition())==IIContent.biomeWasteland)
			event.getEntityLiving().addPotionEffect(new PotionEffect(IIPotions.radiation, 2000, 0, false, false));
		if(event.getEntityLiving() instanceof EntityPlayer&&!event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty()&&ItemNBTHelper.hasKey(event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.CHEST), IIContent.NBT_AdvancedPowerpack))
		{
			ItemStack powerpack = ItemNBTHelper.getItemStack(event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.CHEST), IIContent.NBT_AdvancedPowerpack);
			if(!powerpack.isEmpty())
				powerpack.getItem().onArmorTick(event.getEntityLiving().getEntityWorld(), (EntityPlayer)event.getEntityLiving(), powerpack);
		}
	}

	@SubscribeEvent
	public void spawnEvent(EntityJoinWorldEvent event)
	{
		if(event.getEntity() instanceof EntityMob)
		{
			EntityMob e = (EntityMob)event.getEntity();
			e.targetTasks.addTask(4, new EntityAINearestAttackableTarget<>(e, EntityHans.class, true));
		}
	}

	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		ItemStack head, chest, legs, boots;
		head = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		chest = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		legs = entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		boots = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);

		//plates
		if(event.getSource()==DamageSource.CACTUS||(event.getSource() instanceof EntityDamageSourceIndirect&&event.getSource().getImmediateSource() instanceof EntityArrow))
		{
			if(ItemIIUpgradeableArmor.isArmorWithUpgrade(boots, "toughness_increase"))
				event.setCanceled(true);
		}
		//heat resist
		else if(event.getSource()==DamageSource.IN_FIRE||event.getSource()==DamageSource.HOT_FLOOR)
		{
			if(ItemIIUpgradeableArmor.isArmorWithUpgrade(chest, "heat_coating")&&ItemIIUpgradeableArmor.isArmorWithUpgrade(boots, "reinforced"))
				event.setCanceled(true);
		}
		//springs
		else if(event.getSource()==DamageSource.FALL)
		{
			if(ItemIIUpgradeableArmor.isArmorWithUpgrade(boots, "springs"))
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void hurtEvent(LivingHurtEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		ItemStack head, chest, legs, boots;
		head = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		chest = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		legs = entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		boots = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);

		//plates
		if(event.getSource()==DamageSource.CACTUS||(event.getSource() instanceof EntityDamageSourceIndirect&&event.getSource().getImmediateSource() instanceof EntityArrow))
		{
			if(ItemIIUpgradeableArmor.isArmorWithUpgrade(boots, "toughness_increase"))
				event.setCanceled(true);
		}
		//heat resist
		else if(event.getSource()==DamageSource.IN_FIRE||event.getSource()==DamageSource.HOT_FLOOR)
		{
			if(ItemIIUpgradeableArmor.isArmorWithUpgrade(chest, "heat_coating")&&ItemIIUpgradeableArmor.isArmorWithUpgrade(boots, "reinforced"))
				event.setCanceled(true);
		}
		//springs
		else if(event.getSource()==DamageSource.FALL)
		{
			if(ItemIIUpgradeableArmor.isArmorWithUpgrade(boots, "springs"))
				event.setCanceled(true);
		}
	}
}
