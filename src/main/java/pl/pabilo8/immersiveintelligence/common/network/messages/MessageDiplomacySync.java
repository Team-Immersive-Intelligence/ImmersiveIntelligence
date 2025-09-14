package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyUtils;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 12.09.2025
 */
public class MessageDiplomacySync extends IIMessage
{
	boolean updateAll;
	String identityName;
	boolean shouldRemove;
	EasyNBT tagCompound;

	public MessageDiplomacySync()
	{

	}

	public MessageDiplomacySync(boolean updateAll, String identityName, boolean shouldRemove, EasyNBT tagCompound)
	{
		this.updateAll = updateAll;
		this.identityName = identityName;
		this.shouldRemove = shouldRemove;
		this.tagCompound = tagCompound;
	}

	public static MessageDiplomacySync updateAllMessage()
	{
		return new MessageDiplomacySync(true, null, false, DiplomacyUtils.saveAllToNBT());
	}

	public static MessageDiplomacySync removeIdentityMessage(OwnerIdentity identity)
	{
		return new MessageDiplomacySync(false, identity.getDisplayName(), true, null);
	}

	public static MessageDiplomacySync updateIdentityMessage(OwnerIdentity identity)
	{
		return new MessageDiplomacySync(false, identity.getDisplayName(), false, identity.toNBT());
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
			DiplomacyUtils.loadAllFromNBT(tagCompound);
			return;
		}

		if(shouldRemove)
			DiplomacyUtils.clientRemoveIdentity(identityName);
		else
			DiplomacyUtils.clientUpdateIdentity(identityName, tagCompound);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		updateAll = buf.readBoolean();
		if(!updateAll)
		{
			identityName = readString(buf);
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
			writeString(buf, identityName);
			buf.writeBoolean(shouldRemove);
		}
		if(!shouldRemove)
			writeEasyNBT(buf, tagCompound);
	}
}
