package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;

/**
 * Unrightfully stolen from SpecialAI, then adapted to Hanses
 *
 * @author FatherToast
 * @since 06.02.2022
 */
public class AIHansAvoidArrows extends AIHansBase
{
	//The horizontal velocity of the entity being avoided.
	private Vec3d arrowUVec;
	//Ticks until the entity gives up.
	private int giveUpDelay;
	//Used to prevent mobs from leaping all over the place from multiple arrows.
	private int dodgeDelay;
	//The chance a Hans will dodge
	private final float dodgeChance;

	protected AIHansAvoidArrows(EntityHans hans)
	{
		super(hans);
		dodgeChance = 0.5f;
		setMutexBits(4);
	}

	public static void doDodgeCheckForArrow(Entity arrow)
	{
		float width = arrow.width+0.3F;
		double vH = Math.sqrt(arrow.motionX*arrow.motionX+arrow.motionZ*arrow.motionZ);
		double uX = arrow.motionX/vH;
		double uZ = arrow.motionZ/vH;

		Entity entity;
		int dY;
		double dX, dZ, dH;
		double cos, sin;
		for(int i = 0; i < arrow.world.loadedEntityList.size(); i++)
		{
			entity = arrow.world.loadedEntityList.get(i);
			if(entity instanceof EntityHans)
			{

				dY = (int)entity.posY-(int)arrow.posY;
				if(dY < 0)
					dY = -dY;
				if(dY <= 16)
				{
					// Within vertical range
					dX = entity.posX-arrow.posX;
					dZ = entity.posZ-arrow.posZ;
					dH = Math.sqrt(dX*dX+dZ*dZ);
					if(dH < 24.0)
					{
						// Within horizontal range
						cos = (uX*dX+uZ*dZ)/dH;
						sin = Math.sqrt(1-cos*cos);
						if(width > dH*sin)
						{
							// Within ray width
							tryDodgeArrow((EntityHans)entity, uX, uZ);
						}
					}
				}
			}
		}
	}

	private static void tryDodgeArrow( EntityHans entity, double uX, double uZ )
	{
		for(EntityAITasks.EntityAITaskEntry entry : entity.tasks.taskEntries.toArray( new EntityAITasks.EntityAITaskEntry[ 0 ] ) ) {
			if( entry.action instanceof AIHansAvoidArrows) {
				((AIHansAvoidArrows)entry.action).setDodgeTarget( new Vec3d( uX, 0.0, uZ ) );
			}
		}
	}

	// Tells this AI to dodge an entity.
	private void setDodgeTarget(Vec3d motionU)
	{
		if(motionU==null)
		{
			arrowUVec = null;
			giveUpDelay = 0;
		}
		else if(dodgeDelay <= 0&&hans.getRNG().nextFloat() < dodgeChance)
		{
			arrowUVec = motionU;
			giveUpDelay = 10;
		}
	}

	// Returns whether the AI should begin execution.
	@Override
	public boolean shouldExecute()
	{
		return dodgeDelay-- <= 0&&arrowUVec!=null&&giveUpDelay-- > 0&&hans.onGround&&!hans.isRiding();// && theEntity.getRNG().nextInt(5) == 0;
	}

	// Execute a one shot task or start executing a continuous task.
	@Override
	public void startExecuting()
	{
		if(arrowUVec!=null)
		{
			Vec3d selfUVec = new Vec3d(0.0, 1.0, 0.0);
			Vec3d dodgeUVec = selfUVec.crossProduct(arrowUVec);

			double scale = 0.8;
			if(hans.getRNG().nextBoolean())
				scale = -scale;

			hans.motionX = dodgeUVec.x*scale;
			hans.motionZ = dodgeUVec.z*scale;
			hans.motionY = 0.4;

			setDodgeTarget(null);
			dodgeDelay = 40;
		}
	}

	// Returns whether an in-progress EntityAIBase should continue executing.
	@Override
	public boolean shouldContinueExecuting()
	{
		return false;
	}

	@Override
	public void setRequiredAnimation()
	{

	}
}
