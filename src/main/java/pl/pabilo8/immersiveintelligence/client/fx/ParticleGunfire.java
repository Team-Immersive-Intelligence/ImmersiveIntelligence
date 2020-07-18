package pl.pabilo8.immersiveintelligence.client.fx;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * @author Pabilo8
 * @since 16-11-2019
 */
public class ParticleGunfire extends Particle
{
	private float actualParticleScale;

	public ParticleGunfire(World world, double x, double y, double z, double mx, double my, double mz, float size)
	{
		super(world, x, y, z, mx, my, mz);
		this.motionX = mx*0.025;
		this.motionY = my*0.025;
		this.motionZ = mz*0.025;
		this.particleScale = (float)(size*0.85+(size*0.15*Utils.RAND.nextGaussian()))*2f;
		this.actualParticleScale = this.particleScale;
		this.particleMaxAge = (int)(10+(10*Utils.RAND.nextGaussian()))+1;
	}

	public void onUpdate()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if(this.particleAge++ >= this.particleMaxAge)
		{
			this.setExpired();
		}

		this.move(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.75;
		this.motionY *= 0.75;
		this.motionZ *= 0.75;
	}

	@Override
	public int getFXLayer()
	{
		return 0;
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
		float f = ((float)this.particleAge+partialTicks)/(float)this.particleMaxAge;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		setRBGColorF(1f, 0.3f+(0.7f*f), 0);
		setParticleTextureIndex(3);


		setAlphaF(0.25f-((1f-f)*0.1f));
		this.particleScale = this.actualParticleScale*(1-f)*1.5f;
		super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);

		setAlphaF(1f);
		this.particleScale = this.actualParticleScale*(1-f);
		super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);

	}
}