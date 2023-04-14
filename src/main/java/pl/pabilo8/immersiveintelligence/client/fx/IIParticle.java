package pl.pabilo8.immersiveintelligence.client.fx;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.MathHelper;
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

	protected float getProgress(float partialTicks)
	{
		return MathHelper.clamp((this.particleAge+partialTicks)/(float)this.particleMaxAge,
				0.0F, 1.0F);
	}

	//--- For Use with AMT ---//

	public int getMaxAge()
	{
		return particleMaxAge;
	}

	@Override
	public void setMaxAge(int value)
	{
		super.setMaxAge(value);
	}

	public int getAge()
	{
		return particleMaxAge;
	}

	public void setAge(int value)
	{
		particleAge = value;
	}

	public void setWorld(World world)
	{
		this.world = world;
	}

	@Nonnull
	public abstract DrawingStages getDrawStage();
}
