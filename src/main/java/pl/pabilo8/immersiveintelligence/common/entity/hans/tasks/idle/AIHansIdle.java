package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.idle;

import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansAnimations.HansLegAnimation;
import pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.AIHansBase;

import javax.annotation.Nullable;

/**
 * <p>Based on {@link net.minecraft.entity.ai.EntityAILookIdle}</p>
 *
 * @author Pabilo8
 * @since 22.09.2021
 */
public class AIHansIdle extends AIHansBase
{
	/**
	 * distance Hans will walk {@link #wandersBeforeIdling} times before idling
	 */
	private final int wanderDistance;
	/**
	 * How many walks hans should perform, generated after idling by {@link #reGenerateWanders()}
	 */
	private int wandersBeforeIdling;
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
	 * Position hans wanders to
	 */
	protected double x;
	protected double y;
	protected double z;
	/**
	 * A decrementing tick that stops the entity from being idle once it reaches 0.
	 */
	private int idleTime, pastIdleTime;
	/**
	 * Amount of walks the Hans had
	 */
	private int wanderCount = 0;
	private HansLegAnimation currentAnimation;
	private boolean isWandering = true;
	private boolean positionValid = false;

	public AIHansIdle(EntityHans hans)
	{
		super(hans);
		this.setMutexBits(3); //motion + head movement
		pastIdleTime = getIdleTime();
		wanderDistance = 3+hans.getRNG().nextInt(2);
		reGenerateWanders();
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
		return pastIdleTime==0&&hans.getAttackTarget()==null&&this.hans.getRNG().nextFloat() < 0.01F;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting()
	{
		if(hans.isRiding()||hans.getAttackTarget()!=null)
		{
			idleTime = 0;
			pastIdleTime = getIdleTime();
		}
		return this.idleTime >= 0&&hans.getAttackTarget()==null;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		isWandering = wanderCount < wandersBeforeIdling;

		this.idleTime = 200+this.hans.getRNG().nextInt(120);
		if(!isWandering)
		{
			wanderCount = 0;
			reGenerateWanders();
			double d0 = (Math.PI*2D)*this.hans.getRNG().nextDouble();
			this.lookX = Math.cos(d0);
			this.lookZ = Math.sin(d0);

			//prevents sneaking inside machines
			boolean inside = false;
			for(int x = -1; x < 1; x++)
				for(int z = -1; z < 1; z++)
				{
					if(!hans.world.isAirBlock(hans.getPosition().add(x, 0, z)))
					{
						inside = true;
						break;
					}
				}
			if(inside)
				currentAnimation = ANIMATIONS[hans.getRNG().nextInt(ANIMATIONS.length)];
		}
		else
		{
			wanderCount++;
			Vec3d vv = this.getWanderPosition();
			if(vv!=null)
			{
				this.x = vv.x;
				this.y = vv.y;
				this.z = vv.z;
				positionValid = true;
			}
			else
				positionValid = false;
		}


	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask()
	{
		if(isWandering)
		{
			if(positionValid)
				this.hans.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, 1f);
		}
		else
			this.hans.getLookHelper().setLookPosition(this.hans.posX+this.lookX, this.hans.posY+(double)this.hans.getEyeHeight(), this.hans.posZ+this.lookZ, (float)this.hans.getHorizontalFaceSpeed(), (float)this.hans.getVerticalFaceSpeed());

		--this.idleTime;
		if(pastIdleTime==0)
			pastIdleTime = getIdleTime();
	}

	public int getIdleTime()
	{
		return isWandering?150: 200+this.hans.getRNG().nextInt(isWandering?50: 100);
	}

	/**
	 * @return a position hans will wander to
	 */
	@Nullable
	protected Vec3d getWanderPosition()
	{
		if(this.hans.isInWater())
		{


			Vec3d vec3d = RandomPositionGenerator.getLandPos(this.hans, wanderDistance, 7);
			return vec3d==null?
					RandomPositionGenerator.getLandPos(this.hans, wanderDistance, 15):
					vec3d;
		}
		else
			return RandomPositionGenerator.getLandPos(this.hans, wanderDistance, 15);
	}

	private void reGenerateWanders()
	{
		wandersBeforeIdling = 2+hans.getRNG().nextInt(3);
	}
}
