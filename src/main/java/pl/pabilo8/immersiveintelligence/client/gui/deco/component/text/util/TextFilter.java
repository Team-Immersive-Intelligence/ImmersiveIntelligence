package pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util;

import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ILocalizedEnum;

/**
 * Standard validation modes for Deco text inputs.
 *
 * <p>{@link #accepts(String)} accepts both complete values and useful intermediate editing states,
 * such as an empty numeric field, {@code -}, or {@code 1.}. {@link #isValid(String)} is stricter
 * and reports whether the current text can safely be consumed as a finished value.</p>
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 25.09.2025
 */
public enum TextFilter implements ILocalizedEnum
{
	NONE,
	ALPHANUMERIC,
	LOWERCASE,
	UPPERCASE,
	DECIMAL,
	HEXADECIMAL,
	BINARY,
	FLOAT;

	/**
	 * Checks whether text may exist in the input while it is being edited.
	 */
	public boolean accepts(String input)
	{
		if(input==null)
			return false;
		switch(this)
		{
			case NONE:
				return true;
			case ALPHANUMERIC:
				return allCharactersMatch(input, CharacterMode.ALPHANUMERIC);
			case LOWERCASE:
				return allCharactersMatch(input, CharacterMode.LOWERCASE);
			case UPPERCASE:
				return allCharactersMatch(input, CharacterMode.UPPERCASE);
			case DECIMAL:
				return acceptsInteger(input, 10);
			case HEXADECIMAL:
				return acceptsInteger(input, 16);
			case BINARY:
				return acceptsInteger(input, 2);
			case FLOAT:
				return acceptsFloat(input);
			default:
				return false;
		}
	}

	/**
	 * Checks whether text is a complete value safe for consumption.
	 */
	public boolean isValid(String input)
	{
		if(!accepts(input))
			return false;
		switch(this)
		{
			case DECIMAL:
			case HEXADECIMAL:
			case BINARY:
				return input.length() > 0&&!"-".equals(input)&&!"+".equals(input);
			case FLOAT:
				if(input.isEmpty()||"-".equals(input)||"+".equals(input)||".".equals(input)||"-.".equals(input)||"+.".equals(input))
					return false;
				try
				{
					float value = Float.parseFloat(input);
					return !Float.isNaN(value)&&!Float.isInfinite(value);
				} catch(NumberFormatException ignored)
				{
					return false;
				}
			default:
				return true;
		}
	}

	/**
	 * @return whether this filter represents a numeric value and should use numeric field conventions
	 */
	public boolean isNumeric()
	{
		switch(this)
		{
			case DECIMAL:
			case HEXADECIMAL:
			case BINARY:
			case FLOAT:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Backwards-compatible alias. New code should choose {@link #accepts(String)} or
	 * {@link #isValid(String)} explicitly.
	 */
	@Deprecated
	public boolean test(String input)
	{
		return accepts(input);
	}

	public int parseInt(String input, int fallback)
	{
		if(!isValid(input))
			return fallback;
		try
		{
			return Integer.parseInt(input, getRadix());
		} catch(NumberFormatException ignored)
		{
			return fallback;
		}
	}

	public float parseFloat(String input, float fallback)
	{
		if(this!=FLOAT||!isValid(input))
			return fallback;
		try
		{
			return Float.parseFloat(input);
		} catch(NumberFormatException ignored)
		{
			return fallback;
		}
	}

	public String formatInt(int value)
	{
		switch(this)
		{
			case BINARY:
				return formatSigned(value, 2);
			case HEXADECIMAL:
				return formatSigned(value, 16).toUpperCase();
			case DECIMAL:
			default:
				return Integer.toString(value);
		}
	}

	private int getRadix()
	{
		switch(this)
		{
			case BINARY:
				return 2;
			case HEXADECIMAL:
				return 16;
			default:
				return 10;
		}
	}

	private static String formatSigned(int value, int radix)
	{
		if(value >= 0)
			return Integer.toString(value, radix);
		//Avoid overflowing when negating Integer.MIN_VALUE.
		long magnitude = -(long)value;
		return "-"+Long.toString(magnitude, radix);
	}

	private static boolean acceptsInteger(String input, int radix)
	{
		for(int i = 0; i < input.length(); i++)
		{
			char c = input.charAt(i);
			if(i==0&&(c=='-'||c=='+'))
				continue;
			if(Character.digit(c, radix) < 0)
				return false;
		}
		return true;
	}

	private static boolean acceptsFloat(String input)
	{
		boolean decimalPoint = false;
		boolean exponent = false;
		boolean exponentDigit = false;
		for(int i = 0; i < input.length(); i++)
		{
			char c = input.charAt(i);
			if(Character.isDigit(c))
			{
				if(exponent)
					exponentDigit = true;
				continue;
			}
			if((c=='-'||c=='+')&&(i==0||(i > 0&&(input.charAt(i-1)=='e'||input.charAt(i-1)=='E'))))
				continue;
			if(c=='.'&&!decimalPoint&&!exponent)
			{
				decimalPoint = true;
				continue;
			}
			if((c=='e'||c=='E')&&!exponent&&i > 0&&hasMantissaDigit(input, i))
			{
				exponent = true;
				continue;
			}
			return false;
		}
		//An unfinished exponent is a valid intermediate state.
		return !exponent||exponentDigit||input.endsWith("e")||input.endsWith("E")||input.endsWith("e-")||input.endsWith("E-")||input.endsWith("e+")||input.endsWith("E+");
	}

	private static boolean hasMantissaDigit(String input, int end)
	{
		for(int i = 0; i < end; i++)
			if(Character.isDigit(input.charAt(i)))
				return true;
		return false;
	}

	private static boolean allCharactersMatch(String input, CharacterMode mode)
	{
		for(int i = 0; i < input.length(); i++)
		{
			char c = input.charAt(i);
			if(c=='_'||Character.isDigit(c))
				continue;
			if(mode==CharacterMode.ALPHANUMERIC&&((c >= 'a'&&c <= 'z')||(c >= 'A'&&c <= 'Z')))
				continue;
			if(mode==CharacterMode.LOWERCASE&&c >= 'a'&&c <= 'z')
				continue;
			if(mode==CharacterMode.UPPERCASE&&c >= 'A'&&c <= 'Z')
				continue;
			return false;
		}
		return true;
	}

	@Override
	public String geLocaleKey()
	{
		return IIReference.DESCRIPTION_KEY+"text_filter.";
	}

	private enum CharacterMode implements ILocalizedEnum
	{
		ALPHANUMERIC,
		LOWERCASE,
		UPPERCASE;

		@Override
		public String geLocaleKey()
		{
			return IIReference.DESCRIPTION_KEY+"text_filter.";
		}
	}
}
