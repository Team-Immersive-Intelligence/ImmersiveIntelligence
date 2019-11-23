package pl.pabilo8.immersiveintelligence.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.common.entity.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBullet;

/**
 * Created by Pabilo8 on 08-06-2019.
 */
public class BulletRenderer extends Render<EntityBullet>
{
	private static String texture = ImmersiveIntelligence.MODID+":textures/entity/bullet.png";

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
			size = ItemIIBullet.getCasing(entity.stack).getSize();

		GlStateManager.translate(x+(size/2f), y+(size/2f), z+(size/2f));
		GlStateManager.rotate(180.0F-entityYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(90+entity.rotationPitch, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(-1.0F*size, -1.0F*size, 1.0F*size);

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
		return new ResourceLocation(texture);
	}
}
