package pl.pabilo8.immersiveintelligence.common.util.gui;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * TileEntity-backed II container base.
 *
 * @param <T> tile type
 */
public class ContainerIITileBase<T extends TileEntityIEBase & IIEInventory> extends ContainerIIBase
{
	public final T tile;

	public ContainerIITileBase(EntityPlayer player, T tile)
	{
		super(player.inventory, new InventoryIIEInventory<T>(tile, tile::getDisplayName, entityPlayer ->
				tile.hasWorld()
						&&tile.getWorld().getTileEntity(tile.getPos())==tile
						&&entityPlayer.getDistanceSq(tile.getPos().getX()+0.5D, tile.getPos().getY()+0.5D, tile.getPos().getZ()+0.5D) <= 64.0D
		)
		{
			@Override
			public void markDirty()
			{
				tile.markDirty();
			}
		}, tile.getInventory().size());
		this.tile = tile;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return inv!=null&&inv.isUsableByPlayer(player);
	}

	@Override
	protected boolean isStackValid(int slot, ItemStack stack)
	{
		return tile.isStackValid(slot, stack);
	}

	@Override
	protected int getSlotLimit(int slot)
	{
		return tile.getSlotLimit(slot);
	}

	public T getTile()
	{
		return tile;
	}
}
