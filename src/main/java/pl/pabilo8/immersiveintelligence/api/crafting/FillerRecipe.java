package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.ListUtils;
import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.IIMultiblockRecipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Pabilo8
 * @since 14-04-2020
 */
public class FillerRecipe extends IIMultiblockRecipe
{
	public final IngredientStack itemInput;
	public final ItemStack itemOutput;

	public static ArrayList<FillerRecipe> recipeList = new ArrayList<>();
	public DustStack dust;
	//for bullets only
	IAmmoTypeItem bullet = null;

	public FillerRecipe(ItemStack itemOutput, Object itemInput, DustStack dust, int time, int energy)
	{
		this.itemOutput = itemOutput;
		this.itemInput = ApiUtils.createIngredientStack(itemInput);

		this.inputList = Lists.newArrayList(this.itemInput);
		this.outputList = ListUtils.fromItem(this.itemOutput);

		if(itemOutput.getItem() instanceof IAmmoTypeItem)
			bullet = ((IAmmoTypeItem)itemOutput.getItem());

		this.dust = dust;
		this.totalProcessTime = time;
		this.totalProcessEnergy = energy;
	}

	public static FillerRecipe addRecipe(ItemStack itemOutput, IngredientStack itemInput, DustStack dust, int time, int energy)
	{
		FillerRecipe r = new FillerRecipe(itemOutput, itemInput, dust, time, energy);
		recipeList.add(r);
		return r;
	}

	public static <T extends Item & IAmmoTypeItem> FillerRecipe addRecipe(T bulletItem, int time, int energy)
	{
		ItemStack casingStack = bulletItem.getCasingStack(1);
		ItemNBTHelper.setBoolean(casingStack, "ii_FilledCasing", true);
		FillerRecipe recipe = addRecipe(casingStack, new IngredientStack(bulletItem.getCasingStack(1)).setUseNBT(true), new DustStack("gunpowder", bulletItem.getPropellantNeeded()), time, energy);
		recipe.bullet = bulletItem;
		return recipe;
	}

	public static List<FillerRecipe> removeRecipesForOutput(ItemStack stack)
	{
		List<FillerRecipe> list = new ArrayList<>();
		Iterator<FillerRecipe> it = recipeList.iterator();
		while(it.hasNext())
		{
			FillerRecipe ir = it.next();
			if(OreDictionary.itemMatches(ir.itemOutput, stack, true))
			{
				list.add(ir);
				it.remove();
			}
		}
		return list;
	}

	public static FillerRecipe findRecipe(IngredientStack item_input, DustStack stack)
	{
		for(FillerRecipe recipe : recipeList)
			if(recipe.itemInput.matches(item_input)&&recipe.dust.canMergeWith(stack)&&recipe.dust.amount <= stack.amount)
				return recipe;
		return null;
	}

	public static FillerRecipe findRecipe(ItemStack item_input, DustStack stack)
	{
		for(FillerRecipe recipe : recipeList)
			if(recipe.itemInput.matches(item_input)&&recipe.dust.canMergeWith(stack)&&recipe.dust.amount <= stack.amount)
				return recipe;
		return null;
	}

	@Override
	public int getMultipleProcessTicks()
	{
		return 0;
	}


	public static FillerRecipe loadFromNBT(NBTTagCompound nbt)
	{
		return findRecipe(IngredientStack.readFromNBT(nbt.getCompoundTag("item_input")), new DustStack(nbt.getCompoundTag("dust")));
	}

	@Override
	public EasyNBT writeToNBT()
	{
		return EasyNBT.newNBT()
				.withIngredientStack("item_input", itemInput)
				.withSerializable("dust", dust);
	}

	public DustStack getDust()
	{
		return this.dust;
	}

	@Nullable
	public IAmmoTypeItem getBullet()
	{
		return this.bullet;
	}

}