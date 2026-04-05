package pl.pabilo8.immersiveintelligence.common.compat;

import net.minecraftforge.fml.common.Loader;
import pl.pabilo8.immersiveintelligence.common.IILogger;

/**
 * @author Carver (carver@iiteam.net)
 * @since 05.04.2026
 */
public class NetherBackportHelper
{
    /**
     * checks for unseen's NB
     */
    public static boolean loaded = false;

    public static void init()
    {
        loaded = Loader.isModLoaded("nb");
        if(loaded)
            IILogger.info("Unseen's nether backport loaded, enabling related excavator veins");
    }
}