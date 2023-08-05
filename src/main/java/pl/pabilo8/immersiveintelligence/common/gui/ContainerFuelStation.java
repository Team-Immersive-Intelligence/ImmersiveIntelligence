package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFuelStation;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class ContainerFuelStation extends ContainerIEBase<TileEntityFuelStation>
{
	public ContainerFuelStation(EntityPlayer player, TileEntityFuelStation tile)
	{
		super(player.inventory, tile);

		//Fluid Container Slots
		this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 0, 39, 14, 0));
		this.addSlotToContainer(new IESlot.FluidContainer(this, this.inv, 1, 39, 42, 0));

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(player.inventory, j+i*9+9, 8+j*18, 86+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(player.inventory, i, 8+i*18, 144));
	}
}
