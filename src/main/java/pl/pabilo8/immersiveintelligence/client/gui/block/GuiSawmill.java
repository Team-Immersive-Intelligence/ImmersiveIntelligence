package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Sawmill;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySawmill;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerSawmill;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;

import java.util.ArrayList;

import static pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils.renderEnergyBars;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class GuiSawmill extends GuiIEContainerBase
{
	public static final String texture_skycrate_station = ImmersiveIntelligence.MODID+":textures/gui/sawmill.png";
	TileEntitySawmill tile;

	public GuiSawmill(EntityPlayer player, TileEntitySawmill tile)
	{
		super(new ContainerSawmill(player, tile));
		this.ySize = 168;
		this.tile = tile;

		IIPacketHandler.sendToServer(new MessageBooleanAnimatedPartsSync(true, 0, tile.getPos()));
	}

	@Override
	public void onGuiClosed()
	{
		IIPacketHandler.sendToServer(new MessageBooleanAnimatedPartsSync(false, 0, tile.getPos()));
		super.onGuiClosed();
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		IIClientUtils.drawStringCentered(fontRenderer, I18n.format("tile."+ImmersiveIntelligence.MODID+".wooden_multiblock.sawmill.name"), 0, 0, getXSize(), 6, 0xd99747);
		renderEnergyBars(148, 20, 7, 48, 2, tile.rotation, Sawmill.rpmMin, Sawmill.torqueMin);

		fontRenderer.drawString(tile.getCurrentEfficiency()*100+"%", 45, 48, 0xd99747);

		ClientUtils.bindTexture(texture_skycrate_station);
		//TODO: 13.04.2023 progress bar rendering
		if(tile.currentProcess!=null)
		{
			float progress = tile.getProductionProgress(tile.currentProcess, 0);
			this.drawTexturedModalRect(33, 38, 0, 168, Math.round(49f*progress), 12);
		}
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(texture_skycrate_station);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);

		//Thanks Flaxbeard!
		ArrayList<String> tooltip = new ArrayList<>();

		RotaryUtils.renderEnergyTooltip(tooltip, mx, my, guiLeft+148, guiTop+20, tile.rotation);

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}
}
