package pl.pabilo8.immersiveintelligence.client.fx.nuke;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
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
public class ParticleAtomicBoomRing extends ParticleAtomBase
{
	public ParticleAtomicBoomRing(World world, Vec3d pos, Vec3d motion, float size)
	{
		super(world, pos, motion, size);
		this.particleMaxAge = (int)(20+(10*Utils.RAND.nextGaussian()))+1;
	}

	@Override
	protected float getActualScale(float size)
	{
		return ((float)(size*0.85+(size*0.15*Utils.RAND.nextGaussian()))*2f)*0.45f;
	}

	/**
	 * Renders the particle
	 */
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		float f = getProgress(partialTicks);

		setScale(1f);
		setIndex(5);
		setRBGColorF(0.8f+(0.2f*f), 0.3f+(0.7f*f), (0.4f*f));
		setAlphaF(0.85f);
		super.renderParticle(buffer, entity, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);

		setIndex(7);
		setScale(3f);
		setAlphaF(1f);
		super.renderParticle(buffer, entity, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
	}
}