package pl.pabilo8.immersiveintelligence.client.gui.block.data_input_machine;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.client.gui.IDataMachineGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoDropdownDataLetters;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor.DecoDataEditor;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.widget.DecoManualWidget;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerDataInputMachineEditing;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 31.08.2021.
 * @since 30.06.2019
 */
@DecoTemplate(name = "data_input_machine_edit")
public class GuiDataInputMachineEdit extends DecoGui<TileEntityDataInputMachine, ContainerDataInputMachineEditing> implements IDataMachineGui
{
	@SyncNBT
	public char variableToEdit = 'a';
	public DataType dataType;

	@Nullable
	private DecoDataEditor<? extends DataType> editor = null;

	public GuiDataInputMachineEdit(EntityPlayer player, TileEntityDataInputMachine tile)
	{
		super(player, tile, IIGUI.DATA_INPUT_MACHINE_EDIT);
	}

	@Override
	public void onInit()
	{
		//Cache data type
		this.dataType = tile.storedData.get(variableToEdit);

		//Build background
		startBackground()
				.withBox(IIReference.GUI_BG_STEEL, 0, 0, 176+64, 128+8)
				.withTitleBar(tile)
				.withBox(IIReference.GUI_BG_STEEL, 32, 128+8, 176+32, 32)
				.withBox(IIReference.GUI_BG_WOODEN, 32, 128+8+32, 176, 92)
				.withInventoryTitleBar()

				.withNextLayer()
				.withBox(IIReference.GUI_BG_STEEL, IIReference.RES_TEXTURES_DECO_TEMPLATE_SQUARE, 0, 8, 32, 120)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventorySlots(SlotStyle.IE_INPUT, container.dataInput)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.dataOutput)

				.withNextLayer()
				.withBox(IIReference.GUI_BG_STEEL, IIReference.RES_TEXTURES_DECO_TEMPLATE_ROUND, 32+8-4+4, 48, 176+64-16-32+8-8, 128+8-48+32)
				.withTitleBar("desc.immersiveintelligence.variable_properties")

				.build();

		//Shared parts, like punchtape progress bar, tabs
		addComponents(GuiDataInputMachine.getCommonParts(tile));
		addWidget(new DecoManualWidget());

		//Editor component specific to the data type
		editor = DecoDataEditor.getEditorFor(dataType, 38+8-3, 46+6-1);
		if(editor!=null)
			addComponent(editor)
					.withSize(128+64-16+3+8-1, 80+1+32+1);

		//A cloned packet without the currently edited variable is required, so the selector knows which variable names are unavailable
		DataPacket cloned = tile.storedData.clone();
		cloned.remove(variableToEdit);

		//Add type/name controls
		addComponents(
				//Variable name selector
				new DecoDropdownDataLetters(32+4, 4+8)
						.withConstraints(cloned)
						.withSelectedEntry(variableToEdit)
						.withTranslatedTooltip("desc.immersiveintelligence.variable_properties"),

				//Data Type selector
				new DecoDropdown<TypeMetaInfo<?>>(32+4+10+32-12, 4+8)
						.withListBackgroundLocation(IIReference.GUI_BG_PAPER)
						.withSize(116, 18)
						.withDropdownWidth(116)
						.withMaxDropHeight(128)
						.withEntries(IIDataTypeUtils.metaTypesByName.values())
						.withSelectedEntry(dataType.getTypeMeta())
						.withDisplayFunction(new DecoEntryPanelBuilder<TypeMetaInfo<?>>()
								//Type Icon, Label, and Letter
								.withComponent("image", new DecoImage(0, -1)
										.withSize(16, 16))
								.withLabel("typeLabel",
										new DecoLabel(fontRenderer, 2+16, 0)
												.withSize(48, 16)
												.withAlign(DecoAlignment.LEFT)
												.withText("Integer")
								)
								.withElementApplyMethod((typeMeta, panel) -> {
									//type label (f.e. integer)
									panel.label("typeLabel")
											.withText(typeMeta.getTranslatedName())
											.withTextColor(typeMeta.color.withBrightness(0.4f));
									//type icon
									panel.component("image", DecoImage.class)
											.withImageLocation(typeMeta.getTextureLocation());
								})
								.withElementTooltip(typeMeta -> "a")
								.withBackground(IIReference.GUI_BG_PAPER)
								.withBackgroundMask(IIReference.RES_TEXTURES_DECO_TEMPLATE_TICKET)
						)
						.withOnSelectedEntry((typeMetaInfo, typeMetaInfo2) -> {
							//TODO: 21.07.2025 change type and restart GUI
						})
		);
	}

	@Override
	public void editVariable(char name, DataType initialValue)
	{

	}
}
