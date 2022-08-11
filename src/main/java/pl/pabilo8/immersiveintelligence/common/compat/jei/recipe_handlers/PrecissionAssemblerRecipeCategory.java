package pl.pabilo8.immersiveintelligence.common.compat.jei.recipe_handlers;

import blusunrize.immersiveengineering.client.ClientUtils;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecissionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock0;
import pl.pabilo8.immersiveintelligence.common.compat.jei.IIMultiblockRecipeWrapper;
import pl.pabilo8.immersiveintelligence.common.compat.jei.IIRecipeCategory;

import java.util.ArrayList;
import java.util.List;

public class PrecissionAssemblerRecipeCategory extends IIRecipeCategory<PrecissionAssemblerRecipe, PrecissionAssemblerRecipeCategory.PrecissionAssemblerRecipeWrapper>
{
	static ItemStack machineStack;

	public PrecissionAssemblerRecipeCategory(IGuiHelper helper)
	{
		super("precissionassembler",
				"tile."+ImmersiveIntelligence.MODID+".metal_multiblock.precission_assembler.name",
				helper.createBlankDrawable(156, 74),
				PrecissionAssemblerRecipe.class,
				new ItemStack(IIContent.blockMetalMultiblock0, 1, IIBlockTypes_MetalMultiblock0.PRECISSION_ASSEMBLER.getMeta())
		);
		machineStack = new ItemStack(IIContent.blockMetalMultiblock0, 1, IIBlockTypes_MetalMultiblock0.PRECISSION_ASSEMBLER.getMeta());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, PrecissionAssemblerRecipeWrapper recipeWrapper, IIngredients ingredients)
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, 19, 20);
		guiItemStacks.init(1, true, -1, 0);
		guiItemStacks.init(2, true, -1, 20);
		guiItemStacks.init(3, true, -1, 40);
		guiItemStacks.init(4, false, 134, 9);
		guiItemStacks.init(5, false, 134, 29);
		guiItemStacks.init(6, true, 54, 44);
		guiItemStacks.init(7, true, 74, 44);
		guiItemStacks.init(8, true, 94, 44);
		guiItemStacks.init(9, true, 71, 17);

		guiItemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

		if(recipeWrapper.recipeInputs.length > 1)
		{
			guiItemStacks.set(1, ingredients.getInputs(VanillaTypes.ITEM).get(1));
		}

		if(recipeWrapper.recipeInputs.length > 2)
		{
			guiItemStacks.set(2, ingredients.getInputs(VanillaTypes.ITEM).get(2));
		}

		if(recipeWrapper.recipeInputs.length > 3)
		{
			guiItemStacks.set(3, ingredients.getInputs(VanillaTypes.ITEM).get(3));
		}

		guiItemStacks.set(4, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
		if(recipeWrapper.recipeOutputs.length > 1)
		{
			guiItemStacks.set(5, ingredients.getOutputs(VanillaTypes.ITEM).get(1));
		}

		if(recipeWrapper.tools.size() > 0)
		{
			guiItemStacks.set(6, recipeWrapper.tools.get(0));
		}
		if(recipeWrapper.tools.size() > 1)
		{
			guiItemStacks.set(7, recipeWrapper.tools.get(1));
		}
		if(recipeWrapper.tools.size() > 2)
		{
			guiItemStacks.set(8, recipeWrapper.tools.get(2));
		}

		guiItemStacks.set(9, recipeWrapper.scheme);

	}

	@Override
	public IRecipeWrapper getRecipeWrapper(PrecissionAssemblerRecipe recipe)
	{
		return new PrecissionAssemblerRecipeWrapper(recipe);
	}

	public static class PrecissionAssemblerRecipeWrapper extends IIMultiblockRecipeWrapper
	{
		public PrecissionAssemblerRecipeWrapper(PrecissionAssemblerRecipe recipe)
		{
			super(recipe);
			for(String tool : recipe.tools)
			{
				if(PrecissionAssemblerRecipe.toolMap.containsKey(tool))
				{
					tools.add(PrecissionAssemblerRecipe.toolMap.get(tool).getToolPresentationStack(tool));
				}
			}
			scheme = IIContent.itemAssemblyScheme.getStackForRecipe(recipe);
		}

		protected List<ItemStack> tools = new ArrayList<>();
		protected ItemStack scheme;

		@Override
		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
		{
			ClientUtils.drawSlot(20, 21, 16, 16);
			ClientUtils.drawSlot(0, 1, 16, 16);
			ClientUtils.drawSlot(0, 21, 16, 16);
			ClientUtils.drawSlot(0, 41, 16, 16);
			ClientUtils.drawSlot(135, 10, 16, 16);
			ClientUtils.drawSlot(135, 30, 16, 16);
			ClientUtils.drawSlot(55, 45, 16, 16);
			ClientUtils.drawSlot(75, 45, 16, 16);
			ClientUtils.drawSlot(95, 45, 16, 16);

			GlStateManager.pushMatrix();
			GlStateManager.translate(165F, 1.5F, 150.5F);
			GlStateManager.rotate(55, 0, 1, 0);
			GlStateManager.rotate(9.5f, 1, 0, 0);
			GlStateManager.rotate(-10, 0, 0, 1);
			GlStateManager.scale(65, -65, 65);
			minecraft.getRenderItem().renderItem(machineStack, TransformType.GUI);
			GlStateManager.popMatrix();

			drawEnergyTimeInfo(minecraft, 0, recipeHeight-8);
		}
	}
}