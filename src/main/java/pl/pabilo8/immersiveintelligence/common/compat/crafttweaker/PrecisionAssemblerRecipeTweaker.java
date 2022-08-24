package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

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
 * @author Pabilo8
 * @since 08-08-2019
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".PrecisionAssembler")
@ZenRegister
public class PrecisionAssemblerRecipeTweaker
{
	@ZenMethod
	public static void addRecipe(IItemStack itemOutput, IItemStack trash, IIngredient[] itemInputs, String[] tools, String[] animations, int energy, float timeMultiplier)
	{
		Object[] adds = null;
		if(itemInputs!=null)
		{
			adds = new Object[itemInputs.length];
			for(int i = 0; i < itemInputs.length; i++)
				adds[i] = CraftTweakerHelper.toObject(itemInputs[i]);
		}

		if(adds==null)
		{
			CraftTweakerAPI.getLogger().logError("Could not add precision assembler recipe for "+itemOutput.getDisplayName()+", no valid inputs were provided");
			return;
		}
		if(tools==null)
		{
			CraftTweakerAPI.getLogger().logError("Could not add precision assembler recipe for "+itemOutput.getDisplayName()+", no tools were provided");
			return;
		}
		if(animations==null)
		{
			CraftTweakerAPI.getLogger().logError("Could not add precision assembler recipe for "+itemOutput.getDisplayName()+", no valid animations were provided");
			return;
		}

		PrecissionAssemblerRecipe r = new PrecissionAssemblerRecipe(CraftTweakerHelper.toStack(itemOutput), CraftTweakerHelper.toStack(trash), adds, tools, animations, energy, timeMultiplier);
		CraftTweakerAPI.apply(new Add(r));
	}

	@ZenMethod
	public static void removeRecipe(IItemStack itemOutput)
	{
		CraftTweakerAPI.apply(new Remove(CraftTweakerHelper.toStack(itemOutput)));
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
			return "Adding precision Assembler Recipe for "+recipe.output.getUnlocalizedName();
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
			return "Removing precision Assembler Recipe for "+f1.getUnlocalizedName();
		}
	}

	@ZenClass("mods."+ImmersiveIntelligence.MODID+".PrecissionAssembler")
	@ZenRegister
	public static class OldAssemblerCompat
	{
		/**
		 * Added as a compat for the old CT methods
		 */
		@ZenMethod
		public static void addRecipe(IItemStack itemOutput, IItemStack trash, IIngredient[] itemInputs, String[] tools, String[] animations, int energy, float timeMultiplier)
		{
			PrecisionAssemblerRecipeTweaker.addRecipe(itemOutput, trash, itemInputs, tools, animations, energy, timeMultiplier);
		}

		public static void removeRecipe(IItemStack itemOutput)
		{
			PrecisionAssemblerRecipeTweaker.removeRecipe(itemOutput);
		}
	}

}
