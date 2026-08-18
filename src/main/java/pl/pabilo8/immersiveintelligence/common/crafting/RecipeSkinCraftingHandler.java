package pl.pabilo8.immersiveintelligence.common.crafting;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISkinnable;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler.IISpecialSkin;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 18.07.2026
 * @ii-approved 0.3.1
 * @since 07.08.2021
 */
public class RecipeSkinCraftingHandler extends Impl<IRecipe> implements IRecipe
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
		private ItemStack item;
		private ItemStack manual;

		public SkinResult(InventoryCrafting inv)
		{
			this.manual = ItemStack.EMPTY;
			this.canCraft = process(inv);

			if(canCraft)
			{
				remaining = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
				remaining.set(manualStack, manual.copy());
				String last = ItemNBTHelper.getString(manual, "lastSkin");
				ItemStack op = item.copy();
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
			item = ItemStack.EMPTY;
			skinnable = null;

			for(int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack stack = inv.getStackInSlot(i);
				if(!stack.isEmpty())
					if(stack.getItem()==IEContent.itemTool&&stack.getItemDamage()==3)
						if(manual.isEmpty()&&ItemNBTHelper.hasKey(stack, "lastSkin"))
						{
							manual = stack;
							manualStack = i;
						}
						else
							return false;
					else if(stack.getItem() instanceof ISkinnable)
					{
						if(item.isEmpty())
						{
							item = stack;
							skinnable = ((ISkinnable)stack.getItem());
						}
					}
					else
						return false;
			}

			boolean result = !manual.isEmpty()&&skinnable!=null;
			if(result)
			{
				String[] info = ItemNBTHelper.getString(manual, "lastSkin").split(":");
				if(info.length==2&&IISkinHandler.isValidSkin(info[1]))
				{
					IISpecialSkin skin = IISkinHandler.getSkin(info[1]);
					assert skin!=null;
					return skin.appliesToPlayer(info[0])&&skin.doesApply(skinnable.getSkinnableName());
				}
				else
					return false;
			}

			return result;
		}
	}

}
