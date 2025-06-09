package pl.pabilo8.immersiveintelligence.client.manual.categories;

import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualCategory;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 18.01.2020
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
		super.addPages();
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
