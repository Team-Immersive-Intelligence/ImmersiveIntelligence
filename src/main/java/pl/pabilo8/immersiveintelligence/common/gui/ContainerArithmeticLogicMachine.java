package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

import static pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArithmeticLogicMachine.*;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 03.08.2025
 * @ii-approved 0.3.1
 * @since 30.06.2019
 */
public class ContainerArithmeticLogicMachine extends ContainerIIBase<TileEntityArithmeticLogicMachine>
{
	public final boolean storage;
	public final Slot[] circuitSlots;
	public final Slot[] storageSlots;

	public ContainerArithmeticLogicMachine(EntityPlayer player, TileEntityArithmeticLogicMachine tile, int gui)
	{
		super(player, tile);

		switch(gui)
		{
			case 0: //Storage
			{
				boolean circuitUpgrade = tile.isUpgradeInstalled(IIContent.UPGRADE_CIRCUIT_RACKS);
				this.circuitSlots = addSlotArray(6+2, 26-8-2-1+(circuitUpgrade?0: 18), 0,
						circuitUpgrade?CIRCUITS_UPGRADED: CIRCUITS_BASE, 1, CircuitSlot::new);
				this.storageSlots = addSlotArray(32+4, 6+4+32-16, CIRCUITS_UPGRADED, STORAGE_SLOTS, 6, CircuitSlot::new);
				this.storage = true;
			}
			break;
			default:
				this.storage = false;
				this.circuitSlots = storageSlots = new Slot[0];
				break;
		}

		addPlayerInventory(player.inventory, 8, 141+8);
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
			return stack.getItem() instanceof ItemIIFunctionalCircuit;
		}
	}
}
