package pl.pabilo8.immersiveintelligence.api.style;

import java.util.Set;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 07.09.2025
 */
public class StyleConstraints
{
	private final String defaultStyle;
	private final boolean allowColorCustomization;
	private final Set<String> styles;
	private final Set<String> decorations;

	public StyleConstraints(String defaultStyle, boolean allowColorCustomization, Set<String> styles, Set<String> decorations)
	{
		this.defaultStyle = defaultStyle;
		this.allowColorCustomization = allowColorCustomization;
		this.styles = styles;
		this.decorations = decorations;
	}

	public String getDefaultStyle()
	{
		return defaultStyle;
	}

	public boolean allowsColorCustomization()
	{
		return allowColorCustomization;
	}

	public Set<String> getStyles()
	{
		return styles;
	}

	public Set<String> getDecorations()
	{
		return decorations;
	}
}
