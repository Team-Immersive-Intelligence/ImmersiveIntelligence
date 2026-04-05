package pl.pabilo8.immersiveintelligence.common.compat;

import net.minecraftforge.fml.common.Loader;
import pl.pabilo8.immersiveintelligence.common.IILogger;

/**
 * @author Carver (carver@iiteam.net)
 * @since 05.04.2026
 */
public class DeeperDepthsHelper
{
	/**
	 * checks for Deeper Depths
	 */

	//05.04.26 TODO: Figure compat for certain copper items to be melted down in the arc furnace. Handle via craft recipes.

	public static boolean loaded = false;

	public static void init()
	{
		loaded = Loader.isModLoaded("deeperdepths");
		if(loaded)
			IILogger.info("Deeper Depths loaded, enabling related excavator veins");
	}
}