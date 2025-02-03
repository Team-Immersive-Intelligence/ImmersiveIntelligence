package pl.pabilo8.immersiveintelligence.client.gui.deco;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used to mark {@link pl.pabilo8.immersiveintelligence.common.util.ResLoc} or {@link net.minecraft.util.ResourceLocation}
 * fields in GUIs extending {@link DecoGui} to be preloaded to the texture map.
 *
 * @author Pabilo8
 * @since 4.01.2025
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DecoResource
{

}
