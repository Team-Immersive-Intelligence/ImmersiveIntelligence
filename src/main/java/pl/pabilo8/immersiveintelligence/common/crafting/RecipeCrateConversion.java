package pl.pabilo8.immersiveintelligence.common.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8
 * @since 23-04-2020
 */
public class RecipeCrateConversion extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
	List<ItemStack> inputCrates;

	public RecipeCrateConversion(ItemStack... inputCrates)
	{
		this.inputCrates = Arrays.asList(inputCrates);
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stackInSlot = inv.getStackInSlot(i);
			if(!stackInSlot.isEmpty())
				return inputCrates.stream().anyMatch(stack -> stack.isItemEqual(stackInSlot));
		}
		return false;
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack crate = ItemStack.EMPTY;

		inventory_loop:
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stackInSlot = inv.getStackInSlot(i);
			if(!stackInSlot.isEmpty())
				for(ItemStack inputCrate : inputCrates)
					if(inputCrate.isItemEqual(stackInSlot))
					{
						crate = stackInSlot;
						break inventory_loop;
					}
		}

		if(!crate.isEmpty())
		{
			for(int i = 0; i < inputCrates.size(); i += 1)
			{
				if(inputCrates.get(i).isItemEqual(crate))
				{
					int j = i+1;
					if(j > inputCrates.size()-1)
						j = 0;
					ItemStack output = inputCrates.get(j).copy();
					output.setTagCompound(crate.getTagCompound());
					return output;
				}
			}
		}

		return ItemStack.EMPTY;
	}

	/**
	 * Used to determine if this recipe can fit in a grid of the given width/height
	 */
	@Override
	public boolean canFit(int width, int height)
	{
		return width >= 1&&height >= 1;
	}

	/**
	 * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
	 * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
	 */
	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}

}