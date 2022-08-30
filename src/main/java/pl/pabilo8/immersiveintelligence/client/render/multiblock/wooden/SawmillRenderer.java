package pl.pabilo8.immersiveintelligence.client.render.multiblock.wooden;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils;
import pl.pabilo8.immersiveintelligence.api.utils.ISawblade;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.wooden.ModelSawmill;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.block.multiblocks.wooden.TileEntitySawmill;

/**
 * @author Pabilo8
 * @since 21-06-2019
 */
public class SawmillRenderer extends TileEntitySpecialRenderer<TileEntitySawmill> implements IReloadableModelContainer<SawmillRenderer>
{
	static RenderItem renderItem = ClientUtils.mc().getRenderItem();
	private static ModelSawmill model;
	private static ModelSawmill modelFlipped;

	@Override
	public void render(TileEntitySawmill te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/table_saw.png";
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x, (float)y, (float)z);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			double world_rpm = -1;
			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				RenderHelper.enableStandardItemLighting();
				//RenderHelper.enableGUIStandardItemLighting();

				world_rpm = (te.getWorld().getTotalWorldTime()%RotaryUtils.getRPMMax()+partialTicks)/RotaryUtils.getRPMMax();
			}
			ModelSawmill modelCurrent = te.mirrored?modelFlipped: model;
			float mirrorMod = te.mirrored?-1: 1;
			modelCurrent.getBlockRotation(te.facing, te.mirrored);


			for(ModelRendererTurbo mod : modelCurrent.baseModel)
				mod.render(0.0625f);

			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			for(ModelRendererTurbo mod : modelCurrent.baseTransparentModel)
				mod.render(0.0625f);
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(2.5f, 0.6875f, 0f);
			if(world_rpm > -1)
				GlStateManager.rotate((float)-(world_rpm*360f*te.rotation.getRotationSpeed()), 0, 0, 1);

			for(ModelRendererTurbo mod : modelCurrent.axleModel)
				mod.render(0.0625f);

			if(world_rpm > -1&&te.inventory.get(1).getItem() instanceof ISawblade)
			{
				GlStateManager.translate(0, 0, -0.0625*mirrorMod);

				renderItem.renderItem(new ItemStack(te.inventory.get(1).getItem(), 1, ((ISawblade)te.inventory.get(1).getItem()).getSawbladeDisplayMeta(te.inventory.get(1))), TransformType.FIXED);
				ClientUtils.bindTexture(texture);
			}

			GlStateManager.popMatrix();
			boolean renderInput = !te.inventory.get(0).isEmpty();

			if(te.processPrimary.getCount() > 0)
			{
				GlStateManager.pushMatrix();

				//IILogger.info(te.processTime+" / "+te.processTimeMax);
				float ptime = Math.min(te.processTime+partialTicks, te.processTimeMax);
				float timeFull = ptime/(float)te.processTimeMax;
				boolean renderStack = false, time_half = false;

				float single, time_inserter = 0, inserter_backpush_time = 0, inserter_push = 0;
				int iteration = 0;

				if(timeFull < 0.125f)
				{
					timeFull /= 0.125f;
					renderStack = timeFull > 0.5;
					renderInput = !renderStack||te.inventory.get(0).getCount() > 1;
					inserter_push = (1f-(Math.abs(timeFull-0.5f)/0.5f))*0.75f;


				}
				else if(timeFull < 0.875f)
				{
					//timeFull=(timeFull-0.125f)/0.6255f;

					single = (te.processTimeMax*0.75f)/(float)te.processPrimary.getCount();
					ptime -= te.processTimeMax*0.125f;
					time_inserter = ((ptime)%single)/(single);
					iteration = (int)Math.floor(ptime/single);
					time_half = time_inserter > 0.5f;
					inserter_backpush_time = time_inserter > 0.75f?(time_inserter-0.75f)/0.25f: 0;
					time_inserter = 1f-(Math.abs(Math.min(time_inserter, 1f)-0.5f)/0.5f);
					renderStack = !(iteration+(time_half?1: 0) >= te.processPrimary.getCount());

					if(!time_half&&time_inserter > 0.45f&&getWorld().getTotalWorldTime()%2==0)
						te.active = true;

					if(time_half||iteration > 0)
					{
						GlStateManager.pushMatrix();

						GlStateManager.translate(2.5, 1.25, -0.325f*mirrorMod);

						if(time_half)
						{
							float time = (1f-Math.min(time_inserter/0.65f, 1));
							GlStateManager.translate(-0.65*(time > 0.25f?(time-0.25f)/0.75f: 0f), -0.25*Math.min(time/0.25f, 1f), 0f);
							GlStateManager.rotate(-65*(1f-Math.min(time_inserter/0.65f, 1)), 1, 0, 0);
						}
						else
						{
							float time = (Math.min(time_inserter/0.65f, 1));
							GlStateManager.translate(-0.65, -0.25, 0f);
							GlStateManager.translate(-0.95*(time < 0.75f?time/0.75f: 1f), 0f, 0f);
							GlStateManager.translate(-0.45*(time > 0.75f?(time-0.75f)/0.25f: 0f), -0.45*(time > 0.75f?(time-0.75f)/0.25f: 0f), 0f);
							GlStateManager.rotate(-75-(25*(1f-Math.min(time_inserter/0.65f, 1))), 1, 0, 0);

							GlStateManager.scale(1-(0.15f*time_inserter), 1-(0.15f*time_inserter), 1-(0.15f*time_inserter));
							//GlStateManager.rotate(-120*(time_inserter>0.65f?(time_inserter-0.65f)/0.35f:0f),1,0,0);

							if(time_inserter > 0.65&&time_inserter < 0.75)
								te.spawnLast = true;
						}

						GlStateManager.scale(0.75f, 0.75f, 0.55f);
						renderItem.renderItem(te.processPrimary, TransformType.FIXED);


						GlStateManager.popMatrix();
						ClientUtils.bindTexture(texture);
					}

				}
				else
				{
					iteration = te.processPrimary.getCount();
					//inserter_backpush_time=-((timeFull-0.825f)/0.125f)*te.processPrimary.getCount();

					GlStateManager.pushMatrix();

					GlStateManager.translate(2.5, 1.25, -0.325f*mirrorMod);
					float time = Math.min((timeFull-0.875f)/0.125f, 1f);
					GlStateManager.translate(-0.65, -0.25, 0f);
					GlStateManager.translate(-0.95*(time < 0.75f?time/0.75f: 1f), 0f, 0f);
					GlStateManager.translate(-0.45*(time > 0.75f?(time-0.75f)/0.25f: 0f), -0.45*(time > 0.75f?(time-0.75f)/0.25f: 0f), 0f);
					GlStateManager.rotate(-75-(25*(1f-Math.min(time_inserter/0.65f, 1))), 1, 0, 0);

					GlStateManager.scale(1-(0.15f*time_inserter), 1-(0.15f*time_inserter), 1-(0.15f*time_inserter));

					GlStateManager.scale(0.75f, 0.75f, 0.55f);
					renderItem.renderItem(te.processPrimary, TransformType.FIXED);

					if(time > 0.9&&getWorld().getTotalWorldTime()%4==0)
						te.spawnLast = true;

					GlStateManager.popMatrix();
					ClientUtils.bindTexture(texture);

				}

				GlStateManager.translate(time_inserter*-1.125f, 0f, 0f);

				for(ModelRendererTurbo mod : modelCurrent.inserterBaseModel)
					mod.render(0.0625f);

				GlStateManager.translate(0, 0, mirrorMod*(0.5+(Math.min((iteration+inserter_backpush_time)/(float)te.processPrimary.getCount(), 1)*-0.325)-inserter_push));
				for(ModelRendererTurbo mod : modelCurrent.inserterMovingPartModel)
					mod.render(0.0625f);

				GlStateManager.translate(3.5, 1.25, -0.325f*mirrorMod);
				GlStateManager.scale(0.95f, 0.95f, 0.95f-Math.max((iteration+(time_half?1: 0))/(float)te.processPrimary.getCount(), 0));

				if(renderStack)
					renderItem.renderItem(te.inventory.get(0), TransformType.FIXED);

				GlStateManager.popMatrix();

				if(!te.inventory.get(2).isEmpty()||iteration > 0)
				{
					GlStateManager.pushMatrix();
					int total = Math.min(9, te.inventory.get(2).getCount()+iteration+(!time_half?-1: 0));
					ItemStack stack = te.processPrimary.getCount() > 0?te.processPrimary: te.inventory.get(2);

					GlStateManager.translate(0.25, 0.15, -0.25*mirrorMod);
					GlStateManager.rotate(-45f, 1, 0, 0);

					for(int i = 0; i < total; i += 1)
					{
						GlStateManager.pushMatrix();
						GlStateManager.rotate(45f, 1, 0, 0);
						GlStateManager.translate(i%3*0.25f, (i/3)*0.25f, (i/3)*-0.15*mirrorMod);
						GlStateManager.rotate(-45f, 1, 0, 0);
						GlStateManager.scale(0.75, 0.75, 0.75);

						renderItem.renderItem(stack, TransformType.FIXED);
						GlStateManager.popMatrix();
					}

					GlStateManager.popMatrix();
				}


				ClientUtils.bindTexture(texture);
			}
			else
			{
				for(ModelRendererTurbo mod : modelCurrent.inserterBaseModel)
					mod.render(0.0625f);

				for(ModelRendererTurbo mod : modelCurrent.inserterMovingPartModel)
					mod.render(0.0625f);
			}

			if(renderInput)
			{
				GlStateManager.pushMatrix();
				GlStateManager.translate(3.5, 1.25, -0.35f*mirrorMod);
				GlStateManager.scale(0.9f, 0.9f, 0.9f);
				renderItem.renderItem(te.inventory.get(0), TransformType.FIXED);
				GlStateManager.popMatrix();
				ClientUtils.bindTexture(texture);
			}

			if(!te.inventory.get(3).isEmpty())
			{
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				GlStateManager.translate(2.375f, 0f, 0f);
				float scale = Math.min(1, (float)te.inventory.get(3).getCount()*2f/(float)te.inventory.get(3).getMaxStackSize());
				GlStateManager.scale(scale, scale, scale);
				for(ModelRendererTurbo mod : modelCurrent.sawdustModel)
					mod.render(0.0625f);
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();

		}
		else if(te==null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x-0.75, y-0.25, z+0.35);
			GlStateManager.rotate(7.5f, 0, 0, 1);
			GlStateManager.rotate(-7.5f, 1, 0, 0);
			GlStateManager.scale(0.45, 0.45, 0.45);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			ClientUtils.bindTexture(texture);
			for(ModelRendererTurbo mod : model.baseModel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.baseTransparentModel)
				mod.render(0.0625f);
			GlStateManager.pushMatrix();
			GlStateManager.translate(2.5f, 0.6875f, 0f);
			for(ModelRendererTurbo mod : model.axleModel)
				mod.render(0.0625f);
			GlStateManager.popMatrix();
			for(ModelRendererTurbo mod : model.inserterBaseModel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.inserterMovingPartModel)
				mod.render(0.0625f);
			GlStateManager.translate(2.375f, 0f, 0f);
			for(ModelRendererTurbo mod : model.sawdustModel)
				mod.render(0.0625f);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelSawmill(false);
		modelFlipped = new ModelSawmill(true);
		modelFlipped.flipAllZ();

		modelFlipped.parts.values().forEach(modelRendererTurbos ->
		{
			for(ModelRendererTurbo m : modelRendererTurbos)
			{
				m.rotateAngleX *= -1;
				m.offsetX *= -1;
				m.offsetY *= -1;
				m.offsetZ *= -1;
			}

		});
	}
}
