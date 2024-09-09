package pl.pabilo8.immersiveintelligence.client.manual.categories;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualCategory;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8
 * @since 18-01-2020
 */
public class IIManualCategoryIntelligence extends IIManualCategory
{
	public static IIManualCategoryIntelligence INSTANCE = new IIManualCategoryIntelligence();

	@Override
	public String getCategory()
	{
		return IIReference.CAT_INTELLIGENCE;
	}

	@Override
	public void addPages()
	{
		super.addPages();
		addEntry("intel_main");
		addEntry("alarm_siren")
				.addSource("alarm_siren", getSourceForItem(IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.ALARM_SIREN)));

		addEntry("radar");
		addEntry("tripod_periscope")
				.addSource("tripod_periscope", getSourceForItem(
						new ItemStack(IIContent.itemTripodPeriscope)));

		addEntry("binoculars")
				.addSource("binoculars", getSourceForItem(
					new ItemStack(IIContent.itemBinoculars)));

	}
}
