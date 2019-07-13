package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.api.tool.BulletHandler;
import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.items.ItemBullet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityAmmunitionCrate;

/**
 * Created by Pabilo8 on 2019-05-17.
 */
public class ContainerAmmunitionCrate extends ContainerIEBase
{
	public ContainerAmmunitionCrate(InventoryPlayer inventoryPlayer, TileEntityAmmunitionCrate tile)
	{
		//Normal bullet slots

		super(inventoryPlayer, tile);
		for(int i = 0; i < 20; i++)
			this.addSlotToContainer(new Slot(this.inv, i, 8+(i%5)*18, 18+(i/5)*18)
			{
				/**
				 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
				 */
				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return stack.getItem() instanceof ItemBullet&&!(stack.isItemEqual(BulletHandler.emptyCasing)||stack.isItemEqual(BulletHandler.emptyShell));
				}
			});

		//Empty shell slots

		for(int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(this.inv, 20+i, 8+(i*18), 108)
			{
				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return false;
				}
			});

		//Revolver Layout Slots

		this.addSlotToContainer(new GhostFilteredBullet(this, this.inv, 29, 125, 17));
		this.addSlotToContainer(new GhostFilteredBullet(this, this.inv, 30, 144, 26));
		this.addSlotToContainer(new GhostFilteredBullet(this, this.inv, 31, 152, 44));
		this.addSlotToContainer(new GhostFilteredBullet(this, this.inv, 32, 144, 64));
		this.addSlotToContainer(new GhostFilteredBullet(this, this.inv, 33, 125, 72));
		this.addSlotToContainer(new GhostFilteredBullet(this, this.inv, 34, 106, 64));
		this.addSlotToContainer(new GhostFilteredBullet(this, this.inv, 35, 98, 45));
		this.addSlotToContainer(new GhostFilteredBullet(this, this.inv, 36, 106, 26));


		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 141+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 199));
	}

	public static class GhostFilteredBullet extends IESlot.Ghost
	{
		public GhostFilteredBullet(Container container, IInventory inv, int id, int x, int y)
		{
			super(container, inv, id, x, y);
		}

		/**
		 * Helper method to put a stack in the slot.
		 */
		@Override
		public void putStack(ItemStack itemStack)
		{
			super.putStack(itemStack);
		}

		@Override
		public boolean canTakeStack(EntityPlayer player)
		{
			return false;
		}

		@Override
		public int getSlotStackLimit()
		{
			return 1;
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return stack.getItem() instanceof ItemBullet&&!(stack.isItemEqual(BulletHandler.emptyCasing)||stack.isItemEqual(BulletHandler.emptyShell));
		}
	}
}
