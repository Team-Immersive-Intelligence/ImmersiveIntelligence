package pl.pabilo8.immersiveintelligence.client.gui.deco.component.widget;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoClipboardList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.clipboard.DecoClipboardUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIClipboard;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageClipboardItemSync;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

/**
 * Compact side editor for one Engineer's Clipboard in the player's inventory.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 22.09.2026
 */
public class DecoClipboardWidget extends DecoComponentWidgetBase<DecoClipboardWidget>
{
	private final InventoryPlayer inventory;
	private final int inventorySlot;
	@Nullable
	private final EnumHand hand;
	private DecoClipboardList clipboardList;
	@Nullable
	private String displayName;
	private boolean dirty;

	public DecoClipboardWidget(InventoryPlayer inventory, int inventorySlot)
	{
		this.inventory = inventory;
		this.inventorySlot = inventorySlot;
		this.hand = null;
		withSize(144, 154);
		withBackground(DecoTextures.BG_PAPER);
		withBackgroundMask(DecoTextures.TEMPLATE_PAPER);

		ItemStack stack = inventory.getStackInSlot(inventorySlot);
		this.displayName = stack.hasDisplayName()?
				stack.getDisplayName(): null;
	}

	public DecoClipboardWidget(InventoryPlayer inventory, EnumHand hand)
	{
		this.inventory = inventory;
		this.inventorySlot = -1;
		this.hand = hand;
		withSize(144, 154);
		withBackground(DecoTextures.BG_PAPER);
		withBackgroundMask(DecoTextures.TEMPLATE_PAPER);

		ItemStack stack = inventory.player.getHeldItem(hand);
		this.displayName = stack.hasDisplayName()?stack.getDisplayName(): null;
	}

	@Override
	protected boolean initialize()
	{
		if(!super.initialize())
			return false;

		ItemStack stack = getClipboard();
		withTitleLabel("ii.gui.clipboard.title", DecoAlignment.TOP);
		clipboardList = new DecoClipboardList(4, 8, ignored -> dirty = true);
		addComponent(clipboardList
				.withSize(width-8, height-14)
				.withEntries(DecoClipboardUtils.readEntries(ItemIIClipboard.getEntries(stack))));
		return true;
	}

	private ItemStack getClipboard()
	{
		if(hand!=null)
		{
			ItemStack stack = hand==EnumHand.OFF_HAND?inventory.offHandInventory.get(0): inventory.getCurrentItem();
			return stack.getItem()==IIContent.itemClipboard?stack: ItemStack.EMPTY;
		}
		if(inventorySlot < 0||inventorySlot >= inventory.mainInventory.size())
			return ItemStack.EMPTY;
		ItemStack stack = inventory.mainInventory.get(inventorySlot);
		return stack.getItem()==IIContent.itemClipboard?stack: ItemStack.EMPTY;
	}

	@Override
	public void cleanup()
	{
		if(dirty&&clipboardList!=null)
		{
			ItemStack stack = getClipboard();
			if(!stack.isEmpty())
			{
				NBTTagList entries = DecoClipboardUtils.writeEntries(clipboardList.getEntries());
				ItemIIClipboard.setEntries(stack, entries);
				IIPacketHandler.sendToServer(hand==null?
						new MessageClipboardItemSync(inventorySlot, entries): new MessageClipboardItemSync(hand, entries));
			}
			dirty = false;
		}
		super.cleanup();
	}

	@Override
	public String getName()
	{
		return hand==null?"clipboard_"+inventorySlot: "clipboard_"+hand.name().toLowerCase(Locale.ROOT);
	}

	@Nonnull
	@Override
	public DecoTab provideTab()
	{
		return (DecoTab)new DecoTab()
				.withBackground(DecoTextures.COMPONENT_TAB_WIDGET)
				.withBackgroundColor(IIColor.fromPackedRGB(0x765338))
				.withPadding(6, 2, 2, 2)
				.withIconAlignment(DecoAlignment.CENTER)
				.withIcon(getClipboard().copy())
				.withTranslatedTooltip(displayName==null?IIReference.GUI_TOOLTIP_KEY+"widget.clipboard.show.unnamed":
						I18n.format(IIReference.GUI_TOOLTIP_KEY+"widget.clipboard.show.named", displayName));
	}
}
