package pl.pabilo8.immersiveintelligence.common.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import pl.pabilo8.immersiveintelligence.api.LogisticTag;
import pl.pabilo8.immersiveintelligence.common.IIContent;

/**
 * Crafting: 1x Logistic Tag (not consumed) + 1x any item => tagged output item.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 28.01.2026
 */
public class RecipeLogisticTagCraftingHandler extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		return new TagResult(inv).canCraft;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		return new TagResult(inv).output;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		return new TagResult(inv).remaining;
	}

	@Override
	public boolean canFit(int width, int height)
	{
		return width >= 2||height >= 2;
	}

	private static class TagResult
	{
		private final boolean canCraft;
		private final NonNullList<ItemStack> remaining;
		private final ItemStack output;

		private int tagStackIndex = -1;
		private ItemStack tagStack = ItemStack.EMPTY;
		private ItemStack target = ItemStack.EMPTY;
		private LogisticTag tag = null;

		private TagResult(InventoryCrafting inv)
		{
			this.canCraft = process(inv);

			this.remaining = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
			if(canCraft)
			{
				// keep the tag item
				remaining.set(tagStackIndex, tagStack.copy());

				ItemStack out = target.copy();
				out.setCount(1);
				tag.applyToStack(out);
				this.output = out;
			}
			else
				this.output = ItemStack.EMPTY;
		}

		private boolean process(InventoryCrafting inv)
		{
			tagStackIndex = -1;
			tagStack = ItemStack.EMPTY;
			target = ItemStack.EMPTY;
			tag = null;

			for(int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack stack = inv.getStackInSlot(i);
				if(stack.isEmpty())
					continue;

				// logistic tag carrier (must contain payload)
				if(stack.getItem()==IIContent.itemLogisticTag&&LogisticTag.hasLogisticsTag(stack))
				{
					if(!tagStack.isEmpty())
						return false;
					tagStack = stack;
					tagStackIndex = i;
					tag = LogisticTag.getLogisticsTagFromStack(stack);
					if(tag==null)
						return false;
					continue;
				}

				// target item (any, but only one)
				if(!target.isEmpty())
					return false;

				// avoid tagging the tag-item itself (optional safeguard)
				if(stack.getItem()==IIContent.itemLogisticTag)
					return false;

				target = stack;
			}

			return !tagStack.isEmpty()&&!target.isEmpty()&&tag!=null;
		}
	}
}
