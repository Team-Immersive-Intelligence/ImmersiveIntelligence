package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import pl.pabilo8.immersiveintelligence.common.entity.mounted_weapon.EntityMachinegun;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.04.2021
 */
public class AIHansMachinegun extends EntityAIBase
{
	private final EntityLiving hans;
	private EntityMachinegun mg;

	@Nullable
	private Entity target = null;

	public AIHansMachinegun(EntityLiving hans)
	{
		this.hans = hans;
		this.setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		if(hans.getRidingEntity() instanceof EntityMachinegun)
			mg = ((EntityMachinegun)hans.getRidingEntity());
		else
		{
			hans.tasks.removeTask(this);
			return false;
		}

		if(mg==null||mg.isDead)
		{
			hans.tasks.removeTask(this);
			return false;
		}

		return true;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask()
	{
		target = hans.getAttackTarget();
		if(mg!=null)
		{
			if(mg.controls!=null)
				mg.controls.setKey("fire", false);

			if(target!=null)
			{
				hans.getLookHelper().setLookPositionWithEntity(target, hans.getHorizontalFaceSpeed(), hans.getVerticalFaceSpeed());
				if(mg.aim.isAimed()&&mg.controls!=null)
					mg.controls.setKey("fire", true);
			}
		}
	}
}
