package pl.pabilo8.immersiveintelligence.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Vehicles.Motorbike;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.rotary.MotorBeltData;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.ITowable;
import pl.pabilo8.immersiveintelligence.client.model.vehicle.ModelMotorbike;
import pl.pabilo8.immersiveintelligence.client.model.vehicle.ModelPanzer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.TmtUtil;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityMotorbike;
import pl.pabilo8.immersiveintelligence.common.item.mechanical.ItemIIMotorBelt.MotorBelt;

public class MotorbikeRenderer extends Render<EntityMotorbike> implements IReloadableModelContainer<MotorbikeRenderer>
{
	public static ModelMotorbike model;
	public static ModelPanzer modelPanzer;
	public static MotorBeltData tracks;

	public static final ResourceLocation TEXTURE = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/entity/motorbike.png");

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
		double d0 = (entity.lastTickPosX-entity.posX)*f1;
		double d1 = (entity.lastTickPosY-entity.posY)*f1;
		double d2 = (entity.lastTickPosZ-entity.posZ)*f1;
		//GlStateManager.translate(x+d0, y+d1, z+d2);
		//GlStateManager.translate(-entity.motionX*f1, 0, -entity.motionZ*f1);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableStandardItemLighting();

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

		float totalWorldTime = entity.getEntityWorld().getTotalWorldTime();
		float engineMove = entity.engineWorking?Math.abs(((totalWorldTime%engineSpeed+f1)/engineSpeed)-0.5f): 0;
		float pipesMove = entity.engineWorking?Math.abs(((totalWorldTime%speed+f1)/speed)-0.5f): 0;
		float plannedRotation = entity.rotationYaw-(tilt!=0?f1*tilt*(speed/5f): 0);
		if(!entity.engineWorking&&(entity.turnLeft||entity.turnRight))
			plannedRotation += tilt*0.25*f1;

		boolean isTowing = entity.getRecursivePassengers().stream().anyMatch(entity1 -> entity1 instanceof ITowable);

		float stepAngle = (float)((entity.partWheelFront.posY-entity.partWheelBack.posY)*12.5f);

		GlStateManager.rotate(stepAngle, 1, 0, 0);
		GlStateManager.rotate(-plannedRotation, 0, 1, 0);
		GlStateManager.rotate(-(tilt*(speed/15f))*25, 0, 0, 1);
		GlStateManager.translate(-0.55, 0.5, 0);


		bindTexture(TEXTURE);
		for(ModelRendererTurbo mod : model.baseModel)
			mod.render(0.0625f);

		switch(entity.upgrade)
		{
			case "storage":
				for(ModelRendererTurbo mod : model.upgradeStorageModel)
					mod.render();
				break;
			case "tank":
				for(ModelRendererTurbo mod : model.upgradeTankModel)
					mod.render();
				break;
			case "woodgas":
				break;
			case "seat":
				for(ModelRendererTurbo mod : model.upgradeSeatModel)
					mod.render();
				break;
		}

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
		float wheelRot = entity.partWheelFront.wheelTraverse+(entity.speed > 0?(f1*entity.speed): 0);
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
		wheelRot = entity.partWheelBack.wheelTraverse+(entity.speed > 0?(f1*entity.speed): 0);
		GlStateManager.rotate(wheelRot*2f, 1, 0, 0);
		for(ModelRendererTurbo mod : model.backWheelModel)
			mod.render(0.0625f);
		GlStateManager.popMatrix();
		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();

		/*GlStateManager.translate(0,4,0);
		double world_rpm = (totalWorldTime%(double)RotaryUtils.getRPMMax())/(double)RotaryUtils.getRPMMax();
		RotaryUtils.tessellateMotorBelt(tracks,8*acceleration,world_rpm);*/

