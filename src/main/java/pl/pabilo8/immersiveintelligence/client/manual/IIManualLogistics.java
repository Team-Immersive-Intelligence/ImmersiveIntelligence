package pl.pabilo8.immersiveintelligence.client.manual;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.lib.manual.ManualPages;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.MultiblockSkyCrateStation;

/**
 * Created by Pabilo8 on 18-01-2020.
 */
public class IIManualLogistics extends IIManual
{
	public static IIManualLogistics INSTANCE = new IIManualLogistics();

	@Override
	public String getCategory()
	{
		return ClientProxy.CAT_LOGISTICS;
	}

	@Override
	public void addPages()
	{
		ManualHelper.addEntry("skycrate_station", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "logi_skycrate0", MultiblockSkyCrateStation.instance),
				new ManualPages.Text(ManualHelper.getManual(), "logi_skycrate1")
		);
	}
}
