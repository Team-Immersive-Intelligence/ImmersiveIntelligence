package pl.pabilo8.immersiveintelligence.common.entity.ammo.types;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 16.02.2024
 */
public class EntityAmmoMine extends EntityAmmoBase<EntityAmmoMine>
{
	private static final Vec3d UP = new Vec3d(0, 1, 0);

	public EntityAmmoMine(World world)
	{
		super(world);
	}

	@Nonnull
	@Override
	protected Vec3d getDirection()
	{
		return UP;
	}

	@Override
	protected boolean shouldDecay()
	{
		return false;
	}
}
