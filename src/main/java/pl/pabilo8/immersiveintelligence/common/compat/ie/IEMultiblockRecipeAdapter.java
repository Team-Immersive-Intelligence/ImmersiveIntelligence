package pl.pabilo8.immersiveintelligence.common.compat.ie;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;

/**
 * Read-only adapter base for recipes imported from Immersive Engineering.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.07.2026
 */
public abstract class IEMultiblockRecipeAdapter<T extends MultiblockRecipe> extends IERecipeAdapterBase<T>
{
	protected IEMultiblockRecipeAdapter(T recipe, Object... nameSources)
	{
		super(recipe, recipe.getTotalProcessTime(), recipe.getTotalProcessEnergy(), nameSources);
	}
}
