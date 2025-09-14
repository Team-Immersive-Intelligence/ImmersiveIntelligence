package pl.pabilo8.immersiveintelligence.client.gui.deco.component.label;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.resources.I18n;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * A Deco component used to display one or more lines of text.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 8.01.2025
 */
public class DecoLabel extends GuiLabel
{
	private final List<Object> labels = new ArrayList<>();
	IIColor textColor = IIColor.BLACK, bgColor = IIColor.ALPHA;
	private Supplier<Collection<String>> onTooltip = null;
	private FontRenderer fontRenderer;
	private DecoAlignment textAlignment = DecoAlignment.LEFT;
	private boolean forcedUnicode = false;
	private boolean textShadow = false;
	private int totalHeight = 0;
	private boolean hovered;
	private boolean wrap = false;

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
		recalculateHeight();
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

	/**
	 * Sets whether the label should be forced to use unicode characters.
	 *
	 * @param forcedUnicode If true, the label will use unicode characters regardless of the system's locale settings.
	 * @return this
	 */
	public DecoLabel withForcedUnicode(boolean forcedUnicode)
	{
		this.forcedUnicode = forcedUnicode;
		return this;
	}

	/**
	 * Adds an onTooltip event handler to the component, triggered when the mouse is hovered over the component
	 *
	 * @param onTooltip The tooltip
	 * @return this
	 */
	public final DecoLabel withOnTooltip(Supplier<Collection<String>> onTooltip)
	{
		this.onTooltip = onTooltip;
		return this;
	}

	/**
	 * Adds a tooltip to the component to be displayed when hovered
	 *
	 * @param tooltip The tooltip
	 * @return this
	 */
	public final DecoLabel withTranslatedTooltip(String... tooltip)
	{
		final List<String> collect = Arrays.stream(tooltip)
				.map(I18n::format)
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toList());

		return withOnTooltip(() -> collect);
	}

	public DecoLabel withTranslatedTooltipListener(String textFormat, Supplier<String[]> listener)
	{
		return withOnTooltip(() -> Collections.singletonList(I18n.format(textFormat, (Object[])listener.get())));
	}

	public DecoLabel withWrapping(boolean wrap)
	{
		this.wrap = wrap;
		return this;
	}

	//--- Text Setting ---//

	public DecoLabel withText(String... text)
	{
		this.labels.clear();
		return this.addText(text);
	}

	public DecoLabel withRawText(String... text)
	{
		this.labels.clear();
		return addRawText(text);
	}

	public DecoLabel withTextListener(Supplier<String> listener)
	{
		this.labels.clear();
		return this.addTextListener(listener);
	}

	public DecoLabel withFormattedTextListener(String textFormat, Supplier<String[]> listener)
	{
		this.labels.clear();
		return this.addFormattedTextListener(textFormat, listener);
	}

	//--- Text Concatenation ---//

	public DecoLabel addRawText(String... text)
	{
		Collections.addAll(this.labels, text);
		recalculateHeight();
		return this;
	}

	public DecoLabel addText(String... text)
	{
		for(String label : text)
			this.labels.add(I18n.format(label));
		recalculateHeight();
		return this;
	}

	public DecoLabel addTextListener(Supplier<String> listener)
	{
		this.labels.add(listener);
		recalculateHeight();
		return this;
	}

	public DecoLabel addFormattedTextListener(String textFormat, Supplier<String[]> listener)
	{
		Supplier<String> supplier = () -> I18n.format(textFormat, (Object[])listener.get());
		this.labels.add(supplier);
		return this;
	}

	private void recalculateHeight()
	{
		if(wrap)
		{
			this.totalHeight = 0;
			for(Object line : this.labels)
			{
				String label;
				if(line instanceof String)
					label = ((String)line);
				else
					//noinspection unchecked
					label = ((Supplier<String>)line).get();
				this.totalHeight += fontRenderer.getWordWrappedHeight(label, width);
			}
		}
		else
			this.totalHeight = fontRenderer.FONT_HEIGHT*labels.size();
	}

	//--- Getters ---//

	public int getTotalHeight()
	{
		return totalHeight;
	}

	//--- Drawing ---//

	@Override
	public void drawLabel(@Nonnull Minecraft mc, int mouseX, int mouseY)
	{
		this.hovered = false;
		//Draw a highlight background for the text
		if(bgColor.alpha > 0)
			IIDrawUtils.startColored().drawColorRect(x, y, x+width, y+height, bgColor).finish();

		int lineOffset = y;
		boolean unicode = fontRenderer.getUnicodeFlag();
		//Force unicode if flag is set
		if(forcedUnicode)
			fontRenderer.setUnicodeFlag(true);
		for(Object line : this.labels)
		{
			//Determine the displayed text
			String label;
			if(line instanceof String)
				label = ((String)line);
			else
				//noinspection unchecked
				label = ((Supplier<String>)line).get();

			int xx, yy, stringWidth, stringHeight;
			//Wrap into multiple lines
			if(wrap)
			{
				yy = -1;
				stringHeight = 0;
				stringWidth = 0;
				for(String subLine : fontRenderer.listFormattedStringToWidth(label, width))
				{
					//Calculate the sub-line offset
					int subLineWidth = fontRenderer.getStringWidth(subLine);
					int currentX = textAlignment.getAlignX(x, subLineWidth, width);
					int currentY = textAlignment.getAlignY(lineOffset+stringHeight, totalHeight, height);

					if(yy==-1)
						yy = currentY;

					//Draw the sub-line
					fontRenderer.drawString(subLine, currentX, currentY, textColor.getPackedARGB(), textShadow);
					stringHeight += fontRenderer.FONT_HEIGHT;

					// Track the widest sub-line for hover detection
					if(subLineWidth > stringWidth)
						stringWidth = subLineWidth;
				}
				xx = textAlignment.getAlignX(x, stringWidth, width);
			}
			//Single line
			else
			{
				//Calculate the position of the text
				stringWidth = fontRenderer.getStringWidth(label);
				xx = textAlignment.getAlignX(x, stringWidth, width);
				yy = textAlignment.getAlignY(lineOffset, totalHeight, height);

				//Draw the string
				fontRenderer.drawString(label, xx, yy, textColor.getPackedARGB(), textShadow);
				stringHeight = fontRenderer.FONT_HEIGHT;
			}

			//Check for hover if tooltip is set
			this.hovered = this.hovered||IIMath.isPointInRectangle(xx, yy, xx+stringWidth, yy+stringHeight, mouseX, mouseY);
			lineOffset += stringHeight;
		}
		fontRenderer.setUnicodeFlag(unicode);

	}

	public boolean shouldDisplayTooltip()
	{
		return this.hovered&&onTooltip!=null;
	}

	public List<String> getTooltip()
	{
		if(onTooltip!=null)
			return new ArrayList<>(onTooltip.get());
		return Collections.emptyList();
	}
}
