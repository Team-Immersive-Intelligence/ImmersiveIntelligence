package pl.pabilo8.immersiveintelligence.client.render.vehicle;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.ITowable;
import pl.pabilo8.immersiveintelligence.client.model.vehicle.ModelMotorbike;
import pl.pabilo8.immersiveintelligence.client.render.IPassengerAnimationsRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityMotorbike;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleWheel;

public class MotorbikeRenderer extends Render<EntityMotorbike> implements IReloadableModelContainer<MotorbikeRenderer>, IPassengerAnimationsRenderer<EntityMotorbike>
{
	public static final ResourceLocation TEXTURE = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/entity/motorbike.png");
	public static ModelMotorbike model;

	public MotorbikeRenderer(RenderManager renderManager)
	{
		super(renderManager);
		subscribeToList("motorbike");
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	@Override
	public void doRender(EntityMotorbike entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableStandardItemLighting();

		EntityVehicleWheel<EntityMotorbike> frontWheel = entity.partWheelFront;
		EntityVehicleWheel<EntityMotorbike> backWheel = entity.partWheelBack;

		boolean isTowing = entity.getRecursivePassengers().stream().anyMatch(entity1 -> entity1 instanceof ITowable);

		GlStateManager.rotate(-entityYaw, 0, 1, 0);
		GlStateManager.rotate(-entity.rotationPitch, 1, 0, 0);
		GlStateManager.translate(-0.55, 0.5, 0);


		bindTexture(TEXTURE);
		for(ModelRendererTurbo mod : model.baseModel)
			mod.render(0.0625f);

		if(entity.isUpgradeInstalled(IIContent.UPGRADE_VEHICLE_SMALL_STORAGE))
			for(ModelRendererTurbo mod : model.upgradeStorageModel)
				mod.render();
		else if(entity.isUpgradeInstalled(IIContent.UPGRADE_VEHICLE_SMALL_ADDITIONAL_TANK))
			for(ModelRendererTurbo mod : model.upgradeTankModel)
				mod.render();
		else if(entity.isUpgradeInstalled(IIContent.UPGRADE_VEHICLE_ADDITIONAL_PASSENGER_SEAT))
			for(ModelRendererTurbo mod : model.upgradeSeatModel)
				mod.render();

		if(isTowing)
			for(ModelRendererTurbo mod : model.trailerThingyModel)
				mod.render(0.0625f);

		GlStateManager.pushMatrix();
		for(ModelRendererTurbo mod : model.engineModel)
			mod.render(0.0625f);
		GlStateManager.color(1f, 1f, 1f);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();

		for(ModelRendererTurbo mod : model.exhaustPipesModel)
			mod.render(0.0625f);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5625f, 15/16f, 1f-0.125f);
		GlStateManager.rotate(-18, 1, 0, 0);
		GlStateManager.rotate(-entity.partWheelFront.getSteeringAngle(), 0, 1, 0);
		GlStateManager.translate(0, -15/16f, 0);
		GlStateManager.rotate(18, 1, 0, 0);


		for(ModelRendererTurbo mod : model.frontThingyModel)
			mod.render(0.0625f);

		for(ModelRendererTurbo mod : model.frontThingyUpperModel)
			mod.render(0.0625f);

		GlStateManager.pushMatrix();
		GlStateManager.translate(0.125f, 0F, 0f);
		GlStateManager.rotate(frontWheel.getWheelTraverse()*2f, 1, 0, 0);

		for(ModelRendererTurbo mod : model.frontWheelModel)
			mod.render(0.0625f);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 20F/16f, -0.385);
		for(ModelRendererTurbo mod : model.steeringGearModel)
			mod.render(0.0625f);

		model.accelerationModel[0].rotateAngleX = 0.06981317F;
		for(ModelRendererTurbo mod : model.accelerationModel)
			mod.render(0.0625f);
		GlStateManager.translate(6/16f, 0, 0);
		model.brakeModel[0].rotateAngleX = 0.06981317F;
		for(ModelRendererTurbo mod : model.brakeModel)
			mod.render(0.0625f);
		GlStateManager.popMatrix();


		GlStateManager.popMatrix();


		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5625f, 0F, -1.1875f-0.125);
		GlStateManager.rotate(backWheel.getWheelTraverse(), 1, 0, 0);
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
		return TEXTURE;
	}

	@Override
	public void reloadModels()
	{
		model = new ModelMotorbike();
	}

	@Override
	public boolean handleBipedRotations(ModelBiped model, EntityMotorbike entity, EntityLivingBase passenger, float partialTicks)
	{
		Entity riding = passenger.getRidingEntity();
		if(!(riding instanceof EntityVehicleSeat))
			return false;
		EntityVehicleSeat seat = (EntityVehicleSeat)riding;

		if(seat.seatID.equals("rider"))
		{
			model.bipedBody.rotateAngleX += 0.25f;
			model.bipedRightLeg.rotationPointZ = 2;
			model.bipedLeftLeg.rotationPointZ = 2;

			model.bipedRightArm.rotateAngleZ = 0;
			model.bipedLeftArm.rotateAngleZ = 0;

			model.bipedRightArm.rotationPointZ = -1.5f;
			model.bipedLeftArm.rotationPointZ = -1.5f;

			model.bipedRightArm.rotateAngleX = -1.35f;
			model.bipedRightArm.rotateAngleY = 0.5f;
			model.bipedLeftArm.rotateAngleX = -1.35f;
			model.bipedLeftArm.rotateAngleY = -0.5f;
		}

		model.bipedRightLeg.rotateAngleY = 0.65f;
		model.bipedLeftLeg.rotateAngleY = -0.65f;
		model.bipedRightLeg.rotateAngleX = -0.65f;
		model.bipedLeftLeg.rotateAngleX = -0.65f;
		return true;
	}
}
