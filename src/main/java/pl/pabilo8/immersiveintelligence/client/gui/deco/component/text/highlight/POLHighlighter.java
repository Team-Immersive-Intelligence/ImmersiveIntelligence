package pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.highlight;

import pl.pabilo8.immersiveintelligence.api.data.IIDataOperationUtils;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation.DataOperationMeta;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLKeywords;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import java.util.*;

/**
 * Simple syntax highlighter for POL (.pol) scripts.
 * Dynamically gathers keywords from 	алPOLKeywords enum and registered DataOperations (names & expressions).
 */
public class POLHighlighter extends TextHighlighter
{
	// Dynamic sets built once (lazy) to include enum keywords + operation names/expressions
	private static final Set<String> KEYWORDS = new HashSet<>();
	private static boolean initialized = false;

	// Darcula-esque palette (must match IIManualDataOperation)
	private static final IIColor COLOR_PLAIN = IIColor.fromPackedRGB(0xA9B7C6);
	private static final IIColor COLOR_KEYWORD = IIColor.fromPackedRGB(0xCC7832);
	private static final IIColor COLOR_OPERATION = IIColor.fromPackedRGB(0xe89433);
	private static final IIColor COLOR_VARIABLE = IIColor.fromPackedRGB(0x7e6b80);
	private static final IIColor COLOR_STRING = IIColor.fromPackedRGB(0x6A8759);
	private static final IIColor COLOR_COMMENT = IIColor.fromPackedRGB(0x49633f);
	private static final IIColor COLOR_NUMBER = IIColor.fromPackedRGB(0x6897BB);

	private static void ensureInitialized()
	{
		if(initialized) return;
		// POL enum keywords
		for(POLKeywords kw : POLKeywords.values())
			if(kw.isVisible())
				KEYWORDS.add(kw.getName());
		// Data operation names & expressions
		for(String opName : IIDataOperationUtils.getAllOperationNames())
		{
			KEYWORDS.add(opName.toLowerCase(Locale.ROOT));
			DataOperationMeta meta = IIDataOperationUtils.getOperationMeta(opName);
			if(meta!=null)
			{
				String expr = meta.expression();
				if(!expr.isEmpty())
					KEYWORDS.add(expr);
			}
		}
		initialized = true;
	}

	private static boolean isSymbolic(String s)
	{
		for(char ch : s.toCharArray()) if(Character.isLetterOrDigit(ch)||ch=='_') return false;
		return true;
	}

	@Override
	public List<Segment> highlight(String line)
	{
		ensureInitialized();
		List<Segment> out = new ArrayList<>();
		if(line.isEmpty()) return out;
		// comments start at ';' outside quotes
		int commentIndex = indexOfOutsideQuotes(line, ';');
		String codePart = commentIndex >= 0?line.substring(0, commentIndex): line;
		String commentPart = commentIndex >= 0?line.substring(commentIndex): null;

		int i = 0, n = codePart.length();
		StringBuilder token = new StringBuilder();
		while(i < n)
		{
			char c = codePart.charAt(i);
			if(c=='"')
			{
				if(token.length() > 0) flushToken(token, out);
				int start = ++i;
				boolean esc = false;
				while(i < n)
				{
					char ch = codePart.charAt(i);
					if(ch=='"'&&!esc) break;
					esc = (ch=='\\')&&!esc;
					i++;
				}
				String content = codePart.substring(start, Math.min(i, n));
				out.add(new Segment("\""+content+(i < n&&codePart.charAt(i)=='"'?"\"": ""), COLOR_STRING, false, false));
				if(i < n&&codePart.charAt(i)=='"') i++;
				continue;
			}
			if(Character.isWhitespace(c))
			{
				if(token.length() > 0) flushToken(token, out);
				int ws = i;
				while(i < n&&Character.isWhitespace(codePart.charAt(i))) i++;
				out.add(new Segment(codePart.substring(ws, i), COLOR_PLAIN));
				continue;
			}
			// operator or symbolic token
			if(isOpStart(c))
			{
				if(token.length() > 0) flushToken(token, out);
				String op = Character.toString(c);
				if(!KEYWORDS.contains(op))
				{ // treat lone punctuation as plain
					out.add(new Segment(op, COLOR_PLAIN));
					i += op.length();
					continue;
				}
				out.add(new Segment(op, COLOR_KEYWORD, false, false));
				i += op.length();
				continue;
			}
			token.append(c);
			i++;
		}
		if(token.length() > 0) flushToken(token, out);
		if(commentPart!=null) out.add(new Segment(commentPart, COLOR_COMMENT, false, false));
		return out;
	}

	private void flushToken(StringBuilder token, List<Segment> out)
	{
		String t = token.toString();
		token.setLength(0);
		if(t.isEmpty()) return;
		String lower = t.toLowerCase(Locale.ROOT);
		if(KEYWORDS.contains(lower))
		{
			out.add(new Segment(t, COLOR_KEYWORD, true, false));
			return;
		}
		if(isNumber(t))
		{
			out.add(new Segment(t, COLOR_NUMBER, false, false));
			return;
		}
		if(t.startsWith("@"))
		{
			out.add(new Segment(t, COLOR_VARIABLE, false, false));
			return;
		}
		if(t.endsWith(":"))
		{
			out.add(new Segment(t, COLOR_KEYWORD, false, false));
			return;
		}
		out.add(new Segment(t, COLOR_PLAIN, false, false));
	}

	private boolean isOpStart(char c)
	{
		return "+-*/=!<>|&%^".indexOf(c) >= 0;
	}

	private boolean isNumber(String s)
	{
		if(s.isEmpty()) return false;
		int dots = 0;
		for(char ch : s.toCharArray())
			if(ch=='.') {if(++dots > 1) return false;}
			else if(!Character.isDigit(ch)) return false;
		return true;
	}

	private int indexOfOutsideQuotes(String line, char target)
	{
		boolean inStr = false;
		boolean esc = false;
		for(int i = 0; i < line.length(); i++)
		{
			char c = line.charAt(i);
			if(c=='"'&&!esc) inStr = !inStr;
			if(!inStr&&c==target) return i;
			esc = (c=='\\')&&!esc;
		}
		return -1;
	}
}
