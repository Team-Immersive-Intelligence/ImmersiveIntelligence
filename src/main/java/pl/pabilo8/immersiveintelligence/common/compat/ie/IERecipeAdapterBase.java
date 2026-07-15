package pl.pabilo8.immersiveintelligence.common.compat.ie;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.07.2026
 */
abstract class IERecipeAdapterBase<T> extends IIMultiblockRecipe
{
	protected final T recipe;

	protected IERecipeAdapterBase(T recipe, int totalProcessTime, int totalProcessEnergy, Object... nameSources)
	{
		super(createRecipeName(nameSources));
		this.recipe = recipe;
		setTimeAndEnergy(totalProcessTime, totalProcessEnergy);
	}

	protected IERecipeAdapterBase(T recipe, int totalProcessTime, Object... nameSources)
	{
		this(recipe, totalProcessTime, 0, nameSources);
	}

	private static String createRecipeName(Object... nameSources)
	{
		List<Object> flattenedSources = new ArrayList<>();
		for(Object source : nameSources)
			appendNameSource(flattenedSources, source);

		if(flattenedSources.isEmpty())
			return "unnamed";

		Object first = flattenedSources.remove(0);
		return generateRecipeName(first, flattenedSources.toArray()).toLowerCase(Locale.ROOT);
	}

	private static void appendNameSource(List<Object> destination, @Nullable Object source)
	{
		if(source==null)
			return;
		if(source instanceof Object[])
		{
			for(Object nested : (Object[])source)
				appendNameSource(destination, nested);
			return;
		}
		if(source instanceof ItemStack)
			source = new IngredientStack((ItemStack)source);
		else if(source instanceof FluidStack)
			source = new IngredientStack((FluidStack)source);
		destination.add(source);
	}
}
