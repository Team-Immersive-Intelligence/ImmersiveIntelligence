package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;

import java.util.List;

/**
 * @author Pabilo8
 * @since 21.09.2021
 */
public abstract class AIHansBase extends EntityAIBase
{
	protected final EntityHans hans;

	protected AIHansBase(EntityHans hans)
	{
		this.hans = hans;
	}

	/**
	 * <p>Called by {@link EntityHans}, don't use it inside tasks, it can cause bugs.</p>
	 * <p>
	 * Allows to set the animation needed for current action (kneeling, lying, etc.)<br>
	 * See: {@link pl.pabilo8.immersiveintelligence.common.entity.hans.HansAnimations.HansLegAnimation}
	 * </p>
	 */
	public abstract void setRequiredAnimation();

	protected boolean canShootEntity(EntityLivingBase entity)
	{
		Vec3d start = hans.getPositionEyes(0);
		Vec3d end = entity.getPositionVector();

		AxisAlignedBB potentialCollateralArea = entity.getEntityBoundingBox().union(hans.getEntityBoundingBox());
		List<EntityLivingBase> potentialCollateral = hans.world.getEntitiesWithinAABB(EntityLivingBase.class, potentialCollateralArea);
		for(EntityLivingBase coll : potentialCollateral)
		{
			AxisAlignedBB entityBB = coll.getEntityBoundingBox().grow(.125f/2+.4);
			if(coll!=hans&&!hans.isValidTarget(coll)&&entityBB.calculateIntercept(start, end)!=null)
				return false;
		}
		return true;
	}

	public boolean shouldBeRemoved()
	{
		return false;
	}
}
