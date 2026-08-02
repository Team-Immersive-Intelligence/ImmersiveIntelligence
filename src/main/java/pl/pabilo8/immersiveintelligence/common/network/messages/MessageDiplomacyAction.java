package pl.pabilo8.immersiveintelligence.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.LawForm;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.agreement.term.DiplomaticAgreement;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.DiplomaticAction;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionCategory;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionRole;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.IOwnableProperty;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

/**
 * Sent from client to server to request a diplomatic action.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 27.04.2026
 * @since 12.09.2025
 */
public class MessageDiplomacyAction extends IIMessage
{
	private DiplomaticAction action;
	private UUID propertyUUID;
	private UUID targetFaction;
	private UUID targetPlayer;
	private EasyNBT agreementData;
	private String newDisplayName;
	private LawForm newLawForm;
	private IIColor newColor;
	private ItemStack newBannerStack;
	private String targetRole;
	private PermissionCategory permissionCategory;
	private boolean permissionSetting;

	public MessageDiplomacyAction()
	{
	}

	//--- Client‑side factory methods ---

	@SideOnly(Side.CLIENT)
	public static MessageDiplomacyAction claimProperty(@Nonnull IOwnableProperty property)
	{
		MessageDiplomacyAction msg = new MessageDiplomacyAction();
		msg.action = DiplomaticAction.CLAIM;
		msg.propertyUUID = property.getUUID();
		return msg;
	}

	@SideOnly(Side.CLIENT)
	public static MessageDiplomacyAction startSeizing(@Nonnull IOwnableProperty property)
	{
		MessageDiplomacyAction msg = new MessageDiplomacyAction();
		msg.action = DiplomaticAction.START_SEIZING;
		msg.propertyUUID = property.getUUID();
		return msg;
	}

	@SideOnly(Side.CLIENT)
	public static MessageDiplomacyAction invitePlayer(@Nonnull UUID playerUUID)
	{
		MessageDiplomacyAction msg = new MessageDiplomacyAction();
		msg.action = DiplomaticAction.ADD_MEMBER;
		msg.targetPlayer = playerUUID;
		return msg;
	}

	@SideOnly(Side.CLIENT)
	public static MessageDiplomacyAction removeMember(@Nonnull UUID playerUUID)
	{
		MessageDiplomacyAction msg = new MessageDiplomacyAction();
		msg.action = DiplomaticAction.REMOVE_MEMBER;
		msg.targetPlayer = playerUUID;
		return msg;
	}

	@SideOnly(Side.CLIENT)
	public static MessageDiplomacyAction cancelInvitation(@Nonnull UUID playerUUID)
	{
		MessageDiplomacyAction msg = new MessageDiplomacyAction();
		msg.action = DiplomaticAction.CANCEL_INVITATION;
		msg.targetPlayer = playerUUID;
		return msg;
	}

	@SideOnly(Side.CLIENT)
	public static MessageDiplomacyAction changeMemberRole(@Nonnull UUID playerUUID, @Nonnull PermissionRole role)
	{
		MessageDiplomacyAction msg = new MessageDiplomacyAction();
		msg.action = DiplomaticAction.CHANGE_MEMBER_ROLE;
		msg.targetPlayer = playerUUID;
		msg.targetRole = role.getId();
		return msg;
	}

	@SideOnly(Side.CLIENT)
	public static MessageDiplomacyAction acceptInvitation(@Nonnull UUID factionUUID)
	{
		MessageDiplomacyAction msg = new MessageDiplomacyAction();
		msg.action = DiplomaticAction.ACCEPT_INVITATION;
		msg.targetFaction = factionUUID;
		return msg;
	}

	@SideOnly(Side.CLIENT)
	public static MessageDiplomacyAction denyInvitation(@Nonnull UUID factionUUID)
	{
		MessageDiplomacyAction msg = new MessageDiplomacyAction();
		msg.action = DiplomaticAction.DENY_INVITATION;
		msg.targetFaction = factionUUID;
		return msg;
	}

