package pl.pabilo8.immersiveintelligence.common.compat;

import net.minecraftforge.fml.common.Loader;
import pl.pabilo8.immersiveintelligence.common.IILogger;

/**
 * @author Avalon (avalon@iiteam.net)
 * @since 03.03.2026
 */
public class CratesFeltBlueHelper
{
	/**
	 * checks for cfb.
	 */
	public static boolean loaded = false;

	public static void init()
	{
		loaded = Loader.isModLoaded("cfb");
		if(loaded)
			IILogger.info("Crates Felt Blue detected, crates will drop their items individually when broken");
	}
}

