package pl.pabilo8.immersiveintelligence.client.gui.elements.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * @author Pabilo8
 * @since 16.07.2021
 */
public class GuiButtonTab extends GuiButton
{
	protected final ResourceLocation texture;
	protected final int texU;
	protected final int texV;

	public GuiButtonTab(int buttonId, int x, int y, int w, int h, int u, int v, ResourceLocation texture, String hoverText)
	{
		super(buttonId, x, y, w, h, hoverText);
		this.texture = texture;
		this.texU = u;
		this.texV = v;
	}

	public boolean canClick(Minecraft mc, int mouseX, int mouseY)
	{
		return this.enabled&&this.visible&&mouseX >= this.x&&mouseY >= this.y&&mouseX < this.x+this.width&&mouseY < this.y+this.height;
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent e).
	 */
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		return canClick(mc, mouseX, mouseY);
	}

	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if(this.visible)
		{
			mc.getTextureManager().bindTexture(texture);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			this.hovered = canClick(mc, mouseX, mouseY);

			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			this.drawTexturedModalRect(x, y, texU, texV, width, height);
			this.mouseDragged(mc, mouseX, mouseY);
		}
	}
}
