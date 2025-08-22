package pl.pabilo8.immersiveintelligence.client.gui.block.data_input_machine;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.DataVariable;
import pl.pabilo8.immersiveintelligence.api.data.IDataMachineGui;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoArrows;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoDropdownDataLetters;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor.DecoDataEditor;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerDataInputMachineEditing;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 31.08.2021.
 * @updated 24.02.2025
 * @ii-approved 0.3.1
 * @since 30.06.2019
 */
@DecoTemplate(name = "data_input_machine_edit", category = DecoGuiCategory.DATA_TILE)
public class GuiDataInputMachineEdit extends DecoGui<TileEntityDataInputMachine, ContainerDataInputMachineEditing> implements IDataMachineGui
{
	@SyncNBT
	public DataVariable variableToEdit;
	public boolean cancel = false;
	public DataPacket packet;

	@Nullable
	private DecoDataEditor<? extends DataType> editor = null;

	public GuiDataInputMachineEdit(EntityPlayer player, TileEntityDataInputMachine tile)
	{
		super(player, tile, IIGUI.DATA_INPUT_MACHINE_EDIT);
	}

	@Override
	public void onInit()
	{
		//Use a copy of the tile data
		packet = tile.storedData.clone();

		//Build background
		startBackground()
				.withBox(DecoTextures.GUI_BG_STEEL, 0, 0, 176+64, 128+8)
				.withTitleBar(tile)
				.withBox(DecoTextures.GUI_BG_STEEL, 32, 128+8, 176+32, 32)
				.withBox(DecoTextures.GUI_BG_WOODEN, 32, 128+8+32, 176, 92)
				.withInventoryTitleBar()

				.withNextLayer()
				.withBox(DecoTextures.GUI_BG_STEEL, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_SQUARE, 0, 8, 32, 120)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventorySlots(SlotStyle.IE_INPUT, container.dataInput)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.dataOutput)

				.withNextLayer()
				.withBox(DecoTextures.GUI_BG_PAPER, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_PAPER, 32+8-4+4, 48-8-24-4, 176+64-16-32+8-4-8, 24)

				.withNextLayer()
				.withBox(DecoTextures.GUI_BG_STEEL, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND, 32+8-4+4, 48-8, 176+64-16-32+8-8, 128+8-48+32)
				.withTitleBar("desc.immersiveintelligence.variable_properties")

				.build();

		//Shared parts, like punchtape progress bar, tabs
		addComponents(GuiDataInputMachine.getCommonParts(tile));

		//Editor component specific to the data type
		editor = DecoDataEditor.getEditorFor(variableToEdit.getValue(), 38+8-3, 46+6-1-8);
		if(editor!=null)
			addComponent(editor)
					.withSize(128+64-16+3+8-1, 80+1+32+1-8);

		//A cloned packet without the currently edited variable is required, so the selector knows which variable names are unavailable
		DataPacket cloned = tile.storedData.clone();
		cloned.remove(variableToEdit.getName());

		//Add type/name controls
		addComponents(
				//Variable name selector
				new DecoDropdownDataLetters(32+4+6+1, 4+8+2+1)
						.withConstraints(cloned)
						.withSelectedEntry((Character)variableToEdit.getName())
						.withTranslatedTooltip("desc.immersiveintelligence.variable_properties")
						.withOnSelectedEntry((oldChar, newChar) -> changeVariableName(newChar))
						.withDropdownSymbol(DecoTextures.RES_TEXTURES_DECO_COMPONENT_DROPDOWN_SYMBOL_PAPER)
						.withTextColor(DecoTextures.COLOR_H1, IIColor.fromPackedRGB(0x35322c))
						.withBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_DROPDOWN_DATA_LETTER_PAPER),

				new DecoArrows(32+4+6+1+18+1, 4+8+2+1+2)
						.withSize(8, 14)
						.withBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_ARROWS_PAPER)
						.withOnArrow(arrow -> {
							char cycled = IIUtils.cycleDataPacketCharsAvoiding(variableToEdit.getName(), arrow, false, cloned);
							changeVariableName(cycled);
						}),

				//Data Type selector
				new DecoDropdown<TypeMetaInfo<?>>(32+4+10+32-12+6+1, 4+8+2+1)
						.withScrollBarBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_SLIDER_PAPER)
						.withBackground(DecoTextures.RES_TEXTURES_DECO_BUTTON_PAPER)
						.withListBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_TEXT_FIELD)
						.withSize(116, 18)
						.withDropdownWidth(116)
						.withMaxDropHeight(128)
						.withEntries(DecoDataEditor.getEditorTypes(false))
						.withSelectedEntry(variableToEdit.getValue().getTypeMeta())
						.withDisplayFunction(new DecoEntryPanelBuilder<TypeMetaInfo<?>>()
								.withBackground(DecoTextures.GUI_BG_PAPER)
								.withBackgroundMask(DecoTextures.RES_TEXTURES_DECO_TEMPLATE_PAPER)
								//Type Icon, Label, and Letter
								.withComponent("image", new DecoImage(3, 1)
										.withSize(16, 16))
								.withLabel("typeLabel",
										new DecoLabel(fontRenderer, 2+16+2, 1)
												.withSize(48, 18)
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
											.withImageLocation(typeMeta.getTextureLocation(), true);
								})
								.withElementTooltip(typeMeta -> "a")
						)
						.withOnSelectedEntry((typeMetaInfo, typeMetaInfo2) -> {
							cancel = true;
							variableToEdit = new DataVariable(variableToEdit.getName(), typeMetaInfo2.supplier.get());
							refreshGUI();
						}),

				new DecoButton(xSize-48-4-4-4-2, 128+8-16+32-2+3)
						.withBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_BUTTON_ROUND)
						.withText("Apply")
						.withSize(48, 12)
						.withOnPressed((gui, button, mouseX, mouseY) -> changeGUI(IIGUI.DATA_INPUT_MACHINE_VARIABLES)),
				new DecoButton(xSize-48*2-4-4-4-2, 128+8+32-16-2+3)
						.withBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_BUTTON_ROUND)
						.withText("Cancel")
						.withSize(48, 12)
						.withOnPressed((gui, button, mouseX, mouseY) -> {
							cancel = true;
							return changeGUI(IIGUI.DATA_INPUT_MACHINE_VARIABLES);
						}),

				new DecoButton(162+32-4, 2+8+4+1)
						.withTemplate(DecoGuiUtils.LIST_BUTTON_DUPLICATE_TEMPLATE)
						.withSize(18, 18),
				new DecoButton(162+32-4+1+18, 2+8+4+1)
						.withTemplate(DecoGuiUtils.LIST_BUTTON_CLEAR_TEMPLATE)
						.withSize(18, 18)
		);
	}

	private void changeVariableName(Character newName)
	{
		//Do nothing if the name remains the same
		if(newName==variableToEdit.getName()||editor==null)
			return;

		//Remove existing variable
		packet.remove(variableToEdit.getName());

		//Place it in the packet with the new name
		variableToEdit = new DataVariable(newName, editor.outputType());
		refreshGUI();
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		return super.onSaveTileData()
				.conditionally(editor!=null&&!cancel, easyNBT ->
				{
					variableToEdit = new DataVariable(variableToEdit.getName(), editor.outputType());
					easyNBT.withSerializable("variables",
							packet.with(variableToEdit)
					);
				});
	}

	@Override
	public void editVariable(char name, DataType initialValue)
	{

	}
}
