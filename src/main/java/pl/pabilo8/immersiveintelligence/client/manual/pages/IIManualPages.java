package pl.pabilo8.immersiveintelligence.client.manual.pages;

import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualPages;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

/**
 * @author Pabilo8
 * @since 15-05-2020
 */
public abstract class IIManualPages extends ManualPages
{
	public static final String texture = ImmersiveIntelligence.MODID+":textures/gui/manual.png";

	public IIManualPages(ManualInstance manual, String text)
	{
		super(manual, text);
	}

}
