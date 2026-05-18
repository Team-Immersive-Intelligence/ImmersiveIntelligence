package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.common.entity.mounted_weapon.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

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
			{
				mg.controls.setKey("fire", false);
				if(mg.controls.isDirty())
					mg.updateEntityForEvent(SyncEvents.ENTITY_VEHICLE_CONTROLS);
			}

			if(target!=null)
			{
				hans.getLookHelper().setLookPositionWithEntity(target, hans.getHorizontalFaceSpeed(), hans.getVerticalFaceSpeed());
				if(isAimedAt())
					if(mg.controls!=null)
					{
						mg.controls.setKey("fire", true);
						if(mg.controls.isDirty())
							mg.updateEntityForEvent(SyncEvents.ENTITY_VEHICLE_CONTROLS);
					}
			}

		}
	}

	public boolean isAimedAt()
	{
		return MathHelper.wrapDegrees(hans.rotationPitch)-mg.getGunPitch(0f) < 5
				&&MathHelper.wrapDegrees(hans.rotationYawHead)-MathHelper.wrapDegrees(mg.rotationYaw) < 5;
	}
}
