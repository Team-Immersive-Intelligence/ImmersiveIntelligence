package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks;

import net.minecraft.entity.Entity;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.EntityParachute;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 25.09.2021
 */
public class AIHansCrewman extends AIHansBase
{
	private final Entity mount;

	public AIHansCrewman(@Nonnull EntityHans hans, @Nonnull Entity mount)
	{
		super(hans);
		this.mount = mount;
		this.setMutexBits(3);
	}

	@Override
	public void setRequiredAnimation()
	{

	}

	@Override
	public boolean shouldExecute()
	{
		if(hans.getRidingEntity() instanceof EntityParachute)
			return false;

		if(this.mount==null||this.mount.isDead)
			return false;
		if(hans.getPositionVector().distanceTo(mount.getLowestRidingEntity().getPositionVector()) < 1.5f)
		{
			hans.startRiding(mount);
			return false;
		}
		return true;
	}

	@Override
	public void updateTask()
	{
		super.updateTask();
		this.hans.getNavigator().tryMoveToEntityLiving(this.mount.getLowestRidingEntity(), 1.25f);

		if(hans.getPositionVector().distanceTo(mount.getLowestRidingEntity().getPositionVector()) < 1.5f)
			hans.startRiding(mount);
	}

	public boolean shouldContinueExecuting()
	{
		return this.shouldExecute()&&!this.hans.getNavigator().noPath();
	}

	@Override
	public boolean shouldBeRemoved()
	{
		if(hans.getRidingEntity() instanceof EntityParachute)
			return false;

		// TODO: 25.09.2021 true when other entity is the passenger and Hans can't be mounted
		return (this.mount==null||this.mount.isDead||mount.isPassenger(hans));
	}
}
