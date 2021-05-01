package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.ListUtils;
import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.bullets.IBullet;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Pabilo8
 * @since 14-04-2020
 */
public class CasingFillerRecipe extends MultiblockRecipe
{
	public final IngredientStack itemInput;
	public final ItemStack itemOutput;

	public static ArrayList<CasingFillerRecipe> recipeList = new ArrayList();
	int gunpowder;
	int totalProcessTime;
	int totalProcessEnergy;
	IBullet bullet = null;


	public CasingFillerRecipe(ItemStack itemOutput, Object itemInput, int gunpowder, int time, int energy)
	{
		this.itemOutput = itemOutput;
		this.itemInput = ApiUtils.createIngredientStack(itemInput);

		this.inputList = Lists.newArrayList(this.itemInput);
		this.outputList = ListUtils.fromItem(this.itemOutput);

		this.gunpowder = gunpowder;
		this.totalProcessTime = time;
		this.totalProcessEnergy = energy;
	}

	public static CasingFillerRecipe addRecipe(ItemStack itemOutput, IngredientStack itemInput, int gunpowder, int time, int energy)
	{
		CasingFillerRecipe r = new CasingFillerRecipe(itemOutput, itemInput, gunpowder, time, energy);
		recipeList.add(r);
		return r;
	}

	public static <T extends Item & IBullet> CasingFillerRecipe addRecipe(T bulletItem, int time, int energy)
	{
		ItemStack casingStack = bulletItem.getCasingStack(1);
		ItemNBTHelper.setBoolean(casingStack, "ii_FilledCasing", true);
		CasingFillerRecipe recipe = addRecipe(casingStack, new IngredientStack(bulletItem.getCasingStack(1)).setUseNBT(true), bulletItem.getGunpowderNeeded(), time, energy);
		recipe.bullet = bulletItem;
		return recipe;
	}

	public static List<CasingFillerRecipe> removeRecipesForOutput(ItemStack stack)
	{
		List<CasingFillerRecipe> list = new ArrayList();
		Iterator<CasingFillerRecipe> it = recipeList.iterator();
		while(it.hasNext())
		{
			CasingFillerRecipe ir = it.next();
			if(OreDictionary.itemMatches(ir.itemOutput, stack, true))
			{
				list.add(ir);
				it.remove();
			}
		}
		return list;
	}

	public static CasingFillerRecipe findRecipe(IngredientStack item_input)
	{
		for(CasingFillerRecipe recipe : recipeList)
		{
			if(recipe.itemInput.matches(item_input))
			{
				return recipe;
			}
		}
		return null;
	}

	public static CasingFillerRecipe findRecipe(ItemStack item_input)
	{
		for(CasingFillerRecipe recipe : recipeList)
		{
			if(recipe.itemInput.matches(item_input))
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

	public static CasingFillerRecipe loadFromNBT(NBTTagCompound nbt)
	{
		return findRecipe(IngredientStack.readFromNBT(nbt.getCompoundTag("item_input")));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("item_input", itemInput.writeToNBT(new NBTTagCompound()));
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

	public int getTotalGunpowder()
	{
		return this.gunpowder;
	}

	@Nullable
	public IBullet getBullet()
	{
		return this.bullet;
	}
}