package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityRepairCrate;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class ContainerRepairCrate extends ContainerIEBase
{
	public ContainerRepairCrate(InventoryPlayer inventoryPlayer, TileEntityRepairCrate tile)
	{
		//Normal bullet slots

		super(inventoryPlayer, tile);
		for(int i = 0; i < tile.getInventory().size(); i++)
			this.addSlotToContainer(new Slot(this.inv, i, 8+(i%5)*18, 18+(i/5)*18)
			{
				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return true;
				}
			});

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 141+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 199));
	}
}
