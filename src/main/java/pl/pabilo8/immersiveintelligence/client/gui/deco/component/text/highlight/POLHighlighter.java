package pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.highlight;

import pl.pabilo8.immersiveintelligence.api.data.IIDataOperationUtils;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation.DataOperationMeta;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLKeywords;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import java.util.*;

/**
 * Syntax highlighter for POL scripts.
 * Dynamically gathers language keywords and registered data-operation names.
 */
public class POLHighlighter extends TextHighlighter
{
	private static final Set<String> KEYWORDS = new HashSet<>();
	private static final Set<String> OPERATIONS = new HashSet<>();
	private static final List<String> SYMBOLIC_TOKENS = new ArrayList<>();
	private static volatile boolean initialized = false;

	//Darcula-esque palette (must match IIManualDataOperation)
	private static final IIColor COLOR_PLAIN = IIColor.fromPackedRGB(0xA9B7C6);
	private static final IIColor COLOR_KEYWORD = IIColor.fromPackedRGB(0xCC7832);
	private static final IIColor COLOR_OPERATION = IIColor.fromPackedRGB(0xE89433);
	private static final IIColor COLOR_VARIABLE = IIColor.fromPackedRGB(0x7E6B80);
	private static final IIColor COLOR_STRING = IIColor.fromPackedRGB(0x6A8759);
	private static final IIColor COLOR_COMMENT = IIColor.fromPackedRGB(0x49633F);
	private static final IIColor COLOR_NUMBER = IIColor.fromPackedRGB(0x6897BB);

	private static void ensureInitialized()
	{
		if(initialized)
			return;
		synchronized(POLHighlighter.class)
		{
			if(initialized)
				return;

			for(POLKeywords keyword : POLKeywords.values())
				if(keyword.isVisible())
					registerToken(KEYWORDS, keyword.getName());

			for(String operationName : IIDataOperationUtils.getAllOperationNames())
			{
				registerToken(OPERATIONS, operationName);
				DataOperationMeta meta = IIDataOperationUtils.getOperationMeta(operationName);
				if(meta!=null)
					registerExpressionSymbols(meta.expression());
			}

			SYMBOLIC_TOKENS.sort(Comparator.comparingInt(String::length).reversed());
			initialized = true;
		}
	}

	private static void registerToken(Set<String> target, String token)
	{
		if(token==null||token.isEmpty())
			return;
		String normalized = token.toLowerCase(Locale.ROOT);
		target.add(normalized);
		if(isSymbolic(normalized)&&!SYMBOLIC_TOKENS.contains(token))
			SYMBOLIC_TOKENS.add(token);
	}

	private static void registerExpressionSymbols(String expression)
	{
		if(expression==null||expression.isEmpty())
			return;
		int start = -1;
		for(int i = 0; i <= expression.length(); i++)
		{
			boolean symbol = i < expression.length()&&isOperatorCharacter(expression.charAt(i));
			if(symbol&&start < 0)
				start = i;
			else if(!symbol&&start >= 0)
			{
				String token = expression.substring(start, i);
				OPERATIONS.add(token.toLowerCase(Locale.ROOT));
				if(!SYMBOLIC_TOKENS.contains(token))
					SYMBOLIC_TOKENS.add(token);
				start = -1;
			}
		}
	}

	private static boolean isSymbolic(String token)
	{
		for(int i = 0; i < token.length(); i++)
			if(Character.isLetterOrDigit(token.charAt(i))||token.charAt(i)=='_')
				return false;
		return true;
	}

	@Override
	public List<Segment> highlight(String line)
	{
		ensureInitialized();
		if(line.isEmpty())
			return Collections.emptyList();

		List<Segment> result = new ArrayList<>();
		int index = 0;
		while(index < line.length())
		{
			char current = line.charAt(index);

			if(current==';')
			{
				result.add(new Segment(line.substring(index), COLOR_COMMENT));
				break;
			}

			if(current=='"')
			{
				int end = stringEnd(line, index+1);
				result.add(new Segment(line.substring(index, end), COLOR_STRING));
				index = end;
				continue;
			}

			if(Character.isWhitespace(current))
			{
				int end = index+1;
				while(end < line.length()&&Character.isWhitespace(line.charAt(end)))
					end++;
				result.add(new Segment(line.substring(index, end), COLOR_PLAIN));
				index = end;
				continue;
			}

			if(current=='@')
			{
				int end = index+1;
				while(end < line.length()&&isIdentifierPart(line.charAt(end)))
					end++;
				result.add(new Segment(line.substring(index, end), COLOR_VARIABLE));
				index = end;
				continue;
			}

			if(Character.isDigit(current)||(current=='.'&&index+1 < line.length()&&Character.isDigit(line.charAt(index+1))))
			{
				int end = numberEnd(line, index);
				result.add(new Segment(line.substring(index, end), COLOR_NUMBER));
				index = end;
				continue;
			}

			if(Character.isLetter(current)||current=='_')
			{
				int end = index+1;
				while(end < line.length()&&isIdentifierPart(line.charAt(end)))
					end++;
				String token = line.substring(index, end);
				String normalized = token.toLowerCase(Locale.ROOT);
				IIColor color = KEYWORDS.contains(normalized)?COLOR_KEYWORD:
						(OPERATIONS.contains(normalized)?COLOR_OPERATION: COLOR_PLAIN);
				result.add(new Segment(token, color, KEYWORDS.contains(normalized), false));
				index = end;
				continue;
			}

			String symbolic = matchSymbol(line, index);
			if(symbolic!=null)
			{
				String normalized = symbolic.toLowerCase(Locale.ROOT);
				IIColor color = OPERATIONS.contains(normalized)?COLOR_OPERATION:
						(KEYWORDS.contains(normalized)?COLOR_KEYWORD: COLOR_PLAIN);
				result.add(new Segment(symbolic, color));
				index += symbolic.length();
				continue;
			}

			result.add(new Segment(String.valueOf(current), COLOR_PLAIN));
			index++;
		}
		return result;
	}

	private int stringEnd(String line, int index)
	{
		boolean escaped = false;
		while(index < line.length())
		{
			char current = line.charAt(index++);
			if(current=='"'&&!escaped)
				break;
			if(current=='\\')
				escaped = !escaped;
			else
				escaped = false;
		}
		return index;
	}

	private int numberEnd(String line, int index)
	{
		boolean decimal = false;
		boolean exponent = false;
		while(index < line.length())
		{
			char current = line.charAt(index);
			if(Character.isDigit(current))
			{
				index++;
				continue;
			}
			if(current=='.'&&!decimal&&!exponent)
			{
				decimal = true;
				index++;
				continue;
			}
			if((current=='e'||current=='E')&&!exponent)
			{
				exponent = true;
				index++;
				if(index < line.length()&&(line.charAt(index)=='+'||line.charAt(index)=='-'))
					index++;
				continue;
			}
			break;
		}
		return index;
	}

	private String matchSymbol(String line, int index)
	{
		for(String token : SYMBOLIC_TOKENS)
			if(index+token.length() <= line.length()&&line.regionMatches(index, token, 0, token.length()))
				return token;
		return null;
	}

	private static boolean isIdentifierPart(char character)
	{
		return Character.isLetterOrDigit(character)||character=='_';
	}

	private static boolean isOperatorCharacter(char character)
	{
		return "+-*/=!<>|&%^".indexOf(character) >= 0;
	}
}
