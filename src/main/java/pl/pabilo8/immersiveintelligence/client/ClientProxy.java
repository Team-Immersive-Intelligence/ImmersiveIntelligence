package pl.pabilo8.immersiveintelligence.client;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.energy.wires.WireApi;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.IECustomStateMapper;
import blusunrize.immersiveengineering.client.IEDefaultColourHandlers;
import blusunrize.immersiveengineering.client.models.obj.IEOBJLoader;
import blusunrize.immersiveengineering.client.render.EntityRenderNone;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IColouredBlock;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IIEMetaBlock;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IColouredItem;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IGuiItem;
import blusunrize.immersiveengineering.common.items.ItemIEBase;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.settings.KeyBinding;
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
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.Shrapnel;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.client.fx.nuke.ParticleAtomBase;
import pl.pabilo8.immersiveintelligence.client.fx.particles.ParticleGasCloud;
import pl.pabilo8.immersiveintelligence.client.fx.particles.ParticleGunfire;
import pl.pabilo8.immersiveintelligence.client.gui.block.GuiUpgrade;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualCategory;
import pl.pabilo8.immersiveintelligence.client.manual.categories.*;
import pl.pabilo8.immersiveintelligence.client.model.IIModelRegistry;
import pl.pabilo8.immersiveintelligence.client.model.item.ModelMeasuringCup;
import pl.pabilo8.immersiveintelligence.client.model.item.ModelMeasuringCup.MeasuringCupModelLoader;
import pl.pabilo8.immersiveintelligence.client.model.misc.FluidStateMapper;
import pl.pabilo8.immersiveintelligence.client.render.*;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.render.RadioExplosivesRenderer.RadioExplosivesItemStackRenderer;
import pl.pabilo8.immersiveintelligence.client.render.TellermineRenderer.TellermineItemStackRenderer;
import pl.pabilo8.immersiveintelligence.client.render.TripmineRenderer.TripmineItemStackRenderer;
import pl.pabilo8.immersiveintelligence.client.render.hans.HansRenderer;
import pl.pabilo8.immersiveintelligence.client.render.inserter.AdvancedInserterRenderer;
import pl.pabilo8.immersiveintelligence.client.render.inserter.InserterRenderer;
import pl.pabilo8.immersiveintelligence.client.render.item.*;
import pl.pabilo8.immersiveintelligence.client.render.mechanical_device.MechanicalPumpRenderer;
import pl.pabilo8.immersiveintelligence.client.render.mechanical_device.WheelRenderer;
import pl.pabilo8.immersiveintelligence.client.render.metal_device.*;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.*;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.wooden.*;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRenderer;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRendererCustomGlyphs;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.*;
import pl.pabilo8.immersiveintelligence.common.block.fortification.tileentity.TileEntityMineSign;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityChemicalDispenser;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityFluidInserter;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityLatexCollector;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.conveyors.*;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityAdvancedInserter;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityInserter;
import pl.pabilo8.immersiveintelligence.common.block.mines.tileentity.TileEntityRadioExplosives;
import pl.pabilo8.immersiveintelligence.common.block.mines.tileentity.TileEntityTellermine;
import pl.pabilo8.immersiveintelligence.common.block.mines.tileentity.TileEntityTripMine;
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
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCartStation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCratePost;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIMechanicalConnector;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityMechanicalConnectable;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityMechanicalWheel;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.entity.*;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.*;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityDrone;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityFieldHowitzer;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityMotorbike;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleSeat;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoRevolver;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIINavalMine;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIDrillHead.DrillHeads;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIFluid;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;

