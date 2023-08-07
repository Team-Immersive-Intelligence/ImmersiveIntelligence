package pl.pabilo8.immersiveintelligence.common.entity.vehicle.drone;

import net.minecraft.entity.ai.EntityAIBase;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityDrone;

/**
 * @author Pabilo8
 * @since 19.12.2022
 */
public abstract class AIDroneBase extends EntityAIBase
{
	protected final EntityDrone drone;

	public AIDroneBase(EntityDrone drone)
	{
		this.drone = drone;
	}
}
