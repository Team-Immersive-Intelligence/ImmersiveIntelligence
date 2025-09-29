package pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util;

import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents a text caret used inside of a {@link DecoTextField}
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 25.09.2025
 */
public class TextCaret
{
	public int line, pos;
	public int anchorLine, anchorPos;

	public TextCaret(int line, int pos)
	{
		this.line = line;
		this.pos = pos;
		this.anchorLine = line;
		this.anchorPos = pos;
	}

	public boolean hasSelection()
	{
		return line!=anchorLine||pos!=anchorPos;
	}

	public int startLine()
	{
		return (anchorLine < line)||(anchorLine==line&&anchorPos <= pos)?anchorLine: line;
	}

	public int endLine()
	{
		return (anchorLine > line)||(anchorLine==line&&anchorPos >= pos)?anchorLine: line;
	}

	public int startPos()
	{
		if(line==anchorLine)
			return Math.min(pos, anchorPos);
		return startLine()==anchorLine?anchorPos: pos;
	}

	public int endPos()
	{
		if(line==anchorLine)
			return Math.max(pos, anchorPos);
		return endLine()!=line?anchorPos: pos;
	}

	/**
	 * Unified horizontal movement.
	 *
	 * @param left            direction (true=left, false=right)
	 * @param unit            movement granularity
	 * @param lines           all text lines
	 * @param allowCrossLines whether movement may wrap to previous/next line
	 * @param isWord          predicate to test word characters
	 */
	public void moveHorizontal(boolean left, MoveUnit unit, List<String> lines,
							   boolean allowCrossLines, Predicate<Character> isWord)
	{
		String current = lines.get(line);
		switch(unit)
		{
			case END:
				pos = left?0: current.length();
				return;
			case WORD:
				if(left)
				{
					if(pos==0)
					{
						if(allowCrossLines&&line > 0)
						{
							line--;
							pos = lines.get(line).length();
						}
						return;
					}
					int i = pos;
					while(i > 0&&Character.isWhitespace(current.charAt(i-1))) i--;
					while(i > 0&&isWord.test(current.charAt(i-1))) i--;
					pos = i;
				}
				else
				{
					if(pos >= current.length())
					{
						if(allowCrossLines&&line < lines.size()-1)
						{
							line++;
							pos = 0;
						}
						return;
					}
					int i = pos;
					while(i < current.length()&&isWord.test(current.charAt(i))) i++;
					while(i < current.length()&&Character.isWhitespace(current.charAt(i))) i++;
					pos = i;
				}
				return;
			case CHAR:
				if(left)
				{
					if(pos > 0) pos--;
					else if(allowCrossLines&&line > 0)
					{
						line--;
						pos = lines.get(line).length();
					}
				}
				else
				{
					if(pos < current.length()) pos++;
					else if(allowCrossLines&&line < lines.size()-1)
					{
						line++;
						pos = 0;
					}
				}
		}
	}
}
