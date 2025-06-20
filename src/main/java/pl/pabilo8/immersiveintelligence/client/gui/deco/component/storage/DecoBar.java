package pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoBase;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.function.Supplier;

/**
 * Draws a value bar with an optional icon. Used for drawing stored energy bars.
 * Some methods are package-protected, so they can be used by DecoBarGroup.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 19.06.2025
 * @since 16.07.2021
 */
@ParametersAreNonnullByDefault
public class DecoBar extends GuiComponentDecoBase<DecoBar>
{
	protected ResLoc backgroundLocation, iconBackgroundLocation;
	@Nullable
	protected ResLoc iconLocation;
	private IIColor colorTop, colorBottom;
	private int minValue = 0, interpolatedValue = 0, maxValue = 2;
	private Supplier<Integer> valueSupplier = () -> 1;
	private boolean smoothAnimation = false;

	public DecoBar(int x, int y)
	{
		super(x, y);
		withSize(12, 64+7);
		withColors(IIColor.BLACK, IIColor.WHITE);
		withBackgroundLocation(IIReference.RES_TEXTURES_DECO_COMPONENT_FRAME, IIReference.RES_TEXTURES_DECO_BAR_ICON_BACKGROUND);
	}

	@Deprecated
	public DecoBar(int x, int y, int w, int h, IIColor colorTop, IIColor colorBottom)
	{
		this(x, y);
		withSize(w, h);
		withColors(colorTop, colorBottom);
	}

	//--- Setters ---//

	public DecoBar withBackgroundLocation(ResLoc backgroundLocation, ResLoc iconBackgroundLocation)
	{
		this.backgroundLocation = backgroundLocation;
		this.iconBackgroundLocation = iconBackgroundLocation;
		return this;
	}

	public DecoBar withIconBackgroundLocation(ResLoc iconBackgroundLocation)
	{
		this.iconBackgroundLocation = iconBackgroundLocation;
		return this;
	}

	public DecoBar withIconLocation(ResLoc iconLocation)
	{
		this.iconLocation = iconLocation;
		return this;
	}

	public DecoBar withColors(IIColor colorTop, IIColor colorBottom)
	{
		this.colorTop = colorTop;
		this.colorBottom = colorBottom;
		return this;
	}

	public DecoBar withColor(IIColor color)
	{
		this.colorTop = this.colorBottom = color;
		return this;
	}

	public DecoBar withSmoothAnimation()
	{
		this.smoothAnimation = true;
		return this;
	}

	public DecoBar withLimits(int min, int max, Supplier<Integer> current)
	{
		this.minValue = min;
		this.maxValue = max;
		return withValueSupplier(current);
	}

	public DecoBar withValueSupplier(Supplier<Integer> currentValue)
	{
		this.valueSupplier = currentValue;
		return this;
	}

	public DecoBar withValueTooltip(String text, BarTooltipFormat format, TextFormatting color)
	{
		switch(format)
		{
			case VALUE:
				return this.withOnTooltip(
						decoBar -> Collections.singleton(I18n.format(IIReference.GUI_TOOLTIP_KEY+text, color.toString()+decoBar.getCurrentValue()+TextFormatting.RESET))
				);
			case VALUE_TO_MAX:
				return this.withOnTooltip(
						decoBar -> Collections.singleton(I18n.format(IIReference.GUI_TOOLTIP_KEY+text,
								color.toString()+decoBar.getCurrentValue()+TextFormatting.RESET,
								color.toString()+decoBar.getMaxValue()+TextFormatting.RESET
						))
				);
			case VALUE_WITH_LIMITS:
				return this.withOnTooltip(
						decoBar -> Collections.singleton(I18n.format(IIReference.GUI_TOOLTIP_KEY+text,
								color.toString()+decoBar.getCurrentValue()+TextFormatting.RESET,
								color.toString()+decoBar.getMinValue()+TextFormatting.RESET,
								color.toString()+decoBar.getMaxValue()+TextFormatting.RESET
						))
				);
		}
		return this;
	}

	//--- Getters ---//

	public int getMinValue()
	{
		return minValue;
	}

	public int getMaxValue()
	{
		return maxValue;
	}

	public int getCurrentValue()
	{
		return valueSupplier.get();
	}

	@Override
	protected boolean initialize()
	{
		if(smoothAnimation)
			interpolatedValue = valueSupplier.get();
		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		bindAtlas();

		//Draw the bar background
		IIDrawUtils draw = IIDrawUtils.startTexturedColored();
		drawBarBackground(draw);
		//Draw the icon with its background
		drawIcon(draw, true);
		draw.finish();


		//Disable textures for drawing the bar
		GlStateManager.disableTexture2D();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(GL11.GL_SMOOTH);

		//Draw the bar gradient
		draw = IIDrawUtils.startColored();
		drawBarGradient(draw);
		draw.finish();

		//Re-enable textures
		GlStateManager.enableTexture2D();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
	}

	void drawBarBackground(IIDrawUtils draw)
	{
		TextureAtlasSprite bgSprite = ClientUtils.getSprite(backgroundLocation);
		draw.drawConnectedColorRect(
				x, y+8, width, (((height&1)==0)?height: height+1)-8,
				IIColor.WHITE, 64, 64, 8, 8,
				bgSprite.getMinU(), bgSprite.getMaxU(),
				bgSprite.getMinV(), bgSprite.getMaxV()
		);
	}

	void drawBarGradient(IIDrawUtils draw)
	{
		int totalHeight = (((height&1)==0)?height: height+1)-4-8;

		//Interpolate the value, if smooth animation is enabled
		if(smoothAnimation)
			interpolatedValue = (int)Math.ceil(interpolatedValue+(getCurrentValue()-interpolatedValue)*0.1f);
		else
			interpolatedValue = getCurrentValue();

		//Draw the bar, don't draw outside of bounds
		float barHeight = totalHeight*MathHelper.clamp((interpolatedValue-minValue)/(float)(maxValue-minValue), 0f, 1f);
		draw.drawColorGradient(x+2, y+8+2+totalHeight-barHeight, width-4, (int)barHeight, colorBottom, colorTop);
	}

	void drawIcon(IIDrawUtils draw, boolean drawBackground)
	{
		if(iconLocation==null)
			return;

		//Draw the icon background
		if(drawBackground)
		{
			TextureAtlasSprite iconBgSprite = ClientUtils.getSprite(iconBackgroundLocation);
			draw.drawConnectedColorRect(
					x+(width*0.5f)-9, y-9, 18, 18, IIColor.WHITE,
					32, 32, 4, 4,
					iconBgSprite.getMinU(), iconBgSprite.getMaxU(),
					iconBgSprite.getMinV(), iconBgSprite.getMaxV()
			);
		}
		//and the icon
		TextureAtlasSprite iconSprite = ClientUtils.getSprite(iconLocation);
		draw.drawTexColorRect(
				x+(width*0.5f)-8, y-8, 16, 16, IIColor.WHITE,
				iconSprite.getMinU(), iconSprite.getMaxU(),
				iconSprite.getMinV(), iconSprite.getMaxV()
		);
	}

	@Override
	public void cleanup()
	{

	}

	/**
	 * Used by {@link DecoBarGroup}
	 *
	 * @return The background texture location for the bar
	 */
	ResourceLocation getIconBackgroundLocation()
	{
		return iconBackgroundLocation;
	}

	public enum BarTooltipFormat
	{
		VALUE,
		VALUE_TO_MAX,
		VALUE_WITH_LIMITS,
	}
}
