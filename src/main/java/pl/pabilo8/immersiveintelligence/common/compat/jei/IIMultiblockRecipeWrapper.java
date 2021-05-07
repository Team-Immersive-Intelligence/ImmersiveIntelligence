package pl.pabilo8.immersiveintelligence.common.compat.jei;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.util.compat.jei.MultiblockRecipeWrapper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;

import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 07.05.2021
 */
public class IIMultiblockRecipeWrapper extends MultiblockRecipeWrapper
{
	public IIMultiblockRecipeWrapper(MultiblockRecipe recipe)
	{
		super(recipe);
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		if(!inputs.isEmpty())
			ingredients.setInputLists(VanillaTypes.ITEM, Arrays.asList(recipeInputs.clone()));
		if(!outputs.isEmpty())
			ingredients.setOutputLists(VanillaTypes.ITEM, Arrays.asList(recipeOutputs.clone()));
		if(!fluidInputs.isEmpty())
			ingredients.setInputs(VanillaTypes.FLUID, fluidInputs);
		if(!fluidOutputs.isEmpty())
			ingredients.setOutputs(VanillaTypes.FLUID, fluidOutputs);
	}
}
