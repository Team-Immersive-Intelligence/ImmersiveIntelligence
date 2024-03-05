package pl.pabilo8.immersiveintelligence.client.render.ammunition;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ammo.IIAmmoRegistry;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;

/**
 * @author Pabilo8
 * @since 08-06-2019
 */
public class AmmoRenderer extends Render<EntityAmmoBase>
{
	public AmmoRenderer(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
		this.shadowSize = 0.5F;
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	public void doRender(EntityAmmoBase entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		GlStateManager.pushMatrix();

		double yy, pp;
		yy = entity.prevRotationYaw+((entity.rotationYaw-entity.prevRotationYaw)*partialTicks);
		pp = entity.prevRotationPitch+((entity.rotationPitch-entity.prevRotationPitch)*partialTicks);

		GlStateManager.translate(x, y, z);
		GlStateManager.rotate((float)yy, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((float)(90+pp), 1.0F, 0.0F, 0.0F);

		if(entity.getAmmoType()!=null)
			IIAmmoRegistry.getModel(entity.getAmmoType()).renderAmmoComplete(entity);

		GlStateManager.popMatrix();
	}

	public boolean isMultipass()
	{
		return false;
	}


	@Override
	protected ResourceLocation getEntityTexture(EntityAmmoBase entity)
	{
		String texture = ImmersiveIntelligence.MODID+":textures/entity/bullet.png";
		return new ResourceLocation(texture);
	}
}
