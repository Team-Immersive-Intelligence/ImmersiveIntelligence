package pl.pabilo8.immersiveintelligence.client.gui.deco.component.button;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoTextBase;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiUtils;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A standard button of the Deco GUI system
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 10.01.2025
 */
public class DecoButton extends GuiComponentDecoTextBase<DecoButton>
{
	private DecoAlignment iconAlignment = DecoAlignment.CENTER;
	protected int[] padding = new int[]{2, 2, 2, 2};

	@Nullable
	private ResLoc icon;
	private int iconSize = 16;
	@Nullable
	private ItemStack stack;


	public DecoButton(int x, int y)
	{
		super(x, y);
		this.backgroundLocation = IIReference.RES_TEXTURES_DECO_COMPONENT_BUTTON;
	}

	public DecoButton withIcon(@Nonnull ResourceLocation icon)
	{
		this.icon = icon instanceof ResLoc?((ResLoc)icon): ResLoc.of(icon);
		return this;
	}

	public DecoButton withIcon(@Nonnull ResourceLocation icon, int iconSize)
	{
		this.icon = icon instanceof ResLoc?((ResLoc)icon): ResLoc.of(icon);
		this.iconSize = iconSize;
		return this;
	}

	public DecoButton withIcon(@Nonnull ItemStack stack)
	{
		this.stack = stack;
		return this;
	}

	public DecoButton withBackground(ResLoc background)
	{
		this.backgroundLocation = background;
		return this;
	}

	public DecoButton withIconAlignment(DecoAlignment iconAlignment)
	{
		this.iconAlignment = iconAlignment;
		return this;
	}

	public DecoButton withPadding(int left, int top, int right, int bottom)
	{
		this.padding = new int[]{left, top, right, bottom};
		return this;
	}

	@Override
	protected boolean initialize()
	{
		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		bindAtlas();
		IIDrawUtils draw = IIDrawUtils.startTexturedColored();

		DecoGuiUtils.drawRepeatedRect(draw, x, y, width, height, backgroundLocation, getBackgroundColor(), 32, 8);
		DecoAlignment align = iconAlignment==DecoAlignment.CENTER&&text!=null?DecoAlignment.LEFT: iconAlignment;
		if(icon!=null)
		{
			TextureAtlasSprite iconSprite = ClientUtils.getSprite(icon);
			draw.drawTexColorRect(
					getIconXOffset(align, 16), getIconYOffset(align, 16),
					iconSize, iconSize, getTextColor(false),
					iconSprite.getMinU(), iconSprite.getMaxU(), iconSprite.getMinV(), iconSprite.getMaxV());
		}
		draw.finish();
		if(stack!=null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(getIconXOffset(align, 16), getIconYOffset(align, 16), 0);
			GlStateManager.scale(16/(float)iconSize, 16/(float)iconSize, 1);
			ClientUtils.mc().getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
			GlStateManager.popMatrix();
		}

		if(text!=null)
		{
			int xPadding = padding[0]+padding[2];
			int yPadding = padding[1]+padding[3];
			fontRenderer.drawString(text,
					this.iconAlignment.getAlignX(x+padding[0], fontRenderer.getStringWidth(text), width-xPadding),
					this.iconAlignment.getAlignY(y+padding[1], fontRenderer.FONT_HEIGHT, height-yPadding),
					getTextColor(false).getPackedARGB());
		}
	}

	private int getIconXOffset(DecoAlignment align, int defaultIconSize)
	{
		int xPadding = padding[0]+padding[2];
		return align.getAlignX(x+padding[0], 16, width-xPadding)+(defaultIconSize-this.iconSize)/2;
	}

	private int getIconYOffset(DecoAlignment align, int defaultIconSize)
	{
		int yPadding = padding[1]+padding[3];
		return align.getAlignY(y+padding[1], 16, height-yPadding)+(defaultIconSize-this.iconSize)/2;
	}

	@Override
	public void cleanup()
	{

	}
}
