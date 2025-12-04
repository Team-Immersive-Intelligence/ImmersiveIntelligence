package pl.pabilo8.immersiveintelligence.client.render.entity.weapon;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelTripodPeriscope;
import pl.pabilo8.immersiveintelligence.client.render.IPassengerAnimationsRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools.TripodPeriscope;
import pl.pabilo8.immersiveintelligence.common.entity.EntityTripodPeriscope;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.01.2021
 */
public class TripodPeriscopeRenderer extends Render<EntityTripodPeriscope> implements IReloadableModelContainer<TripodPeriscopeRenderer>, IPassengerAnimationsRenderer<EntityTripodPeriscope>
{
	public static final String texture = ImmersiveIntelligence.MODID+":textures/entity/tripod_periscope.png";
	public static TripodPeriscopeItemstackRenderer instance = new TripodPeriscopeItemstackRenderer();
	public static ModelTripodPeriscope model = new ModelTripodPeriscope();

	public TripodPeriscopeRenderer(RenderManager renderManager)
	{
		super(renderManager);
		subscribeToList("tripod_periscope");
	}

	@Override
	public void doRender(EntityTripodPeriscope entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableStandardItemLighting();

		float progress = MathHelper.clamp(((entity.setupTime+partialTicks)/TripodPeriscope.setupTime), 0, 1);
		ClientUtils.bindTexture(texture);

		if(progress==1)
			renderNormal(entity, entityYaw, partialTicks, progress);
		else
			renderProgress(entity, entityYaw, partialTicks, progress);

		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	private void renderNormal(EntityTripodPeriscope entity, float entityYaw, float partialTicks, float progress)
	{
		GlStateManager.pushMatrix();
		GlStateManager.rotate(entityYaw, 0, 1, 0);

		for(ModelRendererTurbo mod : model.baseModel)
			mod.render();
		for(ModelRendererTurbo mod : model.leg1Model)
			mod.render();
		for(ModelRendererTurbo mod : model.leg2Model)
			mod.render();
		for(ModelRendererTurbo mod : model.leg3Model)
			mod.render();

		float yy = MathHelper.wrapDegrees(360+entity.periscopeNextYaw-entity.periscopeYaw);
		float yaw = MathHelper.wrapDegrees(entity.periscopeYaw+(Math.signum(yy)*MathHelper.clamp(Math.abs(yy), 0, TripodPeriscope.turnSpeed*partialTicks)));

		GlStateManager.popMatrix();
		GlStateManager.rotate(-yaw+90, 0, 1, 0);

		for(ModelRendererTurbo mod : model.periscopeModel)
			mod.render();

		for(ModelRendererTurbo mod : model.leverModel)
			mod.render();
	}

	private void renderProgress(EntityTripodPeriscope entity, float entityYaw, float partialTicks, float progress)
	{
		float tripodProgress = MathHelper.clamp(progress/0.25f, 0, 1);
		float tripodLegProgress = (MathHelper.clamp(progress/0.3f, 0, 1)*0.5934119f)-0.29670597F;
		float periscopePlacementProgress = MathHelper.clamp((progress-0.25f)/0.2f, 0, 1);
		float periscopeRotationProgress = MathHelper.clamp((progress-0.45f)/0.25f, 0, 1);
		float leverProgress = MathHelper.clamp((progress-0.7f)/0.3f, 0, 1);

		GlStateManager.pushMatrix();
		GlStateManager.rotate(entityYaw, 0, 1, 0);
		GlStateManager.color(1f, 1f, 1f, Math.min(tripodProgress, 1));
		GlStateManager.translate(0, (1f-tripodProgress)*1.5f, 0);
		for(ModelRendererTurbo mod : model.baseModel)
			mod.render();

		for(ModelRendererTurbo mod : model.leg1Model)
		{
			mod.rotateAngleX = tripodLegProgress;
			mod.render();
		}
		for(ModelRendererTurbo mod : model.leg2Model)
		{
			mod.rotateAngleX = -tripodLegProgress;
			mod.render();
		}
		for(ModelRendererTurbo mod : model.leg3Model)
		{
			mod.rotateAngleX = tripodLegProgress;
			mod.render();
		}

		GlStateManager.popMatrix();
		GlStateManager.color(1f, 1f, 1f, Math.min(periscopePlacementProgress, 1));
		GlStateManager.translate(0.0625f/2f, (1f-periscopePlacementProgress)*1.5f, -0.0625f/2f);
		GlStateManager.rotate(periscopeRotationProgress*720, 0, 1, 0);
		GlStateManager.rotate(-entity.periscopeYaw+90, 0, 1, 0);
		if(tripodProgress > 0)
		{
			for(ModelRendererTurbo mod : model.periscopeModel)
				mod.render();

			for(ModelRendererTurbo mod : model.leverModel)
			{
				mod.setRotationAngle(1.57f+(3.14f*leverProgress), 0, 0);
				mod.render();
			}
		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelTripodPeriscope();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTripodPeriscope entity)
	{
		return null;
	}

	@Override
	public boolean handleBipedRotations(ModelBiped model, EntityTripodPeriscope mg, EntityLivingBase passenger, float partialTicks)
	{
		float true_head_angle = MathHelper.wrapDegrees(passenger.prevRotationYawHead);
		float wtime = Math.abs((mg.getEntityWorld().getTotalWorldTime()+partialTicks)%40/40f-0.5f)/0.5f-0.5f;

		model.bipedRightArm.rotateAngleX = -1.75f;
		model.bipedRightArm.rotateAngleY = -0.5f;

		model.bipedLeftArm.rotateAngleX = -2.25f;
		model.bipedLeftArm.rotateAngleY = 0.25f;

		model.bipedHead.rotateAngleX = 0;
		model.bipedHeadwear.rotateAngleX = 0;
		model.bipedHead.rotateAngleY = 0;
		model.bipedHeadwear.rotateAngleY = 0;


		if(Math.abs(mg.periscopeYaw-true_head_angle) > 5)
			if(mg.periscopeYaw < true_head_angle)
			{
				model.bipedRightLeg.rotateAngleZ = -(1f-wtime)*0.25f;
				model.bipedLeftLeg.rotateAngleZ = -wtime*0.25f-0.25f;
			}
			else if(mg.periscopeYaw > true_head_angle)
			{
				model.bipedRightLeg.rotateAngleZ = (1f-wtime)*0.25f+0.25f;
				model.bipedLeftLeg.rotateAngleZ = wtime*0.25f;
			}
		return true;
	}

	public static class TripodPeriscopeItemstackRenderer extends TileEntityItemStackRenderer
	{
		@Override
		public void renderByItem(ItemStack itemStackIn, float partialTicks)
		{
			GlStateManager.pushMatrix();

			GlStateManager.translate(0.5f, 0f, 0.5f);
			ClientUtils.bindTexture(texture);
			for(ModelRendererTurbo mod : model.baseModel)
				mod.render();

			for(ModelRendererTurbo mod : model.leg1Model)
			{
				mod.rotateAngleX = 0.29670597F;
				mod.render();
			}
			for(ModelRendererTurbo mod : model.leg2Model)
			{
				mod.rotateAngleX = -0.29670597F;
				mod.render();
			}
			for(ModelRendererTurbo mod : model.leg3Model)
			{
				mod.rotateAngleX = 0.29670597F;
				mod.render();
			}

			GlStateManager.popMatrix();
		}
	}
}
