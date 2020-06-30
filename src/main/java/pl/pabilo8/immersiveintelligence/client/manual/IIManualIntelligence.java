package pl.pabilo8.immersiveintelligence.client.manual;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_Connector;

/**
 * @author Pabilo8
 * @since 18-01-2020
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

		ManualHelper.addEntry("binoculars", getCategory(),
				new ManualPages.Crafting(ManualHelper.getManual(), "binoculars0", Utils.getItemWithMetaName(CommonProxy.item_binoculars, "binoculars")),
				new ManualPages.Crafting(ManualHelper.getManual(), "binoculars1", Utils.getItemWithMetaName(CommonProxy.item_binoculars, "infrared_binoculars"))
		);

		ManualHelper.addEntry("alarm_siren", getCategory(),
				new ManualPages.Crafting(ManualHelper.getManual(), "alarm_siren0", new ItemStack(CommonProxy.block_data_connector, 1, IIBlockTypes_Connector.ALARM_SIREN.getMeta()))
		);
	}
}
