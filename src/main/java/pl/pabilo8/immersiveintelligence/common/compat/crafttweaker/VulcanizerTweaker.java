package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import blusunrize.immersiveengineering.api.ComparableItemStack;
import blusunrize.immersiveengineering.common.util.compat.crafttweaker.CraftTweakerHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.VulcanizerRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

/**
 * @author Pabilo8
 * @since 19.06.2022
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".Vulcanizer")
@ZenRegister
public class VulcanizerTweaker
{
	@ZenMethod
	public static void addRecipe(IIngredient mainInput, IIngredient compoundInput, IIngredient sulfurInput, IItemStack itemMold, IItemStack itemOutput, int energy, String resIn, String resOut)
	{
		for(IIngredient o : new IIngredient[]{mainInput, compoundInput, sulfurInput})
		{
			if(o==null)
			{
				CraftTweakerAPI.getLogger().logError("Could not add vulcanizer recipe for "+itemOutput.getDisplayName()+", input was null");
				return;
			}
		}

		VulcanizerRecipe r = new VulcanizerRecipe(
				CraftTweakerHelper.toStack(itemOutput),
				new ComparableItemStack(CraftTweakerHelper.toStack(itemMold)),
				CraftTweakerHelper.toIEIngredientStack(mainInput),
				CraftTweakerHelper.toIEIngredientStack(compoundInput),
				CraftTweakerHelper.toIEIngredientStack(sulfurInput),
				energy,
				new ResourceLocation(resIn+".png"),
				new ResourceLocation(resOut+".png")
		);

		CraftTweakerAPI.apply(new Add(r));
	}

	@ZenMethod
	public static void addRecipe(IIngredient mainInput, IIngredient compoundInput, IIngredient sulfurInput, IItemStack itemMold, IItemStack itemOutput, int energy)
	{
		addRecipe(mainInput, compoundInput, sulfurInput, itemMold, itemOutput, energy, VulcanizerRecipe.TEXTURE_LATEX.toString(), VulcanizerRecipe.TEXTURE_RUBBER.toString());
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output)
	{
		CraftTweakerAPI.apply(new Remove(CraftTweakerHelper.toStack(output)));
	}

	private static class Add implements IAction
	{
		private final VulcanizerRecipe recipe;

		public Add(VulcanizerRecipe recipe)
		{
			this.recipe = recipe;
		}

		@Override
		public void apply()
		{
			VulcanizerRecipe.recipeList.put(recipe.mold, recipe);
		}

		@Override
		public String describe()
		{
			return "Adding Vulcanizer Recipe for "+recipe.output.getDisplayName();
		}
	}

	private static class Remove implements IAction
	{
		private final ItemStack output;
		List<VulcanizerRecipe> removedRecipes;

		public Remove(ItemStack output)
		{
			this.output = output;
		}

		@Override
		public void apply()
		{
			removedRecipes = VulcanizerRecipe.removeRecipes(output);
		}

		@Override
		public String describe()
		{
			return "Removing Vulcanizer Recipe for "+output.getDisplayName();
		}
	}

}
