package pl.pabilo8.immersiveintelligence.client.gui.deco.component.button;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;

import java.util.function.Consumer;

/**
 * A QT-style color picker with main color area and brightness slider
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 23.08.2025
 */
public class DecoColorPicker extends DecoComponent<DecoColorPicker>
{
	private static final int BRIGHTNESS_WIDTH = 8;
	private static final int GAP = 2;

	private ResourceLocation background = DecoTextures.COMPONENT_SLIDER_BAR;

	private float hue = 0.0f;
	private float saturation = 0.99f;
	private float brightness = 0.5f;
	int backgroundBoxes = 0;

	//Cached color object - only recreated when HSB values change
	private IIColor selectedColor = IIColor.fromHSV(hue, saturation, brightness);

	private boolean draggingColor = false;
	private boolean draggingBrightness = false;

	private Consumer<IIColor> onColorChanged;

	public DecoColorPicker(int x, int y)
	{
		super(x, y);
		this.withSize(64, 64);
		this.withOnPressed((gui, mouseButton, mx, my) -> {
			if(mouseButton==MouseButton.LEFT)
			{
				int colorWidth = gui.width-BRIGHTNESS_WIDTH-GAP-2;
				int sliderX = gui.x+gui.width-BRIGHTNESS_WIDTH;

				//Hue picker
				if(IIMath.isPointInRectangle(gui.x+1, gui.y+1, gui.x+1+colorWidth, gui.y+height-2, mx, my))
				{
					gui.setHueSaturation((float)(mx-gui.x)/colorWidth, (float)(my-gui.y)/gui.height);
					gui.draggingColor = true;
					gui.setFocused(true);
					return true;
				}
				//Brightness slider
				else if(IIMath.isPointInRectangle(sliderX, gui.y, sliderX+BRIGHTNESS_WIDTH, gui.y+gui.height, mx, my))
				{
					gui.setBrightness(1.0f-(float)(my-gui.y)/gui.height);
					gui.draggingBrightness = true;
					gui.setFocused(true);
					return true;
				}
			}
			return false;
		});

		this.withOnReleased((gui, button, mouseX, mouseY) -> {
			gui.draggingColor = false;
			gui.draggingBrightness = false;
			gui.setFocused(false);
			return true;
		});

		this.withOnDragged((gui, button, mouseX, mouseY) -> {
			if(button==MouseButton.LEFT)
			{
				if(gui.draggingColor)
				{
					int colorWidth = gui.width-BRIGHTNESS_WIDTH-GAP;
					gui.setHueSaturation(
							MathHelper.clamp((float)(mouseX-gui.x)/colorWidth, 0f, 1f),
							MathHelper.clamp((float)(mouseY-gui.y)/gui.height, 0f, 1f)
					);
					return true;
				}

				if(gui.draggingBrightness)
				{
					gui.setBrightness(MathHelper.clamp(1.0f-(float)(mouseY-gui.y)/gui.height, 0f, 1f));
					return true;
				}
			}
			return false;
		});
	}

	public DecoColorPicker withBackground(ResourceLocation background)
	{
		this.background = background;
		return this;
	}

	public DecoColorPicker withColor(IIColor color)
	{
		float[] hsv = color.getHSV();
		setHSV(hsv[0], hsv[1], hsv[2]);
		return this;
	}

	public DecoColorPicker withOnColorChanged(Consumer<IIColor> onColorChanged)
	{
		this.onColorChanged = onColorChanged;
		return this;
	}

	public IIColor getSelectedColor()
	{
		return selectedColor;
	}

	private void setHueSaturation(float hue, float saturation)
	{
		if(this.hue!=hue||this.saturation!=saturation)
		{
			this.hue = MathHelper.clamp(hue, 0f, 1f);
			this.saturation = MathHelper.clamp(saturation, 0f, 1f);
			updateSelectedColor();
		}
	}

	private void setBrightness(float brightness)
	{
		if(this.brightness!=brightness)
		{
			this.brightness = MathHelper.clamp(brightness, 0.01f, 0.99f);
			updateSelectedColor();
		}
	}

