package pl.pabilo8.immersiveintelligence.client.fx.particles;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResource;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticle;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleRenderer.DrawingStages;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.IIUtils;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * @author Pabilo8
 * @since 17.07.2020
 */
public class ParticleGasCloud extends IIParticle
{
	private final Fluid fluid;
	public static TextureAtlasSprite TEXTURE;
	public static final HashMap<Fluid, float[]> CACHED_COLORS = new HashMap<>();

	public ParticleGasCloud(World world, Vec3d pos, float size, Fluid fluid)
	{
		super(world, pos);
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
		particleAlpha = 1f;//Math.min((particleAge+partialTicks)/ (particleMaxAge / 2f)*0.25f, 1);
		float brightness = MathHelper.clamp(getBrightnessForRender(partialTicks), 0, 1);
		final float[] fluidColor = getFluidColor(fluid);
		this.particleRed = fluidColor[0]*brightness;
		this.particleGreen = fluidColor[1]*brightness;
		this.particleBlue = fluidColor[2]*brightness;

	}

	@Override
	public int getBrightnessForRender(float p_70070_1_)
	{
		return super.getBrightnessForRender(p_70070_1_);
	}

	@Nonnull
	@Override
	public DrawingStages getDrawStage()
	{
		return DrawingStages.CUSTOM;
	}

	public static class ParticleFlareFlash extends ParticleGasCloud
	{
		public ParticleFlareFlash(World world, Vec3d pos, int colour, float size)
		{
			super(world, pos, size, null);
			float[] rgb = IIUtils.rgbIntToRGB(colour);
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

	public float[] getFluidColor(Fluid fluid)
	{
		if(CACHED_COLORS.containsKey(fluid))
			return CACHED_COLORS.get(fluid);

		InputStream is;
		BufferedImage image;
		float[] texture;
		try
		{
			final ResourceLocation f = new ResourceLocation(fluid.getStill().getResourceDomain(), "textures/"+fluid.getStill().getResourcePath()+".png");
			final IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(f);
			is = resource.getInputStream();
			image = ImageIO.read(is);
			texture = IIUtils.rgbIntToRGB((image.getRGB(0, 0)-0xff000000)<<2);
		} catch(IOException e)
		{
			IILogger.error("Could not load fluid texture file for ParticleGasCloud");
			texture = new float[]{1f, 1f, 1f};
		}
		CACHED_COLORS.put(fluid, texture);
		return texture;
	}
}