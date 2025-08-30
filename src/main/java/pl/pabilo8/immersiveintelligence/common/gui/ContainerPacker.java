package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPacker;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 17.05.2019
 * @since 20.08.2025
 */
public class ContainerPacker extends ContainerIIBase<TileEntityPacker>
{
	public Slot[] slots, slotsfluid, slotsoutput;
	public IESlot.Ghost ghostSlot;
	//added only on client side
	public Runnable ghostUpdateFunction;
	DummyInventory ghostInv = new DummyInventory();

	public ContainerPacker(EntityPlayer player, TileEntityPacker tile)
	{
		super(player, tile);

		slots = new Slot[tile.getInventory().size()];
		assert this.inv!=null;

		if(tile.isUpgradeInstalled(IIContent.UPGRADE_PACKER_FLUID))
		{
			this.slotsfluid[0] = addSlotToContainer(new IESlot.FluidContainer(this, ghostInv, 1, 309, 35, 0));
			this.slotsoutput[0] = addSlotToContainer(new IESlot.Output(this, ghostInv, 2, 309, 71));
			this.slotCount = 2;
		}
		else if(tile.isUpgradeInstalled(IIContent.UPGRADE_PACKER_ENERGY))
		{

			this.slotsfluid[0] = addSlotToContainer(new IESlot.FluidContainer(this, ghostInv, 1, 309, 35, 0));
			this.slotsoutput[0] = addSlotToContainer(new IESlot.Output(this, ghostInv, 2, 309, 71));
			this.slotCount = 2;
		}
		else
		{
			for(int i = 0; i < tile.getInventory().size()-1; i++)
			{
				this.slots[i] = addSlotToContainer(new Slot(this.inv, i+1, 0, 0)
				{
					@Override
					public boolean isItemValid(@Nonnull ItemStack stack)
					{
						return IEApi.isAllowedInCrate(stack);
					}
				});
			}

			addSlotToContainer(ghostSlot = new IESlot.Ghost(this, ghostInv, 0, 0, 0)
			{
				@Override
				public void onSlotChanged()
				{
					super.onSlotChanged();
					if(ghostUpdateFunction!=null)
						ghostUpdateFunction.run();
				}
			});
			this.slotCount = tile.getInventory().size();
		}

		this.addPlayerInventory(player.inventory, 8+64+32-16, 86+32+12);

	}

	private static class DummyInventory implements IInventory
	{
		ItemStack stack = ItemStack.EMPTY;

		@Override
		public int getSizeInventory()
		{
			return 1;
		}

		@Override
		public boolean isEmpty()
		{
			return true;
		}

		@Nonnull
		@Override
		public ItemStack getStackInSlot(int i)
		{
			return stack;
		}

		@Nonnull
		@Override
		public ItemStack decrStackSize(int i, int i1)
		{
			return stack;
		}

		@Nonnull
		@Override
		public ItemStack removeStackFromSlot(int i)
		{
			return stack;
		}

		@Override
		public void setInventorySlotContents(int i, @Nonnull ItemStack itemStack)
		{
			stack = itemStack;
		}

		@Override
		public int getInventoryStackLimit()
		{
			return Integer.MAX_VALUE;
		}

		@Override
		public void markDirty()
		{

		}

		@Override
		public boolean isUsableByPlayer(@Nonnull EntityPlayer entityPlayer)
		{
			return true;
		}

		@Override
		public void openInventory(@Nonnull EntityPlayer entityPlayer)
		{

		}

		@Override
		public void closeInventory(@Nonnull EntityPlayer entityPlayer)
		{

		}

		@Override
		public boolean isItemValidForSlot(int i, @Nonnull ItemStack itemStack)
		{
			return true;
		}

		@Override
		public int getField(int i)
		{
			return 0;
		}

		@Override
		public void setField(int i, int i1)
		{
		}

		@Override
		public int getFieldCount()
		{
			return 0;
		}

		@Override
		public void clear()
		{
		}

		@Nonnull
		@Override
		public String getName()
		{
			return "packer_dummy";
		}

		@Override
		public boolean hasCustomName()
		{
			return false;
		}

		@Override
		public ITextComponent getDisplayName()
		{
			return new TextComponentString("Sekrit Dokuments )))");
		}
	}
}
