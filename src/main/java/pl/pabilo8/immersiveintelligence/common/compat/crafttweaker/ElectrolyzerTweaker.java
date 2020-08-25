package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.ElectrolyzerRecipe;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

/**
 * @author Pabilo8
 * @since 08-08-2019
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".Electrolyzer")
@ZenRegister
public class ElectrolyzerTweaker
{
	public ElectrolyzerTweaker()
	{

	}

	@ZenMethod
	public static void addRecipe(ILiquidStack fluidInput, ILiquidStack fluidOutput1, int energy, int time, @Optional ILiquidStack fluidOutput2)
	{
		FluidStack mcFluidInputStack = CraftTweakerMC.getLiquidStack(fluidInput);
		FluidStack mcFluidOutputStack0 = CraftTweakerMC.getLiquidStack(fluidOutput1);
		FluidStack mcFluidOutputStack1 = null;
		if(fluidOutput2!=null)
			mcFluidOutputStack1 = CraftTweakerMC.getLiquidStack(fluidOutput2);

		ElectrolyzerRecipe r = new ElectrolyzerRecipe(mcFluidInputStack, mcFluidOutputStack0, mcFluidOutputStack1, energy, time);

		CraftTweakerAPI.apply(new Add(r));
	}

	@ZenMethod
	public static void removeRecipe(ILiquidStack fluidOutput1, @Optional ILiquidStack fluidOutput2)
	{
		CraftTweakerAPI.apply(new Remove(CraftTweakerMC.getLiquidStack(fluidOutput1), fluidOutput2!=null?CraftTweakerMC.getLiquidStack(fluidOutput2): null));
	}

	private static class Add implements IAction
	{
		private final ElectrolyzerRecipe recipe;

		public Add(ElectrolyzerRecipe recipe)
		{
			this.recipe = recipe;
		}

		@Override
		public void apply()
		{
			ElectrolyzerRecipe.recipeList.add(recipe);
		}

		@Override
		public String describe()
		{
			return "Adding Electrolyzer Recipe for "+recipe.fluidInput.getLocalizedName();
		}
	}

	private static class Remove implements IAction
	{
		List<ElectrolyzerRecipe> removedRecipes;
		FluidStack f1, f2;

		public Remove(FluidStack f1, FluidStack f2)
		{
			this.f1 = f1;
			this.f2 = f2;
		}

		@Override
		public void apply()
		{
			removedRecipes = ElectrolyzerRecipe.removeRecipesForOutput(f1, f2);
		}

		@Override
		public String describe()
		{
			return "Removing Electrolyzer Recipe for "+f2.getLocalizedName();
		}
	}

}
