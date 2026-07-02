package pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.DataVariable;
import pl.pabilo8.immersiveintelligence.api.data.IDataMachineGui;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 03.08.2025
 * @ii-approved 0.3.1
 * @since 30.06.2019
 */
@DecoTemplate(name = "arithmetic_logic_machine", category = DecoGuiCategory.DATA_TILE)
public class GuiArithmeticLogicMachine extends DecoTileGui<TileEntityArithmeticLogicMachine, ContainerArithmeticLogicMachine> implements IDataMachineGui
{
	@SyncNBT(name = "page", events = SyncEvents.TILE_CLIENT_MESSAGE)
	public int editedCircuit = 0;
	@SyncNBT
	public int scroll = 0;
	public DataPacket expressions = new DataPacket();
	@SyncNBT
	public DataVariable variableToEdit = new DataVariable('a', new DataTypeNull());
	private boolean isStorage;
	private int displayedCircuit = 0;

	private GuiArithmeticLogicMachine(EntityPlayer player, TileEntityArithmeticLogicMachine tile, IIGUI gui)
	{
		super(player, tile, gui);
		isStorage = gui==IIGUI.ARITHMETIC_LOGIC_MACHINE_STORAGE;
	}

	public static GuiArithmeticLogicMachine getStorageGui(EntityPlayer player, TileEntity te)
	{
		return new GuiArithmeticLogicMachine(player, (TileEntityArithmeticLogicMachine)te, IIGUI.ARITHMETIC_LOGIC_MACHINE_STORAGE);
	}

	public static GuiArithmeticLogicMachine getVariablesGui(EntityPlayer player, TileEntity te)
	{
		return new GuiArithmeticLogicMachine(player, (TileEntityArithmeticLogicMachine)te, IIGUI.ARITHMETIC_LOGIC_MACHINE_VARIABLES);
	}


	@Override
	public void onInit()
	{
		syncAnimatedParts(tile.door, !isStorage);
		syncAnimatedParts(tile.drawer, isStorage);
		syncAnimatedParts(tile.keyboard, false);

		//Refresh page when circuit slots change
		for(Slot circuitSlot : container.circuitSlots)
			addValueListener(() -> circuitSlot.getStack().toString())
					.addObserver(stack -> refreshGUI());

		//Build background
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 176, 128+8)
				.withTitleBar(tile)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 128+8, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()

