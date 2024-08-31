package pl.pabilo8.immersiveintelligence.common.util.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 23.12.2022
 */
public class IIEntityUtils
{
	public static Vec3d getEntityCenter(Entity entity)
	{
		return entity.getPositionVector().addVector(-entity.width/2f, entity.height/2f, -entity.width/2f);
	}

	@Nonnull
	public static EnumFacing getFacingBetweenPos(@Nonnull BlockPos fromPos, @Nonnull BlockPos toPos)
	{
		Vec3d vv = new Vec3d(fromPos.subtract(toPos)).normalize();
		return EnumFacing.getFacingFromVector((float)vv.x, (float)vv.y, (float)vv.x);
	}

	/**
	 * No idea why make this client-side only...
	 */
	public static void setEntityVelocity(Entity entity, double motionX, double motionY, double motionZ)
	{
		entity.motionX = motionX;
		entity.motionY = motionY;
		entity.motionZ = motionZ;
		entity.velocityChanged = true;
	}

	public static Vec3d getEntityMotion(Entity entity)
	{
		return new Vec3d(entity.motionX, entity.motionY, entity.motionZ);
	}
}
