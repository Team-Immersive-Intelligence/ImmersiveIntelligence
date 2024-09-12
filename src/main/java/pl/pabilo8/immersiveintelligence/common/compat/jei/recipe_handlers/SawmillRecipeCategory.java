package pl.pabilo8.immersiveintelligence.common.compat.jei.recipe_handlers;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Sawmill;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.BlockIIWoodenMultiblock.WoodenMultiblocks;
import pl.pabilo8.immersiveintelligence.common.compat.jei.IIMultiblockRecipeWrapper;
import pl.pabilo8.immersiveintelligence.common.compat.jei.IIRecipeCategory;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SawmillRecipeCategory extends IIRecipeCategory<SawmillRecipe, SawmillRecipeCategory.SawmillRecipeWrapper>
{
	static ItemStack machineStack;

	public SawmillRecipeCategory(IGuiHelper helper)
	{
		super("sawmill",
				"tile."+ImmersiveIntelligence.MODID+".wooden_multiblock.sawmill.name",
				helper.createBlankDrawable(156, 68),
				SawmillRecipe.class,
				new ItemStack(IIContent.blockWoodenMultiblock, 1, WoodenMultiblocks.SAWMILL.getMeta())
		);
		machineStack = new ItemStack(IIContent.blockWoodenMultiblock, 1, WoodenMultiblocks.SAWMILL.getMeta());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, SawmillRecipeWrapper recipeWrapper, IIngredients ingredients)
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, -1, 20-8);
		guiItemStacks.init(1, true, 114, 20-8);
		guiItemStacks.init(2, true, 134, 20-8);

		guiItemStacks.init(3, true, 64, 20-12);

		//in, out
		guiItemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
		guiItemStacks.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

		//sawdust
		if(ingredients.getOutputs(VanillaTypes.ITEM).size() > 1)
			guiItemStacks.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(1));

		//saw
		guiItemStacks.set(3, ingredients.getInputs(VanillaTypes.ITEM).get(1));

	}

	@Override
	public IRecipeWrapper getRecipeWrapper(SawmillRecipe recipe)
	{
		return new SawmillRecipeWrapper(recipe);
	}

	public static class SawmillRecipeWrapper extends IIMultiblockRecipeWrapper
	{
		protected int hardness, torque;

		public SawmillRecipeWrapper(SawmillRecipe recipe)
		{
			super(recipe);
			hardness = recipe.getHardness();
			torque = recipe.getTorque();
		}

		@Override
		public void getIngredients(IIngredients ingredients)
		{
			//add recipe input
			ArrayList<List<ItemStack>> items = new ArrayList<>(Arrays.asList(recipeInputs.clone()));
			//add saws
			items.add(
					SawmillRecipe.toolMap.entrySet().stream()
							.map(e -> new Tuple<>(e.getValue(), e.getValue().getToolPresentationStack(e.getKey())))
							.filter(e -> e.getFirst().getHardness(e.getSecond()) >= hardness)
							.map(Tuple::getSecond)
							.collect(Collectors.toList())
			);

			super.getIngredients(ingredients);
			if(!inputs.isEmpty())
				ingredients.setInputLists(VanillaTypes.ITEM, items);
		}

		@Override
		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
		{
			ClientUtils.drawSlot(0, 21-8, 16, 16);
			ClientUtils.drawSlot(115, 21-8, 16, 16);
			ClientUtils.drawSlot(135, 21-8, 16, 16);

			GlStateManager.pushMatrix();
			GlStateManager.translate(85F, 13f, 150.5F);
			GlStateManager.rotate(50, 0, 1, 0);
			GlStateManager.rotate(8.5f, 1, 0, 0);
			GlStateManager.rotate(-12.5f, 0, 0, 1);
			GlStateManager.scale(65, -65, 65);
			minecraft.getRenderItem().renderItem(machineStack, TransformType.GUI);
			GlStateManager.popMatrix();

			drawEnergyTimeInfo(minecraft, 0, recipeHeight-26);
		}

		@Override
		public void drawEnergyTimeInfo(Minecraft minecraft, int x, int y)
		{
			minecraft.getTextureManager().bindTexture(IIRecipeCategory.texture);
			//time icon
			ClientUtils.drawTexturedRect(x, y-3, 14, 14, 51/256f, (51+14)/256f, 15/256f, (15+14)/256f);
			//speed icon
			ClientUtils.drawTexturedRect(x+64, y-3, 14, 14, 79/256f, (79+14)/256f, 15/256f, (15+14)/256f);//energy icon
			//torque icon
			ClientUtils.drawTexturedRect(x+64, y-3+16, 14, 14, 93/256f, (93+14)/256f, 15/256f, (15+14)/256f);

			//shift ? ticks : seconds
			String time = GuiScreen.isShiftKeyDown()?
					this.time+" t":
					Utils.formatDouble(this.time*0.05, "0.##")+" s";
			minecraft.fontRenderer.drawString(time, x+16, y, IIReference.COLOR_H2.getPackedRGB());

			minecraft.fontRenderer.drawString(Sawmill.rpmMin+"-"+Sawmill.rpmBreakingMax+" RPM", x+64+16, y, IIReference.COLOR_H2.getPackedRGB());
			minecraft.fontRenderer.drawString(torque+" Nm", x+64+16, y+16, IIReference.COLOR_H2.getPackedRGB());
		}
	}
}