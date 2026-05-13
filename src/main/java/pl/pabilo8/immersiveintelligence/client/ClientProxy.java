package pl.pabilo8.immersiveintelligence.client;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireApi;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.IEDefaultColourHandlers;
import blusunrize.immersiveengineering.client.models.obj.IEOBJLoader;
import blusunrize.immersiveengineering.client.render.EntityRenderNone;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IColouredBlock;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IColouredItem;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IGuiItem;
import blusunrize.immersiveengineering.common.items.ItemIEBase;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent.Pre;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.Shrapnel;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.api.upgrade.IUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticles;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleSystem;
import pl.pabilo8.immersiveintelligence.client.gui.block.GuiUpgrade;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualCategory;
import pl.pabilo8.immersiveintelligence.client.manual.categories.*;
import pl.pabilo8.immersiveintelligence.client.model.IIModelRegistry;
import pl.pabilo8.immersiveintelligence.client.model.TextureRecoloringRegistry;
import pl.pabilo8.immersiveintelligence.client.model.builtin.FluidStateMapper;
import pl.pabilo8.immersiveintelligence.client.model.item.ModelMeasuringCup;
import pl.pabilo8.immersiveintelligence.client.model.item.ModelMeasuringCup.MeasuringCupModelLoader;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.render.ammunition.*;
import pl.pabilo8.immersiveintelligence.client.render.ammunition.NavalMineRenderer.NavalMineItemstackRenderer;
import pl.pabilo8.immersiveintelligence.client.render.entity.*;
import pl.pabilo8.immersiveintelligence.client.render.entity.hans.HansRenderer;
import pl.pabilo8.immersiveintelligence.client.render.entity.vehicle.FieldGunRenderer;
import pl.pabilo8.immersiveintelligence.client.render.entity.vehicle.FieldHowitzerRenderer;
import pl.pabilo8.immersiveintelligence.client.render.entity.vehicle.MotorbikeRenderer;
import pl.pabilo8.immersiveintelligence.client.render.entity.vehicle.TrackedMotorbikeRenderer;
import pl.pabilo8.immersiveintelligence.client.render.entity.weapon.MachinegunRenderer;
import pl.pabilo8.immersiveintelligence.client.render.entity.weapon.MortarRenderer;
import pl.pabilo8.immersiveintelligence.client.render.entity.weapon.TripodPeriscopeRenderer;
import pl.pabilo8.immersiveintelligence.client.render.inserter.AdvancedFluidInserterRenderer;
import pl.pabilo8.immersiveintelligence.client.render.inserter.AdvancedInserterRenderer;
import pl.pabilo8.immersiveintelligence.client.render.inserter.FluidInserterRenderer;
import pl.pabilo8.immersiveintelligence.client.render.inserter.InserterRenderer;
import pl.pabilo8.immersiveintelligence.client.render.item.*;
import pl.pabilo8.immersiveintelligence.client.render.mechanical_device.BeltModelStorage;
import pl.pabilo8.immersiveintelligence.client.render.mechanical_device.MechanicalPumpRenderer;
import pl.pabilo8.immersiveintelligence.client.render.mechanical_device.WheelRenderer;
import pl.pabilo8.immersiveintelligence.client.render.metal_device.*;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.*;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.emplacementweapon.*;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.wooden.*;
import pl.pabilo8.immersiveintelligence.client.util.IICustomStateMapper;
import pl.pabilo8.immersiveintelligence.client.util.IIKeybind;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIItemRendererAMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIItemRendererAMT.RegisteredItemRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRenderer;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRendererCustomGlyphs;
import pl.pabilo8.immersiveintelligence.common.*;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Factions;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMerger;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityRedstoneBuffer;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntitySmallDataBuffer;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityTimedBuffer;
import pl.pabilo8.immersiveintelligence.common.block.fortification.tileentity.TileEntityMineSign;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityLatexCollector;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.conveyors.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockAluminiumChainFenceGate.TileEntityAluminiumChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockAluminiumFenceGate.TileEntityAluminiumFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockSteelChainFenceGate.TileEntitySteelChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockSteelFenceGate.TileEntitySteelFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockWoodenChainFenceGate.TileEntityWoodenChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockWoodenFenceGate.TileEntityWoodenFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityChemicalBath;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityVehicleWorkshop;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityVulcanizer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCartStation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCratePost;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.entity.*;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.*;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.naval_mine.EntityNavalMine;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.naval_mine.EntityNavalMineAnchor;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.EntityAMTTactile;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityDrone;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityMotorbike;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityTrackedMotorbike;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.gun.EntityFieldGun;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.gun.EntityFieldHowitzer;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIINavalMine;
import pl.pabilo8.immersiveintelligence.common.item.ammo.gun.ItemIIAmmoRevolver;
import pl.pabilo8.immersiveintelligence.common.item.ammo.gun.ItemIIAmmoRevolver.RevolverAmmoPart;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit.CircuitTypes;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIDrillHead.DrillHeads;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIFluid;
import pl.pabilo8.immersiveintelligence.common.util.block.IIIStateMappings;
import pl.pabilo8.immersiveintelligence.common.util.block.IIIStateMappings.DummyEnum;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.item.IIIItemTextureOverride;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 07.05.2019
 */
