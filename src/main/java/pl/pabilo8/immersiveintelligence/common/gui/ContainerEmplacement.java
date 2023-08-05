package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityEmplacement;

/**
 * @author Pabilo8
 * @since 16.07.2021
 */
public class ContainerEmplacement extends ContainerIEBase<TileEntityEmplacement>
{
	public ContainerEmplacement(EntityPlayer player, TileEntityEmplacement tile)
	{
		super(player.inventory, tile);

		this.slotCount = 0;
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(player.inventory, j+i*9+9, 8+32+j*18, 141+16+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(player.inventory, i, 8+32+i*18, 199+16));
	}

	public static class ContainerEmplacementStorage extends ContainerIEBase<TileEntityEmplacement>
	{
		public ContainerEmplacementStorage(EntityPlayer player, TileEntityEmplacement tile)
		{
			super(player.inventory, tile);
			this.tile = tile;
			if(tile.currentWeapon!=null)
			{
				final IItemHandler handler = tile.currentWeapon.getItemHandler(true);


				for(int i = 0; i < tile.getInventory().size(); i++)
					addSlotToContainer(
							handler==null?
									new Slot(this.inv, i, 8+((i%9)*18), 32+((int)Math.floor(i/(float)9)*18)):
									new FilteredEmplacementSlot(this.inv, handler, i, 8+((i%9)*18), 32+((int)Math.floor(i/(float)9)*18))

					);
			}
			this.slotCount = tile.getInventory().size();

			for(int i = 0; i < 3; i++)
				for(int j = 0; j < 9; j++)
					addSlotToContainer(new Slot(player.inventory, j+i*9+9, 8+32+j*18, 141+16+i*18));
			for(int i = 0; i < 9; i++)
				addSlotToContainer(new Slot(player.inventory, i, 8+32+i*18, 199+16));
		}
	}

	public static class FilteredEmplacementSlot extends Slot
	{
		final IItemHandler handler;

		public FilteredEmplacementSlot(IInventory inventoryIn, IItemHandler handler, int index, int xPosition, int yPosition)
		{
			super(inventoryIn, index, xPosition, yPosition);
			this.handler = handler;
		}

		@Override
		public boolean isItemValid(ItemStack itemStack)
		{
			return handler.isItemValid(getSlotIndex(), itemStack);
		}
	}
}