package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import pl.pabilo8.immersiveintelligence.common.crafting.IIRecipes;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIItem;

/**
 * @author Pabilo8
 * @since 23.09.2023
 */
public class ContainerCasingPouch extends ContainerIIItem
{

	public ContainerCasingPouch(EntityPlayer player, ItemStack heldStack, EnumHand hand)
	{
		super(player, heldStack, hand);
		ItemNBTHelper.setBoolean(heldStack, "open", true);
	}

	@Override
	public int addSlots(int i)
	{
		//Casing Slots
		for(int y = 0; y < 2; y++)
			for(int x = 0; x < 6; x++)
				this.addSlotToContainer(new IESlot.ContainerCallback(this, this.inv, i++, 26+x*18, 21+y*18));

		//Magazine Slots
		for(int x = 0; x < 6; x++)
			this.addSlotToContainer(new IESlot.ContainerCallback(this, this.inv, i++, 26+x*18, 59));

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
	public boolean canTake(ItemStack stack, int slotNumer, Slot slotObject)
	{
		return true;
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
