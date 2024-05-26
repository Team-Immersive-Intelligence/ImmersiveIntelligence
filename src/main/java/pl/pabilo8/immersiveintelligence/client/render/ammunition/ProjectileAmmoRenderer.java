package pl.pabilo8.immersiveintelligence.client.render.ammunition;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;

/**
 * @author Pabilo8
 * @updated 09.03.2024
 * @ii-approved 0.3.1
 * @since 08-06-2019
 */
public class ProjectileAmmoRenderer extends Render<EntityAmmoProjectile>
{
	public ProjectileAmmoRenderer(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
		this.shadowSize = 0.5F;
	}

	@Override
	public void doRender(EntityAmmoProjectile entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		IAmmoModel<?, EntityAmmoProjectile> model = AmmoRegistry.getModel(entity.getAmmoType());
		if(model==null)
			return;
		GlStateManager.pushMatrix();

		ClientUtils.bindAtlas();
		double entityPitch = entity.prevRotationPitch+((entity.rotationPitch-entity.prevRotationPitch)*partialTicks);

		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(entityYaw, 0, 1, 0);
		GlStateManager.rotate((float)(90+entityPitch), 1, 0, 0);
		model.renderAmmoComplete(entity, partialTicks);

		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityAmmoProjectile entity)
	{
		return null;
	}
}
