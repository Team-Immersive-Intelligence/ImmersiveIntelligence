package pl.pabilo8.immersiveintelligence.client;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.immersiveengineering.api.energy.wires.WireApi;
import blusunrize.immersiveengineering.api.tool.ZoomHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.IECustomStateMapper;
import blusunrize.immersiveengineering.client.IEDefaultColourHandlers;
import blusunrize.immersiveengineering.client.ImmersiveModelRegistry;
import blusunrize.immersiveengineering.client.models.ModelItemDynamicOverride;
import blusunrize.immersiveengineering.client.models.obj.IEOBJLoader;
import blusunrize.immersiveengineering.client.render.EntityRenderNone;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IIEMetaBlock;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_Connector;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IColouredItem;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IGuiItem;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
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
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
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
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCasingType;
import pl.pabilo8.immersiveintelligence.api.utils.IItemScrollable;
import pl.pabilo8.immersiveintelligence.client.gui.GuiPrintedPage;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualDataAndElectronics;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualIntelligence;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualLogistics;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualWarfare;
import pl.pabilo8.immersiveintelligence.client.model.item.ModelMeasuringCup;
import pl.pabilo8.immersiveintelligence.client.render.*;
import pl.pabilo8.immersiveintelligence.client.render.item.*;
import pl.pabilo8.immersiveintelligence.client.render.mechanical_device.WheelRenderer;
import pl.pabilo8.immersiveintelligence.client.render.metal_device.*;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.*;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.wooden.SawmillRenderer;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.wooden.SkyCartStationRenderer;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.wooden.SkyCratePostRenderer;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.wooden.SkyCrateStationRenderer;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IICommandHandler;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIFluid;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityRedstoneInterface;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.*;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.TileEntityMechanicalConnectable;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.TileEntityMechanicalWheel;
import pl.pabilo8.immersiveintelligence.common.blocks.stone.TileEntitySandbags;
import pl.pabilo8.immersiveintelligence.common.blocks.types.*;
import pl.pabilo8.immersiveintelligence.common.entity.*;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityShrapnel;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIPrintedPage;
import pl.pabilo8.immersiveintelligence.common.items.tools.ItemIIDrillHead.DrillHeadPerm;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIWeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageEntityNBTSync;
import pl.pabilo8.immersiveintelligence.common.network.MessageItemScrollableSwitch;
import pl.pabilo8.immersiveintelligence.common.util.SkyCrateSound;

