package pl.pabilo8.immersiveintelligence.client;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.immersiveengineering.api.energy.wires.WireApi;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.IECustomStateMapper;
import blusunrize.immersiveengineering.client.models.obj.IEOBJLoader;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IIEMetaBlock;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_Connector;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
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
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.IItemScrollable;
import pl.pabilo8.immersiveintelligence.client.gui.GuiAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.client.gui.GuiMetalCrate;
import pl.pabilo8.immersiveintelligence.client.gui.GuiPrintedPage;
import pl.pabilo8.immersiveintelligence.client.gui.GuiPrintingPress;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticLogicMachineEdit;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticLogicMachineStorage;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticMachineVariables;
import pl.pabilo8.immersiveintelligence.client.gui.data_input_machine.GuiDataInputMachineEdit;
import pl.pabilo8.immersiveintelligence.client.gui.data_input_machine.GuiDataInputMachineStorage;
import pl.pabilo8.immersiveintelligence.client.gui.data_input_machine.GuiDataInputMachineVariables;
import pl.pabilo8.immersiveintelligence.client.render.*;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIFluid;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.MultiblockSkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySkyCrateStation.TileEntitySkyCrateStationParent;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.entity.EntitySkyCrate;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIPrintedPage;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageItemScrollableSwitch;
import pl.pabilo8.immersiveintelligence.common.util.SkyCrateSound;

