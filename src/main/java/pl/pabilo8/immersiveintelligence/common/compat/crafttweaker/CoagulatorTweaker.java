package pl.pabilo8.immersiveintelligence.common.compat.crafttweaker;

import blusunrize.immersiveengineering.common.util.compat.crafttweaker.CraftTweakerHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.CoagulatorRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.01.2022
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".Coagulator")
@ZenRegister
public class CoagulatorTweaker
{
	@ZenMethod
	public static void addRecipe(IItemStack itemOutput, ILiquidStack fluidInput, ILiquidStack coagulantInput, int energy, int mixingTime)
	{
		FluidStack f1 = CraftTweakerMC.getLiquidStack(fluidInput);
		FluidStack f2 = CraftTweakerMC.getLiquidStack(coagulantInput);
		ItemStack stack = CraftTweakerMC.getItemStack(itemOutput);

		if(f1==null||f2==null)
		{
			CraftTweakerAPI.getLogger().logError("Could not add coagulator recipe for "+itemOutput.getDisplayName()+", input was null");
			return;
		}

		CraftTweakerAPI.apply(new Add(stack, f1, f2, energy, mixingTime));
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output)
	{
		CraftTweakerAPI.apply(new Remove(CraftTweakerHelper.toStack(output)));
	}

	private static class Add implements IAction
	{
		private ItemStack itemOutput;
		private FluidStack fluidInput;
		private FluidStack coagulantInput;
		private int energy;
		private int mixingTime;

		public Add(ItemStack itemOutput, FluidStack fluidInput, FluidStack coagulantInput, int energy, int mixingTime)
		{
			this.itemOutput = itemOutput;
			this.fluidInput = fluidInput;
			this.coagulantInput = coagulantInput;
			this.energy = energy;
			this.mixingTime = mixingTime;
		}

		@Override
		public void apply()
		{
			new CoagulatorRecipe(itemOutput, fluidInput, coagulantInput, energy, mixingTime);
		}

		@Override
		public String describe()
		{
			return "Adding Coagulator Recipe for "+itemOutput.getDisplayName();
		}
	}

	private static class Remove implements IAction
	{
		private final ItemStack output;
		List<CoagulatorRecipe> removedRecipes;

		public Remove(ItemStack output)
		{
			this.output = output;
		}

		@Override
		public void apply()
		{
			this.removedRecipes = IIMultiblockRecipe.removeRecipesByFilter(CoagulatorRecipe.class, recipe -> recipe.itemOutput.isItemEqual(output));
		}

		@Override
		public String describe()
		{
			return "Removing Coagulator Recipe for "+output.getDisplayName();
		}
	}

}
