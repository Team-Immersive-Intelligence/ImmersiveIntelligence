package pl.pabilo8.immersiveintelligence.client.gui.deco.component.button;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoTextBasedComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import java.util.function.Consumer;

/**
 * Used to select a number within a range by dragging a slider handle.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 22.08.2025
 * @ii-approved 0.3.1
 * @since 29.07.2021
 */
public class DecoSlider extends DecoTextBasedComponent<DecoSlider>
{
	private ResourceLocation sliderTop = DecoTextures.RES_TEXTURES_DECO_COMPONENT_SLIDER;
	private Consumer<Float> onValueChanged;
	protected float value = 0.5f, minValue = 0.0f, maxValue = 1.0f;
	//Whether the slider should only allow integer values
	private boolean integersOnly = false;
	//Whether it's a horizontal or vertical slider
	private boolean horizontal = true;

	public DecoSlider(int x, int y)
	{
		super(x, y);
		withSize(120, 12);
		withBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_SLIDER_BAR);
		this.withOnLMBPressed(() -> setFocused(true));
		this.withOnReleased((gui, button, mouseX, mouseY) -> {
			setFocused(false);
			return true;
		});
		this.withOnDragged((gui, button, mouseX, mouseY) -> {
			gui.value = ((mouseX-(gui.x+4))/(float)(gui.width-8))*(gui.maxValue-gui.minValue)+gui.minValue;
			gui.value = MathHelper.clamp(gui.integersOnly?Math.round(gui.value): gui.value, gui.minValue, gui.maxValue);

			if(onValueChanged!=null)
				onValueChanged.accept(gui.value);
			return true;
		});
	}

	@Override
	public DecoSlider withSize(int width, int height)
	{
		this.horizontal = width >= height;
		return super.withSize(width, height);
	}

	public DecoSlider withValue(float value)
	{
		this.value = value;
		return this;
	}

	public DecoSlider withRange(float minValue, float maxValue)
	{
		this.minValue = minValue;
		this.maxValue = maxValue;
		return this;
	}

	public DecoSlider withIntegersOnly(boolean integersOnly)
	{
		this.integersOnly = integersOnly;
		return this;
	}

	public DecoSlider withOnValueChanged(Consumer<Float> onValueChanged)
	{
		this.onValueChanged = onValueChanged;
		return this;
	}

	public float getValue()
	{
		return value;
	}

	@Override
	protected boolean initialize()
	{
		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		TextureAtlasSprite slider = ClientUtils.getSprite(DecoTextures.RES_TEXTURES_DECO_COMPONENT_SLIDER);
		bindAtlas();

		float percentage = value/maxValue;
		IIDrawUtils.startTexturedColored()
				//Draw the slider background
				.drawConnectedTexColorRect(x, y, width, height,
						IIColor.WHITE, backgroundLocation, 32, 32, 8, 8)
				//Slider handle
				.drawTexColorRect(x+2+(int)(percentage*(width-4)), y, 2, height,
						IIColor.WHITE, slider.getMinU(), slider.getInterpolatedU(2),
						slider.getMinV(), slider.getInterpolatedV(9))
				.finish();


		//IIColor textColor = getTextColor(true);
		//fontRenderer.drawString(text, x+(width/2), y-fontRenderer.FONT_HEIGHT-2, textColor.getPackedRGB());
		//fontRenderer.drawString(String.format("%.2f", value), x+(width/2), y+height+2, textColor.getPackedRGB());
	}

	@Override
	public void onGuiEvent(DecoGuiEvent event)
	{
		super.onGuiEvent(event);
		switch(event)
		{
			case COPY:
				GuiScreen.setClipboardString(String.format("%.2f", value));
				break;
			case PASTE:
			{
				String text = GuiScreen.getClipboardString();
				try
				{
					withValue(Float.parseFloat(text));
				} catch(NumberFormatException e)
				{
					// Ignore invalid input
				}
			}
			break;
		}
	}

	@Override
	public void cleanup()
	{

	}
}
