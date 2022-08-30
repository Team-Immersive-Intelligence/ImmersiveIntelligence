package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblocks.metal.tileentities.first.TileEntityChemicalPainter;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class ContainerChemicalPainter extends ContainerIEBase<TileEntityChemicalPainter>
{
	public ContainerChemicalPainter(InventoryPlayer inventoryPlayer, TileEntityChemicalPainter tile)
	{
		super(inventoryPlayer, tile);
		//Input/Output Slots

		this.addSlotToContainer(new Slot(this.inv, 0, 13, 13));
		this.addSlotToContainer(new IESlot.Output(this, this.inv, 1, 13, 59));

		this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 2, 137, 21, 0));
		this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 3, 137, 57, 0));

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 107+15+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 165+15));
	}
}
