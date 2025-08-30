package pl.pabilo8.immersiveintelligence.api.utils;

import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IConstructionRequiringDevice;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @implNote Just a marker to check if the multiblock is advanced (requires advanced hammer to build)
 * For TileEntities use {@link IConstructionRequiringDevice}
 * @since 23.06.2020
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface IAdvancedMultiblock
{

}
