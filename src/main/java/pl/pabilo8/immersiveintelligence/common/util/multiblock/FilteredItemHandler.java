package pl.pabilo8.immersiveintelligence.common.util.multiblock;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 03.01.2026
 */
public class FilteredItemHandler extends ItemStackHandler
{
	private Predicate<ItemStack> filter = null;

	public FilteredItemHandler(NonNullList<ItemStack> stacks)
	{
		super(stacks);
	}

	public FilteredItemHandler withFilter(Predicate<ItemStack> filter)
	{
		this.filter = filter;
		return this;
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack)
	{
		if(filter!=null&&filter.test(stack))
			return false;
		return super.isItemValid(slot, stack);
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
	{
		if(!isItemValid(slot, stack))
			return stack;
		return super.insertItem(slot, stack, simulate);
	}
}
