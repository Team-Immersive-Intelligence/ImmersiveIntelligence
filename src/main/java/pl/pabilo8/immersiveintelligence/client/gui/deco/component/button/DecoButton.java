package pl.pabilo8.immersiveintelligence.client.gui.deco.component.button;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoTextBase;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiUtils;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A standard button of the Deco GUI system
 *
 * @author Pabilo8
 * @since 10.01.2025
 */
public class DecoButton extends GuiComponentDecoTextBase<DecoButton>
{
	private DecoAlignment iconAlignment = DecoAlignment.CENTER;
	protected int[] padding = new int[]{2, 1, 2, 2};

	@Nullable
	private ResLoc icon;
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
		int minIconSize = Math.min(Math.min(width, height), 16);
		DecoAlignment align = iconAlignment==DecoAlignment.CENTER&&text!=null?DecoAlignment.LEFT: iconAlignment;

		int xPadding = padding[0]+padding[2];
		int yPadding = padding[1]+padding[3];

		if(icon==null)
			draw.drawTexColorRect(
					align.getAlignX(x+padding[0], minIconSize, width-xPadding),
					align.getAlignY(y+padding[1], minIconSize, height-yPadding),
					minIconSize, minIconSize, IIColor.WHITE, 0, 0, 1, 1);
		draw.finish();
		if(stack!=null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.scale(16f/minIconSize, 16f/minIconSize, 1);
			ClientUtils.mc().getRenderItem().renderItemAndEffectIntoGUI(stack,
					align.getAlignX(x+padding[0], minIconSize, width-xPadding),
					align.getAlignY(y+padding[1], minIconSize, height-yPadding)
			);
			GlStateManager.popMatrix();
		}

		if(text!=null)
		{
			fontRenderer.drawString(text,
					this.iconAlignment.getAlignX(x+padding[0], fontRenderer.getStringWidth(text), width-xPadding),
					this.iconAlignment.getAlignY(y+padding[1], fontRenderer.FONT_HEIGHT, height-yPadding),
					getTextColor(false).getPackedARGB());
		}
	}

	@Override
	public void cleanup()
	{

	}
}
