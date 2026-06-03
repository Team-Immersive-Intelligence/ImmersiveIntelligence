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
	private final PaintStyleConstraint colorCustomization;
	private final Set<String> styles;
	private final Set<String> decorations;

	public StyleConstraints(String defaultStyle, PaintStyleConstraint colorCustomization, Set<String> styles, Set<String> decorations)
	{
		this.defaultStyle = defaultStyle;
		this.colorCustomization = colorCustomization;
		this.styles = styles;
		this.decorations = decorations;
	}

	public String getDefaultStyle()
	{
		return defaultStyle;
	}

	public PaintStyleConstraint getColorCustomization()
	{
		return colorCustomization;
	}

	public Set<String> getStyles()
	{
		return styles;
	}

	public Set<String> getDecorations()
	{
		return decorations;
	}

	public enum PaintStyleConstraint
	{
		NOT_APPLICABLE,
		ANY_COLOR,
		PAINTS_COLOR_ONLY
	}
}
