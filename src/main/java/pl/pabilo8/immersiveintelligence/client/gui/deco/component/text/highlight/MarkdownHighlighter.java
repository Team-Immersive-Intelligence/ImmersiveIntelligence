package pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.highlight;

import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Lightweight markdown-like highlighter for headings, emphasis, inline code and links.
 */
public class MarkdownHighlighter extends TextHighlighter
{
	private final IIColor heading = IIReference.COLOR_IMMERSIVE_ORANGE.withBrightness(1.1f);
	private final IIColor strong = IIReference.COLOR_IMMERSIVE_ORANGE;
	private final IIColor italic = IIReference.COLOR_IMMERSIVE_ORANGE.withBrightness(0.85f);
	private final IIColor code = IIReference.COLOR_IMMERSIVE_ORANGE.withBrightness(0.7f);
	private final IIColor linkText = IIReference.COLOR_IMMERSIVE_ORANGE.withBrightness(0.95f);
	private final IIColor linkUrl = IIReference.COLOR_IMMERSIVE_ORANGE.withBrightness(0.6f);
	private final IIColor normal = IIColor.WHITE;

	@Override
	public List<Segment> highlight(String line)
	{
		List<Segment> result = new ArrayList<>();
		if(line.isEmpty())
			return result;

		int headingEnd = headingPrefixEnd(line);
		if(headingEnd > 0)
		{
			result.add(new Segment(line.substring(0, headingEnd), heading, true, false));
			if(headingEnd < line.length())
				result.add(new Segment(line.substring(headingEnd), heading));
			return result;
		}

		int plainStart = 0;
		int index = 0;
		while(index < line.length())
		{
			int end;
			if(line.charAt(index)=='`'&&(end = findClosing(line, index+1, "`")) >= 0)
			{
				flushPlain(line, plainStart, index, result);
				result.add(new Segment(line.substring(index, end+1), code, true, false));
				index = end+1;
				plainStart = index;
				continue;
			}

			if(line.startsWith("**", index)&&(end = findClosing(line, index+2, "**")) >= 0)
			{
				flushPlain(line, plainStart, index, result);
				result.add(new Segment(line.substring(index, end+2), strong, true, false));
				index = end+2;
				plainStart = index;
				continue;
			}

			if(line.charAt(index)=='*'&&(end = findClosing(line, index+1, "*")) >= 0)
			{
				flushPlain(line, plainStart, index, result);
				result.add(new Segment(line.substring(index, end+1), italic, false, true));
				index = end+1;
				plainStart = index;
				continue;
			}

			if(line.charAt(index)=='[')
			{
				int closeBracket = line.indexOf(']', index+1);
				int openParen = closeBracket >= 0&&closeBracket+1 < line.length()&&line.charAt(closeBracket+1)=='('?closeBracket+1: -1;
				int closeParen = openParen >= 0?line.indexOf(')', openParen+1): -1;
				if(closeParen >= 0)
				{
					flushPlain(line, plainStart, index, result);
					result.add(new Segment(line.substring(index, closeBracket+1), linkText));
					result.add(new Segment(line.substring(openParen, closeParen+1), linkUrl));
					index = closeParen+1;
					plainStart = index;
					continue;
				}
			}
			index++;
		}
		flushPlain(line, plainStart, line.length(), result);
		return result;
	}

	private int headingPrefixEnd(String line)
	{
		int index = 0;
		while(index < line.length()&&line.charAt(index)=='#')
			index++;
		return index > 0&&index < line.length()&&line.charAt(index)==' '?index+1: -1;
	}

	private int findClosing(String line, int from, String marker)
	{
		return line.indexOf(marker, from);
	}

	private void flushPlain(String line, int from, int to, List<Segment> result)
	{
		if(to > from)
			result.add(new Segment(line.substring(from, to), normal));
	}
}
