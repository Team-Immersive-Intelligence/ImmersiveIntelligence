package pl.pabilo8.immersiveintelligence.client.gui.block.radar;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRadar;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerRadar;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.04.2023
 */
@DecoTemplate(name = "radar_targets", category = DecoGuiCategory.TERRITORY_CONTROL_TILE)
public class GuiRadarTargets extends DecoTileGui<TileEntityRadar, ContainerRadar>
{
	public GuiRadarTargets(EntityPlayer player, TileEntityRadar tile)
	{
		super(player, tile, IIGUI.RADAR_TARGETS);
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
				.withNextLayer()
				.withBox(DecoTextures.BG_STEEL, DecoTextures.TEMPLATE_SQUARE, 152-4, 0, 96+8, 152)
				.build();

		//Tabs
		addLinkTab(IIGUI.RADAR, GuiRadar.ICON_RADAR, "radar_module");
		addLinkTab(IIGUI.RADAR_CONFIG, DecoTextures.ICON_CONFIG, "configuration_module");
		addLinkTab(IIGUI.RADAR_TARGETS, DecoTextures.ICON_TARGETS, "targets_module");

	}
}
