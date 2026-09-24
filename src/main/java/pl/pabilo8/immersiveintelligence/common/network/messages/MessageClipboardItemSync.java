package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.WorldServer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIClipboard;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;

/**
 * Validates and synchronises clipboard item entries from its client GUI.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.09.2026
 */
public class MessageClipboardItemSync extends IIMessage
{
	private EnumHand hand;
	private int inventorySlot = -1;
	private boolean inventoryTarget;
	private NBTTagList entries;

	public MessageClipboardItemSync(EnumHand hand, NBTTagList entries)
	{
		this.hand = hand;
		this.entries = ItemIIClipboard.sanitiseEntries(entries);
	}

	public MessageClipboardItemSync(int inventorySlot, NBTTagList entries)
	{
		this.inventoryTarget = true;
		this.inventorySlot = inventorySlot;
		this.entries = ItemIIClipboard.sanitiseEntries(entries);
	}

	public MessageClipboardItemSync()
	{
	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
		ItemStack clipboard;
		if(inventoryTarget)
		{
			if(inventorySlot < 0||inventorySlot >= handler.player.inventory.mainInventory.size())
				return;
			clipboard = handler.player.inventory.mainInventory.get(inventorySlot);
		}
		else
		{
			if(hand==null)
				return;
			clipboard = handler.player.getHeldItem(hand);
		}
		if(clipboard.getItem()!=IIContent.itemClipboard)
			return;
		ItemIIClipboard.setEntries(clipboard, entries);
		handler.player.inventoryContainer.detectAndSendChanges();
	}

	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		inventoryTarget = buf.readBoolean();
		if(inventoryTarget)
			inventorySlot = buf.readInt();
		else
			hand = readEnum(buf, EnumHand.class);
		NBTTagCompound nbt = readTagCompound(buf);
		entries = ItemIIClipboard.sanitiseEntries(nbt.getTagList(ItemIIClipboard.NBT_ENTRIES, 10));
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(inventoryTarget);
		if(inventoryTarget)
			buf.writeInt(inventorySlot);
		else
			writeEnum(buf, hand);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag(ItemIIClipboard.NBT_ENTRIES, entries);
		writeTagCompound(buf, nbt);
	}
}
