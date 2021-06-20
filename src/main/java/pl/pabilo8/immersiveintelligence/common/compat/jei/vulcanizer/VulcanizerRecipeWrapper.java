package pl.pabilo8.immersiveintelligence.common.compat.jei.vulcanizer;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.crafting.VulcanizerRecipe;
import pl.pabilo8.immersiveintelligence.common.compat.jei.IIMultiblockRecipeWrapper;

public class VulcanizerRecipeWrapper extends IIMultiblockRecipeWrapper
{
	public final ItemStack mold;

	public VulcanizerRecipeWrapper(VulcanizerRecipe recipe)
	{
		super(recipe);
		this.mold = recipe.mold.stack.copy();
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
	{
		ClientUtils.drawSlot(0, 1, 16, 16);
		ClientUtils.drawSlot(0, 21, 16, 16);
		ClientUtils.drawSlot(0, 41, 16, 16);


		ClientUtils.drawSlot(135, 21, 16, 16);

		GlStateManager.pushMatrix();
		GlStateManager.translate(100F, 10f, 15.5F);
		GlStateManager.rotate(105, 0, 1, 0);
		GlStateManager.rotate(22.5f, 1, 0, 0);
		GlStateManager.rotate(-12.5f, 0, 0, 1);
		GlStateManager.scale(65, -65, 65);
		minecraft.getRenderItem().renderItem(VulcanizerRecipeCategory.machineStack, TransformType.GUI);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0,0,100f);
		ClientUtils.drawSlot(61, 41, 16, 16);
		GlStateManager.popMatrix();

	}
}