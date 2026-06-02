package pl.pabilo8.immersiveintelligence.client.gui.block.data_merger;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoTaskList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoTaskList.ListMode;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMerger;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMerger.DataMergeRule;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerDataMerger;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

/**
 * Main Data Merger GUI: list of request/job merge rules.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 02.06.2026
 */
@DecoTemplate(name = "data_merger", category = DecoGuiCategory.DATA_TILE)
public class GuiDataMerger extends DecoTileGui<TileEntityDataMerger, ContainerDataMerger>
{
	private static final String KEY = IIReference.GUI_LABEL_KEY+"data_merger.";

	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public EasyCollection<DataMergeRule, NBTTagCompound> rules;
	@SyncNBT
	public int editedRule = -1;
	@SyncNBT
	public ListMode mode = ListMode.JOBS;

	public GuiDataMerger(EntityPlayer player, TileEntityDataMerger tile)
	{
		super(player, tile, IIGUI.DATA_MERGER);
		if(tile!=null)
			rules = tile.mergeRules.clone();
	}

	@Override
	public void onInit()
	{
		if(rules==null)
			rules = new EasyCollection<>(DataMergeRule::new);

		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 176, 168-8)
				.withTitleBar(tile)
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 168-8, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withNextLayer()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 0, 168-8-20, 176, 20)
				.build();

		DecoTaskList<DataMergeRule> ruleList;
		addComponent((ruleList = new DecoTaskList<>(2, 4))
				.withSize(176-4, 116)
				.withEntries(rules)
				.withIsJobPredicate(DataMergeRule::isJob)
				.withModeHandling(mode, m -> mode = m)
				.withBlankTaskSupplier(() -> {
					DataMergeRule rule = new DataMergeRule();
					rule.usageLimit = ruleList.getMode()==ListMode.REQUESTS?1: -1;
					return rule;
				})
				.withDisplayFunction(new DecoEntryPanelBuilder<DataMergeRule>()
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_TICKET)
						.withComponent("variable", new DecoButton(3, 2)
								.withSize(20, 16)
								.withDisabled(true)
								.withTextDisabledColor(IIColor.fromPackedRGB(0xafafaf))
						)
						.withLabel("route", new DecoLabel(fontRenderer, 26, 5)
								.withSize(116, 12)
								.withAlign(DecoAlignment.LEFT)
						)
						.withComponent(p -> new DecoButton(p.width-17+1-14, 3)
								.withTemplate(DecoTemplates.ACTION_BUTTON_EDIT)
								.withOnLMBPressed(() -> {
									editedRule = rules.indexOf(p.getCurrentElement());
									if(editedRule >= 0)
										changeGUI(IIGUI.DATA_MERGER_EDIT);
								})
						)
						.withComponent(p -> new DecoButton(p.width-17+1, 3)
								.withTemplate(DecoTemplates.ACTION_BUTTON_REMOVE)
								.withOnLMBPressed(() -> {
									int index = rules.indexOf(p.getCurrentElement());
									if(index >= 0)
									{
										rules.remove(index);
										refreshGUI();
									}
								})
						)
						.withElementApplyMethod((rule, panel) -> {
							panel.component("variable", DecoButton.class)
									.withRawText(String.valueOf(rule.variable))
									.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
									.withBackgroundColor(rule.triggerForwarding?DecoColors.ACTION_ADD: DecoColors.ACTION_REMOVE);

							StringBuilder sb = new StringBuilder();
							if(rule.acceptLeft)
								sb.append(I18n.format(KEY+"left"));
							if(rule.acceptRight)
							{
								if(sb.length() > 0)
									sb.append(", ");
								sb.append(I18n.format(KEY+"right"));
							}
							if(rule.triggerForwarding)
							{
								if(sb.length() > 0)
									sb.append(" -> ");
								sb.append(I18n.format(KEY+"send"));
							}

							panel.label("route").withRawText(sb.toString());
						})
				)
		);
	}

	@Override
	protected EasyNBT onSaveTileData()
	{
		return super.onSaveTileData()
				.withSerializable("rules", rules);
	}
}
