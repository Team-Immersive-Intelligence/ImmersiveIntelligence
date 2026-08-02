package pl.pabilo8.immersiveintelligence.client.gui.deco.component.button;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRenderer;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nonnull;

/**
 * A standard inventory tab of the Deco GUI system
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 12.01.2025
 */
public class DecoTab extends DecoButton
{
	private boolean isSelected = false;
	protected IIColor textSelectedColor = IIColor.fromHex("737373");
	protected IIColor backgroundSelectedColor = IIColor.fromHex("b4b4b4");

	public DecoTab()
	{
		super(0, 0);
		withBackground(DecoTextures.COMPONENT_TAB);
		withPadding(6, 1, 4, 3);
	}

	//--- DecoTab ---//

	public DecoTab withLink(IIGUI link)
	{
		this.withOnPressed((gui, mouseButton, mx, my) -> {
			if(mouseButton!=MouseButton.LEFT)
				return false;

			DecoGui<?, ?> parent = gui.getParentGui();
			if(parent==null)
				IILogger.error("DecoTab: No parent gui found, cannot link to "+link);
			else
				parent.changeGUI(link);
			return true;
		});
		return this;
	}

	public DecoTab withSelected(boolean selected)
	{
		isSelected = selected;
		return this;
	}

	public DecoTab withBackgroundSelectedColor(IIColor backgroundSelectedColor)
	{
		this.backgroundSelectedColor = backgroundSelectedColor;
		return this;
	}

	public DecoTab withTextSelectedColor(IIColor textSelectedColor)
	{
		this.textSelectedColor = textSelectedColor;
		return this;
	}

	//--- Overrides ---//

	@Override
	public DecoTab withIcon(@Nonnull ResourceLocation icon)
	{
		return (DecoTab)super.withIcon(icon);
	}

	@Override
	public DecoTab withIcon(@Nonnull ResourceLocation icon, int iconSize)
	{
		return (DecoTab)super.withIcon(icon, iconSize);
	}

	@Override
	public DecoTab withIcon(@Nonnull ItemStack stack)
	{
		return (DecoTab)super.withIcon(stack);
	}

	@Override
	public DecoTab withIconAlignment(DecoAlignment iconAlignment)
	{
		return (DecoTab)super.withIconAlignment(iconAlignment);
	}

	@Override
	public DecoTab withPadding(int left, int top, int right, int bottom)
	{
		return (DecoTab)super.withPadding(left, top, right, bottom);
	}

	@Override
	public DecoTab pack()
	{
		return (DecoTab)super.pack();
	}

	@Override
	public DecoTab withText(String text)
	{
		return (DecoTab)super.withText(text);
	}

	@Override
	public DecoTab withRawText(String text)
	{
		return (DecoTab)super.withRawText(text);
	}

	@Override
	public DecoTab withFontRenderer(IIFontRenderer fontRenderer)
	{
		return (DecoTab)super.withFontRenderer(fontRenderer);
	}

	@Override
	public DecoTab withBackground(ResLoc backgroundLocation)
	{
		return (DecoTab)super.withBackground(backgroundLocation);
	}

	@Override
	public DecoTab withBackgroundColor(IIColor color)
	{
		withBackgroundSelectedColor(color);
		return (DecoTab)super.withBackgroundColor(color);
	}

	@Override
	public DecoTab withTextColor(IIColor textLabelColor, IIColor textBoxColor)
	{
		withTextSelectedColor(textBoxColor);
		return (DecoTab)super.withTextColor(textLabelColor, textBoxColor);
	}

	@Override
	public DecoTab withTextHoveredColor(IIColor textHoveredColor)
	{
		return (DecoTab)super.withTextHoveredColor(textHoveredColor);
	}

	@Override
	public DecoTab withTextPressedColor(IIColor textPressedColor)
	{
		return (DecoTab)super.withTextPressedColor(textPressedColor);
	}

	@Override
	public DecoTab withTextDisabledColor(IIColor textDisabledColor)
	{
		return (DecoTab)super.withTextDisabledColor(textDisabledColor);
	}

	protected final IIColor getBackgroundColor()
	{
		if(!isSelected)
			return backgroundSelectedColor;
		return super.getBackgroundColor();
	}

	protected final IIColor getTextColor(boolean label)
	{
		if(!isSelected)
			return textSelectedColor;
		return super.getTextColor(label);
	}
}
