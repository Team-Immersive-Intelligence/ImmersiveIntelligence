package pl.pabilo8.immersiveintelligence.client.gui.deco.component.button;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoTextBase;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiUtils;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

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
	protected int[] padding = new int[]{2, 2, 2, 2};
	private DecoAlignment iconAlignment = DecoAlignment.CENTER;
	// Cached positions
	private int cachedIconX, cachedIconY, cachedTextX, cachedTextY;

	@Nullable
	private ResourceLocation icon;
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
		return withIcon(icon, 16);
	}

	public DecoButton withIcon(@Nonnull ResourceLocation icon, int iconSize)
	{
		this.icon = icon;
		this.iconSize = iconSize;
		return this;
	}

	public DecoButton withIcon(@Nonnull ItemStack stack)
	{
		this.stack = stack;
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
	public boolean initialize()
	{
		int xPadding = padding[0]+padding[2];
		int yPadding = padding[1]+padding[3];

		int textWidth = text!=null?fontRenderer.getStringWidth(text): 0;
		int textHeight = text!=null?fontRenderer.FONT_HEIGHT: 0;
		int iconSize = (icon!=null||stack!=null)?this.iconSize: 0;
		int combinedWidth = iconSize+textWidth;
		int combinedHeight = Math.max(iconSize, textHeight);

		int alignedX = iconAlignment.getAlignX(x+padding[0], combinedWidth, width-xPadding);
		int alignedY = iconAlignment.getAlignY(y+padding[1], combinedHeight, height-yPadding);

		cachedIconX = alignedX;
		cachedIconY = alignedY;

		//Offset by icon width + spacing
		cachedTextX = cachedIconX+iconSize+(iconSize==0?0: 2);
		//Center text vertically
		cachedTextY = iconSize==0?alignedY: (cachedIconY+(iconSize-fontRenderer.FONT_HEIGHT)/2);


		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		bindAtlas();
		IIDrawUtils draw = IIDrawUtils.startTexturedColored();

		DecoGuiUtils.drawRepeatedRect(draw, x, y, width, height, backgroundLocation, getBackgroundColor(), 32, 8);
		if(icon!=null)
		{
			TextureAtlasSprite iconSprite = ClientUtils.getSprite(icon);
			draw.drawTexColorRect(cachedIconX, cachedIconY,
					iconSize, iconSize, getTextColor(false),
					iconSprite.getMinU(), iconSprite.getMaxU(), iconSprite.getMinV(), iconSprite.getMaxV());
		}
		draw.finish();

		if(stack!=null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(cachedIconX, cachedIconY, 0);
			GlStateManager.scale(16/(float)iconSize, 16/(float)iconSize, 1);
			ClientUtils.mc().getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
			GlStateManager.popMatrix();
		}

		if(text!=null)
			fontRenderer.drawString(text, cachedTextX, cachedTextY, getTextColor(false).getPackedARGB());
	}

	@Override
	public void cleanup()
	{

	}
}
