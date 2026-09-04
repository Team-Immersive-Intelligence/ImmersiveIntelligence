package pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection;

import lombok.Value;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoLineSeparator;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoLineSeparator.LineDirection;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Displays titled and value-based information rows in a scrollable Deco list.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.09.2026
 */
public class DecoInformationList extends DecoList<DecoInformationList.InformationEntry>
{
	private static final int PADDING = 2;
	private static final int ICON_SIZE = 16;
	private static final int COLUMN_GAP = 2;
	private static final float NAME_COLUMN_RATIO = 0.62f;

	/**
	 * Creates an information list at the specified position.
	 *
	 * @param x x position
	 * @param y y position
	 */
	public DecoInformationList(int x, int y)
	{
		super(x, y);
		withListBackground(DecoTextures.BG_PAPER);
		withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_PAPER);
		withDisplayFunction(createDisplay());
	}

	@Override
	public DecoInformationList withSize(int width, int height)
	{
		super.withSize(width, height);
		return this;
	}

	@Override
	public DecoInformationList withWidth(int width)
	{
		super.withWidth(width);
		return this;
	}

	@Override
	public DecoInformationList withHeight(int height)
	{
		super.withHeight(height);
		return this;
	}

	@Override
	public DecoInformationList withEntries(Collection<InformationEntry> entries)
	{
		super.withEntries(entries);
		return this;
	}

	private DecoEntryPanelBuilder<InformationEntry> createDisplay()
	{
		return new DecoEntryPanelBuilder<InformationEntry>()
				.withHeight(0)
				.withBackground(DecoTextures.BG_PAPER)
				.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
				.withComponent("layout", this::createLayout)
				.withComponent("icon", this::createIcon)
				.withComponent("separator", this::createSeparator)
				.withLabel("title", this::createTitleLabel)
				.withLabel("text", this::createTextLabel)
				.withLabel("value", this::createValueLabel);
	}

	private DecoComponent<?> createLayout(DecoEntryPanelBuilder<InformationEntry> panel)
	{
		InformationEntry entry = panel.getCurrentElement();
		panel.withHeight(getContentHeight(entry, panel.width)+PADDING*2);
		return emptyComponent();
	}

	private DecoComponent<?> createIcon(DecoEntryPanelBuilder<InformationEntry> panel)
	{
		InformationEntry entry = panel.getCurrentElement();
		if(entry.isTitle()||entry.getIcon()==null)
			return emptyComponent();

		DecoImage image = new DecoImage(PADDING, PADDING)
				.withSize(ICON_SIZE, ICON_SIZE)
				.withImageLocation(entry.getIcon(), true);
		if(!entry.getTooltip().isEmpty())
			image.withOnTooltip(component -> entry.getTooltip());
		return image;
	}

	private DecoComponent<?> createSeparator(DecoEntryPanelBuilder<InformationEntry> panel)
	{
		InformationEntry entry = panel.getCurrentElement();
		if(entry.isTitle())
			return emptyComponent();

		int separatorX = getSeparatorX(panel.width);
		return new DecoLineSeparator(separatorX, PADDING)
				.withSize(0, getContentHeight(entry, panel.width))
				.withDirection(LineDirection.VERTICAL);
	}

	private DecoLabel createTitleLabel(DecoEntryPanelBuilder<InformationEntry> panel)
	{
		InformationEntry entry = panel.getCurrentElement();
		if(!entry.isTitle())
			return emptyLabel();
		return new DecoLabel(fontRenderer, PADDING, PADDING)
				.withSize(Math.max(0, panel.width-PADDING*2), fontRenderer.FONT_HEIGHT)
				.withAlign(DecoAlignment.TOP)
				.withRawText(entry.getText());
	}

	private DecoLabel createTextLabel(DecoEntryPanelBuilder<InformationEntry> panel)
	{
		InformationEntry entry = panel.getCurrentElement();
		if(entry.isTitle())
			return emptyLabel();

		int x = getTextX(entry);
		int width = getTextWidth(entry, panel.width);
		DecoLabel label = new DecoLabel(fontRenderer, x, PADDING)
				.withSize(width, getWrappedHeight(entry.getText(), width))
				.withAlign(DecoAlignment.TOP_LEFT)
				.withWrapping(true)
				.withRawText(entry.getText());
		if(entry.getIcon()==null&&!entry.getTooltip().isEmpty())
			label.withOnTooltip(entry::getTooltip);
		return label;
	}

	private DecoLabel createValueLabel(DecoEntryPanelBuilder<InformationEntry> panel)
	{
		InformationEntry entry = panel.getCurrentElement();
		if(entry.isTitle())
			return emptyLabel();

		int x = getSeparatorX(panel.width)+COLUMN_GAP;
		int width = Math.max(1, panel.width-x-PADDING);
		return new DecoLabel(fontRenderer, x, PADDING)
				.withSize(width, getWrappedHeight(entry.getValue(), width))
				.withAlign(DecoAlignment.CENTER)
				.withWrapping(true)
				.withRawText(entry.getValue())
				.withOnTooltip(entry::getTooltip);
	}

	private DecoComponent<?> emptyComponent()
	{
		DecoLineSeparator empty = new DecoLineSeparator(0, 0)
				.withSize(0, 0)
				.withDirection(LineDirection.VERTICAL);
		empty.setActive(false);
		return empty;
	}

	private DecoLabel emptyLabel()
	{
		return new DecoLabel(fontRenderer, 0, 0).withSize(0, 0).withRawText("");
	}

	private int getContentHeight(InformationEntry entry, int panelWidth)
	{
		if(entry.isTitle())
			return fontRenderer.FONT_HEIGHT;

		int textWidth = getTextWidth(entry, panelWidth);
		int valueWidth = Math.max(1, panelWidth-getSeparatorX(panelWidth)-COLUMN_GAP-PADDING);
		int iconHeight = entry.getIcon()==null?0: ICON_SIZE;
		return Math.max(fontRenderer.FONT_HEIGHT, Math.max(iconHeight, Math.max(
				getWrappedHeight(entry.getText(), textWidth),
				getWrappedHeight(entry.getValue(), valueWidth)
		)));
	}

	private int getTextX(InformationEntry entry)
	{
		return entry.getIcon()==null?PADDING: PADDING+ICON_SIZE+COLUMN_GAP;
	}

	private int getTextWidth(InformationEntry entry, int panelWidth)
	{
		return Math.max(1, getSeparatorX(panelWidth)-COLUMN_GAP-getTextX(entry));
	}

	private int getSeparatorX(int panelWidth)
	{
		return MathHelper.clamp(Math.round(panelWidth*NAME_COLUMN_RATIO), PADDING+1, panelWidth-PADDING-1);
	}

	private int getWrappedHeight(String text, int width)
	{
		if(text==null||text.isEmpty())
			return fontRenderer.FONT_HEIGHT;
		return fontRenderer.getWordWrappedHeight(text, Math.max(1, width));
	}

	/**
	 * Immutable row description used by {@link DecoInformationList}.
	 */
	@Value
	public static class InformationEntry
	{
		boolean title;
		String text;
		@Nullable
		ResourceLocation icon;
		String value;
		List<String> tooltip;

		/**
		 * Creates a centred title row.
		 */
		public static InformationEntry title(String text)
		{
			return new InformationEntry(true, text==null?"": text, null, "", Collections.emptyList());
		}

		/**
		 * Creates an information row.
		 */
		public static InformationEntry information(String text, @Nullable ResourceLocation icon, Object value, String... tooltip)
		{
			return new InformationEntry(
					false,
					text==null?"": text,
					icon,
					value==null?"": String.valueOf(value),
					Arrays.asList(tooltip)
			);
		}
	}
}
