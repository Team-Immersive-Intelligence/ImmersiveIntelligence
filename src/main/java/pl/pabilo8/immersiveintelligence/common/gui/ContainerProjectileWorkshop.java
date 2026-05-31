package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @since 10.07.2019
 * @since 08.13.2025
 */
public class ContainerProjectileWorkshop extends ContainerIITileBase<TileEntityProjectileWorkshop>
{
	public Slot inputSlot, outputSlot;

	public ContainerProjectileWorkshop(EntityPlayer player, TileEntityProjectileWorkshop tile)
	{
		super(player, tile);


		if(tile.isUpgradeInstalled(IIContent.UPGRADE_CORE_FILLER))
		{
			inputSlot = this.addSlotToContainer(new Slot(this.inv, 0, 8, 46)
			{
				@Override
				public boolean isItemValid(ItemStack itemStack)
				{
					return tile.isStackValid(0, itemStack);
				}
			});
			outputSlot = this.addSlotToContainer(new Slot(this.inv, 1, 48, 20)
			{
				@Override
				public boolean isItemValid(ItemStack itemStack)
				{
					return tile.isStackValid(1, itemStack);
				}
			});
		}
		else
			inputSlot = this.addSlotToContainer(new Slot(this.inv, 0, 8, 8+2)
			{
				@Override
				public boolean isItemValid(ItemStack itemStack)
				{
					return tile.isStackValid(0, itemStack);
				}
			});

		addPlayerInventory(player.inventory, 8+32+8+4, 141+8);
	}
}
