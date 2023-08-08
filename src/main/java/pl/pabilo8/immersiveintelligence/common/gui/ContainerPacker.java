package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPacker;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class ContainerPacker extends ContainerIEBase<TileEntityPacker>
{
	public Slot[] slots;
	public IESlot.Ghost ghostSlot;
	//added only on client side
	public Runnable ghostUpdateFunction;
	DummyInventory ghostInv = new DummyInventory();

	public ContainerPacker(EntityPlayer player, TileEntityPacker tile)
	{
		super(player.inventory, tile);

		slots = new Slot[tile.getInventory().size()];
		assert this.inv!=null;

		if(tile.hasUpgrade(IIContent.UPGRADE_PACKER_FLUID))
		{
			addSlotToContainer(new IESlot.FluidContainer(this, ghostInv, 1, 309, 35, 0));
			addSlotToContainer(new IESlot.Output(this, ghostInv, 2, 309, 71));
			this.slotCount = 2;
		}
		else if(tile.hasUpgrade(IIContent.UPGRADE_PACKER_ENERGY))
		{
			addSlotToContainer(new IESlot.FluidContainer(this, ghostInv, 1, 309, 35, 0));
			addSlotToContainer(new IESlot.Output(this, ghostInv, 2, 309, 71));
			this.slotCount = 2;
		}
		else
		{
			for(int i = 0; i < tile.getInventory().size()-1; i++)
			{
				slots[i] = this.addSlotToContainer(new Slot(this.inv, i+1, 0, 0)
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

		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(player.inventory, j+i*9+9, 89+j*18, 130+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(player.inventory, i, 89+i*18, 161+27));
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
