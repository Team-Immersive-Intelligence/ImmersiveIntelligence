package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler.PlayerInfo;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 26.04.2026
 * @since 12.09.2025
 */
public class MessageDiplomacySync extends IIMessage
{
	private static final List<MessageChunkedData> chunkBuffer = new ArrayList<>();
	private boolean updateAll = false;
	private UUID identity = null;
	private boolean shouldRemove = false;
	private EasyNBT tagCompound = null;
	private PlayerInfo playerInfo = null;
	private MessageChunkedData messageChunk = null;

	public MessageDiplomacySync()
	{

	}

	private MessageDiplomacySync(MessageChunkedData messageChunk)
	{
		this.messageChunk = messageChunk;
		this.updateAll = true;
	}

	private MessageDiplomacySync(PlayerInfo playerInfo)
	{
		this.playerInfo = playerInfo;
	}

	private MessageDiplomacySync(UUID identity, boolean shouldRemove, EasyNBT tagCompound)
	{
		this.identity = identity;
		this.shouldRemove = shouldRemove;
		this.tagCompound = tagCompound;
	}

	public static MessageDiplomacySync[] updateAllMessage()
	{
		DiplomacyHandler handler = DiplomacyHandler.getInstance(false);
		return chunkNBT(handler.saveAllToNBT().unwrap()).stream()
				.map(MessageDiplomacySync::new)
				.toArray(MessageDiplomacySync[]::new);
	}

	public static MessageDiplomacySync removeIdentityMessage(OwnerIdentity identity)
	{
		return new MessageDiplomacySync(identity.getUUID(), true, null);
	}

	public static MessageDiplomacySync updateIdentityMessage(OwnerIdentity identity)
	{
		return new MessageDiplomacySync(identity.getUUID(), false, EasyNBT.wrapNBT(identity.serializeNBT()));
	}

	public static MessageDiplomacySync syncPlayerInfo(PlayerInfo playerInfo)
	{
		return new MessageDiplomacySync(playerInfo);
	}

	public static MessageDiplomacySync requestUpdateMessage()
	{
		//placeholder, because the server replies to any message
		return new MessageDiplomacySync(DiplomacyHandler.DEFAULT_PLAYER_INFO);
	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
		//Request an update
		for(MessageDiplomacySync message : updateAllMessage())
			IIPacketHandler.sendToClient(handler.player, message);
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		if(playerInfo!=null)
		{
			DiplomacyHandler.getInstance(false).updatePlayerInfo(playerInfo);
			return;
		}
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
		//Update player
		if(buf.readBoolean())
		{
			playerInfo = new PlayerInfo(readTagCompound(buf));
			return;
		}
		//Update all
		if(this.updateAll = buf.readBoolean())
		{
			synchronized(chunkBuffer)
			{
				NBTTagCompound nbt = readChunkedNBT(buf, chunkBuffer);
				tagCompound = nbt==null?null: EasyNBT.wrapNBT(nbt);
			}
		}
		//Update single identity
		else
		{
			identity = readUUID(buf);
			shouldRemove = buf.readBoolean();
			if(!shouldRemove)
				tagCompound = readEasyNBT(buf);
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(playerInfo!=null);
		if(playerInfo!=null)
		{
			writeTagCompound(buf, playerInfo.serializeNBT());
			return;
		}

		buf.writeBoolean(updateAll);
		if(updateAll)
		{
			writeChunkedData(buf, messageChunk);
			return;
		}
		writeUUID(buf, identity);
		buf.writeBoolean(shouldRemove);

		if(!shouldRemove)
			writeEasyNBT(buf, tagCompound);
	}
}
