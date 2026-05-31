package pl.pabilo8.immersiveintelligence.client.gui.block.flagpole;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTileGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSwitch;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoElementDisplays;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoList;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoTitleLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoColorPickerPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBannerDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFlagpole;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerFlagpole;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageDiplomacyAction;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler.PlayerInfo;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionCategory;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionRole;

import java.util.UUID;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @updates 03.11.2026
 * @ii-approved 0.3.1
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
				.withBox(DecoTextures.BG_STEEL, 0, 0, 248+32, 152+32)
				.withTitleBar(tile)
				.withNextLayer()
				.withBox(DecoTextures.BG_WOODEN, DecoTextures.TEMPLATE_ROUND_WOODEN, 32+16, 152+32, 176, 92)
				.withInventorySlots(SlotStyle.VANILLA, container.inventorySlots)
				.withInventoryTitleBar()
				.withFrame(DecoTextures.FRAME_WOODEN_THIN, 4, false, new boolean[]{true, false, false, false})
				.build();

		//Tabs
		addLinkTab(IIGUI.FLAGPOLE, DecoTextures.ICON_MAP, "map_module");
		addLinkTab(IIGUI.FLAGPOLE_FACTION, DecoTextures.ICON_FACTION_CONFIG, "faction_module");

		//Faction Name
		addLabel(new DecoTitleLabel(fontRenderer, 6, 4)
				.withBackgroundLocation(DecoTextures.LABEL_STEEL)
				.withSize(60, 8)
				.withRawText("Name")
		);

		//Banner box
		addComponent(new DecoBannerDisplay(5+1, 15+1)
				.withSize(40+2, 20+2)
				.withBackgroundTexture(DecoSprite.atlasSprite(DecoTextures.SLOT_IE, 32, true))
				.withBanner(identity.getBanner())
				.withOnPressed((gui, button, mouseX, mouseY) -> {
					ItemStack stack = getMouseHeldItemStack();
					if(stack.getItem() instanceof ItemBanner)
					{
						ItemStack copy = stack.copy();
						copy.setCount(1);
						gui.withBanner(copy);
						this.factionBanner = copy;
						return true;
					}
					return false;
				})
				.withDisabled(!identity.isPermitted(mc.player, PermissionCategory.MODIFY_INSIGNIA))
		);
		addComponent(new DecoTextField(26+20, 14+2)
				.withSize(120-20+4, 16)
				.withText(identity.getDisplayName())
				.withMaxStringLength(32)
				.withDisabled(!identity.isPermitted(mc.player, PermissionCategory.MODIFY_INSIGNIA))
				.withOnTextChanged(s -> {
					this.factionName = s.isEmpty()?null: s;
				})
		);

		//Color header
		addLabel(new DecoTitleLabel(fontRenderer, 6, 34+4)
				.withBackgroundLocation(DecoTextures.LABEL_STEEL)
				.withSize(60, 8)
				.withRawText("Color")
		);

		//Color picker
		addComponent(new DecoColorPickerPanel(4, 44+4+1)
				{
					@Override
					protected boolean initialize()
					{
						if(!super.initialize())
							return false;
						//Hide the dye color dropdown
						dyeColor.visible = false;
						dyeColor.enabled = false;
						if(!labels.isEmpty())
							labels.remove(labels.size()-1);
						return true;
					}
				}
						.withOnColorChanged((oldColor, newColor) -> {
							factionColor = newColor;
						})
						.withColor(identity.getColor())
						.withSize(144+4, 44)
						.withDisabled(!identity.isPermitted(mc.player, PermissionCategory.MODIFY_INSIGNIA))
		);

		//Members Panel
		DecoPanel panelMembers = addComponent(new DecoPanel(4, 90+8-1)
				.withSize(144+4, 58+24+1)
				.withBackground(DecoTextures.BG_PAPER)
				.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
				.withTitleLabel("Members", DecoAlignment.TOP)
		);

		//Invite row
		DecoTextField usernameField = panelMembers.addComponent(new DecoTextField(4, 6+2)
				.withSize(118+4+2, 16)
				.withMaxStringLength(16)
		);
		panelMembers.addComponent(new DecoButton(124+2+2, 6+2)
				.withTemplate(DecoTemplates.ACTION_BUTTON_ADD)
				.withSize(16, 16)
				.withOnLMBPressed(() -> {
					NetworkPlayerInfo playerInfo = connection.getPlayerInfo(usernameField.getText());
					if(playerInfo!=null)
						IIPacketHandler.sendToServer(MessageDiplomacyAction.invitePlayer(playerInfo.getGameProfile().getId()));
				})
		);

		//Member list
		panelMembers.addComponent(new DecoList<UUID>(4, 22+2)
				.withSize(132+4+2+2, 32+24+2-2)
				.withEntries(identity.getMembers())
				.withDisplayFunction(new DecoEntryPanelBuilder<UUID>()
						.withHeight(18)
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
						.withLabel("name", new DecoLabel(fontRenderer, 16+1+1, 2)
								.withSize(100, 16)
								.withAlign(DecoAlignment.LEFT)
						)
						.withComponent("head", new DecoImage(2, 2)
								.withSize(14, 14)
						)
						.withComponent(p -> new DecoButton(p.width-16, 2)
								.withTemplate(DecoTemplates.ACTION_BUTTON_REMOVE)
								.withOnLMBPressed(() -> IIPacketHandler.sendToServer(MessageDiplomacyAction.removeMember(p.getCurrentElement())))
						)
						.withElementApplyMethod((uuid, panel) -> {
							PlayerInfo playerInfo = DiplomacyHandler.getInstance(true).getPlayerInfo(uuid);
							panel.label("name").withRawText(playerInfo.getName());
							panel.component("head", DecoImage.class).withImageLocation(playerInfo.getSkin())
									.withUV(64, 8, 8, 16, 16);
						})
				)
		);

		//Permissions Panel
		DecoPanel panelPerms = addComponent(new DecoPanel(152+2, 4)
				.withSize(92-2+32, 144+32)
				.withBackground(DecoTextures.BG_STEEL)
				.withBackgroundMask(DecoTextures.TEMPLATE_SQUARE)
				.withTitleLabel("Permissions", DecoAlignment.TOP)
		);

		panelPerms.addLabel("Role:", 4, 8+2-1)
				.withSize(panelPerms.width-72-2, 14)
				.withAlign(DecoAlignment.LEFT);
		panelPerms.addComponents(
				new DecoDropdown<PermissionRole>(panelPerms.width-72, 8+2-2)
						.withSize(72-4, 14)
						.withEntries(identity.getAvailableRoles().values())
						.withDisplayFunction(DecoElementDisplays.getSimpleTextDisplay(PermissionRole::getDisplayName))
						.withSelectedEntry(selectedRole = identity.getRoleOf(playerContainer.player.getUniqueID()))
						.withOnSelectedEntry((oldRole, newRole) -> selectedRole = newRole),
				new DecoList<PermissionCategory>(2, 12+8+2+1)
						.withSize(panelPerms.width-4-2, panelPerms.height-32+8-2)
						.withEntries(PermissionCategory.values())
						.withDisplayFunction(new DecoEntryPanelBuilder<PermissionCategory>()
								.withHeight(18)
								.withComponent("toggle", p -> new DecoSwitch(2, 2)
										.withOnToggle(change -> IIPacketHandler.sendToServer(MessageDiplomacyAction.changePermission(selectedRole,
												p.getCurrentElement(), change)))
								)
								.withElementApplyMethod((permission, panel) -> {
									panel.component("toggle", DecoSwitch.class)
											.withCurrentState(selectedRole.isAllowed(permission))
											.withText(permission.getFullLocaleKey());
								})
						)
		);


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
