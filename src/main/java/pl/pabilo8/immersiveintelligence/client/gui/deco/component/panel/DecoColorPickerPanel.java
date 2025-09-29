package pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.EnumDyeColor;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoColorPicker;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * A configurable color picker component containing a Color Picker box and sliders/buttons, depending on color mode.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @implNote heavy resource usage, limit to when needed
 * @since 23.08.2025
 */
public class DecoColorPickerPanel extends DecoPanel
{
	protected List<IIColor> colorHistory = new ArrayList<>();
	protected int colorHistoryTimer = 10, getColorHistoryTimerMax = 10;
	protected BiConsumer<IIColor, IIColor> onColorChanged;
	protected IIColor color;
	//Text Fields
	protected DecoTextField rgb, hue, saturation, value;
	protected DecoButton colorDisplay;
	protected DecoDropdown<EnumDyeColor> dyeColor;
	private DecoColorPicker colorPicker;

	public DecoColorPickerPanel(int x, int y)
	{
		super(x, y);
	}

	public DecoColorPickerPanel withOnColorChanged(BiConsumer<IIColor, IIColor> onColorChanged)
	{
		this.onColorChanged = onColorChanged;
		return this;
	}

	public DecoColorPickerPanel withColor(IIColor value)
	{
		//Prevent recursive updates
		if(this.color==value)
			return this;

		if(onColorChanged!=null)
			this.onColorChanged.accept(this.color, value);
		this.color = value;
		setSubComponentsColor();

		return this;
	}

	@Override
	protected boolean initialize()
	{
		if(!super.initialize())
			return false;

		int square = Math.min(width, height)-8;
		int startingX = 3+square+10+4, startingY = 1;
		int remainingWidth = width-startingX-24-2, remainingHeight = height-startingY-2;

		//Color picker box
		addComponents(colorPicker = new DecoColorPicker(3, 3)
						.withSize(square+10, square)
						.withOnColorChanged(this::withColor),

				rgb = new DecoTextField(startingX+24, startingY+16)
						.withSize(remainingWidth-16, 16),
				dyeColor = new DecoDropdown<EnumDyeColor>(startingX+24, startingY+16+16)
						.withSize(remainingWidth, 12)
						.withDropdownWidth(remainingWidth)
						.withEntries(EnumDyeColor.values())
						.withDisplayFunction(new DecoEntryPanelBuilder<EnumDyeColor>()
								.withBackground(DecoTextures.GUI_BG_STEEL)
								.withHeight(12)
								.withComponent("icon", new DecoImage(2, 1)
										.withSize(8, 8)
										.withImageLocation(DecoTextures.RES_TEXTURES_DECO_COMPONENT_COLOR, true)
										.withUV(16, 4, 4, 12, 12)
								)
								.withLabel("label",
										new DecoLabel(IIClientUtils.fontRegular, 12, 1)
												.withSize(48, 12)
												.withAlign(DecoAlignment.LEFT)
												.withText("Core")
								)
								.withElementApplyMethod(this::drawColorEntry)
						)
						.withOnSelectedEntry((enumDyeColor, enumDyeColor2) -> {
							if(color.getDyeColor()!=enumDyeColor2)
								withColor(IIColor.fromDye(enumDyeColor2));
						}),
				colorDisplay = new DecoButton(rgb.x+rgb.width+2, rgb.y+1+2)
						.withSize(10, 10)
						.withBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_COLOR)
						.withOnPressed((gui, button, mouseX, mouseY) -> {
							this.onGuiEvent(DecoGuiEvent.COPY);
							return true;
						})
		);

		//Color components
		addComponentsRow(remainingWidth, 1, 16,
				hue = new DecoTextField(startingX+24, startingY).withFilter(TextFilter.DECIMAL),
				saturation = new DecoTextField(startingX+24, startingY).withFilter(TextFilter.DECIMAL),
				value = new DecoTextField(startingX+24, startingY).withFilter(TextFilter.DECIMAL)
		);

		//Mode labels
		addLabel("HSV:", startingX, startingY+4);
		addLabel("RGB:", startingX, startingY+16+4);
		addLabel("Dye:", startingX, startingY+16+16+2);

		setSubComponentsColor();
		return true;
	}

	private void drawColorEntry(EnumDyeColor dye, DecoEntryPanelBuilder<EnumDyeColor> builder)
	{
		builder.component("icon", DecoImage.class).withColor(IIColor.fromDye(dye));
		builder.label("label").withText("item.fireworksCharge."+dye.getUnlocalizedName());
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		super.draw(mouseX, mouseY, partialTicks);
		//Color button background

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		bindAtlas();
		IIDrawUtils.startTexturedColored()
				.drawConnectedTexColorRect(colorDisplay.x-2, colorDisplay.y-2, colorDisplay.width+4, colorDisplay.height+4,
						IIColor.WHITE, DecoTextures.RES_TEXTURES_DECO_COMPONENT_TEXT_FIELD, 32, 32, 8, 8)
				.finish();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();

		//Add current color to history

	}

	private void setSubComponentsColor()
	{
		if(colorPicker==null)
			return;

		//Set HSV
		float[] hsv = color.getHSV();
		hue.withText((int)(hsv[0]*255));
		saturation.withText((int)(hsv[1]*255));
		value.withText((int)(hsv[2]*255));
		//Set RGB
		rgb.withText(color.getHexRGB());
		//Set Dye Color
		dyeColor.withSelectedEntry(color.getDyeColor());
		//Set color picker box
		colorPicker.withColor(this.color);
		colorDisplay.withBackgroundColor(color);
	}

	@Override
	public void onGuiEvent(DecoGuiEvent event)
	{
		switch(event)
		{
			case CUT:
			case COPY:
				GuiScreen.setClipboardString(color.getHexRGB());
				break;
			case PASTE:
			{
				String string = GuiScreen.getClipboardString();
				if(string.length()!=6)
					break;
				withColor(IIColor.fromHex(string));
			}
			break;
			case UNDO:
			{
				if(colorHistory.isEmpty())
					break;
				withColor(colorHistory.remove(colorHistory.size()-1));
			}
			break;
			case REDO:
			{
				if(colorHistory.isEmpty())
					break;
				//withColor(colorHistory.remove(0));
			}
			break;
		}
	}
}
