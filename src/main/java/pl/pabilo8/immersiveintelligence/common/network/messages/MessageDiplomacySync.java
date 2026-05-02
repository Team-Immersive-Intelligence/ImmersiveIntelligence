package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.UUID;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 26.04.2026
 * @since 12.09.2025
 */
public class MessageDiplomacySync extends IIMessage
{
	boolean updateAll;
	UUID identity;
	boolean shouldRemove;
	EasyNBT tagCompound;

	public MessageDiplomacySync()
	{

	}

	private MessageDiplomacySync(boolean updateAll, UUID identity, boolean shouldRemove, EasyNBT tagCompound)
	{
		this.updateAll = updateAll;
		this.identity = identity;
		this.shouldRemove = shouldRemove;
		this.tagCompound = tagCompound;
	}

	public static MessageDiplomacySync updateAllMessage()
	{
		return new MessageDiplomacySync(true, null, false, DiplomacyHandler.getInstance(false).saveAllToNBT());
	}

	public static MessageDiplomacySync removeIdentityMessage(OwnerIdentity identity)
	{
		return new MessageDiplomacySync(false, identity.getUUID(), true, null);
	}

	public static MessageDiplomacySync updateIdentityMessage(OwnerIdentity identity)
	{
		return new MessageDiplomacySync(false, identity.getUUID(), false, identity.toNBT());
	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
		//No action on server
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		if(updateAll)
		{
			DiplomacyHandler.getInstance(true).loadAllFromNBT(tagCompound);
			return;
		}

		if(shouldRemove)
			DiplomacyHandler.getInstance(true).removeIdentity(identity);
		else
			DiplomacyHandler.getInstance(true).updateIdentity(identity, tagCompound);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		updateAll = buf.readBoolean();
		if(!updateAll)
		{
			identity = readUUID(buf);
			shouldRemove = buf.readBoolean();
		}
		if(!shouldRemove)
			tagCompound = readEasyNBT(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(updateAll);
		if(!updateAll)
		{
			writeUUID(buf, identity);
			buf.writeBoolean(shouldRemove);
		}
		if(!shouldRemove)
			writeEasyNBT(buf, tagCompound);
	}
}
