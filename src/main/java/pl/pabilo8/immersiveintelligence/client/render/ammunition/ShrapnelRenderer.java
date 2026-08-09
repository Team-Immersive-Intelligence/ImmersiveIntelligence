package pl.pabilo8.immersiveintelligence.client.render.ammunition;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityShrapnel;

/**
 * Renders a shrapnel particle
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 06.08.2026
 * @ii-approved 0.3.1
 * @since 26.10.2019
 */
public class ShrapnelRenderer extends Render<EntityShrapnel>
{
	private static final ResourceLocation SHRAPNEL_TEXTURE = new ResourceLocation(
			ImmersiveIntelligence.MODID, "textures/entity/shrapnel.png"
	);

	public ShrapnelRenderer(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	public void doRender(EntityShrapnel entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		if(entity.shrapnel==null)
			return;

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderHelper.disableStandardItemLighting();

		GlStateManager.rotate(180-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(180-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		entity.shrapnel.color.glColor();

		ClientUtils.mc().getTextureManager().bindTexture(SHRAPNEL_TEXTURE);
		GlStateManager.translate(-0.125f, -0.25f, 0f);
		ClientUtils.drawTexturedRect(-0.5f, -0.5f, 1f, 1f, 0, 1, 0, 1);

		GlStateManager.color(1f, 1f, 1f, 1f);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityShrapnel entity)
	{
		return SHRAPNEL_TEXTURE;
	}
}
