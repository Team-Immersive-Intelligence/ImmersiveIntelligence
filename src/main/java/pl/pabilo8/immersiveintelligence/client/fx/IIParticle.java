package pl.pabilo8.immersiveintelligence.client.fx;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleRenderer.DrawingStages;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 23.12.2020
 */
public abstract class IIParticle extends Particle
{
	protected IIParticle(World worldIn, double posXIn, double posYIn, double posZIn)
	{
		super(worldIn, posXIn, posYIn, posZIn);
	}

	public IIParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
	}

	@Nonnull
	public abstract DrawingStages getDrawStage();
}
