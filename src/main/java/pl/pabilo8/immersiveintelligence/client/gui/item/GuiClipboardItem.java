package pl.pabilo8.immersiveintelligence.client.gui.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoItemGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoClipboardList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiCategory;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.SlotStyle;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.clipboard.ClipboardEntry;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.clipboard.DecoClipboardUtils;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerClipboardItem;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIClipboard;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageClipboardItemSync;

/**
 * DecoGUI editor for clipboard entries stored on the held item.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.09.2026
 */
@DecoTemplate(name = "clipboard_item", category = DecoGuiCategory.CLIPBOARD)
public class GuiClipboardItem extends DecoItemGui<ContainerClipboardItem>
{
	private DecoClipboardList clipboardList;

	public GuiClipboardItem(EntityPlayer player, ItemStack heldStack, EnumHand hand)
	{
		super(player, heldStack, hand, IIGUI.CLIPBOARD_ITEM);
	}

	@Override
	public void onInit()
	{
		startBackground()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 0, 176, 196)
				.withTitleBar(itemStack)
				.withNextLayer()
				.withBox(DecoTextures.BG_PAPER, DecoTextures.TEMPLATE_PAPER, 4, 4, 160+8, 172+8+8)

				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 196, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventoryTitleBar()
				.build();

		addComponent((clipboardList = new DecoClipboardList(6, 6, ignored -> {
				}))
						.withSize(144+8+8+3, 156+8+16+8-2)
						.withEntries(DecoClipboardUtils.readEntries(ItemIIClipboard.getEntries(itemStack)))
						.withDropAction(this::entryFromCarriedStack)
		);
	}

	private ClipboardEntry entryFromCarriedStack()
	{
		ItemStack carried = playerContainer.getItemStack();
		return carried.isEmpty()?null: DecoClipboardUtils.createEntry(carried.copy());
	}

	@Override
	protected void onGuiClosedWithoutTransition()
	{
		NBTTagList entries = DecoClipboardUtils.writeEntries(clipboardList.getEntries());
		ItemIIClipboard.setEntries(itemStack, entries);
		IIPacketHandler.sendToServer(new MessageClipboardItemSync(hand, entries));
	}
}