@SuppressWarnings("deprecation")
@EventBusSubscriber(value = Side.CLIENT, modid = ImmersiveIntelligence.MODID)
public class ClientProxy extends CommonProxy
{
	public static KeyBinding keybindManualReload, keybindArmorHelmet, keybindArmorExosuit, keybindZoom;
	public static KeyBinding keybindVehicleEngine, keybindVehicleClutch, keybindVehicleTowing;
	public static KeyBinding keybindVehicleGearUp, keybindVehicleGearDown, keybindVehicleReductionSwitch;
	private EasyNBT storedGuiData = EasyNBT.newNBT();

	private HashMap<Class<? extends TileEntityItemStackRenderer>, Block> TEISRRegistryQueue = new HashMap<>();

	public ClientProxy()
	{

	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent evt)
	{
		//You've tricked me
		//I thought the connector rendering is based on something different, but actually it renders the obj connector model with the wire
		WireApi.registerConnectorForRender("empty", new ResourceLocation(ImmersiveIntelligence.MODID+":block/empty.obj"), null);
		WireApi.registerConnectorForRender("tripwire", new ResourceLocation(ImmersiveIntelligence.MODID+":block/tripwire_connector.obj"), null);

		for(Block block : IIContent.BLOCKS)
		{
			ResourceLocation loc = Block.REGISTRY.getNameForObject(block);
			Item blockItem = Item.getItemFromBlock(block);
			//Separate mappings for Fluids
			if(block instanceof BlockIIFluid)
				mapFluidState(block, ((BlockIIFluid)block).getFluid());
				//Block Mappings based on Name/Meta/Custom
			else if(block instanceof IIIStateMappings)
			{
				IIIStateMappings<?> mappings = (IIIStateMappings<?>)block;
				loc = new ResourceLocation(loc.getResourceDomain(), mappings.getMappingsName());
				ModelLoader.setCustomStateMapper(block, IICustomStateMapper.getStateMapper(mappings));
				ResourceLocation finalLoc = loc;
				ModelLoader.setCustomMeshDefinition(blockItem, stack -> new ModelResourceLocation(finalLoc, "inventory"));

				//Check if the block uses custom IDs
				if(mappings.getMappingsEnum()!=null)
				{
					List<Enum<?>> list = new ArrayList<>(Arrays.asList(mappings.getMappingsEnum()));
					@SuppressWarnings("unchecked")
					List<Enum<?>> legacyTESR = (List<Enum<?>>)mappings.getLegacyTESR();

					//Add itemblock models for legacy TMT based TESR
					if(legacyTESR!=null)
					{
						try
						{
							for(Enum<?> meta : legacyTESR)
								ModelLoader.setCustomModelResourceLocation(blockItem, meta.ordinal(), new ModelResourceLocation(
										new ResourceLocation(ImmersiveIntelligence.MODID, "itemblock/"+meta.name()), "inventory"));
							list.removeAll(legacyTESR);
						} catch(Exception e)
						{
							IILogger.error("Couldn't register a legacy TESR for %s!", block);
						}
					}
					//Add itemblock models for modern custom mappings
					for(Enum<?> meta : list)
					{
						String location = loc.toString();
						String custom = mappings.getMappingsExtension(meta.ordinal(), true);
						if(custom!=null)
							location += "/"+custom;
						String variant = meta.equals(DummyEnum.NULL)?"inventory": ("inventory,type="+meta.name());

						try
						{
							ModelLoader.setCustomModelResourceLocation(blockItem, meta.ordinal(), new ModelResourceLocation(location, variant));
						} catch(NullPointerException npe)
						{
							throw new RuntimeException("Ohno, apparently "+block+" lacks an item!", npe);
						}
					}
				}

			}
			//Thy End
			else
				ModelLoader.setCustomModelResourceLocation(blockItem, 0, new ModelResourceLocation(loc, "inventory"));
		}

		//--- Item Models ---//
		for(Item item : IIContent.ITEMS)
		{
			if(item instanceof ItemBlock)
				continue;
			if(item instanceof ItemIIBase)
			{
				ItemIIBase ieMetaItem = (ItemIIBase)item;
				if(ieMetaItem instanceof ItemIISubItemsBase)
					for(IIItemEnum subItem : ((ItemIISubItemsBase<?>)ieMetaItem).getSubItems())
					{
						ResourceLocation loc = new ResourceLocation(ImmersiveIntelligence.MODID, ieMetaItem.itemName+"/"+subItem.getName());
						ModelBakery.registerItemVariants(ieMetaItem, loc);
						ModelLoader.setCustomModelResourceLocation(ieMetaItem, subItem.getMeta(), new ModelResourceLocation(loc, "inventory"));
					}
				else
				{
					final ResourceLocation loc = new ResourceLocation(ImmersiveIntelligence.MODID, ieMetaItem.itemName);
					ModelBakery.registerItemVariants(ieMetaItem, loc);
					ModelLoader.setCustomMeshDefinition(ieMetaItem, stack -> new ModelResourceLocation(loc, "inventory"));
				}
			}
			else if(item instanceof ItemIEBase&&item.getHasSubtypes())
			{
				ItemIEBase ieBase = (ItemIEBase)item;

				String[] subNames = ieBase.getSubNames();
				for(int i = 0, subNamesLength = subNames.length; i < subNamesLength; i++)
				{
					String sub = subNames[i];
					ResourceLocation loc = new ResourceLocation(ImmersiveIntelligence.MODID, ieBase.itemName+"/"+sub);
					ModelBakery.registerItemVariants(ieBase, loc);
					ModelLoader.setCustomModelResourceLocation(ieBase, i, new ModelResourceLocation(loc, "inventory"));
				}
			}
			else
			{
				final ResourceLocation loc = Item.REGISTRY.getNameForObject(item);
				ModelBakery.registerItemVariants(item, loc);
				ModelLoader.setCustomMeshDefinition(item, stack -> new ModelResourceLocation(loc, "inventory"));
			}
		}

		//Measuring Cup
		ModelLoaderRegistry.registerLoader(MeasuringCupModelLoader.INSTANCE);
		ModelLoader.setCustomMeshDefinition(IIContent.itemMeasuringCup, stack -> ModelMeasuringCup.MODEL.getLocation());
		ModelBakery.registerItemVariants(IIContent.itemMeasuringCup, ModelMeasuringCup.MODEL.getLocation());


		AmmoRegistry.registerAmmoModels();
	}

