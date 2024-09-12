package pl.pabilo8.immersiveintelligence.client.gui.elements.label;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiLabel;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @since 16.07.2021
 */
public class GuiLabelNoShadow extends GuiLabel
{
	public GuiLabelNoShadow(FontRenderer fontRendererObj, int id, int x, int y, int w, int h, IIColor textColor)
	{
		super(fontRendererObj, id, x, y, w, h, textColor.getPackedRGB());
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
