package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author GabrielV (gabriel@iiteam.net)
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 28.07.2024
 */
public class IIStringUtil
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

	public static Integer[] parseNumberListString(String listString)
	{
		//Comma separated values
		return Arrays.stream(listString.replace(" ", "").split(","))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.flatMap(s -> {
					String[] subSplit = s.split(":");
					if(subSplit.length==2)
					{
						int start = parseInt(subSplit[0]);
						int end = parseInt(subSplit[1]);
						return IntStream.rangeClosed(Math.min(start, end), Math.max(start, end)).boxed();
					}
					else
						return Stream.of(parseInt(subSplit[0]));
				})
				.toArray(Integer[]::new);
	}

	public static TextComponentString getItemStackTextComponent(ItemStack stack)
	{
		TextComponentString stackText = new TextComponentString(stack.getDisplayName());
		stackText.getStyle().setHoverEvent(
				new HoverEvent(
						HoverEvent.Action.SHOW_ITEM,
						new TextComponentString(stack.serializeNBT().toString())
				)
		);
		stackText.getStyle().setColor(TextFormatting.GOLD);
		return stackText;
	}

	public static int parseInt(String string)
	{
		try
		{
			return Integer.parseInt(string);
		} catch(NumberFormatException e)
		{
			return 0;
		}
	}

	public static float parseFloat(String string)
	{
		try
		{
			return Float.parseFloat(string);
		} catch(NumberFormatException e)
		{
			return 0;
		}
	}
}