	private static void mapFluidState(Block block, Fluid fluid)
	{
		Item item = Item.getItemFromBlock(block);
		FluidStateMapper mapper = new FluidStateMapper(fluid);
		ModelLoader.registerItemVariants(item);
		ModelLoader.setCustomMeshDefinition(item, mapper);
		ModelLoader.setCustomStateMapper(block, mapper);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID < 0)
		{
			IILogger.warn("Trying to access a null GUI on client. Most likely it's work-in-progress or not bound to source yet.");
			return null;
		}

		EnumHand hand;
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		ItemStack stack = player.getHeldItem(hand = (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IGuiItem?EnumHand.MAIN_HAND: EnumHand.OFF_HAND));

		if(ID==IIGUI.UPGRADE.ordinal()&&te instanceof IUpgradableDevice)
		{
			IUpgradableDevice upgradeMaster = ((IUpgradableDevice)te).master();
			if(upgradeMaster!=null)
				//noinspection rawtypes,unchecked
				return new GuiUpgrade(player, ((TileEntityIEBase)upgradeMaster));
		}

		GuiScreen gui = null;
		if(IIGUI.values().length > ID)
		{
			IIGUI guiBuilder = IIGUI.values()[ID];
			if(guiBuilder.item)
				return guiBuilder.guiFromStack.apply(player, stack, hand);

			if(te instanceof IGuiTile&&guiBuilder.teClass.isInstance(te))
				if((gui = guiBuilder.guiFromTile.apply(player, te))!=null)
					((IGuiTile)te).onGuiOpened(player, true);
		}

