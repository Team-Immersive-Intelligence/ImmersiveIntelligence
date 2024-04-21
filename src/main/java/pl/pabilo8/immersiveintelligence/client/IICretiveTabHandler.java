package pl.pabilo8.immersiveintelligence.client;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraftforge.client.event.GuiScreenEvent.BackgroundDrawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pl.pabilo8.immersiveintelligence.client.gui.creative.IIGuiContainerCreative;

/**
 * @author GabrielV (gabriel@iiteam.net)
 * @since 21/04/2024 - 9:04 PM
 */
public class IICretiveTabHandler
{
	@SubscribeEvent
	public void drawScreen(BackgroundDrawnEvent event)
	{
		GuiScreen screen = event.getGui();
		if (screen instanceof GuiContainerCreative)
			IIClientUtils.displayScreen(new IIGuiContainerCreative(IIClientUtils.getPlayer()));
	}
}
