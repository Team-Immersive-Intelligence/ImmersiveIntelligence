package pl.pabilo8.immersiveintelligence.common.event;

import net.minecraftforge.common.MinecraftForge;

/**
 * @author GabrielV (gabriel@iiteam.net)
 * @since 21/04/2024 - 7:11 PM
 */
public class IIBaseEventHandler
{
	public void registerEventHandler()
	{
		System.out.println("[ImmersiveIntelligence] Registering event handler: "+this.toString());
		MinecraftForge.EVENT_BUS.register(this);
	}
}
