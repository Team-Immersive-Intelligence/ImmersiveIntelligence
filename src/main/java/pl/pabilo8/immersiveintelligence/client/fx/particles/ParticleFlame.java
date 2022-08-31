package pl.pabilo8.immersiveintelligence.client.fx.particles;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticle;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleRenderer.DrawingStages;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 17.07.2020
 */
public class ParticleFlame extends IIParticle
{
	private final float actualParticleScale;

	public ParticleFlame(World world, Vec3d pos, Vec3d motion, float size, int lifeTime)
	{
		super(world, pos, motion);
		this.motionX *= 0.65;
		this.motionY *= 0.25;
		this.motionZ *= 0.65;
		this.particleScale = (float)(size*0.85+(size*0.15*Utils.RAND.nextGaussian()))*2f;
		this.actualParticleScale = this.particleScale;
		this.particleMaxAge = (int)(lifeTime*0.5+(lifeTime*0.5*Utils.RAND.nextGaussian()))+1;
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

		this.setParticleTextureIndex((int)(7-(particleAge/(float)particleMaxAge*6)));
		this.motionY += 0.004D;
		this.move(this.motionX, this.motionY, this.motionZ);

		if(this.posY==this.prevPosY)
		{
			this.motionX *= 1.1D;
			this.motionZ *= 1.1D;
		}

		this.motionX *= 0.9599999785423279D;
		this.motionY *= 0.9599999785423279D;
		this.motionZ *= 0.9599999785423279D;

		if(this.onGround)
		{
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}
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
		setRBGColorF(1f, 0.3f+(0.55f*f), 0);


		setAlphaF(0.25f-((1f-f)*0.1f));
		this.particleScale = this.actualParticleScale*(1-f)*1.5f;
		super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);

		setAlphaF(1f);
		this.particleScale = this.actualParticleScale*(1-f);
		super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);

	}

	@Nonnull
	@Override
	public DrawingStages getDrawStage()
	{
		return DrawingStages.NORMAL;
	}
}