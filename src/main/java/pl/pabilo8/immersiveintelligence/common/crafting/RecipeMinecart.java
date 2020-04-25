package pl.pabilo8.immersiveintelligence.common.crafting;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

/**
 * Created by Pabilo8 on 23-04-2020.
 */
public class RecipeMinecart extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
	ItemStack outputMinecart = ItemStack.EMPTY;
	ItemStack inputBlock = null;

	public RecipeMinecart(ItemStack outputMinecart, ItemStack inputBlock)
	{
		this.outputMinecart = outputMinecart;
		this.inputBlock = inputBlock;
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		ItemStack minecart = ItemStack.EMPTY;
		ItemStack crate = ItemStack.EMPTY;

		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stackInSlot = inv.getStackInSlot(i);
			if(!stackInSlot.isEmpty())
				if(minecart.isEmpty()&&stackInSlot.getItem()==Items.MINECART)
				{
					minecart = stackInSlot;
				}
				else if(crate.isEmpty()&&stackInSlot.isItemEqual(inputBlock))
				{
					crate = stackInSlot;
				}
				else
					return false;
		}
		ImmersiveIntelligence.logger.info(!minecart.isEmpty()&&!crate.isEmpty());
		return !minecart.isEmpty()&&!crate.isEmpty();
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack crate = ItemStack.EMPTY;
		ItemStack minecart = ItemStack.EMPTY;
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stackInSlot = inv.getStackInSlot(i);
			if(!stackInSlot.isEmpty())
				if(minecart.isEmpty()&&stackInSlot.getItem()==Items.MINECART)
				{
					minecart = stackInSlot;
				}
				else if(crate.isEmpty()&&stackInSlot.isItemEqual(inputBlock))
				{
					crate = stackInSlot;
				}
		}

		if(!minecart.isEmpty()&&!crate.isEmpty())
		{
			ItemStack output = outputMinecart.copy();
			output.setTagCompound(ItemNBTHelper.getTag(crate).copy());
			return output;
		}

		return ItemStack.EMPTY;
	}

	/**
	 * Used to determine if this recipe can fit in a grid of the given width/height
	 */
	@Override
	public boolean canFit(int width, int height)
	{
		return width >= 2&&height >= 2;
	}

	/**
	 * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
	 * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
	 */
	@Override
	public ItemStack getRecipeOutput()
	{
		return outputMinecart.copy();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}

}