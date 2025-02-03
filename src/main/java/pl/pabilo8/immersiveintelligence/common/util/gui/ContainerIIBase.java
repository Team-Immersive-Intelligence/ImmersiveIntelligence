package pl.pabilo8.immersiveintelligence.common.util.gui;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.crafting.DataProgrammingRecipe;

/**
 * @author Pabilo8
 * @since 23.09.2023
 */
public class ContainerIIBase<T extends TileEntityIEBase & IIEInventory> extends ContainerIEBase<T>
{
	public Slot[] playerInventory = new Slot[0];

	public ContainerIIBase(EntityPlayer player, T tile)
	{
		super(player.inventory, tile);
		this.slotCount = tile.getInventory().size();
		this.tile = tile;
	}

	public Slot[] addPlayerInventory(InventoryPlayer inventoryPlayer, int x, int y)
	{
		playerInventory = new Slot[36];
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(playerInventory[j+i*9] = new Slot(inventoryPlayer, j+i*9+9, x+j*18, y+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(playerInventory[i+27] = new Slot(inventoryPlayer, i, x+i*18, y+58));
		return playerInventory;
	}

	public static class FilteredDataInput extends IESlot
	{
		public FilteredDataInput(Container container, IInventory inv, int id, int x, int y)
		{
			super(container, inv, id, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return DataProgrammingRecipe.RECIPE_LIST.stream().anyMatch(p -> p.input.matches(stack));
		}
	}


	//TODO: 11.01.2025 use instead of ContainerIEBase
}
