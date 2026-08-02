package pl.pabilo8.immersiveintelligence.common.compat.ie.recipe;

import blusunrize.immersiveengineering.api.crafting.AlloyRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;

import javax.annotation.Nullable;

/**
 * Read-only manual adapter for IE Alloy Kiln recipes.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.07.2026
 */
public class AlloyingFurnaceRecipeAdapter extends IERecipeAdapterBase<AlloyRecipe>
{
	public AlloyingFurnaceRecipeAdapter(AlloyRecipe recipe)
	{
		super(recipe, recipe.time, recipe.input0, recipe.input1);
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		IIRecipeLayoutBuilder builder = new IIRecipeLayoutBuilder(158, 60, true)
				.withInputSlot(2, 12, recipe.input0)
				.withInputSlot(22, 12, recipe.input1)
				.withMultiblockModel(48, -12, 68, 68, "")
				.withTimeInfo();
		if(!recipe.output.isEmpty())
			builder.withOutputSlot(138, 12, recipe.output);
		return builder.build();
	}
}
