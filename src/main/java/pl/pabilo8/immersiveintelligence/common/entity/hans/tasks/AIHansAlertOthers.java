package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.util.math.AxisAlignedBB;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;

/**
 * @author Pabilo8
 * @since 07.05.2021
 */
public class AIHansAlertOthers extends EntityAITarget
{
	private final boolean entityCallsForHelp;
	/**
	 * Store the previous revengeTimer value
	 */
	private int revengeTimerOld;
	private final EntityHans hans;

	public AIHansAlertOthers(EntityHans hans, boolean entityCallsForHelpIn)
	{
		super(hans, true);
		this.hans = hans;
		this.entityCallsForHelp = entityCallsForHelpIn;
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		int i = this.taskOwner.getRevengeTimer();
		EntityLivingBase entitylivingbase = this.taskOwner.getRevengeTarget();
		return i!=this.revengeTimerOld&&entitylivingbase!=null&&hans.isValidTarget(entitylivingbase);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		this.taskOwner.setAttackTarget(this.taskOwner.getRevengeTarget());
		this.target = this.taskOwner.getAttackTarget();
		this.revengeTimerOld = this.taskOwner.getRevengeTimer();
		this.unseenMemoryTicks = 300;

		if(this.entityCallsForHelp)
		{
			this.alertOthers();
		}

		super.startExecuting();
	}

	protected void alertOthers()
	{
		double d0 = this.getTargetDistance();
		for(EntityHans anotherHans : this.hans.world.getEntitiesWithinAABB(EntityHans.class, getAABB().grow(d0, 10.0D, d0),
				input -> input!=null&&input.getTeam()==hans.getTeam()
		))
		{
			if(anotherHans.isValidTarget(this.hans.getRevengeTarget()))
			{
				this.setEntityAttackTarget(hans, this.taskOwner.getRevengeTarget());
			}
		}
	}

	protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn)
	{
		creatureIn.setAttackTarget(entityLivingBaseIn);
	}

	protected AxisAlignedBB getAABB()
	{
		return new AxisAlignedBB(this.hans.posX, this.hans.posY, this.hans.posZ, this.hans.posX+1.0D, this.hans.posY+1.0D, this.hans.posZ+1.0D);
	}
}