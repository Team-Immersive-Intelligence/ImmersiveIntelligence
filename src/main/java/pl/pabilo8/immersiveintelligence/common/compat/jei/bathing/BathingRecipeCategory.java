/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package pl.pabilo8.immersiveintelligence.common.compat.jei.bathing;

import blusunrize.immersiveengineering.common.util.compat.jei.JEIHelper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.BathingRecipe;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock0;
import pl.pabilo8.immersiveintelligence.common.compat.jei.IIRecipeCategory;

import java.util.List;

public class BathingRecipeCategory extends IIRecipeCategory<BathingRecipe, BathingRecipeWrapper>
{
	public static ResourceLocation background = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/gui/jei_stuff.png");
	private final IDrawable tankOverlay;
	static ItemStack machineStack;

	public BathingRecipeCategory(IGuiHelper helper)
	{
		super("bathing", "tile."+ImmersiveIntelligence.MODID+".metal_multiblock.chemical_bath.name", helper.createBlankDrawable(140, 50), BathingRecipe.class, new ItemStack(IIContent.blockMetalMultiblock0, 1, IIBlockTypes_MetalMultiblock0.CHEMICAL_BATH.getMeta()));
		tankOverlay = helper.createDrawable(background, 0, 52, 20, 51, -2, 2, -2, 2);
		machineStack = new ItemStack(IIContent.blockMetalMultiblock0, 1, IIBlockTypes_MetalMultiblock0.CHEMICAL_BATH.getMeta());
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
}