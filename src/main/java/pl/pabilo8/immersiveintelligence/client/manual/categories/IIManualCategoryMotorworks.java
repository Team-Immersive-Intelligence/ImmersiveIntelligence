package pl.pabilo8.immersiveintelligence.client.manual.categories;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageBlueprint;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualCategory;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockCoagulator;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockFuelStation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockVehicleWorkshop;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockVulcanizer;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIMaterial.Materials;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8
 * @since 18-01-2020
 */
public class IIManualCategoryMotorworks extends IIManualCategory
{
	public static IIManualCategoryMotorworks INSTANCE = new IIManualCategoryMotorworks();

	@Override
	public String getCategory()
	{
		return IIReference.CAT_MOTORWORKS;
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
				new ManualPageMultiblock(ManualHelper.getManual(), "coagulator0", MultiblockCoagulator.INSTANCE),
				new ManualPages.Text(ManualHelper.getManual(), "coagulator1")
		);
		ManualHelper.addEntry("vulcanizer", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "vulcanizer0", MultiblockVulcanizer.INSTANCE),
				new ManualPages.Text(ManualHelper.getManual(), "vulcanizer1"),
				new ManualPages.Text(ManualHelper.getManual(), "vulcanizer2"),
				new ManualPageBlueprint(ManualHelper.getManual(), "vulcanizer3",
						new ItemStack(IIContent.itemVulcanizerMold, 1, 0),
						new ItemStack(IIContent.itemVulcanizerMold, 1, 1)
				),
				new ManualPages.CraftingMulti(ManualHelper.getManual(), "vulcanizer4", IIContent.itemMaterial.getStack(Materials.RUBBER_COMPOUND))
		);
		ManualHelper.addEntry("fuel_station", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "fuel_station0", MultiblockFuelStation.INSTANCE),
				new ManualPages.Text(ManualHelper.getManual(), "fuel_station1")
		);
		ManualHelper.addEntry("vehicle_workshop", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "vehicle_workshop0", MultiblockVehicleWorkshop.INSTANCE),
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
