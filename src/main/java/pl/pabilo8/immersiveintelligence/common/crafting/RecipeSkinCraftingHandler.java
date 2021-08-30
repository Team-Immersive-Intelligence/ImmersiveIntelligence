package pl.pabilo8.immersiveintelligence.common.crafting;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import flaxbeard.immersivepetroleum.common.items.ItemProjector;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIContent;

/**
 * @author Pabilo8
 * @since 07.08.2021
 */
public class RecipeSkinCraftingHandler extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		return new SkinResult(inv).canCraft;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		return new SkinResult(inv).output;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		return new SkinResult(inv).remaining;
	}

	private class SkinResult
	{
		private final boolean canCraft;
		private final NonNullList<ItemStack> remaining;
		private final ItemStack output;

		private ItemStack manual;
		int manualStack = 0;

		public SkinResult(InventoryCrafting inv)
		{
			this.manual = ItemStack.EMPTY;
			this.canCraft = process(inv);
			if (canCraft)
			{
				remaining = NonNullList.withSize(9, ItemStack.EMPTY);
				remaining.set(manualStack, manual.copy());
				String last = ItemNBTHelper.getString(manual, "lastSkin");


				ItemStack op = new ItemStack(IIContent.itemMachinegun, 1, 0);
				ItemNBTHelper.setString(op, "contributorSkin", last);
				ItemProjector.setFlipped(op, true);
				output = op;
			}
			else
			{
				remaining = NonNullList.withSize(9, ItemStack.EMPTY);
				output = ItemStack.EMPTY;
			}
		}

		private boolean process(InventoryCrafting inv)
		{
			boolean hasPaper = false;
			for (int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack stack = inv.getStackInSlot(i);
				if (!stack.isEmpty())
				{
					if (stack.getItem() == IEContent.itemTool && stack.getItemDamage() == 3)
					{
						if (manual.isEmpty() && ItemNBTHelper.hasKey(stack, "lastSkin"))
						{
							manual = stack;
							manualStack = i;
						}
						else
							return false;
					}
					else if (stack.getItem() == IIContent.itemMachinegun)
					{
						if (!hasPaper)
							hasPaper = true;
						else
							return false;
					}
					else
						return false;

				}
			}
			return !manual.isEmpty() && hasPaper;
		}
	}

	@Override
	public boolean canFit(int width, int height)
	{
		return width >= 2 && height >= 2;
	}

}
