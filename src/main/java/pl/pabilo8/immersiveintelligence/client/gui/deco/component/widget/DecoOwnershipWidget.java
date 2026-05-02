package pl.pabilo8.immersiveintelligence.client.gui.deco.component.widget;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageDiplomacyAction;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomaticStatus;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.IOwnableProperty;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 08.09.2025
 */
public class DecoOwnershipWidget extends DecoComponentWidgetBase<DecoOwnershipWidget>
{
	private final IOwnableProperty property;

	public DecoOwnershipWidget(IOwnableProperty property)
	{
		super();
		withSize(128, 128+32);
		withBackground(DecoTextures.BG_PAPER);
		withBackgroundMask(DecoTextures.TEMPLATE_PAPER);
		this.property = property;
	}

	@Override
	protected boolean initialize()
	{
		if(super.initialize())
		{
			OwnerIdentity identity = property.getOwnerIdentity();
			OwnerIdentity playerIdentity = DiplomacyHandler.getInstance(true).getOwnerIdentityForEntity(ClientUtils.mc().player);

			withTitleLabel(IIReference.GUI_TOOLTIP_KEY+"widget.ownership", DecoAlignment.TOP);

			if(identity==null||identity==DiplomacyHandler.NEUTRAL)
			{
				//"Abandoned" Label
				addLabel(I18n.format("desc.immersiveintelligence.diplomacy.noowner"), 2, 8);
				//Long description
				DecoLabel label = addLabel(TextFormatting.ITALIC+I18n.format("desc.immersiveintelligence.diplomacy.noowner_long."+playerIdentity.getLawForm().getName()), 2, 8+10)
						.withSize(128-4, 24)
						.withWrapping(true);

				addComponent(new DecoButton(3, 8+10+label.getTotalHeight()+24+2)
						.withWidth(width-6)
						.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
						.withBackgroundColor(IIColor.MC_RED)
						.withIcon(DecoTextures.ICON_OWNERSHIP)
						.withText("desc.immersiveintelligence.diplomacy.ownership.claim")
						.withOnLMBPressed(() -> {
							IIPacketHandler.sendToServer(MessageDiplomacyAction.claimProperty(property));
							if(parentGui!=null)
								parentGui.closeGUI();
						})
				);
			}
			else
			{
				String owner = identity.getColor().getHexCol(identity.getDisplayName());
				addLabel(I18n.format("desc.immersiveintelligence.diplomacy.ownedby", owner), 2, 8);

				DiplomaticStatus relation = identity.getRelationTowards(ClientUtils.mc().player);
				addLabel(TextFormatting.ITALIC+I18n.format("desc.immersiveintelligence.diplomacy.status."+relation.getName()+"_long", owner), 2, 8+10)
						.withSize(128-4, 24)
						.withWrapping(true);

				//addLabel();

			}

			return true;
		}
		return false;
	}

	@Override
	public String getName()
	{
		return "ownership";
	}

	@Override
	public DecoTab provideTab()
	{
		return (DecoTab)new DecoTab()
				.withBackground(DecoTextures.COMPONENT_TAB_WIDGET)
				.withBackgroundColor(IIColor.fromPackedRGB(0x5e3c3c))
				.withPadding(6, 2, 2, 2)
				.withIconAlignment(DecoAlignment.CENTER)
				//Engineer's Manual
				.withIcon(DecoTextures.ICON_OWNERSHIP)
				.withTranslatedTooltip(IIReference.GUI_TOOLTIP_KEY+"widget.ownership.show");
	}
}
