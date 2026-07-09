package pl.pabilo8.immersiveintelligence.client.gui.block.data_input_machine;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.DataVariable;
import pl.pabilo8.immersiveintelligence.api.data.IDataMachineGui;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoArrows;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoDropdownDataLetters;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor.DecoDataEditor;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIContent;
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
public class GuiDataInputMachineEdit extends DecoTileGui<TileEntityDataInputMachine, ContainerDataInputMachineEditing> implements IDataMachineGui
{
	@SyncNBT
	public DataVariable variableToEdit;
	private boolean cancel = false;
	private Character originalVariableName = null;
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
		//Sync machine's animated parts
		syncAnimatedParts(tile.drawer, false);
		syncAnimatedParts(tile.hatch, true);

		//Use a persistent copy of the tile data. Rebuilding the GUI after a rename must not re-clone a stale server packet.
		if(packet==null)
			packet = tile.storedData.clone();

		//Build background
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 240, 136)
				.withTitleBar(tile)
				.withBox(DecoTextures.BG_STEEL, 32, 136, 208, 32)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 32, 168, 176, 92)
				.withInventoryTitleBar()

				.withNextLayer()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 0, 8, 32, 120)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventorySlots(SlotStyle.IE_INPUT, container.dataInput)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.dataOutput)

				.withNextLayer()
				.withBox(DecoTextures.BG_PAPER, DecoTextures.TEMPLATE_PAPER, 40, 12, 188, 24)

				.withNextLayer()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_ROUND, 40, 40, 192, 120)
				.withTitleBar("desc.immersiveintelligence.variable_properties")

				.build();

		//Shared parts, like punchtape progress bar, tabs
		addComponents(GuiDataInputMachine.getCommonParts(tile));

		//Editor component specific to the data type
		editor = DecoDataEditor.getEditorFor(variableToEdit.getValue(), 43, 43);
		if(editor!=null)
			addComponent(editor)
					.withSize(186, 106);

		//A cloned packet without the currently edited variable is required, so the selector knows which variable names are unavailable
		DataPacket cloned = packet.clone();
		cloned.remove(variableToEdit.getName());

		//Add type/name controls
		addComponents(
				//Variable name selector
				new DecoDropdownDataLetters(43, 15)
						.withConstraints(cloned)
						.withSelectedEntry((Character)variableToEdit.getName())
						.withTranslatedTooltip("desc.immersiveintelligence.variable_properties")
						.withOnSelectedEntry((oldChar, newChar) -> changeVariableName(newChar))
						.withDropdownSymbol(DecoTextures.COMPONENT_DROPDOWN_SYMBOL_PAPER)
						.withTextColor(DecoColors.H1, IIColor.fromPackedRGB(0x35322c))
						.withBackground(DecoTextures.COMPONENT_DROPDOWN_DATA_LETTER_PAPER),

				new DecoArrows(62, 17)
						.withSize(8, 14)
						.withBackground(DecoTextures.COMPONENT_ARROWS_PAPER)
						.withOnArrow(arrow -> {
							char cycled = IIUtils.cycleDataPacketCharsAvoiding(variableToEdit.getName(), arrow, false, cloned);
							changeVariableName(cycled);
						}),

				//Data Type selector
				new DecoDropdown<TypeMetaInfo<?>>(73, 15)
						.withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_PAPER)
						.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
						.withListBackground(DecoTextures.COMPONENT_TEXT_FIELD)
						.withSize(116, 18)
						.withDropdownWidth(116)
						.withMaxDisplayedEntries(5)
						.withEntries(DecoDataEditor.getEditorTypes(tile.isUpgradeInstalled(IIContent.UPGRADE_ADVANCED_DATA)))
						.withSelectedEntry(variableToEdit.getValue().getTypeMeta())
						.withDisplayFunction(DecoTemplates.getDataTypeEntryDisplay())
						.withOnSelectedEntry((typeMetaInfo, typeMetaInfo2) -> {
							cancel = true;
							variableToEdit = new DataVariable(variableToEdit.getName(), typeMetaInfo2.supplier.get());
							refreshGUI();
						}),

				new DecoButton(xSize-48-4-4-4-2, 153)
						.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
						.withText("ii.gui.button.apply")
						.withSize(48, 12)
						.withOnPressed((gui, button, mouseX, mouseY) -> {
							cancel = false;
							return changeGUI(IIGUI.DATA_INPUT_MACHINE_VARIABLES);
						}),
				new DecoButton(xSize-96-4-4-4-2, 153)
						.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
						.withText("ii.gui.button.cancel")
						.withSize(48, 12)
						.withOnPressed((gui, button, mouseX, mouseY) -> {
							cancel = true;
							return changeGUI(IIGUI.DATA_INPUT_MACHINE_VARIABLES);
						}),

				new DecoButton(190, 15)
						.withTemplate(DecoTemplates.ACTION_BUTTON_DUPLICATE)
						.withSize(18, 18),
				new DecoButton(209, 15)
						.withTemplate(DecoTemplates.ACTION_BUTTON_CLEAR)
						.withSize(18, 18)
		);
	}

	private void changeVariableName(Character newName)
	{
		//Do nothing if the name remains the same
		if(newName==null||newName==variableToEdit.getName()||editor==null)
			return;

		cancel = true;
		storeEditorOutput();

		//Remove existing variable and keep the edited value under the new name in the local packet.
		packet.remove(variableToEdit.getName());
		variableToEdit = new DataVariable(newName, variableToEdit.getValue());
		refreshGUI();
	}

	private void storeEditorOutput()
	{
		if(editor!=null)
			variableToEdit = new DataVariable(variableToEdit.getName(), editor.outputType());
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
				.conditionally(editor!=null&&!cancel, easyNBT ->
				{
					storeEditorOutput();
					DataPacket updatedPacket = packet.clone().with(variableToEdit);
					tile.setStoredDataPacket(updatedPacket);
					easyNBT.withSerializable("variables", updatedPacket);
				});
	}

	@Override
	public void editVariable(char name, DataType initialValue)
	{

	}
}
