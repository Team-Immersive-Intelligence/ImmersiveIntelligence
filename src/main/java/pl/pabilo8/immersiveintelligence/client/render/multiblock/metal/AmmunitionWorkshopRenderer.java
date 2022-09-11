package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.api.tool.ConveyorHandler;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.ConveyorDirection;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorBelt;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.models.ModelConveyor;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.metal_device.ModelAdvancedInserter;
import pl.pabilo8.immersiveintelligence.client.model.metal_device.ModelInserter;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelAmmunitionWorkshop;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityAmmunitionWorkshop;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 21-06-2019
 */
public class AmmunitionWorkshopRenderer extends TileEntitySpecialRenderer<TileEntityAmmunitionWorkshop> implements IReloadableModelContainer<AmmunitionWorkshopRenderer>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation("immersiveintelligence:textures/blocks/multiblock/ammunition_workshop.png");
	public static final ResourceLocation TEXTURE_INSERTER = new ResourceLocation("immersiveintelligence:textures/blocks/multiblock/emplacement/inserter_gray.png");
	public static final ResourceLocation TEXTURE_INSERTER2 = new ResourceLocation("immersiveintelligence:textures/blocks/multiblock/emplacement/inserter_gray_advanced.png");

	private static IConveyorBelt con;
	private static ModelAmmunitionWorkshop model;

	private static ModelInserter modelInserter;
	private static ModelAdvancedInserter modelInserterAdvanced;

	@Override
	public void render(@Nullable TileEntityAmmunitionWorkshop te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null)
		{
			if(te.isDummy())
				return;

			IIClientUtils.bindTexture(TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x, (float)y+1, (float)z);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 0, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			//setup
			float progress = te.processTimeMax==0?0: (te.processTime+partialTicks)/(float)te.processTimeMax;

			boolean conveyorAmmoRunning = progress < 0.15;
			float i1Pitch = 35, i1Pitch2 = 135;
			float i2Pitch = 35, i2Pitch2 = 135;
			IAmmo bullet = null;
			IBulletModel bulletModel = null;

			if(te.effect.getItem() instanceof IAmmo)
			{
				bullet = (IAmmo)te.effect.getItem();
				bulletModel = AmmoRegistry.INSTANCE.registeredModels.get(bullet.getName());
			}

			if(progress > 0.15&&progress < 0.65)
			{
				float v = (progress-0.15f)/0.5f;

				final float[] p1 = new float[]{
						35,
						60,
						35,
						95,
						65,
						95,
						60,
						35
				};

				final float[] p2 = new float[]{
						135,
						165,
						135,
						105,
						135,
						105,
						165,
						135
				};

				int e = (int)MathHelper.clamp(Math.floor(v/0.14f), 0, p1.length-2);
				double vv = (v%0.14)/0.14;

				i1Pitch = (float)MathHelper.clampedLerp(Math.min(p1[e], p1[e+1]), Math.max(p1[e], p1[e+1]), p1[e] > p1[e+1]?1f-vv: vv);
				i1Pitch2 = (float)MathHelper.clampedLerp(Math.min(p2[e], p2[e+1]), Math.max(p2[e], p2[e+1]), p2[e] > p2[e+1]?1f-vv: vv);
			}
			else if(progress > 0.7&&progress < 0.9)
			{
				float v = (progress-0.7f)/0.2f;

				final float[] p1 = new float[]{
						35,
						60,
						60,
						60,
						60,
						35
				};

				final float[] p2 = new float[]{
						135,
						165,
						165,
						165,
						165,
						135
				};

				int e = (int)MathHelper.clamp(Math.floor(v/0.16f), 0, p1.length-2);
				double vv = (v%0.16)/0.16;

				i2Pitch = (float)MathHelper.clampedLerp(Math.min(p1[e], p1[e+1]), Math.max(p1[e], p1[e+1]), p1[e] > p1[e+1]?1f-vv: vv);
				i2Pitch2 = (float)MathHelper.clampedLerp(Math.min(p2[e], p2[e+1]), Math.max(p2[e], p2[e+1]), p2[e] > p2[e+1]?1f-vv: vv);
			}


			List<BakedQuad> conveyorAmmo, conveyorCore, conveyorCorner1, conveyorCorner2;
			conveyorAmmo = ModelConveyor.getBaseConveyor(
					EnumFacing.EAST, 1,
					new Matrix4(EnumFacing.EAST), ConveyorDirection.HORIZONTAL,
					ClientUtils.getSprite(conveyorAmmoRunning?con.getActiveTexture(): con.getInactiveTexture()),
					new boolean[]{true, true}, new boolean[]{true, true}, null, 0);

			conveyorCore = ModelConveyor.getBaseConveyor(
					EnumFacing.EAST, 1,
					new Matrix4(EnumFacing.EAST), ConveyorDirection.HORIZONTAL,
					ClientUtils.getSprite(conveyorAmmoRunning?con.getActiveTexture(): con.getInactiveTexture()),
					new boolean[]{true, true}, new boolean[]{true, true}, null, 0);

			conveyorCorner1 = ModelConveyor.getBaseConveyor(
					EnumFacing.SOUTH, 1,
					new Matrix4(EnumFacing.SOUTH), ConveyorDirection.HORIZONTAL,
					ClientUtils.getSprite(conveyorAmmoRunning?con.getActiveTexture(): con.getInactiveTexture()),
					new boolean[]{te.mirrored, !te.mirrored}, new boolean[]{true, true}, null, 0);

			conveyorCorner2 = ModelConveyor.getBaseConveyor(
					EnumFacing.EAST, 1,
					new Matrix4(EnumFacing.EAST), ConveyorDirection.HORIZONTAL,
					ClientUtils.getSprite(conveyorAmmoRunning?con.getActiveTexture(): con.getInactiveTexture()),
					new boolean[]{!te.mirrored, te.mirrored}, new boolean[]{true, true}, null, 0);

			//render

			model.getBlockRotation(te.facing, te.mirrored);

			if(!te.mirrored)
			{
				GlStateManager.scale(1, 1, -1);
				GlStateManager.cullFace(CullFace.FRONT);
			}

			for(ModelRendererTurbo mod : model.baseModel)
				mod.render();

			//GlStateManager.translate();

			ClientUtils.bindAtlas();
			GlStateManager.pushMatrix();

			GlStateManager.translate(0, 0, -3);
			ClientUtils.renderQuads(conveyorAmmo, 1, 1, 1, 1);
			GlStateManager.translate(1, 0, 0);
			ClientUtils.renderQuads(conveyorCorner1, 1, 1, 1, 1);
			GlStateManager.translate(0, 0, 1);
			ClientUtils.renderQuads(conveyorCorner2, 1, 1, 1, 1);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0, -1);
			for(int i = 0; i < 3; i++)
			{
				ClientUtils.renderQuads(conveyorCore, 1, 1, 1, 1);
				GlStateManager.translate(1, 0, 0);
			}
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(2, 0, -2);
			for(int i = 0; i < 4; i++)
			{
				ClientUtils.renderQuads(conveyorAmmo, 1, 1, 1, 1);
				GlStateManager.translate(1, 0, 0);
			}
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(2.5, 0.6875, -2.5);


			//Shell
			final IAmmo finalBullet = bullet;
			final IBulletModel finalBulletModel = bulletModel;

			ClientUtils.mc().getTextureManager().bindTexture(TEXTURE_INSERTER);
			renderInserter(modelInserter,0, i1Pitch, i1Pitch2, 0,
					model!=null?
							() -> {
								if(progress < 0.15||progress > 0.56)
									return;
								GlStateManager.rotate(-90, 1, 0, 0);
								GlStateManager.translate(0, 0, finalBullet.getCaliber()*0.0625*0.5);

								if(progress > 0.15+(5/7f*0.5f))
									finalBulletModel.renderBulletUnused(finalBullet.getCore(te.effect).getColour(), finalBullet.getCoreType(te.effect), -1);
								else if(progress > 0.15+(3/7f*0.5f))
								{
								}
								else if(progress > 0.15+(1/7f*0.5f))
									finalBulletModel.renderCore(finalBullet.getCore(te.effect).getColour(), finalBullet.getCoreType(te.effect));
							}:
							() -> {
							}
			);
			GlStateManager.translate(1, 0, 0);
			//Core
			ClientUtils.mc().getTextureManager().bindTexture(TEXTURE_INSERTER2);
			renderInserter(modelInserterAdvanced,0, i2Pitch, i2Pitch2, 0, () -> {
			});
			GlStateManager.popMatrix();

			if(progress > 0&&bulletModel!=null)
			{

				float convBeginProgress = MathHelper.clamp(progress/0.15f, 0, 1);


				GlStateManager.translate(0.5, 0.125, -0.5);

				if(progress < 0.15+(5/7f*0.5f))
				{
					GlStateManager.pushMatrix();
					GlStateManager.translate(2*Math.min(convBeginProgress*2, 1), 0, 0);
					if(progress < 0.15+(3f/7f*0.5f))
						bulletModel.renderCasing(1, -1);
					else
						bulletModel.renderBulletUnused(bullet.getCore(te.effect).getColour(), bullet.getCoreType(te.effect), -1);
					GlStateManager.popMatrix();
				}

				GlStateManager.translate(0, 0, -2);

				GlStateManager.pushMatrix();

				if(progress < 0.15f)
					GlStateManager.translate((Math.min(convBeginProgress, 0.33f)/0.33f)+(MathHelper.clamp(convBeginProgress-0.66f, 0, 1)/0.33f), 0,
							MathHelper.clamp(convBeginProgress-0.33f, 0, 0.33f)/0.33f);
				else if(progress > 0.9f)
					GlStateManager.translate(3f+((progress-0.9f)/0.1f*2f), 0, 1f);
				else if(progress > 0.65f&&progress < 0.7f)
					GlStateManager.translate(2f+((progress-0.65)/0.05f), 0, 1f);
				else if(progress > 0.7)
					GlStateManager.translate(3f, 0, 1f);
				else
					GlStateManager.translate(2f, 0, 1f);

				if(progress < 0.15+(1/7f*0.5f))
					bulletModel.renderCore(bullet.getCore(te.effect).getColour(), bullet.getCoreType(te.effect));
				else if(progress > 0.15+(6f/7f*0.5f))
					bulletModel.renderBulletUnused(bullet.getCore(te.effect).getColour(), bullet.getCoreType(te.effect), -1);
				GlStateManager.popMatrix();
			}

			GlStateManager.cullFace(CullFace.BACK);

			GlStateManager.popMatrix();
		}
		else
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x-0.25, y-0.25, z);
			GlStateManager.rotate(7.5f, 0, 0, 1);
			GlStateManager.rotate(-7.5f, 1, 0, 0);
			GlStateManager.scale(0.23, 0.23, 0.23);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			IIClientUtils.bindTexture(TEXTURE);

			for(ModelRendererTurbo mod : model.baseModel)
				mod.render();

			GlStateManager.popMatrix();
		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelAmmunitionWorkshop();
		con = ConveyorHandler.getConveyor(new ResourceLocation("immersiveengineering:conveyor"), null);

		modelInserter = new ModelInserter();
		modelInserterAdvanced = new ModelAdvancedInserter();
	}

	public static void renderInserter(ModelInserter inserter, float yaw, float pitch1, float pitch2, float progress, Runnable function)
	{
		GlStateManager.pushMatrix();

		GlStateManager.translate(-0.5, -0.5, 0.5);
		inserter.baseModel[1].render();
		GlStateManager.translate(0.5, 0.385, -0.5);

		GlStateManager.rotate(yaw, 0, 1, 0);
		for(ModelRendererTurbo mod : inserter.inserterBaseTurntable)
			mod.render(0.0625f);

		GlStateManager.translate(0f, 0.125f, 0);

		GlStateManager.rotate(pitch1, 1, 0, 0);
		for(ModelRendererTurbo mod : inserter.inserterLowerArm)
			mod.render(0.0625f);

		GlStateManager.translate(0f, 0.875f, 0);
		GlStateManager.rotate(-pitch1, 1, 0, 0);
		GlStateManager.translate(0f, 0.0625f, 0.03125f);

		for(ModelRendererTurbo mod : inserter.inserterMidAxle)
			mod.render(0.0625f);

		GlStateManager.rotate(pitch2, 1, 0, 0);

		for(ModelRendererTurbo mod : inserter.inserterUpperArm)
			mod.render(0.0625f);

		GlStateManager.translate(0f, 0.625f, 0.03125f);

		GlStateManager.pushMatrix();

		GlStateManager.translate(0.125f, -0.03125f, -0.03125f);

		GlStateManager.rotate(-45.0f*progress, 0f, 0f, 1f);

		for(ModelRendererTurbo mod : inserter.inserterItemPicker1)
			mod.render(0.0625f);

		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();

		GlStateManager.translate(-0.125f, -0.03125f, -0.03125f);

		GlStateManager.rotate(45f*progress, 0f, 0f, 1f);

		for(ModelRendererTurbo mod : inserter.inserterItemPicker2)
			mod.render(0.0625f);

		GlStateManager.popMatrix();

		function.run();

		GlStateManager.popMatrix();
	}
}