				.withNextLayer()
				.conditionally(isStorage, b -> b
						//Circuit slots background box
						.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 0, 8, 32, 120)
						.withInventorySlots(SlotStyle.IE, container.circuitSlots)
						//Energy bar background box
						.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_ROUND, 128-32+16+32, 8, 32, 120)
						//Circuit storage slots
						.withInventorySlots(SlotStyle.VANILLA, container.storageSlots)
				)
				.conditionally(!isStorage, b -> b
						.withBox(DecoTextures.BG_PAPER, DecoTextures.TEMPLATE_PAPER, 4+2, 8, 170-16, 16)
				)
				.build();

		//"Storage" label and energy bar
		if(isStorage)
		{
			addLabel(IIReference.GUI_LABEL_KEY+"arithmetic_logic_machine.storage", 0, 8+4)
					.withSize(xSize, 11)
					.withAlign(DecoAlignment.TOP);
			addComponent(new DecoBar(128+32-8+2, 24)
					.withHeight(95)
					.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage))
			);
		}
		else if(tile.inventory.size() > editedCircuit)
		{
			displayedCircuit = editedCircuit;
			ItemStack stack = tile.inventory.get(displayedCircuit);
			this.expressions = IIContent.itemCircuit.getStoredData(stack);

			addLabel(stack.getDisplayName(), 4+2+16, 8)
					.withTextColor(DecoColors.H2)
					.withSize(170-16-16, 16)
					.withAlign(DecoAlignment.CENTER);
			addComponent(new DecoButton(4+2, 8)
					.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
					.withBackgroundColor(IIColor.fromPackedRGB(0xb37f46))
					.withIcon(null, 16)
					.withText(String.valueOf(editedCircuit))
					.withIconAlignment(DecoAlignment.CENTER)
					.withSize(16, 16)
			);

			addComponent(
					new DecoList<DataVariable>(4+1, 8+16+2)
							.withSize(136+32-3, 120-16)
							.withEntries(expressions.getAllVariables())
							.withCreateLaterAction(this::addVariable)
							.withGuiSaveAction(gui -> this.scroll = gui.getScroll())
							//Display
							.withDisplayFunction(new DecoEntryPanelBuilder<DataVariable>()
									.withBackground(DecoTextures.BG_PAPER)
									.withBackgroundMask(DecoTextures.TEMPLATE_TICKET)

									//Duplicate / Edit / Remove Buttons
									.withComponent(p -> new DecoButton(p.width-17-16-14+3, 2)
											.withTemplate(DecoTemplates.ACTION_BUTTON_DUPLICATE)
											.withOnLMBPressed(() -> {
												DataVariable current = p.getCurrentElement();
												char name = findNextFreeVariableName();
												if(name=='\0')
													return;
												editVariable(name, current.getValue().clone());
											})
									)
									.withComponent(p -> new DecoButton(p.width-17-16+3, 2)
											.withTemplate(DecoTemplates.ACTION_BUTTON_EDIT)
											.withOnLMBPressed(() -> {
												editVariable(p.getCurrentElement());
												expressions.remove(p.getCurrentElement().getName());
											})
									)
									.withComponent(p -> new DecoButton(p.width-17+1, 2)
											.withTemplate(DecoTemplates.ACTION_BUTTON_REMOVE)
											.withOnLMBPressed(() -> {
												DataVariable current = p.getCurrentElement();
												p.getCurrentList().removeEntry(current);
												expressions.remove(current.getName());
												IIPacketHandler.sendToServer(new MessageIITileSync(tile, onSaveTileData()));
											})
									)
									//Type Icon, Label, and Letter
									.withComponent("image", new DecoImage(2+12, 1)
											.withSize(16, 16))
									.withLabel("typeLabel",
											new DecoLabel(fontRenderer, 2+12+16+2-1, -1)
													.withSize(48, 16)
													.withAlign(DecoAlignment.LEFT)
													.withText("Integer")
									)
									.withLabel("letterLabel",
											new DecoLabel(fontRenderer, 2, 2)
													.withSize(12, 16)
													.withAlign(DecoAlignment.CENTER)
									)
									.withElementApplyMethod((entry, panel) -> {
										TypeMetaInfo<?> typeMeta = entry.getValue().getTypeMeta();

										//letter label (f.e. a)
										panel.label("letterLabel")
												.withRawText(String.valueOf(entry.getName()));
										//type label (f.e. integer)
										panel.label("typeLabel")
												.withText(typeMeta.getTranslatedName())
												.withTextColor(typeMeta.color.withBrightness(0.4f));
										//type icon
										panel.component("image", DecoImage.class)
												.withImageLocation(entry.getValue().getTextureLocation(), true);
									})
							)
							.withScroll(scroll)
			);
		}

		//Storage page tab
		addLinkTab(IIGUI.ARITHMETIC_LOGIC_MACHINE_STORAGE, DecoTextures.ICON_STORAGE, "storage_module");

		if(tile.isUpgradeInstalled(IIContent.UPGRADE_MEMORY))
			addLinkTab(IIGUI.ARITHMETIC_LOGIC_MACHINE_MEMORY_IN, DecoTextures.ICON_MEMORY, "memory_in_module");

		//Circuit tabs
		NonNullList<ItemStack> inventory = tile.inventory;
		for(int i = 0, inventorySize = inventory.size(); i < inventorySize; i++)
		{
			ItemStack circuit = inventory.get(i);
			if(!circuit.isEmpty()&&circuit.getItem() instanceof ItemIIFunctionalCircuit)
			{
				int circuitIndex = i;
				addComponent(new DecoTab()
						.withOnPressed((gui, button, mouseX, mouseY) -> {
							this.editedCircuit = circuitIndex;
							return changeGUI(IIGUI.ARITHMETIC_LOGIC_MACHINE_VARIABLES);
						})
						.withIcon(circuit)
						.withTranslatedTooltip(circuit.getDisplayName(),
								TextFormatting.GRAY.toString()+TextFormatting.ITALIC+I18n.format(IIReference.DESCRIPTION_KEY+"variables_module"))
				);
			}
		}

		if(tile.isUpgradeInstalled(IIContent.UPGRADE_MEMORY))
			addLinkTab(IIGUI.ARITHMETIC_LOGIC_MACHINE_MEMORY_OUT, DecoTextures.ICON_MEMORY, "memory_out_module");
	}

	@Override
	public void onGuiClosed()
	{
		if(!changeGUIFlag)
		{
			syncAnimatedParts(tile.door, false);
			syncAnimatedParts(tile.drawer, false);
			syncAnimatedParts(tile.keyboard, false);
		}
		super.onGuiClosed();
	}

	private DataPacket getCircuitPacket()
	{
		if(tile.inventory.size() <= displayedCircuit)
			return new DataPacket();

		ItemStack stack = tile.inventory.get(displayedCircuit);
		return IIContent.itemCircuit.getStoredData(stack);
	}

	private void updateLocalCircuitStack(int circuit, DataPacket packet)
	{
		if(tile.getInventory().size() <= circuit)
			return;
		ItemStack stack = tile.getInventory().get(circuit);
		if(stack.isEmpty()||!(stack.getItem() instanceof ItemIIFunctionalCircuit))
			return;
		((ItemIIFunctionalCircuit)stack.getItem()).writeDataToItem(stack, packet);
		tile.getInventory().set(circuit, stack);
	}

	private NBTTagCompound getExpressionsTag(int circuit, DataPacket packet)
	{
		return EasyNBT.newNBT()
				.withInt("page", circuit)
				.withSerializable("list", packet)
				.unwrap();
	}

	private char findNextFreeVariableName()
	{
		if(expressions.size() >= DataPacket.VARIABLE_NAMES.length)
			return '\0';
		return IIUtils.cycleDataPacketCharsAvoiding('0', true, false, expressions);
	}

	private void addVariable()
	{
		char name = findNextFreeVariableName();
		if(name=='\0')
			return;
		editVariable(name, new DataTypeExpression());
	}

	private void editExistingVariable(DataVariable variable)
	{
		expressions.remove(variable.getName());
		this.variableToEdit = variable;
		changeGUI(IIGUI.ARITHMETIC_LOGIC_MACHINE_EDIT);
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		EasyNBT nbt = super.onSaveTileData();
		if(isStorage||tile.inventory.size() <= displayedCircuit)
			return nbt;

		updateLocalCircuitStack(displayedCircuit, expressions);
		return nbt.withTag("expressions", getExpressionsTag(displayedCircuit, expressions));
	}

	@Override
	public void editVariable(char name, DataType initialValue)
	{
		if(!expressions.has(name)||expressions.get(name).getClass()!=initialValue.getClass())
			expressions.set(name, initialValue);

		this.variableToEdit = new DataVariable(name, initialValue.clone());
		changeGUI(IIGUI.ARITHMETIC_LOGIC_MACHINE_EDIT);
	}
}
