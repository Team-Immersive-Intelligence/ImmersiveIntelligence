package pl.pabilo8.immersiveintelligence.common.compat.jei;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.compat.jei.MultiblockRecipeWrapper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 07.05.2021
 */
public class IIMultiblockRecipeWrapper extends MultiblockRecipeWrapper
{
	protected final int energy, time;

	public IIMultiblockRecipeWrapper(MultiblockRecipe recipe)
	{
		super(recipe);
		time = recipe.getTotalProcessTime();
		energy = recipe.getTotalProcessEnergy();
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		if(!inputs.isEmpty())
			ingredients.setInputLists(VanillaTypes.ITEM, Arrays.asList(recipeInputs.clone()));
		if(!outputs.isEmpty())
			ingredients.setOutputLists(VanillaTypes.ITEM, Arrays.asList(recipeOutputs.clone()));
		if(!fluidInputs.isEmpty())
			ingredients.setInputs(VanillaTypes.FLUID, fluidInputs);
		if(!fluidOutputs.isEmpty())
			ingredients.setOutputs(VanillaTypes.FLUID, fluidOutputs);
	}

	public void drawEnergyTimeInfo(Minecraft minecraft, int x, int y)
	{
		minecraft.getTextureManager().bindTexture(IIRecipeCategory.texture);
		//time icon
		ClientUtils.drawTexturedRect(x, y-3, 14, 14, 51/256f, (51+14)/256f, 15/256f, (15+14)/256f);
		//energy icon
		ClientUtils.drawTexturedRect(x+64, y-3, 14, 14, 65/256f, (65+14)/256f, 15/256f, (15+14)/256f);

		//shift ? ticks : seconds
		String time = GuiScreen.isShiftKeyDown()?
				this.time+" t":
				Utils.formatDouble(this.time*0.05, "0.##")+" s";
		minecraft.fontRenderer.drawString(time, x+16, y, IIReference.COLOR_H2.getPackedRGB());

		String energy = GuiScreen.isShiftKeyDown()?
				this.energy+" IF":
				Utils.formatDouble(this.energy/(double)this.time, "0")+" IF/t";
		minecraft.fontRenderer.drawString(energy, x+64+16, y, IIReference.COLOR_H2.getPackedRGB());
	}
}
