package pl.pabilo8.immersiveintelligence.client.gui.block.emplacement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
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
		AxisAlignedBB sightRange = new AxisAlignedBB(pos), fireRange = new AxisAlignedBB(pos);
		if(tile.currentWeapon!=null)
		{
			sightRange = tile.currentWeapon.getDetectionRangeBB();
			fireRange = tile.currentWeapon.getAttackRangeBB();
		}

		addComponents(
				new DecoPanel(0, 8)
						.withSize(128, 128)
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_PAPER),
				new DecoPanel(0, 128+8)
						.withSize(128, 24)
						.withBackground(DecoTextures.BG_STEEL)
						.withBackgroundMask(DecoTextures.TEMPLATE_SQUARE),

				new DecoMapDisplay(4, 4+8)
						.withSize(120, 120)
						.withRegion(pos.getX(), pos.getZ(), 128)
						.withBaseZoom(2)
						.withColorMapper(DecoMapDefaultColorMapper.TERRAIN)
						.withPanning(false)
						.withZoomScrolling(0.5, 2)
						//Sight Range
						.withLayer("sight")
						.addRectangle((int)sightRange.minX, (int)sightRange.minZ, (int)sightRange.maxX, (int)sightRange.maxZ, IIColor.MC_BLUE.withAlpha(127f))
						.build()
						//Fire Range
						.withLayer("fire")
						.addRectangle((int)fireRange.minX, (int)fireRange.minZ, (int)fireRange.maxX, (int)fireRange.maxZ, IIColor.MC_RED.withAlpha(127f))
						.build()
						//Markers
						.withScanner(new BlockTypeScanner("flagpoles")
								.withMultiblockFilter(TileEntityFlagpole.class)
								.withMarkerStyle(DecoTextures.MAP_MARKER_FLAGPOLE, 8, IIColor.WHITE)
						)
						.withScanner(new BlockTypeScanner("emplacement")
								.withMultiblockFilter(TileEntityEmplacement.class)
								.withMarkerStyle(DecoTextures.MAP_MARKER_EMPLACEMENT, 8, IIColor.WHITE)
						)
						.withScanner(new BlockTypeScanner("howitzer")
								.withMultiblockFilter(TileEntityArtilleryHowitzer.class)
								.withMarkerStyle(DecoTextures.MAP_MARKER_ARTILLERY_HOWITZER, 8, IIColor.WHITE)
						),

				//
				new DecoPanel(128, 8)
						.withSize(120-4, 152-8+4)
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
		);
	}
}
