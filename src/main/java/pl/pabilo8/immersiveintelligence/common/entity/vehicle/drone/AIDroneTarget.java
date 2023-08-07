package pl.pabilo8.immersiveintelligence.common.entity.vehicle.drone;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget.Sorter;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.math.AxisAlignedBB;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityDrone;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 19.12.2022
 */
public class AIDroneTarget<T extends Entity> extends AIDroneBase
{
	private final Class<T> targetClass;
	protected final Predicate<? super T> targetEntitySelector;
	protected final Sorter sorter;

	protected boolean seesThroughWalls = false;
	private int targetSearchStatus;
	private int targetUnseenTicks;

	protected T targetEntity = null;

	public AIDroneTarget(EntityDrone drone, Class<T> targetClass, Predicate<? super T> targetEntitySelector)
	{
		super(drone);
		this.targetEntitySelector = targetEntitySelector;
		this.targetClass = targetClass;
		this.sorter = new Sorter(drone);
	}

	protected AxisAlignedBB getTargetableArea(double targetDistance)
	{
		return this.drone.getEntityBoundingBox().grow(targetDistance, 4.0D, targetDistance);
	}

	protected double getTargetDistance()
	{
		IAttributeInstance iattributeinstance = this.drone.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
		return iattributeinstance==null?16.0D: iattributeinstance.getAttributeValue();
	}

	@Override
	public boolean shouldExecute()
	{
		List<T> list = this.drone.world.<T>getEntitiesWithinAABB(this.targetClass,
				this.getTargetableArea(this.getTargetDistance()), targetEntitySelector::test);

		if(list.isEmpty())
			return false;

		list.sort(this.sorter);
		this.targetEntity = list.get(0);
		return true;
	}
}
