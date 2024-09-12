package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.TileEntitySmallCrate;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerSmallCrate;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class GuiSmallCrate extends GuiIEContainerBase
{
	private final String texture;

	public GuiSmallCrate(EntityPlayer player, TileEntitySmallCrate tile)
	{
		super(new ContainerSmallCrate(player, tile));
		texture = tile.getBlockMetadata() < 3?"immersiveengineering:textures/gui/crate.png": ImmersiveIntelligence.MODID+":textures/gui/metal_crate.png";
		this.ySize = 168;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRenderer.drawString(net.minecraft.client.resources.I18n.format(((ContainerSmallCrate)this.inventorySlots).tile.getDisplayName().getUnformattedText()), 8, 6, IIReference.COLOR_H1.getPackedRGB());
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