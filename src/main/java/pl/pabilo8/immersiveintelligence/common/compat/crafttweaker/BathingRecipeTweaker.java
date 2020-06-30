package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import blusunrize.immersiveengineering.common.util.compat.crafttweaker.CraftTweakerHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.BathingRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

/**
 * @author Pabilo8
 * @since 08-08-2019
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".ChemicalBath")
@ZenRegister
public class BathingRecipeTweaker
{
	public BathingRecipeTweaker()
	{

	}

	@ZenMethod
	public static void addRecipe(IIngredient itemInput, IItemStack itemOutput, ILiquidStack fluidInput, int energy, int time)
	{

		Object oItemInput = CraftTweakerHelper.toObject(itemInput);
		if(oItemInput==null)
		{
			CraftTweakerAPI.getLogger().logError("Did not add chemical bath recipe for, input was null");
			return;
		}

		FluidStack mcFluidInputStack = CraftTweakerMC.getLiquidStack(fluidInput);

		BathingRecipe r = new BathingRecipe(CraftTweakerHelper.toStack(itemOutput), oItemInput, mcFluidInputStack, energy, time);

		CraftTweakerAPI.apply(new Add(r));
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output)
	{
		CraftTweakerAPI.apply(new Remove(CraftTweakerHelper.toStack(output)));
	}

	private static class Add implements IAction
	{
		private final BathingRecipe recipe;

		public Add(BathingRecipe recipe)
		{
			this.recipe = recipe;
		}

		@Override
		public void apply()
		{
			BathingRecipe.recipeList.add(recipe);
		}

		@Override
		public String describe()
		{
			return "Adding Chemical Bath Recipe for "+recipe.itemOutput.getDisplayName();
		}
	}

	private static class Remove implements IAction
	{
		private final ItemStack output;
		List<BathingRecipe> removedRecipes;

		public Remove(ItemStack output)
		{
			this.output = output;
		}

		@Override
		public void apply()
		{
			removedRecipes = BathingRecipe.removeRecipesForOutput(output);
		}

		@Override
		public String describe()
		{
			return "Removing Chemical Bath Recipe for "+output.getDisplayName();
		}
	}

}
