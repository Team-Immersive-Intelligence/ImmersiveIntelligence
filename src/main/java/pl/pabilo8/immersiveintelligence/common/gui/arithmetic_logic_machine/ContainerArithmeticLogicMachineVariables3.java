package pl.pabilo8.immersiveintelligence.common.gui.arithmetic_logic_machine;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.gui.arithmetic_logic_machine.ContainerArithmeticLogicMachineStorage.CircuitSlot;

/**
 * Created by Pabilo8 on 30-06-2019.
 */
public class ContainerArithmeticLogicMachineVariables3 extends ContainerIEBase
{
	public ContainerArithmeticLogicMachineVariables3(InventoryPlayer inventoryPlayer, TileEntityArithmeticLogicMachine tile)
	{
		super(inventoryPlayer, tile);

		this.addSlotToContainer(new CircuitSlot(this, this.inv, 3, 5, 61));

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 141+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 199));
	}
}
