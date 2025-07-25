package pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoBase;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Displays an item stack inside a Deco GUI.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 10.07.2025
 **/
@ParametersAreNonnullByDefault
public class DecoItemStackDisplay extends GuiComponentDecoBase<DecoItemStackDisplay>
{
	private DecoAlignment iconAlignment = DecoAlignment.CENTER;
	private ItemStack stack = ItemStack.EMPTY;
	protected int[] padding = new int[]{2, 2, 2, 2};
	private int iconSize = 16;
	private int cachedIconX, cachedIconY;
	private TooltipFlags tooltipFlag;

	public DecoItemStackDisplay(int x, int y)
	{
		super(x, y);
		withForcedAdvancedTooltip(false);
	}

	public DecoItemStackDisplay withStack(ItemStack stack)
	{
		this.stack = stack;
		this.initialized = false;
		return this;
	}

	public DecoItemStackDisplay withIconAlignment(DecoAlignment iconAlignment)
	{
		this.iconAlignment = iconAlignment;
		return this;
	}

	public DecoItemStackDisplay withPadding(int[] padding)
	{
		this.padding = padding;
		return this;
	}

	public DecoItemStackDisplay withIconSize(int iconSize)
	{
		this.iconSize = iconSize;
		return this;
	}

	public DecoItemStackDisplay withForcedAdvancedTooltip(boolean forcedAdvancedTooltip)
	{
		this.tooltipFlag = forcedAdvancedTooltip||ClientUtils.mc().gameSettings.advancedItemTooltips?
				TooltipFlags.ADVANCED: TooltipFlags.NORMAL;
		this.initialized = false;
		return this;
	}

	@Override
	protected boolean initialize()
	{
		int xPadding = padding[0]+padding[2];
		int yPadding = padding[1]+padding[3];
		this.cachedIconX = iconAlignment.getAlignX(x+padding[0], width, width-xPadding);
		this.cachedIconY = iconAlignment.getAlignY(y+padding[1], height, height-yPadding);

		this.withOnTooltip(decoItemStackDisplay ->
				stack.getTooltip(ClientUtils.mc().player, tooltipFlag));

		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(cachedIconX, cachedIconY, 0);
		GlStateManager.scale(16/(float)iconSize, 16/(float)iconSize, 1);
		ClientUtils.mc().getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
		GlStateManager.popMatrix();
	}

	@Override
	public void cleanup()
	{

	}
}
