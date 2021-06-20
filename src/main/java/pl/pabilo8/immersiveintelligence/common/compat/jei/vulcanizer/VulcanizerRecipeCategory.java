package pl.pabilo8.immersiveintelligence.common.compat.jei.vulcanizer;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.VulcanizerRecipe;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock1;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_WoodenMultiblock;
import pl.pabilo8.immersiveintelligence.common.compat.jei.IIRecipeCategory;

public class VulcanizerRecipeCategory extends IIRecipeCategory<VulcanizerRecipe, VulcanizerRecipeWrapper>
{
	public static ResourceLocation background = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/gui/jei_stuff.png");
	static ItemStack machineStack;

	public VulcanizerRecipeCategory(IGuiHelper helper)
	{
		super("vulcanizer", "tile."+ImmersiveIntelligence.MODID+".metal_multiblock1.vulcanizer.name", helper.createBlankDrawable(156, 60), VulcanizerRecipe.class, new ItemStack(IIContent.blockMetalMultiblock1, 1, IIBlockTypes_MetalMultiblock1.VULCANIZER.getMeta()));
		machineStack = new ItemStack(IIContent.blockMetalMultiblock1, 1, IIBlockTypes_MetalMultiblock1.VULCANIZER.getMeta());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, VulcanizerRecipeWrapper recipeWrapper, IIngredients ingredients)
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, -1, 0);
		guiItemStacks.init(1, true, -1, 20);
		guiItemStacks.init(2, true, -1, 40);

		guiItemStacks.init(3, true, 60, 40);

		guiItemStacks.init(4, false, 134, 20);


		//guiItemStacks.init(2, true, 134, 20);

		guiItemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
		guiItemStacks.set(1, ingredients.getInputs(VanillaTypes.ITEM).get(1));
		guiItemStacks.set(2, ingredients.getInputs(VanillaTypes.ITEM).get(2));

		guiItemStacks.set(3, recipeWrapper.mold);

		guiItemStacks.set(4, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

	}

	@Override
	public IRecipeWrapper getRecipeWrapper(VulcanizerRecipe recipe)
	{
		return new VulcanizerRecipeWrapper(recipe);
	}
}