	@SideOnly(Side.CLIENT)
	public static MessageDiplomacyAction merge(@Nonnull UUID targetFactionName)
	{
		MessageDiplomacyAction msg = new MessageDiplomacyAction();
		msg.action = DiplomaticAction.MERGE;
		msg.targetFaction = targetFactionName;
		return msg;
	}

	@SideOnly(Side.CLIENT)
	public static MessageDiplomacyAction disband()
	{
		MessageDiplomacyAction msg = new MessageDiplomacyAction();
		msg.action = DiplomaticAction.DISBAND;
		return msg;
	}

	@SideOnly(Side.CLIENT)
	public static MessageDiplomacyAction rename(@Nonnull String newName)
	{
		MessageDiplomacyAction msg = new MessageDiplomacyAction();
		msg.action = DiplomaticAction.RENAME;
		msg.newDisplayName = newName;
		return msg;
	}

	@SideOnly(Side.CLIENT)
	public static MessageDiplomacyAction changeLawForm(@Nonnull LawForm lawForm)
	{
		MessageDiplomacyAction msg = new MessageDiplomacyAction();
		msg.action = DiplomaticAction.CHANGE_LAW_FORM;
		msg.newLawForm = lawForm;
		return msg;
	}

	@SideOnly(Side.CLIENT)
	public static MessageDiplomacyAction changeColor(@Nonnull IIColor color)
	{
		MessageDiplomacyAction msg = new MessageDiplomacyAction();
		msg.action = DiplomaticAction.CHANGE_COLOR;
		msg.newColor = color;
		return msg;
	}

	@SideOnly(Side.CLIENT)
	public static MessageDiplomacyAction changeBanner(@Nonnull ItemStack bannerStack)
	{
		MessageDiplomacyAction msg = new MessageDiplomacyAction();
		msg.action = DiplomaticAction.CHANGE_BANNER;
		msg.newBannerStack = bannerStack;
		return msg;
	}

	@SideOnly(Side.CLIENT)
	public static MessageDiplomacyAction changePermission(PermissionRole role, PermissionCategory category, boolean allowed)
	{
		MessageDiplomacyAction msg = new MessageDiplomacyAction();
		msg.action = DiplomaticAction.CHANGE_PERMISSION;
		msg.targetRole = role.getId();
		msg.permissionCategory = category;
		msg.permissionSetting = allowed;
		return msg;
	}

	@SideOnly(Side.CLIENT)
	public static MessageDiplomacyAction sendOffer(@Nonnull UUID targetFaction, @Nonnull DiplomaticAgreement proposal)
	{
		MessageDiplomacyAction msg = new MessageDiplomacyAction();
		msg.action = DiplomaticAction.SEND_OFFER;
		msg.targetFaction = targetFaction;
		msg.agreementData = proposal.toNBT();
		return msg;
	}

	@SideOnly(Side.CLIENT)
	public static MessageDiplomacyAction sendUltimatum(@Nonnull UUID targetFaction, @Nonnull DiplomaticAgreement proposal)
	{
		MessageDiplomacyAction msg = new MessageDiplomacyAction();
		msg.action = DiplomaticAction.SEND_ULTIMATUM;
		msg.targetFaction = targetFaction;
		msg.agreementData = proposal.toNBT();
		return msg;
	}

	//--- Server handling ---
	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
		EntityPlayerMP sender = handler.player;
		DiplomacyHandler diplomacy = DiplomacyHandler.getInstance(false);

		//Invitation replies are player-scoped actions, they don't require permission checks
		if(action==DiplomaticAction.ACCEPT_INVITATION||action==DiplomaticAction.DENY_INVITATION)
		{
			OwnerIdentity invitedIdentity = diplomacy.getIdentityByUUID(targetFaction);
			if(invitedIdentity.getUUID()==DiplomacyHandler.NEUTRAL_UUID||invitedIdentity.getUUID()==DiplomacyHandler.GLOBAL_ENEMY_UUID)
				return;
			if(invitedIdentity.isInvalid())
				return;
			if(action==DiplomaticAction.ACCEPT_INVITATION)
				diplomacy.acceptInvitation(invitedIdentity, sender.getUniqueID());
			else
				diplomacy.denyInvitation(invitedIdentity, sender.getUniqueID());
			return;

		}

