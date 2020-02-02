package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityAmmunitionFactory;

/**
 * Created by Pabilo8 on 10-07-2019.
 */
public class ContainerAmmunitionFactory extends ContainerIEBase
{
	public ContainerAmmunitionFactory(InventoryPlayer inventoryPlayer, TileEntityAmmunitionFactory tile)
	{
		super(inventoryPlayer, tile);

		this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 0, 147, 20, 0));
		this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 1, 147, 56, 0));

		this.addSlotToContainer(new Slot(this.inv, 2, 13, 19));
		this.addSlotToContainer(new Slot(this.inv, 3, 13, 39));
		this.addSlotToContainer(new Slot(this.inv, 4, 13, 59));
		this.addSlotToContainer(new Slot(this.inv, 5, 34, 39));

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 86+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 144));
	}
}
