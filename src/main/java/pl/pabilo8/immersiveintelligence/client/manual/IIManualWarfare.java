package pl.pabilo8.immersiveintelligence.client.manual;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.ManualPages;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;

/**
 * Created by Pabilo8 on 18-01-2020.
 */
public class IIManualWarfare extends IIManual
{
	public static IIManualWarfare INSTANCE = new IIManualWarfare();

	@Override
	public String getCategory()
	{
		return ClientProxy.CAT_WARFARE;
	}

	@Override
	public void addPages()
	{
		ManualHelper.addEntry("warfare_main", getCategory(),
				new ManualPages.Text(ManualHelper.getManual(), "warfare_main0")
		);
	}
}
