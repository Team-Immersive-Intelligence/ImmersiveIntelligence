package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal.MultiblockProcess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.VulcanizerRecipe;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelVulcanizer;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityVulcanizer;

/**
 * @author Pabilo8
 * @since 21-06-2019
 */
public class VulcanizerRenderer extends TileEntitySpecialRenderer<TileEntityVulcanizer> implements IReloadableModelContainer<VulcanizerRenderer>
{
	private static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/vulcanizer.png";
	static RenderItem renderItem = ClientUtils.mc().getRenderItem();
	private static ModelVulcanizer model;
	private static ModelVulcanizer modelFlipped;

	@Override
	public void render(TileEntityVulcanizer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x, (float)y, (float)z);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			float rotato = 0, rollers = 0, cloth = 0;
			float lowered1 = 0, lowered2 = 0, open1 = 0, open2 = 0, heating1 = 0, heating2 = 0;
			ResourceLocation resIn = null, resOut = null;

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 0, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			if(te.processQueue.size() > 0)
			{
				MultiblockProcess<VulcanizerRecipe> process0 = te.processQueue.get(0);
				double processTime = ((process0.processTick+partialTicks)/(double)process0.maxTicks);
				//rolling
				if(processTime < 0.16)
				{
					cloth = (float)(processTime/0.16);
					rollers = (float)((cloth*60)%4d)/4f;
				}
				//waiting
				else if(processTime < 0.2)
				{

				}
				//heating up
				else if(processTime < 0.3)
				{
					if(te.recipePerformed)
						heating2 = (float)((processTime-0.2)/0.1);
					else
						heating1 = (float)((processTime-0.2)/0.1);
				}
				//actual process
				else if(processTime < 0.78)
				{
					if(te.recipePerformed)
						heating2 = 1;
					else
						heating1 = 1;
				}
				//rotato faster vulcanizer
				else if(processTime < 0.84)
				{
					if(te.recipePerformed)
						heating2 = (float)(1d-((processTime-0.78)/0.02));
					else
						heating1 = (float)(1d-((processTime-0.78)/0.02));

					rotato = Math.min((float)((processTime-0.78)/0.05), 1);
				}
				//lowering holders
				else if(processTime < 0.86)
				{
					rotato = 1;
					if(te.recipePerformed)
						lowered2 = (float)((processTime-0.84)/0.02);
					else
						lowered1 = (float)((processTime-0.84)/0.02);
				}
				//opening door
				else if(processTime < 0.89)
				{
					rotato = 1;
					if(te.recipePerformed)
					{
						lowered2 = 1;
						open2 = (float)((processTime-0.86)/0.03);
					}
					else
					{
						lowered1 = 1;
						open1 = (float)((processTime-0.86)/0.03);
					}
				}
				//yeeting the items
				else if(processTime < 0.91)
				{
					rotato = 1;

					if(te.recipePerformed)
					{
						lowered2 = 1;
						open2 = 1;
					}
					else
					{
						lowered1 = 1;
						open1 = 1;
					}


				}
				//closing the door
				else if(processTime < 0.93)
				{
					rotato = 1;
					if(te.recipePerformed)
					{
						lowered2 = 1;
						open2 = 1f-Math.min((float)((processTime-0.91)/0.015), 1);
					}
					else
					{
						lowered1 = 1;
						open1 = 1f-Math.min((float)((processTime-0.91)/0.015), 1);
					}
				}
				//pulling up holders
				else
				{
					rotato = 1;
					if(te.recipePerformed)
					{
						lowered2 = 1f-Math.min((float)((processTime-0.93)/0.03), 1);
					}
					else
					{
						lowered1 = 1f-Math.min((float)((processTime-0.93)/0.03), 1);
					}
				}

				if(te.processQueue.size() > 1)
				{
					MultiblockProcess<VulcanizerRecipe> process1 = te.processQueue.get(1);
					processTime = ((process1.processTick+partialTicks)/(double)process1.maxTicks);

					//rolling
					if(processTime < 0.16)
					{
						cloth = (float)(processTime/0.16);
						rollers = (float)(((processTime/0.16)*60)%4d)/4f;
					}
					resIn = process1.recipe.resIn;
					resOut = process1.recipe.resOut;
				}
				else
				{
					resIn = process0.recipe.resIn;
					resOut = process0.recipe.resOut;
				}

			}

