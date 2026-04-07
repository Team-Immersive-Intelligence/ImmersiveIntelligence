package pl.pabilo8.immersiveintelligence.common.util.amt;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil.Shaders;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * A builder class for compound animations baked from multiple animations, that can be i.e. loaded from files.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 01.04.2026
 */
public class IIAnimationBuilder
{
	private static final float TIME_EPSILON = 1e-5f;

	private final ResourceLocation res;
	private final List<AnimationFragment> animationFragments = new ArrayList<>();
	private float totalTime = 0;

	public IIAnimationBuilder(ResourceLocation res)
	{
		this.res = res;
	}

	public IIAnimationBuilder addAnimation(float timestamp, float duration, IIAnimation animation)
	{
		return addAnimation(timestamp, duration, animation, 0);
	}

	public IIAnimationBuilder addAnimation(float timestamp, float duration, IIAnimation animation, int layer)
	{
		if(animation==null||duration <= 0)
			return this;

		AnimationFragment frag = new AnimationFragment(animation, timestamp, duration, layer);
		animationFragments.add(frag);
		this.totalTime = Math.max(this.totalTime, timestamp+duration);
		frag.ownerTotalTime = this.totalTime;
		return this;
	}

	public IIAnimation build()
	{
		if(animationFragments.isEmpty()||totalTime <= 0)
			return new IIAnimation(res, new IIAnimationGroup[0]);

		//Ensure all fragments know the final total time
		for(AnimationFragment fragment : animationFragments)
			fragment.ownerTotalTime = totalTime;

		//Sort by timestamp, then layer for deterministic conflict resolution
		animationFragments.sort(Comparator
				.comparingDouble(AnimationFragment::getTimestamp)
				.thenComparingInt(AnimationFragment::getLayer));

		//Collect all group names
		Set<String> groupNames = new LinkedHashSet<>();
		for(AnimationFragment fragment : animationFragments)
			for(IIAnimationGroup group : fragment.animation.groups)
				groupNames.add(group.groupName);

		List<IIAnimationGroup> outGroups = new ArrayList<>();
		for(String groupName : groupNames)
		{
			List<FragmentGroup> contributors = new ArrayList<>();
			for(AnimationFragment fragment : animationFragments)
			{
				IIAnimationGroup g = findGroup(fragment.animation, groupName);
				if(g!=null)
					contributors.add(new FragmentGroup(fragment, g));
			}
			if(contributors.isEmpty())
				continue;

			IIVectorLine position = ensureLine(buildVectorLine(contributors, group -> group.position));
			IIVectorLine scale = ensureLine(buildVectorLine(contributors, group -> group.scale));
			IIVectorLine rotation = ensureLine(buildVectorLine(contributors, group -> group.rotation));
			IIBooleanLine visibility = ensureLine(buildBooleanLine(contributors));
			IIShaderLine shader = ensureLine(buildShaderLine(contributors));
			IIFloatLine property = ensureLine(buildFloatLine(contributors));

			outGroups.add(new IIAnimationGroup(groupName, position, scale, rotation, visibility, shader, property));
		}

		return new IIAnimation(res, outGroups.toArray(new IIAnimationGroup[0]));
	}

	@Nullable
	private static IIAnimationGroup findGroup(IIAnimation animation, String name)
	{
		for(IIAnimationGroup group : animation.groups)
			if(group.groupName.equals(name))
				return group;
		return null;
	}

	private static class FragmentGroup
	{
		final AnimationFragment fragment;
		final IIAnimationGroup group;

		FragmentGroup(AnimationFragment fragment, IIAnimationGroup group)
		{
			this.fragment = fragment;
			this.group = group;
		}
	}

	//--- Utils ---//

