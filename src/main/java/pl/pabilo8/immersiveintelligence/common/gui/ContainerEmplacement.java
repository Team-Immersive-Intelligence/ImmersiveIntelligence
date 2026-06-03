package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 02.06.2026
 * @since 16.07.2021
 */
public class ContainerEmplacement extends ContainerIITileBase<TileEntityEmplacement>
{
	public Slot[] slotsBaseInventory = new Slot[0];
	public Slot[] slotsPlatformInventory = new Slot[0];

	public ContainerEmplacement(EntityPlayer player, TileEntityEmplacement tile)
	{
		super(player, tile);
		addPlayerInventory(player.inventory, 8+32, 86+64+16+8);
	}

	public static ContainerEmplacement getContainerForStoragePage(EntityPlayer player, TileEntityEmplacement tile)
	{
		ContainerEmplacement container = new ContainerEmplacement(player, tile);

		if(tile.currentWeapon!=null)
		{
			//Ensure initialization
			tile.currentWeapon.init(tile);

			//Get inventory handlers
			IEInventoryHandler platformHandler = tile.currentWeapon.getPlatformItemHandler();
			IEInventoryHandler baseHandler = tile.currentWeapon.getBaseItemHandler();

			//Add slots
			if(platformHandler!=null)
				container.slotsPlatformInventory = container.addSlotArray(8, 24, 0, platformHandler.getSlots(), 9,
						(container1, inv1, id, x, y) -> new SlotItemHandler(platformHandler, id, x, y)
						{
							@Override
							public boolean canTakeStack(EntityPlayer playerIn)
							{
								return false;
							}

							@Override
							public boolean isItemValid(@Nonnull ItemStack stack)
							{
								return false;
							}
						});
			if(baseHandler!=null)
				container.slotsBaseInventory = container.addSlotArray(8, 80+16, 0, baseHandler.getSlots(), 9,
						(container1, inv1, id, x, y) -> new SlotItemHandler(baseHandler, id, x, y));
		}
		return container;
	}
}
