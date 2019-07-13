package pl.pabilo8.immersiveintelligence.common.gui.arithmetic_logic_machine;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.items.ItemFunctionalCircuit;

/**
 * Created by Pabilo8 on 30-06-2019.
 */
public class ContainerArithmeticLogicMachineStorage extends ContainerIEBase
{
	public ContainerArithmeticLogicMachineStorage(InventoryPlayer inventoryPlayer, TileEntityArithmeticLogicMachine tile)
	{
		super(inventoryPlayer, tile);

		this.addSlotToContainer(new CircuitSlot(this, this.inv, 0, 5, 25));
		this.addSlotToContainer(new CircuitSlot(this, this.inv, 1, 5, 52));
		this.addSlotToContainer(new CircuitSlot(this, this.inv, 2, 5, 78));
		this.addSlotToContainer(new CircuitSlot(this, this.inv, 3, 5, 104));

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 141+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 199));
	}

	public static class CircuitSlot extends IESlot
	{
		public CircuitSlot(Container container, IInventory inv, int id, int x, int y)
		{
			super(container, inv, id, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return stack.getItem() instanceof ItemFunctionalCircuit;
		}
	}
}
