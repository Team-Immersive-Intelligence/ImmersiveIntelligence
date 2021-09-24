package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.idle;

import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansAnimations.HansLegAnimation;
import pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.AIHansBase;

/**
 * @author Pabilo8
 * @since 22.09.2021
 */
public class AIHansKazachok extends AIHansBase
{
	int timer;

	public AIHansKazachok(EntityHans hans, float samogonAmount)
	{
		super(hans);
		this.timer = (int)(600*samogonAmount);
		setMutexBits(7); //Replaces all tasks
	}

	@Override
	public void setRequiredAnimation()
	{
		hans.legAnimation = HansLegAnimation.KAZACHOK;
		timer--;
	}

	@Override
	public boolean shouldBeRemoved()
	{
		return !this.shouldExecute();
	}

	@Override
	public boolean shouldExecute()
	{
		return hans.getAttackTarget()==null&&timer > 0;
	}
}
