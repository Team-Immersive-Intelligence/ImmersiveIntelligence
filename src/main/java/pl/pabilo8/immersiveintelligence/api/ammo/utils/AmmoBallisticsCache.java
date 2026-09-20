package pl.pabilo8.immersiveintelligence.api.ammo.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoType;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoBallistics.FlightState;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * On-demand cache of complete projectile trajectories and their derived statistics.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 04.06.2024
 */
public final class AmmoBallisticsCache
{
	private static final double ANGLE_PRECISION = 0.25D;
	private static final double MIN_ELEVATION = -89.75D;
	private static final double MAX_ELEVATION = 89.75D;
	private static final double QUERY_PRECISION = 64D;
	private static final double MAX_SOLUTION_ERROR = 0.25D;
	private static final int MAX_CACHED_MODELS = 24;
	private static final int MAX_CACHED_SOLUTIONS = 2048;
	private static final Map<BallisticCacheKey, CachedBallisticStats> CACHE =
			new LinkedHashMap<>(16, 0.75F, true)
			{
				@Override
				protected boolean removeEldestEntry(Map.Entry<BallisticCacheKey, CachedBallisticStats> eldest)
				{
					return size() > MAX_CACHED_MODELS;
				}
			};

	private AmmoBallisticsCache()
	{

	}

	public static CachedBallisticStats get(IAmmoType<?, ?> type, ItemStack stack)
	{
		return get(type, stack, 1D);
	}

	public static CachedBallisticStats get(IAmmoType<?, ?> type, ItemStack stack, double velocityModifier)
	{
		if(type==null||stack==null||!Double.isFinite(velocityModifier)||velocityModifier <= 0)
			return CachedBallisticStats.INVALID;
		return get(type.getBallistics(stack, velocityModifier));
	}

	/**
	 * Retrieves cached stats for any ammo or custom projectile flight model.
	 */
	public static CachedBallisticStats get(AmmoBallistics ballistics)
	{
		if(ballistics==null)
			return CachedBallisticStats.INVALID;

		BallisticCacheKey key = new BallisticCacheKey(ballistics.getCacheIdentity(),
				ballistics.getVelocity(), ballistics.getMaxFlightTime());
		synchronized(CACHE)
		{
			CachedBallisticStats cached = CACHE.get(key);
			if(cached==null)
			{
				cached = new CachedBallisticStats(ballistics);
				CACHE.put(key, cached);
			}
			return cached;
		}
	}

	public static void clear()
	{
		synchronized(CACHE)
		{
			CACHE.clear();
		}
	}

	@Value
	private static class BallisticCacheKey
	{
		Object identity;
		double velocity;
		int maxFlightTime;
	}

	/**
	 * Immutable, pre-calculated statistics for one flight model. Coordinate-specific solutions
	 * are calculated from the stored trajectories once, then retained in a bounded local cache.
	 */
	@Value
	@EqualsAndHashCode(onlyExplicitlyIncluded = true)
	public static class CachedBallisticStats
	{
		private static final CachedBallisticStats INVALID = new CachedBallisticStats();

		@EqualsAndHashCode.Include
		AmmoBallistics ballistics;
		double velocity;
		double maxDirectRange;
		double maxArtilleryRange;
		double maxHeightReached;
		@Getter(AccessLevel.NONE)
		Trajectory[] trajectories;
		@Getter(AccessLevel.NONE)
		Trajectory verticalUp;
		@Getter(AccessLevel.NONE)
		Trajectory verticalDown;
		@Getter(AccessLevel.NONE)
		Map<SolutionKey, BallisticSolution> solutionCache;

		private CachedBallisticStats()
		{
			this.ballistics = null;
			this.velocity = 0;
			this.maxDirectRange = 0;
			this.maxArtilleryRange = 0;
			this.maxHeightReached = 0;
			this.trajectories = new Trajectory[0];
			this.verticalUp = Trajectory.EMPTY;
			this.verticalDown = Trajectory.EMPTY;
			this.solutionCache = Collections.emptyMap();
		}

