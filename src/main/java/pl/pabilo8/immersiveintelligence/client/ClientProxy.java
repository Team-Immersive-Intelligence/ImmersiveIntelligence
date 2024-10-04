package pl.pabilo8.immersiveintelligence.client;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.energy.wires.WireApi;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.IEDefaultColourHandlers;
import blusunrize.immersiveengineering.client.models.obj.IEOBJLoader;
import blusunrize.immersiveengineering.client.render.EntityRenderNone;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IColouredBlock;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.Shrapnel;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.utils.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticles;
import pl.pabilo8.immersiveintelligence.client.gui.block.GuiUpgrade;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualCategory;
import pl.pabilo8.immersiveintelligence.client.manual.categories.*;
import pl.pabilo8.immersiveintelligence.client.model.IIModelRegistry;
import pl.pabilo8.immersiveintelligence.client.model.builtin.FluidStateMapper;
import pl.pabilo8.immersiveintelligence.client.model.item.ModelMeasuringCup;
import pl.pabilo8.immersiveintelligence.client.model.item.ModelMeasuringCup.MeasuringCupModelLoader;
import pl.pabilo8.immersiveintelligence.client.render.*;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.render.ammunition.*;
import pl.pabilo8.immersiveintelligence.client.render.ammunition.NavalMineRenderer.NavalMineItemstackRenderer;
import pl.pabilo8.immersiveintelligence.client.render.hans.HansRenderer;
import pl.pabilo8.immersiveintelligence.client.render.inserter.AdvancedInserterRenderer;
import pl.pabilo8.immersiveintelligence.client.render.inserter.InserterRenderer;
import pl.pabilo8.immersiveintelligence.client.render.item.*;
import pl.pabilo8.immersiveintelligence.client.render.mechanical_device.BeltModelStorage;
import pl.pabilo8.immersiveintelligence.client.render.mechanical_device.MechanicalPumpRenderer;
import pl.pabilo8.immersiveintelligence.client.render.mechanical_device.WheelRenderer;
import pl.pabilo8.immersiveintelligence.client.render.metal_device.*;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.*;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.wooden.*;
import pl.pabilo8.immersiveintelligence.client.render.vehicle.DroneRenderer;
import pl.pabilo8.immersiveintelligence.client.render.vehicle.FieldHowitzerRenderer;
import pl.pabilo8.immersiveintelligence.client.render.vehicle.MortarRenderer;
import pl.pabilo8.immersiveintelligence.client.render.vehicle.MotorbikeRenderer;
import pl.pabilo8.immersiveintelligence.client.util.IICustomStateMapper;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIItemRendererAMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIItemRendererAMT.RegisteredItemRenderer;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRenderer;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRendererCustomGlyphs;
import pl.pabilo8.immersiveintelligence.common.*;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMerger;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityRedstoneBuffer;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntitySmallDataBuffer;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityTimedBuffer;
import pl.pabilo8.immersiveintelligence.common.block.fortification.tileentity.TileEntityMineSign;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityFluidInserter;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityLatexCollector;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.conveyors.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockAluminiumChainFenceGate.TileEntityAluminiumChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockAluminiumFenceGate.TileEntityAluminiumFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockSteelChainFenceGate.TileEntitySteelChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockSteelFenceGate.TileEntitySteelFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockWoodenChainFenceGate.TileEntityWoodenChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockWoodenFenceGate.TileEntityWoodenFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
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
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleSeat;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.gun.EntityFieldHowitzer;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIINavalMine;
import pl.pabilo8.immersiveintelligence.common.item.ammo.gun.ItemIIAmmoRevolver;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIDrillHead.DrillHeads;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIFluid;
import pl.pabilo8.immersiveintelligence.common.util.block.IIIStateMappings;
import pl.pabilo8.immersiveintelligence.common.util.block.IIIStateMappings.DummyEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IIIItemTextureOverride;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author Pabilo8
 * @since 2019-05-07
 */
