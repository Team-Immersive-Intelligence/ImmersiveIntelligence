package pl.pabilo8.immersiveintelligence.client.render.ammunition;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;

/**
 * @author Pabilo8
 * @updated 09.03.2024
 * @ii-approved 0.3.1
 * @since 08-06-2019
 */
public class AmmoRenderer extends Render<EntityAmmoBase>
{
	public AmmoRenderer(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
		this.shadowSize = 0.5F;
	}

	@Override
	public void doRender(EntityAmmoBase entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		IAmmoModel model = AmmoRegistry.getModel(entity.getAmmoType());
		if(model==null)
			return;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		if(entity.getAmmoType()!=null)
			model.renderAmmoComplete(entity, partialTicks);

		GlStateManager.popMatrix();
	}


	@Override
	protected ResourceLocation getEntityTexture(EntityAmmoBase entity)
	{
		return null;
	}
}
