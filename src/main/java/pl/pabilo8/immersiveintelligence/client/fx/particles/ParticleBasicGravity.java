package pl.pabilo8.immersiveintelligence.client.fx.particles;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.utils.IIParticleProperties;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 16.04.2024
 */
public class ParticleBasicGravity extends ParticleModel
{
	private double gravity = 0.05;

	public ParticleBasicGravity(World world, Vec3d pos)
	{
		super(world, pos);
	}

	@Override
	protected void updateMotionVector()
	{
		if(!onGround)
			motionY -= gravity;
	}

	@Override
	public void setProperties(EasyNBT properties)
	{
		super.setProperties(properties);
		properties.checkSetDouble(IIParticleProperties.GRAVITY, g -> gravity = g);
	}

	@Override
	public EasyNBT getProperties(EasyNBT eNBT)
	{
		return super.getProperties(eNBT)
				.withDouble(IIParticleProperties.GRAVITY, gravity);
	}

	/**
	 * @param gravity The gravity to set
	 */
	public void setGravity(double gravity)
	{
		this.gravity = gravity;
	}
}
