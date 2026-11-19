package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIItemBase;

import javax.annotation.Nonnull;

/**
 * Provides access to the contents of a held backpack.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.11.2026
 */
public class ContainerBackpack extends ContainerIIItemBase
{
	private final ItemStack backpack;
	public Slot[] backpackSlots;

	public ContainerBackpack(EntityPlayer player, ItemStack stack, EntityEquipmentSlot slot)
	{
		super(player, stack, slot);
		backpack = stack;
	}

	@Override
	protected int addSlots(int first)
	{
		backpackSlots = addSlotArray(8, 23-8, first, inv.getSlots(), 9);
		addPlayerInventory(inventoryPlayer, 8, 83-4);
		return backpackSlots.length;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return player.getItemStackFromSlot(equipmentSlot)==backpack;
	}

	@Nonnull
	@Override
	public ItemStack slotClick(int slot, int button, ClickType type, EntityPlayer player)
	{
		if(!canInteractWith(player)||type==ClickType.SWAP&&button==40)
			return ItemStack.EMPTY;
		return super.slotClick(slot, button, type, player);
	}
}
