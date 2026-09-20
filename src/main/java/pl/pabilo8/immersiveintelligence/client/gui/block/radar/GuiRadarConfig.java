package pl.pabilo8.immersiveintelligence.client.gui.block.radar;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoCheckbox;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRadar;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerRadar;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 16.09.2026
 * @since 28.04.2023
 */
@DecoTemplate(name = "radar_config", category = DecoGuiCategory.TERRITORY_CONTROL_TILE)
public class GuiRadarConfig extends DecoTileGui<TileEntityRadar, ContainerRadar>
{
	private static final String KEY = IIReference.GUI_LABEL_KEY+"radar.config.";
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public boolean redstoneControlEnabled, dataOutputEnabled;

	public GuiRadarConfig(EntityPlayer player, TileEntityRadar tile)
	{
		super(player, tile, IIGUI.RADAR_CONFIG);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 152+96, 152)
				.withTitleBar(tile)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 32, 152, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.build();

		//Tabs
		addLinkTab(IIGUI.RADAR, GuiRadar.ICON_RADAR, "radar_module");
		addLinkTab(IIGUI.RADAR_CONFIG, DecoTextures.ICON_CONFIG, "configuration_module");
		addLinkTab(IIGUI.RADAR_TARGETS, DecoTextures.ICON_TARGETS, "targets_module");

		addComponents(
				new DecoPanel(4, 12)
						.withSize(152+96-8, 40)
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_PAPER),
				new DecoCheckbox(8, 18)
						.withSize(152+96-16, 12)
						.withText(KEY+"reacts_redstone")
						.withChecked(redstoneControlEnabled = tile.redstoneControlEnabled)
						.withOnToggle(value -> redstoneControlEnabled = value)
						.withTranslatedTooltip(KEY+"reacts_redstone.tooltip"),
				new DecoCheckbox(8, 34)
						.withSize(152+96-16, 12)
						.withText(KEY+"data_output")
						.withChecked(dataOutputEnabled = tile.dataOutputEnabled)
						.withOnToggle(value -> dataOutputEnabled = value)
						.withTranslatedTooltip(KEY+"data_output.tooltip")
		);
	}
}