	private void setHSV(float hue, float saturation, float brightness)
	{
		if(this.hue!=hue||this.saturation!=saturation||this.brightness!=brightness)
		{
			this.hue = hue;
			this.saturation = saturation;
			this.brightness = brightness;
			updateSelectedColor();
		}
	}

	private void updateSelectedColor()
	{
		selectedColor = IIColor.fromHSV(hue, saturation, brightness);
		initialized = false;
		cleanup();
		if(onColorChanged!=null)
			onColorChanged.accept(selectedColor);
	}

	@Override
	protected boolean initialize()
	{
		//Start drawing into a GLCallList
		int colorWidth = width-BRIGHTNESS_WIDTH-GAP;
		IIDrawUtils draw = IIDrawUtils.startColored().createCallList(true);

		//Draw the color box
		int steps = 32;
		for(int i = 0; i < steps; i++)
			for(int j = 0; j < steps; j++)
			{
				float h = (float)i/steps;
				float s = (float)j/steps;

				IIColor stepColor = IIColor.fromHSV(h, s, brightness);

				float x1 = x+(i*colorWidth/(float)steps);
				float y1 = y+(j*height/(float)steps);
				float w = colorWidth/(float)steps;
				float h1 = height/(float)steps;

				draw.drawColorRect(x1, y1, w, h1, stepColor);
			}

		return (backgroundBoxes = draw.finishCallList())!=-1;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		bindAtlas();
		IIDrawUtils.startTexturedColored()
				//Draw color box frame
				.drawConnectedTexColorRect(x, y, width-BRIGHTNESS_WIDTH-GAP, height, IIColor.WHITE, DecoTextures.COMPONENT_TEXT_FIELD, 32, 32, 8, 8)
				//Draw brightness slider frame
				.drawConnectedTexColorRect(x+width-BRIGHTNESS_WIDTH, y, BRIGHTNESS_WIDTH, height, IIColor.WHITE, DecoTextures.COMPONENT_TEXT_FIELD, 32, 32, 8, 8)
				.finish();

		//Disable textures for drawing the bar
		GlStateManager.disableTexture2D();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(GL11.GL_SMOOTH);

		//Draw colors
		GlStateManager.callList(backgroundBoxes);
		IIColor midBrightness = IIColor.fromHSV(hue, saturation, 0.5f);
		IIDrawUtils.startColored()
				.drawColorGradient(x+width-BRIGHTNESS_WIDTH, y, BRIGHTNESS_WIDTH, height/2, midBrightness, IIColor.WHITE)
				.drawColorGradient(x+width-BRIGHTNESS_WIDTH, y+(height/2f), BRIGHTNESS_WIDTH, height/2, IIColor.BLACK, midBrightness)
				.finish();
		//Re-enable textures
		GlStateManager.enableTexture2D();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();

		drawSelectionIndicators();
	}

	private void drawSelectionIndicators()
	{
		TextureAtlasSprite crosshair = ClientUtils.getSprite(DecoTextures.COMPONENT_SWITCH);
		bindAtlas();

		IIDrawUtils draw = IIDrawUtils.startTexturedColored();

		//Color picker crosshair
		int colorWidth = width-BRIGHTNESS_WIDTH-GAP;
		int crossX = (int)(x+hue*colorWidth);
		int crossY = (int)(y+saturation*height);

		draw.drawTexColorRect(crossX-2, crossY-2, 4, 4, IIColor.WHITE,
				crosshair.getMinU(), crosshair.getInterpolatedU(4),
				crosshair.getMinV(), crosshair.getInterpolatedV(4));

		//Brightness slider indicator
		int sliderX = x+width-BRIGHTNESS_WIDTH;
		int sliderY = (int)(y+(1.0f-brightness)*height);

		draw.drawTexColorRect(sliderX-1, sliderY-1, BRIGHTNESS_WIDTH+2, 2, IIColor.WHITE,
				crosshair.getMinU(), crosshair.getInterpolatedU(BRIGHTNESS_WIDTH+2),
				crosshair.getMinV(), crosshair.getInterpolatedV(2));

		draw.finish();
	}

	@Override
	public void cleanup()
	{
		if(backgroundBoxes!=-1)
			GlStateManager.glDeleteLists(backgroundBoxes, 1);
	}
}
