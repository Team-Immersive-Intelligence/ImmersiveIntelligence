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
	int fuelRemaining;

	public EntityAmmoMissile(World world)
	{
		super(world);
	}
}
