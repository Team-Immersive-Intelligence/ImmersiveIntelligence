package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 16.07.2021
 */
public class ContainerEmplacement extends ContainerIITileBase<TileEntityEmplacement>
{
	public Slot[] slotsEmplacementStorage;

	public ContainerEmplacement(EntityPlayer player, TileEntityEmplacement tile)
	{
		super(player, tile);
		addPlayerInventory(player.inventory, 8+32, 86+64+16+8);
	}

	public static ContainerEmplacement getContainerForStoragePage(EntityPlayer player, TileEntityEmplacement tile)
	{
		ContainerEmplacement container = new ContainerEmplacement(player, tile);
		if(tile.currentWeapon!=null)
		{
			final IItemHandler handler = tile.currentWeapon.getBaseItemHandler();
			container.slotsEmplacementStorage = new Slot[0];
			/*this.slotsEmplacementStorage = addSlotArray(8,32,  0, handler.getSlots(), 9,
					(container, inv1, id, x, y) -> new FilteredEmplacementSlot());*/
		}
		return container;
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
