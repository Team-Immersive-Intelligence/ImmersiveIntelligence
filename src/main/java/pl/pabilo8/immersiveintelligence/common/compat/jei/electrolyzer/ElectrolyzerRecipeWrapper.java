package pl.pabilo8.immersiveintelligence.common.compat.jei.electrolyzer;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.util.compat.jei.MultiblockRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;

public class ElectrolyzerRecipeWrapper extends MultiblockRecipeWrapper
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
		minecraft.getRenderItem().renderItem(ElectrolyzerRecipeCategory.machineStack, TransformType.GUI);
		GlStateManager.popMatrix();
	}
}