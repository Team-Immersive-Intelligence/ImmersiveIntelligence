package pl.pabilo8.immersiveintelligence.client;

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
import blusunrize.immersiveengineering.client.render.ItemRendererIEOBJ;
import blusunrize.immersiveengineering.common.Config;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IColouredBlock;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IIEMetaBlock;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_Connector;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IColouredItem;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IGuiItem;
import blusunrize.immersiveengineering.common.items.ItemIEBase;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import blusunrize.lib.manual.IManualPage;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualInstance.ManualEntry;
import blusunrize.lib.manual.ManualPages;
import blusunrize.lib.manual.ManualPages.Table;
import blusunrize.lib.manual.gui.GuiManual;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
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
import net.minecraftforge.client.event.GuiOpenEvent;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.CustomSkinHandler;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.LighterFuelHandler;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler.Shrapnel;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.IBullet;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.utils.IEntityZoomProvider;
import pl.pabilo8.immersiveintelligence.api.utils.IItemScrollable;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleGasCloud;
import pl.pabilo8.immersiveintelligence.client.gui.*;
import pl.pabilo8.immersiveintelligence.client.gui.ammunition_production.GuiAmmunitionWorkshop;
import pl.pabilo8.immersiveintelligence.client.gui.ammunition_production.GuiProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticLogicMachineEdit;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticLogicMachineStorage;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticMachineVariables;
import pl.pabilo8.immersiveintelligence.client.gui.data_input_machine.GuiDataInputMachineEdit;
import pl.pabilo8.immersiveintelligence.client.gui.data_input_machine.GuiDataInputMachineStorage;
import pl.pabilo8.immersiveintelligence.client.gui.data_input_machine.GuiDataInputMachineVariables;
import pl.pabilo8.immersiveintelligence.client.gui.emplacement.GuiEmplacementPageStatus;
import pl.pabilo8.immersiveintelligence.client.gui.emplacement.GuiEmplacementPageStorage;
import pl.pabilo8.immersiveintelligence.client.gui.emplacement.GuiEmplacementPageTasks;
import pl.pabilo8.immersiveintelligence.client.manual.*;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageContributorSkin;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageDataVariables;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageDataVariablesCallback;
import pl.pabilo8.immersiveintelligence.client.model.item.ModelMeasuringCup;
import pl.pabilo8.immersiveintelligence.client.render.*;
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
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IICommandHandler;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIFluid;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.*;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.conveyors.*;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.inserter.TileEntityAdvancedInserter;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.inserter.TileEntityInserter;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.gate.MultiblockAluminiumChainFenceGate.TileEntityAluminiumChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.gate.MultiblockAluminiumFenceGate.TileEntityAluminiumFenceGate;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.gate.MultiblockSteelChainFenceGate.TileEntitySteelChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.gate.MultiblockSteelFenceGate.TileEntitySteelFenceGate;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.gate.MultiblockWoodenChainFenceGate.TileEntityWoodenChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.gate.MultiblockWoodenFenceGate.TileEntityWoodenFenceGate;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.*;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.TileEntityGearbox;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.TileEntityMechanicalConnectable;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.TileEntityMechanicalPump;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.TileEntityMechanicalWheel;
import pl.pabilo8.immersiveintelligence.common.blocks.types.*;
import pl.pabilo8.immersiveintelligence.common.blocks.wooden.TileEntityMineSign;
import pl.pabilo8.immersiveintelligence.common.entity.*;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.*;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;
import pl.pabilo8.immersiveintelligence.common.items.ammunition.ItemIIAmmoRevolver;
import pl.pabilo8.immersiveintelligence.common.items.ammunition.ItemIIBulletBase;
import pl.pabilo8.immersiveintelligence.common.items.ammunition.ItemIINavalMine;
import pl.pabilo8.immersiveintelligence.common.items.tools.ItemIIDrillHead.DrillHeadPerm;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIWeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageEntityNBTSync;
import pl.pabilo8.immersiveintelligence.common.network.MessageItemScrollableSwitch;
import pl.pabilo8.immersiveintelligence.common.network.MessageManualClose;

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
	public static final String CAT_MOTORWORKS = "ii_motorworks";

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
	public static KeyBinding keybind_armorHelmet = new KeyBinding("key."+ImmersiveIntelligence.MODID+".armorHelmet", Keyboard.KEY_V, "key.categories.gameplay");
	public static KeyBinding keybind_armorExosuit = new KeyBinding("key."+ImmersiveIntelligence.MODID+".armorExosuit", Keyboard.KEY_G, "key.categories.gameplay");
	public static KeyBinding keybind_zoom = new KeyBinding("key."+ImmersiveIntelligence.MODID+".mgScope", Keyboard.KEY_Z, "key.categories.gameplay");
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
		WireApi.registerConnectorForRender("tripwire", new ResourceLocation(ImmersiveIntelligence.MODID+":block/tripwire_connector.obj"), null);

		IIContent.itemMeasuringCup.specialModelMap.put(0, ModelMeasuringCup.MODEL);

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
						if(ieMetaBlock.useCustomStateMapper())
						{
							String custom = ieMetaBlock.getCustomStateMapping(meta, true);
							//Use @Nullable annotation!
							if(custom!=null)
								location += "_"+custom;
						}
						try
						{
							ModelLoader.setCustomModelResourceLocation(blockItem, meta, new ModelResourceLocation(location, prop));
						}
						catch(NullPointerException npe)
						{
							throw new RuntimeException("WELP! apparently "+ieMetaBlock+" lacks an item!", npe);
						}
					}

				}
			}
			else
				ModelLoader.setCustomModelResourceLocation(blockItem, 0, new ModelResourceLocation(loc, "inventory"));
		}

		for(Item item : IIContent.ITEMS)
		{
			if(item instanceof ItemBlock)
				continue;
			if(item instanceof ItemIEBase)
			{
				ItemIEBase ieMetaItem = (ItemIEBase)item;
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
					if(item instanceof ItemIIBase&&((ItemIIBase)ieMetaItem).specialModelMap.containsKey(0))
					{
						ModelLoaderRegistry.registerLoader(((ItemIIBase)ieMetaItem).specialModelMap.get(0).getInstance());
						ModelLoader.setCustomMeshDefinition(ieMetaItem, stack -> ((ItemIIBase)ieMetaItem).specialModelMap.get(0).getLocation());
						ModelBakery.registerItemVariants(ieMetaItem, ((ItemIIBase)ieMetaItem).specialModelMap.get(0).getLocation());
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

		addModelToItemSubtype(IIContent.itemSawblade, 8, new ModelResourceLocation(new ResourceLocation(ImmersiveIntelligence.MODID, "sawblade/iron_display"), "inventory"));
		addModelToItemSubtype(IIContent.itemSawblade, 9, new ModelResourceLocation(new ResourceLocation(ImmersiveIntelligence.MODID, "sawblade/steel_display"), "inventory"));
		addModelToItemSubtype(IIContent.itemSawblade, 10, new ModelResourceLocation(new ResourceLocation(ImmersiveIntelligence.MODID, "sawblade/tungsten_display"), "inventory"));
	}

	private static void mapFluidState(Block block, Fluid fluid)
	{
		Item item = Item.getItemFromBlock(block);
		FluidStateMapper mapper = new FluidStateMapper(fluid);
		ModelLoader.registerItemVariants(item);
		ModelLoader.setCustomMeshDefinition(item, mapper);
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

		if(ID==IIGuiList.GUI_UPGRADE.ordinal())
		{
			if(te instanceof IUpgradableMachine)
			{
				TileEntity upgradeMaster = ((IUpgradableMachine)te).getUpgradeMaster();
				if(upgradeMaster!=null)
					return new GuiUpgrade(player.inventory, (TileEntity & IUpgradableMachine)upgradeMaster);
			}
		}

		GuiScreen gui;
		if(IIGuiList.values().length > ID)
		{
			IIGuiList guiBuilder = IIGuiList.values()[ID];
			if(guiBuilder.item)
			{
				return guiBuilder.guiFromStack.apply(player, stack);
			}
			else if(te instanceof IGuiTile&&guiBuilder.teClass.isInstance(te))
			{
				gui = guiBuilder.guiFromTile.apply(player, te);
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
	public void preInit()
	{
		super.preInit();
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(EvenMoreImmersiveModelRegistry.instance);
		OBJLoader.INSTANCE.addDomain(ImmersiveIntelligence.MODID);
		IEOBJLoader.instance.addDomain(ImmersiveIntelligence.MODID);

		//long live .obj models! ^^

		RenderingRegistry.registerEntityRenderingHandler(EntitySkyCrate.class, SkyCrateRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, BulletRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityNavalMine.class, NavalMineRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityNavalMineAnchor.class, NavalMineAnchorRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityShrapnel.class, ShrapnelRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityWhitePhosphorus.class, EntityRenderNone::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMachinegun.class, MachinegunRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMotorbike.class, MotorbikeRenderer::new);
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

		//<s>Railgun overwrite</s> <b>Sunlight Railgun Overdrive!</b>
		ImmersiveModelRegistry.instance.registerCustomItemModel(new ItemStack(IEContent.itemRailgun, 1, 0), new ImmersiveModelRegistry.ItemModelReplacement_OBJ("immersiveengineering:models/item/railgun.obj", true)
				.setTransformations(TransformType.FIRST_PERSON_RIGHT_HAND, new Matrix4().scale(.125, .125, .125).translate(-.1875f, 2.5f, .25f).rotate(Math.PI*.46875, 0, 1, 0).translate(0.5, 0.25, -0.75f)
						.rotate(Math.PI*.0225, 0, 0, 1).scale(1.125, 1.125, 1.125))
				.setTransformations(TransformType.FIRST_PERSON_LEFT_HAND, new Matrix4().scale(.125, .125, .125).translate(-1.75, 1.625, .875).rotate(-Math.PI*.46875, 0, 1, 0))
				.setTransformations(TransformType.THIRD_PERSON_RIGHT_HAND, new Matrix4().scale(.1875, .1875, .1875).translate(0.5, 0.5f, -3.5).rotate(Math.PI*.40125, 0, 1, 0))
				.setTransformations(TransformType.THIRD_PERSON_LEFT_HAND, new Matrix4().translate(-.1875, .5, -.3125).scale(.1875, .1875, .1875).rotate(-Math.PI*.46875, 0, 1, 0).rotate(-Math.PI*.25, 0, 0, 1))
				.setTransformations(TransformType.FIXED, new Matrix4().translate(.1875, .0625, .0625).scale(.125, .125, .125).rotate(-Math.PI*.25, 0, 0, 1))
				.setTransformations(TransformType.GUI, new Matrix4().translate(-.1875, 0, 0).scale(.1875, .1875, .1875).rotate(-Math.PI*.6875, 0, 1, 0).rotate(-Math.PI*.1875, 0, 0, 1))
				.setTransformations(TransformType.GROUND, new Matrix4().translate(.125, .125, .0625).scale(.125, .125, .125)));
		IEContent.itemRailgun.setTileEntityItemStackRenderer(ItemRendererIEOBJ.INSTANCE);

		for(IBullet bullet : BulletRegistry.INSTANCE.registeredBulletItems.values())
		{
			if(bullet instanceof ItemIINavalMine)
			{

			}
			else if(bullet instanceof ItemIIBulletBase)
			{
				EvenMoreImmersiveModelRegistry.instance.registerCustomItemModel(new ItemStack((ItemIIBulletBase)bullet, 1, ItemIIBulletBase.BULLET), new ImmersiveModelRegistry.ItemModelReplacement()
				{
					@Override
					public IBakedModel createBakedModel(IBakedModel existingModel)
					{
						return new ModelItemDynamicOverride(existingModel, null);
					}
				}, ImmersiveIntelligence.MODID);
				EvenMoreImmersiveModelRegistry.instance.registerCustomItemModel(new ItemStack((ItemIIBulletBase)bullet, 1, ItemIIBulletBase.CORE), new ImmersiveModelRegistry.ItemModelReplacement()
				{
					@Override
					public IBakedModel createBakedModel(IBakedModel existingModel)
					{
						return new ModelItemDynamicOverride(existingModel, null);
					}
				}, ImmersiveIntelligence.MODID);
			}
			else if(bullet instanceof ItemIIAmmoRevolver)
			{
				EvenMoreImmersiveModelRegistry.instance.registerCustomItemModel(new ItemStack((ItemIIAmmoRevolver)bullet, 1, ItemIIAmmoRevolver.BULLET), new ImmersiveModelRegistry.ItemModelReplacement()
				{
					@Override
					public IBakedModel createBakedModel(IBakedModel existingModel)
					{
						return new ModelItemDynamicOverride(existingModel, null);
					}
				}, ImmersiveIntelligence.MODID);
				EvenMoreImmersiveModelRegistry.instance.registerCustomItemModel(new ItemStack((ItemIIAmmoRevolver)bullet, 1, ItemIIAmmoRevolver.CORE), new ImmersiveModelRegistry.ItemModelReplacement()
				{
					@Override
					public IBakedModel createBakedModel(IBakedModel existingModel)
					{
						return new ModelItemDynamicOverride(existingModel, null);
					}
				}, ImmersiveIntelligence.MODID);
			}
		}

		for(int i = 0; i < IIContent.itemBulletMagazine.getSubNames().length; i++)
		{
			EvenMoreImmersiveModelRegistry.instance.registerCustomItemModel(new ItemStack(IIContent.itemBulletMagazine, 1, i), new ImmersiveModelRegistry.ItemModelReplacement()
			{
				@Override
				public IBakedModel createBakedModel(IBakedModel existingModel)
				{
					return new ModelItemDynamicOverride(existingModel, null);
				}
			}, ImmersiveIntelligence.MODID);
		}


		EvenMoreImmersiveModelRegistry.instance.registerCustomItemModel(new ItemStack(IIContent.itemBinoculars, 1, 0), new ImmersiveModelRegistry.ItemModelReplacement()
		{
			@Override
			public IBakedModel createBakedModel(IBakedModel existingModel)
			{
				return new ModelItemDynamicOverride(existingModel, null);
			}
		}, ImmersiveIntelligence.MODID);
		EvenMoreImmersiveModelRegistry.instance.registerCustomItemModel(new ItemStack(IIContent.itemBinoculars, 1, 1), new ImmersiveModelRegistry.ItemModelReplacement()
		{
			@Override
			public IBakedModel createBakedModel(IBakedModel existingModel)
			{
				return new ModelItemDynamicOverride(existingModel, null);
			}
		}, ImmersiveIntelligence.MODID);

		IIContent.itemMotorBelt.setRenderModels();
		Config.manual_bool.put("petroleumHere", false);
	}

	@SubscribeEvent
	public void textureStichPre(TextureStitchEvent.Pre event)
	{
		for(Map.Entry<String, IBullet> s : BulletRegistry.INSTANCE.registeredBulletItems.entrySet())
		{
			ImmersiveIntelligence.logger.info("registering sprite for bullet casing: "+s.getKey());
			s.getValue().registerSprites(event.getMap());
		}


		for(String s : IIContent.itemBulletMagazine.getSubNames())
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

		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/bullets/magazines/submachinegun/main_disp");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/bullets/magazines/assault_rifle/main_disp");

		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/binoculars/binoculars");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/binoculars/infrared_binoculars_off");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":items/binoculars/infrared_binoculars_on");

		for(Entry<String, Shrapnel> s : ShrapnelHandler.registry.entrySet())
			ApiUtils.getRegisterSprite(event.getMap(), s.getValue().texture.replace("textures/", ""));

		for(DrillHeadPerm perm : IIContent.itemDrillhead.perms)
			perm.sprite = ApiUtils.getRegisterSprite(event.getMap(), perm.texture);

		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/data_connector_feedtrough");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/data_connector");

		ParticleGasCloud.TEXTURE = ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":particle/gas");

		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubber.texture_on);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubber.texture_off);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberSplitter.texture_on);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberSplitter.texture_off);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberVertical.texture_on);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberVertical.texture_off);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberDropper.texture_on);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberDropper.texture_off);
		ApiUtils.getRegisterSprite(event.getMap(), ConveyorRubberExtract.texture_casing);

		// TODO: 22.07.2022 Make texture loading dynamic
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/metal_device/inserter/inserter_gray");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/metal_device/inserter/inserter_dim");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/metal_device/inserter/inserter");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/metal_device/inserter/advanced_inserter");

		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/metal_device/crate_inserter_upgrade");

		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/metal_device/inserter/tool_green");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/metal_device/inserter/tool_red");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/metal_device/inserter/tool_dim");
		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/metal_device/inserter/tool_gray");

		ApiUtils.getRegisterSprite(event.getMap(), ImmersiveIntelligence.MODID+":blocks/common/common_copper_cable");

	}

	@Override
	public void reInitGui()
	{
		if(ClientUtils.mc().currentScreen!=null)
			ClientUtils.mc().currentScreen.initGui();

		IIGuiList.GUI_SAWMILL.setClientGui((player, te) -> new GuiSawmill(player.inventory, (TileEntitySawmill)te));
		IIGuiList.GUI_PACKER.setClientGui((player, te) -> new GuiPacker(player.inventory, (TileEntityPacker)te));

		IIGuiList.GUI_GEARBOX.setClientGui((player, te) -> new GuiGearbox(player.inventory, (TileEntityGearbox)te));

		IIGuiList.GUI_DATA_REDSTONE_INTERFACE_DATA.setClientGui((player, te) -> new GuiDataRedstoneInterfaceData(player.inventory, (TileEntityRedstoneInterface)te));
		IIGuiList.GUI_DATA_REDSTONE_INTERFACE_REDSTONE.setClientGui((player, te) -> new GuiDataRedstoneInterfaceRedstone(player.inventory, (TileEntityRedstoneInterface)te));
		IIGuiList.GUI_PRINTING_PRESS.setClientGui((player, te) -> new GuiPrintingPress(player.inventory, (TileEntityPrintingPress)te));
		IIGuiList.GUI_CHEMICAL_BATH.setClientGui((player, te) -> new GuiChemicalBath(player.inventory, (TileEntityChemicalBath)te));
		IIGuiList.GUI_ELECTROLYZER.setClientGui((player, te) -> new GuiElectrolyzer(player.inventory, (TileEntityElectrolyzer)te));
		IIGuiList.GUI_PRECISSION_ASSEMBLER.setClientGui((player, te) -> new GuiPrecissionAssembler(player.inventory, (TileEntityPrecissionAssembler)te));
		IIGuiList.GUI_FUEL_STATION.setClientGui((player, te) -> new GuiFuelStation(player.inventory, (TileEntityFuelStation)te));
		IIGuiList.GUI_DATA_MERGER.setClientGui((player, te) -> new GuiDataMerger(player.inventory, (TileEntityDataMerger)te));

		IIGuiList.GUI_METAL_CRATE.setClientGui((player, te) -> new GuiMetalCrate(player.inventory, (TileEntityMetalCrate)te));
		IIGuiList.GUI_AMMUNITION_CRATE.setClientGui((player, te) -> new GuiAmmunitionCrate(player.inventory, (TileEntityAmmunitionCrate)te));
		IIGuiList.GUI_MEDICRATE.setClientGui((player, te) -> new GuiMedicalCrate(player.inventory, (TileEntityMedicalCrate)te));
		IIGuiList.GUI_REPAIR_CRATE.setClientGui((player, te) -> new GuiRepairCrate(player.inventory, (TileEntityRepairCrate)te));
		IIGuiList.GUI_SMALL_CRATE.setClientGui((player, te) -> new GuiSmallCrate(player.inventory, (TileEntitySmallCrate)te));
		IIGuiList.GUI_SKYCRATE_STATION.setClientGui((player, te) -> new GuiSkycrateStation(player.inventory, (TileEntitySkyCrateStation)te));
		IIGuiList.GUI_SKYCART_STATION.setClientGui((player, te) -> new GuiSkycartStation(player.inventory, (TileEntitySkyCartStation)te));
		IIGuiList.GUI_DATA_INPUT_MACHINE_STORAGE.setClientGui((player, te) -> new GuiDataInputMachineStorage(player.inventory, (TileEntityDataInputMachine)te));
		IIGuiList.GUI_DATA_INPUT_MACHINE_VARIABLES.setClientGui((player, te) -> new GuiDataInputMachineVariables(player.inventory, (TileEntityDataInputMachine)te));
		IIGuiList.GUI_DATA_INPUT_MACHINE_EDIT.setClientGui((player, te) -> new GuiDataInputMachineEdit(player.inventory, (TileEntityDataInputMachine)te));
		IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_STORAGE.setClientGui((player, te) -> new GuiArithmeticLogicMachineStorage(player.inventory, (TileEntityArithmeticLogicMachine)te));
		IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_0.setClientGui((player, te) -> new GuiArithmeticMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine)te, 0));
		IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_1.setClientGui((player, te) -> new GuiArithmeticMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine)te, 1));
		IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_2.setClientGui((player, te) -> new GuiArithmeticMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine)te, 2));
		IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_3.setClientGui((player, te) -> new GuiArithmeticMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine)te, 3));
		IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_EDIT.setClientGui((player, te) -> new GuiArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine)te));

		IIGuiList.GUI_PRINTED_PAGE_BLANK.setClientStackGui(GuiPrintedPage::new);
		IIGuiList.GUI_PRINTED_PAGE_TEXT.setClientStackGui(GuiPrintedPage::new);
		IIGuiList.GUI_PRINTED_PAGE_CODE.setClientStackGui(GuiPrintedPage::new);
		IIGuiList.GUI_PRINTED_PAGE_BLUEPRINT.setClientStackGui(GuiPrintedPage::new);

		IIGuiList.GUI_UPGRADE.setClientGui((player, te) -> new GuiUpgrade(player.inventory, ((TileEntity & IUpgradableMachine)te)));

		IIGuiList.GUI_VULCANIZER.setClientGui((player, te) -> new GuiVulcanizer(player.inventory, (TileEntityVulcanizer)te));
		IIGuiList.GUI_EMPLACEMENT_STORAGE.setClientGui((player, te) -> new GuiEmplacementPageStorage(player.inventory, (TileEntityEmplacement)te));
		IIGuiList.GUI_EMPLACEMENT_TASKS.setClientGui((player, te) -> new GuiEmplacementPageTasks(player.inventory, (TileEntityEmplacement)te));
		IIGuiList.GUI_EMPLACEMENT_STATUS.setClientGui((player, te) -> new GuiEmplacementPageStatus(player.inventory, (TileEntityEmplacement)te));

		IIGuiList.GUI_FILLER.setClientGui((player, te) -> new GuiFiller(player.inventory, (TileEntityFiller)te));
		IIGuiList.GUI_CHEMICAL_PAINTER.setClientGui((player, te) -> new GuiChemicalPainter(player.inventory, (TileEntityChemicalPainter)te));

		IIGuiList.GUI_AMMUNITION_WORKSHOP.setClientGui((player, te) -> new GuiAmmunitionWorkshop(player.inventory, (TileEntityAmmunitionWorkshop)te));
		IIGuiList.GUI_PROJECTILE_WORKSHOP.setClientGui((player, te) -> new GuiProjectileWorkshop(player.inventory, (TileEntityProjectileWorkshop)te));

	}

	@Override
	public void init()
	{
		super.init();

		ClientEventHandler handler = new ClientEventHandler();
		MinecraftForge.EVENT_BUS.register(handler);
		((IReloadableResourceManager)ClientUtils.mc().getResourceManager()).registerReloadListener(handler);

		keybind_manualReload.setKeyConflictContext(passenger_action);
		keybind_zoom.setKeyConflictContext(passenger_action);
		keybind_motorbikeEngine.setKeyConflictContext(passenger_action);
		keybind_motorbikeTowing.setKeyConflictContext(passenger_action);

		ClientRegistry.registerKeyBinding(keybind_manualReload);
		ClientRegistry.registerKeyBinding(keybind_zoom);
		ClientRegistry.registerKeyBinding(keybind_motorbikeEngine);
		ClientRegistry.registerKeyBinding(keybind_motorbikeTowing);

		ClientRegistry.registerKeyBinding(keybind_armorHelmet);
		ClientRegistry.registerKeyBinding(keybind_armorExosuit);

		ShaderUtil.init();

		TileEntityFluidInserter.conn_data = new ItemStack(IIContent.blockDataConnector, 1, IIBlockTypes_Connector.DATA_CONNECTOR.getMeta());
		TileEntityFluidInserter.conn_mv = new ItemStack(IEContent.blockConnectors, 1, BlockTypes_Connector.CONNECTOR_MV.getMeta());

		TileEntityChemicalDispenser.conn_data = new ItemStack(IIContent.blockDataConnector, 1, IIBlockTypes_Connector.DATA_CONNECTOR.getMeta());
		TileEntityChemicalDispenser.conn_mv = new ItemStack(IEContent.blockConnectors, 1, BlockTypes_Connector.CONNECTOR_MV.getMeta());

		//TODO:Advanced Fluid Inserter
		//TileEntityInserter.conn_data = new ItemStack(block_data_connector, 1, IIBlockTypes_Connector.DATA_CONNECTOR.getMeta());
		//TileEntityInserter.conn_mv = new ItemStack(IEContent.blockConnectors, 1, BlockTypes_Connector.CONNECTOR_MV.getMeta());

		for(Block block : IIContent.BLOCKS)
			if(block instanceof IColouredBlock&&((IColouredBlock)block).hasCustomBlockColours())
				ClientUtils.mc().getBlockColors().registerBlockColorHandler(IEDefaultColourHandlers.INSTANCE, block);
		for(Item item : IIContent.ITEMS)
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
		IIManualMotorworks.INSTANCE.addPages();

		ManualHelper.addEntry("chemical_bath", ManualHelper.CAT_HEAVYMACHINES,
				new ManualPageMultiblock(ManualHelper.getManual(), "chemical_bath0", MultiblockChemicalBath.instance)
		);
		ManualHelper.addEntry("precission_assembler", ManualHelper.CAT_HEAVYMACHINES,
				new ManualPageMultiblock(ManualHelper.getManual(), "precission_assembler0", MultiblockPrecissionAssembler.instance),
				new ManualPages.Text(ManualHelper.getManual(), "precission_assembler1")
		);
		ManualHelper.addEntry("electrolyzer", ManualHelper.CAT_HEAVYMACHINES,
				new ManualPageMultiblock(ManualHelper.getManual(), "electrolyzer0", MultiblockElectrolyzer.instance),
				new ManualPages.Text(ManualHelper.getManual(), "electrolyzer1")
		);
		ManualHelper.addEntry("chemical_painter", ManualHelper.CAT_HEAVYMACHINES,
				new ManualPageMultiblock(ManualHelper.getManual(), "chemical_painter0", MultiblockChemicalPainter.instance),
				new ManualPages.Text(ManualHelper.getManual(), "chemical_painter1"),
				new ManualPages.Text(ManualHelper.getManual(), "chemical_painter2"),
				new IIManualPageDataVariables(ManualHelper.getManual(), "filler", true)
						.addEntry(new DataTypeString(), 'p')
						.addEntry(new DataTypeInteger(), 'p'),
				new IIManualPageDataVariablesCallback(ManualHelper.getManual(), "filler")
						.addEntry(new DataTypeInteger(), "get_color")
						.addEntry(new DataTypeInteger(), "get_color_hex")
						.addEntry(new DataTypeInteger(), "get_ink", "get_ink_black")
						.addEntry(new DataTypeInteger(), "get_ink_cyan")
						.addEntry(new DataTypeInteger(), "get_ink_magenta")
						.addEntry(new DataTypeInteger(), "get_ink_yellow")
						.addEntry(new DataTypeInteger(), "get_energy")
		);
		ManualHelper.addEntry("filler", ManualHelper.CAT_HEAVYMACHINES,
				new ManualPageMultiblock(ManualHelper.getManual(), "filler0", MultiblockFiller.instance),
				new ManualPages.Text(ManualHelper.getManual(), "filler1")
		);

		ManualHelper.addEntry("rotary_power", ManualHelper.CAT_MACHINES,
				new ManualPages.Image(ManualHelper.getManual(), "rotary_power0", ImmersiveIntelligence.MODID+":textures/misc/rotary.png;0;0;110;64"),
				new ManualPages.Text(ManualHelper.getManual(), "rotary_power1"),
				new ManualPages.Crafting(ManualHelper.getManual(), "rotary_power2", new ItemStack(IIContent.blockMechanicalConnector)),
				new ManualPages.CraftingMulti(ManualHelper.getManual(), "rotary_power3", new ItemStack(IIContent.itemMotorBelt, 1, 0), new ItemStack(IIContent.itemMotorBelt, 1, 1), new ItemStack(IIContent.itemMotorBelt, 1, 2)),
				new ManualPages.Crafting(ManualHelper.getManual(), "rotary_power4", new ItemStack(IIContent.blockGearbox)),
				new ManualPages.Crafting(ManualHelper.getManual(), "rotary_power5", new ItemStack(IIContent.blockMechanicalDevice, 1, IIBlockTypes_MechanicalDevice.WOODEN_TRANSMISSION_BOX.getMeta()))
		);

		ManualHelper.addEntry("sawmill", ManualHelper.CAT_MACHINES,
				new ManualPageMultiblock(ManualHelper.getManual(), "sawmill0", MultiblockSawmill.instance),
				new ManualPages.Text(ManualHelper.getManual(), "sawmill1"),
				new ManualPages.Text(ManualHelper.getManual(), "sawmill2")
		);

		ManualHelper.addEntry("medicrate", ManualHelper.CAT_MACHINES,
				new ManualPages.Crafting(ManualHelper.getManual(), "medicrate0", new ItemStack(IIContent.blockMetalDevice, 1, IIBlockTypes_MetalDevice.MEDIC_CRATE.getMeta())),
				new ManualPages.Text(ManualHelper.getManual(), "medicrate1")
		);

		ManualHelper.addEntry("repair_crate", ManualHelper.CAT_MACHINES,
				new ManualPages.Crafting(ManualHelper.getManual(), "repair_crate0", new ItemStack(IIContent.blockMetalDevice, 1, IIBlockTypes_MetalDevice.REPAIR_CRATE.getMeta())),
				new ManualPages.Text(ManualHelper.getManual(), "repair_crate1")
		);

		ManualHelper.addEntry("advanced_powerpack", ManualHelper.CAT_TOOLS,
				new ManualPages.Crafting(ManualHelper.getManual(), "advanced_powerpack0", new ItemStack(IIContent.itemAdvancedPowerPack)),
				new ManualPages.Text(ManualHelper.getManual(), "advanced_powerpack1")
		);

		ManualHelper.addEntry("electric_tools", ManualHelper.CAT_TOOLS,
				new ManualPages.Crafting(ManualHelper.getManual(), "electric_tools0", new ItemStack(IIContent.itemHammer)),
				new ManualPages.Crafting(ManualHelper.getManual(), "electric_tools1", new ItemStack(IIContent.itemElectricWrench)),
				new ManualPages.Crafting(ManualHelper.getManual(), "electric_tools2", new ItemStack(IIContent.itemWirecutter))
		);

		Fluid[] lighterFluids = LighterFuelHandler.getAllowedFluids();
		ManualPages[] lighterPages = new ManualPages[1+(int)Math.ceil(lighterFluids.length/12f)];
		lighterPages[0] = new ManualPages.Crafting(ManualHelper.getManual(), "lighter0", new ItemStack(IIContent.itemLighter));

		for(int i = 0; i < lighterPages.length-1; i++)
		{
			int off = (i*12);
			int j = (lighterFluids.length-off)%12;
			String[][] tt = new String[j+1][2];

			tt[0][0] = ManualHelper.getManual().formatText("field_fuel");
			tt[0][1] = ManualHelper.getManual().formatText("field_amount");

			for(int ii = 0; ii < j; ii++)
			{
				int amount = LighterFuelHandler.getBurnQuantity(new FluidStack(lighterFluids[off+ii], 1));
				tt[ii+1][0] = lighterFluids[off+ii].getLocalizedName(new FluidStack(lighterFluids[off+ii], amount));
				tt[ii+1][1] = String.format("%d mB", amount);
			}

			lighterPages[i+1] = new Table(ManualHelper.getManual(), "lighter_fuels", tt, true);
		}

		ManualHelper.addEntry("lighter", ManualHelper.CAT_TOOLS, lighterPages);

		ClientCommandHandler.instance.registerCommand(new IICommandHandler("tmt"));


		//Weapons (Items)
		IIContent.itemMachinegun.setTileEntityItemStackRenderer(MachinegunItemStackRenderer.instance);
		IIContent.itemSubmachinegun.setTileEntityItemStackRenderer(SubmachinegunItemStackRenderer.instance);
		ItemIIWeaponUpgrade.addUpgradesToRender();

		IIContent.itemLightEngineerHelmet.setTileEntityItemStackRenderer(LightEngineerArmorItemStackRenderer.instance);
		IIContent.itemLightEngineerChestplate.setTileEntityItemStackRenderer(LightEngineerArmorItemStackRenderer.instance);
		IIContent.itemLightEngineerLeggings.setTileEntityItemStackRenderer(LightEngineerArmorItemStackRenderer.instance);
		IIContent.itemLightEngineerBoots.setTileEntityItemStackRenderer(LightEngineerArmorItemStackRenderer.instance);

		/**Render Layers*/
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		RenderPlayer render = skinMap.get("default");
		render.addLayer(new IIBipedLayerRenderer());
		render = skinMap.get("slim");
		render.addLayer(new IIBipedLayerRenderer());

		//Block / ItemStack rendering
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAmmunitionCrate.class, new AmmunitionCrateRenderer().subscribeToList("ammunition_crate"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMedicalCrate.class, new MedicalCrateRenderer().subscribeToList("medical_crate"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRepairCrate.class, new RepairCrateRenderer().subscribeToList("repair_crate"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInserter.class, new InserterRenderer().subscribeToList("inserter"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAdvancedInserter.class, new AdvancedInserterRenderer().subscribeToList("advanced_inserter"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidInserter.class, new FluidInserterRenderer().subscribeToList("fluid_inserter"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTimedBuffer.class, new TimedBufferRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRedstoneBuffer.class, new RedstoneBufferRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySmallDataBuffer.class, new SmallDataBufferRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDataMerger.class, new DataMergerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDataDebugger.class, new DataDebuggerRenderer().subscribeToList("data_debugger"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLatexCollector.class, new LatexCollectorRenderer());

		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalDevice), IIBlockTypes_MetalDevice.TIMED_BUFFER.getMeta(), TileEntityTimedBuffer.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalDevice), IIBlockTypes_MetalDevice.REDSTONE_BUFFER.getMeta(), TileEntityRedstoneBuffer.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalDevice), IIBlockTypes_MetalDevice.SMALL_DATA_BUFFER.getMeta(), TileEntitySmallDataBuffer.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalDevice), IIBlockTypes_MetalDevice.DATA_MERGER.getMeta(), TileEntityDataMerger.class);


		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMechanicalPump.class, new MechanicalPumpRenderer().subscribeToList("mechanical_pump"));

		//Data Connectors
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChemicalDispenser.class, new ChemicalDispenserRenderer().subscribeToList("chemical_dispenser"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMineSign.class, new MineSignRenderer());

		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockDataConnector), IIBlockTypes_Connector.DATA_CONNECTOR.getMeta(), TileEntityDataConnector.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockDataConnector), IIBlockTypes_Connector.DATA_RELAY.getMeta(), TileEntityDataRelay.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockDataConnector), IIBlockTypes_Connector.DATA_DUPLEX_CONNECTOR.getMeta(), TileEntityDataCallbackConnector.class);

		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockDataConnector), IIBlockTypes_Connector.INSERTER.getMeta(), TileEntityInserter.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockDataConnector), IIBlockTypes_Connector.ADVANCED_INSERTER.getMeta(), TileEntityAdvancedInserter.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockDataConnector), IIBlockTypes_Connector.FLUID_INSERTER.getMeta(), TileEntityFluidInserter.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockDataConnector), IIBlockTypes_Connector.CHEMICAL_DISPENSER.getMeta(), TileEntityChemicalDispenser.class);
		//ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_data_connector), IIBlockTypes_Connector.INSERTER.getMeta(), TileEntityInserter.class);

		//Tools
		IIContent.itemTachometer.setTileEntityItemStackRenderer(TachometerItemStackRenderer.instance);
		IIContent.itemRadioConfigurator.setTileEntityItemStackRenderer(RadioConfiguratorItemStackRenderer.instance);
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
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock0), IIBlockTypes_MetalMultiblock0.RADIO_STATION.getMeta(), TileEntityRadioStation.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDataInputMachine.class, new DataInputMachineRenderer().subscribeToList("data_input_machine"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityArithmeticLogicMachine.class, new ArithmeticLogicMachineRenderer().subscribeToList("arithmetic_logic_machine"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrintingPress.class, new PrintingPressRenderer().subscribeToList("printing_press"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock0), IIBlockTypes_MetalMultiblock0.PRINTING_PRESS.getMeta(), TileEntityPrintingPress.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChemicalBath.class, new ChemicalBathRenderer().subscribeToList("chemical_bath"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock0), IIBlockTypes_MetalMultiblock0.CHEMICAL_BATH.getMeta(), TileEntityChemicalBath.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityElectrolyzer.class, new ElectrolyzerRenderer().subscribeToList("electrolyzer"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock0), IIBlockTypes_MetalMultiblock0.ELECTROLYZER.getMeta(), TileEntityElectrolyzer.class);

		//TODO: Fix misspeling during the Split, it could break stuff
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrecissionAssembler.class, new PrecissionAssemblerRenderer().subscribeToList("precision_assembler"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock0), IIBlockTypes_MetalMultiblock0.PRECISSION_ASSEMBLER.getMeta(), TileEntityPrecissionAssembler.class);

		//Deprecated, but not for deletion, as 0.1 players would *explode*
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAmmunitionFactory.class, new AmmunitionFactoryRenderer());
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock0), IIBlockTypes_MetalMultiblock0.AMMUNITION_FACTORY.getMeta(), TileEntityAmmunitionFactory.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAmmunitionWorkshop.class, new AmmunitionWorkshopRenderer().subscribeToList("ammunition_workshop"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), IIBlockTypes_MetalMultiblock1.AMMUNITION_WORKSHOP.getMeta(), TileEntityAmmunitionWorkshop.class);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityProjectileWorkshop.class, new ProjectileWorkshopRenderer().subscribeToList("projectile_workshop"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), IIBlockTypes_MetalMultiblock1.PROJECTILE_WORKSHOP.getMeta(), TileEntityProjectileWorkshop.class);


		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySawmill.class, new SawmillRenderer().subscribeToList("sawmill"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockWoodenMultiblock), IIBlockTypes_WoodenMultiblock.SAWMILL.getMeta(), TileEntitySawmill.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityConveyorScanner.class, new ConveyorScannerRenderer().subscribeToList("conveyor_scanner"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityArtilleryHowitzer.class, new ArtilleryHowitzerRenderer().subscribeToList("artillery_howitzer"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock0), IIBlockTypes_MetalMultiblock0.ARTILLERY_HOWITZER.getMeta(), TileEntityArtilleryHowitzer.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBallisticComputer.class, new BallisticComputerRenderer().subscribeToList("ballistic_computer"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPacker.class, new PackerRenderer().subscribeToList("packer"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock0), IIBlockTypes_MetalMultiblock0.PACKER.getMeta(), TileEntityPacker.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRedstoneInterface.class, new RedstoneInterfaceRenderer().subscribeToList("redstone_data_interface"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChemicalPainter.class, new ChemicalPainterRenderer().subscribeToList("chemical_painter"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), IIBlockTypes_MetalMultiblock1.CHEMICAL_PAINTER.getMeta(), TileEntityChemicalPainter.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFiller.class, new FillerRenderer().subscribeToList("casing_filler"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEmplacement.class, new EmplacementRenderer().subscribeToList("emplacement"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), IIBlockTypes_MetalMultiblock1.EMPLACEMENT.getMeta(), TileEntityEmplacement.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRadar.class, new RadarRenderer().subscribeToList("radar"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), IIBlockTypes_MetalMultiblock1.RADAR.getMeta(), TileEntityRadar.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFlagpole.class, new FlagpoleRenderer().subscribeToList("flagpole"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), IIBlockTypes_MetalMultiblock1.FLAGPOLE.getMeta(), TileEntityFlagpole.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFuelStation.class, new FuelStationRenderer().subscribeToList("fuel_station"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), IIBlockTypes_MetalMultiblock1.FUEL_STATION.getMeta(), TileEntityFuelStation.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVehicleWorkshop.class, new VehicleWorkshopRenderer().subscribeToList("vehicle_workshop"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), IIBlockTypes_MetalMultiblock1.VEHICLE_WORKSHOP.getMeta(), TileEntityVehicleWorkshop.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVulcanizer.class, new VulcanizerRenderer().subscribeToList("vulcanizer"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), IIBlockTypes_MetalMultiblock1.VULCANIZER.getMeta(), TileEntityVulcanizer.class);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCoagulator.class, new CoagulatorRenderer().subscribeToList("coagulator"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMetalMultiblock1), IIBlockTypes_MetalMultiblock1.COAGULATOR.getMeta(), TileEntityCoagulator.class);

		mech_con_renderer = new MechanicalConnectorRenderer().subscribeToList("motor_belts");
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMechanicalConnectable.class, mech_con_renderer);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMechanicalWheel.class, new WheelRenderer().subscribeToList("wheel"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(IIContent.blockMechanicalConnector), IIBlockTypes_MechanicalConnector.WOODEN_WHEEL.getMeta(), TileEntityMechanicalWheel.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoodenFenceGate.class, new FenceGateRenderer<>());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoodenChainFenceGate.class, new FenceGateRenderer<>());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteelFenceGate.class, new FenceGateRenderer<>());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteelChainFenceGate.class, new FenceGateRenderer<>());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAluminiumFenceGate.class, new FenceGateRenderer<>());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAluminiumChainFenceGate.class, new FenceGateRenderer<>());

		reloadModels();
	}

	@Override
	public void renderTile(TileEntity te)
	{
		TileEntitySpecialRenderer<TileEntity> tesr = TileEntityRendererDispatcher.instance.getRenderer(te);
		tesr.render(te, 0, 0, 0, 0, 0, 0);
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
			if(!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty()||player.getRidingEntity() instanceof IEntityZoomProvider)
			{
				ItemStack equipped = player.getHeldItem(EnumHand.MAIN_HAND);

				if(equipped.getItem() instanceof IItemScrollable&&player.isSneaking())
				{
					IIPacketHandler.INSTANCE.sendToServer(new MessageItemScrollableSwitch(event.getDwheel() > 0));
					event.setCanceled(true);
				}
				if(player.getLowestRidingEntity() instanceof IEntityZoomProvider&&ZoomHandler.isZooming)
				{
					IEntityZoomProvider zoomProvider = (IEntityZoomProvider)player.getLowestRidingEntity();

					if(zoomProvider.getZoom().canZoom(zoomProvider.getZoomStack(), player))
					{
						float[] steps = zoomProvider.getZoom().getZoomSteps(zoomProvider.getZoomStack(), player);
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
		}
		//Rightclick
		if(event.getButton()==1)
		{
			if(ClientUtils.mc().player.getRidingEntity() instanceof EntityMachinegun)
			{
				NBTTagCompound tag = new NBTTagCompound();
				tag.setBoolean("clientMessage", true);
				tag.setBoolean("shoot", event.isButtonstate());
				IIPacketHandler.INSTANCE.sendToServer(new MessageEntityNBTSync(ClientUtils.mc().player.getRidingEntity(), tag));
			}
		}
	}

	@SubscribeEvent
	public static void guiOpen(GuiOpenEvent event)
	{
		// TODO: 26.08.2021 investigate
		if(event.getGui() instanceof GuiManual)
		{
			CustomSkinHandler.getManualPages();
		}
		else if(ClientEventHandler.lastGui instanceof GuiManual)
		{
			GuiManual gui = (GuiManual)ClientEventHandler.lastGui;
			String name = null;

			ManualInstance inst = gui.getManual();
			if(inst!=null)
			{
				ManualEntry entry = inst.getEntry(gui.getSelectedEntry());
				if(entry!=null)
				{
					IManualPage page = entry.getPages()[gui.page];
					if(page instanceof IIManualPageContributorSkin)
					{
						name = ((IIManualPageContributorSkin)page).skin.name;
					}
				}
			}
			EntityPlayer p = ClientUtils.mc().player;

			ItemStack mainItem = p.getHeldItemMainhand();
			ItemStack offItem = p.getHeldItemOffhand();

			boolean main = !mainItem.isEmpty()&&mainItem.getItem()==IEContent.itemTool&&mainItem.getItemDamage()==3;
			boolean off = !offItem.isEmpty()&&offItem.getItem()==IEContent.itemTool&&offItem.getItemDamage()==3;
			ItemStack target = main?mainItem: offItem;

			if(main||off)
			{
				IIPacketHandler.INSTANCE.sendToServer(new MessageManualClose(name==null?"": name));

				if(name==null&&ItemNBTHelper.hasKey(target, "lastSkin"))
				{
					ItemNBTHelper.remove(target, "lastSkin");
				}
				else if(name!=null)
				{
					ItemNBTHelper.setString(target, "lastSkin", name);
				}
			}
		}

		ClientEventHandler.lastGui = event.getGui();
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