package pl.pabilo8.immersiveintelligence.common.entity.ammo.types;

import net.minecraft.world.World;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 02.02.2024
 */
public class EntityAmmoMissile extends EntityAmmoProjectile
{
	//TODO: 15.02.2024 implement fuel system
	int fuelRemaining = 1000;

	public EntityAmmoMissile(World world)
	{
		super(world);
	}

	@Override
	protected void updatePhysics()
	{
		//Gravity suppressed by missile jet
		if(fuelRemaining > 0)
		{
			velocity -= DRAG*velocity;
			fuelRemaining--;
		}
		else
			super.updatePhysics();
	}


}
