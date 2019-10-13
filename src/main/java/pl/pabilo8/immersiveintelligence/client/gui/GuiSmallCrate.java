package pl.pabilo8.immersiveintelligence.client.gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntitySmallCrate;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerSmallCrate;

/**
 * Created by Pabilo8 on 2019-05-17.
 */
public class GuiSmallCrate extends GuiIEContainerBase
{
	public String texture = "";

	public GuiSmallCrate(InventoryPlayer inventoryPlayer, TileEntitySmallCrate tile)
	{
		super(new ContainerSmallCrate(inventoryPlayer, tile));
		texture = tile.getBlockMetadata() < 3?"immersiveengineering:textures/gui/crate.png": "immersiveintelligence:textures/gui/metal_crate.png";
		this.ySize = 168;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRenderer.drawString(net.minecraft.client.resources.I18n.format(((ContainerSmallCrate)this.inventorySlots).tile.getDisplayName().getUnformattedText()), 8, 6, 0x0a0a0a);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
}