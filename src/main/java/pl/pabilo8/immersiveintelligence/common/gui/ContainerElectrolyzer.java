package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityElectrolyzer;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class ContainerElectrolyzer extends ContainerIEBase<TileEntityElectrolyzer>
{
	public ContainerElectrolyzer(InventoryPlayer inventoryPlayer, TileEntityElectrolyzer tile)
	{
		super(inventoryPlayer, tile);

		this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 0, 10, 26, 0));
		this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 1, 10, 54, 0));
		this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 2, 69, 23, 0));
		this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 3, 69, 55, 0));
		this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 4, 140, 23, 0));
		this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 5, 140, 55, 0));

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 86+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 144));
	}
}
