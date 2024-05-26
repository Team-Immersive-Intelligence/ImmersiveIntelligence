package pl.pabilo8.immersiveintelligence.client.fx.particles;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 16.04.2024
 */
public class ParticleDebris extends ParticleBasicGravity
{
	public boolean prop = false;

	public ParticleDebris(World world, Vec3d pos)
	{
		super(world, pos);
	}

	@Override
	protected void updateMotionVector()
	{
		super.updateMotionVector();
		if(!onGround)
		{
			float progress = getProgress(0);
//
			//apply rotation
			Vec3d newRot = new Vec3d((8*progress)%2-1, (8*progress)%2-1, (8*progress)%2-1);
			setRotation(newRot.x, newRot.y, newRot.z);
		}
		else if(!prop)
			lifeTime = maxLifeTime;

	}
}
