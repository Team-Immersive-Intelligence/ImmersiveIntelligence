package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import blusunrize.immersiveengineering.api.ComparableItemStack;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
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
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 19.06.2022
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".Vulcanizer")
@ZenRegister
public class VulcanizerTweaker
{
	@ZenMethod
	public static void addRecipe(IIngredient mainInput, IIngredient compoundInput, IIngredient sulfurInput, IItemStack itemMold, IItemStack itemOutput, int energy, String model)
	{
		for(IIngredient o : new IIngredient[]{mainInput, compoundInput, sulfurInput})
			if(o==null)
			{
				CraftTweakerAPI.getLogger().logError("Could not add vulcanizer recipe for "+itemOutput.getDisplayName()+", input was null");
				return;
			}

		CraftTweakerAPI.apply(new Add(
				CraftTweakerHelper.toStack(itemOutput),
				new ComparableItemStack(CraftTweakerHelper.toStack(itemMold)),
				CraftTweakerHelper.toIEIngredientStack(mainInput),
				CraftTweakerHelper.toIEIngredientStack(compoundInput),
				CraftTweakerHelper.toIEIngredientStack(sulfurInput),
				energy,
				new ResourceLocation(model)
		));
	}

	@ZenMethod
	public static void addRecipe(IIngredient mainInput, IIngredient compoundInput, IIngredient sulfurInput, IItemStack itemMold, IItemStack itemOutput, int energy)
	{
		addRecipe(mainInput, compoundInput, sulfurInput, itemMold, itemOutput, energy, VulcanizerRecipe.MODEL_TIRE.toString());
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output)
	{
		CraftTweakerAPI.apply(new Remove(CraftTweakerHelper.toStack(output)));
	}

	private static class Add implements IAction
	{
		private final ItemStack output;
		private final ComparableItemStack mold;
		private final IngredientStack mainInput;
		private final IngredientStack compoundInput;
		private final IngredientStack sulfurInput;
		private final int energy;
		private final ResourceLocation model;

		public Add(ItemStack output, ComparableItemStack mold, IngredientStack mainInput, IngredientStack compoundInput, IngredientStack sulfurInput,
				   int energy, ResourceLocation model)
		{
			this.output = output;
			this.mold = mold;
			this.mainInput = mainInput;
			this.compoundInput = compoundInput;
			this.sulfurInput = sulfurInput;
			this.energy = energy;
			this.model = model;
		}

		@Override
		public void apply()
		{
			new VulcanizerRecipe(output, mold, mainInput, compoundInput, sulfurInput, energy, model);
		}

		@Override
		public String describe()
		{
			return "Adding Vulcanizer Recipe for "+output.getDisplayName();
		}
	}

	private static class Remove implements IAction
	{
		private final ItemStack output;
		private List<VulcanizerRecipe> removedRecipes;

		public Remove(ItemStack output)
		{
			this.output = output;
		}

		@Override
		public void apply()
		{
			this.removedRecipes = IIMultiblockRecipe.removeRecipesByFilter(VulcanizerRecipe.class, recipe -> recipe.output.isItemEqual(output));
		}

		@Override
		public String describe()
		{
			return "Removing Vulcanizer Recipe for "+output.getDisplayName();
		}
	}

}
