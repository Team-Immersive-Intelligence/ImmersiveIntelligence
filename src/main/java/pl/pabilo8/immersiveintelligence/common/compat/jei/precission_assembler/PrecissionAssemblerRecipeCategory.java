/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package pl.pabilo8.immersiveintelligence.common.compat.jei.precission_assembler;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecissionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock0;
import pl.pabilo8.immersiveintelligence.common.compat.jei.IIRecipeCategory;

import javax.annotation.Nullable;

public class PrecissionAssemblerRecipeCategory extends IIRecipeCategory<PrecissionAssemblerRecipe, PrecissionAssemblerRecipeWrapper>
{
	public static ResourceLocation background = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/gui/jei_stuff.png");
	static ItemStack machineStack;

	public PrecissionAssemblerRecipeCategory(IGuiHelper helper)
	{
		super("precissionassembler", "tile."+ImmersiveIntelligence.MODID+".metal_multiblock.precission_assembler.name", helper.createBlankDrawable(156, 60), PrecissionAssemblerRecipe.class, new ItemStack(CommonProxy.block_metal_multiblock0, 1, IIBlockTypes_MetalMultiblock0.PRECISSION_ASSEMBLER.getMeta()));
		machineStack = new ItemStack(CommonProxy.block_metal_multiblock0, 1, IIBlockTypes_MetalMultiblock0.PRECISSION_ASSEMBLER.getMeta());
	}

	@Nullable
	@Override
	public IDrawable getIcon()
	{
		return super.getIcon();
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
}