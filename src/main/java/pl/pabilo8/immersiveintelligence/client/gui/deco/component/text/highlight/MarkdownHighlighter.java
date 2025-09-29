package pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.highlight;

import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Very lightweight markdown-like highlighter (headings, bold, italic, code, links).
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
		List<Segment> out = new ArrayList<>();
		if(line.isEmpty()) return out;
		int i = 0;
		int n = line.length();
		// heading
		if(line.charAt(0)=='#')
		{
			int hashes = 0;
			while(i < n&&line.charAt(i)=='#')
			{
				hashes++;
				i++;
			}
			if(i < n&&line.charAt(i)==' ')
			{
				out.add(new Segment(line.substring(0, i+1), heading, true, false));
				String rest = line.substring(i+1);
				if(!rest.isEmpty()) out.add(new Segment(rest, heading, false, false));
				return out;
			}
			else i = 0; // fallback
		}
		StringBuilder buf = new StringBuilder();
		while(i < n)
		{
			char c = line.charAt(i);
			// inline code
			if(c=='`')
			{
				// flush
				if(buf.length() > 0)
				{
					out.add(new Segment(buf.toString(), normal));
					buf.setLength(0);
				}
				i++;
				int start = i;
				while(i < n&&line.charAt(i)!='`') i++;
				String codeText = line.substring(start, Math.min(i, n));
				out.add(new Segment(codeText, code, true, false));
				if(i < n&&line.charAt(i)=='`') i++;
				continue;
			}
			// bold or italic
			if(c=='*')
			{
				int stars = 1;
				if(i+1 < n&&line.charAt(i+1)=='*') stars = 2;
				// flush
				if(buf.length() > 0)
				{
					out.add(new Segment(buf.toString(), normal));
					buf.setLength(0);
				}
				i += stars;
				int start = i;
				while(i < n)
				{
					if(line.charAt(i)=='*')
					{
						int ahead = (i+1 < n&&line.charAt(i+1)=='*')?2: 1;
						if(ahead==stars) {break;}
					}
					i++;
				}
				String content = line.substring(start, Math.min(i, n));
				IIColor col = (stars==2)?strong: italic;
				out.add(new Segment(content, col, stars==2, stars==1));
				if(i < n) {i += stars;}
				continue;
			}
			// link [text](url)
			if(c=='[')
			{
				int start = i+1;
				int close = line.indexOf(']', start);
				int openParen = (close >= 0)?line.indexOf('(', close): -1;
				int closeParen = (openParen >= 0)?line.indexOf(')', openParen): -1;
				if(close > 0&&openParen > 0&&closeParen > 0)
				{
					if(buf.length() > 0)
					{
						out.add(new Segment(buf.toString(), normal));
						buf.setLength(0);
					}
					String text = line.substring(start, close);
					String url = line.substring(openParen+1, closeParen);
					out.add(new Segment(text, linkText, false, false));
					out.add(new Segment("("+url+")", linkUrl, false, false));
					i = closeParen+1;
					continue;
				}
			}
			buf.append(c);
			i++;
		}
		if(buf.length() > 0) out.add(new Segment(buf.toString(), normal));
		return out;
	}
}

