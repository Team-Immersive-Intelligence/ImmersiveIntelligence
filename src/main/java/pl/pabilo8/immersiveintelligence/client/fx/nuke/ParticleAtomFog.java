package pl.pabilo8.immersiveintelligence.client.fx.nuke;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticle;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleRenderer;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleRenderer.DrawingStages;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 17.07.2020
 */
public class ParticleAtomFog extends ParticleAtomBase
{
	public ParticleAtomFog(World world, Vec3d pos, Vec3d motion, float size)
	{
		super(world, pos, motion, size);
		this.particleMaxAge = (int)(20+(10*Utils.RAND.nextGaussian()))+1;
	}

	@Override
	protected float getActualScale(float size)
	{
		return (float)(size*0.85+(size*0.15*Utils.RAND.nextGaussian()))*2f;
	}

	/**
	 * Renders the particle
	 */
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		float f = getProgress(partialTicks);
		f = f > 0.9f?(1f-((f-0.9f)/0.1f)): 1f;

		GlStateManager.translate(0, -0.25, 0);

		setAlphaF(0.5f);
		setScale(1.25f*f);
		setRBGColorF(0.15f, 0.064705886f, 0.04411765f);
		setIndex(6);
		super.renderParticle(buffer, entity, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);

		GlStateManager.translate(0, 0.25, 0);

		setScale(f);
		setAlphaF(0.25f);
		super.renderParticle(buffer, entity, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
	}
}