package pl.pabilo8.immersiveintelligence.client.manual;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.ManualPages;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;

/**
 * Created by Pabilo8 on 18-01-2020.
 */
public class IIManualIntelligence extends IIManual
{
	public static IIManualIntelligence INSTANCE = new IIManualIntelligence();

	@Override
	public String getCategory()
	{
		return ClientProxy.CAT_INTELLIGENCE;
	}

	@Override
	public void addPages()
	{
		ManualHelper.addEntry("intel_main", getCategory(),
				new ManualPages.Text(ManualHelper.getManual(), "intel_main0")
		);
	}
}
