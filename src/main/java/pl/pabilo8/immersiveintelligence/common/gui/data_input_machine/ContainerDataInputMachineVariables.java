package pl.pabilo8.immersiveintelligence.common.gui.data_input_machine;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot.Output;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.TileEntityDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.gui.data_input_machine.ContainerDataInputMachine.FilteredPunchtape;

/**
 * Created by Pabilo8 on 30-06-2019.
 */
public class ContainerDataInputMachineVariables extends ContainerIEBase
{
	public ContainerDataInputMachineVariables(InventoryPlayer inventoryPlayer, TileEntityDataInputMachine tile)
	{
		super(inventoryPlayer, tile);

		this.addSlotToContainer(new FilteredPunchtape(this, this.inv, 0, 5, 21));
		this.addSlotToContainer(new Output(this, this.inv, 1, 5, 100));

		this.slotCount=tile.getInventory().size();
		this.tile = tile;

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 141+i*18));
		for (int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 199));
	}
}
