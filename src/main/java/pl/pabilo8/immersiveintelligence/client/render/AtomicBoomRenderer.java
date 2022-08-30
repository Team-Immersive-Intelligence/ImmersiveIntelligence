package pl.pabilo8.immersiveintelligence.client.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityAtomicBoom;

import java.util.Random;

/**
 * @author Pabilo8
 * @since 19.12.2020
 */
public class AtomicBoomRenderer extends Render<EntityAtomicBoom>
{
	public AtomicBoomRenderer(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	public void doRender(EntityAtomicBoom entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		RenderHelper.disableStandardItemLighting();
		float f = ((float)entity.progress+partialTicks)/400.0F;
		float f1 = entity.progress>380?(1f-Math.max((entity.progress+partialTicks-380)/20f,0)):(Math.min((entity.progress+partialTicks)/40f,1));

		Random random = new Random(432L);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.shadeModel(7425);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GlStateManager.disableAlpha();
		GlStateManager.enableCull();
		GlStateManager.depthMask(false);
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, -1.0F, 0F);

		for(int i = 0; (float)i < 60; ++i)
		{
			GlStateManager.rotate(random.nextFloat()*180.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat()*360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat()*180.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat()*360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat()*360.0F+f*25.0F, 0.0F, 0.0F, 1.0F);
			float f2 = random.nextFloat()*35f*f1;
			float f3 = random.nextFloat()*15.0F*f1;
			bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
			bufferbuilder.pos(0.0D, 0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
			bufferbuilder.pos(-0.866D*f3, f2, -0.5F*(double)f3).color(255, 127, 55, 0).endVertex();
			bufferbuilder.pos(0.866D*f3, f2, -0.5F*(double)f3).color(255, 127, 55, 0).endVertex();
			bufferbuilder.pos(0.0D, f2, 1.0F*(double)f3).color(255, 127, 55, 0).endVertex();
			bufferbuilder.pos(-0.866D*(double)f3, f2, (double)-0.5F*f3).color(255, 127, 55, 0).endVertex();
			tessellator.draw();
		}

		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		GlStateManager.shadeModel(7424);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();
		RenderHelper.enableStandardItemLighting();

		GlStateManager.popMatrix();
	}


	@Override
	protected ResourceLocation getEntityTexture(EntityAtomicBoom entity)
	{
		return null;
	}
}
