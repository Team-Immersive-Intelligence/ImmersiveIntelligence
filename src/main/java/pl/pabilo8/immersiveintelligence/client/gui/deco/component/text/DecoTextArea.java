package pl.pabilo8.immersiveintelligence.client.gui.deco.component.text;

import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.highlight.TextHighlighter;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRenderer;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A multi-line Deco text box supporting vertical navigation, multiple carets and syntax highlighting.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 29.07.2026
 */
public class DecoTextArea extends DecoTextInputBase<DecoTextArea>
{
	@Nullable
	private TextHighlighter highlighter;

	public DecoTextArea(int x, int y)
	{
		super(x, y);
	}

	public DecoTextArea withHighlighter(@Nullable TextHighlighter highlighter)
	{
		this.highlighter = highlighter;
		return this;
	}

	@Nullable
	public TextHighlighter getHighlighter()
	{
		return highlighter;
	}

	@Override
	protected boolean isMultiLineInput()
	{
		return true;
	}

	@Override
	protected void drawTextLine(String fullLine, int sliceStart, int drawX, int lineY, int maxWidth, IIColor fallbackColor)
	{
		if(highlighter==null)
		{
			super.drawTextLine(fullLine, sliceStart, drawX, lineY, maxWidth, fallbackColor);
			return;
		}

		List<TextHighlighter.Segment> segments = highlighter.highlightVisible(fullLine, sliceStart);
		if(segments.isEmpty())
		{
			super.drawTextLine(fullLine, sliceStart, drawX, lineY, maxWidth, fallbackColor);
			return;
		}

		IIFontRenderer font = getTextFontRenderer();
		int used = 0;
		for(TextHighlighter.Segment segment : segments)
		{
			if(segment==null||segment.text==null||segment.text.isEmpty()||used >= maxWidth)
				continue;
			String visible = font.trimStringToWidth(segment.text, maxWidth-used);
			if(!visible.isEmpty())
			{
				IIColor color = segment.color==null?fallbackColor: segment.color;
				font.drawString(visible, drawX+used, lineY, color.getPackedARGB());
				used += font.getStringWidth(visible);
			}
			if(visible.length() < segment.text.length())
				break;
		}
	}
}
