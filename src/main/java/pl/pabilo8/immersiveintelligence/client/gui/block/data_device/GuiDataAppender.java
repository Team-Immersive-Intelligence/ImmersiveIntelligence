package pl.pabilo8.immersiveintelligence.client.gui.block.data_device;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.data_device.DataDeviceUtils;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataAppender;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataAppender.AppendRule;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerSmallDataDevice;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

/**
 * Lists ordered variable addition and removal entries.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 25.10.2026
 * @since 24.10.2026
 */
@DecoTemplate(name = "data_appender", category = DecoGuiCategory.DATA_TILE)
public class GuiDataAppender extends DecoTileGui<TileEntityDataAppender, ContainerSmallDataDevice<TileEntityDataAppender>>
{
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public EasyCollection<AppendRule, NBTTagCompound> entries = new EasyCollection<>(AppendRule::new);
	@SyncNBT
	public int editedRule = -1;

	public GuiDataAppender(EntityPlayer player, TileEntityDataAppender tile)
	{
		super(player, tile, IIGUI.DATA_APPENDER);
		if(tile!=null)
			entries = tile.entries.clone();
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_ROUND, 0, 0, 176, 8)
				.withTitleBar(tile)
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 0, 8, 176, 160)
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 168, 176, 92)
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();
		addComponent(new DecoList<AppendRule>(4, 8).withSize(168, 156).withEntries(entries)
				.withCreateLaterAction(() -> {
					if(entries.size() < DataDeviceUtils.MAX_RULES)
					{
						editedRule = entries.size();
						changeGUI(IIGUI.DATA_APPENDER_EDIT);
					}
				})
				.withDisplayFunction(new DecoEntryPanelBuilder<AppendRule>().withHeight(20)
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_TICKET)
						.withComponent("action", p -> new DecoButton(3, 2).withSize(62, 16)
								.withDisabled(true)
								.withTextDisabledColor(DecoColors.TEXT))
						.withComponent(p -> new DecoImage(68, 2).withSize(16, 16)
								.withImageLocation(p.getCurrentElement().value.getTextureLocation(), true))
						.withLabel("variable", p -> new DecoLabel(fontRenderer, 88, 6).withSize(16, 10)
								.withAlign(DecoAlignment.LEFT)
								.withRawText(String.valueOf(Character.toLowerCase(p.getCurrentElement().variable))))
						.withComponent(p -> new DecoButton(p.width-45, 3).withTemplate(DecoTemplates.ACTION_BUTTON_EDIT)
								.withOnLMBPressed(() -> {
									editedRule = entries.indexOf(p.getCurrentElement());
									changeGUI(IIGUI.DATA_APPENDER_EDIT);
								}))
						.withComponent(p -> new DecoButton(p.width-31, 3).withTemplate(DecoTemplates.ACTION_BUTTON_DUPLICATE)
								.withOnLMBPressed(() -> {
									if(entries.size() < DataDeviceUtils.MAX_RULES)
										entries.add(entries.copyEntry(p.getCurrentElement()));
									refreshGUI();
								}))
						.withComponent(p -> new DecoButton(p.width-17, 3).withTemplate(DecoTemplates.ACTION_BUTTON_REMOVE)
								.withOnLMBPressed(() -> {
									entries.remove(p.getCurrentElement());
									refreshGUI();
								}))
						.withElementApplyMethod((entry, panel) -> panel.component("action", DecoButton.class)
								.withText("ii.gui.data_appender.action."+(entry.remove?"remove": entry.overwrite?"overwrite": "add"))
								.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
								.withBackgroundColor(entry.remove?DecoColors.ACTION_REMOVE: entry.overwrite?DecoColors.IN: DecoColors.ACTION_ADD))));
	}
}
