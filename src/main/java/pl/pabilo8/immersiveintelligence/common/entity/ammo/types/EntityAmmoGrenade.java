package pl.pabilo8.immersiveintelligence.common.entity.ammo.types;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.IIPenetrationRegistry.IPenetrationHandler;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 02.02.2024
 */
public class EntityAmmoGrenade extends EntityAmmoProjectile
{
	public EntityAmmoGrenade(World world)
	{
		super(world);
	}

	//TODO: 15.02.2024 grenades should break only glass and thin blocks
	@Override
	protected boolean handleBlockDamage(RayTraceResult hit)
	{
		return super.handleBlockDamage(hit);
	}

	//TODO: 15.02.2024 grenade bouncing
	@Override
	protected void onHitRicochet(RayTraceResult hit, IPenetrationHandler handler)
	{
		super.onHitRicochet(hit, handler);
	}

	//TODO: 15.02.2024 grenades should not penetrate entities
	@Override
	protected void onHitPenetrate(RayTraceResult hit, IPenetrationHandler handler)
	{
		super.onHitRicochet(hit, handler);
	}
}
