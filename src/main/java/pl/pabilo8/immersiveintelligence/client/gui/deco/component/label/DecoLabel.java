package pl.pabilo8.immersiveintelligence.client.gui.deco.component.label;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.resources.I18n;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import java.util.ArrayList;
import java.util.List;

/**
 * A Deco component used to display one or more lines of text.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 8.01.2025
 */
public class DecoLabel extends GuiLabel
{
	private FontRenderer fontRenderer;
	private DecoAlignment textAlignment = DecoAlignment.LEFT;
	private final List<String> labels = new ArrayList<>();
	IIColor textColor = IIColor.BLACK, bgColor = IIColor.ALPHA;
	private boolean textShadow = false;
	private int totalHeight = 0;

	/**
	 * @param fontRenderer The font renderer to use
	 * @param x            The x position of the label
	 * @param y            The y position of the label
	 */
	public DecoLabel(FontRenderer fontRenderer, int x, int y)
	{
		super(fontRenderer, -1, x, y, 8, 8, 0);
		this.fontRenderer = fontRenderer;
		this.width = 200;
		this.height = fontRenderer.FONT_HEIGHT;
	}

	//--- Property Setting ---//

	public DecoLabel withSize(int w, int h)
	{
		this.width = w;
		this.height = h;
		return this;
	}

	public DecoLabel withAlign(DecoAlignment alignment)
	{
		this.textAlignment = alignment;
		return this;
	}

	public DecoLabel withFontRenderer(FontRenderer fontRenderer)
	{
		this.fontRenderer = fontRenderer;
		this.totalHeight = fontRenderer.FONT_HEIGHT*labels.size();
		return this;
	}

	public DecoLabel withTextColor(IIColor textColor)
	{
		this.textColor = textColor;
		return this;
	}

	public DecoLabel withBackgroundColor(IIColor bgColor)
	{
		this.bgColor = bgColor;
		return this;
	}

	public DecoLabel withTextShadow(boolean textShadow)
	{
		this.textShadow = textShadow;
		return this;
	}

	public DecoLabel withText(String... text)
	{
		this.labels.clear();
		for(String label : text)
			this.labels.add(I18n.format(label));
		this.totalHeight = fontRenderer.FONT_HEIGHT*labels.size();
		return this;
	}

	public DecoLabel withRawText(String text)
	{
		this.labels.clear();
		this.labels.add(text);
		this.totalHeight = fontRenderer.FONT_HEIGHT*labels.size();
		return this;
	}

	public DecoLabel withAddedText(String text)
	{
		this.labels.add(I18n.format(text));
		this.totalHeight = fontRenderer.FONT_HEIGHT*labels.size();
		return this;
	}

	//--- Drawing ---//

	@Override
	public void drawLabel(Minecraft mc, int mouseX, int mouseY)
	{
		if(bgColor.alpha > 0)
			IIDrawUtils.startColored().drawColorRect(x, y, x+width, y+height, bgColor).finish();

		int lineOffset = y;
		for(String label : this.labels)
		{
			int xx = textAlignment.getAlignX(x, fontRenderer.getStringWidth(label), width);
			int yy = textAlignment.getAlignY(lineOffset, totalHeight, height);
			fontRenderer.drawString(label, xx, yy, textColor.getPackedARGB(), textShadow);
			lineOffset += fontRenderer.FONT_HEIGHT;
		}
	}


}
