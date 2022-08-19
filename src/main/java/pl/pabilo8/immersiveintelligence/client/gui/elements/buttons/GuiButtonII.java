package pl.pabilo8.immersiveintelligence.client.gui.elements.buttons;

import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

/**
 * @author Pabilo8
 * @since 07-07-2019
 */
public class GuiButtonII extends GuiButtonIE
{
	private final float u, v, uu, vv;

	public GuiButtonII(int buttonId, int x, int y, int w, int h, String texture, float u, float v, float uu, float vv)
	{
		super(buttonId, x, y, w, h, "", texture, 0, 0);
		this.u = u;
		this.v = v;
		this.uu = uu;
		this.vv = vv;
	}

	/**
	 * Not Supported
	 */
	@Override
	public GuiButtonIE setHoverOffset(int x, int y)
	{
		return this;
	}

	@Override
	public void drawTexturedModalRect(int xCoord, int yCoord, int textureX, int textureY, int width, int height)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(xCoord+0.0F, yCoord+(float)height, this.zLevel)
				.tex(u, vv).endVertex();
		bufferbuilder.pos(xCoord+(float)width, yCoord+(float)height, this.zLevel)
				.tex(uu, vv).endVertex();
		bufferbuilder.pos(xCoord+(float)width, yCoord+0.0F, this.zLevel)
				.tex(uu, v).endVertex();
		bufferbuilder.pos(xCoord+0.0F, yCoord+0.0F, this.zLevel)
				.tex(u, v).endVertex();
		tessellator.draw();
	}
}