		OwnerIdentity identity = diplomacy.getOwnerIdentityForEntity(sender);
		if(identity.isInvalid()||!identity.isPermitted(sender, action.getRequiredPermission()))
			return;

		switch(action)
		{
			case CLAIM:
			{
				IOwnableProperty prop = diplomacy.getPropertyByUUID(propertyUUID);
				if(prop!=null&&prop.getOwnerIdentity()==DiplomacyHandler.NEUTRAL)
					diplomacy.claimProperty(identity, prop);
				break;
			}
			case START_SEIZING:
				break;
			case ADD_MEMBER:
			{
				if(targetPlayer!=null&&!identity.isMember(targetPlayer)&&!identity.isInvited(targetPlayer))
				{
					IILogger.info("Invited player "+targetPlayer+" to faction "+identity.getDisplayName());
					identity.invitePlayer(targetPlayer);
					diplomacy.saveAndSyncIdentity(identity);
				}
				break;
			}
			case CANCEL_INVITATION:
			{
				if(targetPlayer!=null&&identity.isInvited(targetPlayer))
				{
					identity.removeInvitation(targetPlayer);
					diplomacy.saveAndSyncIdentity(identity);
				}
				break;
			}
			case REMOVE_MEMBER:
			{
				if(identity.isMember(targetPlayer))
				{
					PermissionRole target = targetPlayer==null?null: identity.getRoleOf(targetPlayer);
					if(target!=null&&!target.isOwner()&&!targetPlayer.equals(sender.getUniqueID()))
					{
						identity.removeMember(targetPlayer);
						//sync = true
						diplomacy.saveAndSyncIdentity(identity);
					}
				}
				break;
			}
			case CHANGE_MEMBER_ROLE:
			{
				PermissionRole actingRole = identity.getRoleOf(sender.getUniqueID());
				PermissionRole currentRole = targetPlayer==null?null: identity.getRoleOf(targetPlayer);
				PermissionRole changedRole = identity.getAvailableRoles().get(targetRole);
				if(actingRole!=null&&actingRole.isOwner()&&currentRole!=null&&!currentRole.isOwner()
						&&changedRole!=null&&!changedRole.isOwner())
				{
					identity.withMember(targetPlayer, changedRole.getId());
					diplomacy.saveAndSyncIdentity(identity);
				}
				break;
			}
			case MERGE:
			{
				OwnerIdentity targetIdentity = diplomacy.getIdentityByUUID(targetFaction);
				if((targetIdentity.getUUID()==DiplomacyHandler.NEUTRAL_UUID||targetIdentity.getUUID()==DiplomacyHandler.GLOBAL_ENEMY_UUID)
						&&!targetIdentity.isInvalid()&&!targetIdentity.equals(identity))
					diplomacy.merge(identity, targetIdentity);
				break;
			}
			case DISBAND:
				break;
			case RENAME:
			{
				identity.withDisplayName(newDisplayName);
				diplomacy.saveAndSyncIdentity(identity);
				break;
			}
			case CHANGE_LAW_FORM:
			{
				identity.withLawForm(newLawForm);
				diplomacy.saveAndSyncIdentity(identity);
				break;
			}
			case CHANGE_COLOR:
			{
				identity.withColor(newColor);
				diplomacy.saveAndSyncIdentity(identity);
				break;
			}
			case CHANGE_BANNER:
			{
				identity.withBanner(newBannerStack);
				diplomacy.saveAndSyncIdentity(identity);
				break;
			}
			case SEND_OFFER:
			case SEND_ULTIMATUM:
			{
				DiplomaticAgreement proposal = new DiplomaticAgreement(agreementData);
				if(!Objects.equals(proposal.getSourceFaction(), identity.getUUID()))
				{
					IILogger.warn("Fake proposal source in message from "+sender.getName());
					return;
				}

				OwnerIdentity targetIdentity = diplomacy.getIdentityByUUID(targetFaction);
				if(targetIdentity.getUUID()!=DiplomacyHandler.NEUTRAL_UUID&&targetIdentity.getUUID()!=DiplomacyHandler.GLOBAL_ENEMY_UUID
						&&!targetIdentity.isInvalid())
					diplomacy.proposeAgreement(identity, targetIdentity, proposal);
				break;
			}
			case CHANGE_PERMISSION:
			{
				PermissionRole role = identity.getRoleOf(sender.getUniqueID());
				PermissionRole changed = identity.getAvailableRoles().get(targetRole);
				if(changed!=null&&!changed.isOwner()&&role!=null&&role.isOwner())
				{
					changed.withPermission(permissionCategory, permissionSetting);
					diplomacy.saveAndSyncIdentity(identity);
				}
				break;
			}
			default:
				break;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		//No action
	}

