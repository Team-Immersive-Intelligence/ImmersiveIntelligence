package pl.pabilo8.immersiveintelligence.client.gui.block.data_device;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoCheckbox;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoDropdownDataLetters;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor.DecoDataEditor;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataAppender;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataAppender.AppendRule;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerSmallDataDevice;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

/**
 * Edits a variable entry with independent apply and cancel actions.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 25.10.2026
 * @since 24.10.2026
 */
@DecoTemplate(name = "data_appender_edit", category = DecoGuiCategory.DATA_TILE)
public class GuiDataAppenderEdit extends DecoTileGui<TileEntityDataAppender, ContainerSmallDataDevice<TileEntityDataAppender>>
{
	private static final String KEY = "ii.gui.small_data_devices.";

	@SyncNBT
	public EasyCollection<AppendRule, NBTTagCompound> entries = new EasyCollection<>(AppendRule::new);
	@SyncNBT
	public int editedRule = -1;
	@SyncNBT
	public AppendRule edited = new AppendRule();
	@SyncNBT
	public boolean loaded = false;
	private DecoDataEditor<?> editor;
	private boolean apply = false;

	public GuiDataAppenderEdit(EntityPlayer player, TileEntityDataAppender tile)
	{
		super(player, tile, IIGUI.DATA_APPENDER_EDIT);
		if(tile!=null)
			entries = tile.entries.clone();
	}

	@Override
	public void onInit()
	{
		if(editedRule < 0||editedRule > entries.size())
		{
			changeGUI(IIGUI.DATA_APPENDER);
			return;
		}
		if(!loaded)
		{
			if(editedRule < entries.size())
				edited = entries.copyEntry(entries.get(editedRule));
			loaded = true;
		}
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 240, 168)
				.withTitleBar(tile)
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 32, 168, 176, 92)
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.withNextLayer()
				.withBox(DecoTextures.BG_PAPER, DecoTextures.TEMPLATE_PAPER, 4, 8, 232, 20)
				.withNextLayer()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 4, 32, 232, 128)
				.build();
		editor = null;
		addComponent(new DecoDropdownDataLetters(8, 11)
				.withConstraints(new DataPacket())
				.withSelectedEntry((Character)edited.variable)
				.withBackground(DecoTextures.COMPONENT_DROPDOWN_DATA_LETTER_PAPER)
				.withDropdownSymbol(DecoTextures.COMPONENT_DROPDOWN_SYMBOL_PAPER)
				.withOnSelectedEntry((oldChar, newChar) -> edited.variable = newChar));
		addComponent(new DecoCheckbox(120, 15).withSize(108, 12).withText(KEY+"remove_variable")
				.withChecked(edited.remove).withOnToggle(value -> {
					storeValue();
					edited.remove = value;
					refreshGUI();
				}));
		if(!edited.remove)
		{
			addComponent(new DecoCheckbox(8, 36).withSize(224, 12).withText(KEY+"overwrite")
					.withChecked(edited.overwrite).withOnToggle(value -> edited.overwrite = value));
			addComponent(new DecoDropdown<TypeMetaInfo<?>>(28, 11)
					.withSize(84, 18)
					.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
					.withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_PAPER)
					.withDropdownWidth(84)
					.withMaxDisplayedEntries(5)
					.withEntries(DecoDataEditor.getEditorTypes(false))
					.withSelectedEntry(edited.value.getTypeMeta())
					.withDisplayFunction(DecoTemplates.getDataTypeEntryDisplay())
					.withSortFunction(DecoTemplates.getDataTypeEntrySorter())
					.withOnSelectedEntry((oldType, newType) -> {
						edited.value = newType.supplier.get();
						refreshGUI();
					}));
			editor = DecoDataEditor.getEditorFor(edited.value, 8, 54);
			if(editor!=null)
				addComponent(editor).withSize(224, 104);
		}
		addComponents(
				new DecoButton(182, 162)
						.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
						.withSize(48, 12)
						.withText("ii.gui.button.apply")
						.withOnLMBPressed(() -> {
							storeValue();
							if(editedRule==entries.size())
								entries.add(entries.copyEntry(edited));
							else
								entries.set(editedRule, entries.copyEntry(edited));
							apply = true;
							changeGUI(IIGUI.DATA_APPENDER);
						}),
				new DecoButton(130, 162)
						.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
						.withSize(48, 12)
						.withText("ii.gui.button.cancel")
						.withOnLMBPressed(() -> changeGUI(IIGUI.DATA_APPENDER))
		);
	}

	private void storeValue()
	{
		if(editor!=null&&!edited.remove)
			edited.value = editor.outputType();
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		return EasyNBT.newNBT().conditionally(apply, nbt -> nbt.withSerializable("entries", entries));
	}
}
