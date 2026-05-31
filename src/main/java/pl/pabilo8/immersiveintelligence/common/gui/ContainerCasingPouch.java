package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import pl.pabilo8.immersiveintelligence.common.crafting.IIRecipes;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIItemBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 23.09.2023
 */
public class ContainerCasingPouch extends ContainerIIItemBase
{
	public Slot[] slotsCasing, slotsMagazine;

	public ContainerCasingPouch(EntityPlayer player, ItemStack heldStack, EnumHand hand)
	{
		super(player, heldStack, hand);
		ItemNBTHelper.setBoolean(heldStack, "open", true);
	}

	@Override
	public int addSlots(int i)
	{
		//Casing Slots
		this.slotsCasing = addSlotArray(26, 21-5, i, 12, 6);
		//Magazine Slots
		this.slotsMagazine = addSlotArray(26, 59-5, i+12, 6, 6);
		addPlayerInventory(inventoryPlayer, 8, 92);
		return i;
	}

	@Override
	public boolean canInsert(ItemStack stack, int slotNumer, Slot slotObject)
	{
		if(slotNumer < 12)
			return IIRecipes.AMMO_CASINGS.matchesItemStackIgnoringSize(stack);
		return stack.getItem() instanceof ItemIIBulletMagazine;
	}

	@Override
	protected void updatePlayerItem(boolean closing)
	{
		boolean contains = false;
		for(int i = 0; i < 18; i++)
			if(!inv.getStackInSlot(i).isEmpty())
			{
				contains = true;
				break;
			}
		ItemNBTHelper.setBoolean(inventorySlots.get(blockedSlot).getStack(), "contains", contains);

		if(closing)
			ItemNBTHelper.remove(inventorySlots.get(blockedSlot).getStack(), "open");
		super.updatePlayerItem(closing);
	}
}
