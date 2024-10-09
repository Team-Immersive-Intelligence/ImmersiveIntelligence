package pl.pabilo8.immersiveintelligence.common.util;

import com.google.common.math.IntMath;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

/**
 * @author GabrielV
 * @since 28-07-2024
 */
public class IIMath extends MathHelper
{
	public static final Vec3d ONE = new Vec3d(1, 1, 1);

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
	 * @author Pabilo8
	 * <p>
	 * Used to calculate 3D vector offset in a direction
	 * </p>
	 */
	public static Vec3d offsetPosDirection(float offset, double yaw, double pitch)
	{
		if(offset==0)
			return new Vec3d(0, 0, 0);

		double yy = (MathHelper.sin((float)pitch)*offset);
		double true_offset = (MathHelper.cos((float)pitch)*offset);

		double xx = (MathHelper.sin((float)yaw)*true_offset);
		double zz = (MathHelper.cos((float)yaw)*true_offset);

		return new Vec3d(xx, yy, zz);
	}

	/**
	 * Works™
	 */
	public static float clampedLerp3Par(float e1, float e2, float e3, float percent)
	{
		return (float)MathHelper.clampedLerp(MathHelper.clampedLerp(e1, e2, percent*2), e3, Math.max(percent-0.5f, 0)*2);
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
}
