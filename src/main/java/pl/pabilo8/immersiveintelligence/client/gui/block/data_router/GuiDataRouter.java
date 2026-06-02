package pl.pabilo8.immersiveintelligence.client.gui.block.data_router;

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
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataRouter;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataRouter.DataRoutingRule;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataRouter.RoutingAction;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerDataRouter;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

/**
 * Main Data Router GUI: list of request/job routing rules.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 02.06.2026
 */
@DecoTemplate(name = "data_router", category = DecoGuiCategory.DATA_TILE)
public class GuiDataRouter extends DecoTileGui<TileEntityDataRouter, ContainerDataRouter>
{
	private static final String KEY = IIReference.GUI_LABEL_KEY+"data_router.";

	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public EasyCollection<DataRoutingRule, NBTTagCompound> rules;
	@SyncNBT
	public int editedRule = -1;
	@SyncNBT
	public ListMode mode = ListMode.JOBS;

	public GuiDataRouter(EntityPlayer player, TileEntityDataRouter tile)
	{
		super(player, tile, IIGUI.DATA_ROUTER);
		if(tile!=null)
			rules = tile.routingRules.clone();
	}

	@Override
	public void onInit()
	{
		if(rules==null)
			rules = new EasyCollection<>(DataRoutingRule::new);

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

		DecoTaskList<DataRoutingRule> ruleList;
		addComponent((ruleList = new DecoTaskList<>(2, 4))
				.withSize(176-4, 116)
				.withEntries(rules)
				.withIsJobPredicate(DataRoutingRule::isJob)
				.withModeHandling(mode, m -> mode = m)
				.withBlankTaskSupplier(() -> {
					DataRoutingRule rule = new DataRoutingRule();
					rule.usageLimit = ruleList.getMode()==ListMode.REQUESTS?1: -1;
					return rule;
				})
				.withDisplayFunction(new DecoEntryPanelBuilder<DataRoutingRule>()
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_TICKET)
						.withComponent("action", new DecoButton(3, 2)
								.withSize(36, 16)
								.withDisabled(true)
								.withTextDisabledColor(IIColor.fromPackedRGB(0xafafaf))
						)
						.withLabel("route", new DecoLabel(fontRenderer, 3+36+2, 5)
								.withSize(58, 12)
								.withAlign(DecoAlignment.LEFT)
						)
						.withComponent(p -> new DecoButton(p.width-17+1, 2)
								.withTemplate(DecoTemplates.ACTION_BUTTON_EDIT)
								.withOnLMBPressed(() -> {
									editedRule = rules.indexOf(p.getCurrentElement());
									if(editedRule >= 0)
										changeGUI(IIGUI.DATA_ROUTER_EDIT);
								})
						)
						.withElementApplyMethod((rule, panel) -> {
							panel.component("action", DecoButton.class)
									.withRawText(rule.action.getLocalizedName())
									.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
									.withBackgroundColor(rule.action==RoutingAction.ALLOW?DecoColors.ACTION_ADD: DecoColors.ACTION_REMOVE);
							panel.label("route").withRawText(I18n.format(KEY+"into",
									I18n.format(IIReference.DESCRIPTION_KEY+"side."+rule.outgoingSide.getName())
							));
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
