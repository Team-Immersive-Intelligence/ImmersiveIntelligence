package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyUtils;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomaticAction;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.IOwnableProperty;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;

import java.util.UUID;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 12.09.2025
 */
public class MessageDiplomacyAction extends IIMessage
{
	private DiplomaticAction action;
	private IOwnableProperty property;

	public MessageDiplomacyAction(DiplomaticAction action, IOwnableProperty property)
	{
		this.action = action;
		this.property = property;
	}

	public MessageDiplomacyAction()
	{

	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
		EntityPlayerMP sender = handler.player;
		//Get or create the identity of the sender
		OwnerIdentity ownerIdentity = DiplomacyUtils.getOwnerIdentityForEntity(sender);
		//Check if the identity is valid
		if(ownerIdentity.isInvalid())
			return;

		//Perform action based on parameters
		switch(action)
		{
			case CLAIM:
			case START_SEIZING:
				DiplomacyUtils.claimProperty(ownerIdentity, property);
				break;
			case MERGE:
				//DiplomacyUtils.merge(ownerIdentity, targetIdentity);
				break;
			case ADD_MEMBER:
				//DiplomacyUtils.attemptAddMember(ownerIdentity, actor, target);
				break;
			case REMOVE_MEMBER:
				//DiplomacyUtils.attemptRemoveMember(ownerIdentity, actor, target);
				break;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		//No action
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		//this.actor = DiplomacyUtils.getIdentityByName(this.readString(buf));
		this.action = this.readEnum(buf, DiplomaticAction.class);
		switch(action)
		{
			case CLAIM:
			case START_SEIZING:
				property = DiplomacyUtils.getPropertyByUUID(UUID.fromString(readString(buf)));
				break;
			case MERGE:
				break;
			case ADD_MEMBER:
			case REMOVE_MEMBER:
				break;
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		//this.writeString(buf, this.actor.getDisplayName());
		this.writeEnum(buf, this.action);
		switch(action)
		{
			case CLAIM:
			case START_SEIZING:
				writeString(buf, property.getUUID().toString());
				break;
			case MERGE:
				break;
			case ADD_MEMBER:
			case REMOVE_MEMBER:
				break;
		}
	}
}
