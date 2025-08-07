package pl.pabilo8.immersiveintelligence.client.gui.deco.util;

import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A metadata interface for {@link DecoGui}
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 4.01.2025
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DecoTemplate
{
	/**
	 * The name of the gui, used for the translation key
	 */
	String name();

	DecoGuiCategory category();

	/**
	 * The style of the gui, used for picking the default background and component textures
	 */
	String style() default "steel";
}
