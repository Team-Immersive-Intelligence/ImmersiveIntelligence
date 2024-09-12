package pl.pabilo8.immersiveintelligence.client.gui.elements.buttons;

import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @since 16.07.2021
 */
public class GuiButtonSwitch extends GuiButtonState
{
	private final ResourceLocation TEXTURE;
	private final int sliderWidth, backWidth, texSliderU;

	final IIColor textColor, color1, color2;
	int timer;
	final int MAX_SWITCH_TICKS = 20;

	public GuiButtonSwitch(int buttonId, int x, int y, int textWidth, int sliderWidth, int backWidth, int h, int u, int v, boolean state, ResourceLocation texture, IIColor textColor, IIColor color1, IIColor color2, String name, boolean firstTime)
	{
		super(buttonId, x, y, textWidth, h, name, state, "", u, v, 0);
		this.TEXTURE = texture;
		this.sliderWidth = sliderWidth;
		this.backWidth = backWidth;
		this.textColor = textColor;
		this.texSliderU = u+backWidth;

		this.color1 = color1;
		this.color2 = color2;

		//Should animate when false
		timer = firstTime^state?MAX_SWITCH_TICKS: 0;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if(this.visible)
		{
			mc.getTextureManager().bindTexture(TEXTURE);
			this.hovered = canClick(mc, mouseX, mouseY);
			GlStateManager.color(1f, 1f, 1f, 1f);
			this.drawTexturedModalRect(x, y, texU, texV, backWidth, height);

			timer = MathHelper.clamp(timer+(state?-1: 1), 0, MAX_SWITCH_TICKS);

			float progress = 1f-(MathHelper.clamp((timer+(this.state?partialTicks: -partialTicks)), 0, MAX_SWITCH_TICKS)/MAX_SWITCH_TICKS);

			int offset = (int)(progress*(backWidth-sliderWidth+1));
			color1.mixedWith(color2, progress).glColor();
			this.drawTexturedModalRect(x+offset, y, texSliderU, texV, sliderWidth, height);//176, 98, 8, 9
			GlStateManager.color(1f, 1f, 1f, 1f);

			this.mouseDragged(mc, mouseX, mouseY);
			if(displayString!=null&&!displayString.isEmpty())
			{
				//textColor
				mc.fontRenderer.drawSplitString(displayString, x+backWidth+2, y+1, width, textColor.getPackedRGB());
			}
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

	public int getTextHeight(FontRenderer fontRendererIn)
	{
		return fontRendererIn.getWordWrappedHeight(displayString, width)+2;
	}
}
