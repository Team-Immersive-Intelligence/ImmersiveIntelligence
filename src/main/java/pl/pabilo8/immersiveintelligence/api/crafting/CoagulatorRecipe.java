package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Coagulator;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author Pabilo8
 * @since 08-08-2019
 */
public class CoagulatorRecipe extends MultiblockRecipe
{
	public final FluidStack fluidInput, coagulantInput;
	public final ItemStack itemOutput;

	public static LinkedList<CoagulatorRecipe> recipeList = new LinkedList<>();
	public static HashMap<ItemStack, Integer> dryingMap = new HashMap<>();
	int totalProcessTime;
	int totalProcessEnergy;

	public CoagulatorRecipe(ItemStack itemOutput, FluidStack fluidInput, FluidStack coagulantInput, int energy, int mixingTime)
	{
		this.itemOutput = itemOutput;
		this.fluidInput = fluidInput;
		this.coagulantInput = coagulantInput;
		this.totalProcessEnergy = (int)Math.floor((float)energy);
		this.totalProcessTime = (int)Math.floor((float)mixingTime);

		this.fluidInputList = new ArrayList<>();
		this.fluidInputList.add(this.fluidInput);
		this.fluidInputList.add(this.coagulantInput);

		this.outputList = NonNullList.from(ItemStack.EMPTY, itemOutput);
	}

	public static CoagulatorRecipe addRecipe(ItemStack itemOutput, FluidStack fluidInput, FluidStack coagulantInput, int energy, int mixingTime, int dryingTime)
	{
		CoagulatorRecipe r = new CoagulatorRecipe(itemOutput, fluidInput, coagulantInput, energy, mixingTime);
		recipeList.add(r);
		dryingMap.put(itemOutput, dryingTime);
		return r;
	}

	public static List<CoagulatorRecipe> removeRecipesForOutput(ItemStack stack)
	{
		List<CoagulatorRecipe> list = new ArrayList<>();
		Iterator<CoagulatorRecipe> it = recipeList.iterator();
		while(it.hasNext())
		{
			CoagulatorRecipe ir = it.next();
			if(OreDictionary.itemMatches(ir.itemOutput, stack, true))
			{
				list.add(ir);
				it.remove();
			}
		}
		return list;
	}

	public static CoagulatorRecipe findRecipe(FluidStack fluidInput, FluidStack coagulantInput)
	{
		for(CoagulatorRecipe recipe : recipeList)
		{
			if(fluidInput.isFluidEqual(recipe.fluidInput)&&fluidInput.amount >= recipe.fluidInput.amount&&
					coagulantInput.isFluidEqual(recipe.coagulantInput)&&coagulantInput.amount >= recipe.coagulantInput.amount)
			{
				return recipe;
			}
		}
		return null;
	}

	@Nonnull
	public static FluidStack getFluidForOutputStack(ItemStack effect)
	{
		for(CoagulatorRecipe recipe : recipeList)
			if(OreDictionary.itemMatches(effect, recipe.itemOutput, false))
				return recipe.fluidInput;
		//¯\_(ツ)_/¯
		return new FluidStack(FluidRegistry.WATER, 1000);
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
		nbt.setTag("coagulant_input", coagulantInput.writeToNBT(new NBTTagCompound()));
		return nbt;
	}

	public static CoagulatorRecipe loadFromNBT(NBTTagCompound nbt)
	{
		IngredientStack item_input = IngredientStack.readFromNBT(nbt.getCompoundTag("item_input"));
		FluidStack fluidInput = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("fluid_input"));
		FluidStack coagulantInput = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("coagulant_input"));

		return findRecipe(fluidInput, coagulantInput);
	}

	public static int getBucketProgressForStack(ItemStack stack)
	{
		return dryingMap.entrySet().stream()
				.filter(e -> OreDictionary.itemMatches(e.getKey(), stack, false))
				.map(Entry::getValue).findFirst().orElse(Coagulator.bucketTime);
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