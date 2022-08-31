package pl.pabilo8.immersiveintelligence.client.fx.particles;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticle;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleRenderer.DrawingStages;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityBullet;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 16-11-2019
 */
public class ParticleTracer extends IIParticle
{
	private final float actualParticleScale;
	private final float[] color;

	public ParticleTracer(World world, Vec3d pos, Vec3d motion, float size, int color)
	{
		super(world, pos, motion);
		setMotion(motion.scale(1/EntityBullet.DEV_SLOMO));
		this.particleScale = size*16f;
		this.actualParticleScale = size;
		this.particleMaxAge = (int)(1/EntityBullet.DEV_SLOMO);
		this.color = IIUtils.rgbIntToRGB(color);
	}

	public void onUpdate()
	{
		if(this.particleAge++ >= this.particleMaxAge)
		{
			this.setExpired();
		}
	}

	@Override
	public int getFXLayer()
	{
		return 3;
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_)
	{
		return 240<<16|240;
	}

	/**
	 * Renders the particle
	 */
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.25f);
		float x = (float)((float)posX-entityIn.posX);
		float y = (float)((float)posY-entityIn.posY);
		float z = (float)((float)posZ-entityIn.posZ);

		float x2 = (float)((float)posX-(motionX)-entityIn.posX);
		float y2 = (float)((float)prevPosY-(motionY)-entityIn.posY);
		float z2 = (float)((float)prevPosZ-(motionZ)-entityIn.posZ);

		float exp = this.actualParticleScale/2f;

		buffer.pos(x, y+exp, z).color(color[0], color[1], color[2], 1f).endVertex();
		buffer.pos(x2, y2+exp, z2).color(color[0], color[1], color[2], 0.125f).endVertex();
		buffer.pos(x, y-exp, z).color(color[0], color[1], color[2], 1f).endVertex();
		buffer.pos(x2, y2-exp, z2).color(color[0], color[1], color[2], 0.125f).endVertex();

		buffer.pos(x, y, z+exp).color(color[0], color[1], color[2], 1f).endVertex();
		buffer.pos(x2, y2, z2+exp).color(color[0], color[1], color[2], 0.125f).endVertex();
		buffer.pos(x, y, z-exp).color(color[0], color[1], color[2], 1f).endVertex();
		buffer.pos(x2, y2, z2-exp).color(color[0], color[1], color[2], 0.125f).endVertex();

		/*
		buffer.pos(x+exp, y+exp, z+exp).color(color[0],color[1],color[2],0f).endVertex();
		buffer.pos(x-exp, y-exp, z-exp).color(color[0],color[1],color[2],0f).endVertex();
		buffer.pos(prevPosX-exp, prevPosY-exp, prevPosZ-exp).color(color[0],color[1],color[2],0f).endVertex();
		buffer.pos(prevPosX+exp, prevPosY+exp, prevPosZ+exp).color(color[0],color[1],color[2],0f).endVertex();
		 */

		//buffer.pos(x, y, z).color(color[0],color[1],color[2],0.5f).endVertex();
	}

	@Nonnull
	@Override
	public DrawingStages getDrawStage()
	{
		return DrawingStages.TRACER;
	}
}