package pl.pabilo8.immersiveintelligence.client.gui.block.emplacement;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerEmplacement;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 16.07.2021
 */
public abstract class GuiEmplacement extends DecoGui<TileEntityEmplacement, ContainerEmplacement>
{
	public GuiEmplacement(EntityPlayer player, TileEntityEmplacement tile, IIGUI gui)
	{
		super(player, tile, gui);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_STEEL_ROUGH, 0, 0, 152+96, 152+8)
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
}