		GlStateManager.popMatrix();

/*
		GlStateManager.translate(0,0.25,0);
		ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/entity/panzer.png");
		GlStateManager.rotate(-plannedRotation, 0, 1, 0);

		for(ModelRendererTurbo m : modelPanzer.baseModel)
			m.render();
		for(ModelRendererTurbo m : modelPanzer.trailerModel)
			m.render();


		for(ModelRendererTurbo m : modelPanzer.leftBackWheelModel)
			m.render();
		for(ModelRendererTurbo m : modelPanzer.rightBackWheelModel)
			m.render();
		for(ModelRendererTurbo m : modelPanzer.leftTrackWheelModels)
			m.render();
		for(ModelRendererTurbo m : modelPanzer.rightTrackWheelModels)
			m.render();

		for(ModelRendererTurbo m : modelPanzer.frontWheelModel)
			m.render();
		for(ModelRendererTurbo m : modelPanzer.backWheelModel)
			m.render();
		for(ModelRendererTurbo m : modelPanzer.leftTrackModel)
			m.render();

		double world_rpm = (totalWorldTime%(double)RotaryUtils.getRPMMax())/(double)RotaryUtils.getRPMMax();

		float rotat_e=TmtUtil.AngleToTMT((float)(world_rpm*360f*8f));
		for(ModelRendererTurbo m : modelPanzer.leftFrontWheelModel)
		{
			//m.rotateAngleZ=rotat_e;

			m.render();
		}
		for(ModelRendererTurbo m : modelPanzer.rightFrontWheelModel)
			m.render();

		for(ModelRendererTurbo m : modelPanzer.trailerModel)
			m.render();


		GlStateManager.pushMatrix();
		GlStateManager.translate(-0.75+0.03125,2.75-0.0625,2.5);
		GlStateManager.rotate(((totalWorldTime%40)/40f*155f)-115f,0,1,0);
		for(ModelRendererTurbo m : modelPanzer.rightTrackModel)
			m.render();
		GlStateManager.translate(0.125+0.0625,-0.75,-0.3125-0.025-0.25);
		GlStateManager.rotate(180,0,1,0);
		MachinegunRenderer.renderMachinegun(ItemStack.EMPTY,null);
		GlStateManager.translate(+0.5-0.125,0,0);
		GlStateManager.scale(-1,1,1);
		GlStateManager.cullFace(CullFace.FRONT);
		MachinegunRenderer.renderMachinegun(ItemStack.EMPTY,null);
		GlStateManager.cullFace(CullFace.BACK);
		ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/entity/panzer.png");
		GlStateManager.popMatrix();

		//turret
		for(ModelRendererTurbo m : modelPanzer.turretModel)
			m.render();
		for(ModelRendererTurbo m : modelPanzer.bodyDoorOpenModel)
			m.render();
		for(ModelRendererTurbo m : modelPanzer.bodyDoorCloseModel)
			m.render();
		for(ModelRendererTurbo m : modelPanzer.barrelModel)
			m.render();


		GlStateManager.pushMatrix();
		GlStateManager.translate(-1.5+0.125,2.5,-0.5);
		GlStateManager.rotate(180,0,1,0);
		GlStateManager.pushMatrix();
		for(int i = 0; i < 4; i++)
		{
			for(int i1 = 0; i1 < 4; i1++)
			{
				MachinegunRenderer.renderMachinegun(ItemStack.EMPTY,null);
				GlStateManager.translate(0,0.25,0);
			}
			GlStateManager.translate(0.25,-1,0);
		}
		GlStateManager.popMatrix();
		GlStateManager.translate(-4.5,0,0);
		GlStateManager.pushMatrix();
		for(int i = 0; i < 4; i++)
		{
			for(int i1 = 0; i1 < 4; i1++)
			{
				MachinegunRenderer.renderMachinegun(ItemStack.EMPTY,null);
				GlStateManager.translate(0,0.25,0);
			}
			GlStateManager.translate(0.25,-1,0);
		}
		GlStateManager.popMatrix();

		ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/entity/panzer.png");
		GlStateManager.popMatrix();



		GlStateManager.translate(1.5,0.25,-4.75);
		RotaryUtils.tessellateMotorBelt(tracks,8*acceleration,world_rpm);
		GlStateManager.translate(-5,0,0);
		RotaryUtils.tessellateMotorBelt(tracks,8*acceleration,world_rpm);
		*/
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityMotorbike entity)
	{
		return TEXTURE;
	}

	@Override
	public void reloadModels()
	{
		model = new ModelMotorbike();
		tracks = MotorBeltData.createBeltData(
				new Vec3d[]{
						new Vec3d(0,0,0),
						new Vec3d(0,0,3),
						new Vec3d(0,-2,1.5)
				},
				new float[]{
						8,8,8
				},
				MotorBelt.STEEL.type
		);

		/*modelPanzer = new ModelPanzer();
		(
				MotorBelt.STEEL.model,
				5,
				6,
				13,
				35,
				30,
				-90,
				new Vec3d[]{
						new Vec3d(0.5, 0.500571428711396, 0.5),
						new Vec3d(0.5, 0.4494106213142892, 1.0),
						new Vec3d(0.5, 0.4061420692593032, 1.5),
						new Vec3d(0.5, 0.3707550876433068, 2.0),
						new Vec3d(0.5, 0.34324093786857546, 2.5),
						new Vec3d(0.5, 0.3235928254848339, 3.0),
						new Vec3d(0.5, 0.31180589851142315, 3.5),
						new Vec3d(0.5, 0.3078772462391335, 4.0),
						new Vec3d(0.5, 0.31180589851142315, 4.5),
						new Vec3d(0.5, 0.3235928254848339, 5.0),
						new Vec3d(0.5, 0.34324093786857546, 5.5),
						new Vec3d(0.5, 0.3707550876433068, 6.0),
						new Vec3d(0.5, 0.4061420692593032, 6.5),
						new Vec3d(0.5, 0.4494106213142892, 7.0625)
				},
				new float[]{
						-0.11022197f, -0.09442547f, -0.0786523f, -0.06289854f, -0.04716032f,
						-0.031433746f, -0.015714932f, 0.0f, 0.015714932f, 0.031433746f,
						0.04716032f, 0.06289854f, 0.0786523f, 0.09442547f},
				new Vec3d(0.5, 0.5, 0.5),
				MotorBelt.STEEL.res.toString()+".png",
				6,
				8,
				8.0f
		)
		 */

	}

	private void renderWheelThingy(float tilt, ModelRendererTurbo[] model)
	{
		GlStateManager.rotate(tilt*-35, 0, 1, 0);
		for(ModelRendererTurbo mod : model)
			mod.render(0.0625f);
	}

}