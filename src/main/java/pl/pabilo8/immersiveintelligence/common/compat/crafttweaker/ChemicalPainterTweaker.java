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
import pl.pabilo8.immersiveintelligence.api.crafting.PaintingRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

/**
 * @author Pabilo8
 * @since 06.01.2022
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".ChemicalPainter")
@ZenRegister
public class ChemicalPainterTweaker
{
	@ZenMethod
	public static void addRecipe(IIngredient itemInput, IChemicalPainterFunction function, int paintAmount, int energy, int time)
	{

		IngredientStack oItemInput = CraftTweakerHelper.toIEIngredientStack(itemInput);
		if(oItemInput==null)
		{
			CraftTweakerAPI.getLogger().logError("Could not add chemical painter recipe for "+itemInput.toCommandString()+", input was null");
			return;
		}

		PaintingRecipe r = new PaintingRecipe((rgb, stack) ->
				CraftTweakerMC.getItemStack(function.process(CraftTweakerMC.getIItemStack(stack),
						rgb.getPackedRGB(),
						rgb.getDyeColor().getColorValue())),
				oItemInput, energy, time, paintAmount);

		CraftTweakerAPI.apply(new Add(r));
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output)
	{
		CraftTweakerAPI.apply(new Remove(CraftTweakerHelper.toStack(output)));
	}

	private static class Add implements IAction
	{
		private final PaintingRecipe recipe;

		public Add(PaintingRecipe recipe)
		{
			this.recipe = recipe;
		}

		@Override
		public void apply()
		{
			PaintingRecipe.recipeList.add(recipe);
		}

		@Override
		public String describe()
		{
			return "Adding Chemical Painter Recipe for "+recipe.itemInput.getExampleStack().getDisplayName();
		}
	}

	private static class Remove implements IAction
	{
		private final ItemStack input;
		List<PaintingRecipe> removedRecipes;

		public Remove(ItemStack output)
		{
			this.input = output;
		}

		@Override
		public void apply()
		{
			removedRecipes = PaintingRecipe.removeRecipesForInput(input);
		}

		@Override
		public String describe()
		{
			return "Removing Chemical Painter Recipe for "+input.getDisplayName();
		}
	}

	@ZenClass("mods."+ImmersiveIntelligence.MODID+".IChemicalPainterFunction")
	@ZenRegister
	public interface IChemicalPainterFunction
	{
		IItemStack process(IItemStack input, int color, int baseColor);
	}

}
