package pl.pabilo8.immersiveintelligence.client.fx.utils;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

	/**
	 * @param settingValue setting value specific to a particle effect
	 * @return the lower detail level selected by either the effect setting or the global Video Settings option
	 */
	public static ParticleDetail getParticleDetailLevel(ParticleDetail settingValue)
	{
		int globalSetting = MathHelper.clamp(
				ClientUtils.mc().gameSettings.particleSetting,
				0, ParticleDetail.values().length-1
		);
		return ParticleDetail.values()[Math.max(settingValue.ordinal(), globalSetting)];
	}

	/**
	 * @return particle detail level from the global Video Settings option
	 */
	public static ParticleDetail getParticleDetailLevel()
	{
		int globalSetting = MathHelper.clamp(
				ClientUtils.mc().gameSettings.particleSetting,
				0, ParticleDetail.values().length-1
		);
		return ParticleDetail.values()[globalSetting];
	}

	/**
	 * Calculates a reusable particle budget multiplier from effect detail and viewer distance.
	 * The returned value is 1.0 at full detail and close range, then falls to 0.65 and 0.35.
	 *
	 * @param detail       resolved detail level for the effect
	 * @param distance     distance between the viewer and the effect
	 * @param nearDistance distance up to which the full budget is retained
	 * @param farDistance  distance at which the lowest distance multiplier begins
	 */
	public static float getParticleBudgetScale(ParticleDetail detail, float distance,
	                                           float nearDistance, float farDistance)
	{
		if(!detail.isEnabled())
			return 0f;

		float safeNear = Math.max(0f, nearDistance);
		float safeFar = Math.max(safeNear, farDistance);
		float distanceScale = distance < safeNear?1f: (distance < safeFar?0.65f: 0.35f);

		float detailScale;
		switch(detail)
		{
			case REDUCED:
				detailScale = 0.65f;
				break;
			case MINIMAL:
				detailScale = 0.35f;
				break;
			case DISABLED:
				return 0f;
			default:
			case DETAILED:
				detailScale = 1f;
		}

		return distanceScale*detailScale;
	}

	/**
	 * Calculates a bounded adaptive particle budget. The magnitude is normalised against a reference
	 * value, raised to the requested growth exponent, and then scaled by the resolved detail budget.
	 *
	 * @param magnitude          measured size or extent of the effect
	 * @param referenceMagnitude magnitude corresponding to a normalised value of 1
	 * @param baseBudget         fixed part of the budget
	 * @param growthBudget       amount added by the normalised growth term
	 * @param growthExponent     1 for linear growth, 0.5 for square-root growth, etc.
	 * @param budgetScale        multiplier returned by {@link #getParticleBudgetScale}
	 * @param minimum            minimum returned budget
	 * @param maximum            maximum returned budget
	 */
	public static int calculateAdaptiveParticleBudget(float magnitude, float referenceMagnitude,
	                                                  float baseBudget, float growthBudget,
	                                                  float growthExponent, float budgetScale,
	                                                  int minimum, int maximum)
	{
		if(maximum <= 0||budgetScale <= 0f)
			return 0;

		int safeMinimum = MathHelper.clamp(minimum, 0, maximum);
		float reference = Math.max(0.0001f, referenceMagnitude);
		float normalisedMagnitude = Math.max(1f, magnitude/reference);
		float growth = (float)Math.pow(normalisedMagnitude, growthExponent);
		int budget = Math.round((baseBudget+growthBudget*growth)*budgetScale);
		return MathHelper.clamp(budget, safeMinimum, maximum);
	}

	/**
	 * Selects a fixed number of elements at regular intervals while preserving source order.
	 * This is useful for representative particle, sound, decal, or animation samples.
	 */
	public static <T> List<T> selectEvenlyDistributed(List<T> elements, int amount)
	{
		if(elements==null||elements.isEmpty()||amount <= 0)
			return Collections.emptyList();
		if(elements.size() <= amount)
			return new ArrayList<>(elements);

		List<T> selected = new ArrayList<>(amount);
		float stride = elements.size()/(float)amount;
		for(int i = 0; i < amount; i++)
			selected.add(elements.get(Math.min(
					elements.size()-1,
					MathHelper.floor((i+0.5f)*stride)
			)));
		return selected;
	}
}
