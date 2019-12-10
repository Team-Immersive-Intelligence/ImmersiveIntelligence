package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.wooden.ModelSkyCrate;
import pl.pabilo8.immersiveintelligence.common.entity.EntitySkyCrate;

/**
 * Created by Pabilo8 on 08-06-2019.
 */
public class SkyCrateRenderer extends Render<EntitySkyCrate>
{
	private static String texture = ImmersiveIntelligence.MODID+":textures/entity/skycrate.png";

	protected ModelSkyCrate model = new ModelSkyCrate();

	public SkyCrateRenderer(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
		this.shadowSize = 0.5F;
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	public void doRender(EntitySkyCrate entity, double x, double y, double z, float entityYaw, float partialTicks)
	{

		GlStateManager.pushMatrix();

		ClientUtils.bindTexture(texture);

		GlStateManager.translate(x, y+0.375F, z);
		GlStateManager.rotate(180.0F-entityYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);

		ImmersiveIntelligence.logger.info("o");
		model.render();

		GlStateManager.popMatrix();

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	public boolean isMultipass()
	{
		return false;
	}


	@Override
	protected ResourceLocation getEntityTexture(EntitySkyCrate entity)
	{
		return new ResourceLocation(texture);
	}
}
