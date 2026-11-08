package pl.pabilo8.immersiveintelligence.common.compat.jei.ingredients;

import blusunrize.immersiveengineering.client.ClientUtils;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * A recipe fluid tank with a shared display capacity and unchanged ingredient amounts.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.10.2026
 */
public class JEIFluidStackRenderer implements IIngredientRenderer<FluidStack>
{
	private final int width, height;
	private final double capacity;

	public JEIFluidStackRenderer(int width, int height, double capacity)
	{
		this.width = width;
		this.height = height;
		this.capacity = Math.max(1, capacity);
	}

	@Override
	public void render(Minecraft minecraft, int xPosition, int yPosition, @Nullable FluidStack ingredient)
	{
		if(ingredient==null||ingredient.amount <= 0)
			return;

		int filledHeight = Math.min(height, Math.max(1, (int)Math.ceil(height*(double)ingredient.amount/capacity)));
		ResourceLocation texture = ingredient.getFluid().getStill(ingredient);
		if(texture==null)
			texture = new ResourceLocation(minecraft.getTextureMapBlocks().getMissingSprite().getIconName());
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		ClientUtils.bindAtlas();
		IIDrawUtils.startTexturedColored()
				.drawRepeatedTexColorRect(xPosition, yPosition+height-filledHeight, width, filledHeight,
						IIColor.fromPackedRGB(ingredient.getFluid().getColor(ingredient)), texture, 16)
				.finish();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
	}

	@Override
	public List<String> getTooltip(Minecraft minecraft, FluidStack ingredient, ITooltipFlag tooltipFlag)
	{
		return Arrays.asList(ingredient.getLocalizedName(), TextFormatting.GRAY.toString()+ingredient.amount+" mB"+TextFormatting.RESET);
	}
}
