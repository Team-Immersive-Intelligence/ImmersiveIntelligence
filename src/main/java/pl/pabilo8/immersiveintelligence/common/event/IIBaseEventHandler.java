package pl.pabilo8.immersiveintelligence.common.event;

import net.minecraftforge.common.MinecraftForge;
import pl.pabilo8.immersiveintelligence.common.IILogger;

/**
 * @author GabrielV (gabriel@iiteam.net)
 * @since 21.04.2024
 */
public class IIBaseEventHandler
{
	public void registerEventHandler()
	{
		IILogger.info("Registering event handler: "+this);
		MinecraftForge.EVENT_BUS.register(this);
	}
}
