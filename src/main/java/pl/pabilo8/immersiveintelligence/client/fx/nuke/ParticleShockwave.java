package pl.pabilo8.immersiveintelligence.client.fx.nuke;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author Pabilo8
 * @since 17.07.2020
 */
public class ParticleShockwave extends ParticleAtomBase
{
	public ParticleShockwave(World world, Vec3d pos, Vec3d motion, float size)
	{
		super(world, pos, motion, size);
		this.particleMaxAge = (int)(20+(10*Utils.RAND.nextGaussian()))+1;
	}

	/**
	 * Renders the particle
	 */
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		float f = MathHelper.clamp(getProgress(partialTicks), 0.35F, 1.0F);
		float f2 = 1f-(Math.abs(f-0.5f)/0.5f*0.25f);

		setIndex(5);
		setScale(f2*1.25f);
		setRBGColorF(1f, 1f, 1f);
		setAlphaF((1-f)*0.25f);

		GlStateManager.translate(0, -0.25, 0);
		super.renderParticle(buffer, entity, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
		GlStateManager.translate(0, 0.25, 0);

		setScale(f2);
		setRBGColorF(0.85f, 0.85f, 0.85f);
		setAlphaF((1-f)*0.15f);
		setIndex(4);
		super.renderParticle(buffer, entity, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);

	}

	@Override
	protected float getActualScale(float size)
	{
		return (float)(size*0.85+(size*0.15*Utils.RAND.nextGaussian()))*2f;
	}
}