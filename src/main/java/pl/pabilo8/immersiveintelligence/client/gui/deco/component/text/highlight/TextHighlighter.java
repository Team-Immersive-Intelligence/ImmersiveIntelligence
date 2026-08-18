package pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.highlight;

import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base syntax highlighter.
 *
 * <p>Implementations must return ordered segments whose concatenated text is exactly equal to
 * the supplied line. Preserving the source text allows the renderer to clip a horizontally
 * scrolled line without reparsing a partial token and losing its original colour.</p>
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

		private Segment slice(int from)
		{
			return new Segment(text.substring(from), color, bold, italic);
		}
	}

	/**
	 * Highlights a complete source line.
	 */
	public abstract List<Segment> highlight(String line);

	/**
	 * Highlights a complete line and then removes the source characters before {@code start}.
	 * This deliberately does not call {@link #highlight(String)} with a substring: a visible
	 * suffix may start halfway through a keyword, string or comment and still needs the style
	 * assigned to the complete token.
	 */
	public final List<Segment> highlightVisible(String line, int start)
	{
		if(line==null||line.isEmpty())
			return Collections.emptyList();

		int clampedStart = Math.max(0, Math.min(start, line.length()));
		List<Segment> highlighted = highlight(line);
		if(highlighted==null||highlighted.isEmpty())
			return Collections.emptyList();

		List<Segment> result = new ArrayList<>();
		int consumed = 0;
		for(Segment segment : highlighted)
		{
			if(segment==null||segment.text==null||segment.text.isEmpty())
				continue;

			int segmentEnd = consumed+segment.text.length();
			if(segmentEnd <= clampedStart)
			{
				consumed = segmentEnd;
				continue;
			}

			int localStart = Math.max(0, clampedStart-consumed);
			result.add(localStart==0?segment: segment.slice(localStart));
			consumed = segmentEnd;
		}
		return result;
	}

	protected static List<Segment> single(String line, IIColor color)
	{
		return Collections.singletonList(new Segment(line, color));
	}

	protected static boolean isWord(char c)
	{
		return Character.isLetterOrDigit(c)||c=='_'||c=='#';
	}
}
