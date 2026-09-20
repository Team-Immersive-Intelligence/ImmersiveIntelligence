package pl.pabilo8.immersiveintelligence.client.gui.block.emplacement;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.tree.target.GuiTargetDecisionTree;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetConfiguration;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerEmplacement;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * Edits Emplacement target-tree presets and synchronizes each valid change.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.09.2026
 * @since 16.07.2021
 */
@DecoTemplate(name = "emplacement_target_filters", category = DecoGuiCategory.TERRITORY_CONTROL_TILE)
public class GuiEmplacementTargets extends GuiTargetDecisionTree<TileEntityEmplacement, ContainerEmplacement>
{
	public GuiEmplacementTargets(EntityPlayer player, TileEntityEmplacement tile)
	{
		super(player, tile, IIGUI.EMPLACEMENT_TARGET_FILTERS,
				tile==null?null: tile.taskManager.copyTargetConfiguration());
	}

	@Override
	protected void initTargetEditorGui()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 152+96, 152+8+8)
				.withTitleBar(tile)
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 32, 152+8, 176, 92)
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.build();

		//Tabs
		addLinkTab(IIGUI.EMPLACEMENT_STORAGE, DecoTextures.ICON_STORAGE, "storage_module");
		addLinkTab(IIGUI.EMPLACEMENT_CONFIG, DecoTextures.ICON_CONFIG, "configuration_module");
		addLinkTab(IIGUI.EMPLACEMENT_TARGET_FILTERS, DecoTextures.ICON_TARGETS, "targets_module");
		addLinkTab(IIGUI.EMPLACEMENT_FIRE_MISSIONS, DecoTextures.ICON_FIRE_MISSIONS, "fire_missions_module");
	}

	@Override
	protected void sendTargetConfigurationUpdate(TargetConfiguration configuration)
	{
		IIPacketHandler.sendToServer(new MessageIITileSync(tile, EasyNBT.newNBT()
				.withTag("tasks", tile.taskManager.createTargetConfigurationUpdate(configuration))));
	}
}
