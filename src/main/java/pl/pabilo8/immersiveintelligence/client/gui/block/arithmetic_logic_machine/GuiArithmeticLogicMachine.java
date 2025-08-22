package pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.DataVariable;
import pl.pabilo8.immersiveintelligence.api.data.IDataMachineGui;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
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
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 03.08.2025
 * @ii-approved 0.3.1
 * @since 30.06.2019
 */
@DecoTemplate(name = "arithmetic_logic_machine", category = DecoGuiCategory.DATA_TILE)
public class GuiArithmeticLogicMachine extends DecoGui<TileEntityArithmeticLogicMachine, ContainerArithmeticLogicMachine> implements IDataMachineGui
{
	@DecoResource
	public static ResourceLocation ICON_STORAGE = ResLoc.of(IIReference.RES_II, "gui/tab_icons/storage");
	@DecoResource
	public static ResourceLocation ICON_MEMORY = ResLoc.of(IIReference.RES_II, "gui/tab_icons/memory");

	@SyncNBT
	public int editedCircuit = 0;
	@SyncNBT
	public int scroll = 0;
	@SyncNBT
	public DataVariable variableToEdit = new DataVariable('a', new DataTypeNull());

	@Nullable
	protected DecoList<DataVariable> list;
	boolean isStorage;

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
		syncAnimatedParts(tile.door, true);
		syncAnimatedParts(tile.drawer, true);
		syncAnimatedParts(tile.keyboard, false);

		//Refresh page when circuit slots change
		for(Slot circuitSlot : container.circuitSlots)
			addValueListener(() -> circuitSlot.getStack().toString())
					.addObserver(stack -> refreshGUI());

		//Build background
		startBackground()
				.withBox(DecoTextures.GUI_BG_STEEL, 0, 0, 176, 128+8)
				.withTitleBar(tile)
				.withBox(DecoTextures.GUI_BG_WOODEN, 0, 128+8, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()

				.withNextLayer()
				.conditionally(isStorage, b -> b
						//Circuit slots background box
						.withBox(DecoTextures.GUI_BG_STEEL, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_SQUARE, 0, 8, 32, 120)
						.withInventorySlots(SlotStyle.IE, container.circuitSlots)
						//Energy bar background box
						.withBox(DecoTextures.GUI_BG_STEEL, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND, 128-32+16+32, 8, 32, 120)
						//Circuit storage slots
						.withInventorySlots(SlotStyle.VANILLA, container.storageSlots)
				)
				.conditionally(!isStorage, b -> b
						.withBox(DecoTextures.GUI_BG_PAPER, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_PAPER, 4+2, 8, 170-16, 16)
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
					.withTemplate(DecoGuiUtils.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage))
			);
		}
		else if(tile.inventory.size() > editedCircuit)
		{
			ItemStack stack = tile.inventory.get(editedCircuit);
			DataPacket storedData = IIContent.itemCircuit.getStoredData(stack);

			addLabel(stack.getDisplayName(), 4+2+16, 8)
					.withTextColor(DecoTextures.COLOR_H2)
					.withSize(170-16-16, 16)
					.withAlign(DecoAlignment.CENTER);
			addComponent(new DecoButton(4+2, 8)
					.withBackground(DecoTextures.RES_TEXTURES_DECO_BUTTON_PAPER)
					.withBackgroundColor(IIColor.fromPackedRGB(0xb37f46))
					.withIcon(null, 16)
					.withText(String.valueOf(editedCircuit))
					.withIconAlignment(DecoAlignment.CENTER)
					.withSize(16, 16)
			);

			list = addComponent(
					new DecoList<DataVariable>(4+1, 8+16+2)
							.withSize(136+32-3, 120-16)
							.withEntries(storedData.getAllVariables())
							.withCreateLaterAction(this::addVariable)
							.withGuiSaveAction(gui -> this.scroll = gui.getScroll())
							//Display
							.withDisplayFunction(new DecoEntryPanelBuilder<DataVariable>()
									.withBackground(DecoTextures.GUI_BG_PAPER)
									.withBackgroundMask(DecoTextures.RES_TEXTURES_DECO_TEMPLATE_TICKET)

									//Duplicate / Edit / Remove Buttons
									.withComponent(p -> new DecoButton(p.width-17-16-14+3, 2)
											.withTemplate(DecoGuiUtils.LIST_BUTTON_DUPLICATE_TEMPLATE)
											.withOnLMBPressed(() -> {
												DataVariable current = p.getCurrentElement();
												char name = findNextFreeVariableName();
												if(name=='\0')
													return;
												editVariable(name, current.getValue());
											})
									)
									.withComponent(p -> new DecoButton(p.width-17-16+3, 2)
											.withTemplate(DecoGuiUtils.LIST_BUTTON_EDIT_TEMPLATE)
											.withOnLMBPressed(() -> {
												editVariable(p.getCurrentElement());
											})
									)
									.withComponent(p -> new DecoButton(p.width-17+1, 2)
											.withTemplate(DecoGuiUtils.LIST_BUTTON_REMOVE_TEMPLATE)
											.withOnLMBPressed(() -> {
												p.getCurrentList().removeEntry(p.getCurrentElement());
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
		addComponent(new DecoTab()
				.withLink(IIGUI.ARITHMETIC_LOGIC_MACHINE_STORAGE)
				.withIcon(ICON_STORAGE)
				.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"storage_module")
		);

		if(tile.hasUpgrade(IIContent.UPGRADE_MEMORY))
			addComponent(new DecoTab()
					.withLink(IIGUI.ARITHMETIC_LOGIC_MACHINE_STORAGE)
					.withIcon(ICON_MEMORY)
					.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"memory_in_module")
			);

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

		if(tile.hasUpgrade(IIContent.UPGRADE_MEMORY))
			addComponent(new DecoTab()
					.withLink(IIGUI.ARITHMETIC_LOGIC_MACHINE_STORAGE)
					.withIcon(ICON_MEMORY)
					.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"memory_out_module")
			);
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
		if(tile.inventory.size() <= editedCircuit)
			return new DataPacket();

		ItemStack stack = tile.inventory.get(editedCircuit);
		return IIContent.itemCircuit.getStoredData(stack);
	}

	private char findNextFreeVariableName()
	{
		if(list==null)
			return '\0';
		DataPacket currentPacket = new DataPacket(list.getEntries());
		if(currentPacket.size() >= DataPacket.VARIABLE_NAMES.length)
			return '\0';
		return IIUtils.cycleDataPacketCharsAvoiding('a', true, false, currentPacket);
	}

	private void addVariable()
	{
		char name = findNextFreeVariableName();
		if(name=='\0')
			return;
		editVariable(name, new DataTypeInteger());
	}

	@Override
	public void editVariable(char name, DataType initialValue)
	{
		DataPacket currentPacket = list==null?getCircuitPacket(): new DataPacket(list.getEntries());

		if(!currentPacket.has(name)||currentPacket.get(name).getClass()!=initialValue.getClass())
			currentPacket.set(name, initialValue);

		variableToEdit = new DataVariable(name, initialValue);
		changeGUI(IIGUI.ARITHMETIC_LOGIC_MACHINE_EDIT);
	}
}
