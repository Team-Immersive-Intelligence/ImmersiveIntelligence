package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityChemicalBath;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class ContainerChemicalBath extends ContainerIEBase<TileEntityChemicalBath>
{
	public ContainerChemicalBath(EntityPlayer player, TileEntityChemicalBath tile)
	{
		super(player.inventory, tile);
		//Input/Output Slots

		this.addSlotToContainer(new Slot(this.inv, 0, 10, 39));
		this.addSlotToContainer(new IESlot.Output(this, this.inv, 1, 140, 39));

		//Fluid Container Slots

		this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 2, 44, 28, 0));
		this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 3, 106, 28, 0));

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(player.inventory, j+i*9+9, 8+j*18, 86+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(player.inventory, i, 8+i*18, 144));
	}
}
