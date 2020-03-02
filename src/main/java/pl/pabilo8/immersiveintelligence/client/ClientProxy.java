package pl.pabilo8.immersiveintelligence.client;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.ApiUtils;
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
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
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
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
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
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCasingType;
import pl.pabilo8.immersiveintelligence.api.utils.IItemScrollable;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleGunfire;
import pl.pabilo8.immersiveintelligence.client.gui.*;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticLogicMachineEdit;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticLogicMachineStorage;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticMachineVariables;
import pl.pabilo8.immersiveintelligence.client.gui.data_input_machine.GuiDataInputMachineEdit;
import pl.pabilo8.immersiveintelligence.client.gui.data_input_machine.GuiDataInputMachineStorage;
import pl.pabilo8.immersiveintelligence.client.gui.data_input_machine.GuiDataInputMachineVariables;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualDataAndElectronics;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualIntelligence;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualLogistics;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualWarfare;
import pl.pabilo8.immersiveintelligence.client.render.*;
import pl.pabilo8.immersiveintelligence.client.render.mechanical_device.WheelRenderer;
import pl.pabilo8.immersiveintelligence.client.render.metal_device.*;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.metal.*;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.wooden.SkyCratePostRenderer;
import pl.pabilo8.immersiveintelligence.client.render.multiblock.wooden.SkyCrateStationRenderer;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIFluid;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityRedstoneInterface;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySkyCratePost;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.TileEntityGearbox;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.TileEntityMechanicalConnectable;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.TileEntityMechanicalWheel;
import pl.pabilo8.immersiveintelligence.common.blocks.stone.TileEntitySandbags;
import pl.pabilo8.immersiveintelligence.common.blocks.types.*;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.entity.EntitySkyCrate;
import pl.pabilo8.immersiveintelligence.common.entity.EntitySkycrateInternal;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityShrapnel;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIPrintedPage;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIWeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageItemScrollableSwitch;
import pl.pabilo8.immersiveintelligence.common.network.MessageMachinegunSync;
import pl.pabilo8.immersiveintelligence.common.util.SkyCrateSound;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by Pabilo8 on 2019-05-07.
 */
@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ImmersiveIntelligence.MODID)
public class ClientProxy extends CommonProxy
{
	public static final String CAT_DATA = "ii_data";
	public static final String CAT_WARFARE = "ii_warfare";
	public static final String CAT_LOGISTICS = "ii_logi";
	public static final String CAT_INTELLIGENCE = "ii_intel";

