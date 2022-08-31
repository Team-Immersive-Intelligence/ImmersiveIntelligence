package pl.pabilo8.immersiveintelligence.client.fx.particles;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 16-11-2019
 */
public class ParticleTMTModel extends Particle
{
	private final ModelRendererTurbo model;
	private final ResourceLocation texture;

	public ParticleTMTModel(World world, Vec3d pos, Vec3d motion, float size, ModelRendererTurbo model, ResourceLocation texture)
	{
		super(world, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
		this.particleMaxAge = 100+(int)(rand.nextGaussian()*32d);
		this.particleAngle = (float)(90f*rand.nextGaussian());

		this.particleScale = size;
		this.model = model;
		this.texture = texture;
		particleGravity = 0.75f;
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
		GlStateManager.pushMatrix();
		float f5 = (float)(this.prevPosX+(this.posX-this.prevPosX)*(double)partialTicks-interpPosX);
		float f6 = (float)(this.prevPosY+(this.posY-this.prevPosY)*(double)partialTicks-interpPosY);
		float f7 = (float)(this.prevPosZ+(this.posZ-this.prevPosZ)*(double)partialTicks-interpPosZ);
		float ox = (model.rotationPointX-model.offsetX);
		float oy = (model.rotationPointY-model.offsetY);
		float oz = (model.rotationPointZ-model.offsetZ);
		GlStateManager.translate(f5-(ox*0.0625f), f6-(oy*0.0625f)+0.125, f7-(oz*0.0625f));
		GlStateManager.rotate(particleAngle, 1, 0, 0);
		ClientUtils.mc().getTextureManager().bindTexture(texture);
		float age = 1f-(particleAge/(float)particleMaxAge);
		GlStateManager.color(age, age, age, 1f-Math.max(0, ((particleAge/(float)particleMaxAge)-0.75f)/0.25f));
		model.render(0.0625f);
		GlStateManager.popMatrix();
		if(!onGround)
			this.particleAngle += 1f;
	}
}