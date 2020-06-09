package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.rotary.IMotorGear;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySkyCartStation;

/**
 * Created by Pabilo8 on 2019-05-17.
 */
public class ContainerSkycartStation extends ContainerIEBase
{
	public ContainerSkycartStation(InventoryPlayer inventoryPlayer, TileEntitySkyCartStation tile)
	{
		super(inventoryPlayer, tile);
		for(int i = 0; i < tile.getInventory().size(); i++)
			this.addSlotToContainer(new Slot(this.inv, i, 52+(i%9)*18, 29+(i/9)*18)
			{
				@Override
				public int getSlotStackLimit()
				{
					return 1;
				}

				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return stack.getItem() instanceof IMotorGear;
				}
			});
		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 87+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 145));
	}
}