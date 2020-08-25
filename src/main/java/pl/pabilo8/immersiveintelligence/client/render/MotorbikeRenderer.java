/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Vehicles.Motorbike;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.ITowable;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelMotorbike;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.tmt.TmtUtil;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMotorbike;

public class MotorbikeRenderer extends Render<EntityMotorbike> implements IReloadableModelContainer<MotorbikeRenderer>
{
	public static ModelMotorbike model = new ModelMotorbike();
	public static final String texture = ImmersiveIntelligence.MODID+":textures/entity/motorbike.png";

	public MotorbikeRenderer(RenderManager renderManager)
	{
		super(renderManager);
		subscribeToList("motorbike");
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	@Override
	public void doRender(EntityMotorbike entity, double x, double y, double z, float f0, float f1)
	{

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableStandardItemLighting();

		float wheelRot = entity.wheelTraverse+(entity.speed > 0?(f1*entity.speed): 0);
		float tilt = entity.tilt;
		if(entity.turnLeft)
			tilt -= 0.1f*f1;
		else if(entity.turnRight)
			tilt += 0.1f*f1;
		else if(tilt!=0)
		{
			tilt = tilt < 0?tilt+(0.1f*f1): tilt-(0.1f*f1);
			if(Math.abs(tilt) < 0.01f)
				tilt = 0;
		}
		tilt = MathHelper.clamp(tilt, -1f, 1f);

		float speed = 4, engineSpeed = speed/2;
		float acceleration = entity.acceleration;
		if(entity.engineWorking&&entity.accelerated)
			acceleration = Math.min(acceleration+(0.1f*f1), 1f);
		else
			acceleration = Math.max(acceleration-(0.15f*f1), 0f);

		float brake = entity.brakeProgress;
		if(entity.engineWorking&&entity.brake)
			brake = Math.min(brake+(0.15f*f1), 1f);
		else
			brake = Math.max(brake-(0.25f*f1), 0f);

		float totalWorldTime = entity.getEntityWorld().getTotalWorldTime()+f1;
		float engineMove = entity.engineWorking?Math.abs((totalWorldTime%engineSpeed/engineSpeed)-0.5f): 0;
		float pipesMove = entity.engineWorking?Math.abs((totalWorldTime%speed/speed)-0.5f): 0;
		float plannedRotation = entity.rotationYaw-(tilt!=0?f1*tilt*(speed/5f): 0);
		if(!entity.engineWorking&&(entity.turnLeft||entity.turnRight))
			plannedRotation += tilt*0.25*f1;

		boolean isTowing = entity.getRecursivePassengers().stream().anyMatch(entity1 -> entity1 instanceof ITowable);

		float stepAngle = (float)((entity.partWheelFront.posY-entity.partWheelBack.posY)*12.5f);

		GlStateManager.rotate(stepAngle, 1, 0, 0);
		GlStateManager.rotate(-plannedRotation, 0, 1, 0);
		GlStateManager.rotate(-tilt*25, 0, 0, 1);
		GlStateManager.translate(-0.55, 0.5, 0);


		ClientUtils.bindTexture(texture);
		for(ModelRendererTurbo mod : model.baseModel)
			mod.render(0.0625f);

		if(isTowing)
			for(ModelRendererTurbo mod : model.trailerThingyModel)
				mod.render(0.0625f);

		GlStateManager.pushMatrix();
		float partDurability = entity.engineDurability/(float)Motorbike.engineDurability;
		GlStateManager.translate(engineMove*0.03125f*partDurability, -engineMove*0.03125f*partDurability, 0);
		GlStateManager.color(partDurability, partDurability, partDurability);
		for(ModelRendererTurbo mod : model.engineModel)
			mod.render(0.0625f);
		GlStateManager.color(1f, 1f, 1f);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, pipesMove*0.03125f*partDurability, 0);

		for(ModelRendererTurbo mod : model.exhaustPipesModel)
			mod.render(0.0625f);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5625f, 15/16f, 1f-0.125f);
		GlStateManager.rotate(-18, 1, 0, 0);
		GlStateManager.rotate(tilt*-35f, 0, 1, 0);
		GlStateManager.translate(0, -15/16f, 0);
		GlStateManager.rotate(18, 1, 0, 0);


		for(ModelRendererTurbo mod : model.frontThingyModel)
			mod.render(0.0625f);

		for(ModelRendererTurbo mod : model.frontThingyUpperModel)
			mod.render(0.0625f);

		GlStateManager.pushMatrix();
		GlStateManager.translate(0.125f, 0F, 0f);
		GlStateManager.rotate(wheelRot*2f, 1, 0, 0);

		for(ModelRendererTurbo mod : model.frontWheelModel)
			mod.render(0.0625f);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 20F/16f, -0.385);
		for(ModelRendererTurbo mod : model.steeringGearModel)
			mod.render(0.0625f);

		model.accelerationModel[0].rotateAngleX = 0.06981317F-TmtUtil.AngleToTMT(55f*acceleration);
		for(ModelRendererTurbo mod : model.accelerationModel)
			mod.render(0.0625f);
		GlStateManager.translate(6/16f, 0, 0);
		model.brakeModel[0].rotateAngleX = 0.06981317F+TmtUtil.AngleToTMT(55f*brake);
		for(ModelRendererTurbo mod : model.brakeModel)
			mod.render(0.0625f);
		GlStateManager.popMatrix();


		GlStateManager.popMatrix();


		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5625f, 0F, -1.1875f-0.125);
		GlStateManager.rotate(wheelRot*2f, 1, 0, 0);
		for(ModelRendererTurbo mod : model.backWheelModel)
			mod.render(0.0625f);
		GlStateManager.popMatrix();


		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityMotorbike entity)
	{
		return new ResourceLocation(texture);
	}

	@Override
	public void reloadModels()
	{
		model = new ModelMotorbike();
	}

	private void renderWheelThingy(float tilt, ModelRendererTurbo[] model)
	{
		GlStateManager.rotate(tilt*-35, 0, 1, 0);
		for(ModelRendererTurbo mod : model)
			mod.render(0.0625f);
	}

}