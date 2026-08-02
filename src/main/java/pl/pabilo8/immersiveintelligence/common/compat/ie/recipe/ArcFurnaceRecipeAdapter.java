package pl.pabilo8.immersiveintelligence.common.compat.ie.recipe;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.IOType;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Read-only manual adapter for IE Arc Furnace recipes.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.07.2026
 */
public class ArcFurnaceRecipeAdapter extends IEMultiblockRecipeAdapter<ArcFurnaceRecipe>
{
	public ArcFurnaceRecipeAdapter(ArcFurnaceRecipe recipe)
	{
		super(recipe, recipe.input, recipe.additives);
	}

	private List<ItemStack> getOutputs()
	{
		List<ItemStack> outputs = recipe.getItemOutputs();
		if(outputs==null||outputs.isEmpty())
			return recipe.output==null||recipe.output.isEmpty()?Collections.emptyList(): Collections.singletonList(recipe.output);
		return outputs;
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		List<ItemStack> outputs = getOutputs();
		int additiveRows = Math.max(1, (recipe.additives.length+1)/2);
		int outputRows = Math.max(1, (outputs.size()+1)/2);
		int height = Math.max(72, 28+Math.max(additiveRows, outputRows)*20);
		IIRecipeLayoutBuilder builder = new IIRecipeLayoutBuilder(188, height)
				.withSlot(2, 22, recipe.input, IOType.INPUT, "frame")
				.withMultiblockModel(58, -4, 64, 64, "")
				.withTimeInfo()
				.withPowerInfo();

		for(int i = 0; i < recipe.additives.length; i++)
			builder.withSlot(26+(i%2)*20, 12+(i/2)*20, recipe.additives[i], IOType.INPUT, "frame");
		for(int i = 0; i < outputs.size(); i++)
			if(!outputs.get(i).isEmpty())
				builder.withSlot(126+(i%2)*20, 12+(i/2)*20, outputs.get(i), IOType.OUTPUT, "frame");
		if(!recipe.slag.isEmpty())
			builder.withSlot(168, 22, recipe.slag, IOType.OUTPUT, "frame_red");
		return builder.build();
	}
}
