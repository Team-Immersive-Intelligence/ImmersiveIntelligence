package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @since 08-08-2019
 */
public class ElectrolyzerRecipe extends MultiblockRecipe
{
	public static ArrayList<ElectrolyzerRecipe> recipeList = new ArrayList<>();
	public final FluidStack fluidInput;
	public final FluidStack[] fluidOutputs;
	int totalProcessTime;
	int totalProcessEnergy;

	public ElectrolyzerRecipe(FluidStack fluidInput, FluidStack fluidOutput1, @Nullable FluidStack fluidOutput2, int energy, int time)
	{
		this.fluidOutputs = new FluidStack[2];
		this.fluidOutputs[0] = fluidOutput1;
		this.fluidOutputs[1] = fluidOutput2;
		this.fluidInput = fluidInput;

		this.totalProcessEnergy = (int)Math.floor((float)energy);
		this.totalProcessTime = (int)Math.floor((float)time);

		this.fluidInputList = Collections.singletonList(this.fluidInput);
		this.fluidOutputList = Arrays.asList(fluidOutputs);
	}

	public static ElectrolyzerRecipe addRecipe(FluidStack fluidInput, FluidStack fluidOutput1, FluidStack fluidOutput2, int energy, int time)
	{
		ElectrolyzerRecipe r = new ElectrolyzerRecipe(fluidInput, fluidOutput1, fluidOutput2, energy, time);
		recipeList.add(r);
		return r;
	}

	public static List<ElectrolyzerRecipe> removeRecipesForInput(FluidStack fluidInput)
	{
		List<ElectrolyzerRecipe> recipes = recipeList.stream()
				.filter(r -> r.fluidInput.isFluidEqual(fluidInput))
				.collect(Collectors.toList());

		for(ElectrolyzerRecipe recipe : recipes)
			recipeList.remove(recipe);

		return recipeList;
	}

	public static ElectrolyzerRecipe findRecipe(FluidStack fluidInput)
	{
		for(ElectrolyzerRecipe recipe : recipeList)
			if(recipe.fluidInput.getFluid()==fluidInput.getFluid()&&fluidInput.amount >= recipe.fluidInput.amount)
				return recipe;
		return null;
	}

	public static ElectrolyzerRecipe findIncompleteRecipe(FluidStack fluidInput)
	{
		for(ElectrolyzerRecipe recipe : recipeList)
			if(recipe.fluidInput.getFluid()==fluidInput.getFluid())
				return recipe;
		return null;
	}

	public static ElectrolyzerRecipe loadFromNBT(NBTTagCompound nbt)
	{
		FluidStack fluid_input = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("fluid_input"));
		return findRecipe(fluid_input);
	}

	@Override
	public int getMultipleProcessTicks()
	{
		return 0;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("fluid_input", fluidInput.writeToNBT(new NBTTagCompound()));
		return nbt;
	}

	public int getTotalProcessTime()
	{
		return this.totalProcessTime;
	}

	public int getTotalProcessEnergy()
	{
		return this.totalProcessEnergy;
	}

}