package pl.pabilo8.immersiveintelligence.common.compat.jei.ingredients;

import blusunrize.immersiveengineering.client.ClientUtils;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
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
 * @since 14.03.2026
 */
public class JEIDustStackRenderer implements IIngredientRenderer<DustStack>
{
	private final int width, height;

	public JEIDustStackRenderer()
	{
		this.width = this.height = 16;
	}

	public JEIDustStackRenderer(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

	@Override
	public void render(Minecraft minecraft, int xPosition, int yPosition, @Nullable DustStack ingredient)
	{
		if(ingredient==null)
			return;
		if(ingredient.name.isEmpty())
			return;

		ClientUtils.bindAtlas();
		IIDrawUtils.startTexturedColored()
				.drawRepeatedTexColorRect(xPosition, yPosition+(height/2), width, height/2,
						DustUtils.getColor(ingredient), DecoTextures.COMPONENT_TANK_DUST, 16)
				.finish();
	}

	@Override
	public List<String> getTooltip(Minecraft minecraft, DustStack ingredient, ITooltipFlag tooltipFlag)
	{
		return Arrays.asList(DustUtils.getDustName(ingredient), ingredient.amount+" mB");
	}
}
