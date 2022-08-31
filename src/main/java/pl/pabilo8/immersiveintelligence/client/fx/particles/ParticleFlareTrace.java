package pl.pabilo8.immersiveintelligence.client.fx.particles;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticle;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleRenderer.DrawingStages;
import pl.pabilo8.immersiveintelligence.common.IIUtils;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 17.07.2020
 */
public class ParticleFlareTrace extends IIParticle
{
	private final float actualParticleScale;

	public ParticleFlareTrace(World world, Vec3d pos, float size, int colour, int lifeTime)
	{
		super(world, pos, Vec3d.ZERO);
		this.motionY *= 0.25;
		this.particleScale = (float)(size*0.85+(size*0.15*Utils.RAND.nextGaussian()))*2f;
		this.actualParticleScale = this.particleScale;
		this.particleMaxAge = (int)(lifeTime*0.5+(lifeTime*0.5*Utils.RAND.nextGaussian()))+1;

		float[] rgb = IIUtils.rgbIntToRGB(colour);
		this.particleRed = rgb[0];
		this.particleGreen = rgb[1];
		this.particleBlue = rgb[2];
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
		this.motionY += 0.002D;
		this.move(0, this.motionY, 0);

		this.motionY *= 0.9599999785423279D;

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