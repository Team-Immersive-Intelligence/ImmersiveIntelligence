package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRadar;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerRadar;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.04.2023
 */
@DecoTemplate(name = "radar_tasks", category = DecoGuiCategory.TERRITORY_CONTROL_TILE)
public class GuiRadarTasks extends DecoGui<TileEntityRadar, ContainerRadar>
{
	public GuiRadarTasks(EntityPlayer player, TileEntityRadar tile)
	{
		super(player, tile, IIGUI.RADAR_TASKS);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.GUI_BG_STEEL, 0, 0, 152+96, 152)
				.withTitleBar(tile)
				.withBox(DecoTextures.GUI_BG_WOODEN, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND_WOODEN, 32, 152, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.withNextLayer()
				.withBox(DecoTextures.GUI_BG_STEEL, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_SQUARE, 152-4, 0, 96+8, 152)
				.build();

		addComponents(
				new DecoTab()
						.withLink(IIGUI.RADAR)
						.withIcon(GuiRadar.ICON_RADAR)
						.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"radar_module"),
				new DecoTab()
						.withLink(IIGUI.RADAR_STATUS)
						.withIcon(GuiRadar.ICON_CONFIG)
						.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"status_module"),
				new DecoTab()
						.withLink(IIGUI.RADAR_TASKS)
						.withIcon(GuiRadar.ICON_TARGETS)
						.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"tasks_module")
		);
	}
}