import java.util.Locale;
import java.util.Map;
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
	public static MechanicalConnectorRenderer mech_con_renderer;
	public NBTTagCompound storedGuiData = new NBTTagCompound();

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

		//TODO: 06.09.2022 rework
		for(Block block : IIContent.BLOCKS)
		{
			final ResourceLocation loc = Block.REGISTRY.getNameForObject(block);
			Item blockItem = Item.getItemFromBlock(block);
			if(block instanceof BlockIIFluid)
			{
				mapFluidState(block, ((BlockIIFluid)block).getFluid());
			}
			else if(block instanceof IIEMetaBlock)
			{
				IIEMetaBlock ieMetaBlock = (IIEMetaBlock)block;
				if(ieMetaBlock.useCustomStateMapper())
					ModelLoader.setCustomStateMapper(block, IECustomStateMapper.getStateMapper(ieMetaBlock));
				ModelLoader.setCustomMeshDefinition(blockItem, stack -> new ModelResourceLocation(loc, "inventory"));
				for(int meta = 0; meta < ieMetaBlock.getMetaEnums().length; meta++)
				{
					BlockIIBase<?> b = (BlockIIBase<?>)block;
					if(!b.tesrMap.isEmpty()&&b.tesrMap.containsKey(meta))
					{
						ModelLoader.setCustomModelResourceLocation(blockItem, meta, new ModelResourceLocation(new ResourceLocation(ImmersiveIntelligence.MODID, "itemblock/"+b.tesrMap.get(meta)), "inventory"));
					}
					else
					{
						String location = loc.toString();
						String prop = ieMetaBlock.appendPropertiesToState()?("inventory,"+ieMetaBlock.getMetaProperty().getName()+"="+ieMetaBlock.getMetaEnums()[meta].toString().toLowerCase(Locale.US)): null;

						String custom = ieMetaBlock.getCustomStateMapping(meta, true);
						if(custom!=null)
							location += "_"+custom;

						try
						{
							ModelLoader.setCustomModelResourceLocation(blockItem, meta, new ModelResourceLocation(location, prop));
						} catch(NullPointerException npe)
						{
							throw new RuntimeException("WELP! apparently "+ieMetaBlock+" lacks an item!", npe);
						}
					}

				}
			}
			else
				ModelLoader.setCustomModelResourceLocation(blockItem, 0, new ModelResourceLocation(loc, "inventory"));
		}

		//measuring cup
		ModelLoaderRegistry.registerLoader(MeasuringCupModelLoader.INSTANCE);
		ModelLoader.setCustomMeshDefinition(IIContent.itemMeasuringCup, stack -> ModelMeasuringCup.MODEL.getLocation());
		ModelBakery.registerItemVariants(IIContent.itemMeasuringCup, ModelMeasuringCup.MODEL.getLocation());

		for(Item item : IIContent.ITEMS)
		{
			if(item instanceof ItemBlock)
				continue;
			if(item instanceof ItemIIBase)
			{
				ItemIIBase ieMetaItem = (ItemIIBase)item;
				if(ieMetaItem instanceof ItemIISubItemsBase)
				{
					for(IIItemEnum subItem : ((ItemIISubItemsBase<?>)ieMetaItem).getSubItems())
					{
						ResourceLocation loc = new ResourceLocation(ImmersiveIntelligence.MODID, ieMetaItem.itemName+"/"+subItem.getName());
						ModelBakery.registerItemVariants(ieMetaItem, loc);
						ModelLoader.setCustomModelResourceLocation(ieMetaItem, subItem.getMeta(), new ModelResourceLocation(loc, "inventory"));
					}
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

		return gui;
	}

	@Override
	public void preInit()
	{
		super.preInit();
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(IIModelRegistry.instance);
		OBJLoader.INSTANCE.addDomain(ImmersiveIntelligence.MODID);
		IEOBJLoader.instance.addDomain(ImmersiveIntelligence.MODID);

		//long live .obj models! ^^

		//Register entity renderers
		RenderingRegistry.registerEntityRenderingHandler(EntitySkyCrate.class, SkyCrateRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, BulletRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityNavalMine.class, NavalMineRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityNavalMineAnchor.class, NavalMineAnchorRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityShrapnel.class, ShrapnelRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityWhitePhosphorus.class, EntityRenderNone::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMachinegun.class, MachinegunRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMotorbike.class, MotorbikeRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDrone.class, DroneRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFieldHowitzer.class, FieldHowitzerRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityTripodPeriscope.class, TripodPeriscopeRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMortar.class, MortarRenderer::new);
		//Thanks Blu!
		RenderingRegistry.registerEntityRenderingHandler(EntityCamera.class, EntityRenderNone::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySkycrateInternal.class, EntityRenderNone::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityVehicleSeat.class, EntityRenderNone::new);

		RenderingRegistry.registerEntityRenderingHandler(EntityAtomicBoom.class, AtomicBoomRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityGasCloud.class, EntityRenderNone::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFlare.class, EntityRenderNone::new);

		RenderingRegistry.registerEntityRenderingHandler(EntityHans.class, HansRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityParachute.class, ParachuteRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityEmplacementWeapon.class, EntityRenderNone::new);


		IIContent.itemAssaultRifle.setTileEntityItemStackRenderer(new AssaultRifleRenderer().subscribeToList("assault_rifle"));
		IIContent.itemRifle.setTileEntityItemStackRenderer(new RifleRenderer().subscribeToList("rifle"));

		for(IAmmo bullet : AmmoRegistry.INSTANCE.registeredBulletItems.values())
		{
			if(bullet instanceof ItemIINavalMine)
				continue;
			if(bullet instanceof ItemIIAmmoBase)
				IIModelRegistry.instance.registerCustomItemModel(((ItemIIAmmoBase)bullet));
			else if(bullet instanceof ItemIIAmmoRevolver)
				IIModelRegistry.instance.registerCustomItemModel((ItemIIAmmoRevolver)bullet, ImmersiveIntelligence.MODID, ItemIIAmmoRevolver.BULLET, ItemIIAmmoRevolver.CORE);
		}

		IIModelRegistry.instance.registerCustomItemModel(IIContent.itemBulletMagazine);
		IIModelRegistry.instance.registerCustomItemModel(IIContent.itemBinoculars);
		IIModelRegistry.instance.registerCustomItemModel(IIContent.itemCasingPouch);

		IIContent.itemMotorBelt.setRenderModels();

		//Compat
		IICompatModule.doModulesClientPreInit();
	}

	@SubscribeEvent
	public void textureStichPre(TextureStitchEvent.Pre event)
	{
		//Bullets
		AmmoRegistry.INSTANCE.registeredBulletItems.forEach((key, value) -> value.registerSprites(event.getMap()));

		//Magazines
		for(int i = 0; i < 4; i++)
		{
			ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/bullets/magazines/common/bullet"+i);
			ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/bullets/magazines/common/paint"+i);
		}
		for(Magazines magazine : IIContent.itemBulletMagazine.getSubItems())
		{
			String name = magazine.getName();
			ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name);
			if(magazine.hasDisplayTexture)
				ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/bullets/magazines/"+name+"_disp");
		}

		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/binoculars/binoculars");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/binoculars/infrared_binoculars_off");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/binoculars/infrared_binoculars_on");

		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/casing_pouch/empty");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/casing_pouch/filled");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/casing_pouch/closed");

		for(Entry<String, Shrapnel> s : ShrapnelHandler.registry.entrySet())
			ApiUtils.getRegisterSprite(event.getMap(), s.getValue().texture.replace("textures/", ""));

		for(DrillHeads perm : DrillHeads.values())
			perm.sprite = ApiUtils.getRegisterSprite(event.getMap(), perm.texture);

		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/data_connector_feedtrough");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/data_connector");

		ParticleGasCloud.TEXTURE = ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":particle/gas");
		ParticleGunfire.TEXTURES = new TextureAtlasSprite[8];
		for(int i = 0; i < 8; i++)
			ParticleGunfire.TEXTURES[i] = ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":particle/gunshot"+i);
		ParticleAtomBase.TEXTURES = new TextureAtlasSprite[8];
		for(int i = 0; i < 8; i++)
			ParticleAtomBase.TEXTURES[i] = ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":particle/smoke"+i);

		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubber.texture_on);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubber.texture_off);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberSplitter.texture_on);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberSplitter.texture_off);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberVertical.texture_on);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberVertical.texture_off);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberDropper.texture_on);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberDropper.texture_off);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberExtract.texture_casing);

		// TODO: 20.03.2023 Update AMT models to use new mtl loading tech
		IIModelRegistry.instance.registerSprites(event.getMap());

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

		//Register coloured blocks
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

		//--- Add Manual Pages ---//
		IILogger.info("Registering II Manual Pages.");
		reloadManual();


		//Weapons (Items)
		IIContent.itemMachinegun.setTileEntityItemStackRenderer(MachinegunItemStackRenderer.instance);
		IIContent.itemSubmachinegun.setTileEntityItemStackRenderer(SubmachinegunItemStackRenderer.instance);
		ItemIIWeaponUpgrade.addUpgradesToRender();

		IIContent.itemLightEngineerHelmet.setTileEntityItemStackRenderer(LightEngineerArmorItemStackRenderer.instance);
		IIContent.itemLightEngineerChestplate.setTileEntityItemStackRenderer(LightEngineerArmorItemStackRenderer.instance);
		IIContent.itemLightEngineerLeggings.setTileEntityItemStackRenderer(LightEngineerArmorItemStackRenderer.instance);
		IIContent.itemLightEngineerBoots.setTileEntityItemStackRenderer(LightEngineerArmorItemStackRenderer.instance);

		//Render Layers
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		RenderPlayer render = skinMap.get("default");
		render.addLayer(new IIBipedLayerRenderer());
		render = skinMap.get("slim");
		render.addLayer(new IIBipedLayerRenderer());

		//Block / ItemStack rendering
		//TODO: 16.07.2023 automate it fully

		registerTileRenderer(AmmunitionCrateRenderer.class);
		registerTileRenderer(MedicalCrateRenderer.class);
		registerTileRenderer(RepairCrateRenderer.class);

		registerTileRenderer(InserterRenderer.class);
		registerTileRenderer(AdvancedInserterRenderer.class);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidInserter.class, new FluidInserterRenderer().subscribeToList("fluid_inserter"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTimedBuffer.class, new TimedBufferRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRedstoneBuffer.class, new RedstoneBufferRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySmallDataBuffer.class, new SmallDataBufferRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDataMerger.class, new DataMergerRenderer());

		registerTileRenderer(DataDebuggerRenderer.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLatexCollector.class, new LatexCollectorRenderer());

		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalDevice), BlockIIMetalDevice.IIBlockTypes_MetalDevice.TIMED_BUFFER.getMeta(), TileEntityTimedBuffer.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalDevice), BlockIIMetalDevice.IIBlockTypes_MetalDevice.REDSTONE_BUFFER.getMeta(), TileEntityRedstoneBuffer.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalDevice), BlockIIMetalDevice.IIBlockTypes_MetalDevice.SMALL_DATA_BUFFER.getMeta(), TileEntitySmallDataBuffer.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalDevice), BlockIIMetalDevice.IIBlockTypes_MetalDevice.DATA_MERGER.getMeta(), TileEntityDataMerger.class);


		registerTileRenderer(MechanicalPumpRenderer.class);
		//Data Connectors
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChemicalDispenser.class, new ChemicalDispenserRenderer().subscribeToList("chemical_dispenser"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMineSign.class, new MineSignRenderer());

		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockDataConnector), BlockIIDataDevice.IIBlockTypes_Connector.DATA_CONNECTOR.getMeta(), TileEntityDataConnector.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockDataConnector), BlockIIDataDevice.IIBlockTypes_Connector.DATA_RELAY.getMeta(), TileEntityDataRelay.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockDataConnector), BlockIIDataDevice.IIBlockTypes_Connector.DATA_DUPLEX_CONNECTOR.getMeta(), TileEntityDataCallbackConnector.class);

		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockDataConnector), BlockIIDataDevice.IIBlockTypes_Connector.INSERTER.getMeta(), TileEntityInserter.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockDataConnector), BlockIIDataDevice.IIBlockTypes_Connector.ADVANCED_INSERTER.getMeta(), TileEntityAdvancedInserter.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockDataConnector), BlockIIDataDevice.IIBlockTypes_Connector.FLUID_INSERTER.getMeta(), TileEntityFluidInserter.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockDataConnector), BlockIIDataDevice.IIBlockTypes_Connector.CHEMICAL_DISPENSER.getMeta(), TileEntityChemicalDispenser.class);
		//ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_data_connector), IIBlockTypes_Connector.INSERTER.getMeta(), TileEntityInserter.class);

		//Tools
		IIContent.itemTachometer.setTileEntityItemStackRenderer(TachometerItemStackRenderer.instance);
		IIContent.itemRadioTuner.setTileEntityItemStackRenderer(RadioTunerItemStackRenderer.instance);
		IIContent.itemTripodPeriscope.setTileEntityItemStackRenderer(TripodPeriscopeRenderer.instance);
		IIContent.itemMortar.setTileEntityItemStackRenderer(MortarRenderer.instance);
		IIContent.itemMineDetector.setTileEntityItemStackRenderer(MineDetectorRenderer.instance);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTripMine.class, new TripmineRenderer().subscribeToList("tripmine"));
		Item.getItemFromBlock(IIContent.blockTripmine).setTileEntityItemStackRenderer(new TripmineItemStackRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTellermine.class, new TellermineRenderer().subscribeToList("tellermine"));
		Item.getItemFromBlock(IIContent.blockTellermine).setTileEntityItemStackRenderer(new TellermineItemStackRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRadioExplosives.class, new RadioExplosivesRenderer().subscribeToList("radio_explosives"));
		Item.getItemFromBlock(IIContent.blockRadioExplosives).setTileEntityItemStackRenderer(new RadioExplosivesItemStackRenderer());
		IIContent.itemNavalMine.setTileEntityItemStackRenderer(NavalMineRenderer.instance);

		//Multiblocks
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkyCrateStation.class, new SkyCrateStationRenderer().subscribeToList("skycrate_station"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkyCartStation.class, new SkyCartStationRenderer().subscribeToList("skycart_station"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkyCratePost.class, new SkyCratePostRenderer().subscribeToList("skycrate_post"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRadioStation.class, new RadioStationRenderer().subscribeToList("radio_station"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock0), MetalMultiblocks0.RADIO_STATION.getMeta(), TileEntityRadioStation.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDataInputMachine.class, new DataInputMachineRenderer().subscribeToList("data_input_machine"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityArithmeticLogicMachine.class, new ArithmeticLogicMachineRenderer().subscribeToList("arithmetic_logic_machine"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrintingPress.class, new PrintingPressRenderer().subscribeToList("printing_press"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock0), MetalMultiblocks0.PRINTING_PRESS.getMeta(), TileEntityPrintingPress.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChemicalBath.class, new ChemicalBathRenderer().subscribeToList("chemical_bath"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock0), MetalMultiblocks0.CHEMICAL_BATH.getMeta(), TileEntityChemicalBath.class);

		registerTileRenderer(ElectrolyzerRenderer.class);

		//TODO: Fix misspeling during the Split, it could break stuff
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrecisionAssembler.class, new PrecisionAssemblerRenderer().subscribeToList("precision_assembler"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock0), MetalMultiblocks0.PRECISION_ASSEMBLER.getMeta(), TileEntityPrecisionAssembler.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAmmunitionWorkshop.class, new AmmunitionWorkshopRenderer().subscribeToList("ammunition_workshop"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), MetalMultiblocks1.AMMUNITION_WORKSHOP.getMeta(), TileEntityAmmunitionWorkshop.class);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityProjectileWorkshop.class, new ProjectileWorkshopRenderer().subscribeToList("projectile_workshop"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), MetalMultiblocks1.PROJECTILE_WORKSHOP.getMeta(), TileEntityProjectileWorkshop.class);

		registerTileRenderer(SawmillRenderer.class);
		registerTileRenderer(PackerRenderer.class);
		registerTileRenderer(ScanningConveyorRenderer.class);
		registerTileRenderer(ArtilleryHowitzerRenderer.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBallisticComputer.class, new BallisticComputerRenderer().subscribeToList("ballistic_computer"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRedstoneInterface.class, new RedstoneInterfaceRenderer().subscribeToList("redstone_data_interface"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChemicalPainter.class, new ChemicalPainterRenderer().subscribeToList("chemical_painter"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), MetalMultiblocks1.CHEMICAL_PAINTER.getMeta(), TileEntityChemicalPainter.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFiller.class, new FillerRenderer().subscribeToList("casing_filler"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEmplacement.class, new EmplacementRenderer().subscribeToList("emplacement"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), MetalMultiblocks1.EMPLACEMENT.getMeta(), TileEntityEmplacement.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRadar.class, new RadarRenderer().subscribeToList("radar"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), MetalMultiblocks1.RADAR.getMeta(), TileEntityRadar.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFlagpole.class, new FlagpoleRenderer().subscribeToList("flagpole"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), MetalMultiblocks1.FLAGPOLE.getMeta(), TileEntityFlagpole.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFuelStation.class, new FuelStationRenderer().subscribeToList("fuel_station"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), MetalMultiblocks1.FUEL_STATION.getMeta(), TileEntityFuelStation.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVehicleWorkshop.class, new VehicleWorkshopRenderer().subscribeToList("vehicle_workshop"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), MetalMultiblocks1.VEHICLE_WORKSHOP.getMeta(), TileEntityVehicleWorkshop.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVulcanizer.class, new VulcanizerRenderer().subscribeToList("vulcanizer"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), MetalMultiblocks1.VULCANIZER.getMeta(), TileEntityVulcanizer.class);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCoagulator.class, new CoagulatorRenderer().subscribeToList("coagulator"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), MetalMultiblocks1.COAGULATOR.getMeta(), TileEntityCoagulator.class);

		mech_con_renderer = new MechanicalConnectorRenderer().subscribeToList("motor_belts");
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMechanicalConnectable.class, mech_con_renderer);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMechanicalWheel.class, new WheelRenderer().subscribeToList("wheel"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMechanicalConnector), BlockIIMechanicalConnector.IIBlockTypes_MechanicalConnector.WOODEN_WHEEL.getMeta(), TileEntityMechanicalWheel.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoodenFenceGate.class, new FenceGateRenderer<>());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoodenChainFenceGate.class, new FenceGateRenderer<>());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteelFenceGate.class, new FenceGateRenderer<>());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteelChainFenceGate.class, new FenceGateRenderer<>());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAluminiumFenceGate.class, new FenceGateRenderer<>());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAluminiumChainFenceGate.class, new FenceGateRenderer<>());

		reloadModels();

		//Compat
		IICompatModule.doModulesClientPostInit();
	}

	private <T extends TileEntity> void registerTileRenderer(Class<? extends IITileRenderer<T>> clazz)
	{
		RegisteredTileRenderer rt = clazz.getAnnotation(RegisteredTileRenderer.class);
		try
		{
			IITileRenderer<T> tileRenderer = clazz.newInstance();
			ClientRegistry.bindTileEntitySpecialRenderer(((Class<T>)rt.clazz()), tileRenderer.subscribeToList(rt.name()));
		} catch(InstantiationException|IllegalAccessException ignored) {}
	}

	@Override
	public void reloadModels()
	{
		IIModelRegistry.instance.reloadRegisteredModels();
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
	}
}