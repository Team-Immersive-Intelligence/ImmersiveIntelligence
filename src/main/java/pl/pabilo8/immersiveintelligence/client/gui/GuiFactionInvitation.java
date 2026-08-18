package pl.pabilo8.immersiveintelligence.client.gui;

import blusunrize.immersiveengineering.client.ClientProxy;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoPlayerGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoTitleLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBannerDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoPlayerDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Graphics;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerPlayerGui;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageDiplomacyAction;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageDiplomacySync;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler.PlayerInfo;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionRole;

import java.util.List;

/**
 * Lists faction invitations addressed to the local player. Uses vanilla GUI style.
 *
 * @author Pabilo8
 * @since 22.07.2026
 */
@DecoTemplate(name = "faction_invitations", category = DecoGuiCategory.GENERIC_PLAYER)
public class GuiFactionInvitation extends DecoPlayerGui<ContainerPlayerGui>
{
	public GuiFactionInvitation(EntityPlayer player)
	{
		super(player, IIGUI.FACTION_INVITATIONS);
	}

	@Override
	public void onInit()
	{
		OwnerIdentity userIdentity = DiplomacyHandler.getLocalPlayerIdentity();
		startBackground()
				.conditionally(Graphics.decoVanillaGUIStyle==DecoVanillaGUIStyle.VANILLA,
						b -> b.withBox(DecoTextures.BG_VANILLA, DecoTextures.TEMPLATE_ROUND, 0, 0, 220, 144))
				.conditionally(Graphics.decoVanillaGUIStyle==DecoVanillaGUIStyle.WOODEN,
						b -> b.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 0, 0, 220, 144))
				.withTitleBar(I18n.format(IIReference.GUI_LABEL_KEY+"faction_invitation"))
				.build();

		//Fallback, for when faction/player info is missing
		if(userIdentity==DiplomacyHandler.NEUTRAL)
		{
			addLabel(IIReference.GUI_LABEL_KEY+"faction_invitation.invalid", 0, 0)
					.withSize(220, 144)
					.withAlign(DecoAlignment.CENTER);
			IIPacketHandler.INSTANCE.sendToServer(MessageDiplomacySync.requestUpdateMessage());
			return;
		}
		String factionName = userIdentity.getDisplayName();
		PlayerInfo playerInfo = DiplomacyHandler.getInstance(true).getPlayerInfo(ClientUtils.mc().player);
		String playerName = playerInfo.getName();
		PermissionRole role = userIdentity.getRoleOf(player.getUniqueID());
		String roleName = role==null?"unknown": role.getDisplayName();

		//Current faction status
		if(Graphics.decoVanillaGUIStyle==DecoVanillaGUIStyle.VANILLA)
			addLabel(IIReference.GUI_LABEL_KEY+"faction_invitation.current", 4, 8);
		else
			addLabel(new DecoTitleLabel(this.fontRenderer, 4+4, 8).withBackgroundLocation(DecoTextures.LABEL_WOODEN)
					.withText(IIReference.GUI_LABEL_KEY+"faction_invitation.current")
					.withSize(fontRenderer.getStringWidth(I18n.format(IIReference.GUI_LABEL_KEY+"faction_invitation.current"))+4, 10)
			);

		DecoPanel userPanel = addComponent(new DecoPanel(4, 8+12-2)
				.withSize(204+16-4, 22)
		);
		if(Graphics.decoVanillaGUIStyle==DecoVanillaGUIStyle.VANILLA)
			userPanel.withBackground(DecoTextures.BG_VANILLA);
		else
			userPanel.withBackground(DecoTextures.BG_PAPER)
					.withBackgroundMask(DecoTextures.TEMPLATE_PAPER);
		userPanel.addComponents(
				//Player head
				new DecoPlayerDisplay(3, 3)
						.withEntity(player)
						.withSize(16, 16),
				//Faction banner
				new DecoBannerDisplay(220-48+4, 3)
						.withBanner(userIdentity.getBanner())
		);

		//Info text
		DecoLabel userLabel;
		if(userIdentity.getMembers().size()==1&&factionName.equals(playerName))
			userLabel = userPanel.addLabel(new DecoLabel(ClientProxy.itemFont, 2+18+1, 0))
					.withText(I18n.format(IIReference.GUI_LABEL_KEY+"faction_invitation.current.alone", playerName));
		else
			userLabel = userPanel.addLabel(new DecoLabel(ClientProxy.itemFont, 2+18+1, 0))
					.withRawText(I18n.format(IIReference.GUI_LABEL_KEY+"faction_invitation.current.faction",
							playerName, roleName, userIdentity.getColor().getHexCol(factionName)
					));
		userLabel.withSize(128+16+16-6, 22)
				.withAlign(DecoAlignment.CENTER)
				.withWrapping(true);

