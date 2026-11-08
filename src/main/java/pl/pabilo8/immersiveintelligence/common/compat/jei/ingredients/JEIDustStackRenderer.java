package pl.pabilo8.immersiveintelligence.common.compat.jei.ingredients;

import blusunrize.immersiveengineering.client.ClientUtils;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.api.crafting.DustStack;
import pl.pabilo8.immersiveintelligence.api.crafting.DustUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 07.10.2026
 * @since 14.03.2026
 */
public class JEIDustStackRenderer implements IIngredientRenderer<DustStack>
{
	private final int width, height;
	private final double capacity;

	public JEIDustStackRenderer()
	{
		this(16, 16);
	}

	public JEIDustStackRenderer(int width, int height)
	{
		this(width, height, 0);
	}

	/**
	 * A positive capacity scales recipe tanks; zero retains the half-filled ingredient icon.
	 */
	public JEIDustStackRenderer(int width, int height, double capacity)
	{
		this.width = width;
		this.height = height;
		this.capacity = Math.max(0, capacity);
	}

	@Override
	public void render(Minecraft minecraft, int xPosition, int yPosition, @Nullable DustStack ingredient)
	{
		if(ingredient==null)
			return;
		if(ingredient.name.isEmpty()||ingredient.amount <= 0)
			return;

		int filledHeight = capacity==0?height/2:
				Math.min(height, Math.max(1, (int)Math.ceil(height*(double)ingredient.amount/capacity)));
		ClientUtils.bindAtlas();
		IIDrawUtils.startTexturedColored()
				.drawRepeatedTexColorRect(xPosition, yPosition+height-filledHeight, width, filledHeight,
						DustUtils.getColor(ingredient), DecoTextures.COMPONENT_TANK_DUST, 16)
				.finish();
	}

	@Override
	public List<String> getTooltip(Minecraft minecraft, DustStack ingredient, ITooltipFlag tooltipFlag)
	{
		return Arrays.asList(DustUtils.getDustName(ingredient), TextFormatting.GRAY.toString()+ingredient.amount+" mB"+TextFormatting.RESET);
	}
}
