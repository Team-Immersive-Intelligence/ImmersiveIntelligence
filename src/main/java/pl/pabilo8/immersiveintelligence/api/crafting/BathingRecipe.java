package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.IOType;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;

import javax.annotation.Nullable;
import java.util.*;

//REFACTOR: 06.12.2025 use new system

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 08.08.2019
 */
public class BathingRecipe extends IIMultiblockRecipe
{
	public static final LinkedList<BathingRecipe> recipeList = new LinkedList<>();
	public final IngredientStack itemInput;
	public final ItemStack itemOutput;
	public final boolean isWashing;
	public final FluidStack fluidInput;

	public BathingRecipe(ItemStack itemOutput, Object itemInput, FluidStack fluidInput, int energy, int time, boolean isWashing)
	{
		super(isWashing?"washing": "bathing", itemOutput, fluidInput);
		setTimeAndEnergy(time, energy);

		this.itemOutput = itemOutput;
		this.itemInput = ApiUtils.createIngredientStack(itemInput);
		this.fluidInput = fluidInput;
		this.isWashing = isWashing;

		this.fluidInputList = Collections.singletonList(this.fluidInput);
		this.inputList = Lists.newArrayList(this.itemInput);
		this.outputList = NonNullList.from(ItemStack.EMPTY, itemOutput);
	}

	public static BathingRecipe addRecipe(ItemStack itemOutput, IngredientStack itemInput, FluidStack fluidInput, int energy, int time)
	{
		BathingRecipe r = new BathingRecipe(itemOutput, itemInput, fluidInput, energy, time, false);
		recipeList.add(r);
		return r;
	}

	public static BathingRecipe addWashingRecipe(ItemStack itemOutput, IngredientStack itemInput, FluidStack fluidInput, int energy, int time)
	{
		BathingRecipe r = new BathingRecipe(itemOutput, itemInput, fluidInput, energy, time, true);
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

	public static BathingRecipe loadFromNBT(NBTTagCompound nbt)
	{
		IngredientStack item_input = IngredientStack.readFromNBT(nbt.getCompoundTag("item_input"));
		FluidStack fluid_input = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("fluid_input"));

		return findRecipe(item_input.stack, fluid_input);
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		return new IIRecipeLayoutBuilder(148, 64)
				.withSlot(2, 16+2, itemInput, IOType.INPUT, "frame")
				.withInputFluidTank(2+20, 3, fluidInput)
				.withSlot(128, 16+2, itemOutput, IOType.OUTPUT, "frame")
				.withTimeInfo()
				.withPowerInfo()
				.withMultiblockModel(32+8, -8)
				.build();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("item_input", itemInput.writeToNBT(new NBTTagCompound()));
		nbt.setTag("fluid_input", fluidInput.writeToNBT(new NBTTagCompound()));
		return nbt;
	}

}
