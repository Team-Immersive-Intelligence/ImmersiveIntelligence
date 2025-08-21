package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 16.07.2021
 */
public class ContainerEmplacement extends ContainerIIBase<TileEntityEmplacement>
{

	public Slot[] slotsEmplacement;

	public ContainerEmplacement(EntityPlayer player, TileEntityEmplacement tile)
	{
		super(player, tile);

		this.slotCount = 0;

		this.slotsEmplacement = addPlayerInventory(player.inventory, 8, 86);

	}

	public static class ContainerEmplacementStorage extends ContainerIIBase<TileEntityEmplacement>
	{

		public Slot slotsEmplacementStorage;

		public ContainerEmplacementStorage(EntityPlayer player, TileEntityEmplacement tile)
		{
			super(player, tile);
			this.tile = tile;
			if(tile.currentWeapon!=null)
			{
				final IItemHandler handler = tile.currentWeapon.getItemHandler(true);


				for(int i = 0; i < tile.getInventory().size(); i++)
					addSlotToContainer(
							handler==null?
									this.slotsEmplacementStorage = addSlotToContainer(new Slot(this.inv, i, 8+((i%9)*18), 32+((int)Math.floor(i/(float)9)*18))):
									new FilteredEmplacementSlot(this.inv, handler, i, 8+((i%9)*18), 32+((int)Math.floor(i/(float)9)*18))

					);
			}

			this.addPlayerInventory(player.inventory, 8, 86);

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
