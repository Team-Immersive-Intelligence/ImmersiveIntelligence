package pl.pabilo8.immersiveintelligence.common.compat.ie;

import blusunrize.immersiveengineering.api.ComparableItemStack;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MetalPressRecipe;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.IOType;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;

import javax.annotation.Nullable;

/**
 * Read-only manual adapter for IE Metal Press recipes.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.07.2026
 */
public class MetalPressRecipeAdapter extends IEMultiblockRecipeAdapter<MetalPressRecipe>
{
	public MetalPressRecipeAdapter(MetalPressRecipe recipe)
	{
		super(recipe, recipe.input, getMold(recipe.mold));
	}

	private static IngredientStack getMold(ComparableItemStack mold)
	{
		if(mold.oreID >= 0)
			return new IngredientStack(OreDictionary.getOreName(mold.oreID));
		return new IngredientStack(mold.stack);
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		IIRecipeLayoutBuilder builder = new IIRecipeLayoutBuilder(156, 60)
				.withInputSlot(2, 12, recipe.input)
				.withSlot(28, 12, getMold(recipe.mold), IOType.NEUTRAL, "frame")
				.withMultiblockModel(50, -14, 64, 64, "")
				.withTimeInfo()
				.withPowerInfo();
		if(!recipe.output.isEmpty())
			builder.withOutputSlot(136, 12, recipe.output);
		return builder.build();
	}
}
