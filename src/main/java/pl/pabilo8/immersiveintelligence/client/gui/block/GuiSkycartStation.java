package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBarGroup;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.SkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFuelStation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCartStation;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerFuelStation;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerSkycartStation;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemUtils;

import java.util.ArrayList;

import static pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils.renderEnergyBars;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 10.07.2019
 * @since 27.08.2025
 */
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
				.withBox(DecoTextures.GUI_BG_WOODEN, 0, 0, 176, 76)
				.withStandaloneFrame(24, 12, 128, 64-8, DecoTextures.GUI_FRAME_CORNERS_BRASS, 4, true)
				.withTitleBar(tile)
				.withBox(DecoTextures.GUI_BG_WOODEN, 0, 76, 176, 92)
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
