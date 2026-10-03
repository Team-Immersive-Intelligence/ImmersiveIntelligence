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
 * Provides the Emplacement weapon and player inventory slots.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 27.09.2026
 * @since 16.07.2021
 */
public class ContainerEmplacement extends ContainerIITileBase<TileEntityEmplacement>
{
	private static final int WEAPON_SLOTS_X = 22;
	private static final int PLATFORM_SLOTS_Y = 24;
	private static final int BASE_SLOTS_Y = 98;
	private static final int SLOTS_PER_ROW = 8;
	private static final int MAX_PANEL_SLOT_ROWS = 3;
	private static final int SLOT_DISTANCE = 18;
	private static final int SLOT_GROUP_GAP = 0;

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

	public static ContainerEmplacement getContainerForConfigPage(EntityPlayer player, TileEntityEmplacement tile)
	{
		ContainerEmplacement container = new ContainerEmplacement(player, tile, false);
		container.addPlayerInventory(player.inventory, 8+32, 86+64+16+8+16);
		return container;
	}

	public static ContainerEmplacement getContainerForStoragePage(EntityPlayer player, TileEntityEmplacement tile)
	{
		ContainerEmplacement container = new ContainerEmplacement(player, tile, false);

		if(tile.currentWeapon!=null)
		{
			tile.currentWeapon.init(tile);
			IItemHandler platformAmmo = tile.currentWeapon.getPlatformItemHandler(true);
			IItemHandler platformCasings = tile.currentWeapon.getPlatformItemHandler(false);
			IItemHandler baseAmmo = tile.currentWeapon.getBaseItemHandler(true);

			container.slotsPlatformAmmo = container.addWeaponSlots(
					platformAmmo, PLATFORM_SLOTS_Y, true);
			container.slotsPlatformCasings = container.addWeaponSlots(
					platformCasings, container.getNextSlotGroupY(PLATFORM_SLOTS_Y,
							getSlotCount(platformAmmo), getSlotCount(platformCasings)), true);
			container.slotsBaseAmmo = container.addWeaponSlots(
					baseAmmo, BASE_SLOTS_Y, false);
			/*container.slotsBaseCasings = container.addWeaponSlots(
					baseCasings, container.getNextSlotGroupY(BASE_SLOTS_Y,
							getSlotCount(baseAmmo), getSlotCount(baseCasings)), false);*/
		}

		container.slotCount = container.slotsPlatformAmmo.length+container.slotsPlatformCasings.length
				+container.slotsBaseAmmo.length+container.slotsBaseCasings.length;
		container.addPlayerInventory(player.inventory, 8+32, 86+64+16+8);
		return container;
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot)
	{
		if(slot < 0||slot >= inventorySlots.size()||!inventorySlots.get(slot).canTakeStack(player))
			return ItemStack.EMPTY;
		return super.transferStackInSlot(player, slot);
	}

	@Override
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
	{
		//Vanilla's existing-stack merge bypasses isItemValid, so exclude the read-only prefix entirely.
		startIndex = Math.max(startIndex, slotsPlatformAmmo.length+slotsPlatformCasings.length);
		return startIndex < endIndex&&super.mergeItemStack(stack, startIndex, endIndex, reverseDirection);
	}

	private Slot[] addWeaponSlots(IItemHandler handler, int y, boolean readOnly)
	{
		if(handler==null)
			return new Slot[0];

		return addSlotArray(WEAPON_SLOTS_X, y, 0, handler.getSlots(), SLOTS_PER_ROW,
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

	private int getNextSlotGroupY(int firstGroupY, int ammoSlots, int casingSlots)
	{
		if(ammoSlots==0)
			return firstGroupY;
		int rows = shouldScrollAmmo(ammoSlots, casingSlots)?getScrollableRows(casingSlots): getSlotRows(ammoSlots);
		return firstGroupY+rows*SLOT_DISTANCE+SLOT_GROUP_GAP;
	}

	private static int getSlotCount(IItemHandler handler)
	{
		return handler==null?0: handler.getSlots();
	}

	/**
	 * @return true when ammunition slots are the group that must scroll to keep the panel within three rows
	 */
	public static boolean shouldScrollAmmo(int ammoSlots, int casingSlots)
	{
		int ammoRows = getSlotRows(ammoSlots);
		int casingRows = getSlotRows(casingSlots);
		return ammoRows+casingRows > MAX_PANEL_SLOT_ROWS&&ammoRows >= casingRows;
	}

	/**
	 * @return true when casing slots are the group that must scroll to keep the panel within three rows
	 */
	public static boolean shouldScrollCasings(int ammoSlots, int casingSlots)
	{
		return getSlotRows(ammoSlots)+getSlotRows(casingSlots) > MAX_PANEL_SLOT_ROWS
				&&!shouldScrollAmmo(ammoSlots, casingSlots);
	}

	/**
	 * @return visible rows left for a scrolling group after the other group is placed
	 */
	public static int getScrollableRows(int otherSlots)
	{
		return Math.max(1, MAX_PANEL_SLOT_ROWS-getSlotRows(otherSlots));
	}

	private static int getSlotRows(int slots)
	{
		return (slots+SLOTS_PER_ROW-1)/SLOTS_PER_ROW;
	}
}
