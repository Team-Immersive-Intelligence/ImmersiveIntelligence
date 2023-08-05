package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityRepairCrate;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class ContainerRepairCrate extends ContainerIEBase<TileEntityRepairCrate>
{
	public ContainerRepairCrate(EntityPlayer player, TileEntityRepairCrate tile)
	{
		//Normal bullet slots

		super(player.inventory, tile);
		int shift = tile.hasUpgrade(IIContent.UPGRADE_INSERTER)?0:27;
		for(int i = 0; i < tile.getInventory().size(); i++)
			this.addSlotToContainer(new Slot(this.inv, i, shift+16+(i%4)*21, 7+(i/4)*18)
			{
				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return Utils.compareToOreName(stack, "plateSteel");
				}
			});

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(player.inventory, j+i*9+9, 8+j*18, 87+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(player.inventory, i, 8+i*18, 145));
	}
}