	/**
	 * Collects and normalises all global time keys from a set of contributors for a given line type.
	 *
	 * @param contributors the fragments and their groups
	 * @param lineGetter   extracts the desired line from an animation group
	 * @return sorted list of unique global times in [0,1] (empty if none)
	 */
	private List<Float> collectGlobalTimes(List<FragmentGroup> contributors,
										   Function<IIAnimationGroup, IIAnimationLine<?>> lineGetter)
	{
		List<Float> globalTimes = new ArrayList<>();
		for(FragmentGroup fg : contributors)
		{
			IIAnimationLine<?> line = lineGetter.apply(fg.group);
			if(line!=null&&line.timeframes.length > 0)
				addLineKeyTimes(globalTimes, fg.fragment, line);
		}
		return normalizeAndSortTimes(globalTimes);
	}

	/**
	 * Adds the start (0), end (1) and all key frames of a line to the global time list.
	 */
	private void addLineKeyTimes(List<Float> outGlobalTimes, AnimationFragment fragment, IIAnimationLine<?> line)
	{
		addGlobalTime(outGlobalTimes, fragment, 0f);
		for(float tLocal : line.timeframes)
			addGlobalTime(outGlobalTimes, fragment, tLocal);
		addGlobalTime(outGlobalTimes, fragment, 1f);
	}

	private void addGlobalTime(List<Float> outGlobalTimes, AnimationFragment fragment, float tLocal)
	{
		float tGlobal = (fragment.timestamp+tLocal*fragment.duration)/totalTime;
		tGlobal = Math.max(0f, Math.min(1f, tGlobal));
		outGlobalTimes.add(tGlobal);
	}

	private float toLocalTime(float tGlobal, AnimationFragment fragment)
	{
		float absolute = tGlobal*fragment.ownerTotalTime;
		return (absolute-fragment.timestamp)/fragment.duration;
	}

	private List<Float> normalizeAndSortTimes(List<Float> times)
	{
		if(times.isEmpty())
			return Collections.emptyList();
		Collections.sort(times);
		List<Float> deduped = new ArrayList<>(times.size());
		Float last = null;
		for(Float t : times)
		{
			if(last==null||Math.abs(t-last) > TIME_EPSILON)
			{
				deduped.add(t);
				last = t;
			}
			else
			{
				//replace last with current (keeps later-sorted value)
				deduped.set(deduped.size()-1, t);
				last = t;
			}
		}
		return deduped;
	}

	//--- Line Builders ---//

	@Nullable
	private IIVectorLine buildVectorLine(List<FragmentGroup> contributors, Function<IIAnimationGroup, IIVectorLine> getter)
	{
		List<Float> globalTimes = collectGlobalTimes(contributors, getter::apply);
		if(globalTimes.isEmpty())
			return null;

		float[] times = new float[globalTimes.size()];
		Vec3d[] values = new Vec3d[globalTimes.size()];
		for(int i = 0; i < globalTimes.size(); i++)
		{
			float tGlobal = globalTimes.get(i);
			times[i] = tGlobal;
			Map<Integer, LayeredVectorAccum> byLayer = new HashMap<>();
			for(FragmentGroup fg : contributors)
			{
				IIVectorLine line = getter.apply(fg.group);
				if(line==null)
					continue;
				float tLocal = toLocalTime(tGlobal, fg.fragment);
				if(tLocal < 0f||tLocal > 1f)
					continue;

				LayeredVectorAccum accum = byLayer.computeIfAbsent(fg.fragment.layer, key -> new LayeredVectorAccum());
				accum.sum = accum.sum.add(line.getForTime(tLocal));
				accum.count++;
			}

			Vec3d total = Vec3d.ZERO;
			for(LayeredVectorAccum accum : byLayer.values())
				if(accum.count > 0)
					total = total.add(accum.sum.scale(1f/accum.count));
			values[i] = total;
		}
		return new IIVectorLine(times, values);
	}

