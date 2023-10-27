package pl.pabilo8.immersiveintelligence.common.compat.jei.recipe_handlers;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.compat.jei.JEIHelper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.BathingRecipe;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.compat.jei.IIMultiblockRecipeWrapper;
import pl.pabilo8.immersiveintelligence.common.compat.jei.IIRecipeCategory;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.List;

public class BathingRecipeCategory extends IIRecipeCategory<BathingRecipe, BathingRecipeCategory.BathingRecipeWrapper>
{
	private final IDrawable tankOverlay;
	static ItemStack machineStack;

	public BathingRecipeCategory(IGuiHelper helper, boolean washing)
	{
		super(washing?"washing": "bathing",
				washing?IIReference.DESCRIPTION_KEY+"jei.washing_recipe": "tile."+ImmersiveIntelligence.MODID+".metal_multiblock.chemical_bath.name"
				, helper.createBlankDrawable(140, 64), BathingRecipe.class, new ItemStack(IIContent.blockMetalMultiblock0, 1, MetalMultiblocks0.CHEMICAL_BATH.getMeta()));
		tankOverlay = helper.drawableBuilder(texture, 0, 52, 20, 51)
				.addPadding(-2, 2, -2, 2)
				.build();
		machineStack = new ItemStack(IIContent.blockMetalMultiblock0, 1, MetalMultiblocks0.CHEMICAL_BATH.getMeta());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, BathingRecipeWrapper recipeWrapper, IIngredients ingredients)
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(0, true, 0, 12);
		guiItemStacks.init(1, false, 100, 12);
		guiItemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
		guiItemStacks.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
		guiItemStacks.setBackground(0, JEIHelper.slotDrawable);
		guiItemStacks.setBackground(1, JEIHelper.slotDrawable);

		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
		List<FluidStack> lfs = ingredients.getInputs(VanillaTypes.FLUID).get(0);
		guiFluidStacks.init(0, true, 75, 0, 16, 47, lfs.get(0).amount*4, false, tankOverlay);
		guiFluidStacks.set(0, lfs);
		guiFluidStacks.addTooltipCallback(JEIHelper.fluidTooltipCallback);

	}

	@Override
	public IRecipeWrapper getRecipeWrapper(BathingRecipe recipe)
	{
		return new BathingRecipeWrapper(recipe);
	}

	public static class BathingRecipeWrapper extends IIMultiblockRecipeWrapper
	{
		public BathingRecipeWrapper(MultiblockRecipe recipe)
		{
			super(recipe);
		}

		@Override
		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
		{
			ClientUtils.drawSlot(101, 13, 16, 16);
			GlStateManager.pushMatrix();
			GlStateManager.translate(45F, 20F, 32.5F);
			GlStateManager.scale(45, -45, 45);
			minecraft.getRenderItem().renderItem(machineStack, TransformType.GUI);
			GlStateManager.popMatrix();

			drawEnergyTimeInfo(minecraft, 0, recipeHeight-10);
		}
	}
}