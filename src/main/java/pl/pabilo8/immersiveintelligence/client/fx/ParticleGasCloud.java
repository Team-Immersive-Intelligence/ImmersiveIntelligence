package pl.pabilo8.immersiveintelligence.client.fx;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleRenderer.DrawingStages;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 17.07.2020
 */
public class ParticleGasCloud extends IIParticle
{
	private final Fluid fluid;
	public static TextureAtlasSprite TEXTURE;

	public ParticleGasCloud(World world, double x, double y, double z, float size, Fluid fluid)
	{
		super(world, x, y, z);
		this.fluid = fluid;
		motionY = 0.05f;
		this.particleScale = (float)(size*0.85+(size*0.15*Utils.RAND.nextGaussian()));
		this.particleMaxAge = (int)(20+(10*Utils.RAND.nextGaussian()))+1;
		setParticleTexture(TEXTURE);
	}

	public void onUpdate()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		this.motionY += 0.0005D;
		this.move(this.motionX, this.motionY, this.motionZ);

		if(particleAge++ > particleMaxAge)
			this.setExpired();

		if(this.posY==this.prevPosY)
		{
			this.motionX *= 1.1D;
			this.motionZ *= 1.1D;
		}

		this.motionX *= 0.9;
		this.motionY *= 0.9;
		this.motionZ *= 0.9;

		if(this.onGround)
		{
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}
	}

	@Override
	public int getFXLayer()
	{
		return 1;
	}

	/**
	 * Renders the particle
	 */
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		getFluidColor(partialTicks);
		super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
	}

	protected void getFluidColor(float partialTicks)
	{
		particleAlpha = 2f;//Math.min((particleAge+partialTicks)/ (particleMaxAge / 2f)*0.25f, 1);
		float brightness = getBrightnessForRender(partialTicks);
		particleRed = 1*brightness;
		particleGreen = 0*brightness;
		particleBlue = 0*brightness;
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_)
	{
		return super.getBrightnessForRender(p_70070_1_);
	}

	@Nonnull
	@Override
	public ParticleRenderer.DrawingStages getDrawStage()
	{
		return DrawingStages.CUSTOM;
	}

	public static class ParticleFlareFlash extends ParticleGasCloud
	{
		public ParticleFlareFlash(World world, double x, double y, double z, int colour, float size)
		{
			super(world, x, y, z, size, null);
			float[] rgb = pl.pabilo8.immersiveintelligence.api.Utils.rgbIntToRGB(colour);
			this.particleRed = rgb[0];
			this.particleGreen = rgb[1];
			this.particleBlue = rgb[2];
		}

		@Override
		protected void getFluidColor(float partialTicks)
		{
			particleAlpha = 2f; //Math.min((particleAge+partialTicks)/ (particleMaxAge / 2f)*0.25f, 1);
		}

		@Nonnull
		@Override
		public DrawingStages getDrawStage()
		{
			return DrawingStages.CUSTOM_ADDITIVE;
		}
	}
}