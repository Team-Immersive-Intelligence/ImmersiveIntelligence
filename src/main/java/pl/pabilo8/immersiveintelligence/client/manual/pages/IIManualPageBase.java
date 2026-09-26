package pl.pabilo8.immersiveintelligence.client.manual.pages;

import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualPages;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 15.05.2020
 */
public abstract class IIManualPageBase extends ManualPages
{
	public static final String texture = ImmersiveIntelligence.MODID+":textures/gui/manual.png";

	public IIManualPageBase(ManualInstance manual, String text)
	{
		super(manual, text);
	}

	@Nullable
	public abstract EasyNBT provideManualData();
}