import javax.annotation.Nonnull;
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
	public static final String CAT_DATA = "ii_data";
	public static final String CAT_WARFARE = "ii_warfare";
	public static final String CAT_LOGISTICS = "ii_logi";
	public static final String CAT_INTELLIGENCE = "ii_intel";

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

	public static KeyBinding keybind_manualReload = new KeyBinding("key."+ImmersiveIntelligence.MODID+".manualReload", Keyboard.KEY_R, "key.categories.gameplay");
	public static KeyBinding keybind_machinegunScope = new KeyBinding("key."+ImmersiveIntelligence.MODID+".mgScope", Keyboard.KEY_Z, "key.categories.gameplay");
	public static KeyBinding keybind_motorbikeEngine = new KeyBinding("key."+ImmersiveIntelligence.MODID+".motorbikeEngine", Keyboard.KEY_R, "key.categories.gameplay");
	public static KeyBinding keybind_motorbikeTowing = new KeyBinding("key."+ImmersiveIntelligence.MODID+".motorbikeTowing", Keyboard.KEY_Z, "key.categories.gameplay");
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
		//WireApi.registerConnectorForRender("conn_data", new ResourceLocation(ImmersiveIntelligence.MODID+":block/data_connector.obj"), null);

		item_measuring_cup.specialModelMap.put(0, ModelMeasuringCup.MODEL);

		for(Block block : CommonProxy.blocks)
		{
			final ResourceLocation loc = Block.REGISTRY.getNameForObject(block);
			Item blockItem = Item.getItemFromBlock(block);
			if(blockItem==null)
				throw new RuntimeException("ITEMBLOCK FOR "+loc+" : "+block+" IS NULL");
			if(block instanceof BlockIIFluid)
			{
				mapFluidState(block, ((BlockIIFluid)block).getFluid());
			}
			else if(block instanceof IIEMetaBlock)
			{
				IIEMetaBlock ieMetaBlock = (IIEMetaBlock)block;
				if(ieMetaBlock.useCustomStateMapper())
					ModelLoader.setCustomStateMapper(block, IECustomStateMapper.getStateMapper(ieMetaBlock));
				ModelLoader.setCustomMeshDefinition(blockItem, new ItemMeshDefinition()
				{
					@Override
					public ModelResourceLocation getModelLocation(ItemStack stack)
					{
						return new ModelResourceLocation(loc, "inventory");
					}
				});
				for(int meta = 0; meta < ieMetaBlock.getMetaEnums().length; meta++)
				{
					BlockIIBase b = (BlockIIBase)block;
					if(!b.tesrMap.isEmpty()&&b.tesrMap.containsKey(Integer.valueOf(meta)))
					{
						ModelLoader.setCustomModelResourceLocation(blockItem, meta, new ModelResourceLocation(new ResourceLocation(ImmersiveIntelligence.MODID, "itemblock/"+b.tesrMap.get(Integer.valueOf(meta))), "inventory"));
					}
					else
					{
						String location = loc.toString();
						String prop = ieMetaBlock.appendPropertiesToState()?("inventory,"+ieMetaBlock.getMetaProperty().getName()+"="+ieMetaBlock.getMetaEnums()[meta].toString().toLowerCase(Locale.US)): null;
						if(ieMetaBlock.useCustomStateMapper())
						{
							String custom = ieMetaBlock.getCustomStateMapping(meta, true);
							if(custom!=null)
								location += "_"+custom;
						}
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

		for(Item item : CommonProxy.items)
		{
			if(item instanceof ItemBlock)
				continue;
			if(item instanceof ItemIIBase)
			{
				ItemIIBase ieMetaItem = (ItemIIBase)item;
				if(ieMetaItem.registerSubModels&&ieMetaItem.getSubNames()!=null&&ieMetaItem.getSubNames().length > 0)
				{
					for(int meta = 0; meta < ieMetaItem.getSubNames().length; meta++)
					{
						ResourceLocation loc = new ResourceLocation(ImmersiveIntelligence.MODID, ieMetaItem.itemName+"/"+ieMetaItem.getSubNames()[meta]);
						ModelBakery.registerItemVariants(ieMetaItem, loc);
						ModelLoader.setCustomModelResourceLocation(ieMetaItem, meta, new ModelResourceLocation(loc, "inventory"));
					}
				}
				else
				{
					if(ieMetaItem.specialModelMap.containsKey(0))
					{
						ModelLoaderRegistry.registerLoader(ieMetaItem.specialModelMap.get(0).getInstance());
						ModelLoader.setCustomMeshDefinition(ieMetaItem, stack -> ieMetaItem.specialModelMap.get(0).getLocation());
						ModelBakery.registerItemVariants(ieMetaItem, ieMetaItem.specialModelMap.get(0).getLocation());
					}
					else
					{
						final ResourceLocation loc = new ResourceLocation(ImmersiveIntelligence.MODID, ieMetaItem.itemName);
						ModelBakery.registerItemVariants(ieMetaItem, loc);
						ModelLoader.setCustomMeshDefinition(ieMetaItem, stack -> new ModelResourceLocation(loc, "inventory"));
					}
				}
			}
			else
			{
				final ResourceLocation loc = Item.REGISTRY.getNameForObject(item);
				ModelBakery.registerItemVariants(item, loc);
				ModelLoader.setCustomMeshDefinition(item, stack -> new ModelResourceLocation(loc, "inventory"));
			}
		}

		addModelToItemSubtype(item_sawblade, 8, new ModelResourceLocation(new ResourceLocation(ImmersiveIntelligence.MODID, "sawblade/iron_display"), "inventory"));
		addModelToItemSubtype(item_sawblade, 9, new ModelResourceLocation(new ResourceLocation(ImmersiveIntelligence.MODID, "sawblade/steel_display"), "inventory"));
		addModelToItemSubtype(item_sawblade, 10, new ModelResourceLocation(new ResourceLocation(ImmersiveIntelligence.MODID, "sawblade/tungsten_display"), "inventory"));
	}

	private static void mapFluidState(Block block, Fluid fluid)
	{
		Item item = Item.getItemFromBlock(block);
		FluidStateMapper mapper = new FluidStateMapper(fluid);
		if(item!=null)
		{
			ModelLoader.registerItemVariants(item);
			ModelLoader.setCustomMeshDefinition(item, mapper);
		}
		ModelLoader.setCustomStateMapper(block, mapper);
	}

	public static void addModelToItemSubtype(ItemIIBase item, int meta, ResourceLocation loc)
	{
		ModelBakery.registerItemVariants(item, loc);
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(loc.toString()));
		ModelLoader.setCustomMeshDefinition(item, stack -> new ModelResourceLocation(loc, "inventory"));
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		ItemStack stack = player.getHeldItem(player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IGuiItem?EnumHand.MAIN_HAND: EnumHand.OFF_HAND);
		if(te instanceof IGuiTile)
		{
			GuiScreen gui = null;
			if(IIGuiList.values().length > ID&&IIGuiList.values()[ID].teClass.isInstance(te))
			{
				gui = IIGuiList.values()[ID].gui.apply(player, te);
			}

			if(gui!=null)
			{
				((IGuiTile)te).onGuiOpened(player, true);
				return gui;
			}
		}
		else
		{
			if(stack.getItem() instanceof ItemIIPrintedPage&&ID==IIGuiList.GUI_PRINTED_PAGE_TEXT.ordinal())
			{
				return new GuiPrintedPage(player, stack);
			}
		}
		return null;
	}

	@Override
	public void preInit()
	{
		super.preInit();
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(EvenMoreImmersiveModelRegistry.instance);
		OBJLoader.INSTANCE.addDomain(ImmersiveIntelligence.MODID);
		IEOBJLoader.instance.addDomain(ImmersiveIntelligence.MODID);
		//None of your trickery is present here...
		//And yes, I would like to have .obj models, really...
		//But using TMT seems easier (and more weird).
		RenderingRegistry.registerEntityRenderingHandler(EntitySkyCrate.class, SkyCrateRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, BulletRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityShrapnel.class, ShrapnelRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMachinegun.class, MachinegunRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMotorbike.class, MotorbikeRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFieldHowitzer.class, FieldHowitzerRenderer::new);
		//Thanks Blu!
		RenderingRegistry.registerEntityRenderingHandler(EntitySkycrateInternal.class, EntityRenderNone::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityVehicleSeat.class, EntityRenderNone::new);

		EvenMoreImmersiveModelRegistry.instance.registerCustomItemModel(new ItemStack(item_bullet, 1, 0), new ImmersiveModelRegistry.ItemModelReplacement()
		{
			@Override
			public IBakedModel createBakedModel(IBakedModel existingModel)
			{
				return new ModelItemDynamicOverride(existingModel, null);
			}
		}, ImmersiveIntelligence.MODID);

		EvenMoreImmersiveModelRegistry.instance.registerCustomItemModel(new ItemStack(item_bullet_magazine, 1, 0), new ImmersiveModelRegistry.ItemModelReplacement()
		{
			@Override
			public IBakedModel createBakedModel(IBakedModel existingModel)
			{
				return new ModelItemDynamicOverride(existingModel, null);
			}
		}, ImmersiveIntelligence.MODID);

		EvenMoreImmersiveModelRegistry.instance.registerCustomItemModel(new ItemStack(item_binoculars, 1, 1), new ImmersiveModelRegistry.ItemModelReplacement()
		{
			@Override
			public IBakedModel createBakedModel(IBakedModel existingModel)
			{
				return new ModelItemDynamicOverride(existingModel, null);
			}
		}, ImmersiveIntelligence.MODID);

		item_motor_belt.setRenderModels();
	}

	@SubscribeEvent
	public void textureStichPre(TextureStitchEvent.Pre event)
	{
		for(Map.Entry<String, IBulletCasingType> s : BulletRegistry.INSTANCE.registeredCasings.entrySet())
		{
			ImmersiveIntelligence.logger.info("registering sprite for bullet casing: "+s.getKey());
			ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/bullets/bullet_"+s.getKey().toLowerCase()+"_main");
			ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/bullets/bullet_"+s.getKey().toLowerCase()+"_core");
			ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/bullets/bullet_"+s.getKey().toLowerCase()+"_paint");
		}


		for(String s : item_bullet_magazine.getSubNames())
		{
			ImmersiveIntelligence.logger.info("registering sprite for bullet magazine type: "+s);
			ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/bullets/magazines/"+s+"/main");

			ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/bullets/magazines/"+s+"/bullet0");
			ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/bullets/magazines/"+s+"/paint0");

			ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/bullets/magazines/"+s+"/bullet1");
			ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/bullets/magazines/"+s+"/paint1");

			ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/bullets/magazines/"+s+"/bullet2");
			ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/bullets/magazines/"+s+"/paint2");

			ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/bullets/magazines/"+s+"/bullet3");
			ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/bullets/magazines/"+s+"/paint3");

		}

		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/binoculars/infrared_binoculars_off");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/binoculars/infrared_binoculars_on");

		for(Entry<String, Shrapnel> s : ShrapnelHandler.registry.entrySet())
			ApiUtils.getRegisterSprite(event.getMap(), s.getValue().texture.replace("textures/", ""));

		for(DrillHeadPerm perm : item_drillhead.perms)
			perm.sprite = ApiUtils.getRegisterSprite(event.getMap(), perm.texture);

		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/data_connector_feedtrough");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/data_connector");

	}

	@Override
	public void reInitGui()
	{
		if(ClientUtils.mc().currentScreen!=null)
			ClientUtils.mc().currentScreen.initGui();
	}

	@Override
	public void init()
	{
		super.init();

		ClientEventHandler handler = new ClientEventHandler();
		MinecraftForge.EVENT_BUS.register(handler);
		((IReloadableResourceManager)ClientUtils.mc().getResourceManager()).registerReloadListener(handler);

		keybind_manualReload.setKeyConflictContext(passenger_action);
		keybind_machinegunScope.setKeyConflictContext(passenger_action);
		keybind_motorbikeEngine.setKeyConflictContext(passenger_action);
		keybind_motorbikeTowing.setKeyConflictContext(passenger_action);

		ClientRegistry.registerKeyBinding(keybind_manualReload);
		ClientRegistry.registerKeyBinding(keybind_machinegunScope);
		ClientRegistry.registerKeyBinding(keybind_motorbikeEngine);
		ClientRegistry.registerKeyBinding(keybind_motorbikeTowing);

		ShaderUtil.init();

		TileEntityInserter.conn_data = new ItemStack(block_data_connector, 1, IIBlockTypes_Connector.DATA_CONNECTOR.getMeta());
		TileEntityInserter.conn_mv = new ItemStack(IEContent.blockConnectors, 1, BlockTypes_Connector.CONNECTOR_MV.getMeta());

		TileEntityAdvancedInserter.conn_data = new ItemStack(block_data_connector, 1, IIBlockTypes_Connector.DATA_CONNECTOR.getMeta());
		TileEntityAdvancedInserter.conn_mv = new ItemStack(IEContent.blockConnectors, 1, BlockTypes_Connector.CONNECTOR_MV.getMeta());

		TileEntityFluidInserter.conn_data = new ItemStack(block_data_connector, 1, IIBlockTypes_Connector.DATA_CONNECTOR.getMeta());
		TileEntityFluidInserter.conn_mv = new ItemStack(IEContent.blockConnectors, 1, BlockTypes_Connector.CONNECTOR_MV.getMeta());

		TileEntityChemicalDispenser.conn_data = new ItemStack(block_data_connector, 1, IIBlockTypes_Connector.DATA_CONNECTOR.getMeta());
		TileEntityChemicalDispenser.conn_mv = new ItemStack(IEContent.blockConnectors, 1, BlockTypes_Connector.CONNECTOR_MV.getMeta());

		//TODO:Advanced Fluid Inserter
		//TileEntityInserter.conn_data = new ItemStack(block_data_connector, 1, IIBlockTypes_Connector.DATA_CONNECTOR.getMeta());
		//TileEntityInserter.conn_mv = new ItemStack(IEContent.blockConnectors, 1, BlockTypes_Connector.CONNECTOR_MV.getMeta());

		for(Item item : items)
			if(item instanceof IColouredItem&&((IColouredItem)item).hasCustomItemColours())
				ClientUtils.mc().getItemColors().registerItemColorHandler(IEDefaultColourHandlers.INSTANCE, item);
	}

	@Override
	public void postInit()
	{
		super.postInit();
		ImmersiveIntelligence.logger.info("Registering II Manual Pages.");

		IIManualDataAndElectronics.INSTANCE.addPages();
		IIManualLogistics.INSTANCE.addPages();
		IIManualWarfare.INSTANCE.addPages();
		IIManualIntelligence.INSTANCE.addPages();

		ManualHelper.addEntry("chemical_bath", ManualHelper.CAT_HEAVYMACHINES,
				new ManualPageMultiblock(ManualHelper.getManual(), "chemical_bath0", MultiblockChemicalBath.instance)
		);
		ManualHelper.addEntry("precission_assembler", ManualHelper.CAT_HEAVYMACHINES,
				new ManualPageMultiblock(ManualHelper.getManual(), "precission_assembler0", MultiblockPrecissionAssembler.instance),
				new ManualPages.Text(ManualHelper.getManual(), "precission_assembler1")
		);
		ManualHelper.addEntry("electrolyzer", ManualHelper.CAT_HEAVYMACHINES,
				new ManualPageMultiblock(ManualHelper.getManual(), "electrolyzer0", MultiblockElectrolyzer.instance)
		);

		ManualHelper.addEntry("rotary_power", ManualHelper.CAT_MACHINES,
				new ManualPages.Image(ManualHelper.getManual(), "rotary_power0", ImmersiveIntelligence.MODID+":textures/misc/rotary.png;0;0;110;64"),
				new ManualPages.Text(ManualHelper.getManual(), "rotary_power1"),
				new ManualPages.Crafting(ManualHelper.getManual(), "rotary_power2", new ItemStack(block_mechanical_connector)),
				new ManualPages.CraftingMulti(ManualHelper.getManual(), "rotary_power3", new ItemStack(item_motor_belt, 1, 0), new ItemStack(item_motor_belt, 1, 1)),
				new ManualPages.Crafting(ManualHelper.getManual(), "rotary_power4", new ItemStack(block_gearbox)),
				new ManualPages.Crafting(ManualHelper.getManual(), "rotary_power5", new ItemStack(block_mechanical_device, 1, IIBlockTypes_MechanicalDevice.WOODEN_TRANSMISSION_BOX.getMeta())),
				new ManualPages.CraftingMulti(ManualHelper.getManual(), "rotary_power6", new ItemStack(item_wrench), new ItemStack(item_electric_wrench))
		);

		ManualHelper.addEntry("sawmill", ManualHelper.CAT_MACHINES,
				new ManualPageMultiblock(ManualHelper.getManual(), "sawmill0", MultiblockSawmill.instance),
				new ManualPages.Text(ManualHelper.getManual(), "sawmill1"),
				new ManualPages.Text(ManualHelper.getManual(), "sawmill2")
		);

		ClientCommandHandler.instance.registerCommand(new IICommandHandler("tmt"));


		//Weapons (Items)
		item_machinegun.setTileEntityItemStackRenderer(MachinegunItemStackRenderer.instance);
		item_submachinegun.setTileEntityItemStackRenderer(SubmachinegunItemStackRenderer.instance);
		ItemIIWeaponUpgrade.addUpgradesToRender();

		//Block / ItemStack rendering
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAmmunitionCrate.class, new AmmunitionCrateRenderer());

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInserter.class, new InserterRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAdvancedInserter.class, new AdvancedInserterRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidInserter.class, new FluidInserterRenderer());

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTimedBuffer.class, new TimedBufferRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRedstoneBuffer.class, new RedstoneBufferRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySmallDataBuffer.class, new SmallDataBufferRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDataMerger.class, new DataMergerRenderer());

		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_metal_device), IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta(), TileEntityAmmunitionCrate.class);


		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_metal_device), IIBlockTypes_MetalDevice.TIMED_BUFFER.getMeta(), TileEntityTimedBuffer.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_metal_device), IIBlockTypes_MetalDevice.REDSTONE_BUFFER.getMeta(), TileEntityRedstoneBuffer.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_metal_device), IIBlockTypes_MetalDevice.SMALL_DATA_BUFFER.getMeta(), TileEntitySmallDataBuffer.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_metal_device), IIBlockTypes_MetalDevice.DATA_MERGER.getMeta(), TileEntityDataMerger.class);

		//Data Connectors
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDataConnector.class, new DataConnectorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDataRelay.class, new DataRelayRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChemicalDispenser.class, new ChemicalDispenserRenderer());

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySandbags.class, new SandbagsRenderer());
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_stone_decoration), IIBlockTypes_StoneDecoration.SANDBAGS.getMeta(), TileEntitySandbags.class);

		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_data_connector), IIBlockTypes_Connector.DATA_CONNECTOR.getMeta(), TileEntityDataConnector.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_data_connector), IIBlockTypes_Connector.DATA_RELAY.getMeta(), TileEntityDataRelay.class);

		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_data_connector), IIBlockTypes_Connector.INSERTER.getMeta(), TileEntityInserter.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_data_connector), IIBlockTypes_Connector.ADVANCED_INSERTER.getMeta(), TileEntityAdvancedInserter.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_data_connector), IIBlockTypes_Connector.FLUID_INSERTER.getMeta(), TileEntityFluidInserter.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_data_connector), IIBlockTypes_Connector.CHEMICAL_DISPENSER.getMeta(), TileEntityChemicalDispenser.class);
		//ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_data_connector), IIBlockTypes_Connector.INSERTER.getMeta(), TileEntityInserter.class);

		//Alarm Siren
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAlarmSiren.class, new AlarmSirenRenderer());
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_data_connector), IIBlockTypes_Connector.ALARM_SIREN.getMeta(), TileEntityAlarmSiren.class);

		//Programmable Speaker
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityProgrammableSpeaker.class, new ProgrammableSpeakerRenderer());
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_data_connector), IIBlockTypes_Connector.PROGRAMMABLE_SPEAKER.getMeta(), TileEntityProgrammableSpeaker.class);

		//Small Crate
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySmallCrate.class, new SmallCrateRenderer());
		Item.getItemFromBlock(block_small_crate).setTileEntityItemStackRenderer(SmallCrateItemStackRenderer.instance);

		//Tools
		item_tachometer.setTileEntityItemStackRenderer(TachometerItemStackRenderer.instance);
		item_radio_configurator.setTileEntityItemStackRenderer(RadioConfiguratorItemStackRenderer.instance);

		//Multiblocks
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkyCrateStation.class, new SkyCrateStationRenderer().subscribeToList("skycrate_station"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkyCartStation.class, new SkyCartStationRenderer().subscribeToList("skycart_station"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkyCratePost.class, new SkyCratePostRenderer().subscribeToList("skycrate_post"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRadioStation.class, new RadioStationRenderer().subscribeToList("radio_station"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_metal_multiblock0), IIBlockTypes_MetalMultiblock0.RADIO_STATION.getMeta(), TileEntityRadioStation.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDataInputMachine.class, new DataInputMachineRenderer().subscribeToList("data_input_machine"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityArithmeticLogicMachine.class, new ArithmeticLogicMachineRenderer().subscribeToList("arithmetic_logic_machine"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrintingPress.class, new PrintingPressRenderer().subscribeToList("printing_press"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_metal_multiblock0), IIBlockTypes_MetalMultiblock0.PRINTING_PRESS.getMeta(), TileEntityPrintingPress.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChemicalBath.class, new ChemicalBathRenderer().subscribeToList("chemical_bath"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_metal_multiblock0), IIBlockTypes_MetalMultiblock0.CHEMICAL_BATH.getMeta(), TileEntityChemicalBath.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityElectrolyzer.class, new ElectrolyzerRenderer().subscribeToList("electrolyzer"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_metal_multiblock0), IIBlockTypes_MetalMultiblock0.ELECTROLYZER.getMeta(), TileEntityElectrolyzer.class);

		//TODO: Fix misspeling on 1.14, not now, it could break stuff
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrecissionAssembler.class, new PrecissionAssemblerRenderer().subscribeToList("precision_assembler"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_metal_multiblock0), IIBlockTypes_MetalMultiblock0.PRECISSION_ASSEMBLER.getMeta(), TileEntityPrecissionAssembler.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAmmunitionFactory.class, new AmmunitionFactoryRenderer());
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_metal_multiblock0), IIBlockTypes_MetalMultiblock0.AMMUNITION_FACTORY.getMeta(), TileEntityAmmunitionFactory.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySawmill.class, new SawmillRenderer().subscribeToList("sawmill"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_wooden_multiblock), IIBlockTypes_WoodenMultiblock.SAWMILL.getMeta(), TileEntitySawmill.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityConveyorScanner.class, new ConveyorScannerRenderer().subscribeToList("conveyor_scanner"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityArtilleryHowitzer.class, new ArtilleryHowitzerRenderer().subscribeToList("artillery_howitzer"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_metal_multiblock0), IIBlockTypes_MetalMultiblock0.ARTILLERY_HOWITZER.getMeta(), TileEntityArtilleryHowitzer.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBallisticComputer.class, new BallisticComputerRenderer().subscribeToList("ballistic_computer"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPacker.class, new PackerRenderer().subscribeToList("packer"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRedstoneInterface.class, new RedstoneInterfaceRenderer().subscribeToList("redstone_data_interface"));

		mech_con_renderer = new MechanicalConnectorRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMechanicalConnectable.class, mech_con_renderer);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMechanicalWheel.class, new WheelRenderer());
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_mechanical_connector), IIBlockTypes_MechanicalConnector.WOODEN_WHEEL.getMeta(), TileEntityMechanicalWheel.class);

		reloadModels();
	}

	@Override
	public void renderTile(TileEntity te)
	{
		TileEntitySpecialRenderer<TileEntity> tesr = TileEntityRendererDispatcher.instance.getRenderer(te);

		tesr.render(te, 0, 0, 0, 0, 0, 0);
	}

	@Override
	public void startSkyhookSound(EntitySkyCrate hook)
	{
		ClientUtils.mc().getSoundHandler().playSound(new SkyCrateSound(hook,
				new ResourceLocation(ImmersiveEngineering.MODID, "skyhook")));
	}

	@Override
	public void onBreakBlock(BreakEvent event)
	{
		if(!event.getWorld().isRemote)
		{
			super.onBreakBlock(event);
		}
	}

	@SubscribeEvent
	public void onMouseEvent(MouseEvent event)
	{
		if(event.getDwheel()!=0)
		{
			EntityPlayer player = ClientUtils.mc().player;
			if(!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty()||player.getRidingEntity() instanceof EntityMachinegun)
			{
				ItemStack equipped = player.getHeldItem(EnumHand.MAIN_HAND);

				if(equipped.getItem() instanceof IItemScrollable&&player.isSneaking())
				{
					IIPacketHandler.INSTANCE.sendToServer(new MessageItemScrollableSwitch(event.getDwheel() > 0));
					event.setCanceled(true);
				}
				if(player.getRidingEntity() instanceof EntityMachinegun&&ZoomHandler.isZooming&&EntityMachinegun.scope.canZoom(((EntityMachinegun)player.getRidingEntity()).gun, player))
				{
					float[] steps = EntityMachinegun.scope.getZoomSteps(((EntityMachinegun)player.getRidingEntity()).gun, player);
					if(steps!=null&&steps.length > 0)
					{
						int curStep = -1;
						float dist = 0;
						for(int i = 0; i < steps.length; i++)
							if(curStep==-1||Math.abs(steps[i]-ZoomHandler.fovZoom) < dist)
							{
								curStep = i;
								dist = Math.abs(steps[i]-ZoomHandler.fovZoom);
							}
						if(curStep!=-1)
						{
							int newStep = curStep+(event.getDwheel() > 0?-1: 1);
							if(newStep >= 0&&newStep < steps.length)
								ZoomHandler.fovZoom = steps[newStep];
							event.setCanceled(true);
						}
					}
				}
			}
		}
		//Rightclick
		if(ClientUtils.mc().player.getRidingEntity() instanceof EntityMachinegun&&event.getButton()==1)
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setBoolean("clientMessage", true);
			tag.setBoolean("shoot", event.isButtonstate());
			IIPacketHandler.INSTANCE.sendToServer(new MessageEntityNBTSync(ClientUtils.mc().player.getRidingEntity(), tag));
		}
	}

	@Override
	public void reloadModels()
	{
		EvenMoreImmersiveModelRegistry.instance.reloadRegisteredModels();
	}

	static class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition
	{
		public final ModelResourceLocation location;

		public FluidStateMapper(Fluid fluid)
		{
			this.location = new ModelResourceLocation(ImmersiveIntelligence.MODID+":fluid_block", fluid.getName());
		}

		@Nonnull
		@Override
		protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state)
		{
			return location;
		}

		@Nonnull
		@Override
		public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack)
		{
			return location;
		}
	}
}