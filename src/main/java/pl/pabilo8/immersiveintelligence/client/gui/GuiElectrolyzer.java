package pl.pabilo8.immersiveintelligence.client.gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityElectrolyzer;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerElectrolyzer;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class GuiElectrolyzer extends GuiIEContainerBase
{
	public static final String texture_electrolyzer = ImmersiveIntelligence.MODID+":textures/gui/electrolyzer.png";
	TileEntityElectrolyzer tile;

	public GuiElectrolyzer(InventoryPlayer inventoryPlayer, TileEntityElectrolyzer tile)
	{
		super(new ContainerElectrolyzer(inventoryPlayer, tile));
		this.ySize = 168;
		this.tile = tile;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRenderer.drawString(I18n.format("tile."+ImmersiveIntelligence.MODID+".metal_multiblock.electrolyzer.name"), 8, 6, 0x0a0a0a);
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

		int stored = (int)(47*(tile.getEnergyStored(null)/(float)tile.getMaxEnergyStored(null)));
		ClientUtils.drawGradientRect(guiLeft+161, guiTop+24+(47-stored), guiLeft+168, guiTop+71, 0xffb51500, 0xff600b00);

		if(tile.active&&tile.processTimeMax!=0)
		{
			float progress = Math.min(1f, tile.processTime/(float)tile.processTimeMax);
			this.drawTexturedModalRect(guiLeft+66, guiTop+42, 176, 71, Math.round(54*progress), 10);
		}
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
		this.renderHoveredToolTip(mx, my);

		ArrayList<String> tooltip = new ArrayList();

		ClientUtils.handleGuiTank(tile.tanks[0], guiLeft+32, guiTop+23, 16, 47, 176, 0, 20, 51, mx, my, texture_electrolyzer, tooltip);

		ClientUtils.handleGuiTank(tile.tanks[1], guiLeft+90, guiTop+23, 45, 16, 176, 51, 49, 20, mx, my, texture_electrolyzer, tooltip);
		ClientUtils.handleGuiTank(tile.tanks[2], guiLeft+90, guiTop+55, 45, 16, 176, 51, 49, 20, mx, my, texture_electrolyzer, tooltip);

		if(mx > guiLeft+161&&mx < guiLeft+168&&my > guiTop+24&&my < guiTop+71)
			tooltip.add(tile.getEnergyStored(null)+"/"+tile.getMaxEnergyStored(null)+" IF");

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}
}
