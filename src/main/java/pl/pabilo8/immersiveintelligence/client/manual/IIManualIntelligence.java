package pl.pabilo8.immersiveintelligence.client.manual;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.MultiblockRadar;
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
				new ManualPages.Crafting(ManualHelper.getManual(), "binoculars0", Utils.getStackWithMetaName(IIContent.itemBinoculars, "binoculars")),
				new ManualPages.Crafting(ManualHelper.getManual(), "binoculars1", Utils.getStackWithMetaName(IIContent.itemBinoculars, "infrared_binoculars"))
		);

		ManualHelper.addEntry("alarm_siren", getCategory(),
				new ManualPages.Crafting(ManualHelper.getManual(), "alarm_siren0", new ItemStack(IIContent.blockDataConnector, 1, IIBlockTypes_Connector.ALARM_SIREN.getMeta()))
		);

		ManualHelper.addEntry("tripod_periscope", getCategory(),
				new ManualPages.Crafting(ManualHelper.getManual(), "tripod_periscope0", new ItemStack(IIContent.itemTripodPeriscope)),
				new ManualPages.Text(ManualHelper.getManual(), "tripod_periscope1")
		);

		ManualHelper.addEntry("radar", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "radar0", MultiblockRadar.instance),
				new ManualPages.Text(ManualHelper.getManual(), "radar1")
		);
	}
}
