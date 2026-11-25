package pl.pabilo8.immersiveintelligence.client.gui.deco.component.widget;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
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
	private final EntityEquipmentSlot slot;
	private DecoClipboardList clipboardList;
	@Nullable
	private String displayName;
	private boolean dirty;

	public DecoClipboardWidget(InventoryPlayer inventory, int inventorySlot)
	{
		this.inventory = inventory;
		this.inventorySlot = inventorySlot;
		this.slot = null;
		withSize(144, 154);
		withBackground(DecoTextures.BG_PAPER);
		withBackgroundMask(DecoTextures.TEMPLATE_PAPER);

		ItemStack stack = inventory.getStackInSlot(inventorySlot);
		this.displayName = stack.hasDisplayName()?
				stack.getDisplayName(): null;
	}

	public DecoClipboardWidget(InventoryPlayer inventory, EntityEquipmentSlot slot)
	{
		this.inventory = inventory;
		this.inventorySlot = -1;
		this.slot = slot;
		withSize(144, 154);
		withBackground(DecoTextures.BG_PAPER);
		withBackgroundMask(DecoTextures.TEMPLATE_PAPER);

		ItemStack stack = inventory.player.getItemStackFromSlot(slot);
		this.displayName = stack.hasDisplayName()?stack.getDisplayName(): null;
	}

	@Override
	protected boolean initialize()
	{
		if(!super.initialize())
			return false;

		ItemStack stack = getClipboard();
		withTitleLabel("ii.gui.clipboard.title", DecoAlignment.TOP);
		clipboardList = new DecoClipboardList(4, 8, ignored -> dirty = true)
				.withPositionSupplier(() -> inventory.player.getPositionVector());
		addComponent(clipboardList
				.withSize(width-8, height-14)
				.withEntries(DecoClipboardUtils.readEntries(ItemIIClipboard.getEntries(stack))));
		return true;
	}

	private ItemStack getClipboard()
	{
		if(slot!=null)
		{
			ItemStack stack = slot==EntityEquipmentSlot.OFFHAND?inventory.offHandInventory.get(0): inventory.getCurrentItem();
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
				IIPacketHandler.sendToServer(slot==null?
						new MessageClipboardItemSync(inventorySlot, entries): new MessageClipboardItemSync(slot, entries));
			}
			dirty = false;
		}
		super.cleanup();
	}

	@Override
	public String getName()
	{
		return slot==null?"clipboard_"+inventorySlot: "clipboard_"+slot.getName().toLowerCase();
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
