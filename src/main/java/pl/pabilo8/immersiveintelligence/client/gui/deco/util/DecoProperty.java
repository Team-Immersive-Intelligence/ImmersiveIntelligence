package pl.pabilo8.immersiveintelligence.client.gui.deco.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark setter fields in Deco GUI components to apply a text CSS-like json style sheet.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.08.2025
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DecoProperty
{
	String name();
}
