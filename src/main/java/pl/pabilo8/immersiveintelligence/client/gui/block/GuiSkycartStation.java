package pl.pabilo8.immersiveintelligence.client.gui.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBarGroup;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCartStation;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerSkycartStation;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @updated 27.08.2025
 * @ii-approved 0.3.1
 * @since 10.07.2019
 */
@DecoTemplate(name = "skycart_station", category = DecoGuiCategory.PRODUCTION_TILE)
public class GuiSkycartStation extends DecoGui<TileEntitySkyCartStation, ContainerSkycartStation>
{
	public GuiSkycartStation(EntityPlayer player, TileEntitySkyCartStation tile)
	{
		super(player, tile, IIGUI.SKYCART_STATION);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.GUI_BG_WOODEN, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND_WOODEN, 0, 0, 176, 76)
				.withStandaloneFrame(24, 12, 128, 64-8, DecoTextures.GUI_FRAME_CORNERS_BRASS, 4, true)
				.withTitleBar(tile)
				.withBox(DecoTextures.GUI_BG_WOODEN, DecoTextures.RES_TEXTURES_DECO_TEMPLATE_ROUND_WOODEN, 0, 76, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.build();

		addComponents(
				new DecoBarGroup(128+28, 0)
						.withBar(b -> b.withTemplate(DecoGuiUtils.BAR_MECH_TORQUE.apply(tile.rotation)))
						.withBar(b -> b.withTemplate(DecoGuiUtils.BAR_MECH_SPEED.apply(tile.rotation)))
		);

		addLabel(IIReference.INFO_KEY+"gear_ratio_short", this::getRatio, 24, 48)
				.withTranslatedTooltipListener(IIReference.INFO_KEY+"gear_ratio", this::getRatio)
				.withSize(128, 11)
				.withTextColor(IIReference.COLOR_GUI_BRASS)
				.withAlign(DecoAlignment.CENTER);
	}

	private String[] getRatio()
	{
		float torqueRatio = IIRotaryUtils.getGearTorqueRatio(tile.getInventory());

		//speed : torque
		float speed = torqueRatio < 1&&torqueRatio!=0?1f/torqueRatio: 1;
		float torque = torqueRatio >= 1?torqueRatio: 1;

		return new String[]{
				TextFormatting.GOLD+(speed%1==0?Integer.toString((int)speed): Float.toString(speed))+TextFormatting.RESET,
				TextFormatting.GOLD+(torque%1==0?Integer.toString((int)torque): Float.toString(torque))+TextFormatting.RESET
		};
	}
}
