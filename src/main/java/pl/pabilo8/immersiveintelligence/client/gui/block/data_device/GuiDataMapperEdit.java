package pl.pabilo8.immersiveintelligence.client.gui.block.data_device;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoDropdownDataLetters;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor.DecoDataEditor;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.data_device.DataDeviceUtils;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMapper;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMapper.MappingRule;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMapper.ValueMapping;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerSmallDataDevice;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

/**
 * Edits input and output types, variable names and exact value mappings.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 25.10.2026
 * @since 24.10.2026
 */
@DecoTemplate(name = "data_mapper_edit", category = DecoGuiCategory.DATA_TILE)
public class GuiDataMapperEdit extends DecoTileGui<TileEntityDataMapper, ContainerSmallDataDevice<TileEntityDataMapper>>
{
	@SyncNBT
	public EasyCollection<MappingRule, NBTTagCompound> mappings = new EasyCollection<>(MappingRule::new);
	@SyncNBT
	public int editedRule = -1, editedValue = -1;
	@SyncNBT
	public MappingRule edited = new MappingRule();
	@SyncNBT
	public boolean loaded = false;
	private boolean apply = false;

	public GuiDataMapperEdit(EntityPlayer player, TileEntityDataMapper tile)
	{
		super(player, tile, IIGUI.DATA_MAPPER_EDIT);
		if(tile!=null)
			mappings = tile.mappings.clone();
	}

	@Override
	public void onInit()
	{
		if(editedRule < 0||editedRule > mappings.size())
		{
			changeGUI(IIGUI.DATA_MAPPER);
			return;
		}
		if(!loaded)
		{
			if(editedRule < mappings.size())
				edited = mappings.copyEntry(mappings.get(editedRule));
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
				.build();

		//input and target selection panel
		addComponent(new DecoDropdownDataLetters(8, 11)
				.withConstraints(new DataPacket())
				.withSelectedEntry((Character)edited.input)
				.withBackground(DecoTextures.COMPONENT_DROPDOWN_DATA_LETTER_PAPER)
				.withDropdownSymbol(DecoTextures.COMPONENT_DROPDOWN_SYMBOL_PAPER)
				.withOnSelectedEntry((oldChar, newChar) -> edited.input = newChar));
		addComponent(new DecoDropdown<TypeMetaInfo<?>>(34-8+2, 11)
				.withSize(84, 18)
				.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
				.withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_PAPER)
				.withDropdownWidth(84)
				.withMaxDisplayedEntries(5)
				.withEntries(DecoDataEditor.getEditorTypes(false))
				.withSelectedEntry(edited.inputType.getTypeMeta())
				.withDisplayFunction(DecoTemplates.getDataTypeEntryDisplay())
				.withSortFunction(DecoTemplates.getDataTypeEntrySorter())
				.withOnSelectedEntry((oldType, newType) -> {
					DataType value = newType.supplier.get();
					edited.inputType = value;
					for(ValueMapping mapping : edited.values)
						mapping.expected = value.clone();
					refreshGUI();
				}));
		addLabel("->", 116-4+4-2, 16);
		addComponent(new DecoDropdownDataLetters(134-8, 11)
				.withConstraints(new DataPacket())
				.withSelectedEntry((Character)edited.output)
				.withBackground(DecoTextures.COMPONENT_DROPDOWN_DATA_LETTER_PAPER)
				.withDropdownSymbol(DecoTextures.COMPONENT_DROPDOWN_SYMBOL_PAPER)
				.withOnSelectedEntry((oldChar, newChar) -> edited.output = newChar));
		addComponent(new DecoDropdown<TypeMetaInfo<?>>(158-8-4, 11)
				.withSize(84, 18)
				.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
				.withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_PAPER)
				.withDropdownWidth(84)
				.withMaxDisplayedEntries(5)
				.withEntries(DecoDataEditor.getEditorTypes(false))
				.withSelectedEntry(edited.outputType.getTypeMeta())
				.withDisplayFunction(DecoTemplates.getDataTypeEntryDisplay())
				.withSortFunction(DecoTemplates.getDataTypeEntrySorter())
				.withOnSelectedEntry((oldType, newType) -> {
					DataType value = newType.supplier.get();
					edited.outputType = value;
					for(ValueMapping mapping : edited.values)
						mapping.output = value.clone();
					refreshGUI();
				}));

		//Mappings list
		addComponent(new DecoList<ValueMapping>(4, 40-8).withSize(224+8, 104+16+8).withEntries(edited.values)
				.withCreateLaterAction(() -> {
					if(edited.values.size() < DataDeviceUtils.MAX_RULES)
					{
						editedValue = edited.values.size();
						changeGUI(IIGUI.DATA_MAPPER_VALUE);
					}
				})
				.withDisplayFunction(new DecoEntryPanelBuilder<ValueMapping>().withHeight(24)
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_TICKET)
						.withLabel("pair", p -> new DecoLabel(fontRenderer, 4, 8).withSize(156, 10)
								.withAlign(DecoAlignment.LEFT)
								.withRawText(p.getCurrentElement().expected+" -> "+p.getCurrentElement().output))
						.withComponent(p -> new DecoButton(p.width-35, 4).withTemplate(DecoTemplates.ACTION_BUTTON_EDIT)
								.withOnLMBPressed(() -> {
									editedValue = edited.values.indexOf(p.getCurrentElement());
									changeGUI(IIGUI.DATA_MAPPER_VALUE);
								}))
						.withComponent(p -> new DecoButton(p.width-18, 4).withTemplate(DecoTemplates.ACTION_BUTTON_REMOVE)
								.withOnLMBPressed(() -> {
									edited.values.remove(p.getCurrentElement());
									refreshGUI();
								}))));

		//Cancel and Apply buttons
		addComponents(
				new DecoButton(182, 150+12)
						.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
						.withSize(48, 12)
						.withText("ii.gui.button.apply")
						.withOnLMBPressed(() -> {
							if(editedRule==mappings.size())
								mappings.add(mappings.copyEntry(edited));
							else
								mappings.set(editedRule, mappings.copyEntry(edited));
							apply = true;
							changeGUI(IIGUI.DATA_MAPPER);
						}),
				new DecoButton(130, 150+12)
						.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
						.withSize(48, 12)
						.withText("ii.gui.button.cancel")
						.withOnLMBPressed(() -> changeGUI(IIGUI.DATA_MAPPER))
		);
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		return EasyNBT.newNBT().conditionally(apply, nbt -> nbt.withSerializable("mappings", mappings));
	}
}
