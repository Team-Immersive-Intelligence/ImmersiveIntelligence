package pl.pabilo8.immersiveintelligence.api.style;


/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 08.09.2025
 */
public interface IStyleCustomizable
{
	IStyleCustomizable master();

	StyleCustomization getStyle();
}
