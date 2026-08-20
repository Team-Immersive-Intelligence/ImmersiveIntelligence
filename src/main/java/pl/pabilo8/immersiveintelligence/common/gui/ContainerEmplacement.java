package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 18.08.2026
 * @since 16.07.2021
 */
public class ContainerEmplacement extends ContainerIITileBase<TileEntityEmplacement>
{
	public Slot[] slotsBaseAmmo = new Slot[0], slotsBaseCasings = new Slot[0];
	public Slot[] slotsPlatformAmmo = new Slot[0], slotsPlatformCasings = new Slot[0];

	public ContainerEmplacement(EntityPlayer player, TileEntityEmplacement tile)
	{
		this(player, tile, true);
	}

	private ContainerEmplacement(EntityPlayer player, TileEntityEmplacement tile, boolean addPlayerSlots)
	{
		super(player, tile);
		this.slotCount = 0;
		if(addPlayerSlots)
			addPlayerInventory(player.inventory, 8+32, 86+64+16+8);
	}

	public static ContainerEmplacement getContainerForStoragePage(EntityPlayer player, TileEntityEmplacement tile)
	{
		ContainerEmplacement container = new ContainerEmplacement(player, tile, false);

		if(tile.currentWeapon!=null)
		{
			tile.currentWeapon.init(tile);

			container.slotsPlatformAmmo = container.addWeaponSlots(tile.currentWeapon.getPlatformItemHandler(true), 26, true);
			container.slotsPlatformCasings = container.addWeaponSlots(tile.currentWeapon.getPlatformItemHandler(false), 46, true);
			container.slotsBaseAmmo = container.addWeaponSlots(tile.currentWeapon.getBaseItemHandler(true), 98, false);
			container.slotsBaseCasings = container.addWeaponSlots(tile.currentWeapon.getBaseItemHandler(false), 118, false);
		}

		container.slotCount = container.slotsPlatformAmmo.length+container.slotsPlatformCasings.length
				+container.slotsBaseAmmo.length+container.slotsBaseCasings.length;
		container.addPlayerInventory(player.inventory, 8+32, 86+64+16+8);
		return container;
	}

	private Slot[] addWeaponSlots(IItemHandler handler, int y, boolean readOnly)
	{
		if(handler==null)
			return new Slot[0];

		return addSlotArray(8+4, y, 0, handler.getSlots(), 10,
				(container, inventory, id, x, slotY) -> new SlotItemHandler(handler, id, x, slotY)
				{
					@Override
					public boolean canTakeStack(EntityPlayer playerIn)
					{
						return !readOnly&&super.canTakeStack(playerIn);
					}

					@Override
					public boolean isItemValid(@Nonnull ItemStack stack)
					{
						return !readOnly&&super.isItemValid(stack);
					}
				});
	}
}
