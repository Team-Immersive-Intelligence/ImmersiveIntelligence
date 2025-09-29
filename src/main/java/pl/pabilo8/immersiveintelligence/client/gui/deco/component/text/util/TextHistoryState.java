package pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 26.09.2025
 */
public class TextHistoryState
{
	public final List<String> lines;
	public final List<TextCaret> carets;

	public TextHistoryState(List<String> l, List<TextCaret> c)
	{
		this.lines = new ArrayList<>(l);
		this.carets = new ArrayList<>();
		for(TextCaret tc : c)
		{
			TextCaret copy = new TextCaret(tc.line, tc.pos);
			copy.anchorLine = tc.anchorLine;
			copy.anchorPos = tc.anchorPos;
			this.carets.add(copy);
		}
	}
}
