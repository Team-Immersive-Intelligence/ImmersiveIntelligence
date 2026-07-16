package pl.pabilo8.immersiveintelligence.common.compat.ie.recipe;

import blusunrize.immersiveengineering.api.crafting.FermenterRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;

import javax.annotation.Nullable;

/**
 * Read-only manual adapter for IE Fermenter recipes.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.07.2026
 */
public class FermenterRecipeAdapter extends IEMultiblockRecipeAdapter<FermenterRecipe>
{
	public FermenterRecipeAdapter(FermenterRecipe recipe)
	{
		super(recipe, recipe.input);
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		IIRecipeLayoutBuilder builder = new IIRecipeLayoutBuilder(158, 64)
				.withInputSlot(2, 16, recipe.input)
				.withMultiblockModel(38, -10, 68, 68, "")
				.withTimeInfo()
				.withPowerInfo();
		if(!recipe.itemOutput.isEmpty())
			builder.withOutputSlot(112, 16, recipe.itemOutput);
		if(recipe.fluidOutput!=null)
			builder.withOutputFluidTank(136, 3, recipe.fluidOutput);
		return builder.build();
	}
}
