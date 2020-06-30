package pl.pabilo8.immersiveintelligence.client.fx; /**
 * @author Pabilo8
 * @since 16-11-2019
 */

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ParticleGunfire extends Particle
{
	//colour ints, interpolated when needed

	public ParticleGunfire(World world, double x, double y, double z, double mx, double my, double mz, float size)
	{
		super(world, x, y, z, mx, my, mz);
		this.particleMaxAge = 16;
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.motionX = mx;
		this.motionY = my;
		this.motionZ = mz;
		this.setParticleTextureIndex(Utils.RAND.nextInt(3));
		this.setSize(size, size);
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
		this.setRBGColorF(1f, particleAge/16f, 0f);
		super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
	}
}