@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ImmersiveIntelligence.MODID)
public class ClientProxy extends CommonProxy
{
	public static KeyBinding keybind_manualReload, keybind_armorHelmet, keybind_armorExosuit, keybind_zoom, keybind_motorbikeEngine, keybind_motorbikeTowing;
	public NBTTagCompound storedGuiData = new NBTTagCompound();

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

		if(ID==IIGuiList.GUI_UPGRADE.ordinal()&&te instanceof IUpgradableMachine)
		{
			TileEntity upgradeMaster = ((IUpgradableMachine)te).getUpgradeMaster();
			if(upgradeMaster!=null)
				return new GuiUpgrade(player, (TileEntity & IUpgradableMachine)upgradeMaster);
		}

		GuiScreen gui = null;
		if(IIGuiList.values().length > ID)
		{
			IIGuiList guiBuilder = IIGuiList.values()[ID];
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
	public void preInit()
	{
		//long live .obj models! ^^
		super.preInit();
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(IIModelRegistry.INSTANCE);
		OBJLoader.INSTANCE.addDomain(ImmersiveIntelligence.MODID);
		IEOBJLoader.instance.addDomain(ImmersiveIntelligence.MODID);

		//Load particle models
		IIParticles.preInit();
		IIParticles.init();

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
		registerEntityRenderer(EntityDrone.class, DroneRenderer::new);
		registerEntityRenderer(EntityFieldHowitzer.class, FieldHowitzerRenderer::new);
		registerEntityRenderer(EntityTripodPeriscope.class, TripodPeriscopeRenderer::new);
		registerEntityRenderer(EntityMortar.class, MortarRenderer::new);
		registerEntityRenderer(EntityHMXDynamitePrimed.class, HMXDynamitePrimedRenderer::new);
		//Thanks Blu!
		registerEntityRenderer(EntityCamera.class, EntityRenderNone::new);
		registerEntityRenderer(EntitySkycrateInternal.class, EntityRenderNone::new);
		registerEntityRenderer(EntityVehicleSeat.class, EntityRenderNone::new);


		registerEntityRenderer(EntityAtomicBoom.class, AtomicBoomRenderer::new);
		registerEntityRenderer(EntityGasCloud.class, EntityRenderNone::new);
		registerEntityRenderer(EntityFlare.class, EntityRenderNone::new);

		registerEntityRenderer(EntityHans.class, HansRenderer::new);
		registerEntityRenderer(EntityParachute.class, ParachuteRenderer::new);
		registerEntityRenderer(EntityEmplacementWeapon.class, EntityRenderNone::new);
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

		for(IAmmoTypeItem bullet : AmmoRegistry.getAllAmmoItems())
		{
			if(bullet instanceof ItemIINavalMine)
				continue;
			if(bullet instanceof ItemIIAmmoBase)
				IIModelRegistry.INSTANCE.registerCustomItemModel(((ItemIIAmmoBase)bullet));
			else if(bullet instanceof ItemIIAmmoRevolver)
				IIModelRegistry.INSTANCE.registerCustomItemModel((ItemIIAmmoRevolver)bullet, ImmersiveIntelligence.MODID, ItemIIAmmoRevolver.BULLET, ItemIIAmmoRevolver.CORE);
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
		//TODO: 29.12.2023 fluid inserter
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidInserter.class, new FluidInserterRenderer().subscribeToList("device/inserter/fluid_inserter"));

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
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRadioStation.class, new RadioStationRenderer().subscribeToList("multiblock/radio_station"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDataInputMachine.class, new DataInputMachineRenderer().subscribeToList("multiblock/data_input_machine"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityArithmeticLogicMachine.class, new ArithmeticLogicMachineRenderer().subscribeToList("multiblock/arithmetic_logic_machine"));
		registerTileRenderer(PrintingPressRenderer.class);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBallisticComputer.class, new BallisticComputerRenderer().subscribeToList("multiblock/ballistic_computer"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRedstoneInterface.class, new RedstoneInterfaceRenderer().subscribeToList("multiblock/redstone_data_interface"));

		//Logistics multiblocks renderers
		registerTileRenderer(PackerRenderer.class);
		registerTileRenderer(ScanningConveyorRenderer.class);

		//Production multiblocks renderers
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChemicalBath.class, new ChemicalBathRenderer().subscribeToList("multiblock/chemical_bath"));

		registerTileRenderer(ElectrolyzerRenderer.class);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrecisionAssembler.class, new PrecisionAssemblerRenderer().subscribeToList("multiblock/precision_assembler"));

		registerTileRenderer(FillerRenderer.class);

		//Ammunition production multiblocks renderers
		registerTileRenderer(HeavyAmmunitionAssemblerRenderer.class);
		registerTileRenderer(AmmunitionAssemblerRenderer.class);
		registerTileRenderer(ProjectileWorkshopRenderer.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChemicalPainter.class, new ChemicalPainterRenderer().subscribeToList("multiblock/chemical_painter"));


		//Warfare multiblocks renderers
		registerTileRenderer(ArtilleryHowitzerRenderer.class);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEmplacement.class, new EmplacementRenderer().subscribeToList("multiblock/emplacement"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFlagpole.class, new FlagpoleRenderer().subscribeToList("multiblock/flagpole"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRadar.class, new RadarRenderer().subscribeToList("multiblock/radar"));


		//Vehicle multiblocks renderers
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFuelStation.class, new FuelStationRenderer().subscribeToList("multiblock/fuel_station"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVehicleWorkshop.class, new VehicleWorkshopRenderer().subscribeToList("multiblock/vehicle_workshop"));


		//Rubber processing machines renderers
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVulcanizer.class, new VulcanizerRenderer().subscribeToList("multiblock/vulcanizer"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCoagulator.class, new CoagulatorRenderer().subscribeToList("multiblock/coagulator"));


		//Gate renderers
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoodenFenceGate.class, new FenceGateRenderer<>("multiblock/wooden_gate"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoodenChainFenceGate.class, new FenceGateRenderer<>("multiblock/wooden_chain_gate"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteelFenceGate.class, new FenceGateRenderer<>("multiblock/steel_gate"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteelChainFenceGate.class, new FenceGateRenderer<>("multiblock/steel_chain_gate"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAluminiumFenceGate.class, new FenceGateRenderer<>("multiblock/aluminium_gate"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAluminiumChainFenceGate.class, new FenceGateRenderer<>("multiblock/aluminium_chain_gate"));

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
		if(temp instanceof IReloadableModelContainer)
			IIModelRegistry.INSTANCE.addTemporaryModel(((IReloadableModelContainer<?>)temp));
	}

	@SubscribeEvent
	public void textureStichPre(TextureStitchEvent.Pre event)
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
	}

	@Override
	public void init()
	{
		super.init();
		IIGuiList.initClientGUIs();

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
		IKeyConflictContext passenger_action = new IKeyConflictContext()
		{
			@Override
			public boolean isActive()
			{
				return KeyConflictContext.IN_GAME.isActive();
			}

			@Override
			public boolean conflicts(IKeyConflictContext other)
			{
				return other==KeyConflictContext.IN_GAME&&other!=this;
			}
		};

		keybind_manualReload = new KeyBinding("key."+ImmersiveIntelligence.MODID+".manualReload", Keyboard.KEY_R, "key.categories.gameplay");
		keybind_manualReload.setKeyConflictContext(passenger_action);

		keybind_armorHelmet = new KeyBinding("key."+ImmersiveIntelligence.MODID+".armorHelmet", Keyboard.KEY_V, "key.categories.gameplay");
		keybind_armorExosuit = new KeyBinding("key."+ImmersiveIntelligence.MODID+".armorExosuit", Keyboard.KEY_G, "key.categories.gameplay");
		keybind_zoom = new KeyBinding("key."+ImmersiveIntelligence.MODID+".mgScope", Keyboard.KEY_Z, "key.categories.gameplay");
		keybind_zoom.setKeyConflictContext(passenger_action);

		keybind_motorbikeEngine = new KeyBinding("key."+ImmersiveIntelligence.MODID+".motorbikeEngine", Keyboard.KEY_R, "key.categories.gameplay");
		keybind_motorbikeEngine.setKeyConflictContext(passenger_action);
		keybind_motorbikeTowing = new KeyBinding("key."+ImmersiveIntelligence.MODID+".motorbikeTowing", Keyboard.KEY_Z, "key.categories.gameplay");
		keybind_motorbikeTowing.setKeyConflictContext(passenger_action);

		ClientRegistry.registerKeyBinding(keybind_manualReload);
		ClientRegistry.registerKeyBinding(keybind_zoom);
		ClientRegistry.registerKeyBinding(keybind_motorbikeEngine);
		ClientRegistry.registerKeyBinding(keybind_motorbikeTowing);

		ClientRegistry.registerKeyBinding(keybind_armorHelmet);
		ClientRegistry.registerKeyBinding(keybind_armorExosuit);

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
	public void postInit()
	{
		super.postInit();

		//Render Layers
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		RenderPlayer render = skinMap.get("default");
		render.addLayer(new IIBipedLayerRenderer());
		render = skinMap.get("slim");
		render.addLayer(new IIBipedLayerRenderer());

		//Load Manual Pages
		IILogger.info("Registering II Manual Pages.");
		reloadManual();

		//TESR Itemstacks
		TEISRRegistryQueue.forEach(this::registerTEISR);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock0), MetalMultiblocks0.RADIO_STATION.getMeta(), TileEntityRadioStation.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock0), MetalMultiblocks0.CHEMICAL_BATH.getMeta(), TileEntityChemicalBath.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock0), MetalMultiblocks0.PRECISION_ASSEMBLER.getMeta(), TileEntityPrecisionAssembler.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), MetalMultiblocks1.CHEMICAL_PAINTER.getMeta(), TileEntityChemicalPainter.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), MetalMultiblocks1.EMPLACEMENT.getMeta(), TileEntityEmplacement.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), MetalMultiblocks1.FLAGPOLE.getMeta(), TileEntityFlagpole.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), MetalMultiblocks1.RADAR.getMeta(), TileEntityRadar.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), MetalMultiblocks1.FUEL_STATION.getMeta(), TileEntityFuelStation.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), MetalMultiblocks1.VEHICLE_WORKSHOP.getMeta(), TileEntityVehicleWorkshop.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), MetalMultiblocks1.VULCANIZER.getMeta(), TileEntityVulcanizer.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), MetalMultiblocks1.COAGULATOR.getMeta(), TileEntityCoagulator.class);

		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockDataConnector), BlockIIDataDevice.IIBlockTypes_Connector.FLUID_INSERTER.getMeta(), TileEntityFluidInserter.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalDevice), BlockIIMetalDevice.IIBlockTypes_MetalDevice.TIMED_BUFFER.getMeta(), TileEntityTimedBuffer.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalDevice), BlockIIMetalDevice.IIBlockTypes_MetalDevice.REDSTONE_BUFFER.getMeta(), TileEntityRedstoneBuffer.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalDevice), BlockIIMetalDevice.IIBlockTypes_MetalDevice.SMALL_DATA_BUFFER.getMeta(), TileEntitySmallDataBuffer.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalDevice), BlockIIMetalDevice.IIBlockTypes_MetalDevice.DATA_MERGER.getMeta(), TileEntityDataMerger.class);

		//Load Models
		reloadModels();
		IICompatModule.doModulesClientPostInit();
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
		//IIManualCategoryOther.INSTANCE.addPages();
	}
}