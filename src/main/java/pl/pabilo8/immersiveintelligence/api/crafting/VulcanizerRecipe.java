/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.ComparableItemStack;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.util.ListUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.function.Function;

/**
 * @author BluSunrize - 07.01.2016
 * <p>
 * The recipe for the metal press
 */
public class VulcanizerRecipe extends MultiblockRecipe
{
	public static float energyModifier = 1;
	public static float timeModifier = 1;

	public final IngredientStack input;
	public final IngredientStack input2;
	public final ComparableItemStack mold;
	public final ItemStack output;
	int totalProcessTime;
	int totalProcessEnergy;

	public VulcanizerRecipe(ItemStack output, ComparableItemStack mold, IngredientStack input, IngredientStack input2, int energy)
	{
		this.output = output;
		this.mold = mold;
		this.input = ApiUtils.createIngredientStack(input);
		this.input2 = ApiUtils.createIngredientStack(input2);

		this.totalProcessEnergy = (int)Math.floor(energy*energyModifier);
		this.totalProcessTime = (int)Math.floor(120*timeModifier);

		this.inputList = Lists.newArrayList(this.input);
		this.outputList = ListUtils.fromItem(this.output);
	}

	public VulcanizerRecipe setInputSize(int size)
	{
		this.input.inputSize = size;
		return this;
	}

	@Override
	public void setupJEI()
	{
		super.setupJEI();
		this.jeiItemInputList = new ArrayList[2];
		this.jeiItemInputList[0] = Lists.newArrayList(jeiTotalItemInputList);
		this.jeiItemInputList[1] = Lists.newArrayList(mold.stack);
		this.jeiTotalItemInputList.add(mold.stack);
	}

	public boolean matches(ItemStack mold, ItemStack input)
	{
		return this.input.matches(input);
	}

	public VulcanizerRecipe getActualRecipe(ItemStack mold, ItemStack input)
	{
		return this;
	}

	public static ArrayListMultimap<ComparableItemStack, VulcanizerRecipe> recipeList = ArrayListMultimap.create();

	public static VulcanizerRecipe addRecipe(ItemStack output, ComparableItemStack mold, IngredientStack input, IngredientStack input2, int energy)
	{
		VulcanizerRecipe r = new VulcanizerRecipe(output, mold, input, input2, energy);
		recipeList.put(mold, r);
		return r;
	}

	public static VulcanizerRecipe findRecipe(ItemStack mold, ItemStack input)
	{
		if(mold.isEmpty()||input.isEmpty())
			return null;
		ComparableItemStack comp = ApiUtils.createComparableItemStack(mold, false);
		List<VulcanizerRecipe> list = recipeList.get(comp);
		for(VulcanizerRecipe recipe : list)
			if(recipe.matches(mold, input))
				return recipe.getActualRecipe(mold, input);
		return null;
	}

	public static List<VulcanizerRecipe> removeRecipes(ItemStack output)
	{
		List<VulcanizerRecipe> list = new ArrayList();
		Set<ComparableItemStack> keySet = new HashSet<ComparableItemStack>(recipeList.keySet());
		for(ComparableItemStack mold : keySet)
		{
			Iterator<VulcanizerRecipe> it = recipeList.get(mold).iterator();
			while(it.hasNext())
			{
				VulcanizerRecipe ir = it.next();
				if(OreDictionary.itemMatches(ir.output, output, true))
				{
					list.add(ir);
					it.remove();
				}
			}
		}
		return list;
	}

	public static boolean isValidMold(ItemStack itemStack)
	{
		if(itemStack.isEmpty())
			return false;
		return recipeList.containsKey(ApiUtils.createComparableItemStack(itemStack, false));
	}

	@Override
	public int getMultipleProcessTicks()
	{
		return 0;
	}

	public static HashMap<String, Function<NBTTagCompound, VulcanizerRecipe>> deserializers = new HashMap<>();

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("mold", mold.writeToNBT(new NBTTagCompound()));
		return nbt;
	}

	@Override
	public int getTotalProcessTime()
	{
		return this.totalProcessTime;
	}

	@Override
	public int getTotalProcessEnergy()
	{
		return this.totalProcessEnergy;
	}


	public static VulcanizerRecipe loadFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("type")&&deserializers.containsKey(nbt.getString("type")))
			return deserializers.get(nbt.getString("type")).apply(nbt);
		IngredientStack input = IngredientStack.readFromNBT(nbt.getCompoundTag("input"));
		ComparableItemStack mold = ComparableItemStack.readFromNBT(nbt.getCompoundTag("mold"));
		List<VulcanizerRecipe> list = recipeList.get(mold);
		for(VulcanizerRecipe recipe : list)
			if(recipe.input.equals(input))
				return recipe;
		return null;
	}
}