		//Invitation list panel
		if(Graphics.decoVanillaGUIStyle==DecoVanillaGUIStyle.VANILLA)
			addLabel(IIReference.GUI_LABEL_KEY+"faction_invitation.invitations", 4, 42+2-2);
		else
			addLabel(new DecoTitleLabel(this.fontRenderer, 4+4, 42+2-2)
					.withBackgroundLocation(DecoTextures.LABEL_WOODEN)
					.withText(IIReference.GUI_LABEL_KEY+"faction_invitation.invitations")
					.withSize(fontRenderer.getStringWidth(I18n.format(IIReference.GUI_LABEL_KEY+"faction_invitation.invitations"))+4, 10)
					.withAlign(DecoAlignment.CENTER)
			);
		List<OwnerIdentity> invitations = DiplomacyHandler.getInstance(true)
				.getPendingInvitationIdentitiesForPlayer(player.getUniqueID());

		if(invitations.isEmpty())
		{
			//No invitations info
			DecoPanel cardPanel = addComponent(new DecoPanel(4, 42+12-2).withSize(204+16-4, 86));
			if(Graphics.decoVanillaGUIStyle==DecoVanillaGUIStyle.VANILLA)
				cardPanel.withBackground(DecoTextures.BG_VANILLA);
			else
				cardPanel.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_PAPER);
			addLabel(IIReference.GUI_LABEL_KEY+"faction_invitation.invitations.none", 8, 28+136/2-12)
					.withSize(204, 16)
					.withAlign(DecoAlignment.CENTER);
		}
		else //Invitation list
		{
			DecoList<OwnerIdentity> list = addComponent(new DecoList<OwnerIdentity>(4, 42+12-2)
					.withSize(204+16-4, 86)
					.withEntries(invitations)
					.withDisplayFunction(new DecoEntryPanelBuilder<OwnerIdentity>()
							.withHeight(22)
							.withBackground(Graphics.decoVanillaGUIStyle==DecoVanillaGUIStyle.VANILLA?DecoTextures.BG_VANILLA: DecoTextures.BG_PAPER)
							.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
							//Faction banner and name
							.withComponent("banner", p -> new DecoBannerDisplay(3, 3)
									.withSize(32, 20)
							)
							.withLabel("name", p -> new DecoLabel(fontRenderer, 34, 4)
									.withSize(128, 16)
									.withAlign(DecoAlignment.LEFT))
							//Accept and Reject buttons
							.withComponent("accept", p -> new DecoButton(p.width-32-2-2, 4-1)
									.withTemplate(DecoTemplates.ACTION_BUTTON_ACCEPT)
									.withSize(16, 16)
									.withBackgroundColor(IIColor.fromPackedRGB(0x698756))
									.withOnLMBPressed(() -> {
										IIPacketHandler.sendToServer(MessageDiplomacyAction.acceptInvitation(p.getCurrentElement().getUUID()));
										closeGUI();
									})
							)
							.withComponent("reject", p -> new DecoButton(p.width-17-2, 4-1)
									.withTemplate(DecoTemplates.ACTION_BUTTON_REJECT)
									.withSize(16, 16)
									.withBackgroundColor(IIColor.fromPackedRGB(0x8C5353))
									.withOnLMBPressed(() -> {
										IIPacketHandler.sendToServer(MessageDiplomacyAction.denyInvitation(p.getCurrentElement().getUUID()));
										closeGUI();
									})
							)
							.withElementApplyMethod((identity, entry) -> {
								entry.label("name").withRawText(identity.getDisplayName());
								entry.component("banner", DecoBannerDisplay.class).withBanner(identity.getBanner());
							})
					)
			);
			if(Graphics.decoVanillaGUIStyle==DecoVanillaGUIStyle.VANILLA)
				list.withBackground(DecoTextures.BG_VANILLA)
						.withListBackground(DecoTextures.BG_VANILLA)
						.withListBackgroundColor(IIColor.BLACK)
						.withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_VANILLA);
		}
	}
}
