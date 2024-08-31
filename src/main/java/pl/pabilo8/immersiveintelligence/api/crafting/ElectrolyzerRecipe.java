package pl.pabilo8.immersiveintelligence.api.crafting;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.IIMultiblockRecipe;

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
public class ElectrolyzerRecipe extends IIMultiblockRecipe
{
	public final FluidStack fluidInput;
	public final FluidStack[] fluidOutputs;

	public static ArrayList<ElectrolyzerRecipe> recipeList = new ArrayList<>();
	public int energyPerTick;

	public ElectrolyzerRecipe(FluidStack fluidInput, FluidStack fluidOutput1, @Nullable FluidStack fluidOutput2, int energy, int time)
	{
		this.fluidOutputs = new FluidStack[2];
		this.fluidOutputs[0] = fluidOutput1;
		this.fluidOutputs[1] = fluidOutput2;
		this.fluidInput = fluidInput;

		this.totalProcessEnergy = energy;
		this.totalProcessTime = time;

		this.fluidInputList = Collections.singletonList(this.fluidInput);
		this.fluidOutputList = Arrays.asList(fluidOutputs);
		this.energyPerTick = (int)Math.floor((float)energy/time);
	}

	public static ElectrolyzerRecipe addRecipe(FluidStack fluidInput, FluidStack fluidOutput1, FluidStack fluidOutput2, int energy, int time)
	{
		//nwd(f1,f2)
		int gcd = IIMath.gcd(fluidInput.amount, fluidOutput1.amount, fluidOutput2.amount, energy, time);
		fluidInput.amount /= gcd;
		fluidOutput1.amount /= gcd;
		fluidOutput2.amount /= gcd;
		energy /= gcd;
		time /= gcd;

		ElectrolyzerRecipe r = new ElectrolyzerRecipe(fluidInput, fluidOutput1, fluidOutput2, energy, time);
		recipeList.add(r);
		return r;
	}

	public static List<ElectrolyzerRecipe> removeRecipesForInput(FluidStack fluidInput)
	{
		List<ElectrolyzerRecipe> recipes = recipeList.stream()
				.filter(r -> r.fluidInput.isFluidEqual(fluidInput))
				.collect(Collectors.toList());
		recipeList.removeAll(recipes);

		return recipes;
	}

	public static ElectrolyzerRecipe findRecipe(FluidStack fluidInput)
	{
		for(ElectrolyzerRecipe recipe : recipeList)
			if(recipe.fluidInput.getFluid()==fluidInput.getFluid()&&fluidInput.amount >= recipe.fluidInput.amount)
				return recipe;
		return null;
	}

	@Override
	public int getMultipleProcessTicks()
	{
		return 0;
	}

	@Override
	public EasyNBT writeToNBT()
	{
		return EasyNBT.newNBT().withFluidStack("fluid_input", fluidInput);
	}

	public static ElectrolyzerRecipe loadFromNBT(NBTTagCompound nbt)
	{
		FluidStack fluid_input = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("fluid_input"));
		return findRecipe(fluid_input);
	}

}