package pl.pabilo8.immersiveintelligence.common.gui.arithmetic_logic_machine;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.gui.arithmetic_logic_machine.ContainerArithmeticLogicMachineStorage.CircuitSlot;

/**
 * Created by Pabilo8 on 30-06-2019.
 */
public class ContainerArithmeticLogicMachineVariables extends ContainerIEBase
{
	public ContainerArithmeticLogicMachineVariables(InventoryPlayer inventoryPlayer, TileEntityArithmeticLogicMachine tile, int page)
	{
		super(inventoryPlayer, tile);

		this.addSlotToContainer(new CircuitSlot(this, this.inv, page, 5, 61));

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 141+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 199));
	}
}
