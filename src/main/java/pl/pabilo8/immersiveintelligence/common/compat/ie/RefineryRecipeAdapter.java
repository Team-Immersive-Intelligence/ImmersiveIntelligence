package pl.pabilo8.immersiveintelligence.common.compat.ie;

import blusunrize.immersiveengineering.api.crafting.RefineryRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;

import javax.annotation.Nullable;

/**
 * Read-only manual adapter for IE Refinery recipes.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.07.2026
 */
public class RefineryRecipeAdapter extends IEMultiblockRecipeAdapter<RefineryRecipe>
{
	public RefineryRecipeAdapter(RefineryRecipe recipe)
	{
		super(recipe, recipe.input0, recipe.input1);
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		IIRecipeLayoutBuilder builder = new IIRecipeLayoutBuilder(178, 66)
				.withMultiblockModel(48, -8, 76, 68, "")
				.withTimeInfo()
				.withPowerInfo();
		if(recipe.input0!=null)
			builder.withInputFluidTank(2, 3, recipe.input0);
		if(recipe.input1!=null)
			builder.withInputFluidTank(24, 3, recipe.input1);
		if(recipe.output!=null)
			builder.withOutputFluidTank(158, 3, recipe.output);
		return builder.build();
	}
}
