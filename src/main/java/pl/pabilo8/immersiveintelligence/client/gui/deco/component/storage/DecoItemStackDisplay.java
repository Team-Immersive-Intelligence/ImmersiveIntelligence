package pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Displays an item stack inside a Deco GUI.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 10.07.2025
 **/
@ParametersAreNonnullByDefault
public class DecoItemStackDisplay extends DecoComponent<DecoItemStackDisplay>
{
	private List<ItemStack> stackList;
	private IngredientStack stack = new IngredientStack(ItemStack.EMPTY);
	private int maxTimer = 0;

	private int displayTime = 30;
	protected int[] padding = new int[]{2, 2, 2, 2};
	private ResourceLocation backgroundTexture;
	private Supplier<Float> progressBarValue;
	private IIColor barGradientColor1 = IIColor.fromPackedRGB(0xb51500), barGradientColor2 = IIColor.fromPackedRGB(0x600b00);
	private DecoAlignment iconAlignment = DecoAlignment.CENTER;
	private int iconSize = 16;
	private int cachedIconX, cachedIconY;
	private TooltipFlags tooltipFlag;

	public DecoItemStackDisplay(int x, int y)
	{
		super(x, y);
		withForcedAdvancedTooltip(false);
		withOnTooltip(this::onDisplayTooltip);
	}

	public DecoItemStackDisplay withStack(ItemStack stack)
	{
		this.stack = new IngredientStack(stack);
		this.stackList = null;
		this.initialized = false;
		return this;
	}

	public DecoItemStackDisplay withStack(IngredientStack stack)
	{
		this.stack = stack;
		this.stackList = null;
		this.initialized = false;
		return this;
	}

	public DecoItemStackDisplay withProgressBar(Supplier<Float> progressBarValue, IIColor barGradientColor1, IIColor barGradientColor2)
	{
		this.progressBarValue = progressBarValue;
		this.barGradientColor1 = barGradientColor1;
		this.barGradientColor2 = barGradientColor2;
		return this;
	}

	public DecoItemStackDisplay withBackgroundTexture(ResourceLocation backgroundTexture)
	{
		this.backgroundTexture = backgroundTexture;
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
		this.cachedIconX = iconAlignment.getAlignX(x+padding[0], iconSize, width-xPadding);
		this.cachedIconY = iconAlignment.getAlignY(y+padding[1], iconSize, height-yPadding);

		this.stackList = stack.getStackList().isEmpty()?
				Collections.singletonList(ItemStack.EMPTY):
				stack.getStackList();
		this.maxTimer = (stackList.size())*displayTime;

		return true;
	}

	private Collection<String> onDisplayTooltip(DecoItemStackDisplay gui)
	{
		ItemStack stack = getCurrentlyDisplayedStack();
		//Do not display tooltip for air
		if(stack.isEmpty())
			return Collections.emptyList();

		List<String> tooltip = stack.getTooltip(ClientUtils.mc().player, tooltipFlag);
		//Set default gray formatting for description
		for(int i = 1; i < tooltip.size(); i++)
			tooltip.set(i, TextFormatting.GRAY+tooltip.get(i));
		return tooltip;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		GlStateManager.pushMatrix();

		if(backgroundTexture!=null)
		{
			bindAtlas();
			IIDrawUtils draw = IIDrawUtils.startTexturedColored();
			if(progressBarValue!=null)
				draw.drawConnectedTexColorRect(x+width-padding[0]-padding[2], y-padding[1], 6, height, IIColor.WHITE, backgroundTexture, 32, 32, 2, 2);
			draw.drawConnectedTexColorRect(x-padding[0], y-padding[1], width, height, IIColor.WHITE, backgroundTexture, 32, 32, 8, 8)
					.finish();
		}
		if(progressBarValue!=null)
		{
			float progress = MathHelper.clamp(progressBarValue.get(), 0, 1);
			IIDrawUtils.startColored()
					.drawColorGradient(
							x+width-padding[0]-padding[2]+2, y-padding[1]-padding[3]+height-(int)((height-4)*progress),
							2, (int)((height-4)*progress),
							barGradientColor1, barGradientColor2
					)
					.finish();
		}

		GlStateManager.translate(cachedIconX, cachedIconY, 0);
		GlStateManager.scale(iconSize*0.0625f, iconSize*0.0625f, 1);
		GlStateManager.enableDepth();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		RenderHelper.enableGUIStandardItemLighting();
		ClientUtils.mc().getRenderItem().renderItemAndEffectIntoGUI(getCurrentlyDisplayedStack(), 0, 0);
		ClientUtils.mc().getRenderItem().renderItemOverlayIntoGUI(IIClientUtils.fontRegular, getCurrentlyDisplayedStack(), 0, 0, null);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableDepth();
		GlStateManager.popMatrix();
	}

	private ItemStack getCurrentlyDisplayedStack()
	{
		float progress = AMTUtils.getDebugProgress(maxTimer, 0);
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
