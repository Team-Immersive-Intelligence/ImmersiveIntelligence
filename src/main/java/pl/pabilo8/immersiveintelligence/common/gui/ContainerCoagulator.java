package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityCoagulator;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class ContainerCoagulator extends ContainerIEBase<TileEntityCoagulator>
{
	public ContainerCoagulator(InventoryPlayer inventoryPlayer, TileEntityCoagulator tile)
	{
		super(inventoryPlayer, tile);
		//Input/Output Slots

		this.addSlotToContainer(new Slot(this.inv, 0, 13, 13));

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 107+15+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 165+15));
	}
}