		private CachedBallisticStats(AmmoBallistics ballistics)
		{
			this.ballistics = ballistics;
			this.velocity = ballistics.getVelocity();
			int count = (int)Math.round((MAX_ELEVATION-MIN_ELEVATION)/ANGLE_PRECISION)+1;
			this.trajectories = new Trajectory[count];

			double artilleryRange = 0;
			for(int i = 0; i < count; i++)
			{
				double elevation = MIN_ELEVATION+i*ANGLE_PRECISION;
				Trajectory trajectory = simulate(ballistics, elevation);
				trajectories[i] = trajectory;
				artilleryRange = Math.max(artilleryRange, trajectory.getGroundRange());
			}

			this.verticalUp = simulate(ballistics, 90D);
			this.verticalDown = simulate(ballistics, -90D);
			this.maxHeightReached = verticalUp.getMaximumHeight();
			this.maxDirectRange = getTrajectory(0D).getDistanceAtVerticalDeviation(1D);
			this.maxArtilleryRange = artilleryRange;
			this.solutionCache = Collections.synchronizedMap(
					new LinkedHashMap<>(64, 0.75F, true)
					{
						@Override
						protected boolean removeEldestEntry(Map.Entry<SolutionKey, BallisticSolution> eldest)
						{
							return size() > MAX_CACHED_SOLUTIONS;
						}
					}
			);
		}

		public BallisticSolution getDirectSolution(double distance, double height)
		{
			return getSolution(distance, height, BallisticFireMode.DIRECT);
		}

		public BallisticSolution getArtillerySolution(double distance, double height)
		{
			return getSolution(distance, height, BallisticFireMode.ARTILLERY);
		}

		public BallisticSolution getSolution(double distance, double height, BallisticFireMode mode)
		{
			if(ballistics==null||mode==null||!Double.isFinite(distance)||!Double.isFinite(height)||distance < 0)
				return BallisticSolution.INVALID;

			SolutionKey key = new SolutionKey(quantize(distance), quantize(height), mode);
			synchronized(solutionCache)
			{
				BallisticSolution solution = solutionCache.get(key);
				if(solution==null)
				{
					solution = calculateSolution(key.getDistance()/QUERY_PRECISION,
							key.getHeight()/QUERY_PRECISION, mode);
					solutionCache.put(key, solution);
				}
				return solution;
			}
		}

		public float getDirectFireAngle(double distance, double height)
		{
			return (float)getDirectSolution(distance, height).getElevation();
		}

		public float getArtilleryAngle(double distance, double height)
		{
			return (float)getArtillerySolution(distance, height).getElevation();
		}

		public int getDirectImpactTime(double distance, double height)
		{
			return getDirectSolution(distance, height).getImpactTicks();
		}

		public int getArtilleryImpactTime(double distance, double height)
		{
			return getArtillerySolution(distance, height).getImpactTicks();
		}

		public int getImpactTime(double distance, double height, BallisticFireMode mode)
		{
			return getSolution(distance, height, mode).getImpactTicks();
		}

		public int getImpactTimeAtDistance(double elevation, double distance)
		{
			TrajectorySample sample = getTrajectory(elevation).sampleAtDistance(distance);
			return sample.isValid()?(int)Math.ceil(sample.getTime()): -1;
		}

		public int getImpactTimeAtHeight(double elevation, double height, boolean preferLater)
		{
			double time = getTrajectory(elevation).getTimeAtHeight(height, preferLater);
			return Double.isFinite(time)?(int)Math.ceil(time): -1;
		}

		public double getDistanceAtAngle(double elevation)
		{
			return getTrajectory(elevation).getGroundRange();
		}

