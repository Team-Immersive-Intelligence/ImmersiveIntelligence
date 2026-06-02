package pl.pabilo8.immersiveintelligence.client.gui.block.data_merger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoCheckbox;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoDropdownDataLetters;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoTaskList.ListMode;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMerger;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMerger.DataMergeRule;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerDataMerger;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

/**
 * Data Merger rule editor.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 02.06.2026
 */
@DecoTemplate(name = "data_merger_edit", category = DecoGuiCategory.DATA_TILE)
public class GuiDataMergerEdit extends DecoTileGui<TileEntityDataMerger, ContainerDataMerger>
{
	private static final String KEY = IIReference.GUI_LABEL_KEY+"data_merger.";

	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public EasyCollection<DataMergeRule, NBTTagCompound> rules;
	@SyncNBT
	public int editedRule = -1;
	@SyncNBT
	public ListMode mode = ListMode.JOBS;
	@SyncNBT
	public DataMergeRule edited = new DataMergeRule();

	private boolean cancel = true, loaded = false;

	public GuiDataMergerEdit(EntityPlayer player, TileEntityDataMerger tile)
	{
		super(player, tile, IIGUI.DATA_MERGER_EDIT);
		if(tile!=null)
			rules = tile.mergeRules.clone();
	}

	@Override
	public void onInit()
	{
		if(rules==null)
			rules = new EasyCollection<>(DataMergeRule::new);
		if(editedRule < 0||editedRule >= rules.size())
		{
			cancel = true;
			changeGUI(IIGUI.DATA_MERGER);
			return;
		}
		if(!loaded)
		{
			edited = rules.get(editedRule).copy();
			loaded = true;
		}

		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 176, 168-8)
				.withTitleBar(KEY+"rule_properties")
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 168-8, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withNextLayer()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_ROUND, 4, 8+4, 168-4, 72+8)
				.withTitleBar(KEY+"inputs", DecoAlignment.TOP_LEFT)
				.withNextLayer()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_ROUND, 4, 8+4+72-4+16, 168-4, 84-16-8-8)
				.withTitleBar(KEY+"override_policy", DecoAlignment.TOP_LEFT)
				.build();

		DecoPanel panel = addComponent(new DecoPanel(12, 16))
				.withSize(216, 128)
				.withBackground(null);

		addInputSection(panel, 0);
		addPolicySection(panel, 96-4);

		addComponents(
				new DecoButton(xSize-48-4, 156+8-8-2)
						.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
						.withText("ii.gui.button.apply")
						.withSize(48, 12)
						.withOnPressed((gui, button, mouseX, mouseY) -> {
							rules.set(editedRule, edited.copy());
							cancel = false;
							return changeGUI(IIGUI.DATA_MERGER);
						}),
				new DecoButton(xSize-48*2-4, 156+8-8-2)
						.withBackground(DecoTextures.COMPONENT_BUTTON_ROUND)
						.withText("ii.gui.button.cancel")
						.withSize(48, 12)
						.withOnPressed((gui, button, mouseX, mouseY) -> {
							cancel = true;
							return changeGUI(IIGUI.DATA_MERGER);
						})
		);
	}

	private void addInputSection(DecoPanel panel, int yy)
	{
		DecoPanel panelPaper = panel.addComponent(new DecoPanel(-4, yy+4)
				.withBackground(DecoTextures.BG_PAPER)
				.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
				.withSize(176-8-12+4, 18)
		);
		panelPaper.addLabel(KEY+"variable", 4, 6)
				.withSize(48, 9)
				.withAlign(DecoAlignment.LEFT);
		panelPaper.addComponent(new DecoDropdownDataLetters(176-8-12+4-18, 0)
				.withConstraints(new DataPacket())
				.withSelectedEntry((Character)edited.variable)
				.withTextColor(DecoColors.H1, IIColor.fromPackedRGB(0x35322c))
				.withBackground(DecoTextures.COMPONENT_DROPDOWN_DATA_LETTER_PAPER)
				.withDropdownSymbol(DecoTextures.COMPONENT_DROPDOWN_SYMBOL_PAPER)
				.withOnSelectedEntry((oldChar, newChar) -> edited.variable = newChar)
		);

		panel.addComponent(new DecoCheckbox(0, yy+22)
				.withSize(84, 12)
				.withText(KEY+"accept_left")
				.withChecked(edited.acceptLeft)
				.withOnToggle(checked -> edited.acceptLeft = checked)
				.withTranslatedTooltip(KEY+"accept_left.tooltip")
		);
		panel.addComponent(new DecoCheckbox(0, yy+22+12)
				.withSize(84, 12)
				.withText(KEY+"accept_right")
				.withChecked(edited.acceptRight)
				.withOnToggle(checked -> edited.acceptRight = checked)
				.withTranslatedTooltip(KEY+"accept_right.tooltip")
		);
		panel.addComponent(new DecoCheckbox(0, yy+22+12+12)
				.withSize(120, 12)
				.withText(KEY+"trigger_forwarding")
				.withChecked(edited.triggerForwarding)
				.withOnToggle(checked -> edited.triggerForwarding = checked)
				.withTranslatedTooltip(KEY+"trigger_forwarding.tooltip")
		);

		panel.addLabel(KEY+"usage_limit", 0, yy+22+12+12+8+4+3)
				.withSize(74, 9)
				.withAlign(DecoAlignment.LEFT)
				.withTranslatedTooltip(KEY+"usage_limit.tooltip");
		panel.addComponent(new DecoTextField(78, yy+22+12+12+8+4-1)
				.withSize(42, 16)
				.withFilter(TextFilter.DECIMAL)
				.withText(edited.usageLimit < 0?"": String.valueOf(edited.usageLimit))
				.withOnTextChanged(text -> edited.usageLimit = text==null||text.trim().isEmpty()?-1: IIStringUtil.parseInt(text.trim()))
				.withTranslatedTooltip(KEY+"usage_limit.tooltip")
		);
	}

	private void addPolicySection(DecoPanel panel, int yy)
	{
		panel.addComponent(new DecoCheckbox(0, yy)
				.withSize(164, 12)
				.withText(KEY+"right_overrides_cached")
				.withTranslatedTooltip(KEY+"right_overrides_cached.tooltip")
				.withChecked(edited.rightOverridesCachedValue)
				.withOnToggle(checked -> edited.rightOverridesCachedValue = checked)
		);
		panel.addComponent(new DecoCheckbox(0, yy+16)
				.withSize(164, 12)
				.withText(KEY+"right_overrides_value")
				.withTranslatedTooltip(KEY+"right_overrides_value.tooltip")
				.withChecked(edited.rightOverridesValue)
				.withOnToggle(checked -> edited.rightOverridesValue = checked)
		);
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		return super.onSaveTileData()
				.conditionally(!cancel&&rules!=null, easyNBT -> easyNBT.withSerializable("rules", rules));
	}
}