import javax.annotation.Nonnull;
import java.util.Locale;

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

		for(Block block : CommonProxy.blocks)
		{
			final ResourceLocation loc = Block.REGISTRY.getNameForObject(block);
			Item blockItem = Item.getItemFromBlock(block);
			if(blockItem==null)
				throw new RuntimeException("ITEMBLOCK FOR "+loc+" : "+block+" IS NULL");
			if(block instanceof BlockIIFluid)
			{
				ImmersiveIntelligence.logger.info("fluid model");
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
					ImmersiveIntelligence.logger.info(b.tesrMap);
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
					//Currently supports only 1-meta items
					//TODO: EXPAND
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
		OBJLoader.INSTANCE.addDomain(ImmersiveIntelligence.MODID);
		IEOBJLoader.instance.addDomain(ImmersiveIntelligence.MODID);
		//None of your trickery is present here...
		//And yes, I would like to have .obj models, really...
		//But using TMT seems easier (and more weird).
		RenderingRegistry.registerEntityRenderingHandler(EntitySkyCrate.class, SkyCrateRenderer::new);

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
		TileEntityInserter.conn_data = new ItemStack(block_data_connector, 1, IIBlockTypes_Connector.DATA_CONNECTOR.getMeta());
		TileEntityInserter.conn_mv = new ItemStack(IEContent.blockConnectors, 1, BlockTypes_Connector.CONNECTOR_MV.getMeta());
	}

	@Override
	public void postInit()
	{
		super.postInit();
		ImmersiveIntelligence.logger.info("Registering II Manual Pages.");

		//Data

		ManualHelper.addEntry("data_main", CAT_DATA,
				new ManualPages.Text(ManualHelper.getManual(), "data_main0"),
				new ManualPages.Text(ManualHelper.getManual(), "data_main1")
		);

		String[][] intInfoTable = {{"def_value", "0"}, {"min_value", "-8192"}, {"max_value", "8192"}};
		String[][] stringInfoTable = {{"def_value", "\'\'"}, {"max_length", "512"}};
		String[][] boolInfoTable = {{"def_value", "false"}, {"accepted_values", "tf"}};
		String[][] itemstackInfoTable = {{"def_value", "empty"}};
		String[][] arrayInfoTable = {{"def_value", "empty"}, {"min_index", "0"}, {"max_index", "255"}};
		String[][] pairInfoTable = {{"def_value", "empty"}};
		String[][] rangeInfoTable = {{"def_value", "0-0"}};

		ManualHelper.addEntry("data_variable_types", CAT_DATA,
				new ManualPages.Text(ManualHelper.getManual(), "data_variable_types"),
				new ManualPages.Text(ManualHelper.getManual(), "data_variable_types_null"),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_int", intInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_string", stringInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_bool", boolInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_itemstack", itemstackInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_array", arrayInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_pair", pairInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_range", rangeInfoTable, true)
		);

		ManualHelper.addEntry("radio_station", CAT_DATA,
				new ManualPageMultiblock(ManualHelper.getManual(), "radio_station0", MultiblockRadioStation.instance),
				new ManualPages.Text(ManualHelper.getManual(), "radio_station1")
		);


		ManualHelper.addEntry("skycrate_station", CAT_LOGISTICS,
				new ManualPageMultiblock(ManualHelper.getManual(), "logi_skycrate0", MultiblockSkyCrateStation.instance),
				new ManualPages.Text(ManualHelper.getManual(), "logi_skycrate1")
		);

		//Warfare

		ManualHelper.addEntry("warfare_main", CAT_WARFARE,
				new ManualPages.Text(ManualHelper.getManual(), "warfare_main0")
		);

		//Logistics

		ManualHelper.addEntry("logi_main", CAT_LOGISTICS,
				new ManualPages.Text(ManualHelper.getManual(), "logi_main0")
		);

		//Intelligence

		ManualHelper.addEntry("intel_main", CAT_INTELLIGENCE,
				new ManualPages.Text(ManualHelper.getManual(), "intel_main0")
		);

		//Ammo Crate
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAmmunitionCrate.class, new AmmunitionCrateRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInserter.class, new InserterRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTimedBuffer.class, new TimedBufferRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRedstoneBuffer.class, new RedstoneBufferRenderer());

		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_metal_device), IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta(), TileEntityAmmunitionCrate.class);


		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_metal_device), IIBlockTypes_MetalDevice.TIMED_BUFFER.getMeta(), TileEntityTimedBuffer.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_metal_device), IIBlockTypes_MetalDevice.REDSTONE_BUFFER.getMeta(), TileEntityRedstoneBuffer.class);

		//Data Connectors
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDataConnector.class, new DataConnectorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDataRelay.class, new DataRelayRenderer());

		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_data_connector), IIBlockTypes_Connector.DATA_CONNECTOR.getMeta(), TileEntityDataConnector.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_data_connector), IIBlockTypes_Connector.DATA_RELAY.getMeta(), TileEntityDataRelay.class);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_data_connector), IIBlockTypes_Connector.INSERTER.getMeta(), TileEntityInserter.class);

		//Alarm Siren
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAlarmSiren.class, new AlarmSirenRenderer());
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block_data_connector), IIBlockTypes_Connector.ALARM_SIREN.getMeta(), TileEntityAlarmSiren.class);

		//Multiblocks
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkyCrateStationParent.class, new SkyCrateStationRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRadioStation.class, new RadioStationRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDataInputMachine.class, new DataInputMachineRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityArithmeticLogicMachine.class, new ArithmeticLogicMachineRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrintingPress.class, new PrintingPressRenderer());

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
		Minecraft.getMinecraft().getSoundHandler().playSound(new SkyCrateSound(hook,
				new ResourceLocation(ImmersiveEngineering.MODID, "skyhook")));
	}

	@SubscribeEvent
	public void onMouseEvent(MouseEvent event)
	{
		if(event.getDwheel()!=0)
		{
			EntityPlayer player = ClientUtils.mc().player;
			if(!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty()&&player.isSneaking())
			{
				ItemStack equipped = player.getHeldItem(EnumHand.MAIN_HAND);

				if(equipped.getItem() instanceof IItemScrollable)
				{
					IIPacketHandler.INSTANCE.sendToServer(new MessageItemScrollableSwitch(event.getDwheel() > 0));
					event.setCanceled(true);
				}
			}
		}
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