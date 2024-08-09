package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;

import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 15.02.2024
 */
public abstract class EmplacementTask
{
	public abstract float[] getPositionVector(TileEntityEmplacement emplacement);

	public void onShot()
	{

	}

	/**
	 * @return whether the task should continue execution, should be always true if it is a permanent detection task like {@link EmplacementTaskEntities}
	 */
	public abstract boolean shouldContinue();

	/**
	 * @return target name/id for loading from nbt and init
	 */
	public abstract String getName();

	public NBTTagCompound saveToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("name", getName());
		return nbt;
	}

	/**
	 * Reduces lag.
	 * Used to update visible targets list on target seeking tasks, like {@link EmplacementTaskEntities}
	 * Leave empty if it is a single target and you're not doing any detection
	 *
	 * @param emplacement to which the task belongs
	 */
	public abstract void updateTargets(TileEntityEmplacement emplacement);

	protected static float[] getPosForEntityTask(TileEntityEmplacement emplacement, Entity entity)
	{
		if(entity!=null&&entity.isEntityAlive())
		{
			if(entity instanceof IEntityMultiPart)
			{
				Entity[] parts = entity.getParts();
				if(parts!=null&&parts.length > 0)
				{
					//target the biggest hitbox
					Entity t = Arrays.stream(parts).max((o1, o2) -> (int)((o1.width*o1.height)-(o2.width*o2.height))).orElse(parts[0]);
					return emplacement.currentWeapon.getAnglePrediction(emplacement.getWeaponCenter(),
							t.getPositionVector().addVector(-t.width/2f, t.height/2f, -t.width/2f),
							new Vec3d(entity.motionX, entity.motionY, entity.motionZ));
				}
			}
			return emplacement.currentWeapon.getAnglePrediction(emplacement.getWeaponCenter(),
					entity.getPositionVector().addVector(-entity.width/2f, entity.height/2f, -entity.width/2f),
					new Vec3d(entity.motionX, entity.motionY, entity.motionZ));
		}
		return new float[]{emplacement.currentWeapon.yaw, emplacement.currentWeapon.pitch};
	}
}
