package pl.pabilo8.immersiveintelligence.client.gui.block.data_device;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor.DecoDataEditor;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataBuffer;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataBuffer.BufferCondition;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerSmallDataDevice;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

/**
 * Edits an exact value for a buffer matching condition.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 25.10.2026
 * @since 24.10.2026
 */
@DecoTemplate(name = "data_buffer_edit", category = DecoGuiCategory.DATA_TILE)
public class GuiDataBufferEdit extends DecoTileGui<TileEntityDataBuffer, ContainerSmallDataDevice<TileEntityDataBuffer>>
{
	@SyncNBT
	public EasyCollection<BufferCondition, NBTTagCompound> conditions = new EasyCollection<>(BufferCondition::new);
	@SyncNBT
	public int editedRule = -1;
	@SyncNBT
	public boolean loaded = false;
	private DecoDataEditor<?> editor;
	private DataType expected = new DataTypeNull();
	private boolean apply = false;

	public GuiDataBufferEdit(EntityPlayer player, TileEntityDataBuffer tile)
	{
		super(player, tile, IIGUI.DATA_BUFFER_EDIT);
		if(tile!=null)
			conditions = tile.conditions.clone();
	}

	@Override
	public void onInit()
	{
		if(editedRule < 0||editedRule >= conditions.size())
		{
			changeGUI(IIGUI.DATA_BUFFER);
			return;
		}
		if(!loaded)
		{
			expected = conditions.get(editedRule).expected.clone();
			loaded = true;
		}
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 176, 168)
				.withTitleBar(tile)
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 168, 176, 92)
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.withNextLayer()
				.withBox(DecoTextures.BG_PAPER, DecoTextures.TEMPLATE_PAPER, 4, 12, 168, 152)
				.build();
		addComponent(new DecoDropdown<TypeMetaInfo<?>>(10, 16)
				.withSize(154, 18)
				.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
				.withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_PAPER)
				.withDropdownWidth(116)
				.withMaxDisplayedEntries(5)
				.withEntries(DecoDataEditor.getEditorTypes(false))
				.withSelectedEntry(expected.getTypeMeta())
				.withDisplayFunction(DecoTemplates.getDataTypeEntryDisplay())
				.withSortFunction(DecoTemplates.getDataTypeEntrySorter())
				.withOnSelectedEntry((oldType, newType) -> {
					expected = newType.supplier.get();
					refreshGUI();
				}));
		editor = DecoDataEditor.getEditorFor(expected, 10, 40);
		if(editor!=null)
			addComponent(editor).withSize(154, 104);
		addComponents(
				new DecoButton(118, 150)
						.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
						.withSize(48, 12)
						.withText("ii.gui.button.apply")
						.withOnLMBPressed(() -> {
							if(editor!=null)
								expected = editor.outputType();
							conditions.get(editedRule).expected = expected.clone();
							apply = true;
							changeGUI(IIGUI.DATA_BUFFER);
						}),
				new DecoButton(66, 150)
						.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
						.withSize(48, 12)
						.withText("ii.gui.button.cancel")
						.withOnLMBPressed(() -> changeGUI(IIGUI.DATA_BUFFER))
		);
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		return EasyNBT.newNBT().conditionally(apply, nbt -> nbt.withSerializable("conditions", conditions));
	}
}
