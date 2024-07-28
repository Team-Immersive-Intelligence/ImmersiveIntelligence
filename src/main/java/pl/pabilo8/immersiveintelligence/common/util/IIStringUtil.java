package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.util.text.TextFormatting;

/**
 * @author gabrielv
 * @since 28-07-2024
 */
public final class IIStringUtil
{
	public static String getItalicString(String string)
	{
		return TextFormatting.ITALIC+string+TextFormatting.RESET;
	}

	public static String toSnakeCase(String value)
	{
		return value.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
	}

	//Converts snake_case to camelCase or CamelCase
	//Copy as you wish
	public static String toCamelCase(String string, boolean startSmall)
	{
		StringBuilder result = new StringBuilder();
		String[] all = string.split("_");
		for(String s : all)
		{
			result.append(Character.toUpperCase(s.charAt(0)));
			result.append(s.substring(1));
		}
		if(startSmall)
			result.setCharAt(0, Character.toLowerCase(result.charAt(0)));
		return result.toString();
	}

}
