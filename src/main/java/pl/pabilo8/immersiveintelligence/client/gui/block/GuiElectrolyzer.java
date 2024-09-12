package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityElectrolyzer;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerElectrolyzer;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class GuiElectrolyzer extends GuiIEContainerBase
{
	//REFACTOR: 22.10.2023 use drawutils 
	public static final String texture_electrolyzer = ImmersiveIntelligence.MODID+":textures/gui/electrolyzer.png";
	TileEntityElectrolyzer tile;

	public GuiElectrolyzer(EntityPlayer player, TileEntityElectrolyzer tile)
	{
		super(new ContainerElectrolyzer(player, tile));
		this.ySize = 168;
		this.tile = tile;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRenderer.drawString(I18n.format("tile."+ImmersiveIntelligence.MODID+".metal_multiblock.electrolyzer.name"), 8, 6, IIReference.COLOR_H1.getPackedRGB());
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(texture_electrolyzer);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		ClientUtils.handleGuiTank(tile.tanks[0], guiLeft+32, guiTop+23, 16, 47, 176, 0, 20, 51, mx, my, texture_electrolyzer, null);
		ClientUtils.handleGuiTank(tile.tanks[1], guiLeft+90, guiTop+23, 45, 16, 176, 51, 49, 20, mx, my, texture_electrolyzer, null);
		ClientUtils.handleGuiTank(tile.tanks[2], guiLeft+90, guiTop+55, 45, 16, 176, 51, 49, 20, mx, my, texture_electrolyzer, null);

		ClientUtils.bindTexture(texture_electrolyzer);

		this.drawTexturedModalRect(guiLeft+50, guiTop+21, 196, 0, 15, 51);

		IIClientUtils.drawPowerBar(guiLeft+161, guiTop+24, 7, 47, tile.getEnergyStored(null)/(float)tile.getMaxEnergyStored(null));

		if(tile.currentProcess!=null)
		{
			float progress = Math.min(1f, tile.currentProcess.ticks/(float)tile.currentProcess.maxTicks);
			this.drawTexturedModalRect(guiLeft+66, guiTop+42, 176, 71, Math.round(54*progress), 10);
		}
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
		this.renderHoveredToolTip(mx, my);

		ArrayList<String> tooltip = new ArrayList<>();

		ClientUtils.handleGuiTank(tile.tanks[0], guiLeft+32, guiTop+23, 16, 47, 176, 0, 20, 51, mx, my, texture_electrolyzer, tooltip);

		ClientUtils.handleGuiTank(tile.tanks[1], guiLeft+90, guiTop+23, 45, 16, 176, 51, 49, 20, mx, my, texture_electrolyzer, tooltip);
		ClientUtils.handleGuiTank(tile.tanks[2], guiLeft+90, guiTop+55, 45, 16, 176, 51, 49, 20, mx, my, texture_electrolyzer, tooltip);

		if(mx > guiLeft+161&&mx < guiLeft+168&&my > guiTop+24&&my < guiTop+71)
			tooltip.add(IIUtils.getPowerLevelString(tile.energyStorage.getEnergyStored(), tile.energyStorage.getMaxEnergyStored()));

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}
}
