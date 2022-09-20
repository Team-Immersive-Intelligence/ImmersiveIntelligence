package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.api.tool.ConveyorHandler;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.ConveyorDirection;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorBelt;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.models.ModelConveyor;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelChemicalPainter;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityChemicalPainter;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 21-06-2019
 */
public class ChemicalPainterRenderer extends TileEntitySpecialRenderer<TileEntityChemicalPainter> implements IReloadableModelContainer<ChemicalPainterRenderer>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/blocks/multiblock/chemical_painter.png");
	private static IConveyorBelt con;
	static RenderItem renderItem = ClientUtils.mc().getRenderItem();
	private static ModelChemicalPainter model;
	private static ModelChemicalPainter modelFlipped;

	@Override
	public void render(@Nullable TileEntityChemicalPainter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			IIClientUtils.bindTexture(TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x, (float)y, (float)z);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			float ff = (getWorld().getTotalWorldTime()%200+partialTicks)/200;

			ModelChemicalPainter modelCurrent = te.mirrored?modelFlipped: model;
			boolean conveyorRunning = true;
			float lifterProgress = 0, lifterRotate = 0, nozzleRotate = 0, nozzleSwivel = 0, lampsProgress = 0, conveyorProgress = 0, itemProgress = 0;

			if(te.active)
			{
				float progress = (te.processTime+partialTicks)/(float)te.processTimeMax;
				conveyorRunning = false;
				if(progress < 0.1) //0.1 conveyor
				{
					conveyorProgress = (progress/0.1f)*0.5f;
					conveyorRunning = true;
				}
				else if(progress < 0.15) //0.05 idle
				{
					conveyorProgress = 0.5f;
				}
				else if(progress < 0.25) //0.1 lift up
				{
					conveyorProgress = 0.5f;
					lifterProgress = ((progress-0.15f)/0.1f);
					nozzleRotate = Math.min(lifterProgress*1.5f, 1f);
				}
				else if(progress < 0.65) //0.4 rotate
				{
					conveyorProgress = 0.5f;
					lifterProgress = 1f;
					nozzleRotate = 1f;
					lifterRotate = ((progress-0.25f)/0.1f)%1f;
					itemProgress = ((progress-0.25f)/0.4f);
					nozzleSwivel = 1;
				}
				else if(progress < 0.75) //0.1 lamps on
				{
					conveyorProgress = 0.5f;
					lifterProgress = 1f;
					itemProgress = 1;
					lampsProgress = (progress-0.65f)/0.1f;
					nozzleSwivel = 1f-((progress-0.65f)/0.1f);
				}
				else if(progress < 0.85) //0.1 lift down
				{
					conveyorProgress = 0.5f;
					lifterProgress = 1f-((progress-0.75f)/0.1f);
					nozzleRotate = Math.max(1f-((progress-0.75f)/0.07f), 0);
					lampsProgress = 1;
					itemProgress = 1;
				}
				else if(progress < 0.9) //0.05 idle
				{
					conveyorProgress = 0.5f;
					lampsProgress = 1;
					itemProgress = 1;
				}
				else //0.1 conveyor, lamps off
				{
					conveyorProgress = 0.5f+(((progress-0.9f)/0.1f)*0.5f);
					conveyorRunning = true;
					lampsProgress = 1f-Math.max(0, (progress-0.95f)/0.05f);
					itemProgress = 1;
				}

			}

			modelCurrent.getBlockRotation(te.facing, te.mirrored);
			for(ModelRendererTurbo mod : modelCurrent.baseModel)
				mod.render();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, lifterProgress*0.4375, 0);
			for(ModelRendererTurbo mod : modelCurrent.conveyorLifterModel)
			{
				mod.rotateAngleY = lifterRotate*6.28f;
				mod.render();
			}
			GlStateManager.popMatrix();

			float ny = (nozzleSwivel > 0?(-0.275f+nozzleSwivel*((Math.abs(((ff%0.2f)/0.2f)-0.5f)*2f)*0.55f)): 0f);
			float nx = (nozzleSwivel > 0?nozzleSwivel*((Math.abs(((ff%0.33f)/0.33f)-0.5f)*2f)*0.55f): 0);
			for(ModelRendererTurbo mod : modelCurrent.nozzleModel)
			{
				mod.rotateAngleY = nx;
				mod.rotateAngleX = nozzleRotate*0.3f+ny;
				mod.render();
			}

			GlStateManager.color(1f-(0.15f*lampsProgress), 1f-(lampsProgress*0.9f), 1f, 1f);
			for(ModelRendererTurbo mod : modelCurrent.lampsModel)
				mod.render();

			GlStateManager.color(1f-(0.1f*lampsProgress), 1f-(lampsProgress*0.35f), 1f, 1f);
			GlStateManager.enableBlend();
			for(ModelRendererTurbo mod : modelCurrent.glassModel)
				mod.render();
			GlStateManager.disableBlend();

			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);


			List<BakedQuad> quads = ModelConveyor.getBaseConveyor(
					te.mirrored?EnumFacing.EAST: EnumFacing.WEST, 1,
					new Matrix4(te.mirrored?EnumFacing.EAST: EnumFacing.WEST), ConveyorDirection.HORIZONTAL,
					ClientUtils.getSprite(conveyorRunning?con.getActiveTexture(): con.getInactiveTexture()), new boolean[]{true, true}, new boolean[]{true, true}, null, 0);
			ClientUtils.bindAtlas();
			GlStateManager.pushMatrix();
			GlStateManager.translate(te.mirrored?0.001f: -0.999, 1f-0.0625f, -2.001f);
			GlStateManager.pushMatrix();
			if(te.active)
			{
				GlStateManager.translate((0.25-3.5*conveyorProgress)*(te.mirrored?-1:1)+(te.mirrored?1:0), 0.385+lifterProgress*0.4375, 0.5);
				GlStateManager.rotate(lifterRotate*360, 0, 1, 0);
				if(te.effect.getItem() instanceof IAmmo)
				{
					IAmmo bullet = (IAmmo)te.effect.getItem();
					IBulletModel bModel = AmmoRegistry.INSTANCE.registeredModels.get(bullet.getName());
					GlStateManager.translate(0, -0.25f, 0);
					bModel.renderBulletUnused(itemProgress > 0.5f?te.effect: te.inventory.get(0));
					ClientUtils.bindAtlas();
				}
				else
					IIClientUtils.drawItemProgress(te.inventory.get(0), te.effect, itemProgress, TransformType.NONE, Tessellator.getInstance(), 0.5f);
			}
			GlStateManager.popMatrix();

			for(int i = 0; i < 5; i++)
			{
				if(i!=2)
				{
					if(i==0||i==4)
					{
						GlStateManager.pushMatrix();
						GlStateManager.translate(0, 0.0625f, 0);
						ClientUtils.renderQuads(quads, 1, 1, 1, 1);
						GlStateManager.popMatrix();
					}
					else
						ClientUtils.renderQuads(quads, 1, 1, 1, 1);
				}
				else
				{
					GlStateManager.pushMatrix();
					GlStateManager.translate(0.5f, lifterProgress*0.4375, 0.5f);
					GlStateManager.rotate(lifterRotate*360, 0, 1, 0);
					GlStateManager.translate(-0.5f, 0, -0.5f);

					ClientUtils.renderQuads(quads, 1, 1, 1, 1);
					GlStateManager.popMatrix();
				}
				GlStateManager.translate(te.mirrored?1f: -1, 0f, 0f);

			}
			GlStateManager.popMatrix();


			GlStateManager.popMatrix();

			//IILogger.info(ImmersiveEngineering.proxy.drawConveyorInGui("immersiveengineering:conveyor", te.facing));
		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelChemicalPainter();
		model.flipAllX();
		for(ModelRendererTurbo mod : model.baseModel)
		{
			if(mod.flip)
				mod.rotationPointY *= -1;
		}

		model.glassModel[9].rotateAngleY += 3.14f;
		model.glassModel[10].rotateAngleY += 3.14f;


		modelFlipped = new ModelChemicalPainter();
		con = ConveyorHandler.getConveyor(new ResourceLocation("immersiveengineering:conveyor"), null);

	}
}
