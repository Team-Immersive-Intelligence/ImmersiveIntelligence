package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIItemBase;

/**
 * Inventory bridge for the item form of the Engineer's Clipboard.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.09.2026
 */
public class ContainerClipboardItem extends ContainerIIItemBase
{
	public ContainerClipboardItem(EntityPlayer player, ItemStack heldStack, EnumHand hand)
	{
		super(player, heldStack, hand);
	}

	@Override
	protected int addSlots(int i)
	{
		addPlayerInventory(inventoryPlayer, 8, 216-8);
		return i;
	}
}
