package pl.pabilo8.immersiveintelligence.client.gui.deco.component;

import net.minecraft.client.resources.I18n;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRenderer;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 16.01.2025
 **/
@SuppressWarnings("unchecked")
public abstract class GuiComponentDecoTextBase<TYPE extends GuiComponentDecoTextBase<? super TYPE>> extends GuiComponentDecoBase<TYPE>
{
	protected static final IIColor BACKGROUND = IIColor.fromHex("efefef");
	protected static final IIColor BACKGROUND_HOVERED = IIColor.WHITE;
	protected static final IIColor BACKGROUND_PRESSED = IIColor.MC_GRAY;
	protected static final IIColor BACKGROUND_DISABLED = IIColor.fromHex("404040");

	protected IIColor backgroundColor = BACKGROUND;
	protected IIColor backgroundColorHovered = BACKGROUND_HOVERED;
	protected IIColor backgroundColorPressed = BACKGROUND_PRESSED;
	protected IIColor backgroundColorDisabled = BACKGROUND_DISABLED;

	protected ResLoc backgroundLocation;
	protected String text = "";
	protected IIFontRenderer fontRenderer = IIClientUtils.fontRegular;
	protected IIColor textLabelColor = IIReference.COLOR_H1;
	protected IIColor textBoxColor = IIColor.fromHex("afafaf");
	protected IIColor textHoveredColor = IIColor.fromHex("cfcfcf");
	protected IIColor textPressedColor = IIReference.COLOR_IMMERSIVE_ORANGE;
	protected IIColor textDisabledColor = IIColor.BLACK;

	public GuiComponentDecoTextBase(int x, int y)
	{
		super(x, y);
	}

	//--- Properties ---//

	public TYPE withText(String text)
	{
		this.text = I18n.format(text);
		return (TYPE)this;
	}

	public TYPE withRawText(String text)
	{
		this.text = text;
		return (TYPE)this;
	}

	public TYPE withFontRenderer(IIFontRenderer fontRenderer)
	{
		this.fontRenderer = fontRenderer;
		return (TYPE)this;
	}

	public TYPE withBackground(ResLoc backgroundLocation)
	{
		this.backgroundLocation = backgroundLocation;
		return (TYPE)this;
	}

	public TYPE withBackgroundColor(IIColor color)
	{
		this.backgroundColor = color;
		this.backgroundColorDisabled = color.withBrightness(0.25f);
		this.backgroundColorHovered = color.withBrightness(0.75f);
		this.backgroundColorPressed = color.withBrightness(1f);

		return (TYPE)this;
	}

	public TYPE withTextColor(IIColor textLabelColor, IIColor textBoxColor)
	{
		this.textLabelColor = textLabelColor;
		this.textBoxColor = textBoxColor;
		return (TYPE)this;
	}

	public TYPE withTextHoveredColor(IIColor textHoveredColor)
	{
		this.textHoveredColor = textHoveredColor;
		return (TYPE)this;
	}

	public TYPE withTextPressedColor(IIColor textPressedColor)
	{
		this.textPressedColor = textPressedColor;
		return (TYPE)this;
	}

	public TYPE withTextDisabledColor(IIColor textDisabledColor)
	{
		this.textDisabledColor = textDisabledColor;
		return (TYPE)this;
	}

	//--- Utilities ---//

	protected final IIColor getBackgroundColor()
	{
		return enabled?(pressed?backgroundColorPressed: (hovered?backgroundColorHovered: backgroundColor)): backgroundColorDisabled;
	}

	protected final IIColor getTextColor(boolean label)
	{
		return enabled?(pressed?textPressedColor: (hovered?textHoveredColor: (label?textLabelColor: textBoxColor))): textDisabledColor;
	}
}
