package pl.pabilo8.immersiveintelligence.client.gui.block.data_input_machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.DataVariable;
import pl.pabilo8.immersiveintelligence.api.data.IDataMachineGui;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent.MouseButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage.ImageAnimationDirection;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 30.08.2021
 * @updated 24.02.2025
 * @ii-approved 0.3.1
 * @since 30.06.2019
 */
@DecoTemplate(name = "data_input_machine", category = DecoGuiCategory.DATA_TILE)
public class GuiDataInputMachine extends DecoTileGui<TileEntityDataInputMachine, ContainerDataInputMachine> implements IDataMachineGui
{
	@DecoResource
	public static ResourceLocation ICON_SEND_PACKET = ResLoc.of(IIReference.RES_II, "gui/tab_icons/send_packet");
	@DecoResource
	public static ResourceLocation PROGRESS_IMAGE = ResLoc.of(IIReference.RES_II, "gui/data_input_machine");
	@SyncNBT
	public int scroll;
	@SyncNBT
	public DataVariable variableToEdit = new DataVariable('a', new DataTypeNull());
	protected DecoList<DataVariable> list;

	public GuiDataInputMachine(EntityPlayer player, TileEntityDataInputMachine tile, IIGUI gui)
	{
		super(player, tile, gui);
	}

	public static GuiDataInputMachine getStorageGui(EntityPlayer player, TileEntityDataInputMachine tile)
	{
		return new GuiDataInputMachine(player, tile, IIGUI.DATA_INPUT_MACHINE_STORAGE);
	}

	public static GuiDataInputMachine getVariablesGui(EntityPlayer player, TileEntityDataInputMachine tile)
	{
		return new GuiDataInputMachine(player, tile, IIGUI.DATA_INPUT_MACHINE_VARIABLES);
	}

	public static DecoComponent<?>[] getCommonParts(TileEntityDataInputMachine tile)
	{
		return new DecoComponent[]{
				new DecoImage(4+2, 12+24+8-2-1)
						.withSize(20, 52)
						.withImageLocation(PROGRESS_IMAGE, true)
						.withUV(64, 0, 0, 20, 52),
				new DecoImage(4+2, 12+24+8-2-1)
						.withSize(20, 52)
						.withImageLocation(PROGRESS_IMAGE, true)
						.withUV(64, 20, 0, 40, 52)
						.withAnimation(ImageAnimationDirection.TOP_TO_BOTTOM, DecoGuiUtils.getMultiblockProductionSingleProgress(tile)),
				new DecoTab()
						.withLink(IIGUI.DATA_INPUT_MACHINE_STORAGE)
						.withIcon(DecoTextures.ICON_STORAGE)
						.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"storage_module"),
				new DecoTab()
						.withLink(IIGUI.DATA_INPUT_MACHINE_VARIABLES)
						.withIcon(DecoTextures.ICON_VARIABLES)
						.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"variables_module"),

