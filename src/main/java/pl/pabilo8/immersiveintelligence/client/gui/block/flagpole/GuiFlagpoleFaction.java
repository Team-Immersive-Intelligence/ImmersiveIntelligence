package pl.pabilo8.immersiveintelligence.client.gui.block.flagpole;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSwitch;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTabGroup;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoElementDisplays;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoColorPickerPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBannerDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoPlayerDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFlagpole;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerFlagpole;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageDiplomacyAction;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler.PlayerInfo;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionCategory;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionRole;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides basic faction management.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 27.07.2026
 * @since 27.12.2025
 */
@DecoTemplate(name = "flagpole_faction", category = DecoGuiCategory.TERRITORY_CONTROL_TILE)
public class GuiFlagpoleFaction extends DecoTileGui<TileEntityFlagpole, ContainerFlagpole>
{
	private String factionName = null;
	private IIColor factionColor = null;
	private ItemStack factionBanner = null;
	private PermissionRole selectedRole;

	public GuiFlagpoleFaction(EntityPlayer player, TileEntityFlagpole tile)
	{
		super(player, tile, IIGUI.FLAGPOLE_FACTION);
	}

	@Override
	public void onInit()
	{
		OwnerIdentity identity = DiplomacyHandler.getLocalPlayerIdentity();
		NetHandlerPlayClient connection = mc.getConnection();
		assert connection!=null;

		startBackground()
				.withBox(DecoTextures.BG_STEEL, 0, 0, 152+96, 152)
				.withTitleBar(tile)
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 32, 152, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.build();

		//Tabs
		addLinkTab(IIGUI.FLAGPOLE, DecoTextures.ICON_MAP, "map_module");
		addLinkTab(IIGUI.FLAGPOLE_CONFIG, DecoTextures.ICON_CONFIG, "configuration_module");
		addLinkTab(IIGUI.FLAGPOLE_FACTION, DecoTextures.ICON_FACTION_CONFIG, "faction_module");

		//Content tabs
		addComponent(new DecoTabGroup(4, 4+4+1)
				.withSize(152+96-8, 14)
				.withHorizontalAlignment(true)
				.withSpacing(1)
				.withTab((DecoTab)new DecoTab().withText(IIReference.GUI_LABEL_KEY+"faction_management.insignia")
								.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"faction_management.insignia.tooltip"),
						buildInsigniaPage(identity))
				.withTab((DecoTab)new DecoTab().withText(IIReference.GUI_LABEL_KEY+"faction_management.members")
								.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"faction_management.members.tooltip"),
						buildMembersPage(identity))
				.withTab((DecoTab)new DecoTab().withText(IIReference.GUI_LABEL_KEY+"faction_management.invitations")
								.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"faction_management.invitations.tooltip"),
						buildInvitationsPage(identity, connection))
				.withTab((DecoTab)new DecoTab().withText(IIReference.GUI_LABEL_KEY+"faction_management.permissions")
								.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"faction_management.permissions.tooltip"),
						buildRolesPage(identity))
		);
	}

	private DecoPanel createContentPanel()
	{
		return addComponent(new DecoPanel(4, 20+2)
				.withSize(152+96-8, 160-16-16+2)
				.withBackground(DecoTextures.BG_STEEL)
				.withBackgroundMask(DecoTextures.TEMPLATE_SQUARE)
		);
	}

	private DecoPanel buildInsigniaPage(OwnerIdentity identity)
	{
		DecoPanel panel = createContentPanel();
		boolean disabled = !identity.isPermitted(mc.player, PermissionCategory.MODIFY_INSIGNIA);

		DecoPanel topPanel = panel.addComponent(new DecoPanel(2, 2)
				.withSize(panel.width-4, 36-2)
				.withBackground(DecoTextures.BG_PAPER)
				.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
		);

		topPanel.addComponent(new DecoBannerDisplay(4, 4)
				.withSize((int)(44*1.25f), (int)(24*1.25f))
				.withBackgroundTexture(DecoSprite.atlasSprite(DecoTextures.SLOT_IE, 32, true))
				.withBanner(identity.getBanner())
				.withOnPressed((gui, button, mouseX, mouseY) -> {
					ItemStack stack = getMouseHeldItemStack();
					if(!(stack.getItem() instanceof ItemBanner))
						return false;
					ItemStack copy = stack.copy();
					copy.setCount(1);
					gui.withBanner(copy);
					factionBanner = copy;
					return true;
				})
				.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"faction_management.insignia.banner.tooltip")
				.withDisabled(disabled)
		);

		topPanel.addLabel(new DecoLabel(fontRenderer, 64-4, 3)
				.withSize(panel.width-76+8, 12)
				.withAlign(DecoAlignment.LEFT)
				.withText(IIReference.GUI_LABEL_KEY+"faction_management.insignia.name")
				.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"faction_management.insignia.name.tooltip")
		);
		topPanel.addComponent(new DecoTextField(64-4, 2+12)
				.withSize(panel.width-76+8, 16)
				.withText(identity.getDisplayName())
				.withMaxStringLength(32)
				.withDisabled(disabled)
				.withOnTextChanged(s -> factionName = s.isEmpty()?null: s)
				.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"faction_management.insignia.name.tooltip")
		);

		panel.addComponent(new DecoColorPickerPanel(4, 38+2)
				.withOnColorChanged((oldColor, newColor) -> factionColor = newColor)
				.withColor(identity.getColor())
				.withSize(panel.width-8, 82)
				.withDisabled(disabled)
		);
		return panel;
	}

	private DecoPanel buildMembersPage(OwnerIdentity identity)
	{
		DecoPanel panel = createContentPanel();
		boolean canRemove = identity.isPermitted(mc.player, PermissionCategory.REMOVE_MEMBERS);
		boolean canChangeRoles = identity.isOwner(mc.player.getUniqueID());
		DiplomacyHandler handler = DiplomacyHandler.getInstance(true);
		List<PermissionRole> assignableRoles = identity.getAvailableRoles().values().stream()
				.filter(role -> !role.isOwner())
				.collect(Collectors.toList());

		List<UUID> members = identity.getMembers().stream()
				.sorted(Comparator
						.comparing((UUID uuid) -> handler.getPlayerInfo(uuid).getName(), String.CASE_INSENSITIVE_ORDER)
						.thenComparing(UUID::toString))
				.collect(Collectors.toList());

		panel.addComponent(new DecoList<UUID>(2, 2)
				.withSize(panel.width-4, 152-26-2)
				.withEntries(members)
				.withDisplayFunction(new DecoEntryPanelBuilder<UUID>()
						.withHeight(22)
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
						.withLabel("name", () -> new DecoLabel(fontRenderer, 20, 3)
								.withSize(78, 16)
								.withAlign(DecoAlignment.LEFT))
						.withComponent("head", () -> new DecoPlayerDisplay(3, 3).withSize(16, 16))
						.withComponent("role", p -> new DecoDropdown<PermissionRole>(p.width-118, 3)
								.withSize(96, 16)
								.withEntries(assignableRoles)
								.withDisplayFunction(DecoElementDisplays.getSimpleTextDisplay(PermissionRole::getDisplayName)))
						.withComponent("remove", p -> new DecoButton(p.width-19, 3)
								.withSize(16, 16)
								.withTemplate(DecoTemplates.ACTION_BUTTON_REMOVE)
								.withOnLMBPressed(() -> {
									UUID current = p.getCurrentElement();
									IIPacketHandler.sendToServer(MessageDiplomacyAction.removeMember(current));
									p.getCurrentList().removeEntry(current);
								}))
						.withElementApplyMethod((uuid, entry) -> {
							PlayerInfo info = handler.getPlayerInfo(uuid);
							PermissionRole role = identity.getRoleOf(uuid);
							entry.label("name").withRawText(info.getName());
							entry.component("head", DecoPlayerDisplay.class)
									.withPlayerInfo(info);

							DecoDropdown<PermissionRole> dropdown = entry.component("role", DecoDropdown.class);
							dropdown.withEntries(role!=null&&role.isOwner()?Collections.singletonList(role): assignableRoles)
									.withSelectedEntry(role)
									.withDisabled(!canChangeRoles||role==null||role.isOwner())
									.withOnSelectedEntry((oldRole, newRole) -> {
										if(newRole!=null&&!newRole.equals(oldRole))
											IIPacketHandler.sendToServer(MessageDiplomacyAction.changeMemberRole(uuid, newRole));
									});

							entry.component("remove", DecoButton.class)
									.withDisabled(!canRemove||role==null||role.isOwner()||uuid.equals(mc.player.getUniqueID()));
						})
				)
		);
		return panel;
	}

	private DecoPanel buildInvitationsPage(OwnerIdentity identity, NetHandlerPlayClient connection)
	{
		DecoPanel panel = createContentPanel();
		boolean canInvite = identity.isPermitted(mc.player, PermissionCategory.INVITE_MEMBERS);
		DiplomacyHandler handler = DiplomacyHandler.getInstance(true);

		DecoTextField username = panel.addComponent(new DecoTextField(2, 2)
				.withSize(panel.width-20, 16)
				.withMaxStringLength(16)
				.withDisabled(!canInvite)
				.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"faction_management.invitations.username.tooltip")
		);
		panel.addComponent(new DecoButton(panel.width-18, 2)
				.withTemplate(DecoTemplates.ACTION_BUTTON_ADD)
				.withSize(16, 16)
				.withDisabled(!canInvite)
				.withOnLMBPressed(() -> {
					NetworkPlayerInfo playerInfo = connection.getPlayerInfo(username.getText());
					if(playerInfo!=null&&!identity.isMember(playerInfo.getGameProfile().getId()))
					{
						IIPacketHandler.sendToServer(MessageDiplomacyAction.invitePlayer(playerInfo.getGameProfile().getId()));
						username.withText("");
					}
				})
		);

		List<UUID> invited = new ArrayList<>(identity.getInvitedPlayers());
		invited.sort(Comparator
				.comparing((UUID uuid) -> handler.getPlayerInfo(uuid).getName(), String.CASE_INSENSITIVE_ORDER)
				.thenComparing(UUID::toString));

		panel.addComponent(new DecoList<UUID>(2, 24-4)
				.withSize(panel.width-4, 132-24-2)
				.withEntries(invited)
				.withDisplayFunction(new DecoEntryPanelBuilder<UUID>()
						.withHeight(22)
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
						.withLabel("name", () -> new DecoLabel(fontRenderer, 20, 3)
								.withSize(panel.width-62, 16)
								.withAlign(DecoAlignment.LEFT))
						.withComponent("head", () -> new DecoPlayerDisplay(3, 3).withSize(16, 16))
						.withComponent("cancel", p -> new DecoButton(p.width-19, 3)
								.withSize(16, 16)
								.withTemplate(DecoTemplates.ACTION_BUTTON_REMOVE)
								.withDisabled(!canInvite)
								.withOnLMBPressed(() -> IIPacketHandler.sendToServer(
										MessageDiplomacyAction.cancelInvitation(p.getCurrentElement()))))
						.withElementApplyMethod((uuid, entry) -> {
							PlayerInfo info = handler.getPlayerInfo(uuid);
							entry.label("name").withRawText(info.getName());
							entry.component("head", DecoPlayerDisplay.class)
									.withPlayerInfo(info);
						})
				)
		);
		return panel;
	}

	private DecoPanel buildRolesPage(OwnerIdentity identity)
	{
		DecoPanel panel = createContentPanel();
		List<PermissionRole> roles = new ArrayList<>(identity.getAvailableRoles().values());
		boolean canEditRoles = identity.isOwner(mc.player.getUniqueID());

		if(selectedRole==null||!roles.contains(selectedRole))
			selectedRole = roles.stream()
					.filter(role -> !role.isOwner())
					.findFirst()
					.orElse(identity.getRoleOf(mc.player.getUniqueID()));

		DecoEntryPanelBuilder<PermissionCategory> permissionDisplay = selectedRole==null?null:
				new DecoEntryPanelBuilder<PermissionCategory>()
						.withHeight(20)
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
						.withComponent("toggle", p -> new DecoSwitch(3, 2)
								.withDisabled(!canEditRoles||selectedRole.isOwner())
								.withOnToggle(change -> IIPacketHandler.sendToServer(
										MessageDiplomacyAction.changePermission(selectedRole, p.getCurrentElement(), change)))
								.withTranslatedTooltip(p.getCurrentElement().getFullLocaleKey(),
										TextFormatting.GRAY+p.getCurrentElement().getFullLocaleKey()+".tooltip")
						)
						.withElementApplyMethod((permission, entry) -> entry.component("toggle", DecoSwitch.class)
								.withCurrentState(selectedRole.isAllowed(permission))
								.withText(permission.getFullLocaleKey()));

		panel.addLabel(IIReference.GUI_LABEL_KEY+"faction_management.permissions.role", 4, 3)
				.withSize(64, 16)
				.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"faction_management.permissions.role.tooltip");
		DecoDropdown<PermissionRole> roleDropdown = panel.addComponent(new DecoDropdown<PermissionRole>(panel.width-150-2, 2)
				.withSize(150, 16)
				.withEntries(roles)
				.withDisplayFunction(DecoElementDisplays.getSimpleTextDisplay(PermissionRole::getDisplayName))
				.withSelectedEntry(selectedRole)
				.withTranslatedTooltip(IIReference.GUI_LABEL_KEY+"faction_management.permissions.role.tooltip")
		);
		roleDropdown.withOnSelectedEntry((oldRole, newRole) -> {
			if(newRole!=null&&!newRole.equals(oldRole))
			{
				selectedRole = newRole;
				if(permissionDisplay!=null)
					permissionDisplay.refreshCache();
			}
		});

		if(selectedRole==null)
		{
			panel.addLabel(new DecoLabel(fontRenderer, 4, 30)
					.withSize(panel.width-14, 16)
					.withAlign(DecoAlignment.CENTER)
					.withText(IIReference.GUI_LABEL_KEY+"faction_management.permissions.empty")
			);
		}
		else
		{
			panel.addComponent(new DecoList<PermissionCategory>(2, 24-4)
					.withSize(panel.width-4, 132-26)
					.withEntries(PermissionCategory.values())
					.withDisplayFunction(permissionDisplay)
			);
		}

		return panel;
	}

	@Override
	public void onGuiClosed()
	{
		if(factionName!=null)
			IIPacketHandler.sendToServer(MessageDiplomacyAction.rename(factionName));
		if(factionColor!=null)
			IIPacketHandler.sendToServer(MessageDiplomacyAction.changeColor(factionColor));
		if(factionBanner!=null)
			IIPacketHandler.sendToServer(MessageDiplomacyAction.changeBanner(factionBanner));
		super.onGuiClosed();
	}
}
