package pl.pabilo8.immersiveintelligence.common.compat.ie;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;

import javax.annotation.Nullable;

/**
 * Read-only manual adapter for IE Blast Furnace recipes.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.07.2026
 */
public class BlastFurnaceRecipeAdapter extends IERecipeAdapterBase<BlastFurnaceRecipe>
{
	public BlastFurnaceRecipeAdapter(BlastFurnaceRecipe recipe)
	{
		super(recipe, recipe.time, recipe.input);
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		IIRecipeLayoutBuilder builder = new IIRecipeLayoutBuilder(158, 60, true)
				.withInputSlot(2, 12, ApiUtils.createIngredientStack(recipe.input))
				.withMultiblockModel(38, -12, 70, 68, "")
				.withTimeInfo();
		if(!recipe.output.isEmpty())
			builder.withOutputSlot(116, 12, recipe.output);
		if(!recipe.slag.isEmpty())
			builder.withSlot(138, 12, recipe.slag, IIRecipeLayout.IOType.OUTPUT, "frame_red");
		return builder.build();
	}
}
