package pl.pabilo8.immersiveintelligence.client.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.common.entity.EntityAtomicBoom;

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

		if(entity.ticksExisted > 0)
		{
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			RenderHelper.disableStandardItemLighting();

			/*
			if(entity.ticksExisted < 50*entity.size)
			{

			}
			 */
		}

		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityAtomicBoom entity)
	{
		return null;
	}
}
