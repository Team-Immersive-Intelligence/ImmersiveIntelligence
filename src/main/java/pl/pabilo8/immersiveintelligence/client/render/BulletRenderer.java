package pl.pabilo8.immersiveintelligence.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;

/**
 * @author Pabilo8
 * @since 08-06-2019
 */
public class BulletRenderer extends Render<EntityBullet>
{

	public BulletRenderer(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
		this.shadowSize = 0.5F;
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	public void doRender(EntityBullet entity, double x, double y, double z, float entityYaw, float partialTicks)
	{

		GlStateManager.pushMatrix();

		float size = 1;
		if(!entity.stack.isEmpty())
			size = entity.getSize();

		GlStateManager.translate(x, y+0.25, z);
		GlStateManager.rotate(entityYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(90+entity.rotationPitch, 1.0F, 0.0F, 0.0F);

		GlStateManager.scale(size, size, size);

		if(entity.name!=null)
			BulletRegistry.INSTANCE.registeredModels.get(entity.name).renderBullet(entity.colourCore, entity.colourPaint);

		GlStateManager.popMatrix();

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	public boolean isMultipass()
	{
		return false;
	}


	@Override
	protected ResourceLocation getEntityTexture(EntityBullet entity)
	{
		String texture = ImmersiveIntelligence.MODID+":textures/entity/bullet.png";
		return new ResourceLocation(texture);
	}
}
