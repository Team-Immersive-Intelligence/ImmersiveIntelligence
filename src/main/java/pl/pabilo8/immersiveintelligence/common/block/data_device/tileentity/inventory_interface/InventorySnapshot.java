package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.inventory_interface;

import lombok.Getter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * Stores an immutable copy of an inventory.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 27.10.2026
 */
public final class InventorySnapshot
{
	private final ItemStack[] stacks;
	private final NBTTagCompound[] stackTags;
	private final int[] handlerLimits;
	private final int[] slotLimits;
	/**
	 * Returns the item count, limited to the largest int.
	 */
	@Getter
	private final int totalItems;
	@Getter
	private final int occupiedSlots;
	@Getter
	private final int comparatorOutput;
	/**
	 * Returns the mean slot fill from 0 to 100, including all exposed slots.
	 */
	@Getter
	private final float fillPercent;
	@Getter
	private final boolean full;
	private final int itemCount;

	private InventorySnapshot(IItemHandler handler)
	{
		int slots = Math.max(0, handler.getSlots());
		this.stacks = new ItemStack[slots];
		this.stackTags = new NBTTagCompound[slots];
		this.handlerLimits = new int[slots];
		this.slotLimits = new int[slots];
		int items = 0, occupied = 0;
		double filledSlots = 0;
		boolean allFull = slots > 0;
		for(int slot = 0; slot < slots; slot++)
		{
			ItemStack source = handler.getStackInSlot(slot);
			ItemStack stack = source.copy();
			this.stacks[slot] = stack;
			this.handlerLimits[slot] = handler.getSlotLimit(slot);
			int limit = Math.max(0, handlerLimits[slot]);
			if(!stack.isEmpty())
				limit = MathHelper.clamp(stack.getMaxStackSize(), 0, limit);
			this.slotLimits[slot] = limit;
			if(!stack.isEmpty())
			{
				occupied++;
				items += stack.getCount();
				filledSlots += limit==0?1: Math.min(1, stack.getCount()/(double)limit);
				if(stack.getCount() < limit)
					allFull = false;
			}
			else
				allFull = false;
		}
		this.itemCount = items;
		this.totalItems = MathHelper.clamp((int)items, 0, Integer.MAX_VALUE);
		this.occupiedSlots = occupied;
		this.full = allFull;

		double fill = slots==0?0: filledSlots/slots;
		fillPercent = (float)Math.min(100, fill*100);
		comparatorOutput = occupied==0?0: Math.min(15, (int)Math.floor(fill*14)+1);
	}

	/**
	 * Returns a copy of the handler state, or null if there is no handler.
	 */
	@Nullable
	public static InventorySnapshot capture(@Nullable IItemHandler handler)
	{
		return handler==null?null: new InventorySnapshot(handler);
	}

	public int getTotalSlots()
	{
		return stacks.length;
	}

	public int getEmptySlots()
	{
		return stacks.length-occupiedSlots;
	}

	/**
	 * Counts matching items. An empty filter matches all items. Count is limited to the largest int.
	 */
	public int getItemCount(@Nullable ItemStack filter, boolean matchNBT)
	{
		return MathHelper.clamp(countItems(filter, matchNBT), 0, Integer.MAX_VALUE);
	}

	public int getStackCount(int slot)
	{
		return hasSlot(slot)?stacks[slot].getCount(): 0;
	}

	/**
	 * Returns a stack copy, or an empty stack if the slot is outside the inventory.
	 */
	public ItemStack getStack(int slot)
	{
		return hasSlot(slot)?stacks[slot].copy(): ItemStack.EMPTY;
	}

	/**
	 * Returns the slot capacity, limited by the current item maximum, or zero for an invalid slot.
	 */
	public int getSlotLimit(int slot)
	{
		return hasSlot(slot)?slotLimits[slot]: 0;
	}

	public boolean isEmpty()
	{
		return occupiedSlots==0;
	}

	/**
	 * Compares slot count, items, full item NBT, counts, and slot capacities.
	 */
	public boolean sameContents(@Nullable InventorySnapshot other)
	{
		if(other==null||stacks.length!=other.stacks.length)
			return false;
		for(int slot = 0; slot < stacks.length; slot++)
			if(!sameSlot(other, slot))
				return false;
		return true;
	}

	/**
	 * Compares one slot. Two absent slots match; a present slot does not match an absent slot.
	 */
	public boolean sameSlot(@Nullable InventorySnapshot other, int slot)
	{
		if(other==null)
			return false;
		boolean present = hasSlot(slot);
		if(present!=other.hasSlot(slot))
			return false;
		if(!present)
			return true;
		ItemStack first = stacks[slot];
		ItemStack second = other.stacks[slot];
		return handlerLimits[slot]==other.handlerLimits[slot]
				&&slotLimits[slot]==other.slotLimits[slot]
				&&first.getItem()==second.getItem()
				&&first.getMetadata()==second.getMetadata()
				&&first.getCount()==second.getCount()
				&&getStackTag(slot).equals(other.getStackTag(slot));
	}

	int countItems(@Nullable ItemStack filter, boolean matchNBT)
	{
		if(filter==null||filter.isEmpty())
			return itemCount;
		int count = 0;
		for(ItemStack stack : stacks)
			if(matches(stack, filter, matchNBT))
				count += stack.getCount();
		return count;
	}

	static boolean matches(ItemStack stack, @Nullable ItemStack filter, boolean matchNBT)
	{
		return !stack.isEmpty()&&(filter==null||filter.isEmpty()
				||(stack.isItemEqual(filter)&&(!matchNBT||ItemStack.areItemStackTagsEqual(stack, filter))));
	}

	//Compare full item and capability NBT only when a rule needs content changes.
	private NBTTagCompound getStackTag(int slot)
	{
		if(stackTags[slot]==null)
			stackTags[slot] = stacks[slot].writeToNBT(new NBTTagCompound());
		return stackTags[slot];
	}

	private boolean hasSlot(int slot)
	{
		return slot >= 0&&slot < stacks.length;
	}
}
