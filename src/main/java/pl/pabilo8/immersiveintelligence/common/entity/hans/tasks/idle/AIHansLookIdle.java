package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.idle;

import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansAnimations.HansLegAnimation;
import pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.AIHansBase;

/**
 * <p>Based on {@link net.minecraft.entity.ai.EntityAILookIdle}</p>
 *
 * @author Pabilo8
 * @since 22.09.2021
 */
public class AIHansLookIdle extends AIHansBase
{
	private static final HansLegAnimation[] ANIMATIONS = new HansLegAnimation[]{HansLegAnimation.KNEELING, HansLegAnimation.SQUATTING};
	/**
	 * X offset to look at
	 */
	private double lookX;
	/**
	 * Z offset to look at
	 */
	private double lookZ;
	/**
	 * A decrementing tick that stops the entity from being idle once it reaches 0.
	 */
	private int idleTime, pastIdleTime;
	private HansLegAnimation currentAnimation;

	public AIHansLookIdle(EntityHans hans)
	{
		super(hans);
		this.setMutexBits(3); //motion + head movement
		pastIdleTime = getIdleTime();
	}

	@Override
	public void setRequiredAnimation()
	{
		if(shouldContinueExecuting()&&currentAnimation!=null)
			hans.legAnimation = currentAnimation;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		if(hans.getAttackTarget()==null)
			pastIdleTime = Math.max(--pastIdleTime, 0);
		else if(pastIdleTime!=0)
			pastIdleTime = getIdleTime();
		return pastIdleTime==0&&hans.getAttackTarget()==null&&hans.moveForward==0&&this.hans.getRNG().nextFloat() < 0.005F;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting()
	{
		if(hans.getAttackTarget()!=null)
		{
			idleTime = 0;
			pastIdleTime = getIdleTime();
		}
		return this.idleTime >= 0&&hans.getAttackTarget()==null&&hans.moveForward==0;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		double d0 = (Math.PI*2D)*this.hans.getRNG().nextDouble();
		this.lookX = Math.cos(d0);
		this.lookZ = Math.sin(d0);
		this.idleTime = 200+this.hans.getRNG().nextInt(120);

		currentAnimation = ANIMATIONS[hans.getRNG().nextInt(ANIMATIONS.length)];
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask()
	{
		--this.idleTime;
		if(pastIdleTime==0)
			pastIdleTime = getIdleTime();
		this.hans.getLookHelper().setLookPosition(this.hans.posX+this.lookX, this.hans.posY+(double)this.hans.getEyeHeight(), this.hans.posZ+this.lookZ, (float)this.hans.getHorizontalFaceSpeed(), (float)this.hans.getVerticalFaceSpeed());
	}

	public int getIdleTime()
	{
		return 300+this.hans.getRNG().nextInt(200);
	}
}
