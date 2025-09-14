package pl.pabilo8.immersiveintelligence.client.gui.deco.component.widget;

import net.minecraft.client.resources.I18n;
import pl.pabilo8.immersiveintelligence.api.style.IStyleCustomizable;
import pl.pabilo8.immersiveintelligence.api.style.StyleCustomization;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 08.09.2025
 */
public class DecoStyleWidget extends DecoComponentWidgetBase<DecoStyleWidget>
{
	private final IStyleCustomizable tile;
	private final StyleCustomization style;

	public DecoStyleWidget(IStyleCustomizable tile)
	{
		super();
		this.tile = tile;
		this.style = tile.getStyle();
		withSize(128, 128+32);
		withBackground(DecoTextures.GUI_BG_PAPER);
		withBackgroundMask(DecoTextures.RES_TEXTURES_DECO_TEMPLATE_PAPER);
	}

	@Override
	protected boolean initialize()
	{
		if(super.initialize())
		{
			withTitleLabel(I18n.format(IIReference.GUI_TOOLTIP_KEY+"widget.style"), DecoAlignment.TOP);
			addLabel("Style", 2, 8+2);
			addComponent(new DecoDropdown<>(32, 8)
					.withWidth(width-32-4)
					.withEntries(style.getConstraints().getStyles().toArray(new String[0]))
					.withSelectedEntry(style.getStyle())
					.withBackground(DecoTextures.RES_TEXTURES_DECO_BUTTON_PAPER)
					.withDropdownSymbol(DecoTextures.RES_TEXTURES_DECO_COMPONENT_DROPDOWN_SYMBOL_PAPER)
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
				.withBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_TAB_WIDGET)
				.withBackgroundColor(IIColor.fromPackedRGB(0x3c5e49))
				.withPadding(6, 2, 2, 2)
				.withIconAlignment(DecoAlignment.CENTER)
				//Engineer's Manual
				.withIcon(DecoTextures.RES_ICON_STYLE)
				.withTranslatedTooltip(IIReference.GUI_TOOLTIP_KEY+"widget.style.show");
	}
}
