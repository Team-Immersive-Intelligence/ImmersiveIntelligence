package pl.pabilo8.immersiveintelligence.common.compat.jei.recipe_handlers;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.DustStack;
import pl.pabilo8.immersiveintelligence.api.crafting.DustUtils;
import pl.pabilo8.immersiveintelligence.api.crafting.FillerRecipe;
import pl.pabilo8.immersiveintelligence.client.gui.block.GuiFiller;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.compat.jei.IIMultiblockRecipeWrapper;
import pl.pabilo8.immersiveintelligence.common.compat.jei.IIRecipeCategory;
import pl.pabilo8.immersiveintelligence.common.compat.jei.recipe_handlers.FillerRecipeCategory.FillerRecipeWrapper;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.ArrayList;
import java.util.List;

public class FillerRecipeCategory extends IIRecipeCategory<FillerRecipe, FillerRecipeWrapper>
{
	static ItemStack machineStack;

	public FillerRecipeCategory(IGuiHelper helper)
	{
		super("filler",
				"tile."+ImmersiveIntelligence.MODID+".metal_multiblock1.filler.name",
				helper.createBlankDrawable(156, 74),
				FillerRecipe.class,
				new ItemStack(IIContent.blockMetalMultiblock1, 1, MetalMultiblocks1.FILLER.getMeta())
		);
		machineStack = new ItemStack(IIContent.blockMetalMultiblock1, 1, MetalMultiblocks1.FILLER.getMeta());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, FillerRecipeWrapper recipeWrapper, IIngredients ingredients)
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, 5, 20);
		guiItemStacks.init(1, false, 135, 20);
		guiItemStacks.init(2, false, 104, 4);

		guiItemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
		guiItemStacks.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
		guiItemStacks.set(2, DustUtils.getDustStacks(recipeWrapper.dust.name));

	}

	@Override
	public IRecipeWrapper getRecipeWrapper(FillerRecipe recipe)
	{
		return new FillerRecipeWrapper(recipe);
	}

	public static class FillerRecipeWrapper extends IIMultiblockRecipeWrapper
	{
		public DustStack dust;

		public FillerRecipeWrapper(FillerRecipe recipe)
		{
			super(recipe);
			this.dust = recipe.dust.copy();
		}

		@Override
		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
		{
			ClientUtils.drawSlot(6, 21, 16, 16);

			GlStateManager.pushMatrix();

			ClientUtils.bindTexture(GuiFiller.TEXTURE);
			ClientUtils.drawTexturedRect(39, 0, 84, 64, 54/256f, (54+84)/256f, 0/256f, (64)/256f);
			int stored = (int)(60*0.5); //
			float[] rgb = DustUtils.getColor(dust).getFloatRGB();

			GlStateManager.color(rgb[0], rgb[1], rgb[2]);
			ClientUtils.drawTexturedRect(41, 2+stored, 80, stored, 176/256f, (256)/256f, stored/256f, 60/256f);
			//ClientUtils.drawTexturedRect(0, 0, 64, 64-stored, 176/256f, (256)/256f, 0/256f, (79-stored)/256f);
			GlStateManager.color(1f, 1f, 1f);

			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0, 100f);
			ClientUtils.drawSlot(136, 21, 16, 16);
			GlStateManager.popMatrix();

			drawEnergyTimeInfo(minecraft, 0, recipeHeight-10);
		}

		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY)
		{
			if(IIMath.isPointInRectangle(41, 30, 101, 64, mouseX, mouseY))
			{
				List<String> tooltip = new ArrayList<>();
				tooltip.add(DustUtils.getDustName(dust));
				tooltip.add(dust.amount+" mB");
				return tooltip;
			}

			return super.getTooltipStrings(mouseX, mouseY);
		}

		@Override
		public void drawEnergyTimeInfo(Minecraft minecraft, int x, int y)
		{
			minecraft.getTextureManager().bindTexture(IIRecipeCategory.texture);
			//time icon
			ClientUtils.drawTexturedRect(x, y-3, 14, 14, 51/256f, (51+14)/256f, 15/256f, (15+14)/256f);
			//energy icon
			ClientUtils.drawTexturedRect(x+104, y-3, 14, 14, 65/256f, (65+14)/256f, 15/256f, (15+14)/256f);

			//shift ? ticks : seconds
			String time = GuiScreen.isShiftKeyDown()?
					this.time+" t":
					Utils.formatDouble(this.time*0.05, "0.##")+" s";
			minecraft.fontRenderer.drawString(time, x+16, y, IIReference.COLOR_H2.getPackedRGB());

			String energy = GuiScreen.isShiftKeyDown()?
					this.energy+" IF":
					Utils.formatDouble(this.energy/(double)this.time, "0")+" IF/t";
			minecraft.fontRenderer.drawString(energy, x+104+16, y, IIReference.COLOR_H2.getPackedRGB());
		}
	}
}