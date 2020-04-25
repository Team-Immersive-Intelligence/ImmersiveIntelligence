/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package pl.pabilo8.immersiveintelligence.common.compat.jei.sawmill;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.compat.jei.MultiblockRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;

public class SawmillRecipeWrapper extends MultiblockRecipeWrapper
{
	protected int hardness, torque;

	public SawmillRecipeWrapper(SawmillRecipe recipe)
	{
		super(recipe);
		hardness = recipe.getHardness();
		torque = recipe.getTorque();
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
	{
		ClientUtils.drawSlot(0, 21, 16, 16);
		ClientUtils.drawSlot(115, 21, 16, 16);
		ClientUtils.drawSlot(135, 21, 16, 16);

		GlStateManager.pushMatrix();
		GlStateManager.translate(85F, 20f, 150.5F);
		GlStateManager.rotate(50, 0, 1, 0);
		GlStateManager.rotate(8.5f, 1, 0, 0);
		GlStateManager.rotate(-12.5f, 0, 0, 1);
		GlStateManager.scale(65, -65, 65);
		minecraft.getRenderItem().renderItem(SawmillRecipeCategory.machineStack, TransformType.GUI);
		GlStateManager.popMatrix();
	}
}