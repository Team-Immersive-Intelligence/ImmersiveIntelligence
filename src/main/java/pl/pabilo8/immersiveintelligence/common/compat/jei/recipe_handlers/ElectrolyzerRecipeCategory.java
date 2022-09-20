package pl.pabilo8.immersiveintelligence.common.compat.jei.recipe_handlers;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.util.compat.jei.JEIHelper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
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
import pl.pabilo8.immersiveintelligence.api.crafting.ElectrolyzerRecipe;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.compat.jei.IIMultiblockRecipeWrapper;
import pl.pabilo8.immersiveintelligence.common.compat.jei.IIRecipeCategory;

import java.util.List;

public class ElectrolyzerRecipeCategory extends IIRecipeCategory<ElectrolyzerRecipe, ElectrolyzerRecipeCategory.ElectrolyzerRecipeWrapper>
{
	private final IDrawable tankOverlay;
	static ItemStack machineStack;

	public ElectrolyzerRecipeCategory(IGuiHelper helper)
	{
		super("electrolyzer",
				"tile."+ImmersiveIntelligence.MODID+".metal_multiblock.electrolyzer.name",
				helper.createBlankDrawable(140, 64),
				ElectrolyzerRecipe.class,
				new ItemStack(IIContent.blockMetalMultiblock0, 1, MetalMultiblocks0.ELECTROLYZER.getMeta())
		);
		tankOverlay =helper.drawableBuilder(texture, 0, 52, 20, 51)
				.addPadding(-2, 2, -2, 2)
				.build();
		machineStack = new ItemStack(IIContent.blockMetalMultiblock0, 1, MetalMultiblocks0.ELECTROLYZER.getMeta());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, ElectrolyzerRecipeWrapper recipeWrapper, IIngredients ingredients)
	{
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
		List<FluidStack> fs_in = ingredients.getInputs(VanillaTypes.FLUID).get(0);
		List<FluidStack> fs_out = ingredients.getOutputs(VanillaTypes.FLUID).get(0);

		guiFluidStacks.init(0, true, 4, 0, 16, 47, fs_in.get(0).amount*4, false, tankOverlay);
		guiFluidStacks.set(0, fs_in);

		guiFluidStacks.init(1, false, 96, 0, 16, 47, fs_in.get(0).amount*4, false, tankOverlay);
		guiFluidStacks.set(1, fs_out);

		if(ingredients.getOutputs(VanillaTypes.FLUID).size() > 1)
		{
			List<FluidStack> fs_out2 = ingredients.getOutputs(VanillaTypes.FLUID).get(1);
			guiFluidStacks.init(2, false, 118, 0, 16, 47, fs_in.get(0).amount*4, false, tankOverlay);
			guiFluidStacks.set(2, fs_out2);
		}
		guiFluidStacks.addTooltipCallback(JEIHelper.fluidTooltipCallback);

	}

	@Override
	public IRecipeWrapper getRecipeWrapper(ElectrolyzerRecipe recipe)
	{
		return new ElectrolyzerRecipeWrapper(recipe);
	}

	public static class ElectrolyzerRecipeWrapper extends IIMultiblockRecipeWrapper
	{
		public ElectrolyzerRecipeWrapper(MultiblockRecipe recipe)
		{
			super(recipe);
		}

		@Override
		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(55F, 20F, 85);
			GlStateManager.enableDepth();
			GlStateManager.scale(45, -45, 45);
			minecraft.getRenderItem().renderItem(machineStack, TransformType.GUI);
			GlStateManager.popMatrix();

			drawEnergyTimeInfo(minecraft, 0, recipeHeight-10);
		}
	}
}