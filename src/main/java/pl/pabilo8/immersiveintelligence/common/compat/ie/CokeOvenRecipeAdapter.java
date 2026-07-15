package pl.pabilo8.immersiveintelligence.common.compat.ie;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;

import javax.annotation.Nullable;

/**
 * Read-only manual adapter for IE Coke Oven recipes.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.07.2026
 */
public class CokeOvenRecipeAdapter extends IERecipeAdapterBase<CokeOvenRecipe>
{
	public CokeOvenRecipeAdapter(CokeOvenRecipe recipe)
	{
		super(recipe, recipe.time, recipe.input);
	}

	@Nullable
	private FluidStack getCreosoteOutput()
	{
		return recipe.creosoteOutput > 0?FluidRegistry.getFluidStack("creosote", recipe.creosoteOutput): null;
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		FluidStack creosoteOutput = getCreosoteOutput();
		IIRecipeLayoutBuilder builder = new IIRecipeLayoutBuilder(164, 64, true)
				.withInputSlot(2, 16, ApiUtils.createIngredientStack(recipe.input))
				.withMultiblockModel(38, -8, 68, 68, "")
				.withTimeInfo();
		if(!recipe.output.isEmpty())
			builder.withOutputSlot(116, 16, recipe.output);
		if(creosoteOutput!=null)
			builder.withOutputFluidTank(140, 3, creosoteOutput);
		return builder.build();
	}
}