	@Nullable
	private IIFloatLine buildFloatLine(List<FragmentGroup> contributors)
	{
		List<Float> globalTimes = collectGlobalTimes(contributors, g -> g.property);
		if(globalTimes.isEmpty())
			return null;

		float[] times = new float[globalTimes.size()];
		Float[] values = new Float[globalTimes.size()];
		for(int i = 0; i < globalTimes.size(); i++)
		{
			float tGlobal = globalTimes.get(i);
			times[i] = tGlobal;
			Map<Integer, LayeredFloatAccum> byLayer = new HashMap<>();
			for(FragmentGroup fg : contributors)
			{
				IIFloatLine line = fg.group.property;
				if(line==null)
					continue;
				float tLocal = toLocalTime(tGlobal, fg.fragment);
				if(tLocal < 0f||tLocal > 1f)
					continue;
				Float v = line.getForTime(tLocal);
				if(v==null)
					continue;

				LayeredFloatAccum accum = byLayer.computeIfAbsent(fg.fragment.layer, key -> new LayeredFloatAccum());
				accum.sum += v;
				accum.count++;
			}

			float total = 0f;
			for(LayeredFloatAccum accum : byLayer.values())
				if(accum.count > 0)
					total += accum.sum/accum.count;
			values[i] = total;
		}
		return new IIFloatLine(times, values);
	}

	@Nullable
	private IIBooleanLine buildBooleanLine(List<FragmentGroup> contributors)
	{
		List<Float> globalTimes = collectGlobalTimes(contributors, g -> g.visibility);
		if(globalTimes.isEmpty())
			return null;

		float[] times = new float[globalTimes.size()];
		Boolean[] values = new Boolean[globalTimes.size()];
		for(int i = 0; i < globalTimes.size(); i++)
		{
			float tGlobal = globalTimes.get(i);
			times[i] = tGlobal;
			boolean any = false;
			boolean hadContributor = false;
			for(FragmentGroup fg : contributors)
			{
				IIBooleanLine line = fg.group.visibility;
				if(line==null)
					continue;
				float tLocal = toLocalTime(tGlobal, fg.fragment);
				if(tLocal < 0f||tLocal > 1f)
					continue;
				hadContributor = true;
				any |= Boolean.TRUE.equals(line.getForTime(tLocal));
			}
			values[i] = hadContributor&&any;
		}
		return new IIBooleanLine(times, values);
	}

	@Nullable
	private IIShaderLine buildShaderLine(List<FragmentGroup> contributors)
	{
		//Determine shader type (last fragment wins)
		Shaders shaderType = null;
		for(FragmentGroup fg : contributors)
			if(fg.group.shader!=null)
				shaderType = fg.group.shader.getShader();
		if(shaderType==null)
			return null;

		List<Float> globalTimes = collectGlobalTimes(contributors, g -> g.shader);
		if(globalTimes.isEmpty())
			return null;

		float[] times = new float[globalTimes.size()];
		Float[][] values = new Float[globalTimes.size()][];
		for(int i = 0; i < globalTimes.size(); i++)
		{
			float tGlobal = globalTimes.get(i);
			times[i] = tGlobal;
			Float[] chosen = null;
			//iterate backwards – later fragments (higher timestamp) win
			for(int c = contributors.size()-1; c >= 0; c--)
			{
				FragmentGroup fg = contributors.get(c);
				IIShaderLine line = fg.group.shader;
				if(line==null)
					continue;
				float tLocal = toLocalTime(tGlobal, fg.fragment);
				if(tLocal < 0f||tLocal > 1f)
					continue;
				chosen = line.getForTime(tLocal);
				break;
			}
			values[i] = chosen;
		}
		return new IIShaderLine(shaderType, times, values);
	}

	//--- Timeline start and end checks ---//

