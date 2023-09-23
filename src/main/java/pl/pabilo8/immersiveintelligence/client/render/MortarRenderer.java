package pl.pabilo8.immersiveintelligence.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Mortar;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.model.bullet.ModelBulletMortar6bCal;
import pl.pabilo8.immersiveintelligence.client.model.weapon.ModelMortar;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMortar;

/**
 * @author Pabilo8
 * @since 21.01.2021
 */
public class MortarRenderer extends Render<EntityMortar> implements IReloadableModelContainer<MortarRenderer>
{
	public static MortarItemstackRenderer instance = new MortarItemstackRenderer();
	public static ModelMortar model;
	public static ModelBulletMortar6bCal modelShell;
	public static final ResourceLocation TEXTURE = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/entity/mortar.png");

	public MortarRenderer(RenderManager renderManager)
	{
		super(renderManager);
		subscribeToList("mortar");
	}

	@Override
	public void doRender(EntityMortar entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		//GlStateManager.rotate(-90,0,1,0);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableStandardItemLighting();

		float progress = MathHelper.clamp(((entity.setupTime+partialTicks)/Mortar.setupTime), 0, 1);
		bindTexture(TEXTURE);
		GlStateManager.rotate(-entityYaw+90, 0, 1, 0);

		if(progress==1)
			renderNormal(entity, entityYaw, partialTicks, (entity.shootingProgress+partialTicks)/Mortar.shootTime);
		else
			renderProgress(entity, entityYaw, partialTicks, progress);

		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	private void renderNormal(EntityMortar entity, float entityYaw, float partialTicks, float progress)
	{
		GlStateManager.pushMatrix();

		float pitch = MathHelper.clamp((((entity.rotationPitch)+80)/25f), 0, 1);

		//0.0625f, 35f, 0.60625f, 0.25f
		//-0.25f, 10f, 0f, 0.125f
		float baseDistance = -0.25f+(pitch*0.875f), tubeAngle = 10f+(pitch*25), tubeDistance = 0.60625f*Math.min((pitch/1.025f), 1);
		float bipodHeight = 0.125f+(0.125f*pitch);

		GlStateManager.pushMatrix();

		GlStateManager.translate(baseDistance, 0, 0);

		for(ModelRendererTurbo mod : model.baseModel)
			mod.render();
		GlStateManager.pushMatrix();
		for(ModelRendererTurbo mod : model.baseHandleModel)
		{
			mod.offsetX = 0f;
			mod.rotateAngleZ = 0f;
			mod.render();
		}
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.rotate(tubeAngle, 0, 0, 1);
		for(ModelRendererTurbo mod : model.tubeModel)
			mod.render();
		GlStateManager.translate(0, tubeDistance, 0);
		for(ModelRendererTurbo mod : model.tubeRodsModel)
			mod.render();

		if(progress > 0.2f&&progress < 0.5f)
		{
			float f = (progress-0.2f)/0.3f;

			if(entity.getPassengers().size() > 0)
			{
				Entity psg = entity.getPassengers().get(0);
				if(psg instanceof EntityLivingBase)
				{
					GlStateManager.pushMatrix();
					GlStateManager.translate(0,1.25*(1f-f),0);
					GlStateManager.scale(0.8f,0.8f,0.8f);
					modelShell.renderBulletUnused(((EntityLivingBase)psg).getHeldItem(EnumHand.MAIN_HAND));
					bindTexture(TEXTURE);
					GlStateManager.popMatrix();
				}
			}
		}

		GlStateManager.popMatrix();

		GlStateManager.popMatrix();

		for(ModelRendererTurbo mod : model.bipodModel)
			mod.render();

		for(ModelRendererTurbo mod : model.heightKnobModel)
		{
			mod.rotateAngleX = (1f-pitch)*12.56f;
			mod.render();
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, bipodHeight, 0);
		for(ModelRendererTurbo mod : model.heightRodModel)
			mod.render();
		for(ModelRendererTurbo mod : model.horizontalRodModel)
			mod.render();
		for(ModelRendererTurbo mod : model.horizontalKnobModel)
		{
			mod.rotateAngleZ = pitch*6.28f;
			mod.render();
		}
		for(ModelRendererTurbo mod : model.sightsHolderModel)
		{
			mod.rotateAngleZ = 0.78539816F-(pitch*0.5f);
			mod.render();
		}
		GlStateManager.translate(0, -pitch*0.15f, 0);
		for(ModelRendererTurbo mod : model.sightsModel)
			mod.render();
		GlStateManager.popMatrix();

		GlStateManager.popMatrix();
	}

	private void renderProgress(EntityMortar entity, float entityYaw, float partialTicks, float progress)
	{
		GlStateManager.pushMatrix();
		float baseProgress = MathHelper.clamp(progress/0.1f, 0, 1);
		float bipodProgress = MathHelper.clamp((progress-0.1f)/0.1f, 0, 1);
		float tubeMountProgress = MathHelper.clamp((progress-0.2f)/0.2f, 0, 1);
		float tubeAngleProgress = MathHelper.clamp((progress-0.4f)/0.15f, 0, 1);
		float bipodHeightProgress = MathHelper.clamp((progress-0.65f)/0.15f, 0, 1);
		float sightsMountProgress = MathHelper.clamp((progress-0.8f)/0.1f, 0, 1);
		float sightsKnobProgress = MathHelper.clamp((progress-0.9f)/0.1f, 0, 1);

		if(baseProgress > 0)
		{
			GlStateManager.pushMatrix();
			GlStateManager.color(1f, 1f, 1f, Math.min(baseProgress, 1));
			float v = 1f-baseProgress;
			GlStateManager.translate(v*-0.5-0.25f, v, 0);
			for(ModelRendererTurbo mod : model.baseModel)
				mod.render();
			for(ModelRendererTurbo mod : model.baseHandleModel)
			{
				mod.offsetX = -1f*v;
				mod.rotateAngleZ = 1.25f*v;
				mod.render();
				mod.render();
			}
			GlStateManager.popMatrix();

			if(bipodProgress > 0)
			{
				GlStateManager.pushMatrix();
				GlStateManager.color(1f, 1f, 1f, Math.min(bipodProgress, 1));
				v = 1f-bipodProgress;
				GlStateManager.translate(v*0.35, v*1.5, 0);
				for(ModelRendererTurbo mod : model.bipodModel)
					mod.render();
				for(ModelRendererTurbo mod : model.heightKnobModel)
				{
					mod.rotateAngleX = bipodHeightProgress*12.56f;
					mod.render();
				}

				GlStateManager.translate(0, bipodHeightProgress*0.25f, 0);
				for(ModelRendererTurbo mod : model.heightRodModel)
					mod.render();
				for(ModelRendererTurbo mod : model.horizontalRodModel)
					mod.render();
				for(ModelRendererTurbo mod : model.horizontalKnobModel)
				{
					mod.rotateAngleZ = sightsKnobProgress*6.28f;
					mod.render();
				}

				GlStateManager.pushMatrix();
				GlStateManager.color(1f, 1f, 1f, Math.min(sightsMountProgress, 1));
				GlStateManager.translate(0, 0, (1f-sightsMountProgress)*0.25f);
				for(ModelRendererTurbo mod : model.sightsHolderModel)
				{
					mod.rotateAngleZ = 0.78539816F-(sightsKnobProgress*0.5f);
					mod.render();
				}
				GlStateManager.translate(0, -sightsKnobProgress*0.15f, 0);
				for(ModelRendererTurbo mod : model.sightsModel)
					mod.render();
				GlStateManager.popMatrix();

				GlStateManager.popMatrix();
			}

			if(tubeMountProgress > 0)
			{
				GlStateManager.pushMatrix();
				v = 1f-tubeMountProgress;
				GlStateManager.translate(v*0.35-0.25f, 0, 0);
				GlStateManager.rotate(tubeAngleProgress*10f-(bipodHeightProgress), 0, 0, 1);
				GlStateManager.color(1f, 1f, 1f, Math.min(tubeMountProgress, 1));
				for(ModelRendererTurbo mod : model.tubeModel)
					mod.render();

				GlStateManager.translate(0, -0.125f+(0.25f*bipodHeightProgress), 0);
				for(ModelRendererTurbo mod : model.tubeRodsModel)
					mod.render();
				GlStateManager.popMatrix();
			}
		}

		GlStateManager.popMatrix();
	}

	@Override
	public void reloadModels()
	{
		model = new ModelMortar();
		for(ModelRendererTurbo mod : model.baseHandleModel)
			mod.hasOffset = true;
		modelShell = new ModelBulletMortar6bCal();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMortar entity)
	{
		return null;
	}

	public static class MortarItemstackRenderer extends TileEntityItemStackRenderer
	{
		@Override
		public void renderByItem(ItemStack itemStackIn, float partialTicks)
		{
			GlStateManager.pushMatrix();

			GlStateManager.translate(0.5f, 0f, 0.5f);
			GlStateManager.rotate(-90, 0, 1, 0);
			IIClientUtils.bindTexture(TEXTURE);
			for(ModelRendererTurbo mod : model.baseModel)
				mod.render();

			for(ModelRendererTurbo mod : model.baseHandleModel)
			{
				mod.offsetX = -1f;
				mod.rotateAngleZ = 1.25f;
				mod.render();
				mod.render();
			}

			for(ModelRendererTurbo mod : model.tubeModel)
				mod.render();
			for(ModelRendererTurbo mod : model.tubeRodsModel)
				mod.render();

			GlStateManager.popMatrix();
		}
	}
}
