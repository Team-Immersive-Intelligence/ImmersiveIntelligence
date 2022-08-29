package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.idle;

import net.minecraft.entity.Entity;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansAnimations.HansArmAnimation;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansAnimations.HansLegAnimation;

/**
 * @author Pabilo8
 * @since 02.02.2022
 */
public class AIHansSalute extends AIHansTimedLookAtEntity
{
	public AIHansSalute(EntityHans hans, Entity lookedAt)
	{
		super(hans, lookedAt, 40, 1f);
	}

	@Override
	public void setRequiredAnimation()
	{
		super.setRequiredAnimation();
		if(hans.legAnimation==HansLegAnimation.STANDING)
			hans.armAnimation = HansArmAnimation.SALUTE;
	}
}
