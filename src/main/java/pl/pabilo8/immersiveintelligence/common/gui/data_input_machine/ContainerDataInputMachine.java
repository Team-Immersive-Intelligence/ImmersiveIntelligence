package pl.pabilo8.immersiveintelligence.common.gui.data_input_machine;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.gui.IESlot.Output;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.TileEntityDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIPunchtape;

/**
 * Created by Pabilo8 on 30-06-2019.
 */
public class ContainerDataInputMachine extends ContainerIEBase
{
	public ContainerDataInputMachine(InventoryPlayer inventoryPlayer, TileEntityDataInputMachine tile)
	{
		super(inventoryPlayer, tile);

		this.addSlotToContainer(new FilteredPunchtape(this, this.inv, 0, 5, 21));
		this.addSlotToContainer(new Output(this, this.inv, 1, 5, 100));

		for(int i=2; i<26; i++)
			this.addSlotToContainer(new FilteredPunchtape(this, this.inv, i, 30+((i-2)%8)*18, 20+((i-2)/8)*18));

		this.slotCount=0;
		this.tile = tile;

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 141+i*18));
		for (int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 199));
	}

	public static class FilteredPunchtape extends IESlot
	{
		public FilteredPunchtape(Container container, IInventory inv, int id, int x, int y)
		{
			super(container, inv, id, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return stack.getItem() instanceof ItemIIPunchtape || Utils.compareToOreName(stack,"punchtapeEmpty");
		}
	}

	public static class FilteredEmptyPunchtape extends IESlot
	{
		public FilteredEmptyPunchtape(Container container, IInventory inv, int id, int x, int y)
		{
			super(container, inv, id, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return Utils.compareToOreName(stack,"punchtapeEmpty");
		}
	}
}