	/**
	 * Generic method to ensure a line has keyframes at times 0 and 1.
	 * If missing, the first/last value is repeated.
	 *
	 * @param line    the line to process (may be null)
	 * @param creator factory to create a new line from padded arrays
	 * @param <L>     line type
	 * @param <V>     value array type (e.g. Vec3d[], Float[], Boolean[], Float[][])
	 * @return the padded line, or the original if already correct
	 */
	@Nullable
	private <L, V> L ensureLine(@Nullable L line,
								BiFunction<float[], V, L> creator,
								Function<L, float[]> timesExtractor,
								Function<L, V> valuesExtractor,
								IntFunction<V> arrayCreator)
	{
		if(line==null)
			return null;
		float[] times = timesExtractor.apply(line);
		if(times.length==0)
			return line;
		V values = valuesExtractor.apply(line);

		boolean has0 = Math.abs(times[0]) <= TIME_EPSILON;
		boolean has1 = Math.abs(times[times.length-1]-1f) <= TIME_EPSILON;
		if(has0&&has1)
			return line;

		int newLen = times.length+(has0?0: 1)+(has1?0: 1);
		float[] newTimes = new float[newLen];
		V newValues = arrayCreator.apply(newLen);

		int idx = 0;
		if(!has0)
		{
			newTimes[idx] = 0f;
			//copy first value
			System.arraycopy(values, 0, newValues, idx, 1);
			idx++;
		}
		System.arraycopy(times, 0, newTimes, idx, times.length);
		System.arraycopy(values, 0, newValues, idx, times.length);
		idx += times.length;
		if(!has1)
		{
			newTimes[idx] = 1f;
			//copy last value
			System.arraycopy(values, times.length-1, newValues, idx, 1);
		}
		return creator.apply(newTimes, newValues);
	}

	//Convenience wrappers for each line type
	@Nullable
	private IIVectorLine ensureLine(@Nullable IIVectorLine line)
	{
		return ensureLine(line, IIVectorLine::new, l -> l.timeframes, l -> l.values, Vec3d[]::new);
	}

	@Nullable
	private IIFloatLine ensureLine(@Nullable IIFloatLine line)
	{
		return ensureLine(line, IIFloatLine::new, l -> l.timeframes, l -> l.values, Float[]::new);
	}

	@Nullable
	private IIBooleanLine ensureLine(@Nullable IIBooleanLine line)
	{
		return ensureLine(line, IIBooleanLine::new, l -> l.timeframes, l -> l.values, Boolean[]::new);
	}

	@Nullable
	private IIShaderLine ensureLine(@Nullable IIShaderLine line)
	{
		if(line==null)
			return null;
		//Shader line uses Float[][] as value type – handle separately because we cannot use a generic array creator easily
		float[] times = line.timeframes;
		Float[][] values = line.values;
		if(times.length==0)
			return line;

		boolean has0 = Math.abs(times[0]) <= TIME_EPSILON;
		boolean has1 = Math.abs(times[times.length-1]-1f) <= TIME_EPSILON;
		if(has0&&has1)
			return line;

		int newLen = times.length+(has0?0: 1)+(has1?0: 1);
		float[] newTimes = new float[newLen];
		Float[][] newValues = new Float[newLen][];

		int idx = 0;
		if(!has0)
		{
			newTimes[idx] = 0f;
			newValues[idx] = values[0];
			idx++;
		}
		System.arraycopy(times, 0, newTimes, idx, times.length);
		System.arraycopy(values, 0, newValues, idx, values.length);
		idx += times.length;
		if(!has1)
		{
			newTimes[idx] = 1f;
			newValues[idx] = values[values.length-1];
		}
		return new IIShaderLine(line.getShader(), newTimes, newValues);
	}

	private static class AnimationFragment
	{
		final IIAnimation animation;
		final float timestamp, duration;
		final int layer;
		float ownerTotalTime;   //totalTime of the owning builder

		AnimationFragment(IIAnimation animation, float timestamp, float duration)
		{
			this(animation, timestamp, duration, 0);
		}

		AnimationFragment(IIAnimation animation, float timestamp, float duration, int layer)
		{
			this.animation = animation;
			this.timestamp = timestamp;
			this.duration = duration;
			this.layer = layer;
		}

		float getTimestamp()
		{
			return timestamp;
		}

		float getDuration()
		{
			return duration;
		}

		int getLayer()
		{
			return layer;
		}
	}

	private static class LayeredVectorAccum
	{
		Vec3d sum = Vec3d.ZERO;
		int count = 0;
	}

	private static class LayeredFloatAccum
	{
		float sum = 0f;
		int count = 0;
	}
}
