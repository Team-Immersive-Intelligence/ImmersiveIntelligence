package pl.pabilo8.immersiveintelligence.client.gui.block.emplacement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoResource;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 16.07.2021
 */
public abstract class GuiEmplacement extends DecoGui<TileEntityEmplacement, ContainerEmplacement>
{
	@DecoResource
	public static ResourceLocation ICON_STORAGE = ResLoc.of(IIReference.RES_II, "gui/tab_icons/storage");
	@DecoResource
	public static ResourceLocation ICON_CONFIG = ResLoc.of(IIReference.RES_II, "gui/tab_icons/config");
	@DecoResource
	public static ResourceLocation ICON_TARGETS = ResLoc.of(IIReference.RES_II, "gui/tab_icons/targets");
	@DecoResource
	public static ResourceLocation ICON_TASKS = ResLoc.of(IIReference.RES_II, "gui/tab_icons/fire_missions");

	public GuiEmplacement(EntityPlayer player, TileEntityEmplacement tile, IIGUI gui)
	{
		super(player, tile, gui);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.GUI_BG_STEEL_ROUGH, 0, 0, 152+96, 152+8)
				.withTitleBar(tile)
				.withNextLayer()
				.withBox(DecoTextures.GUI_BG_WOODEN, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND_WOODEN, 32, 152+8, 176, 92)
				.withFrame(DecoTextures.GUI_FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.build();

		addLinkTab(IIGUI.EMPLACEMENT_STORAGE, ICON_STORAGE, "storage_module");
		addLinkTab(IIGUI.EMPLACEMENT_CONFIG, ICON_CONFIG, "configuration_module");
		addLinkTab(IIGUI.EMPLACEMENT_TARGET_FILTERS, ICON_TARGETS, "targets_module");
		addLinkTab(IIGUI.EMPLACEMENT_FIRE_MISSIONS, ICON_TASKS, "fire_missions_module");
	}
}
