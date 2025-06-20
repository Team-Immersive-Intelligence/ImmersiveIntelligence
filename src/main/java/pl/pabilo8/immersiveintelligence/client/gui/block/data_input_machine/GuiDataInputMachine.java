package pl.pabilo8.immersiveintelligence.client.gui.block.data_input_machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.client.gui.IDataMachineGui;
import pl.pabilo8.immersiveintelligence.client.gui.ITabbedGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoResource;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.widget.GuiWidgetManualWrapper;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
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
@DecoTemplate(name = "data_input_machine")
public class GuiDataInputMachine extends DecoGui<TileEntityDataInputMachine, ContainerDataInputMachine> implements ITabbedGui, IDataMachineGui
{
	@DecoResource
	public static ResourceLocation ICON_STORAGE = ResLoc.of(IIReference.RES_II, "gui/tab_icons/storage");
	@DecoResource
	public static ResourceLocation ICON_VARIABLES = ResLoc.of(IIReference.RES_II, "gui/tab_icons/variables");
	@DecoResource
	public static ResourceLocation ICON_SEND_PACKET = ResLoc.of(IIReference.RES_II, "gui/tab_icons/send_packet");

	public GuiWidgetManualWrapper sideManual = null;
	protected DecoButton manualButton;

	protected DecoList<Pair<Character, DataType>> list;

	@SyncNBT
	protected boolean soundPlayed = false;
	@SyncNBT
	protected boolean manual = false;
	@SyncNBT
	int scroll;

	public GuiDataInputMachine(EntityPlayer player, TileEntityDataInputMachine tile, IIGUI gui)
	{
		super(player, tile, gui);
	}

	@Override
	public void onInit()
	{
		//Set animation for the machine hatches
		boolean isStorage = container.hasStorage;
		syncAnimatedParts(0, isStorage);
		syncAnimatedParts(1, !isStorage);

		//Build background
		startBackground()
				.withBox(IIReference.GUI_BG_STEEL, 0, 0, 176, 128+8)
				.withTitleBar(tile)
				.withBox(IIReference.GUI_BG_WOODEN, 0, 128+8, 176, 92)
				.withInventoryTitleBar()

				.withNextLayer()
				.withBox(IIReference.GUI_BG_STEEL, IIReference.RES_TEXTURES_DECO_TEMPLATE_SQUARE, 0, 8, 32, 120)
				.conditionally(isStorage,
						b -> b
								.withBox(IIReference.GUI_BG_STEEL, IIReference.RES_TEXTURES_DECO_TEMPLATE_ROUND, 128-32+16+32, 8, 32, 120)
								.withInventorySlots(SlotStyle.VANILLA, container.punchtapeStorage)
				)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventorySlots(SlotStyle.IE_INPUT, container.dataInput)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.dataOutput)
				.build();

		//Add tabs
		addComponents(
				new DecoTab()
						.withLink(IIGUI.DATA_INPUT_MACHINE_STORAGE)
						.withIcon(ICON_STORAGE)
						.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"storage_module"),
				new DecoTab()
						.withLink(IIGUI.DATA_INPUT_MACHINE_VARIABLES)
						.withIcon(ICON_VARIABLES)
						.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"variables_module"),
				new DecoTab()
						.withIcon(ICON_SEND_PACKET, 32)
						.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"variable_send_packet")
						.withOnPressed((gui, mouseX, mouseY) -> {
							IIPacketHandler.sendToServer(new MessageIITileSync(tile, EasyNBT.newNBT()
									.withBoolean("send_packet", true)
							));
							return true;
						})
		);

		//TODO: 10.06.2025 Add manual widget
//		addWidget(new GuiWidgetManual<>());

		//Add storage display and bars or the variable list, if in the "variables" tab
		if(isStorage)
		{
			addLabel(IIReference.GUI_LABEL_KEY+"data_input_machine.storage", 0, 8+4)
					.withSize(xSize, 11)
					.withAlign(DecoAlignment.TOP);
			addLabel(IIReference.GUI_LABEL_KEY+"data_input_machine.memory", 0, 8+4+76+4+2+4)
					.withSize(xSize, 11)
					.withAlign(DecoAlignment.TOP);
			addComponent(new DecoDropdown<String>(32+8-4-2, 8+76+8+8+8-4+4)
					.withWidth(96+8+4)
					.withEntries("Slot 1", "Slot 2", "Slot 3", "Slot 4")
			);
			addComponent(new DecoBar(128+32-8+2, 24)
					.withHeight(95)
					.withTemplate(DecoGuiUtils.BAR_ELECTRIC_ENERGY.apply(tile.energyStorage))
			);
		}
		else
			list = addComponent(
					new DecoList<Pair<Character, DataType>>(32, 8)
							.withSize(136, 120)
							.withEntries(tile.storedData.getAllVariables())
							.withCreateLaterAction(() -> changeGUI(IIGUI.DATA_INPUT_MACHINE_EDIT))
							.withGuiSaveAction(gui -> this.scroll = gui.getScroll())
							//Display
							.withDisplayFunction(new DecoEntryPanelBuilder<Pair<Character, DataType>>()
									.withPadding(1, 1)
									//Edit / Remove Buttons
									.withComponent(p -> new DecoButton(p.width-17-16+3, 2)
											.withTemplate(DecoGuiUtils.LIST_BUTTON_EDIT_TEMPLATE)
											.withOnPressed((gui, mouseX, mouseY) -> changeGUI(IIGUI.DATA_INPUT_MACHINE_EDIT))
									)
									.withComponent(p -> new DecoButton(p.width-17+1, 2)
											.withTemplate(DecoGuiUtils.LIST_BUTTON_REMOVE_TEMPLATE)
											.withOnPressed((gui, mouseX, mouseY) -> p.getCurrentList().removeEntry(p.getCurrentElement()))
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
												.withRawText(entry.getKey().toString());
										//type label (f.e. integer)
										panel.label("typeLabel")
												.withText(typeMeta.getTranslatedName())
												.withTextColor(typeMeta.color.withBrightness(0.4f));
										//type icon
										panel.component("image", DecoImage.class)
												.withImageLocation(entry.getValue().getTextureLocation());
									})
							)
							.withScroll(scroll)
			);
	}

	@Override
	public void onGuiClosed()
	{
		//Close the hatches
		if(!soundPlayed)
		{
			syncAnimatedParts(0, false);
			syncAnimatedParts(1, false);
		}
		super.onGuiClosed();
	}

	@Override
	public void editVariable(char c, DataType type)
	{
		/*if(!list.variables.containsKey(c)||list.getPacketVariable(c).getClass()!=type.getClass())
			list.setVariable(c, type);

		//Save gui scroll, tile pos for validation
		saveBasicData(tile);
		syncDataToServer();

		proxy.getStoredGuiData().withString("variableToEdit", String.valueOf(c));
		//Set variable and change gui
		refreshStoredData();
		syncDataToServer();

		soundPlayed = true;
		IIPacketHandler.sendToServer(new MessageGuiNBT(IIGuiList.GUI_DATA_INPUT_MACHINE_EDIT, tile));*/
	}
}
