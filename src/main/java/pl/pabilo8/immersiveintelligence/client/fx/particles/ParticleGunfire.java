package pl.pabilo8.immersiveintelligence.client.fx.particles;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticle;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleRenderer.DrawingStages;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 16-11-2019
 */
public class ParticleGunfire extends IIParticle
{
	public static TextureAtlasSprite[] TEXTURES;
	final float rotationYaw, rotationPitch;

	public ParticleGunfire(World world, Vec3d pos, Vec3d motion, float size)
	{
		super(world, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);

		Vec3d normalized = motion.normalize();
		float motionXZ = MathHelper.sqrt(normalized.x*normalized.x+normalized.z*normalized.z);
		this.rotationYaw = (float)((Math.atan2(normalized.x, normalized.z)*180D)/3.1415927410125732D);
		this.rotationPitch = -(float)((Math.atan2(normalized.y, motionXZ)*180D)/3.1415927410125732D);

		this.particleScale = (size+Utils.RAND.nextFloat());
		this.particleMaxAge = (int)(2+(3*Utils.RAND.nextGaussian()));
	}

	public void onUpdate()
	{
		/*motionX *= 0.6f;
		motionY *= 0.6f;
		motionZ *= 0.6f;*/

		if(this.particleAge++ >= this.particleMaxAge)
			this.setExpired();
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_)
	{
		return 240<<16|240;
	}

	public int getFXLayer()
	{
		return 2;
	}

	/**
	 * Renders the particle
	 */
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		Tessellator tes = Tessellator.getInstance();
		VertexFormat format = buffer.getVertexFormat();

		tes.draw();

		GlStateManager.pushMatrix();
		float x = (float)(this.prevPosX+(this.posX-this.prevPosX)*(double)partialTicks-interpPosX);
		float y = (float)(this.prevPosY+(this.posY-this.prevPosY)*(double)partialTicks-interpPosY);
		float z = (float)(this.prevPosZ+(this.posZ-this.prevPosZ)*(double)partialTicks-interpPosZ);

		TextureAtlasSprite tex = TEXTURES[(int)(MathHelper.clamp(particleAge/(float)particleMaxAge, 0, 1)*7)];
		float u = tex.getMinU(), v = tex.getInterpolatedV(8), uu = tex.getInterpolatedU(8), vv = tex.getMaxV();

		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(rotationYaw+90, 0, 1f, 0);
		GlStateManager.rotate(rotationPitch, 0, 0, 1);

		float scale = particleScale*0.25f;
		GlStateManager.scale(scale, scale, scale);
		GlStateManager.translate(0, 0.25, 0);

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		IIClientUtils.drawFace(buffer, 0.385, -0.5, -0.5, 0.385, 0.5, 0.5,
				u, uu, v, vv);
		u = tex.getInterpolatedU(8);
		uu = tex.getMaxU();
		IIClientUtils.drawFace(buffer, 0.125, -0.5, -0.5, 0.125, 0.5, 0.5,
				u, uu, v, vv);

		Tessellator.getInstance().draw();


		//set texture to the "beam" part
		u = tex.getMinU();
		uu = tex.getMaxU();
		v = tex.getMinV();
		vv = tex.getInterpolatedV(8);

		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		for(int i = 0; i < 4; i++)
		{
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			IIClientUtils.drawFace(buffer, 0, -0.5, 0, 2, 0.5, 0,
					u, uu, v, vv);
			Tessellator.getInstance().draw();
			GlStateManager.rotate(45, 1, 0, 0);
		}

		GlStateManager.popMatrix();

		buffer.begin(GL11.GL_QUADS, format);
	}

	@Nonnull
	@Override
	public DrawingStages getDrawStage()
	{
		return DrawingStages.CUSTOM_ADDITIVE;
	}
}