package pl.pabilo8.immersiveintelligence.common.compat.jei.precission_assembler;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.compat.jei.MultiblockRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecissionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.common.IIContent;

import java.util.ArrayList;
import java.util.List;

public class PrecissionAssemblerRecipeWrapper extends MultiblockRecipeWrapper
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
	protected ItemStack scheme = ItemStack.EMPTY;

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
		minecraft.getRenderItem().renderItem(PrecissionAssemblerRecipeCategory.machineStack, TransformType.GUI);
		GlStateManager.popMatrix();
	}
}