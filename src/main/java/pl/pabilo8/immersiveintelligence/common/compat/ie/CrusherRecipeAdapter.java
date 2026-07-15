package pl.pabilo8.immersiveintelligence.common.compat.ie;

import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.IOType;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;

import javax.annotation.Nullable;

/**
 * Read-only manual adapter for IE Crusher recipes.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.07.2026
 */
public class CrusherRecipeAdapter extends IEMultiblockRecipeAdapter<CrusherRecipe>
{
	public CrusherRecipeAdapter(CrusherRecipe recipe)
	{
		super(recipe, recipe.input);
	}

	private ItemStack[] getSecondaryOutputs()
	{
		return recipe.secondaryOutput==null?new ItemStack[0]: recipe.secondaryOutput;
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		ItemStack[] secondaryOutputs = getSecondaryOutputs();
		int rows = Math.max(1, (secondaryOutputs.length+1)/2);
		int height = Math.max(64, 26+rows*20);
		IIRecipeLayoutBuilder builder = new IIRecipeLayoutBuilder(178, height)
				.withInputSlot(2, 16, recipe.input)
				.withMultiblockModel(38, -8, 72, 68, "")
				.withTimeInfo()
				.withPowerInfo();

		if(!recipe.output.isEmpty())
			builder.withOutputSlot(116, 16, recipe.output);
		for(int i = 0; i < secondaryOutputs.length; i++)
			if(!secondaryOutputs[i].isEmpty())
				builder.withSlot(138+(i%2)*20, 6+(i/2)*20, secondaryOutputs[i], IOType.OUTPUT, "frame_red");
		return builder.build();
	}
}
