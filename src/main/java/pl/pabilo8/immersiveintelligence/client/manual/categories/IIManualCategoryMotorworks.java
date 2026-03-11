package pl.pabilo8.immersiveintelligence.client.manual.categories;

import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.crafting.VulcanizerRecipe;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualCategory;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author fastdelaspeed
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
		addEntry("motorworks");
		addEntry("fuel_station");
		addEntry("vehicle_workshop");
		addEntry("rubber_production");
		addEntry("coagulator")
				.addSource("recipe", getSourceForRecipe(VulcanizerRecipe.class, "rubber"));
		addEntry("vulcanizer")
				.addSource("compound", getSourceForItem(new ItemStack(IIContent.itemMaterial, 1, 31)))
				.addSource("vulcanizer_blueprint", getSourceForItem(
						BlueprintCraftingRecipe.getTypedBlueprint("vulcanizer_molds")
				))
				.addSource("recipe_tires", getSourceForRecipe(VulcanizerRecipe.class, "rubber_tires"))
				.addSource("recipe_belts", getSourceForRecipe(VulcanizerRecipe.class, "rubber_belts"));
	}
}
