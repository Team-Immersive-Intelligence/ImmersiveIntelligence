package pl.pabilo8.immersiveintelligence.client.manual.categories;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageBlueprint;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualCategory;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockCoagulator;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockFuelStation;
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
		super .addPages();
		addEntry("coagulator");
		addEntry("fuel_station");
		addEntry("motorworks");
		addEntry("rubber_production");
		addEntry("vehicle_workshop");
		addEntry("vulcanizer")

				//.addSource("compound_silicon", getSourceForItem(new ItemStack(IIitemMaterial)))
				.addSource("vulcanizer_blueprint", getSourceForItem(
						BlueprintCraftingRecipe.getTypedBlueprint("vulcanizer_molds")
				));
	}
}
