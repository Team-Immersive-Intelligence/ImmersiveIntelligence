package pl.pabilo8.immersiveintelligence.common.util;

import com.google.common.math.IntMath;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author GabrielV (gabriel@iiteam.net)
 * @since 28.07.2024
 */
public class IIMath extends MathHelper
{
	public static final Vec3d ONE = new Vec3d(1, 1, 1);
	public static final double GOLDEN_ANGLE = Math.PI*(3-Math.sqrt(5));

	/**
	 * @param value to be squared
	 * @return value squared
	 */
	public static int pow2(int value)
	{
		return value*value;
	}

	/**
	 * <a href="https://stackoverflow.com/a/52284357/9876980">https://stackoverflow.com/a/52284357/9876980</a>
	 */
	public static double root(double num, double root)
	{
		double d = Math.pow(num, 1.0/root);
		long rounded = Math.round(d);
		return Math.abs(rounded-d) < 0.00000000000001?rounded: d;
	}

	//Copied from GUIContainer
	public static boolean isPointInRectangle(double x, double y, double xx, double yy, double px, double py)
	{
		return px >= x&&px < xx&&py >= y&&py < yy;
	}

	/**
	 * From StackOverflow (yet again!)
	 * <a href="https://stackoverflow.com/a/9755252/9876980">https://stackoverflow.com/a/9755252/9876980</a>
	 *
	 * @param x   point 1's x
	 * @param y   point 1's y
	 * @param xx  point 2's x
	 * @param yy  point 2's y
	 * @param xxx point 3's x
	 * @param yyy point 3's y
	 * @return whether px and py is inside the triangle
	 */
	public static boolean isPointInTriangle(int x, int y, int xx, int yy, int xxx, int yyy, int px, int py)
	{
		int as_x = px-x;
		int as_y = py-y;

		boolean s_ab = (xx-x)*as_y-(yy-y)*as_x > 0;

		if((xxx-x)*as_y-(yyy-y)*as_x > 0==s_ab) return false;
		return (xxx-xx)*(py-yy)-(yyy-yy)*(px-xx) > 0==s_ab;
	}

	/**
	 * @param offset (length) of the vector
	 * @param yaw    of the vector (in radians)
	 * @param pitch  of the vector (in radians)
	 * @return direction transformed position
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * <p>
	 * Used to calculate 3D vector offset in a direction
	 * </p>
	 */
	public static Vec3d offsetPosDirection(double offset, double yaw, double pitch)
	{
		if(offset==0)
			return Vec3d.ZERO;

		double yy = (MathHelper.sin((float)pitch)*offset);
		double true_offset = (MathHelper.cos((float)pitch)*offset);

		double xx = (MathHelper.sin((float)yaw)*true_offset);
		double zz = (MathHelper.cos((float)yaw)*true_offset);

		return new Vec3d(xx, yy, zz);
	}

	public static Vec3d offsetPosDirectionXZ(double xOffset, double zOffset, float rotationYaw, float rotationPitch)
	{
		//If no offset, return 0 vector
		if(xOffset==0&&zOffset==0)
			return Vec3d.ZERO;

		float yaw = (float)Math.toRadians(-rotationYaw);
		float yawZ = (float)(yaw-1.5707963267948966);
		float pitch = (float)Math.toRadians(rotationPitch);

		return offsetPosDirection(xOffset, yaw, pitch).add(offsetPosDirection(zOffset, yawZ, 0));
	}

	public static Vec3d offsetPosDirectionXYZ(Vec3d offset, float rotationYaw, float rotationPitch, float rotationRoll)
	{
		//If no offset, return 0 vector
		if(offset.x==0&&offset.y==0&&offset.z==0)
			return Vec3d.ZERO;

		float yaw = (float)Math.toRadians(-rotationYaw);
		float yawZ = (float)(yaw-1.5707963267948966);
		float pitch = (float)Math.toRadians(rotationPitch);
		float pitchY = (float)(pitch+1.5707963267948966);

		return offsetPosDirection(offset.x, yaw, pitch)
				.add(offsetPosDirection(offset.y, yaw, pitchY))
				.add(offsetPosDirection(offset.z, yawZ, 0));
	}


	/**
	 * Calculates yaw and pitch from a vector.
	 *
	 * @param x x component of the vector
	 * @param y y component of the vector
	 * @param z z component of the vector
	 * @return yaw and pitch of the vector in degrees (-180 to 180)
	 */
	public static float[] getRotationFromVector(double x, double y, double z)
	{
		return getRotationFromVector(new Vec3d(x, y, z));
	}