			float rotMod = te.mirrored?-1: 1;
			ModelVulcanizer modelCurrent = te.mirrored?modelFlipped: model;
			modelCurrent.getBlockRotation(te.facing, te.mirrored);

			for(ModelRendererTurbo mod : modelCurrent.baseModel)
				mod.render();
			for(int i = 0; i < modelCurrent.rollerModel.length; i++)
			{
				ModelRendererTurbo mod = modelCurrent.rollerModel[i];
				mod.rotateAngleZ = rollers*-rotMod*6.28f*(i%2==0?1: -1);
				mod.render();
			}
			renderCloth(cloth, te.mirrored, resIn, resOut);
			//renderCloth((te.getWorld().getTotalWorldTime()%80f+partialTicks)/80f,VulcanizerRecipe.TEXTURE_LATEX, VulcanizerRecipe.TEXTURE_RUBBER);//resIn,resOut);

			GlStateManager.translate(te.mirrored?-1.0625f: 1.0625f, 0, 1.3125f);

			GlStateManager.rotate(rotato*180*rotMod, 0, 1, 0);

			if(te.recipePerformed)
				GlStateManager.rotate(180*rotMod, 0, 1, 0);

			for(ModelRendererTurbo mod : modelCurrent.rotatoModel)
				mod.render();


			GlStateManager.translate(0.125f, 0, 0);
			GlStateManager.rotate(180, 0, 1, 0);
			renderBasin(modelCurrent, te.mirrored, lowered1, open1, heating1);

			GlStateManager.translate(0.125f, 0, 0);
			GlStateManager.rotate(180, 0, 1, 0);
			renderBasin(modelCurrent, te.mirrored, lowered2, open2, heating2);

