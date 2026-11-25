package pl.pabilo8.immersiveintelligence.client.gui.block.data_device;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTabGroup;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor.DecoDataEditor;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMapper;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMapper.MappingRule;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMapper.ValueMapping;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerSmallDataDevice;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

/**
 * Edits both values of one mapping with the standard data editor.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 25.10.2026
 * @since 24.10.2026
 */
@DecoTemplate(name = "data_mapper_value", category = DecoGuiCategory.DATA_TILE)
public class GuiDataMapperValue extends DecoTileGui<TileEntityDataMapper, ContainerSmallDataDevice<TileEntityDataMapper>>
{
	private static final String KEY = "ii.gui.small_data_devices.";

	@SyncNBT
	@SuppressWarnings("unused")
	public EasyCollection<MappingRule, NBTTagCompound> mappings = new EasyCollection<>(MappingRule::new);
	@SyncNBT
	public int editedRule = -1, editedValue = -1;
	@SyncNBT
	public MappingRule edited = new MappingRule();
	@SyncNBT
	public boolean loaded = true, valueLoaded = false, output = false;
	@SyncNBT
	public ValueMapping value = new ValueMapping();
	private DecoDataEditor<?> editor;

	public GuiDataMapperValue(EntityPlayer player, TileEntityDataMapper tile)
	{
		super(player, tile, IIGUI.DATA_MAPPER_VALUE);
	}

	@Override
	public void onInit()
	{
		if(editedValue < 0||editedValue > edited.values.size())
		{
			changeGUI(IIGUI.DATA_MAPPER_EDIT);
			return;
		}
		if(!valueLoaded)
		{
			if(editedValue < edited.values.size())
				value = edited.values.copyEntry(edited.values.get(editedValue));
			else
			{
				value.expected = edited.inputType.clone();
				value.output = edited.outputType.clone();
			}
			valueLoaded = true;
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
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 4, 26, 232, 136)
				.build();
		DecoTabGroup tabs = new DecoTabGroup(4, 8)
				.withSize(232, 18)
				.withHorizontalAlignment(true)
				.withTabWidth(116)
				.withTab(new DecoTab().withText(KEY+"input_value"), () -> {
					storeValue();
					output = false;
					refreshGUI();
				})
				.withTab(new DecoTab().withText(KEY+"output_value"), () -> {
					storeValue();
					output = true;
					refreshGUI();
				});
		tabs.selectTab(output?1: 0, false);
		addComponent(tabs);
		editor = DecoDataEditor.getEditorFor(output?value.output: value.expected, 8, 30);
		if(editor!=null)
			addComponent(editor).withSize(224, 128);
		addComponents(
				new DecoButton(182, 162)
						.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
						.withSize(48, 12)
						.withText("ii.gui.button.apply")
						.withOnLMBPressed(() -> {
							storeValue();
							if(editedValue==edited.values.size())
								edited.values.add(edited.values.copyEntry(value));
							else
								edited.values.set(editedValue, edited.values.copyEntry(value));
							changeGUI(IIGUI.DATA_MAPPER_EDIT);
						}),
				new DecoButton(130, 162)
						.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
						.withSize(48, 12)
						.withText("ii.gui.button.cancel")
						.withOnLMBPressed(() -> changeGUI(IIGUI.DATA_MAPPER_EDIT))
		);
	}

	private void storeValue()
	{
		if(editor!=null)
			if(output)
				value.output = editor.outputType();
			else
				value.expected = editor.outputType();
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		return EasyNBT.newNBT();
	}
}
