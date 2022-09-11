package pl.pabilo8.immersiveintelligence.api.utils;

import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Pabilo8
 * @since 23-06-2020
 * @implNote Just a marker to check if the multiblock is advanced (requires advanced hammer to build)
 * For TileEntities use {@link IIMultiblockInterfaces.IAdvancedMultiblockTileEntity}
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface IAdvancedMultiblock
{

}
