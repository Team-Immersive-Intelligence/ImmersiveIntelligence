package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityProjectileWorkshop;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class ContainerProjectileWorkshop extends ContainerIEBase<TileEntityProjectileWorkshop>
{
	public ContainerProjectileWorkshop(EntityPlayer player, TileEntityProjectileWorkshop tile)
	{
		super(player.inventory, tile);
		//Input/Output Slots

		if(tile.hasUpgrade(IIContent.UPGRADE_CORE_FILLER))
		{
			this.addSlotToContainer(new IESlot(this, this.inv, 0, 8, 14+32)
			{
				@Override
				public boolean isItemValid(ItemStack itemStack)
				{
					return tile.isStackValid(0, itemStack);
				}
			});
			this.addSlotToContainer(new IESlot(this, this.inv, 1, 8+48-8, 20)
			{
				@Override
				public boolean isItemValid(ItemStack itemStack)
				{
					return tile.isStackValid(1, itemStack);
				}
			});
		}
		else
			this.addSlotToContainer(new IESlot(this, this.inv, 0, 8, 14+20+6)
			{
				@Override
				public boolean isItemValid(ItemStack itemStack)
				{
					return tile.isStackValid(0, itemStack);
				}
			});

		this.slotCount = tile.getInventory().size();
		this.tile = tile;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(player.inventory, j+i*9+9, 30+j*18, 95+i*18));
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(player.inventory, i, 30+i*18, 165-27+15));
	}
}
