package pl.pabilo8.immersiveintelligence.client.fx;

import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleRenderer.DrawingStages;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 23.12.2020
 */
public abstract class IIParticle extends Particle
{
	public IIParticle(World world, Vec3d pos, Vec3d motion)
	{
		this(world, pos.x, pos.y, pos.z, 0, 0, 0);
		setMotion(motion);
	}

	public IIParticle(World world, Vec3d pos)
	{
		this(world, pos, Vec3d.ZERO);
		setMotion(Vec3d.ZERO);
	}

	protected IIParticle(World world, double x, double y, double z, double motionX, double motionY, double motionZ)
	{
		super(world, x, y, z, motionX, motionY, motionZ);
	}

	//--- Protected Methods ---//

	protected void setMotion(Vec3d motion)
	{
		this.motionX = motion.x;
		this.motionY = motion.y;
		this.motionZ = motion.z;
	}

	protected Vec3d getMotion()
	{
		return new Vec3d(this.motionX, this.motionY, this.motionZ);
	}

	@Nonnull
	public abstract DrawingStages getDrawStage();
}
