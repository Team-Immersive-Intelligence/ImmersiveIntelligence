package pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.highlight;

import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base syntax highlighter: implement highlight(line) to return ordered colored segments.
 */
public abstract class TextHighlighter
{
	public static class Segment
	{
		public final String text;
		public final IIColor color;
		public final boolean bold;
		public final boolean italic;

		public Segment(String text, IIColor color)
		{
			this(text, color, false, false);
		}

		public Segment(String text, IIColor color, boolean bold, boolean italic)
		{
			this.text = text;
			this.color = color;
			this.bold = bold;
			this.italic = italic;
		}
	}

	public abstract List<Segment> highlight(String line);

	protected static List<Segment> single(String line, IIColor color)
	{
		return Collections.singletonList(new Segment(line, color));
	}

	protected static boolean isWord(char c)
	{
		return Character.isLetterOrDigit(c)||c=='_'||c=='#';
	}

	// Utility: simple token split preserving delimiters
	protected static List<String> splitPreserve(String line)
	{
		List<String> out = new ArrayList<>();
		StringBuilder cur = new StringBuilder();
		for(char ch : line.toCharArray())
		{
			if(Character.isWhitespace(ch))
			{
				cur.append(ch);
			}
			else
			{
				if(cur.length() > 0)
				{
					out.add(cur.toString());
					cur.setLength(0);
				} // flush whitespace
				out.add(Character.toString(ch));
			}
		}
		if(cur.length() > 0) out.add(cur.toString());
		return out;
	}
}

