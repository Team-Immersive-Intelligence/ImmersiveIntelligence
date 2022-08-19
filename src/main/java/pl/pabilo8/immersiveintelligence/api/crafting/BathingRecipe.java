package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

/**
 * @author Pabilo8
 * @since 08-08-2019
 */
public class BathingRecipe extends MultiblockRecipe
{
	public final IngredientStack itemInput;
	public final ItemStack itemOutput;
	public final boolean isWashing;

	public final FluidStack fluidInput;

	public static final LinkedList<BathingRecipe> recipeList = new LinkedList<>();
	final int totalProcessTime;
	final int totalProcessEnergy;

	public BathingRecipe(ItemStack itemOutput, Object itemInput, FluidStack fluidInput, int energy, int time, boolean isWashing)
	{
		this.itemOutput = itemOutput;
		this.itemInput = ApiUtils.createIngredientStack(itemInput);
		this.fluidInput = fluidInput;
		this.totalProcessEnergy = (int)Math.floor((float)energy);
		this.totalProcessTime = (int)Math.floor((float)time);
		this.isWashing = isWashing;

		this.fluidInputList = Collections.singletonList(this.fluidInput);

		this.inputList = Lists.newArrayList(this.itemInput);
		this.outputList = NonNullList.from(ItemStack.EMPTY, itemOutput);
	}

	public static BathingRecipe addRecipe(ItemStack itemOutput, IngredientStack itemInput, FluidStack fluidInput, int energy, int time)
	{
		BathingRecipe r = new BathingRecipe(itemOutput, itemInput, fluidInput, energy, time,false);
		recipeList.add(r);
		return r;
	}

	public static BathingRecipe addWashingRecipe(ItemStack itemOutput, IngredientStack itemInput, FluidStack fluidInput, int energy, int time)
	{
		BathingRecipe r = new BathingRecipe(itemOutput, itemInput, fluidInput, energy, time,true);
		recipeList.add(r);
		return r;
	}

	public static List<BathingRecipe> removeRecipesForOutput(ItemStack stack)
	{
		List<BathingRecipe> list = new ArrayList<>();
		Iterator<BathingRecipe> it = recipeList.iterator();
		while(it.hasNext())
		{
			BathingRecipe ir = it.next();
			if(OreDictionary.itemMatches(ir.itemOutput, stack, true))
			{
				list.add(ir);
				it.remove();
			}
		}
		return list;
	}

	public static BathingRecipe findRecipe(ItemStack item_input, FluidStack fluid_input)
	{
		for(BathingRecipe recipe : recipeList)
		{
			if(recipe.itemInput.matchesItemStack(item_input)&&fluid_input.isFluidEqual(recipe.fluidInput)&&fluid_input.amount >= recipe.fluidInput.amount)
			{
				return recipe;
			}
		}
		return null;
	}

	public static List<BathingRecipe> findIncompleteBathingRecipe(ItemStack item_input, FluidStack fluid_input)
	{
		if(item_input==null||fluid_input==null)
			return null;
		List<BathingRecipe> list = Lists.newArrayList();

		for(BathingRecipe recipe : recipeList)
			if(recipe.itemInput.matchesItemStack(item_input)||fluid_input.isFluidEqual(recipe.fluidInput))
			{
				list.add(recipe);
				break;
			}
		return list;
	}

	@Override
	public int getMultipleProcessTicks()
	{
		return 0;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("item_input", itemInput.writeToNBT(new NBTTagCompound()));
		nbt.setTag("fluid_input", fluidInput.writeToNBT(new NBTTagCompound()));
		return nbt;
	}

	public static BathingRecipe loadFromNBT(NBTTagCompound nbt)
	{
		IngredientStack item_input = IngredientStack.readFromNBT(nbt.getCompoundTag("item_input"));
		FluidStack fluid_input = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("fluid_input"));

		return findRecipe(item_input.stack, fluid_input);
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