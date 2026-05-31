package pl.pabilo8.immersiveintelligence.common.util.gui;

import blusunrize.immersiveengineering.common.gui.IESlot.ContainerCallback;
import blusunrize.immersiveengineering.common.gui.IESlot.ICallbackContainer;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemStackHandler;

import javax.annotation.Nonnull;

/**
 * ItemStack-backed II container base.
 */
public abstract class ContainerIIItemBase extends ContainerIIBase implements ICallbackContainer
{
	protected final World world;
	protected final EntityEquipmentSlot equipmentSlot;
	protected final ItemStack heldItem;
	protected final EntityPlayer player;
	public final IItemHandler itemHandler;
	/**
	 * Kept as the short legacy field name because existing item containers usually reference {@code inv} directly.
	 */
	public final IItemHandler inv;
	public int internalSlots;
	protected int blockedSlot;

	public ContainerIIItemBase(EntityPlayer player, ItemStack heldStack, EnumHand hand)
	{
		super(player.inventory);
		this.player = player;
		this.world = player.world;
		this.equipmentSlot = hand==EnumHand.MAIN_HAND?EntityEquipmentSlot.MAINHAND: EntityEquipmentSlot.OFFHAND;
		this.heldItem = heldStack.copy();

		this.itemHandler = heldStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		this.inv = this.itemHandler;
		if(inv instanceof IIItemStackHandler)
			((IIItemStackHandler)inv).setInventoryForUpdate(inventoryPlayer);
		updateSlots();
	}

	protected void updateSlots()
	{
		if(inv==null)
			return;
		this.internalSlots = this.addSlots(0);
		this.slotCount = this.internalSlots;
		this.blockedSlot = this.inventoryPlayer.currentItem+27+internalSlots;
	}

	protected abstract int addSlots(int i);

	@Override
	protected Slot addSlot(int x, int y, int index)
	{
		return addItemSlot(x, y, index, (container, inv1, id, x1, y1) ->
				new ContainerCallback((Container)container, inv1, id, x1, y1));
	}

	@SuppressWarnings("unchecked")
	protected <SLOT extends Slot> SLOT addItemSlot(int x, int y, int index, ItemSlotConstructor<SLOT> aNew)
	{
		if(inv==null)
			throw new IllegalStateException(getClass().getSimpleName()+" has no item handler inventory");
		return (SLOT)addSlotToContainer(aNew.construct(this, this.inv, index, x, y));
	}

	protected <SLOT extends Slot> Slot[] addItemSlotArray(int x, int y, int startIndex, int totalSlots, int slotsPerRow, ItemSlotConstructor<SLOT> aNew)
	{
		return addItemSlotArray(x, y, startIndex, totalSlots, slotsPerRow, 18, aNew);
	}

	protected <SLOT extends Slot> Slot[] addItemSlotArray(int x, int y, int startIndex, int totalSlots, int slotsPerRow, int slotDistance, ItemSlotConstructor<SLOT> aNew)
	{
		java.util.ArrayList<SLOT> slots = new java.util.ArrayList<>();
		for(int i = 0; i < totalSlots; i++)
			slots.add(this.addItemSlot(x+i%slotsPerRow*slotDistance, y+i/slotsPerRow*slotDistance, i+startIndex, aNew));
		return slots.toArray(new Slot[0]);
	}

	protected <SLOT extends Slot> Slot[] addVirtualItemSlots(int startIndex, int totalSlots, ItemSlotConstructor<SLOT> aNew)
	{
		java.util.ArrayList<SLOT> slots = new java.util.ArrayList<>();
		for(int i = 0; i < totalSlots; i++)
			slots.add(this.addItemSlot(-32, -32, i+startIndex, aNew));
		return slots.toArray(new Slot[0]);
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the item inventory.
	 */
	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slot)
	{
		ItemStack oldStackInSlot = ItemStack.EMPTY;
		Slot slotObject = inventorySlots.get(slot);

		if(slotObject!=null&&slotObject.getHasStack())
		{
			ItemStack stackInSlot = slotObject.getStack();
			oldStackInSlot = stackInSlot.copy();

			if(slot < internalSlots)
			{
				if(!this.mergeItemStack(stackInSlot, internalSlots, internalSlots+36, true))
					return ItemStack.EMPTY;
			}
			else if(allowShiftclicking()&&!stackInSlot.isEmpty())
			{
				boolean untouched = true;
				for(int i = 0; i < internalSlots; i++)
				{
					Slot targetSlot = inventorySlots.get(i);
					if(targetSlot!=null&&targetSlot.isItemValid(stackInSlot))
					{
						if(!targetSlot.getStack().isEmpty()&&(!ItemStack.areItemsEqual(stackInSlot, targetSlot.getStack())||!Utils.compareItemNBT(stackInSlot, targetSlot.getStack())))
							continue;
						int space = Math.min(targetSlot.getItemStackLimit(stackInSlot), stackInSlot.getMaxStackSize());
						if(!targetSlot.getStack().isEmpty())
							space -= targetSlot.getStack().getCount();
						if(space <= 0)
							continue;
						ItemStack insert = stackInSlot;
						if(space < stackInSlot.getCount())
							insert = stackInSlot.splitStack(space);
						if(this.mergeItemStack(insert, i, i+1, true))
							untouched = false;
					}
				}
				if(untouched)
					return ItemStack.EMPTY;
			}

			if(stackInSlot.getCount()==0)
				slotObject.putStack(ItemStack.EMPTY);
			else
				slotObject.onSlotChanged();

			slotObject.inventory.markDirty();
			if(stackInSlot.getCount()==oldStackInSlot.getCount())
				return ItemStack.EMPTY;
			slotObject.onTake(player, oldStackInSlot);

			updatePlayerItem(false);
			detectAndSendChanges();
		}
		return oldStackInSlot;
	}

	protected boolean allowShiftclicking()
	{
		return true;
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer entityplayer)
	{
		return ItemStack.areItemsEqual(player.getItemStackFromSlot(equipmentSlot), heldItem);
	}

	@Override
	public boolean canInsert(ItemStack stack, int slotNumer, Slot slotObject)
	{
		return inv==null||inv.isItemValid(slotNumer, stack);
	}

	@Nonnull
	@Override
	public ItemStack slotClick(int par1, int par2, ClickType par3, EntityPlayer par4EntityPlayer)
	{
		if(par1==this.blockedSlot||(par3==ClickType.SWAP&&par2==par4EntityPlayer.inventory.currentItem))
			return ItemStack.EMPTY;
		ItemStack ret = super.slotClick(par1, par2, par3, par4EntityPlayer);
		updatePlayerItem(false);
		return ret;
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);
		if(inv instanceof IIItemStackHandler)
			((IIItemStackHandler)inv).setInventoryForUpdate(null);
		if(!world.isRemote)
			updatePlayerItem(true);
	}

	protected void updatePlayerItem(boolean closing)
	{

	}

	@FunctionalInterface
	public interface ItemSlotConstructor<SLOT extends Slot>
	{
		SLOT construct(ICallbackContainer container, IItemHandler inv, int id, int x, int y);
	}
}
