package pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoBase;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

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
	private List<ItemStack> stackList;
	private IngredientStack stack = new IngredientStack(ItemStack.EMPTY);
	private int maxTimer = 0;

	private int displayTime = 30;
	protected int[] padding = new int[]{2, 2, 2, 2};
	private DecoAlignment iconAlignment = DecoAlignment.CENTER;
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
		this.stack = new IngredientStack(stack);
		this.initialized = false;
		return this;
	}

	public DecoItemStackDisplay withStack(IngredientStack stack)
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

	public DecoItemStackDisplay withDisplayTime(int displayTime)
	{
		this.displayTime = displayTime;
		return this;
	}

	@Override
	protected boolean initialize()
	{
		int xPadding = padding[0]+padding[2];
		int yPadding = padding[1]+padding[3];
		this.cachedIconX = iconAlignment.getAlignX(x+padding[0], width, width-xPadding);
		this.cachedIconY = iconAlignment.getAlignY(y+padding[1], height, height-yPadding);

		this.stackList = stack.getStackList().isEmpty()?
				Collections.singletonList(ItemStack.EMPTY):
				stack.getStackList();
		this.maxTimer = (stackList.size())*displayTime;

		this.withOnTooltip(decoItemStackDisplay ->
				getCurrentlyDisplayedStack().getTooltip(ClientUtils.mc().player, tooltipFlag));

		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(cachedIconX, cachedIconY, 0);
		GlStateManager.scale(16/(float)iconSize, 16/(float)iconSize, 1);
		GlStateManager.enableDepth();
		GlStateManager.enableRescaleNormal();
		ClientUtils.mc().getRenderItem().renderItemAndEffectIntoGUI(getCurrentlyDisplayedStack(), 0, 0);
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableDepth();
		GlStateManager.popMatrix();
	}

	private ItemStack getCurrentlyDisplayedStack()
	{
		float progress = IIAnimationUtils.getDebugProgress(maxTimer, 0);
		return this.stackList.get((int)MathHelper.clamp(progress*this.stackList.size(), 0, stackList.size()-1));
	}

	@Override
	public void cleanup()
	{

	}

	@Nullable
	@Override
	public Object getProvidedIngredient()
	{
		return getCurrentlyDisplayedStack();
	}
}
