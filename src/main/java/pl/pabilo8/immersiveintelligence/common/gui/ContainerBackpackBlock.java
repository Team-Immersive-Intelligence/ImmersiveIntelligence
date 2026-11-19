package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.backpack.TileEntityStorageBackpack;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

import javax.annotation.Nonnull;

/**
 * Provides access to the inventory of a placed storage backpack.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 18.11.2026
 */
public class ContainerBackpackBlock extends ContainerIITileBase<TileEntityStorageBackpack>
{
	public final Slot[] backpackSlots;

	public ContainerBackpackBlock(EntityPlayer player, TileEntityStorageBackpack tile)
	{
		super(player, tile);
		this.backpackSlots = addSlotArray(8, 23-8, 0, slotCount, 9);
		addPlayerInventory(player.inventory, 8, 83-4);
	}

	@Nonnull
	@Override
	public ItemStack slotClick(int slot, int button, ClickType type, EntityPlayer player)
	{
		return canInteractWith(player)?super.slotClick(slot, button, type, player): ItemStack.EMPTY;
	}
}
