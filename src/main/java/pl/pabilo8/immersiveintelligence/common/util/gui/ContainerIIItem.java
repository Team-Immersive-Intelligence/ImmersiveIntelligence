package pl.pabilo8.immersiveintelligence.common.util.gui;

import blusunrize.immersiveengineering.common.gui.IESlot.ICallbackContainer;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
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
 * @author Pabilo8
 * @since 23.09.2023
 */
public abstract class ContainerIIItem extends Container implements ICallbackContainer
{
	protected final InventoryPlayer inventoryPlayer;
	protected final World world;
	public IItemHandler inv;

	protected int blockedSlot;
	protected final EntityEquipmentSlot equipmentSlot;
	protected final ItemStack heldItem;
	protected final EntityPlayer player;
	public int internalSlots;

	public ContainerIIItem(EntityPlayer player, ItemStack heldStack, EnumHand hand)
	{
		this.player = player;
		this.inventoryPlayer = player.inventory;
		this.world = player.world;
		this.equipmentSlot = hand==EnumHand.MAIN_HAND?EntityEquipmentSlot.MAINHAND: EntityEquipmentSlot.OFFHAND;
		this.heldItem = heldStack.copy();

		this.inv = heldStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(inv instanceof IIItemStackHandler)
			((IIItemStackHandler)inv).setInventoryForUpdate(inventoryPlayer);
		updateSlots();
	}

	protected void updateSlots()
	{
		if(inv==null)
			return;
		this.internalSlots = this.addSlots(0);
		this.blockedSlot = (this.inventoryPlayer.currentItem+27+internalSlots);
	}

	protected abstract int addSlots(int i);

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
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
				if(!this.mergeItemStack(stackInSlot, internalSlots, (internalSlots+36), true))
					return ItemStack.EMPTY;
			}
			else if(allowShiftclicking()&&!stackInSlot.isEmpty())
			{
				boolean b = true;
				for(int i = 0; i < internalSlots; i++)
				{
					Slot s = inventorySlots.get(i);
					if(s!=null&&s.isItemValid(stackInSlot))
					{
						if(!s.getStack().isEmpty()&&(!ItemStack.areItemsEqual(stackInSlot, s.getStack())||!Utils.compareItemNBT(stackInSlot, s.getStack())))
							continue;
						int space = Math.min(s.getItemStackLimit(stackInSlot), stackInSlot.getMaxStackSize());
						if(!s.getStack().isEmpty())
							space -= s.getStack().getCount();
						if(space <= 0)
							continue;
						ItemStack insert = stackInSlot;
						if(space < stackInSlot.getCount())
							insert = stackInSlot.splitStack(space);
						if(this.mergeItemStack(insert, i, i+1, true))
						{
							b = false;
						}
					}
				}
				if(b)
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

	/**
	 * Determines whether supplied player can use this container
	 */
	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer entityplayer)
	{
		return ItemStack.areItemsEqual(player.getItemStackFromSlot(equipmentSlot), heldItem);
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

	/**
	 * Called when the container is closed.
	 */
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

	protected void addPlayerInventory(InventoryPlayer inventoryPlayer, int x, int y)
	{
		//Inventory
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, x+j*18, y+i*18));

		//Hotbar
		for(int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(inventoryPlayer, i, x+i*18, y+58));
	}
}
