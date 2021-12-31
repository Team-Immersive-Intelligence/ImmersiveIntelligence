package pl.pabilo8.immersiveintelligence.client.manual;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageBlueprint;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.MultiblockCoagulator;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.MultiblockFuelStation;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.MultiblockVehicleWorkshop;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.MultiblockVulcanizer;

/**
 * @author Pabilo8
 * @since 18-01-2020
 */
public class IIManualMotorworks extends IIManual
{
	public static IIManualMotorworks INSTANCE = new IIManualMotorworks();

	@Override
	public String getCategory()
	{
		return ClientProxy.CAT_MOTORWORKS;
	}

	@Override
	public void addPages()
	{
		ManualHelper.addEntry("motorworks", getCategory(),
				new ManualPages.Text(ManualHelper.getManual(), "motorworks")
		);

		ManualHelper.addEntry("rubber_production", getCategory(),
				new ManualPages.Image(ManualHelper.getManual(), "rubber_production0", "immersiveintelligence:textures/misc/rotary.png;110;0;64;64"),
				new ManualPages.Image(ManualHelper.getManual(), "rubber_production1", "immersiveintelligence:textures/misc/rotary.png;174;0;64;64"),
				new ManualPages.Text(ManualHelper.getManual(), "rubber_production2"),
				new ManualPages.Text(ManualHelper.getManual(), "rubber_production3"),
				new ManualPages.Text(ManualHelper.getManual(), "rubber_production4"),
				new ManualPages.Text(ManualHelper.getManual(), "rubber_production5"),
				new ManualPages.Text(ManualHelper.getManual(), "rubber_production6")
		);
		ManualHelper.addEntry("coagulator", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "coagulator0", MultiblockCoagulator.instance),
				new ManualPages.Text(ManualHelper.getManual(), "coagulator1")
		);
		ManualHelper.addEntry("vulcanizer", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "vulcanizer0", MultiblockVulcanizer.instance),
				new ManualPages.Text(ManualHelper.getManual(), "vulcanizer1"),
				new ManualPages.Text(ManualHelper.getManual(), "vulcanizer2"),
				new ManualPageBlueprint(ManualHelper.getManual(), "vulcanizer3",
						new ItemStack(IIContent.itemVulcanizerMold, 1, 0),
						new ItemStack(IIContent.itemVulcanizerMold, 1, 1)
				),
				new ManualPages.CraftingMulti(ManualHelper.getManual(), "vulcanizer4", Utils.getStackWithMetaName(IIContent.itemMaterial, "rubber_compound"))
		);
		ManualHelper.addEntry("fuel_station", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "fuel_station0", MultiblockFuelStation.instance),
				new ManualPages.Text(ManualHelper.getManual(), "fuel_station1")
		);
		ManualHelper.addEntry("vehicle_workshop", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "vehicle_workshop0", MultiblockVehicleWorkshop.instance),
				new ManualPages.Text(ManualHelper.getManual(), "vehicle_workshop1"),
				new ManualPages.Text(ManualHelper.getManual(), "vehicle_workshop2"),
				new ManualPages.Text(ManualHelper.getManual(), "vehicle_workshop3")
		);

		ManualHelper.addEntry("motorbike", getCategory(),
				new ManualPages.Image(ManualHelper.getManual(), "motorbike0", "immersiveintelligence:textures/misc/rotary.png;0;192;64;64")
		);
		ManualHelper.addEntry("field_howitzer", getCategory(),
				new ManualPages.Image(ManualHelper.getManual(), "field_howitzer0", "immersiveintelligence:textures/misc/rotary.png;64;192;64;64")
		);
		/*ManualHelper.addEntry("generator_trailer", getCategory(),
				new ManualPages.Text(ManualHelper.getManual(), "generator_trailer0")
		);*/
	}
}
