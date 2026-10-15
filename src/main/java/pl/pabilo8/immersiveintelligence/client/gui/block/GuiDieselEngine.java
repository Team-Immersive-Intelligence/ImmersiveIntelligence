package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSlider;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBarGroup;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntityDieselEngine;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerDieselEngine;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import java.util.Locale;

/**
 * Shows Diesel Engine power and its torque-to-speed control.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 15.10.2026
 */
@DecoTemplate(name = "diesel_engine", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiDieselEngine extends DecoTileGui<TileEntityDieselEngine, ContainerDieselEngine>
{
	@SyncNBT(events = SyncEvents.TILE_CLIENT_MESSAGE)
	public float torqueSpeedRatio;

	public GuiDieselEngine(EntityPlayer player, TileEntityDieselEngine tile)
	{
		super(player, tile, IIGUI.DIESEL_ENGINE);
	}

	@Override
	public void onInit()
	{
		this.torqueSpeedRatio = tile.getTorqueSpeedRatio();
		syncAnimatedParts(tile.maintenanceHatch, true);

		//Background
		startBackground()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 0, 176, 76)
				.withStandaloneFrame(24, 12, 128, 56, DecoTextures.FRAME_CORNERS_BRASS, 4, true)
				.withTitleBar(tile)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.build();

		//Components
		addComponents(
				new DecoFluidTank(-2, 4)
						.withHeight(64+7-4)
						.withFluidTank(tile.fuelTank)
						.withColorMarker(IIReference.COLOR_ENGINEERS_BLUE),
				new DecoBarGroup(156, 0)
						.withBar(b -> b.withTemplate(DecoTemplates.BAR_MECH_TORQUE_OUTPUT.apply(tile.rotation))
								.withLimits(0, 128, () -> (int)(tile.rotation.getTorque()*torqueSpeedRatio)))
						.withBar(b -> b.withTemplate(DecoTemplates.BAR_MECH_SPEED_OUTPUT.apply(tile.rotation))
								.withLimits(0, 720, () -> (int)(tile.rotation.getRotationSpeed()/torqueSpeedRatio))),


				//Gear ratio slider
				new DecoSlider(32, 30)
						.withSize(112, 12)
						.withRange(0, 1)
						.withValue((float)(Math.log(torqueSpeedRatio)/Math.log(4)+1)*0.5f)
						.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"diesel_engine.ratio.tooltip")
						.withOnValueChanged(value -> torqueSpeedRatio = (float)Math.pow(4, value*2-1))
		);
		//Gear ratio label
		addLabel(IIReference.INFO_KEY+"gear_ratio_short", this::getRatio, 24, 48)
				.withTranslatedTooltipListener(IIReference.INFO_KEY+"gear_ratio", this::getRatio)
				.withSize(128, 11)
				.withTextColor(IIReference.COLOR_GUI_BRASS)
				.withAlign(DecoAlignment.CENTER);
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		syncAnimatedParts(tile.maintenanceHatch, false);
	}

	private String[] getRatio()
	{
		return new String[]{
				String.format(Locale.ROOT, "%.2f", torqueSpeedRatio < 1?1/torqueSpeedRatio: 1),
				String.format(Locale.ROOT, "%.2f", torqueSpeedRatio >= 1?torqueSpeedRatio: 1)
		};
	}
}
