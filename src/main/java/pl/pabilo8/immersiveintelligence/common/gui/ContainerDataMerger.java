package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityDataMerger;

/**
 * @author Pabilo8
 * @since 30-06-2019
 */
public class ContainerDataMerger extends ContainerIEBase
{
	public ContainerDataMerger(InventoryPlayer inventoryPlayer, TileEntityDataMerger tile)
	{
		super(inventoryPlayer, tile);

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 141+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 199));
	}

}