		if(gui==null)
			IILogger.warn("Trying to access a GUI on client, but no GUI is registered for ID "+ID);
		return gui;
	}

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		//long live .obj models! ^^
		super.preInit(event);
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(IIModelRegistry.INSTANCE);
		OBJLoader.INSTANCE.addDomain(ImmersiveIntelligence.MODID);
		IEOBJLoader.instance.addDomain(ImmersiveIntelligence.MODID);

		//Load particle models
		IIParticles.preInit();
		IIParticles.init();
		ParticleRegistry.loadAllParticleFiles();

		//Register entity renderers
		registerEntityRenderer(EntitySkyCrate.class, SkyCrateRenderer::new);
		registerEntityRenderer(EntityAmmoBase.class, AmmoRenderer::new);
		registerEntityRenderer(EntityAmmoProjectile.class, ProjectileAmmoRenderer::new);
		registerEntityRenderer(EntityNavalMine.class, NavalMineRenderer::new);
		registerEntityRenderer(EntityNavalMineAnchor.class, NavalMineAnchorRenderer::new);
		registerEntityRenderer(EntityShrapnel.class, ShrapnelRenderer::new);
		registerEntityRenderer(EntityWhitePhosphorus.class, EntityRenderNone::new);
		registerEntityRenderer(EntityMachinegun.class, MachinegunRenderer::new);
		registerEntityRenderer(EntityMotorbike.class, MotorbikeRenderer::new);
		registerEntityRenderer(EntityTrackedMotorbike.class, TrackedMotorbikeRenderer::new);
		registerEntityRenderer(EntityDrone.class, DroneRenderer::new);
		//Towables
		registerEntityRenderer(EntityFieldHowitzer.class, FieldHowitzerRenderer::new);
		registerEntityRenderer(EntityFieldGun.class, FieldGunRenderer::new);

		registerEntityRenderer(EntityTripodPeriscope.class, TripodPeriscopeRenderer::new);
		registerEntityRenderer(EntityMortar.class, MortarRenderer::new);
		registerEntityRenderer(EntityCamera.class, EntityRenderNone::new);
		registerEntityRenderer(EntitySkycrateInternal.class, EntityRenderNone::new);
		registerEntityRenderer(EntityVehicleSeat.class, EntityRenderNone::new);

		registerEntityRenderer(EntityAtomicBoom.class, AtomicBoomRenderer::new);
		registerEntityRenderer(EntityGasCloud.class, EntityRenderNone::new);
		registerEntityRenderer(EntityFlare.class, EntityRenderNone::new);

		registerEntityRenderer(EntityHans.class, HansRenderer::new);
		registerEntityRenderer(EntityParachute.class, ParachuteRenderer::new);
		registerEntityRenderer(EntityAMTTactile.class, EntityRenderNone::new);

		//Hand Weapons
		registerItemRenderer(IIContent.itemAssaultRifle, new AssaultRifleRenderer());
		registerItemRenderer(IIContent.itemAssaultRifle, new AssaultRifleRenderer());
		registerItemRenderer(IIContent.itemRifle, new RifleRenderer());
		registerItemRenderer(IIContent.itemSubmachinegun, new SubmachinegunRenderer());

		//Tools
		//TODO: 22.09.2024 binoculars
		registerItemRenderer(IIContent.itemRadioTuner, new RadioTunerRenderer());
		registerItemRenderer(IIContent.itemTachometer, new TachometerRenderer());
		//TODO: 22.09.2024 mine detector renderer
		//TODO: 22.09.2024 power tool 3D models

		for(IAmmoTypeItem<?, ?> bullet : AmmoRegistry.getAllAmmoItems())
		{
			if(bullet instanceof ItemIINavalMine)
				continue;
			if(bullet instanceof ItemIIAmmoBase)
				//noinspection rawtypes
				IIModelRegistry.INSTANCE.registerCustomItemModel(((ItemIIAmmoBase)bullet));
			else if(bullet instanceof ItemIIAmmoRevolver)
				IIModelRegistry.INSTANCE.registerCustomItemModel((ItemIIAmmoRevolver)bullet, ImmersiveIntelligence.MODID,
						RevolverAmmoPart.BULLET.ordinal(), RevolverAmmoPart.CORE.ordinal());
		}

		//Old items
		IIModelRegistry.INSTANCE.registerCustomItemModel(IIContent.itemBulletMagazine);
		IIModelRegistry.INSTANCE.registerCustomItemModel(IIContent.itemBinoculars);
		IIModelRegistry.INSTANCE.registerCustomItemModel(IIContent.itemCasingPouch);

		//Mechanical
		registerTileRenderer(SawmillRenderer.class);
		registerTileRenderer(MechanicalPumpRenderer.class);
		registerTileRenderer(WheelRenderer.class);
		new BeltModelStorage().subscribeToList("mechanical/belt");

		//Weapons (Items)
		IIContent.itemMachinegun.setTileEntityItemStackRenderer(MachinegunItemStackRenderer.instance);
		ItemIIWeaponUpgrade.addUpgradesToRender();

		IIContent.itemLightEngineerHelmet.setTileEntityItemStackRenderer(LightEngineerArmorItemStackRenderer.instance);
		IIContent.itemLightEngineerChestplate.setTileEntityItemStackRenderer(LightEngineerArmorItemStackRenderer.instance);
		IIContent.itemLightEngineerLeggings.setTileEntityItemStackRenderer(LightEngineerArmorItemStackRenderer.instance);
		IIContent.itemLightEngineerBoots.setTileEntityItemStackRenderer(LightEngineerArmorItemStackRenderer.instance);

		//Data Connectors
		registerTileRenderer(AmmunitionCrateRenderer.class);
		registerTileRenderer(MedicalCrateRenderer.class);
		registerTileRenderer(RepairCrateRenderer.class);
		registerTileRenderer(ChemicalDispenserRenderer.class);

		registerTileRenderer(InserterRenderer.class);
		registerTileRenderer(AdvancedInserterRenderer.class);
		registerTileRenderer(FluidInserterRenderer.class);
		registerTileRenderer(AdvancedFluidInserterRenderer.class);

		//TODO: 29.12.2023 data devices (0.4.0)
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTimedBuffer.class, new TimedBufferRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRedstoneBuffer.class, new RedstoneBufferRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySmallDataBuffer.class, new SmallDataBufferRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDataMerger.class, new DataMergerRenderer());
		registerTileRenderer(DataDebuggerRenderer.class);

		//TODO: 29.12.2023 latex collector renderer
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLatexCollector.class, new LatexCollectorRenderer());

		//Decorations
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMineSign.class, new MineSignRenderer());


		//Tools
		IIContent.itemTripodPeriscope.setTileEntityItemStackRenderer(TripodPeriscopeRenderer.instance);
		IIContent.itemMortar.setTileEntityItemStackRenderer(MortarRenderer.instance);
		IIContent.itemMineDetector.setTileEntityItemStackRenderer(MineDetectorRenderer.instance);

		//Mines and explosives
		registerTileRenderer(TripmineRenderer.class, IIContent.blockTripmine);
		registerTileRenderer(TellermineRenderer.class, IIContent.blockTellermine);
		registerTileRenderer(RadioExplosivesRenderer.class, IIContent.blockRadioExplosives);
		registerTEISR(NavalMineItemstackRenderer.class, IIContent.itemNavalMine);

		//Skycrate multiblocks renderers
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkyCrateStation.class, new SkyCrateStationRenderer().subscribeToList("multiblock/skycrate_station"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkyCartStation.class, new SkyCartStationRenderer().subscribeToList("multiblock/skycart_station"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkyCratePost.class, new SkyCratePostRenderer().subscribeToList("multiblock/skycrate_post"));

		//Data multiblocks renderers
		registerTileRenderer(RadioStationRenderer.class);

		registerTileRenderer(DataInputMachineRenderer.class);
		registerTileRenderer(ArithmeticLogicMachineRenderer.class);
		registerTileRenderer(PrintingPressRenderer.class);
		registerTileRenderer(BallisticComputerRenderer.class);

		//Logistics multiblocks renderers
		registerTileRenderer(PackerRenderer.class);
		registerTileRenderer(ScanningConveyorRenderer.class);

		//Production multiblocks renderers
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChemicalBath.class, new ChemicalBathRenderer().subscribeToList("multiblock/chemical_bath"));
		registerTileRenderer(ElectrolyzerRenderer.class);
		registerTileRenderer(ChemicalPainterRenderer.class);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrecisionAssembler.class, new PrecisionAssemblerRenderer().subscribeToList("multiblock/precision_assembler"));
		registerTileRenderer(FillerRenderer.class);

		//Ammunition production multiblocks renderers
		registerTileRenderer(HeavyAmmunitionAssemblerRenderer.class);
		registerTileRenderer(AmmunitionAssemblerRenderer.class);
		registerTileRenderer(ProjectileWorkshopRenderer.class);


		//Warfare multiblocks renderers
		registerTileRenderer(ArtilleryHowitzerRenderer.class);
		registerTileRenderer(EmplacementRenderer.class);
		registerTileRenderer(FlagpoleRenderer.class);
		registerTileRenderer(RadarRenderer.class);

		//Emplacement weapon renderers
		new EWRMachinegun();
		new EWRHeavyChemthrower();
		new EWRCPDS();
		new EWRGuidedMissileLauncher();
		new EWRAutocannon();
		new EWRHeavyRailgun();
		new EWRInfraredObserver();
		new EWRTeslaCoil();
		new EWRLightHowitzer();
		new EWRMortar();
		new EWRSearchlight();
		new EWRSpotlightTower();
		new EWRRocketLauncher();

		//Vehicle multiblocks renderers
		registerTileRenderer(FuelStationRenderer.class);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVehicleWorkshop.class, new VehicleWorkshopRenderer().subscribeToList("multiblock/vehicle_workshop"));


		//Rubber processing machines renderers
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVulcanizer.class, new VulcanizerRenderer().subscribeToList("multiblock/vulcanizer"));
		registerTileRenderer(CoagulatorRenderer.class);


		//Gate renderers
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoodenFenceGate.class, new FenceGateRenderer<>("multiblock/wooden_gate"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoodenChainFenceGate.class, new FenceGateRenderer<>("multiblock/wooden_chain_gate"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteelFenceGate.class, new FenceGateRenderer<>("multiblock/steel_gate"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteelChainFenceGate.class, new FenceGateRenderer<>("multiblock/steel_chain_gate"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAluminiumFenceGate.class, new FenceGateRenderer<>("multiblock/aluminium_gate"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAluminiumChainFenceGate.class, new FenceGateRenderer<>("multiblock/aluminium_chain_gate"));

		//GUIs (auto texture registering)
		IIGUI.initClientGUIs();

		//Compat
		IICompatModule.doModulesClientPreInit();
	}

	private <I extends Item> void registerItemRenderer(I item, IIItemRendererAMT<I> renderer)
	{
		item.setTileEntityItemStackRenderer(renderer);
		RegisteredItemRenderer annotation = IIUtils.getAnnotation(RegisteredItemRenderer.class, renderer);
		if(annotation!=null)
			renderer.subscribeToList(annotation.name());
	}

	private <T extends Entity> void registerEntityRenderer(Class<T> entityClass, IRenderFactory<? super T> renderFactory)
	{
		RenderingRegistry.registerEntityRenderingHandler(entityClass, renderFactory);
		Render<? super T> temp = renderFactory.createRenderFor(null);
		//Add a temporary registration for loading textures
		if(temp instanceof IReloadableModelContainer)
			IIModelRegistry.INSTANCE.addTemporaryModel(((IReloadableModelContainer<?>)temp));
	}

	@SubscribeEvent
	public void textureStichPre(Pre event)
	{
		//Bullets
		for(Item item : IIContent.ITEMS)
			if(item instanceof IIIItemTextureOverride)
				((IIIItemTextureOverride)item).registerSprites(event.getMap());

		for(Entry<String, Shrapnel> s : ShrapnelHandler.registry.entrySet())
			ApiUtils.getRegisterSprite(event.getMap(), s.getValue().texture.replace("textures/", ""));
		for(DrillHeads perm : DrillHeads.values())
			perm.sprite = ApiUtils.getRegisterSprite(event.getMap(), perm.texture);

		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/data_connector_feedtrough");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/data_connector");
		//Conveyors

		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubber.texture_on);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubber.texture_off);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberSplitter.texture_on);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberSplitter.texture_off);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberVertical.texture_on);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberVertical.texture_off);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberDropper.texture_on);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberDropper.texture_off);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberExtract.texture_casing);

		//Rest of models
		IIModelRegistry.INSTANCE.registerSprites(event.getMap());

		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/metal_device/inserter/inserter_gray");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/metal_device/inserter/inserter_dim");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/metal_device/inserter/inserter");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/metal_device/inserter/advanced_inserter");

		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/metal_device/crate_inserter_upgrade");

		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/metal_device/inserter/tool_green");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/metal_device/inserter/tool_red");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/metal_device/inserter/tool_dim");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/metal_device/inserter/tool_gray");

		//Universal Circuit Textures
		for(CircuitTypes value : CircuitTypes.values())
			ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/multiblock/circuits/circuit_"+value.getName());

		//Data Types
		IIDataTypeUtils.metaTypesByClass.values().stream()
				.map(TypeMetaInfo::getTextureLocation)
				.forEach(res -> ApiUtils.getRegisterSprite(event.getMap(), res));
		ApiUtils.getRegisterSprite(event.getMap(), IIReference.RES_CONTEXT_DATA_IN);
		ApiUtils.getRegisterSprite(event.getMap(), IIReference.RES_CONTEXT_DATA_OUT);
		ApiUtils.getRegisterSprite(event.getMap(), IIReference.RES_CONTEXT_DATA_CALLBACK);
		ApiUtils.getRegisterSprite(event.getMap(), IIReference.RES_CONTEXT_DATA_EVENT);

		//Upgrade icons
		for(Upgrade upgrade : Upgrade.getAllUpgrades())
			ApiUtils.getRegisterSprite(event.getMap(), upgrade.getIcon());

		//GUIs
		DecoTextures.registerAllTextures(event.getMap());

		//Recolored textures
		TextureRecoloringRegistry.onTextureStitch(event);

	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);

		ClientEventHandler handler = new ClientEventHandler();
		MinecraftForge.EVENT_BUS.register(handler);
		((IReloadableResourceManager)ClientUtils.mc().getResourceManager()).registerReloadListener(handler);

		//Register fonts
		IIClientUtils.fontRegular = new IIFontRenderer(new ResourceLocation("textures/font/ascii.png"));
		IIClientUtils.fontEngineerTimes = new IIFontRendererCustomGlyphs(new ResourceLocation(ImmersiveIntelligence.MODID, "textures/font/engineer_times.png"));
		IIClientUtils.fontNormung = new IIFontRendererCustomGlyphs(new ResourceLocation(ImmersiveIntelligence.MODID, "textures/font/normung.png"));
		IIClientUtils.fontKaiser = new IIFontRendererCustomGlyphs(new ResourceLocation(ImmersiveIntelligence.MODID, "textures/font/kaiser_fraktur.png"));
		IIClientUtils.fontTinkerer = new IIFontRendererCustomGlyphs(new ResourceLocation(ImmersiveIntelligence.MODID, "textures/font/tinkerer.png"));

		//Register Keybindings
		keybindManualReload = new IIKeybind("manualReload", Keyboard.KEY_R, IIKeybind.CATEGORY_GAMEPLAY)
				.withContext(KeyConflictContext.IN_GAME).register();

		keybindArmorHelmet = new IIKeybind("armorHelmet", Keyboard.KEY_V, IIKeybind.CATEGORY_GAMEPLAY)
				.withContext(KeyConflictContext.IN_GAME).register();
		keybindArmorExosuit = new IIKeybind("armorExosuit", Keyboard.KEY_G, IIKeybind.CATEGORY_GAMEPLAY)
				.withContext(KeyConflictContext.IN_GAME).register();
		keybindZoom = new IIKeybind("mgScope", Keyboard.KEY_Z, IIKeybind.CATEGORY_GAMEPLAY)
				.withContext(KeyConflictContext.IN_GAME).register();

		//Vehicle Keybinds
		keybindVehicleEngine = new IIKeybind("vehicle.engine.toggle", Keyboard.KEY_R, IIKeybind.CATEGORY_VEHICLES)
				.withContext(IIKeybind.VEHICLE_KEY_CONTEXT).register();
		keybindVehicleTowing = new IIKeybind("vehicle.tow.toggle", Keyboard.KEY_Z, IIKeybind.CATEGORY_VEHICLES)
				.withContext(IIKeybind.VEHICLE_KEY_CONTEXT).register();

		//Vehicle Gearbox Keybinds
		keybindVehicleClutch = new IIKeybind("vehicle.clutch", Keyboard.KEY_C, IIKeybind.CATEGORY_VEHICLES)
				.withContext(IIKeybind.VEHICLE_KEY_CONTEXT).register();
		keybindVehicleGearUp = new IIKeybind("vehicle.gear.up", Keyboard.KEY_Y, IIKeybind.CATEGORY_VEHICLES)
				.withContext(IIKeybind.VEHICLE_KEY_CONTEXT).register();
		keybindVehicleGearDown = new IIKeybind("vehicle.gear.down", Keyboard.KEY_H, IIKeybind.CATEGORY_VEHICLES)
				.withContext(IIKeybind.VEHICLE_KEY_CONTEXT).register();
		keybindVehicleReductionSwitch = new IIKeybind("vehicle.gear2.toggle", Keyboard.KEY_G, IIKeybind.CATEGORY_VEHICLES)
				.withContext(IIKeybind.VEHICLE_KEY_CONTEXT).register();

		//Register shaders
		ShaderUtil.init();

		//Register colored blocks
		for(Block block : IIContent.BLOCKS)
			if(block instanceof IColouredBlock&&((IColouredBlock)block).hasCustomBlockColours())
				ClientUtils.mc().getBlockColors().registerBlockColorHandler(IEDefaultColourHandlers.INSTANCE, block);
		for(Item item : IIContent.ITEMS)
			if(item instanceof IColouredItem&&((IColouredItem)item).hasCustomItemColours())
				ClientUtils.mc().getItemColors().registerItemColorHandler(IEDefaultColourHandlers.INSTANCE, item);

		//Compat
		IICompatModule.doModulesClientInit();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);

		//Render Layers
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		RenderPlayer render = skinMap.get("default");
		render.addLayer(new IIBipedWearableLayerRenderer());
		render = skinMap.get("slim");
		render.addLayer(new IIBipedWearableLayerRenderer());

		//Load Manual Pages
		IILogger.info("Registering II Manual Pages.");
		reloadManual();

		//TESR Itemstacks
		TEISRRegistryQueue.forEach(this::registerTEISR);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), MetalMultiblocks1.VULCANIZER.getMeta(), TileEntityVulcanizer.class);

		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalDevice), IIBlockTypes_MetalDevice.TIMED_BUFFER.getMeta(), TileEntityTimedBuffer.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalDevice), IIBlockTypes_MetalDevice.REDSTONE_BUFFER.getMeta(), TileEntityRedstoneBuffer.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalDevice), IIBlockTypes_MetalDevice.SMALL_DATA_BUFFER.getMeta(), TileEntitySmallDataBuffer.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalDevice), IIBlockTypes_MetalDevice.DATA_MERGER.getMeta(), TileEntityDataMerger.class);

		//Load Models
		reloadModels();
		IIMultiblockRecipe.loadAllClientSideContent();
		IICompatModule.doModulesClientPostInit();
		if(Factions.enableFactions)
			MinecraftForge.EVENT_BUS.register(DiplomacyHandler.getInstance(true));
	}

	private <T extends TileEntity> void registerTileRenderer(Class<? extends TileEntitySpecialRenderer<T>> clazz)
	{
		registerTileRenderer(clazz, null);
	}

	@SuppressWarnings("unchecked")
	private <T extends TileEntity> void registerTileRenderer(Class<? extends TileEntitySpecialRenderer<T>> clazz, @Nullable Block teisrBlock)
	{
		RegisteredTileRenderer[] annotations = clazz.getAnnotationsByType(RegisteredTileRenderer.class);
		for(RegisteredTileRenderer rt : annotations)
			try
			{
				//Create a new instance of the tile renderer
				TileEntitySpecialRenderer<T> tileRenderer = clazz.newInstance();
				ClientRegistry.bindTileEntitySpecialRenderer(((Class<T>)rt.clazz()), tileRenderer);

				//Register the tile renderer for auto reloading
				if(tileRenderer instanceof IReloadableModelContainer<?>)
					((IReloadableModelContainer<?>)tileRenderer).subscribeToList(rt.name());

				//Register the item renderer (if applicable)
				if(teisrBlock!=null&&rt.teisrClazz()!=TileEntityItemStackRenderer.class)
					TEISRRegistryQueue.put(rt.teisrClazz(), teisrBlock);

			} catch(InstantiationException|IllegalAccessException e)
			{
				IILogger.info("Failed to register TileEntitySpecialRenderer: "+clazz.getName());
			}
	}


	private void registerTEISR(Class<? extends TileEntityItemStackRenderer> rendererClass, Block item)
	{
		registerTEISR(rendererClass, Item.getItemFromBlock(item));
	}

	private void registerTEISR(Class<? extends TileEntityItemStackRenderer> rendererClass, Item item)
	{
		try
		{
			item.setTileEntityItemStackRenderer(rendererClass.newInstance());
		} catch(InstantiationException|IllegalAccessException e)
		{
			IILogger.error("Failed to register TileEntityItemStackRenderer for "+rendererClass.getName());
		}
	}

	@Override
	public void reloadModels()
	{
		IIModelRegistry.INSTANCE.reloadRegisteredModels();
	}

	@Override
	public void reloadManual()
	{
		IIManualCategory.cleanFolderEntries();
		IIManualCategoryData.INSTANCE.addPages();
		IIManualCategoryLogistics.INSTANCE.addPages();
		IIManualCategoryWarfare.INSTANCE.addPages();
		IIManualCategoryMotorworks.INSTANCE.addPages();
		IIManualCategoryIntelligence.INSTANCE.addPages();
		IIManualCategoryOther.INSTANCE.addPages();
	}

	@Override
	public void onMechanicalConnectorRemoved(Connection connection)
	{
		IIModelRegistry.INSTANCE.removeMotorBeltConnectionModel(connection);
	}

	@Override
	public void reloadParticles()
	{
		super.reloadParticles();
		ParticleSystem.reload();
	}

	//--- Stored GUI Data ---//

	/**
	 * @return The stored GUI data, or an empty one if none was stored previously
	 */
	@Nonnull
	public EasyNBT getStoredGuiData()
	{
		return storedGuiData;
	}

	/**
	 * @return A new NBT tag compound to store GUI data in
	 */
	public EasyNBT setStoredGuiData()
	{
		return this.storedGuiData = EasyNBT.newNBT();
	}
}
