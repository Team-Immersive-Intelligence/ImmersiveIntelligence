package pl.pabilo8.immersiveintelligence.common.compat;

import blusunrize.immersiveengineering.common.util.compat.crafttweaker.CraftTweakerHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecissionAssemblerRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

/**
 * Created by Pabilo8 on 08-08-2019.
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".PrecissionAssembler")
@ZenRegister
public class PrecissionAssemblerRecipeTweaker
{
	@ZenMethod
	public static void addRecipe(IItemStack itemOutput, IItemStack trash, IIngredient[] itemInputs, String[] tools, String[] animations, int energy, int timeMultiplier)
	{
		Object[] adds = null;
		if(itemInputs!=null)
		{
			adds = new Object[itemInputs.length];
			for(int i = 0; i < itemInputs.length; i++)
				adds[i] = CraftTweakerHelper.toObject(itemInputs[i]);
		}

		PrecissionAssemblerRecipe r = new PrecissionAssemblerRecipe(CraftTweakerHelper.toStack(itemOutput), CraftTweakerHelper.toStack(trash), adds, tools, animations, energy, timeMultiplier);
		CraftTweakerAPI.apply(new PrecissionAssemblerRecipeTweaker.Add(r));
	}

	@ZenMethod
	public static void removeRecipe(IItemStack itemOutput)
	{
		CraftTweakerAPI.apply(new PrecissionAssemblerRecipeTweaker.Remove(CraftTweakerHelper.toStack(itemOutput)));
	}

	private static class Add implements IAction
	{
		private final PrecissionAssemblerRecipe recipe;

		public Add(PrecissionAssemblerRecipe recipe)
		{
			this.recipe = recipe;
		}

		@Override
		public void apply()
		{
			PrecissionAssemblerRecipe.recipeList.add(recipe);
		}

		@Override
		public String describe()
		{
			return "Adding Precission Assembler Recipe for "+recipe.output.getTranslationKey();
		}
	}

	private static class Remove implements IAction
	{
		List<PrecissionAssemblerRecipe> removedRecipes;
		ItemStack f1;

		public Remove(ItemStack stack)
		{
			this.f1 = stack;
		}

		@Override
		public void apply()
		{
			removedRecipes = PrecissionAssemblerRecipe.removeRecipesForOutput(f1);
		}

		@Override
		public String describe()
		{
			return "Removing Chemical Bath Recipe for "+f1.getTranslationKey();
		}
	}

}