	public static KeyBinding keybind_machinegunScope = new KeyBinding("key."+ImmersiveIntelligence.MODID+".mgScope", Keyboard.KEY_Z, "key.categories.gameplay");
	public static MechanicalConnectorRenderer mech_con_renderer;
	public NBTTagCompound storedGuiData = new NBTTagCompound();

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent evt)
	{

		//You've tricked me
		//I thought the connector rendering is based on something different, but actually it renders the obj connector model with the wire
		WireApi.registerConnectorForRender("conn_data", new ResourceLocation(ImmersiveIntelligence.MODID+":block/empty.obj"), null);
		WireApi.registerConnectorForRender("rel_data", new ResourceLocation(ImmersiveIntelligence.MODID+":block/empty.obj"), null);
		WireApi.registerConnectorForRender("alarm_siren", new ResourceLocation(ImmersiveIntelligence.MODID+":block/empty.obj"), null);

		WireApi.registerConnectorForRender("inserter", new ResourceLocation(ImmersiveIntelligence.MODID+":block/empty.obj"), null);
		WireApi.registerConnectorForRender("advanced_inserter", new ResourceLocation(ImmersiveIntelligence.MODID+":block/empty.obj"), null);
		WireApi.registerConnectorForRender("fluid_inserter", new ResourceLocation(ImmersiveIntelligence.MODID+":block/empty.obj"), null);
		WireApi.registerConnectorForRender("advanced_fluid_inserter", new ResourceLocation(ImmersiveIntelligence.MODID+":block/empty.obj"), null);
		WireApi.registerConnectorForRender("chemical_dispenser", new ResourceLocation(ImmersiveIntelligence.MODID+":block/empty.obj"), null);

		WireApi.registerConnectorForRender("redstone_data_interface", new ResourceLocation(ImmersiveIntelligence.MODID+":block/empty.obj"), null);

		WireApi.registerConnectorForRender("skycrate_station", new ResourceLocation(ImmersiveIntelligence.MODID+":block/empty.obj"), null);
		WireApi.registerConnectorForRender("skycrate_post", new ResourceLocation(ImmersiveIntelligence.MODID+":block/empty.obj"), null);

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
						ModelLoader.setCustomMeshDefinition(ieMetaItem, new ItemMeshDefinition()
						{
							@Override
							public ModelResourceLocation getModelLocation(ItemStack stack)
							{
								return new ModelResourceLocation(loc, "inventory");
							}
						});
					}
				}
			}
			else
			{
				final ResourceLocation loc = Item.REGISTRY.getNameForObject(item);
				ModelBakery.registerItemVariants(item, loc);
				ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition()
				{
					@Override
					public ModelResourceLocation getModelLocation(ItemStack stack)
					{
						return new ModelResourceLocation(loc, "inventory");
					}
				});
			}
		}

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

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		ItemStack stack = player.getHeldItemMainhand();
		ItemStack stack2 = player.getHeldItemOffhand();
		if(te instanceof IGuiTile)
		{

			Object gui = null;
			if(ID==IIGuiList.GUI_METAL_CRATE&&te instanceof TileEntityMetalCrate)
				gui = new GuiMetalCrate(player.inventory, (TileEntityMetalCrate)te);
			else if(ID==IIGuiList.GUI_AMMUNITION_CRATE&&te instanceof TileEntityAmmunitionCrate)
				gui = new GuiAmmunitionCrate(player.inventory, (TileEntityAmmunitionCrate)te);
			else if(ID==IIGuiList.GUI_SMALL_CRATE&&te instanceof TileEntitySmallCrate)
				gui = new GuiSmallCrate(player.inventory, (TileEntitySmallCrate)te);

			else if(ID==IIGuiList.GUI_DATA_INPUT_MACHINE_STORAGE&&te instanceof TileEntityDataInputMachine)
				gui = new GuiDataInputMachineStorage(player.inventory, (TileEntityDataInputMachine)te);
			else if(ID==IIGuiList.GUI_DATA_INPUT_MACHINE_VARIABLES&&te instanceof TileEntityDataInputMachine)
				gui = new GuiDataInputMachineVariables(player.inventory, (TileEntityDataInputMachine)te);
			else if(ID==IIGuiList.GUI_DATA_INPUT_MACHINE_EDIT&&te instanceof TileEntityDataInputMachine)
				gui = new GuiDataInputMachineEdit(player.inventory, (TileEntityDataInputMachine)te);

			else if(ID==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_STORAGE&&te instanceof TileEntityArithmeticLogicMachine)
				gui = new GuiArithmeticLogicMachineStorage(player.inventory, (TileEntityArithmeticLogicMachine)te);
			else if(ID==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_1&&te instanceof TileEntityArithmeticLogicMachine)
				gui = new GuiArithmeticMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine)te, 0);
			else if(ID==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_2&&te instanceof TileEntityArithmeticLogicMachine)
				gui = new GuiArithmeticMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine)te, 1);
			else if(ID==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_3&&te instanceof TileEntityArithmeticLogicMachine)
				gui = new GuiArithmeticMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine)te, 2);
			else if(ID==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_4&&te instanceof TileEntityArithmeticLogicMachine)
				gui = new GuiArithmeticMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine)te, 3);

			else if(ID==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_1&&te instanceof TileEntityArithmeticLogicMachine)
				gui = new GuiArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine)te, 0);
			else if(ID==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_2&&te instanceof TileEntityArithmeticLogicMachine)
				gui = new GuiArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine)te, 1);
			else if(ID==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_3&&te instanceof TileEntityArithmeticLogicMachine)
				gui = new GuiArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine)te, 2);
			else if(ID==IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_4&&te instanceof TileEntityArithmeticLogicMachine)
				gui = new GuiArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine)te, 3);

			else if(ID==IIGuiList.GUI_PRINTING_PRESS&&te instanceof TileEntityPrintingPress)
				gui = new GuiPrintingPress(player.inventory, (TileEntityPrintingPress)te);
			else if(ID==IIGuiList.GUI_CHEMICAL_BATH&&te instanceof TileEntityChemicalBath)
				gui = new GuiChemicalBath(player.inventory, (TileEntityChemicalBath)te);
			else if(ID==IIGuiList.GUI_ELECTROLYZER&&te instanceof TileEntityElectrolyzer)
				gui = new GuiElectrolyzer(player.inventory, (TileEntityElectrolyzer)te);
			else if(ID==IIGuiList.GUI_PRECISSION_ASSEMBLER&&te instanceof TileEntityPrecissionAssembler)
				gui = new GuiPrecissionAssembler(player.inventory, (TileEntityPrecissionAssembler)te);
			else if(ID==IIGuiList.GUI_AMMUNITION_FACTORY&&te instanceof TileEntityAmmunitionFactory)
				gui = new GuiAmmunitionFactory(player.inventory, (TileEntityAmmunitionFactory)te);

			else if(ID==IIGuiList.GUI_PACKER&&te instanceof TileEntityPacker)
				gui = new GuiPacker(player.inventory, (TileEntityPacker)te);
			/*else if(ID==IIGuiList.GUI_UNPACKER&&te instanceof TileEntityUnpacker)
				gui = new GuiUnpacker(player.inventory, (TileEntityUnpacker)te);*/

			else if(ID==IIGuiList.GUI_DATA_MERGER&&te instanceof TileEntityDataMerger)
				gui = new GuiDataMerger(player.inventory, (TileEntityDataMerger)te);
			else if(ID==IIGuiList.GUI_DATA_REDSTONE_INTERFACE_DATA&&te instanceof TileEntityRedstoneInterface)
				gui = new GuiDataRedstoneInterfaceData(player.inventory, (TileEntityRedstoneInterface)te);
			else if(ID==IIGuiList.GUI_DATA_REDSTONE_INTERFACE_REDSTONE&&te instanceof TileEntityRedstoneInterface)
				gui = new GuiDataRedstoneInterfaceRedstone(player.inventory, (TileEntityRedstoneInterface)te);

			else if(ID==IIGuiList.GUI_SKYCRATE_STATION&&te instanceof TileEntitySkyCrateStation)
				gui = new GuiSkycrateStation(player.inventory, (TileEntitySkyCrateStation)te);
			else if(ID==IIGuiList.GUI_GEARBOX&&te instanceof TileEntityGearbox)
				gui = new GuiGearbox(player.inventory, (TileEntityGearbox)te);

			((IGuiTile)te).onGuiOpened(player, true);
			return gui;
		}
		else
		{
			if((stack.getItem() instanceof ItemIIPrintedPage||stack2.getItem() instanceof ItemIIPrintedPage)&&ID==IIGuiList.GUI_PRINTED_PAGE_TEXT)
			{
				return new GuiPrintedPage(player, (stack.getItem() instanceof ItemIIPrintedPage)?stack: stack2);
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
		//Thanks Blu!
		RenderingRegistry.registerEntityRenderingHandler(EntitySkycrateInternal.class, EntityRenderNone::new);

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

		keybind_machinegunScope.setKeyConflictContext(KeyConflictContext.IN_GAME);
		ClientRegistry.registerKeyBinding(keybind_machinegunScope);

		ShaderUtil.init();

		TileEntityInserter.conn_data = new ItemStack(block_data_connector, 1, IIBlockTypes_Connector.DATA_CONNECTOR.getMeta());
		TileEntityInserter.conn_mv = new ItemStack(IEContent.blockConnectors, 1, BlockTypes_Connector.CONNECTOR_MV.getMeta());

		TileEntityAdvancedInserter.conn_data = new ItemStack(block_data_connector, 1, IIBlockTypes_Connector.DATA_CONNECTOR.getMeta());
		TileEntityAdvancedInserter.conn_mv = new ItemStack(IEContent.blockConnectors, 1, BlockTypes_Connector.CONNECTOR_MV.getMeta());

		TileEntityFluidInserter.conn_data = new ItemStack(block_data_connector, 1, IIBlockTypes_Connector.DATA_CONNECTOR.getMeta());
		TileEntityFluidInserter.conn_mv = new ItemStack(IEContent.blockConnectors, 1, BlockTypes_Connector.CONNECTOR_MV.getMeta());

		TileEntityChemicalDispenser.conn_data = new ItemStack(block_data_connector, 1, IIBlockTypes_Connector.DATA_CONNECTOR.getMeta());
		TileEntityChemicalDispenser.conn_mv = new ItemStack(IEContent.blockConnectors, 1, BlockTypes_Connector.CONNECTOR_MV.getMeta());

		TileEntitySkyCrateStation.gear_iron = new ItemStack(item_motor_gear, 1, 2);

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

		//Weapons (Items)
		item_machinegun.setTileEntityItemStackRenderer(MachinegunItemStackRenderer.instance);
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

		//Small Crate
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySmallCrate.class, new SmallCrateRenderer());
		Item.getItemFromBlock(block_small_crate).setTileEntityItemStackRenderer(SmallCrateItemStackRenderer.instance);

		item_tachometer.setTileEntityItemStackRenderer(TachometerItemStackRenderer.instance);

		//Multiblocks
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkyCrateStation.class, new SkyCrateStationRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkyCratePost.class, new SkyCratePostRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRadioStation.class, new RadioStationRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDataInputMachine.class, new DataInputMachineRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityArithmeticLogicMachine.class, new ArithmeticLogicMachineRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrintingPress.class, new PrintingPressRenderer());

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChemicalBath.class, new ChemicalBathRenderer());
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_metal_multiblock0), IIBlockTypes_MetalMultiblock0.CHEMICAL_BATH.getMeta(), TileEntityChemicalBath.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityElectrolyzer.class, new ElectrolyzerRenderer());
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_metal_multiblock0), IIBlockTypes_MetalMultiblock0.ELECTROLYZER.getMeta(), TileEntityElectrolyzer.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrecissionAssembler.class, new PrecissionAssemblerRenderer());
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_metal_multiblock0), IIBlockTypes_MetalMultiblock0.PRECISSION_ASSEMBLER.getMeta(), TileEntityPrecissionAssembler.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAmmunitionFactory.class, new AmmunitionFactoryRenderer());
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_metal_multiblock0), IIBlockTypes_MetalMultiblock0.AMMUNITION_FACTORY.getMeta(), TileEntityAmmunitionFactory.class);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityConveyorScanner.class, new ConveyorScannerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityArtilleryHowitzer.class, new ArtilleryHowitzerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBallisticComputer.class, new BallisticComputerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPacker.class, new PackerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRedstoneInterface.class, new RedstoneInterfaceRenderer());

		mech_con_renderer = new MechanicalConnectorRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMechanicalConnectable.class, mech_con_renderer);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMechanicalWheel.class, new WheelRenderer());
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_mechanical_connector), IIBlockTypes_MechanicalConnector.WOODEN_WHEEL.getMeta(), TileEntityMechanicalWheel.class);

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
			IIPacketHandler.INSTANCE.sendToServer(new MessageMachinegunSync(ClientUtils.mc().player.getRidingEntity(), tag));
		}
	}

	@Override
	public void spawnGunfireFX(World world, double x, double y, double z, double mx, double my, double mz, float size)
	{
		Particle particle = new ParticleGunfire(world, x, y, z, mx, my, mz, size);
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);
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