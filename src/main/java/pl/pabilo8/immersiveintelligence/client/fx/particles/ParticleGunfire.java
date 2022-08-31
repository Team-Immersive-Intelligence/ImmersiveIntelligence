package pl.pabilo8.immersiveintelligence.client.fx.particles;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticle;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleRenderer.DrawingStages;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 16-11-2019
 */
public class ParticleGunfire extends IIParticle
{
	final Vec3d dir;

	public ParticleGunfire(World world, Vec3d pos, Vec3d motion, float size)
	{
		super(world, pos, motion);
		this.dir = motion.normalize();

		this.particleScale = (size+Utils.RAND.nextFloat());
		this.particleMaxAge = (int)(2+(3*Utils.RAND.nextGaussian()));
	}

	public void onUpdate()
	{
		motionX *= 0.6f;
		motionY *= 0.6f;
		motionZ *= 0.6f;

		if(this.particleAge++ >= this.particleMaxAge)
			this.setExpired();
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

		float f = ((float)this.particleAge+partialTicks)/(float)this.particleMaxAge;
		f = MathHelper.clamp(f, 0.0F, 1.0F);

		float[] color = new float[]{1f, 0.2f+(0.75f*f), (0.25f*f)};
		float[] colorEnd = new float[]{1f, 0.3f+(0.55f*f), (0.25f*f)};
		float exp = this.particleScale/16f;

		buffer.pos(x, y+exp, z).color(color[0], color[1], color[2], 1f).endVertex();
		buffer.pos(x2, y2+exp, z2).color(colorEnd[0], colorEnd[1], colorEnd[2], 0.125f).endVertex();
		buffer.pos(x, y-exp, z).color(color[0], color[1], color[2], 1f).endVertex();
		buffer.pos(x2, y2-exp, z2).color(colorEnd[0], colorEnd[1], colorEnd[2], 0.125f).endVertex();

		buffer.pos(x, y, z+exp).color(color[0], color[1], color[2], 1f).endVertex();
		buffer.pos(x2, y2, z2+exp).color(colorEnd[0], colorEnd[1], colorEnd[2], 0.125f).endVertex();
		buffer.pos(x, y, z-exp).color(color[0], color[1], color[2], 1f).endVertex();
		buffer.pos(x2, y2, z2-exp).color(colorEnd[0], colorEnd[1], colorEnd[2], 0.125f).endVertex();

		/*
		buffer.pos(x+exp, y, z-exp).color(color[0],color[1],color[2],0.125f).endVertex();
		buffer.pos(x2+exp, y2, z2-exp).color(color2[0],color2[1],color2[2],1f).endVertex();
		buffer.pos(x2-exp, y2, z2+exp).color(color2[0],color2[1],color2[2],1f).endVertex();
		buffer.pos(x-exp, y, z+exp).color(color[0],color[1],color[2],0.125f).endVertex();
		 */

	}

	@Nonnull
	@Override
	public DrawingStages getDrawStage()
	{
		return DrawingStages.TRACER;
	}
}