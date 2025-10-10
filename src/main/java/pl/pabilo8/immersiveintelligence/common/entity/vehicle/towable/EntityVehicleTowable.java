package pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.ITowable;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleWheel;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 01.10.2025
 */
public abstract class EntityVehicleTowable<T extends EntityVehicleTowable<T>> extends EntityVehicleBase<T> implements ITowable
{
	@SyncNBT
	public boolean towingOperation = false;
	@SyncNBT
	public int setupTime = 0;

	/**
	 * Constructs a new towable vehicle entity.
	 *
	 * @param world the world in which the entity will be spawned
	 */
	public EntityVehicleTowable(World world)
	{
		super(world);
	}

	//--- ITowable ---//

	@Override
	public Entity getTowingEntity()
	{
		return getRidingEntity();
	}

	@Override
	public boolean startTowing(Entity tower)
	{
		if(getTowingEntity()==null&&getRecursivePassengers().stream().allMatch(entity -> entity instanceof EntityVehicleSeat))
		{
			towingOperation = true;
			setupTime = 0;
			startRiding(tower);
			return true;
		}
		return false;
	}

	@Override
	public boolean stopTowing()
	{
		if(getTowingEntity()!=null)
		{
			towingOperation = true;
			setupTime = 0;
			dismountRidingEntity();
			return true;
		}
		return false;
	}

	@Override
	public boolean canMoveTowed()
	{
		return !towingOperation;
	}

	@Override
	public void moveTowableWheels(float speed)
	{
		// Apply towing force to all wheels
		Vec3d forward = new Vec3d(
				-Math.sin(Math.toRadians(this.rotationYaw)),
				0,
				Math.cos(Math.toRadians(this.rotationYaw))
		);

		Vec3d towingForce = forward.scale(speed*0.1);
		this.velocity = this.velocity.add(towingForce);

		// Also update wheel visuals
		for(EntityVehicleWheel<?> wheel : this.wheels)
			wheel.addWheelTraverse(speed);
	}
}
