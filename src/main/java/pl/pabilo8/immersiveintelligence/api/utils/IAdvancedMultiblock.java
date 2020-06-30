package pl.pabilo8.immersiveintelligence.api.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Pabilo8 on 23-06-2020.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface IAdvancedMultiblock
{
	/**
	 * Just a marker to check if the multiblock is advanced (requires advanced hammer to build)
	 * For TileEntities use {@link IAdvancedMultiblockTileEntity}
	 */
}
