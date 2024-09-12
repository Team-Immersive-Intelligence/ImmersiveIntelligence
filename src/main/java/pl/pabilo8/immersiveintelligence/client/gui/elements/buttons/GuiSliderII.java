package pl.pabilo8.immersiveintelligence.client.gui.elements.buttons;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.elements.GuiSliderIE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @since 29.07.2021
 */
public class GuiSliderII extends GuiSliderIE
{
	private final int textColor;

	public GuiSliderII(int buttonId, int x, int y, int width, String name, float value, IIColor textColor)
	{
		super(buttonId, x, y, width, name, value);
		this.textColor = textColor.getPackedRGB();
	}

	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if(this.visible)
		{
			ClientUtils.bindTexture("immersiveengineering:textures/gui/hud_elements.png");
			FontRenderer fontrenderer = mc.fontRenderer;
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.x&&mouseY >= this.y&&mouseX < this.x+this.width&&mouseY < this.y+this.height;
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			this.drawTexturedModalRect(x, y, 8, 128, 4, height);
			this.drawTexturedModalRect(x+width-4, y, 16, 128, 4, height);
			for(int i = 0; i < width-8; i += 2)
				this.drawTexturedModalRect(x+4+i, y, 13, 128, 2, height);
			this.mouseDragged(mc, mouseX, mouseY);
			fontrenderer.drawSplitString(displayString, x, y+1-fontrenderer.getWordWrappedHeight(displayString, width+10), width+10, textColor);
		}
	}


	/**
	 * Renders the specified text to the screen, center-aligned.
	 * Without shadow.
	 */
	@Override
	public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color)
	{
		fontRendererIn.drawString(text, x-fontRendererIn.getStringWidth(text)/2, y, color);
	}

	/**
	 * Renders the specified text to the screen.
	 * Without shadow.
	 */
	@Override
	public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color)
	{
		fontRendererIn.drawString(text, x, y, color);
	}
}