		private BallisticSolution calculateSolution(double distance, double height, BallisticFireMode mode)
		{
			if(distance==0)
			{
				Trajectory vertical = height >= 0?verticalUp: verticalDown;
				double time = vertical.getTimeAtHeight(height, mode==BallisticFireMode.ARTILLERY);
				return Double.isFinite(time)?new BallisticSolution(height >= 0?90D: -90D, time, 0D): BallisticSolution.INVALID;
			}

			BallisticSolution selected = BallisticSolution.INVALID;
			BallisticSolution nearest = BallisticSolution.INVALID;
			double previousError = Double.NaN;
			TrajectorySample previousSample = TrajectorySample.INVALID;
			double previousElevation = 0;

			for(Trajectory trajectory : trajectories)
			{
				TrajectorySample sample = trajectory.sampleAtDistance(distance);
				if(!sample.isValid())
				{
					previousError = Double.NaN;
					previousSample = TrajectorySample.INVALID;
					continue;
				}

				double error = sample.getHeight()-height;
				BallisticSolution candidate = new BallisticSolution(
						trajectory.getElevation(), sample.getTime(), Math.abs(error)
				);
				if(!nearest.isValid()||candidate.getVerticalError() < nearest.getVerticalError())
					nearest = candidate;

				if(error==0)
					selected = select(selected, candidate, mode);
				else if(Double.isFinite(previousError)&&Math.signum(previousError)!=Math.signum(error))
				{
					double fraction = previousError/(previousError-error);
					candidate = new BallisticSolution(
							lerp(previousElevation, trajectory.getElevation(), fraction),
							lerp(previousSample.getTime(), sample.getTime(), fraction), 0D
					);
					selected = select(selected, candidate, mode);
				}

				previousError = error;
				previousSample = sample;
				previousElevation = trajectory.getElevation();
			}

			if(selected.isValid())
				return selected;
			return nearest.isValid()&&nearest.getVerticalError() <= MAX_SOLUTION_ERROR?
					nearest: BallisticSolution.INVALID;
		}

		private BallisticSolution select(BallisticSolution current, BallisticSolution candidate,
										 BallisticFireMode mode)
		{
			if(!current.isValid())
				return candidate;
			if(mode==BallisticFireMode.DIRECT)
				return candidate.getImpactTime() < current.getImpactTime()?candidate: current;
			return candidate.getImpactTime() > current.getImpactTime()?candidate: current;
		}

		private Trajectory getTrajectory(double elevation)
		{
			if(elevation >= 90D)
				return verticalUp;
			if(elevation <= -90D)
				return verticalDown;
			int index = (int)Math.round((elevation-MIN_ELEVATION)/ANGLE_PRECISION);
			return trajectories[Math.max(0, Math.min(trajectories.length-1, index))];
		}
	}

	public enum BallisticFireMode
	{
		DIRECT,
		ARTILLERY
	}

	/**
	 * A firing solution whose elevation is measured in degrees above the horizontal plane.
	 */
	@Value
	public static class BallisticSolution
	{
		private static final BallisticSolution INVALID = new BallisticSolution(Double.NaN, -1D, Double.POSITIVE_INFINITY);

		double elevation;
		double impactTime;
		double verticalError;

		public boolean isValid()
		{
			return Double.isFinite(elevation)&&Double.isFinite(impactTime)&&impactTime >= 0;
		}

		public int getImpactTicks()
		{
			return isValid()?(int)Math.ceil(impactTime): -1;
		}
	}

	@Value
	private static class SolutionKey
	{
		long distance;
		long height;
		BallisticFireMode mode;
	}

	@Value
	private static class TrajectorySample
	{
		private static final TrajectorySample INVALID = new TrajectorySample(Double.NaN, Double.NaN);

		double height;
		double time;

		boolean isValid()
		{
			return Double.isFinite(height)&&Double.isFinite(time);
		}
	}

	@Value
	private static class Trajectory
	{
		private static final Trajectory EMPTY = new Trajectory(0, new float[]{0}, new float[]{0});

		double elevation;
		@Getter(AccessLevel.NONE)
		float[] distance;
		@Getter(AccessLevel.NONE)
		float[] height;

