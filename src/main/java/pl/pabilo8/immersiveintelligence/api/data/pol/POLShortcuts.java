package pl.pabilo8.immersiveintelligence.api.data.pol;

/**
 * These keywords are used by functions for accessing default values in context
 *
 * @author Pabilo8
 * @since 12.05.2022
 */
public enum POLShortcuts
{
	SUP("@+"),
	SUB("@-"),
	SWP("@="),
	VAL("@@");

	final String pattern;

	POLShortcuts(String pattern)
	{
		this.pattern = pattern;
	}
}
