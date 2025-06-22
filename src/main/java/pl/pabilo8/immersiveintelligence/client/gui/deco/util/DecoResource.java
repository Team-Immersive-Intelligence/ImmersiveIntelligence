package pl.pabilo8.immersiveintelligence.client.gui.deco.util;

import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark {@link pl.pabilo8.immersiveintelligence.common.util.ResLoc} or {@link net.minecraft.util.ResourceLocation}
 * fields in GUIs extending {@link DecoGui} to be preloaded to the texture map.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 4.01.2025
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DecoResource
{

}