	/**
	 * Calculates yaw and pitch from a vector.
	 *
	 * @param vector the vector to get the rotation from
	 * @return yaw and pitch of the vector in degrees (-180 to 180)
	 */
	public static float[] getRotationFromVector(Vec3d vector)
	{
		Vec3d normalized = vector.normalize();
		float horizontal = MathHelper.sqrt(normalized.x*normalized.x+normalized.z*normalized.z);
		return new float[]{
				(float)((MathHelper.atan2(normalized.x, normalized.z)*180D)/Math.PI),
				(float)((MathHelper.atan2(normalized.y, horizontal)*180D)/Math.PI)
		};
	}

	/**
	 * Works™
	 */
	public static float clampedLerp3Par(float e1, float e2, float e3, float percent)
	{
		return (float)MathHelper.clampedLerp(MathHelper.clampedLerp(e1, e2, percent*2), e3, Math.max(percent-0.5f, 0)*2);
	}

	public static float progressValue(float initialValue, float goalValue, float maxProgress, float partialTicks)
	{
		if(partialTicks==0)
			partialTicks = 1;

		if(initialValue==goalValue)
			return initialValue;
		if(initialValue > goalValue)
			return Math.max(initialValue-(maxProgress*partialTicks), goalValue);
		return Math.min(initialValue+(maxProgress*partialTicks), goalValue);
	}

	public static boolean isAABBContained(@Nonnull AxisAlignedBB compared, @Nonnull AxisAlignedBB comparedTo)
	{
		Vec3d c0, c1, c2, c3, c4, c5, c6, c7;
		c0 = new Vec3d(compared.minX, compared.minY, compared.minZ);
		c1 = new Vec3d(compared.maxX, compared.minY, compared.minZ);
		c2 = new Vec3d(compared.minX, compared.maxY, compared.minZ);
		c3 = new Vec3d(compared.maxX, compared.maxY, compared.minZ);
		c4 = new Vec3d(compared.minX, compared.minY, compared.maxZ);
		c5 = new Vec3d(compared.maxX, compared.minY, compared.maxZ);
		c6 = new Vec3d(compared.minX, compared.maxY, compared.maxZ);
		c7 = new Vec3d(compared.maxX, compared.maxY, compared.maxZ);

		AxisAlignedBB comp2 = comparedTo.grow(0.1f);

		return comp2.contains(c0)&&comp2.contains(c1)&&comp2.contains(c2)&&comp2.contains(c3)
				&&comp2.contains(c4)&&comp2.contains(c5)&&comp2.contains(c6)&&comp2.contains(c7);
	}

	public static Vec3d getAABBCenter(@Nonnull AxisAlignedBB aabb)
	{
		return new Vec3d(aabb.minX+(aabb.maxX-aabb.minX)*0.5D, aabb.minY+(aabb.maxY-aabb.minY)*0.5D, aabb.minZ+(aabb.maxZ-aabb.minZ)*0.5D);
	}

	public static Vec3d getAABBSize(AxisAlignedBB aabb)
	{
		return new Vec3d(
				Math.abs(aabb.maxX-aabb.minX),
				Math.abs(aabb.maxY-aabb.minY),
				Math.abs(aabb.maxZ-aabb.minZ)
		);
	}

	/**
	 * Creates a Vec3 using the pitch and yaw of the entities rotation.
	 */
	public static Vec3d getVectorForRotation(float pitch, float yaw)
	{
		float f = MathHelper.cos(-yaw*0.017453292F-(float)Math.PI);
		float f1 = MathHelper.sin(-yaw*0.017453292F-(float)Math.PI);
		float f2 = -MathHelper.cos(-pitch*0.017453292F);
		float f3 = MathHelper.sin(-pitch*0.017453292F);
		return new Vec3d(f1*f2, f3, f*f2);
	}

	public static boolean inRange(int value, int maxValue, double min, double max)
	{
		double vv = value/(double)maxValue;
		return vv >= min&&vv <= max;
	}

	/**
	 * @param number   to be rounded
	 * @param decimals after the separator
	 * @return a (efficiently) rounded number
	 */
	public static float roundFloat(float number, int decimals)
	{
		int pow = 1;
		for(int i = 0; i < decimals; i++)
			pow *= 10;
		float tmp = number*pow;

		return (float)Math.round(tmp)/pow;
	}

	/**
	 * @param numbers array of numbers, must contain at least two
	 * @return Greatest Common Divisor of multiple numbers
	 */
	public static int gcd(int... numbers)
	{
		int gcd = numbers[0];
		for(int i = 1; i < numbers.length; i++)
			gcd = IntMath.gcd(gcd, numbers[i]);

		return gcd;
	}

	public static boolean isNumberFinite(double... values)
	{
		for(double value : values)
			if(!Double.isFinite(value))
				return false;
		return true;
	}
}
