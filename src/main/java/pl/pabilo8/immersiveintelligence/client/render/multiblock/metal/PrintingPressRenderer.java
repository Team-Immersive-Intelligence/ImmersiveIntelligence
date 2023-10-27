package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.PrintingPress;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelPrintingPress;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.TmtUtil;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrintingPress;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class PrintingPressRenderer extends TileEntitySpecialRenderer<TileEntityPrintingPress> implements IReloadableModelContainer<PrintingPressRenderer>
{
	static RenderItem renderItem = ClientUtils.mc().getRenderItem();
	private static ModelPrintingPress model;
	private static ModelPrintingPress modelFlipped;

	@Override
	public void render(TileEntityPrintingPress te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/printing_press.png";
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x, (float)y, (float)z);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			ModelPrintingPress modelCurrent = te.mirrored?modelFlipped: model;
			modelCurrent.getBlockRotation(te.facing, te.mirrored);
			modelCurrent.rotate(modelCurrent.paperInserterDoorModel, 0, 0, -1.57079633F);

			float time = (PrintingPress.printTime-(te.processTimeLeft-partialTicks))/((float)PrintingPress.printTime);
			float product_x = 0f, product_y = 0f, product_z = 0f, rotation_x = 0f, rotation_y = 0f;
			if(te.processTimeLeft!=0)
			{
				if(time <= 0.125&&te.active)
				{
					product_y = 0.375f;
					modelCurrent.rotate(modelCurrent.paperInserterDoorModel, 0, 0, -1.57079633F+(float)((time/0.125)*1.57079633F));
				}
				else if(time <= 0.375)
				{
					product_y = 0.375f;
					float ttime = (time-0.125f)/0.125f;
					product_x = ttime/2f;

					modelCurrent.rotate(modelCurrent.paperInserterDoorModel, 0, 0, 0);
				}
				else if(time <= 0.5)
				{
					product_y = 0.25f;
					product_x = 1f;
					modelCurrent.rotate(modelCurrent.paperInserterDoorModel, 0, 0, -(float)(((time-0.375)/0.125)*1.57079633F));
					rotation_y = -15f;
					float ttime = (time-0.375f)/0.125f;

					if(ttime < 0.5)
					{
						rotation_y = -15f*(ttime/0.5f);
					}
					else if(ttime > 0.5f)
					{
						product_z = 0.5f*((ttime-0.5f)/0.5f);
						product_y -= 0.125f*((ttime-0.5f)/0.5f);
						rotation_y -= 15f;
					}

				}
				else if(time <= 0.9375)
				{
					product_y = 0.125f;
					product_x = 1f;
					product_z = 0.5f;
					rotation_y = -15f;
					float ttime = (time-0.5f)/0.4375f;

					product_z += 2f*ttime;

					if(ttime < 0.125f)
					{
						rotation_y += (15*(ttime/0.125f));
						product_y -= 0.1875f*(ttime/0.125f);
					}
					else
					{
						rotation_y += 15f;
						product_y -= 0.1875f;
					}

					if(ttime >= 0.125f&&ttime < 0.375f)
					{
						rotation_y -= 12.5f;
					}
					else if(ttime >= 0.375f&&ttime < 0.575f)
					{
						rotation_y += 12.5f;
					}
					else if(ttime >= 0.775f&&ttime < 0.975f)
					{
						rotation_y -= 12.5f;
					}
					else if(ttime >= 0.975f)
					{
						rotation_y += 12.5f;
					}

				}
				else
				{
					product_y = -0.125f;
					product_x = 1f;
					product_z = 2.5f;
					rotation_y = -12.5f;
					float ttime = (time-0.9375f)/0.125f;

					if(ttime > 0.125f&&ttime < 0.5f)
					{
						product_y -= 0.5f*((ttime-0.125f)/0.475f);
					}

					if(ttime < 0.5f)
					{
						product_z += 0.25f*(ttime/0.5f);
						rotation_y -= 12.5f*(ttime/0.5f);
					}
					else
					{
						product_z += 0.25f+(0.5f*(ttime/0.5f));
						product_y -= 0.5f+(0.5f*((ttime-0.5f)/0.5f));
					}

				}
			}

			for(ModelRendererTurbo mod : modelCurrent.rollerModel)
			{
				mod.rotateAngleZ = TmtUtil.AngleToTMT(te.rollerRotation+(te.active?partialTicks: 0f));
			}

			modelCurrent.parts.values().forEach(modelRendererTurbos ->
			{
				for(ModelRendererTurbo mod : modelRendererTurbos)
					mod.render(0.0625f);
			});

			GlStateManager.pushMatrix();

			if(!te.mirrored)
				GlStateManager.translate(1.625f+product_x, 1.5f+product_y, -3.7f+product_z);
			else
				GlStateManager.translate(1.625f+product_x, 1.5f+product_y, 3.7f-product_z);
			GlStateManager.scale(1.5f, 1.5f, 1.5f);
			GlStateManager.rotate(270f, 1f, 0f, 0f);
			GlStateManager.rotate(90f, 0f, 0f, 1f);
			GlStateManager.rotate(te.mirrored?-rotation_x: rotation_x, 1f, 0f, 0f);
			GlStateManager.rotate(te.mirrored?-rotation_y: rotation_y, 0f, 1f, 0f);
			renderItem.renderItem(time > 0.625?te.renderStack1: te.renderStack0, TransformType.GROUND);

			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(2.6875f, 0.375f, te.mirrored?0.375f: -0.375f);
			GlStateManager.scale(1.5f, 1.5f, 1.5f);
			GlStateManager.rotate(270f, 1f, 0f, 0f);
			GlStateManager.rotate(90f, 0f, 0f, 1f);
			GlStateManager.rotate(te.mirrored?45f: -45f, 0f, 1f, 0f);

			for(int i = te.inventory.get(1).getCount(); i > 0; i--)
			{
				renderItem.renderItem(te.inventory.get(1), TransformType.GROUND);
				GlStateManager.translate(te.mirrored?-0.03125f: 0.03125f, 0f, 0.03125f);
			}
			GlStateManager.popMatrix();

			GlStateManager.popMatrix();

		}
		else if(te==null)
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+0.5, (float)y+0.5, (float)z+1.75);
			GlStateManager.scale(0.3, 0.3, 0.3);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			model.render();
			GlStateManager.popMatrix();

		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelPrintingPress(false);
		modelFlipped = new ModelPrintingPress(true);
		for(int i = 14; i <= 19; i += 1)
			modelFlipped.baseModel[i].rotateAngleY *= -1;
		modelFlipped.flipAllZ();

		//A special case (the model is done in a deprecated way), not applied in other models
		modelFlipped.parts.values().forEach(modelRendererTurbos ->
		{
			for(ModelRendererTurbo m : modelRendererTurbos)
			{
				m.rotateAngleX *= -1;
				m.rotateAngleZ *= -1;
				m.offsetX *= -1;
				m.offsetY *= -1;
				m.offsetZ *= -1;
			}

		});
	}
}
