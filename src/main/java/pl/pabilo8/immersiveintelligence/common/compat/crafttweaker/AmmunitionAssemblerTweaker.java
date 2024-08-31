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
import pl.pabilo8.immersiveintelligence.api.crafting.AmmunitionAssemblerRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

/**
 * @author Pabilo8
 * @since 06.01.2022
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".AmmunitionAssembler")
@ZenRegister
public class AmmunitionAssemblerTweaker
{
	@ZenMethod
	public static void addRecipe(IIngredient coreInput, IIngredient casingInput, IAmmunitionAssemblerFunction function, int energy, int time)
	{

		IngredientStack iCoreInput = CraftTweakerHelper.toIEIngredientStack(coreInput);
		IngredientStack iCasingInput = CraftTweakerHelper.toIEIngredientStack(casingInput);

		Object oItemInput = CraftTweakerHelper.toObject(coreInput);
		if(oItemInput==null)
		{
			CraftTweakerAPI.getLogger().logError("Could not add ammunition assembler recipe, input was null");
			return;
		}

		AmmunitionAssemblerRecipe r = new AmmunitionAssemblerRecipe(
				(s1, s2) -> CraftTweakerMC.getItemStack(function.process(CraftTweakerMC.getIItemStack(s1), CraftTweakerMC.getIItemStack(s2))),
				iCoreInput, iCasingInput, energy, time, false);

		CraftTweakerAPI.apply(new Add(r));
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output)
	{
		CraftTweakerAPI.apply(new Remove(CraftTweakerHelper.toStack(output)));
	}

	private static class Add implements IAction
	{
		private final AmmunitionAssemblerRecipe recipe;

		public Add(AmmunitionAssemblerRecipe recipe)
		{
			this.recipe = recipe;
		}

		@Override
		public void apply()
		{
			AmmunitionAssemblerRecipe.RECIPES.add(recipe);
		}

		@Override
		public String describe()
		{
			return "Adding Ammunition Assembler Recipe for "+recipe.coreInput.getExampleStack().getDisplayName();
		}
	}

	private static class Remove implements IAction
	{
		private final ItemStack input;
		List<AmmunitionAssemblerRecipe> removedRecipes;

		public Remove(ItemStack input)
		{
			this.input = input;
		}

		@Override
		public void apply()
		{
			removedRecipes = AmmunitionAssemblerRecipe.removeRecipesForCore(input);
		}

		@Override
		public String describe()
		{
			return "Removing Ammunition Assembler Recipe for "+input.getDisplayName();
		}
	}

	@ZenClass("mods."+ImmersiveIntelligence.MODID+".IAmmunitionAssemblerFunction")
	@ZenRegister
	public interface IAmmunitionAssemblerFunction
	{
		IItemStack process(IItemStack inputCore, IItemStack inputCasing);
	}

}
