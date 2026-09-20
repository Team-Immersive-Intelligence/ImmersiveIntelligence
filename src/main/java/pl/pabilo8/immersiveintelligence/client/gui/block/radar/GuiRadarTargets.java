package pl.pabilo8.immersiveintelligence.client.gui.block.radar;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.target.GuiTargetDecisionTree;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRadar;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetConfiguration;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerRadar;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * Edits the Radar target decision tree.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 16.09.2026
 * @since 28.04.2023
 */
@DecoTemplate(name = "radar_targets", category = DecoGuiCategory.TERRITORY_CONTROL_TILE)
public class GuiRadarTargets extends GuiTargetDecisionTree<TileEntityRadar, ContainerRadar>
{
	public GuiRadarTargets(EntityPlayer player, TileEntityRadar tile)
	{
		super(player, tile, IIGUI.RADAR_TARGETS,
				tile==null?null: tile.targetManager.copyTargetConfiguration());
	}

	@Override
	protected void initTargetEditorGui()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 152+96, 152+8+8)
				.withTitleBar(tile)
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 32, 152+8, 176, 92-4)
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.build();

		addLinkTab(IIGUI.RADAR, GuiRadar.ICON_RADAR, "radar_module");
		addLinkTab(IIGUI.RADAR_CONFIG, DecoTextures.ICON_CONFIG, "configuration_module");
		addLinkTab(IIGUI.RADAR_TARGETS, DecoTextures.ICON_TARGETS, "targets_module");
	}

	@Override
	protected void sendTargetConfigurationUpdate(TargetConfiguration configuration)
	{
		IIPacketHandler.sendToServer(new MessageIITileSync(tile, EasyNBT.newNBT()
				.withTag("targets", tile.targetManager.createTargetConfigurationUpdate(configuration))));
	}
}
