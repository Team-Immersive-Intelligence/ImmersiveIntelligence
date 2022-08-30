package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityShrapnel;

public class ShrapnelRenderer extends Render<EntityShrapnel>
{
	public ShrapnelRenderer(RenderManager renderManager)
	{
		super(renderManager);
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	@Override
	public void doRender(EntityShrapnel entity, double x, double y, double z, float f0, float f1)
	{

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderHelper.disableStandardItemLighting();

		GlStateManager.rotate(180-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(180-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

		if(entity.shrapnel!=null)
		{
			ClientUtils.bindTexture(ShrapnelHandler.registry.get(entity.shrapnel).texture+".png");
			GlStateManager.translate(-0.125f, -0.25f, 0f);
			ClientUtils.drawTexturedRect(0f, 0f, 0.25f, 0.25f, 0.25, 0.5, 0.25, 0.5);
		}
		RenderHelper.enableStandardItemLighting();
		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityShrapnel entity)
	{
		return new ResourceLocation("immersiveengineering:textures/models/bullet.png");
	}


}