package pl.pabilo8.immersiveintelligence.client.gui.deco.component.widget;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.api.style.IStyleCustomizable;
import pl.pabilo8.immersiveintelligence.api.style.StyleConstraints.PaintStyleConstraint;
import pl.pabilo8.immersiveintelligence.api.style.StyleCustomization;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoColorPicker;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageEntityNBTSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 08.09.2025
 */
public class DecoStyleWidget extends DecoComponentWidgetBase<DecoStyleWidget>
{
	private final IStyleCustomizable customizable;
	private final StyleCustomization style;

	public DecoStyleWidget(IStyleCustomizable customizable)
	{
		super();
		this.customizable = customizable;
		this.style = customizable.getStyle();
		withSize(128, 128+32);
		withBackground(DecoTextures.BG_PAPER);
		withBackgroundMask(DecoTextures.TEMPLATE_PAPER);
	}

	@Override
	protected boolean initialize()
	{
		if(super.initialize())
		{
			withTitleLabel(IIReference.GUI_TOOLTIP_KEY+"widget.style", DecoAlignment.TOP);
			DecoLabel headInfo = addLabel(TextFormatting.ITALIC+I18n.format(IIReference.GUI_TOOLTIP_KEY+"widget.style.desc"), 4, 2+4)
					.withSize(width-4-4, 32)
					.withWrapping(true);

			addLabel(IIReference.GUI_TOOLTIP_KEY+"widget.style.style", 2, 10+2+headInfo.getTotalHeight());
			addComponent(new DecoDropdown<String>(32, 10+headInfo.getTotalHeight())
					.withWidth(width-32-4)
					.withDropdownWidth(width-32-4)
					.withMaxDisplayedEntries(5)
					.withEntries(style.getConstraints().getStyles())
					.withSelectedEntry(style.getStyle())
					.withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_PAPER)
					.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
					.withDropdownSymbol(DecoTextures.COMPONENT_DROPDOWN_SYMBOL_PAPER)
					.withOnSelectedEntry((oldStyle, newStyle) -> {
						customizable.getStyle().withStyle(newStyle);
						//Notify the server side of the new style
						if(customizable instanceof TileEntityIEBase)
							IIPacketHandler.sendToServer(new MessageIITileSync((TileEntityIEBase)customizable, EasyNBT.newNBT()
									.withTag("style", customizable.getStyle().serializeNBT()).unwrap()
							));
						else if(customizable instanceof Entity)
							IIPacketHandler.sendToServer(new MessageEntityNBTSync((Entity)customizable, EasyNBT.newNBT()
									.withTag("style", customizable.getStyle().serializeNBT()).unwrap()
							));
					})
			);
			if(style.getConstraints().getColorCustomization()!=PaintStyleConstraint.NOT_APPLICABLE)
				addComponent(new DecoColorPicker(32, 10+16+headInfo.getTotalHeight())
						.withWidth(width-32-4)
						.withColor(style.getColor())
						.withOnColorChanged(newColor -> {
							customizable.getStyle().withColor(newColor);
							//Notify the server side of the new color
							if(customizable instanceof TileEntityIEBase)
								IIPacketHandler.sendToServer(new MessageIITileSync((TileEntityIEBase)customizable, EasyNBT.newNBT()
										.withTag("style", customizable.getStyle().serializeNBT()).unwrap()
								));
							else if(customizable instanceof Entity)
								IIPacketHandler.sendToServer(new MessageEntityNBTSync((Entity)customizable, EasyNBT.newNBT()
										.withTag("style", customizable.getStyle().serializeNBT()).unwrap()
								));
						})
				);
			return true;
		}
		return false;

	}

	@Override
	public String getName()
	{
		return "style";
	}

	@Override
	public DecoTab provideTab()
	{
		return (DecoTab)new DecoTab()
				.withBackground(DecoTextures.COMPONENT_TAB_WIDGET)
				.withBackgroundColor(IIColor.fromPackedRGB(0x3c5e49))
				.withPadding(6, 2, 2, 2)
				.withIconAlignment(DecoAlignment.CENTER)
				//Engineer's Manual
				.withIcon(DecoTextures.ICON_STYLE)
				.withTranslatedTooltip(IIReference.GUI_TOOLTIP_KEY+"widget.style.show");
	}
}
