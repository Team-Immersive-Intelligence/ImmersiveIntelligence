package pl.pabilo8.immersiveintelligence.common.crafting;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ISkinnable;

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

	@Override
	public boolean canFit(int width, int height)
	{
		return width >= 2||height >= 2;
	}

	private static class SkinResult
	{
		private final boolean canCraft;
		private final NonNullList<ItemStack> remaining;
		private final ItemStack output;
		int manualStack = 0;
		private ISkinnable skinnable;
		private ItemStack gun;
		private ItemStack manual;

		public SkinResult(InventoryCrafting inv)
		{
			this.manual = ItemStack.EMPTY;
			this.canCraft = process(inv);
			if(canCraft)
			{
				// TODO: 14.01.2022 check if skin matches
				remaining = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
				remaining.set(manualStack, manual.copy());
				String last = ItemNBTHelper.getString(manual, "lastSkin");
				ItemStack op = gun.copy();
				skinnable.applySkinnableSkin(op, last);
				output = op;
			}
			else
			{
				remaining = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
				output = ItemStack.EMPTY;
			}
		}

		private boolean process(InventoryCrafting inv)
		{
			manual = ItemStack.EMPTY;
			gun = ItemStack.EMPTY;
			skinnable = null;

			for(int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack stack = inv.getStackInSlot(i);
				if(!stack.isEmpty())
				{
					if(stack.getItem()==IEContent.itemTool&&stack.getItemDamage()==3)
					{
						if(manual.isEmpty()&&ItemNBTHelper.hasKey(stack, "lastSkin"))
						{
							manual = stack;
							manualStack = i;
						}
						else
							return false;
					}
					else if(stack.getItem() instanceof ISkinnable)
					{
						if(gun.isEmpty())
						{
							gun = stack;
							skinnable = ((ISkinnable)stack.getItem());
						}
					}
					else
						return false;

				}
			}
			return !manual.isEmpty()&&skinnable!=null;
		}
	}

}
