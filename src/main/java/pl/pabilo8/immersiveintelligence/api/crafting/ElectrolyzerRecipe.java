package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author Pabilo8
 * @since 08-08-2019
 */
public class ElectrolyzerRecipe extends MultiblockRecipe
{
	public static float energyModifier = 1.0F;
	public static float timeModifier = 1.0F;

	public final FluidStack fluidInput;
	public final FluidStack[] fluidOutputs;

	public static ArrayList<ElectrolyzerRecipe> recipeList = new ArrayList();
	int totalProcessTime;
	int totalProcessEnergy;

	public ElectrolyzerRecipe(FluidStack fluidInput, FluidStack fluidOutput1, @Nullable FluidStack fluidOutput2, int energy, int time)
	{
		this.fluidOutputs = new FluidStack[2];
		this.fluidOutputs[0] = fluidOutput1;
		this.fluidOutputs[1] = fluidOutput2;
		this.fluidInput = fluidInput;

		this.totalProcessEnergy = (int)Math.floor((float)energy*energyModifier);
		this.totalProcessTime = (int)Math.floor((float)time*timeModifier);

		this.fluidInputList = Collections.singletonList(this.fluidInput);
		this.fluidOutputList = Arrays.asList(fluidOutputs);
	}

	public static ElectrolyzerRecipe addRecipe(FluidStack fluidInput, FluidStack fluidOutput1, FluidStack fluidOutput2, int energy, int time)
	{
		ElectrolyzerRecipe r = new ElectrolyzerRecipe(fluidInput, fluidOutput1, fluidOutput2, energy, time);
		recipeList.add(r);
		return r;
	}

	public static List<ElectrolyzerRecipe> removeRecipesForOutput(FluidStack fluidOutput1, @Nullable FluidStack fluidOutput2)
	{
		List<ElectrolyzerRecipe> list = new ArrayList();
		Iterator<ElectrolyzerRecipe> it = recipeList.iterator();
		while(it.hasNext())
		{
			ElectrolyzerRecipe ir = it.next();
			if(fluidOutput2==null)
			{
				if(ir.fluidOutputList.get(0)==fluidOutput1||ir.fluidOutputList.get(1)==fluidOutput1)
				{
					list.add(ir);
					it.remove();
				}
			}
			else
			{
				if(ir.fluidOutputList.get(0)==fluidOutput1||ir.fluidOutputList.get(1)==fluidOutput2)
				{
					list.add(ir);
					it.remove();
				}
			}
		}
		return list;
	}

	public static ElectrolyzerRecipe findRecipe(FluidStack fluidInput)
	{
		for(ElectrolyzerRecipe recipe : recipeList)
		{
			if(recipe.fluidInput.getFluid()==fluidInput.getFluid()&&fluidInput.amount >= recipe.fluidInput.amount)
			{
				return recipe;
			}
		}
		return null;
	}

	public static ElectrolyzerRecipe findIncompleteRecipe(FluidStack fluidInput)
	{
		for(ElectrolyzerRecipe recipe : recipeList)
		{
			if(recipe.fluidInput.getFluid()==fluidInput.getFluid())
			{
				return recipe;
			}
		}
		return null;
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

	public static ElectrolyzerRecipe loadFromNBT(NBTTagCompound nbt)
	{
		FluidStack fluid_input = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("fluid_input"));

		return findRecipe(fluid_input);
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