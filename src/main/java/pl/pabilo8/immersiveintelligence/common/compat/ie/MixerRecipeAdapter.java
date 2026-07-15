package pl.pabilo8.immersiveintelligence.common.compat.ie;

import blusunrize.immersiveengineering.api.crafting.MixerRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;

import javax.annotation.Nullable;

/**
 * Read-only manual adapter for IE Mixer recipes.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.07.2026
 */
public class MixerRecipeAdapter extends IEMultiblockRecipeAdapter<MixerRecipe>
{
	public MixerRecipeAdapter(MixerRecipe recipe)
	{
		super(recipe, recipe.fluidInput, recipe.itemInputs);
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		int columns = 4;
		int rows = Math.max(1, (recipe.itemInputs.length+columns-1)/columns);
		int height = Math.max(66, 20+rows*20);
		IIRecipeLayoutBuilder builder = new IIRecipeLayoutBuilder(178, height)
				.withProgressArrow(116, Math.max(14, (height-29)/2))
				.withTimeInfo()
				.withPowerInfo();

		if(recipe.fluidInput!=null)
			builder.withInputFluidTank(2, 3, recipe.fluidInput);
		for(int i = 0; i < recipe.itemInputs.length; i++)
			builder.withInputSlot(28+(i%columns)*20, 3+(i/columns)*20, recipe.itemInputs[i]);
		if(recipe.fluidOutput!=null)
			builder.withOutputFluidTank(158, 3, recipe.fluidOutput);
		return builder.build();
	}
}