	//--- Serialization ---
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.action = this.readEnum(buf, DiplomaticAction.class);
		switch(action)
		{
			case CLAIM:
			case START_SEIZING:
				this.propertyUUID = UUID.fromString(readString(buf));
				break;
			case ADD_MEMBER:
			case REMOVE_MEMBER:
			case CANCEL_INVITATION:
				this.targetPlayer = UUID.fromString(readString(buf));
				break;
			case CHANGE_MEMBER_ROLE:
				this.targetPlayer = UUID.fromString(readString(buf));
				this.targetRole = readString(buf);
				break;
			case MERGE:
			case ACCEPT_INVITATION:
			case DENY_INVITATION:
				this.targetFaction = readUUID(buf);
				break;
			case DISBAND:
				//no extra data
				break;
			case RENAME:
				this.newDisplayName = readString(buf);
				break;
			case CHANGE_LAW_FORM:
				this.newLawForm = readEnum(buf, LawForm.class);
				break;
			case CHANGE_COLOR:
				this.newColor = readColor(buf);
				break;
			case CHANGE_BANNER:
				this.newBannerStack = readItemStack(buf);
				break;
			case SEND_OFFER:
			case SEND_ULTIMATUM:
				this.targetFaction = readUUID(buf);
				this.agreementData = readEasyNBT(buf);
				break;
			case CHANGE_PERMISSION:
				this.permissionCategory = readEnum(buf, PermissionCategory.class);
				this.permissionSetting = buf.readBoolean();
				this.targetRole = readString(buf);
				break;
			default:
				break;
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		writeEnum(buf, action);
		switch(action)
		{
			case CLAIM:
			case START_SEIZING:
				writeString(buf, propertyUUID.toString());
				break;
			case ADD_MEMBER:
			case REMOVE_MEMBER:
			case CANCEL_INVITATION:
				writeString(buf, targetPlayer.toString());
				break;
			case CHANGE_MEMBER_ROLE:
				writeString(buf, targetPlayer.toString());
				writeString(buf, targetRole);
				break;
			case MERGE:
			case ACCEPT_INVITATION:
			case DENY_INVITATION:
				writeUUID(buf, targetFaction);
				break;
			case DISBAND:
				break;
			case RENAME:
				writeString(buf, newDisplayName);
				break;
			case CHANGE_LAW_FORM:
				writeEnum(buf, newLawForm);
				break;
			case CHANGE_COLOR:
				writeColor(buf, newColor);
				break;
			case CHANGE_BANNER:
				writeItemStack(buf, newBannerStack);
				break;
			case SEND_OFFER:
			case SEND_ULTIMATUM:
				writeUUID(buf, targetFaction);
				writeEasyNBT(buf, agreementData);
				break;
			case CHANGE_PERMISSION:
				writeEnum(buf, permissionCategory);
				buf.writeBoolean(permissionSetting);
				writeString(buf, targetRole);
				break;
			default:
				break;
		}
	}
}
