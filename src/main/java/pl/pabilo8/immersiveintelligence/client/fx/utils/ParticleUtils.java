package pl.pabilo8.immersiveintelligence.client.fx.utils;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.client.fx.particles.IIParticle;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

/**
 * @author Pabilo8
 * @since 17.04.2024
 */
public class ParticleUtils
{
	public static final VertexFormat PARTICLE_SOLID = new VertexFormat();

	static
	{
		PARTICLE_SOLID.addElement(DefaultVertexFormats.POSITION_3F);
		PARTICLE_SOLID.addElement(DefaultVertexFormats.TEX_2F);
		PARTICLE_SOLID.addElement(DefaultVertexFormats.COLOR_4UB);
		PARTICLE_SOLID.addElement(DefaultVertexFormats.TEX_2S);
		PARTICLE_SOLID.addElement(DefaultVertexFormats.NORMAL_3B);
	}

	public static Supplier<Float> randFloat = Utils.RAND::nextFloat;
	public static Supplier<Double> randDouble = Utils.RAND::nextDouble;
	public static Supplier<Integer> randInt = Utils.RAND::nextInt;

	/**
	 * @return a random float value ranging from -1.0 to 1.0
	 */
	public static float getRandomPosNegFloat()
	{
		return (ParticleUtils.randFloat.get()-0.5f)*2;
	}

	/**
	 * @return a random vector with values between 0.0 and 1.0
	 */
	public static Vec3d getPositiveRand()
	{
		return new Vec3d(ParticleUtils.randFloat.get(), ParticleUtils.randFloat.get(), ParticleUtils.randFloat.get());
	}

	/**
	 * @return a random vector with X and Z values between 0.0 and 1.0
	 */
	public static Vec3d getPositiveXZRand()
	{
		return new Vec3d(ParticleUtils.randFloat.get(), 0, ParticleUtils.randFloat.get());
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

	/**
	 * @param color1 first color
	 * @param color2 second color
	 * @return mixer of two colors in random ratio
	 */
	public static <T extends IIParticle> BiConsumer<T, EasyNBT> getRandomColorMixer(IIColor color1, IIColor color2)
	{
		return (particle, easyNBT) -> easyNBT.withColor(IIParticleProperties.COLOR,
				color1.mixedWith(color2, ParticleUtils.randFloat.get()));
	}

	//--- Position ---//

	/**
	 * Supplies positions and directions based on an existing particle's position using a generator
	 *
	 * @param generator generator to use
	 * @param particle  particle to base positions on
	 * @param amount    amount of positions to generate
	 * @param size      size of the positions
	 * @param action    action to perform on each generated position
	 */
	public static void position(PositionGenerator generator, IIParticle particle, int amount, double size, BiConsumer<Vec3d, Vec3d> action)
	{
		position(generator, particle.getPosition(), amount, size, action);
	}

	public static void position(PositionGenerator generator, Vec3d pos, int amount, double size, BiConsumer<Vec3d, Vec3d> action)
	{
		generator.generate(pos, size, amount).forEach(tuple -> action.accept(pos.add(tuple.getFirst()), tuple.getSecond()));
	}

	public static <T extends IIParticle> void getFoliageColor(T particle, EasyNBT easyNBT)
	{
		BlockPos pos = new BlockPos(particle.getPosition());
		easyNBT.withColor(IIParticleProperties.COLOR,
				IIColor.fromPackedRGB(particle.getWorld().getBiome(pos).getFoliageColorAtPos(pos)));
	}

	/**
	 * Specific method of vector normalization for an explosion direction, has a lower threshold for vertical axis
	 *
	 * @param actual vector to normalize
	 * @return normalized vector
	 */
	public static Vec3d normalizeExplosionDirection(Vec3d actual)
	{
		if(Math.abs(actual.y) > 0.25)
			return new Vec3d(0, Math.signum(actual.y), 0);

		Vec3d normalized = withY(actual, 0).normalize();
		return new Vec3d(EnumFacing.getFacingFromVector(
						(float)normalized.x,
						(float)normalized.y,
						(float)normalized.z)
				.getDirectionVec());
	}

	/**
	 * Generates a stream of positions and motions based around an origin point.
	 */
	public enum PositionGenerator implements ISerializableEnum
	{
		RAND_XZ()
				{
					@Override
					public Stream<Tuple<Vec3d, Vec3d>> generate(Vec3d origin, double size, int amount)
					{
						Builder<Tuple<Vec3d, Vec3d>> builder = Stream.builder();
						for(int i = 0; i < amount; i++)
						{
							Vec3d randXZ = getRandXZ();
							builder.accept(new Tuple<>(origin.add(randXZ), randXZ.scale(size)));
						}
						return builder.build();
					}
				},
		CIRCLE_XZ()
				{
					@Override
					public Stream<Tuple<Vec3d, Vec3d>> generate(Vec3d origin, double size, int amount)
					{
						return Stream.empty();
					}
				},
		CONE_XZ()
				{
					@Override
					public Stream<Tuple<Vec3d, Vec3d>> generate(Vec3d origin, double size, int amount)
					{
						return Stream.empty();
					}
				},
		CONE_XY()
				{
					@Override
					public Stream<Tuple<Vec3d, Vec3d>> generate(Vec3d origin, double size, int amount)
					{
						return Stream.empty();
					}
				},
		CONE_ZY()
				{
					@Override
					public Stream<Tuple<Vec3d, Vec3d>> generate(Vec3d origin, double size, int amount)
					{
						return Stream.empty();
					}
				},
		SPHERE()
				{
					@Override
					public Stream<Tuple<Vec3d, Vec3d>> generate(Vec3d origin, double size, int amount)
					{
						return Stream.empty();
					}
				},
		SQUARE()
				{
					@Override
					public Stream<Tuple<Vec3d, Vec3d>> generate(Vec3d origin, double size, int amount)
					{
						return Stream.empty();
					}
				},
		STAR()
				{
					@Override
					public Stream<Tuple<Vec3d, Vec3d>> generate(Vec3d origin, double size, int amount)
					{
						return Stream.empty();
					}
				},
		ORB()
				{
					@Override
					public Stream<Tuple<Vec3d, Vec3d>> generate(Vec3d origin, double size, int amount)
					{
						return Stream.empty();
					}
				},
		CUBE()
				{
					@Override
					public Stream<Tuple<Vec3d, Vec3d>> generate(Vec3d origin, double size, int amount)
					{
						return Stream.empty();
					}
				};

		public abstract Stream<Tuple<Vec3d, Vec3d>> generate(Vec3d origin, double size, int amount);

		public Stream<Tuple<Vec3d, Vec3d>> generate(Vec3d origin, Vec3d rotation, double size, int minAmount, int maxAmount)
		{
			return generate(origin, size, Utils.RAND.nextInt(maxAmount-minAmount)+minAmount);
		}
	}
}
