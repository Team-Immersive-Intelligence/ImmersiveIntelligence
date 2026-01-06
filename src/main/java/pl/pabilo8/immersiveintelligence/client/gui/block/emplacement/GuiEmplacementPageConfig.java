package pl.pabilo8.immersiveintelligence.client.gui.block.emplacement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoMapDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.DecoMapDefaultColorMapper;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.scanners.BlockTypeScanner;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFlagpole;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 16.07.2021
 */
@DecoTemplate(name = "emplacement_config", category = DecoGuiCategory.TERRITORY_CONTROL_TILE)
public class GuiEmplacementPageConfig extends GuiEmplacement
{
	public GuiEmplacementPageConfig(EntityPlayer player, TileEntityEmplacement tile)
	{
		super(player, tile, IIGUI.EMPLACEMENT_CONFIG);
	}

	@Override
	public void onInit()
	{
		super.onInit();

		BlockPos pos = new BlockPos(tile.getWeaponCenter());
		addComponents(
				new DecoPanel(0, 8)
						.withSize(128, 128)
						.withBackground(DecoTextures.GUI_BG_PAPER)
						.withBackgroundMask(DecoTextures.RES_TEXTURES_DECO_TEMPLATE_PAPER),
				new DecoPanel(0, 128+8)
						.withSize(128, 24)
						.withBackground(DecoTextures.GUI_BG_STEEL)
						.withBackgroundMask(DecoTextures.RES_TEXTURES_DECO_TEMPLATE_SQUARE),

				new DecoMapDisplay(4, 4+8)
						.withSize(120, 120)
						.withRegion(pos.getX(), pos.getZ(), 128)
						.withBaseZoom(2)
						.withColorMapper(DecoMapDefaultColorMapper.TERRAIN)
						.withPanning(false)
						.withZoomScrolling(0.5, 2)
						.withScanner(new BlockTypeScanner("flagpoles")
								.withMultiblockFilter(TileEntityFlagpole.class)
								.withMarkerStyle(DecoTextures.MAP_MARKER_FLAGPOLE, 8, IIColor.WHITE)
						)
						.withScanner(new BlockTypeScanner("weapons")
								.withMultiblockFilter(TileEntityEmplacement.class)
								.withMultiblockFilter(TileEntityArtilleryHowitzer.class)
								.withMarkerStyle(DecoTextures.MAP_MARKER_EMPLACEMENT, 8, IIColor.WHITE)
						),

				//
				new DecoPanel(128, 8)
						.withSize(120-4, 152-8+4)
						.withBackground(DecoTextures.GUI_BG_PAPER)
						.withBackgroundMask(DecoTextures.RES_TEXTURES_DECO_TEMPLATE_PAPER)
		);
	}
}
