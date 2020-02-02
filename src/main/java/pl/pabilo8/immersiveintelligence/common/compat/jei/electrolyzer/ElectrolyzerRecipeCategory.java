/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package pl.pabilo8.immersiveintelligence.common.compat.jei.electrolyzer;

import blusunrize.immersiveengineering.common.util.compat.jei.JEIHelper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.ElectrolyzerRecipe;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock0;
import pl.pabilo8.immersiveintelligence.common.compat.jei.IIRecipeCategory;

import javax.annotation.Nullable;
import java.util.List;

public class ElectrolyzerRecipeCategory extends IIRecipeCategory<ElectrolyzerRecipe, ElectrolyzerRecipeWrapper>
{
	public static ResourceLocation background = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/gui/jei_stuff.png");
	private final IDrawable tankOverlay;
	static ItemStack machineStack;

	public ElectrolyzerRecipeCategory(IGuiHelper helper)
	{
		super("electrolyzer", "tile."+ImmersiveIntelligence.MODID+".metal_multiblock.electrolyzer.name", helper.createBlankDrawable(140, 50), ElectrolyzerRecipe.class, new ItemStack(CommonProxy.block_metal_multiblock0, 1, IIBlockTypes_MetalMultiblock0.ELECTROLYZER.getMeta()));
		tankOverlay = helper.createDrawable(background, 0, 52, 20, 51, -2, 2, -2, 2);
		machineStack = new ItemStack(CommonProxy.block_metal_multiblock0, 1, IIBlockTypes_MetalMultiblock0.ELECTROLYZER.getMeta());
	}

	@Nullable
	@Override
	public IDrawable getIcon()
	{
		return super.getIcon();
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
}