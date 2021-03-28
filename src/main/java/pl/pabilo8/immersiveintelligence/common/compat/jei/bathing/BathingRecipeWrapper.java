package pl.pabilo8.immersiveintelligence.common.compat.jei.bathing;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.compat.jei.MultiblockRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;

public class BathingRecipeWrapper extends MultiblockRecipeWrapper
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
		minecraft.getRenderItem().renderItem(BathingRecipeCategory.machineStack, TransformType.GUI);
		GlStateManager.popMatrix();
	}
}