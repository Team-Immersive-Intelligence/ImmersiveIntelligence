package pl.pabilo8.immersiveintelligence.common.compat.jei.sawmill;

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
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_WoodenMultiblock;
import pl.pabilo8.immersiveintelligence.common.compat.jei.IIRecipeCategory;

public class SawmillRecipeCategory extends IIRecipeCategory<SawmillRecipe, SawmillRecipeWrapper>
{
	public static ResourceLocation background = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/gui/jei_stuff.png");
	static ItemStack machineStack;

	public SawmillRecipeCategory(IGuiHelper helper)
	{
		super("sawmill", "tile."+ImmersiveIntelligence.MODID+".wooden_multiblock.sawmill.name", helper.createBlankDrawable(156, 60), SawmillRecipe.class, new ItemStack(IIContent.blockWoodenMultiblock, 1, IIBlockTypes_WoodenMultiblock.SAWMILL.getMeta()));
		machineStack = new ItemStack(IIContent.blockWoodenMultiblock, 1, IIBlockTypes_WoodenMultiblock.SAWMILL.getMeta());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, SawmillRecipeWrapper recipeWrapper, IIngredients ingredients)
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, -1, 20);
		guiItemStacks.init(1, true, 114, 20);
		guiItemStacks.init(2, true, 134, 20);

		guiItemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
		guiItemStacks.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

		if(ingredients.getOutputs(VanillaTypes.ITEM).size() > 1)
			guiItemStacks.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(1));

	}

	@Override
	public IRecipeWrapper getRecipeWrapper(SawmillRecipe recipe)
	{
		return new SawmillRecipeWrapper(recipe);
	}
}