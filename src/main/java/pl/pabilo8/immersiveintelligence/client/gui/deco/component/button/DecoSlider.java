package pl.pabilo8.immersiveintelligence.client.gui.deco.component.button;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
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
	private ResourceLocation sliderTop = DecoTextures.COMPONENT_SWITCH_MOVING;
	private Consumer<Float> onValueChanged;
	protected float value = 0.5f, minValue = 0.0f, maxValue = 1.0f;
	//Whether the slider should only allow integer values
	private boolean integersOnly = false;
	//Whether it's a horizontal or vertical slider
	private boolean horizontal = true;
	private IIColor barLeft = null, barRight = null;

	public DecoSlider(int x, int y)
	{
		super(x, y);
		withSize(120, 12);
		withBackground(DecoTextures.COMPONENT_TEXT_FIELD);
		this.withOnPressed((gui, button, mouseX, mouseY) -> {
			handleMouse(gui, mouseX, mouseY);
			setFocused(true);
			return true;
		});
		this.withOnReleased((gui, button, mouseX, mouseY) -> {
			handleMouse(gui, mouseX, mouseY);
			setFocused(false);
			return true;
		});
		this.withOnDragged((gui, button, mouseX, mouseY) -> {
			handleMouse(gui, mouseX, mouseY);
			return true;
		});
	}

	private void handleMouse(DecoSlider gui, int mouseX, int mouseY)
	{
		if(horizontal)
			gui.value = ((mouseX-(gui.x+4))/(float)(gui.width-8))*(gui.maxValue-gui.minValue)+gui.minValue;
		else
			gui.value = ((mouseY-(gui.y+4))/(float)(gui.height-8))*(gui.maxValue-gui.minValue)+gui.minValue;
		gui.value = MathHelper.clamp(gui.integersOnly?Math.round(gui.value): gui.value, gui.minValue, gui.maxValue);

		if(onValueChanged!=null)
			onValueChanged.accept(gui.value);
	}

	//--- Setters ---//

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

	public DecoSlider withBarColors(IIColor barLeft, IIColor barRight)
	{
		this.barLeft = barLeft;
		this.barRight = barRight;
		return this;
	}

	//--- Getters ---//

	public float getValue()
	{
		return value;
	}

	//--- Drawing ---//

	@Override
	protected boolean initialize()
	{
		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		TextureAtlasSprite slider = ClientUtils.getSprite(sliderTop);
		bindAtlas();

		float percentage = value/maxValue;
		IIDrawUtils.startTexturedColored()
				//Draw the slider background
				.drawConnectedTexColorRect(x, y, width, height,
						IIColor.WHITE, backgroundLocation, 32, 32, 8, 8)
				//Slider handle
				.drawTexColorRect(x+2+(int)(percentage*(width-8)), y, 4, height,
						IIColor.WHITE, slider.getMinU(), slider.getInterpolatedU(4),
						slider.getMinV(), slider.getInterpolatedV(9))
				.finish();

		//Draw percentage gradient
		if(barLeft!=null&&barRight!=null)
		{
			//Disable textures
			GlStateManager.disableTexture2D();
			GlStateManager.disableAlpha();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.shadeModel(GL11.GL_SMOOTH);

			IIColor colorRight = barLeft.mixedWith(barRight, percentage);
			IIDrawUtils.startColored()
					.drawColorGradient(x+2, y+2, (int)(percentage*(width-8)), height-4,
							barLeft, colorRight, barLeft, colorRight)
					.finish();

			//Re-enable textures
			GlStateManager.enableTexture2D();
			GlStateManager.shadeModel(GL11.GL_FLAT);
			GlStateManager.disableBlend();
			GlStateManager.enableAlpha();
		}
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
