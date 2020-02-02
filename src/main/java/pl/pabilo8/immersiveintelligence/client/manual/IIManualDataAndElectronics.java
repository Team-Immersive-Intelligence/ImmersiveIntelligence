package pl.pabilo8.immersiveintelligence.client.manual;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.lib.manual.ManualPages;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.MultiblockRadioStation;

/**
 * Created by Pabilo8 on 18-01-2020.
 */
public class IIManualDataAndElectronics extends IIManual
{
	public static IIManualDataAndElectronics INSTANCE = new IIManualDataAndElectronics();

	@Override
	public String getCategory()
	{
		return ClientProxy.CAT_DATA;
	}

	@Override
	public void addPages()
	{
		ManualHelper.addEntry("data_main", getCategory(),
				new ManualPages.Text(ManualHelper.getManual(), "data_main0"),
				new ManualPages.Text(ManualHelper.getManual(), "data_main1")
		);

		String[][] intInfoTable = {{"def_value", "0"}, {"min_value", "-8192"}, {"max_value", "8192"}};
		String[][] stringInfoTable = {{"def_value", "\'\'"}, {"max_length", "512"}};
		String[][] boolInfoTable = {{"def_value", "false"}, {"accepted_values", "tf"}};
		String[][] itemstackInfoTable = {{"def_value", "empty"}};
		String[][] arrayInfoTable = {{"def_value", "empty"}, {"min_index", "0"}, {"max_index", "255"}};
		String[][] pairInfoTable = {{"def_value", "empty"}};
		String[][] rangeInfoTable = {{"def_value", "0-0"}};

		ManualHelper.addEntry("data_variable_types", getCategory(),
				new ManualPages.Text(ManualHelper.getManual(), "data_variable_types"),
				new ManualPages.Text(ManualHelper.getManual(), "data_variable_types_null"),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_int", intInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_string", stringInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_bool", boolInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_itemstack", itemstackInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_array", arrayInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_pair", pairInfoTable, true),
				new ManualPages.Table(ManualHelper.getManual(), "data_variable_types_range", rangeInfoTable, true)
		);

		ManualHelper.addEntry("radio_station", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "radio_station0", MultiblockRadioStation.instance),
				new ManualPages.Text(ManualHelper.getManual(), "radio_station1")
		);
	}
}
