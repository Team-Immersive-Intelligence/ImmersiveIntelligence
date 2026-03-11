package pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util;

import java.util.function.Predicate;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 25.09.2025
 */
public enum TextFilter
{
	NONE((s) -> true),
	ALPHANUMERIC((s) -> s.matches("[a-zA-Z0-9_]*")),
	LOWERCASE((s) -> s.matches("[a-z0-9_]*")),
	UPPERCASE((s) -> s.matches("[A-Z0-9_]*")),
	DECIMAL((s) -> s.matches("[0-9.-]*")),
	HEXADECIMAL((s) -> s.matches("[0-9A-Fa-f]*")),
	BINARY((s) -> s.matches("[01]*")),
	FLOAT((s) -> s.matches("[0-9.-]*")||s.matches("[0-9.-]*[eE][+-]?[0-9]+"));

	private final Predicate<String> filter;

	TextFilter(Predicate<String> filter)
	{
		this.filter = filter;
	}

	public boolean test(String input)
	{
		return filter.test(input);
	}
}
