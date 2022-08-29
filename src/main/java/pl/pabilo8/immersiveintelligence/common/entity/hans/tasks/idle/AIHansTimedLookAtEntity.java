package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.idle;

import net.minecraft.entity.Entity;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.AIHansBase;

/**
 * @author Pabilo8
 * @since 28.12.2021
 */
public class AIHansTimedLookAtEntity extends AIHansBase
{
	final Entity lookedAt;
	int timer;
	final float rotatoSped;

	public AIHansTimedLookAtEntity(EntityHans hans, Entity lookedAt, int time, float rotatoSped)
	{
		super(hans);
		this.lookedAt = lookedAt;
		this.timer = time;
		this.rotatoSped = rotatoSped;
		setMutexBits(2); //Replaces head movement
	}

	@Override
	public void setRequiredAnimation()
	{
		hans.getLookHelper().setLookPositionWithEntity(lookedAt,hans.getHorizontalFaceSpeed()*rotatoSped,hans.getVerticalFaceSpeed()*rotatoSped);
	}

	@Override
	public boolean shouldBeRemoved()
	{
		return lookedAt==null||timer <= 0;
	}

	@Override
	public boolean shouldExecute()
	{
		timer--;
		return !shouldBeRemoved();
	}
}
