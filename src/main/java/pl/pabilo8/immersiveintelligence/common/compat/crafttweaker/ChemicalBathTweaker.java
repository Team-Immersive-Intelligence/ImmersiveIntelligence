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
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 08.08.2019
 */
@ZenClass("mods."+ImmersiveIntelligence.MODID+".ChemicalBath")
@ZenRegister
public class ChemicalBathTweaker
{
	@ZenMethod
	public static void addWashingRecipe(IIngredient itemInput, IItemStack itemOutput, ILiquidStack fluidInput, int energy, int time)
	{
		Object oItemInput = CraftTweakerHelper.toObject(itemInput);
		if(oItemInput==null)
		{
			CraftTweakerAPI.getLogger().logError("Could not add chemical bath washing recipe for "+itemOutput.getDisplayName()+", input was null");
			return;
		}

		FluidStack mcFluidInputStack = CraftTweakerMC.getLiquidStack(fluidInput);
		CraftTweakerAPI.apply(new Add(CraftTweakerHelper.toStack(itemOutput), oItemInput, mcFluidInputStack, energy, time, true));
	}

	@ZenMethod
	public static void addRecipe(IIngredient itemInput, IItemStack itemOutput, ILiquidStack fluidInput, int energy, int time)
	{

		Object oItemInput = CraftTweakerHelper.toObject(itemInput);
		if(oItemInput==null)
		{
			CraftTweakerAPI.getLogger().logError("Could not add chemical bath recipe for "+itemOutput.getDisplayName()+", input was null");
			return;
		}

		FluidStack mcFluidInputStack = CraftTweakerMC.getLiquidStack(fluidInput);
		CraftTweakerAPI.apply(new Add(CraftTweakerHelper.toStack(itemOutput), oItemInput, mcFluidInputStack, energy, time, false));
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output)
	{
		CraftTweakerAPI.apply(new Remove(CraftTweakerHelper.toStack(output)));
	}

	private static class Add implements IAction
	{
		private final ItemStack itemOutput;
		private final Object itemInput;
		private final FluidStack fluidInputStack;
		private final int energy;
		private final int time;
		private final boolean washing;

		public Add(ItemStack itemOutput, Object itemInput, FluidStack fluidInputStack, int energy, int time, boolean washing)
		{
			this.itemOutput = itemOutput;
			this.itemInput = itemInput;
			this.fluidInputStack = fluidInputStack;
			this.energy = energy;
			this.time = time;
			this.washing = washing;
		}

		@Override
		public void apply()
		{
			new BathingRecipe(itemOutput, itemInput, fluidInputStack, energy, time, washing);
		}

		@Override
		public String describe()
		{
			return "Adding Chemical Bath Recipe for "+itemOutput.getDisplayName();
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
			removedRecipes = IIMultiblockRecipe.removeRecipesByFilter(BathingRecipe.class, bathingRecipe -> bathingRecipe.itemOutput.isItemEqual(output));
		}

		@Override
		public String describe()
		{
			return "Removing Chemical Bath Recipe for "+output.getDisplayName();
		}
	}

}
