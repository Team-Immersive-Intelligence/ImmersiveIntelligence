package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityPackerOld;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class ContainerPacker extends ContainerIEBase
{
	public ContainerPacker(InventoryPlayer inventoryPlayer, TileEntityPackerOld tile)
	{
		super(inventoryPlayer, tile);
		for(int i = 0; i < tile.getInventory().size()-1; i++)
			this.addSlotToContainer(new Slot(this.inv, i+1, 8+(i%8)*18, 24+(i/8)*18)
			{
				/**
				 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
				 */
				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return IEApi.isAllowedInCrate(stack);
				}
			});
		this.slotCount = tile.getInventory().size()-1;
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 103+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 161));
	}
}
