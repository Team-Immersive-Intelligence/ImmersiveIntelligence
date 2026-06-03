package pl.pabilo8.immersiveintelligence.client.gui.block.emplacement;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSlider;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSwitch;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoMapDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.DecoMapDefaultColorMapper;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.scanners.BlockTypeScanner;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFlagpole;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 16.07.2021
 */
@DecoTemplate(name = "emplacement_config", category = DecoGuiCategory.TERRITORY_CONTROL_TILE)
public class GuiEmplacementPageConfig extends GuiEmplacement
{
	private static final String KEY = IIReference.GUI_LABEL_KEY+"emplacement.config.";
	@SyncNBT(events = {SyncEvents.TILE_CLIENT_MESSAGE})
	public boolean redstoneControlEnabled, dataControlEnabled;
	@SyncNBT(events = {SyncEvents.TILE_CLIENT_MESSAGE})
	public float weaponHideHealthThreshold, weaponRepairSatisfactoryThreshold;

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

		addLabel(I18n.format(KEY+"vision_range", calculateRange(sightRange, pos)), 4, 132+8-1)
				.withSize(120, 10)
				.withAlign(DecoAlignment.LEFT);
		addLabel(I18n.format(KEY+"attack_range", calculateRange(fireRange, pos)), 4, 142+8-1)
				.withSize(120, 10)
				.withAlign(DecoAlignment.LEFT);

		addComponent(new DecoSwitch(136-4-2, 16-2)
				.withText(KEY+"reacts_redstone")
				.withSize(22, 14)
				.withCurrentState(this.redstoneControlEnabled = tile.redstoneControlEnabled)
				.withOnToggle(value -> this.redstoneControlEnabled = value)
				.withTranslatedTooltip(KEY+"reacts_redstone.tooltip"));
		addComponent(new DecoSwitch(136-4-2, 34-2-8+2)
				.withText(KEY+"reacts_data")
				.withSize(22, 14)
				.withCurrentState(this.dataControlEnabled = tile.dataControlEnabled)
				.withOnToggle(value -> this.dataControlEnabled = value)
				.withTranslatedTooltip(KEY+"reacts_data.tooltip"));

		addLabel(KEY+"hide_health", 136-4-2, 60-20-2)
				.withSize(110, 24)
				.withWrapping(true)
				.withAlign(DecoAlignment.LEFT);
		addComponent(new DecoSlider(136-4-2+1, 72-20+8)
				.withSize(110, 12)
				.withRange(0f, 1f)
				.withValue(this.weaponHideHealthThreshold = tile.weaponHideHealthThreshold)
				.withOnValueChanged(value -> this.weaponHideHealthThreshold = value)
				.withBarColors(DecoColors.ARMOR_INTEGRITY_1, DecoColors.ARMOR_INTEGRITY_2)
				.withTranslatedTooltip(KEY+"hide_health.tooltip"));

		addLabel(KEY+"resurface_health", 136-4-2, 60-20+32-2)
				.withSize(110, 24)
				.withWrapping(true)
				.withAlign(DecoAlignment.LEFT);
		addComponent(new DecoSlider(136-4-2+1, 72-20+8+32)
				.withSize(110, 12)
				.withRange(0f, 1f)
				.withValue(this.weaponRepairSatisfactoryThreshold = tile.weaponRepairSatisfactoryThreshold)
				.withOnValueChanged(value -> this.weaponRepairSatisfactoryThreshold = Math.max(tile.weaponHideHealthThreshold, value))
				.withBarColors(DecoColors.ARMOR_INTEGRITY_1, DecoColors.ARMOR_INTEGRITY_2)
				.withTranslatedTooltip(KEY+"resurface_health.tooltip"));

	}

	private String calculateRange(AxisAlignedBB range, BlockPos center)
	{
		double value = Math.max(
				Math.max(Math.abs(range.minX-center.getX()), Math.abs(range.maxX-center.getX())),
				Math.max(Math.abs(range.minZ-center.getZ()), Math.abs(range.maxZ-center.getZ()))
		);
		return String.valueOf((int)Math.ceil(value));
	}
}
