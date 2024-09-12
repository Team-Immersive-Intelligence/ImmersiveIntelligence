package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import blusunrize.immersiveengineering.common.util.compat.crafttweaker.CraftTweakerHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

/**
 * @author Pabilo8
 * @since 08-08-2019
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".Sawmill")
@ZenRegister
public class SawmillTweaker
{
	@ZenMethod
	public static void addRecipe(IIngredient itemInput, IItemStack itemOutput, IItemStack secondaryItemOutput, int torque, int time, int hardness, int dustColor)
	{

		Object oItemInput = CraftTweakerHelper.toObject(itemInput);
		if(oItemInput==null)
		{
			CraftTweakerAPI.getLogger().logError("Could not add sawmill recipe for "+itemOutput.getDisplayName()+", input was null");
			return;
		}


		SawmillRecipe r = new SawmillRecipe(CraftTweakerHelper.toStack(itemOutput), oItemInput, CraftTweakerHelper.toStack(secondaryItemOutput), torque, time, hardness, IIColor.fromPackedRGB(dustColor));

		CraftTweakerAPI.apply(new Add(r));
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output)
	{
		CraftTweakerAPI.apply(new Remove(CraftTweakerHelper.toStack(output)));
	}

	private static class Add implements IAction
	{
		private final SawmillRecipe recipe;

		public Add(SawmillRecipe recipe)
		{
			this.recipe = recipe;
		}

		@Override
		public void apply()
		{
			SawmillRecipe.RECIPES.add(recipe);
		}

		@Override
		public String describe()
		{
			return "Adding Sawmill Recipe for "+recipe.itemOutput.getDisplayName();
		}
	}

	private static class Remove implements IAction
	{
		private final ItemStack output;
		List<SawmillRecipe> removedRecipes;

		public Remove(ItemStack output)
		{
			this.output = output;
		}

		@Override
		public void apply()
		{
			removedRecipes = SawmillRecipe.removeRecipesForOutput(output);
		}

		@Override
		public String describe()
		{
			return "Removing Sawmill Recipe for "+output.getDisplayName();
		}
	}

}
