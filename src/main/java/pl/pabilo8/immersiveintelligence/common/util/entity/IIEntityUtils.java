package pl.pabilo8.immersiveintelligence.common.util.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
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
		float x = fromPos.getX()-toPos.getX();
		float y = fromPos.getY()-toPos.getY();
		float z = fromPos.getZ()-toPos.getZ();
		float xA = Math.abs(x);
		float yA = Math.abs(y);
		float zA = Math.abs(z);

		if(xA > yA)
			return xA > zA?EnumFacing.getFacingFromAxis(getSignDirection(x), Axis.X): EnumFacing.getFacingFromAxis(getSignDirection(z), Axis.Z);
		return yA > zA?EnumFacing.getFacingFromAxis(getSignDirection(y), Axis.Y): EnumFacing.getFacingFromAxis(getSignDirection(z), Axis.Z);
	}

	private static AxisDirection getSignDirection(float number)
	{
		return number < 0?AxisDirection.NEGATIVE: AxisDirection.POSITIVE;
	}

	/**
	 * Sets an entity's motion vector, because {@link Entity#setVelocity(double, double, double)} is client side only.
	 *
	 * @param entity  entity to set motion to
	 * @param motionX motion on X axis
	 * @param motionY motion on Y axis
	 * @param motionZ motion on Z axis
	 */
	public static void setEntityMotion(Entity entity, double motionX, double motionY, double motionZ)
	{
		entity.motionX = motionX;
		entity.motionY = motionY;
		entity.motionZ = motionZ;
		entity.velocityChanged = true;
	}

	/**
	 * @param entity entity to get motion from
	 * @return entity's motion (velocity) vector
	 */
	public static Vec3d getEntityMotion(Entity entity)
	{
		return new Vec3d(entity.motionX, entity.motionY, entity.motionZ);
	}
}
