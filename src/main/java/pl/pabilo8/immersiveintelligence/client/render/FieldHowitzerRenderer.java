package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Vehicles.FieldHowitzer;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.vehicle.ModelFieldHowitzer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.TmtUtil;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityFieldHowitzer;

public class FieldHowitzerRenderer extends Render<EntityFieldHowitzer> implements IReloadableModelContainer<FieldHowitzerRenderer>
{
	public static ModelFieldHowitzer model = new ModelFieldHowitzer();
	public static final String texture = ImmersiveIntelligence.MODID+":textures/entity/field_howitzer.png";

	public FieldHowitzerRenderer(RenderManager renderManager)
	{
		super(renderManager);
		subscribeToList("field_howitzer");
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	@Override
	public void doRender(EntityFieldHowitzer entity, double x, double y, double z, float f0, float f1)
	{

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableStandardItemLighting();

		float pitch = -entity.gunPitch;//Math.abs(((tt%120)/120f)-0.5f)*-155f;
		float firing = (entity.shootingProgress+(entity.shootingProgress > 0?f1: 0))/FieldHowitzer.fireTime;
		float recoil;
		float gun_recoil = 0;
		float plate_recoil = 0;

		//firing = (tt%50)/50f;
		float trigger = 0;
		if(entity.reloadProgress > 0)
		{
			float reload = (entity.reloadProgress+f1)/FieldHowitzer.reloadTime;
			trigger = (reload > 0.1f?(reload < 0.9f?(reload < 0.2f?(reload-0.1f)/0.1f: 1f): 1f-((reload-0.9f)/0.1f)): 0f);
			plate_recoil = trigger*2f;
		}
		else if(firing > 0.1)
		{
			recoil = firing > 0.6?(firing-0.6f)/0.4f: 0;
			gun_recoil = 1f-Math.abs(Math.min(recoil/0.5f, 1f)-0.5f)/0.5f;
			plate_recoil = 1f-(Math.abs(Math.max((recoil-0.5f)/0.5f, 0f)-0.5f)/0.5f);
			trigger = -(firing > 0.1f?(firing < 0.9f?(firing < 0.2f?(firing-0.1f)/0.1f: 1f): 1f-((firing-0.9f)/0.1f)): 0f);
		}

		float yaw = -entity.rotationYaw;//(tt%60)/60f*360f;

		float towing;
		if(entity.isRiding())
		{
			GlStateManager.rotate(yaw, 0, 1, 0);
			GlStateManager.translate(-0.45, 0.45, 1.65);
			towing=entity.towingOperation?(entity.setupTime/(float)FieldHowitzer.towingTime):0;
		}
		else
		{
			GlStateManager.rotate(yaw, 0, 1, 0);
			GlStateManager.translate(-0.45, 0.45, 0.5);
			towing=1f-((entity.setupTime/(float)FieldHowitzer.setupTime)*1.75f);
		}

		ClientUtils.bindTexture(texture);

		GlStateManager.rotate(towing*-8, 1, 0, 0);
		for(ModelRendererTurbo mod : model.baseModel)
			mod.render(0.0625f);

		for(ModelRendererTurbo mod : model.triggerModel)
		{
			mod.rotateAngleX = trigger;
			mod.render(0.0625f);
		}

		for(ModelRendererTurbo mod : model.pitchThingyModel)
		{
			mod.rotateAngleY = TmtUtil.AngleToTMT(pitch*8);
			mod.render(0.0625f);
		}
		for(ModelRendererTurbo mod : model.wheelAxleModel)
			mod.render(0.0625f);


		GlStateManager.pushMatrix();

		GlStateManager.translate(0.25f, 0.6875f, -0.3125f);
		GlStateManager.rotate(pitch, 1, 0, 0);

		for(ModelRendererTurbo mod : model.gunModel)
			mod.render(0.0625f);

		GlStateManager.translate(0, 0, -gun_recoil*0.25f);
		for(ModelRendererTurbo mod : model.barrelModel)
			mod.render(0.0625f);

		GlStateManager.translate(0.0625, 0.25, 0.385);
		GlStateManager.rotate(35*plate_recoil, 1, 0, 0);
		for(ModelRendererTurbo mod : model.ejectionPlateModel)
			mod.render(0.0625f);

		GlStateManager.popMatrix();

		GlStateManager.rotate(towing*8, 1, 0, 0);

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0, -0.5f);
		GlStateManager.rotate(entity.partWheelLeft.wheelTraverse+(entity.turnLeft ?(f1*1.5f): 0)-(entity.turnRight ?(f1*1.5f): 0), 1, 0, 0);
		for(ModelRendererTurbo mod : model.leftWheelModel)
			mod.render(0.0625f);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0, -0.5f);
		GlStateManager.rotate(entity.partWheelRight.wheelTraverse+(entity.turnRight ?(f1*1.5f): 0)-(entity.turnLeft ?(f1*1.5f): 0), 1, 0, 0);
		for(ModelRendererTurbo mod : model.rightWheelModel)
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
	protected ResourceLocation getEntityTexture(EntityFieldHowitzer entity)
	{
		return new ResourceLocation(texture);
	}

	@Override
	public void reloadModels()
	{
		model = new ModelFieldHowitzer();
	}

	private void renderWheelThingy(float tilt, ModelRendererTurbo[] model)
	{
		GlStateManager.rotate(tilt*-35, 0, 1, 0);
		for(ModelRendererTurbo mod : model)
			mod.render(0.0625f);
	}


}