			GlStateManager.popMatrix();
		}
		else if(te==null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x-0.25, y-0.25, z);
			GlStateManager.rotate(7.5f, 0, 0, 1);
			GlStateManager.rotate(-7.5f, 1, 0, 0);
			GlStateManager.scale(0.23, 0.23, 0.23);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			ClientUtils.bindTexture(TEXTURE);

			for(ModelRendererTurbo mod : model.baseModel)
				mod.render();
			for(ModelRendererTurbo mod : model.rollerModel)
			{
				mod.rotateAngleZ = 0;
				mod.render();
			}

			GlStateManager.translate(1.0625f, 0, 1.3125f);

			for(ModelRendererTurbo mod : model.rotatoModel)
				mod.render();

			GlStateManager.translate(0.125f, 0, 0);
			GlStateManager.rotate(180, 0, 1, 0);
			renderBasin(model, false, 0, 0, 0);

			GlStateManager.translate(0.125f, 0, 0);
			GlStateManager.rotate(180, 0, 1, 0);
			renderBasin(model, false, 0, 0, 0);

			GlStateManager.popMatrix();
		}
	}

	private void renderCloth(float progress, boolean mirrored, ResourceLocation resIn, ResourceLocation resOut)
	{
		if(resIn==null||resOut==null)
			return;

		GlStateManager.pushMatrix();

		GlStateManager.disableCull();
		if(!mirrored)
			GlStateManager.translate(-1.125, 0, 0);
		GlStateManager.translate(0.25, 0.5+0.125+1.125, -1.25+0.125-0.0625);
		Minecraft.getMinecraft().getTextureManager().bindTexture(resIn);
		drawSingleCloth((progress*10)%1f, progress);
		GlStateManager.translate(0, -1.125, 0);
		Minecraft.getMinecraft().getTextureManager().bindTexture(resOut);
		drawSingleCloth((progress*10)%1f, Math.max(progress-0.2f, 0)/0.8f);
		GlStateManager.enableCull();

		GlStateManager.popMatrix();
		ClientUtils.bindTexture(TEXTURE);
	}

	private void drawSingleCloth(float progress, float totalProgress)
	{
		boolean upToDown = totalProgress < 0.85f;
		float prog1, prog2, prog3;
		if(totalProgress < 0.15)
		{
			prog1 = Math.max(totalProgress-0.1f, 0)/0.05f;
			prog2 = MathHelper.clamp(totalProgress-0.05f, 0, 0.05f)/0.05f;
			prog3 = (totalProgress)/0.05f;
		}
		else if(totalProgress > 0.85)
		{
			prog1 = 1f-Math.max(totalProgress-0.95f, 0)/0.05f;
			prog2 = 1f-MathHelper.clamp(totalProgress-0.9f, 0, 0.05f)/0.05f;
			prog3 = 1f-MathHelper.clamp(totalProgress-0.85f, 0, 0.05f)/0.05f;
		}
		else
		{
			prog1 = 1;
			prog2 = 1;
			prog3 = 1;
		}

		GlStateManager.pushMatrix();
		GlStateManager.rotate(-25, 1, 0, 0);
		ClientUtils.drawTexturedRect(0, 0.5f*(upToDown?(1-prog1): 0), 0.625f, 0.5f*prog1, 0d, 1d, 0d+progress, 0.5d*prog1+progress);
		GlStateManager.translate(0, 0.5f, 0);
		GlStateManager.rotate(-65, 1, 0, 0);
		ClientUtils.drawTexturedRect(0, 0.25f*(upToDown?(1-prog2): 0), 0.625f, 0.25f*prog2, 0d, 1d, 0.5d*prog1+progress, 0.75*prog2+progress);
		GlStateManager.translate(0, 0.25f, 0);
		GlStateManager.rotate(75, 1, 0, 0);
		ClientUtils.drawTexturedRect(0, 0.25f*(upToDown?(1-prog3): 0), 0.625f, 0.25f*prog3, 0d, 1d, 0.75d*prog2+progress, 1d*prog3+progress);
		GlStateManager.popMatrix();
	}

	@Override
	public void reloadModels()
	{
		model = new ModelVulcanizer();
		model.flipAllX();
		model.flipX(model.rollerModel);
		model.translate(model.rollerModel, -4, 0, 0);


		model.castTopModel[26] = new ModelRendererTurbo(model, 125, 228, 256, 256); // TOP_TWO01
		model.castTopModel[26].addShapeBox(0F, 0F, 0F, 12, 0, 7, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // TOP_TWO01
		model.castTopModel[26].setRotationPoint(41F-30, 15f+27.5F, -44.8F+21);
		model.castTopModel[26].rotateAngleX = 1.57079633F;


		modelFlipped = new ModelVulcanizer();
	}

	public void renderBasin(ModelVulcanizer model, boolean mirrored, float lowered, float open, float heating)
	{
		GlStateManager.pushMatrix();

		GlStateManager.translate(0, -0.5*lowered, 0);
		//1-4
		final float l = 0.33161256F*2.5f*lowered*(mirrored?1: -1);
		final float xx = -lowered*(mirrored?1: -1)*3f;
		final float yy = lowered*7.5f;
		model.castTopModel[2].rotateAngleZ = l;
		model.castTopModel[3].rotateAngleZ = l;
		model.castTopModel[4].rotateAngleZ = l;

		model.castTopModel[22].offsetY = yy;
		model.castTopModel[22].offsetX = xx;
		model.castTopModel[23].offsetY = yy;
		model.castTopModel[23].offsetX = xx;
		model.castTopModel[24].offsetY = yy;
		model.castTopModel[24].offsetX = xx;

		for(ModelRendererTurbo mod : model.castTopModel)
			mod.render();

		for(ModelRendererTurbo mod : model.castDoorModel)
			mod.render();

		GlStateManager.pushMatrix();
		GlStateManager.translate(mirrored?-1.90625: 1.90625, (8.5+21)/16f, 0);
		GlStateManager.rotate((mirrored?-open: open)*175, 0, 0, 1);
		for(ModelRendererTurbo mod : model.castBoxModel)
			mod.render();
		GlStateManager.popMatrix();

		GlStateManager.enableBlend();
		GlStateManager.color(1, 1, 1, heating);
		for(ModelRendererTurbo mod : model.castHeatingModel)
			mod.render();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.disableBlend();

		GlStateManager.popMatrix();
	}
}