		TrajectorySample sampleAtDistance(double targetDistance)
		{
			if(targetDistance < 0||distance.length==0||targetDistance > distance[distance.length-1])
				return TrajectorySample.INVALID;
			if(targetDistance==0)
				return new TrajectorySample(0, 0);

			int low = 0;
			int high = distance.length-1;
			while(low+1 < high)
			{
				int middle = (low+high) >>> 1;
				if(distance[middle] < targetDistance)
					low = middle;
				else
					high = middle;
			}

			double width = distance[high]-distance[low];
			if(width <= 0)
				return TrajectorySample.INVALID;
			double fraction = (targetDistance-distance[low])/width;
			return new TrajectorySample(lerp(height[low], height[high], fraction), lerp(low, high, fraction));
		}

		double getGroundRange()
		{
			int initialSign = 0;
			for(int i = 1; i < height.length; i++)
			{
				int sign = Double.compare(height[i], 0D);
				if(initialSign==0&&sign!=0)
				{
					initialSign = sign;
					continue;
				}
				if(initialSign!=0&&sign!=0&&sign!=initialSign)
				{
					double fraction = height[i-1]/(double)(height[i-1]-height[i]);
					return lerp(distance[i-1], distance[i], fraction);
				}
			}
			return initialSign==0&&distance.length > 0?distance[distance.length-1]: 0;
		}

		double getMaximumHeight()
		{
			double maximum = 0;
			for(float value : height)
				maximum = Math.max(maximum, value);
			return maximum;
		}

		double getDistanceAtVerticalDeviation(double deviation)
		{
			for(int i = 1; i < height.length; i++)
				if(Math.abs(height[i]) >= deviation)
				{
					double previous = Math.abs(height[i-1]);
					double fraction = (deviation-previous)/(Math.abs(height[i])-previous);
					return lerp(distance[i-1], distance[i], fraction);
				}
			return distance.length==0?0: distance[distance.length-1];
		}

		double getTimeAtHeight(double targetHeight, boolean preferLater)
		{
			double selected = Double.NaN;
			for(int i = 1; i < height.length; i++)
			{
				double previousError = height[i-1]-targetHeight;
				double error = height[i]-targetHeight;
				if(previousError==0)
				{
					if(!preferLater)
						return i-1;
					selected = i-1;
				}
				else if(error==0||Math.signum(previousError)!=Math.signum(error))
				{
					double fraction = previousError/(previousError-error);
					double time = lerp(i-1, i, fraction);
					if(!preferLater)
						return time;
					selected = time;
				}
			}
			return selected;
		}
	}

	private static Trajectory simulate(AmmoBallistics ballistics, double elevation)
	{
		int maxTicks = ballistics.getMaxFlightTime();
		float[] distance = new float[maxTicks+1];
		float[] height = new float[maxTicks+1];
		FlightState state = ballistics.createState(elevation);
		int length = 1;
		for(int tick = 1; tick <= maxTicks; tick++)
		{
			ballistics.update(state);
			if(!isFinite(state))
				break;
			distance[length] = (float)state.getPositionX();
			height[length] = (float)state.getPositionY();
			length++;
		}

		if(length < distance.length)
		{
			float[] trimmedDistance = new float[length];
			float[] trimmedHeight = new float[length];
			System.arraycopy(distance, 0, trimmedDistance, 0, length);
			System.arraycopy(height, 0, trimmedHeight, 0, length);
			distance = trimmedDistance;
			height = trimmedHeight;
		}
		return new Trajectory(elevation, distance, height);
	}

	private static boolean isFinite(FlightState state)
	{
		return Double.isFinite(state.getPositionX())&&Double.isFinite(state.getPositionY())
				&&Double.isFinite(state.getMotionX())&&Double.isFinite(state.getMotionY());
	}

	private static long quantize(double value)
	{
		return Math.round(value*QUERY_PRECISION);
	}

	private static double lerp(double first, double second, double fraction)
	{
		return first+(second-first)*fraction;
	}
}
