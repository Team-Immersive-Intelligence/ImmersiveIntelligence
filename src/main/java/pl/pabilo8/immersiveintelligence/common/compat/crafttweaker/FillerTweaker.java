package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.compat.crafttweaker.CraftTweakerHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.DustStack;
import pl.pabilo8.immersiveintelligence.api.crafting.FillerRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

/**
 * @author Pabilo8
 * @since 05.01.2022
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".Filler")
@ZenRegister
public class FillerTweaker
{
	@ZenMethod
	public static void addRecipe(IItemStack itemOutput, IIngredient itemInput, String dust, int amount, int time, int energy)
	{
		ItemStack stackOut = CraftTweakerMC.getItemStack(itemOutput);
		IngredientStack stackIn = CraftTweakerHelper.toIEIngredientStack(itemInput);

		FillerRecipe r = new FillerRecipe(stackOut, stackIn, new DustStack(dust, amount), time, energy);
		CraftTweakerAPI.apply(new Add(r));
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output)
	{
		CraftTweakerAPI.apply(new Remove(CraftTweakerHelper.toStack(output)));
	}

	private static class Add implements IAction
	{
		private final FillerRecipe recipe;

		public Add(FillerRecipe recipe)
		{
			this.recipe = recipe;
		}

		@Override
		public void apply()
		{
			FillerRecipe.recipeList.add(recipe);
		}

		@Override
		public String describe()
		{
			return "Adding Filler Recipe for "+recipe.itemOutput.getDisplayName();
		}
	}

	private static class Remove implements IAction
	{
		private final ItemStack output;
		List<FillerRecipe> removedRecipes;

		public Remove(ItemStack output)
		{
			this.output = output;
		}

		@Override
		public void apply()
		{
			removedRecipes = FillerRecipe.removeRecipesForOutput(output);
		}

		@Override
		public String describe()
		{
			return "Removing Filler Recipe for "+output.getDisplayName();
		}
	}

}
