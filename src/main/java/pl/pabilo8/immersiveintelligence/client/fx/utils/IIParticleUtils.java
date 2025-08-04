package pl.pabilo8.immersiveintelligence.client.fx.utils;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.vecmath.Vector2f;
import java.util.function.Supplier;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 17.04.2024
 */
public class IIParticleUtils
{
	public static final VertexFormat PARTICLE_SOLID = new VertexFormat();
	public static Supplier<Float> randFloat = Utils.RAND::nextFloat;
	public static Supplier<Double> randDouble = Utils.RAND::nextGaussian;
	public static Supplier<Integer> randInt = Utils.RAND::nextInt;

	static
	{
		PARTICLE_SOLID.addElement(DefaultVertexFormats.POSITION_3F);
		PARTICLE_SOLID.addElement(DefaultVertexFormats.TEX_2F);
		PARTICLE_SOLID.addElement(DefaultVertexFormats.COLOR_4UB);
		PARTICLE_SOLID.addElement(DefaultVertexFormats.TEX_2S);
		PARTICLE_SOLID.addElement(DefaultVertexFormats.NORMAL_3B);
	}

	/**
	 * @return a random float value ranging from -1.0 to 1.0
	 */
	public static float getRandomPosNegFloat()
	{
		return (IIParticleUtils.randFloat.get()-0.5f)*2;
	}

	/**
	 * @return a random vector with values between 0.0 and 1.0
	 */
	public static Vec3d getPositiveRand()
	{
		return new Vec3d(IIParticleUtils.randFloat.get(), IIParticleUtils.randFloat.get(), IIParticleUtils.randFloat.get());
	}

	/**
	 * @return a random vector with X and Z values between 0.0 and 1.0
	 */
	public static Vec3d getPositiveXZRand()
	{
		return new Vec3d(IIParticleUtils.randFloat.get(), 0, IIParticleUtils.randFloat.get());
	}

	/**
	 * @return a random vector with X and Z values between -1.0 and 1.0
	 */
	public static Vec3d getRandXZ()
	{
		return new Vec3d(getRandomPosNegFloat(), 0, getRandomPosNegFloat());
	}

	/**
	 * @param vector source vector
	 * @param scale  scale factor
	 * @return a new vector with the X and Z values multiplied by the given scale
	 */
	public static Vec3d scaleXZ(Vec3d vector, double scale)
	{
		return new Vec3d(vector.x*scale, vector.y, vector.z*scale);
	}

	/**
	 * @param vector source vector
	 * @param y      Y value to set
	 * @return source vector with passed Y value
	 */
	public static Vec3d withY(Vec3d vector, double y)
	{
		return new Vec3d(vector.x, y, vector.z);
	}

	public static Vector2f toVector2f(Vec3d direction)
	{
		float yaw = (float)Math.atan2(direction.z, direction.x);
		float pitch = (float)Math.atan2(Math.sqrt(direction.x*direction.x+direction.z*direction.z), direction.y);
		return new Vector2f(yaw, pitch);
	}

	//--- Position ---//

	/**
	 * Specific method of vector normalization for an explosion direction, has a lower threshold for vertical axis
	 *
	 * @param actual vector to normalize
	 * @return normalized vector
	 **/
	public static Vec3d normalizeExplosionDirection(Vec3d actual)
	{
		if(Math.abs(actual.y) > 0.25)
			return new Vec3d(0, Math.signum(actual.y), 0);

		Vec3d normalized = withY(actual, 0).normalize();
		return new Vec3d(EnumFacing.getFacingFromVector((float)normalized.x, (float)normalized.y, (float)normalized.z).getDirectionVec());
	}

	/**
	 * Generates a stream of positions and motions based around an origin point.
	 */
	public enum PositionGenerator implements ISerializableEnum
	{
		SAME()
				{
					@Override
					public Vec3d generatePosition(Vec3d origin, int index, float size, int amount)
					{
						return new Vec3d(origin.x, origin.y, origin.z);
					}
				},
		RAND_XZ()
				{
					@Override
					public Vec3d generatePosition(Vec3d origin, int index, float size, int amount)
					{
						Vec3d pos = new Vec3d(randFloat.get(), 0, randFloat.get());
						return pos.scale(size).add(origin);
					}
				},
		CIRCLE_XZ()
				{
					@Override
					public Vec3d generatePosition(Vec3d origin, int index, float size, int amount)
					{
						double angle = Math.toRadians(360/(float)amount*index);
						Vec3d pos = new Vec3d((float)Math.cos(angle), 0, (float)Math.sin(angle));
						return pos.scale(size).add(origin);
					}
				},
		CONE_XZ(),
		CONE_XY(),
		CONE_ZY(),
		SPHERE()
				{
					@Override
					public Vec3d generatePosition(Vec3d origin, int index, float size, int amount)
					{
						double phi = Math.acos(1-2*randFloat.get());
						double theta = 2*Math.PI*randFloat.get();
						Vec3d pos = new Vec3d(
								Math.sin(phi)*Math.cos(theta),
								Math.sin(phi)*Math.sin(theta),
								Math.cos(phi)
						);
						return pos.scale(size).add(origin);
					}
				},
		SQUARE(),
		STAR(),
		ORB(),
		CUBE();


		public Vec3d generatePosition(Vec3d origin, int index, float size, int amount)
		{
			return Vec3d.ZERO;
		}

		public Vec3d generateMotion(Vec3d origin, int index, float size, int amount)
		{
			Vec3d vec = generatePosition(Vec3d.ZERO, index, size, amount);
			vec.normalize();
			return vec;
		}

		public Vector2f generateRotation(Vec3d origin, int index, float size, int amount)
		{
			return new Vector2f(0, 0);
		}
	}
}