				new DecoTab()
						.withIcon(ICON_SEND_PACKET, 32)
						.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"variable_send_packet")
						.withOnPressed((gui, mouseButton, mouseX, mouseY) -> {
					if(mouseButton==MouseButton.LEFT)
					{
						IIPacketHandler.sendToServer(new MessageIITileSync(tile, EasyNBT.newNBT()
								.withBoolean("send_packet", true)
						));
						return true;
					}
					return false;
				})
		};
	}

	@Override
	public void onInit()
	{
		//Set animation for the machine hatches
		boolean isStorage = container.hasStorage;
		syncAnimatedParts(tile.drawer, isStorage);
		syncAnimatedParts(tile.hatch, !isStorage);

		//Build background
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 176, 128+8)
				.withTitleBar(tile)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 128+8, 176, 92)
				.withInventoryTitleBar()

				.withNextLayer()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 0, 8, 32, 120)
				.conditionally(isStorage,
						b -> b
								.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_ROUND, 128-32+16+32, 8, 32, 120)
								.withInventorySlots(SlotStyle.VANILLA, container.punchtapeStorage)
				)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventorySlots(SlotStyle.IE_INPUT, container.dataInput)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.dataOutput)
				.build();

		//Add tabs and energy bars
		addComponents(getCommonParts(tile));

		//Add storage display and bars or the variable list, if in the "variables" tab
		if(isStorage)
		{
			addLabel(IIReference.GUI_LABEL_KEY+"data_input_machine.storage", 0, 8+4)
					.withSize(xSize, 11)
					.withAlign(DecoAlignment.TOP);
			if(tile.isUpgradeInstalled(IIContent.UPGRADE_ADVANCED_DATA))
			{
				addLabel(IIReference.GUI_LABEL_KEY+"data_input_machine.memory", 0, 8+4+76+4+2+4)
						.withSize(xSize, 11)
						.withAlign(DecoAlignment.TOP);

				addComponent(new DecoDropdown<Integer>(32+8-4-2, 8+76+8+8+8-4+4)
						.withWidth(96+8+4)
						.withEntries(0, 1, 2, 3)
						.withSelectedEntry(tile.selectedDataSlot)
						.withOnSelectedEntry((oldEntry, newEntry) -> {
							tile.switchDataSlot(newEntry);
							IIPacketHandler.sendToServer(new MessageIITileSync(tile, EasyNBT.newNBT()
									.withInt("selectedDataSlot", newEntry)
							));
							refreshGUI();
						})
				);
			}
			addComponent(new DecoBar(128+32-8+2, 24)
					.withHeight(95)
					.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage))
			);
		}
		else //List the variables in the packet
		{
			list = addComponent(
					new DecoList<DataVariable>(32, 8)
							.withSize(136, 120)
							.withEntries(tile.storedData.getAllVariables())
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
											})
									)
									.withComponent(p -> new DecoButton(p.width-17+1, 2)
											.withTemplate(DecoTemplates.ACTION_BUTTON_REMOVE)
											.withOnLMBPressed(() -> {
												p.getCurrentList().removeEntry(p.getCurrentElement());
												IIPacketHandler.sendToServer(new MessageIITileSync(tile, onSaveTileData()));
											})
									)
									//Type Icon, Label, and Letter
									.withComponent("image", new DecoImage(2+12, 1)
											.withSize(16, 16))
									.withLabel("typeLabel",
											new DecoLabel(fontRenderer, 2+12+16+2, 2)
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
			//Listen to changes in the DataPacket's size
			addValueListener(() -> tile.storedData)
					.withObserver(v -> list.withEntries(v.getAllVariables()));
		}
	}

	@Override
	public void onGuiClosed()
	{
		//Close the hatches
		if(!refreshGUIFlag)
		{
			syncAnimatedParts(tile.drawer, false);
			syncAnimatedParts(tile.hatch, false);
		}
		super.onGuiClosed();
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		return super.onSaveTileData()
				.conditionally(list!=null, e -> {
					DataPacket packet = new DataPacket(list.getEntries());
					tile.setStoredDataPacket(packet);
					e.withSerializable("variables", packet);
				});
	}

	private char findNextFreeVariableName()
	{
		DataPacket currentPacket = new DataPacket(list.getEntries());
		if(currentPacket.size() >= DataPacket.VARIABLE_NAMES.length)
			return '\0';
		return IIUtils.cycleDataPacketCharsAvoiding('0', true, false, currentPacket);
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
		DataPacket currentPacket = list==null?tile.storedData: new DataPacket(list.getEntries());

		if(!currentPacket.has(name)||currentPacket.get(name).getClass()!=initialValue.getClass())
			currentPacket.set(name, initialValue);

		variableToEdit = new DataVariable(name, initialValue.clone());
		changeGUI(IIGUI.DATA_INPUT_MACHINE_EDIT);
	}
}
