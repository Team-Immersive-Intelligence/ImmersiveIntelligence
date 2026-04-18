package pl.pabilo8.immersiveintelligence.common.util.amt;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil.Shaders;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * A builder class for compound animations baked from multiple animations.
 *
 * @author Pabilo8
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
		if(animation!=null&&duration > 0)
		{
			AnimationFragment frag = new AnimationFragment(animation, timestamp, duration, layer);
			animationFragments.add(frag);
			totalTime = Math.max(totalTime, timestamp+duration);
			frag.ownerTotalTime = totalTime;
		}
		return this;
	}

	public IIAnimationBuilder addAnimation(float timestamp, float duration, IIAnimationGroup group, int layer)
	{
		if(group!=null&&duration > 0)
		{
			IIAnimation anim = new IIAnimation(IIReference.RES_II.with(group.groupName), new IIAnimationGroup[]{group});
			AnimationFragment frag = new AnimationFragment(anim, timestamp, duration, layer);
			animationFragments.add(frag);
			totalTime = Math.max(totalTime, timestamp+duration);
			frag.ownerTotalTime = totalTime;
		}
		return this;
	}

	public float getLastKeyframeTimeFor(String groupName)
	{
		float last = 0f;
		for(AnimationFragment f : animationFragments)
		{
			IIAnimationGroup g = findGroup(f.animation, groupName);
			if(g!=null) last = Math.max(last, f.timestamp+f.duration);
		}
		return last;
	}

	public float getFirstKeyframeTimeFor(String groupName)
	{
		float first = totalTime;
		for(AnimationFragment f : animationFragments)
		{
			IIAnimationGroup g = findGroup(f.animation, groupName);
			if(g!=null) first = Math.min(first, f.timestamp);
		}
		return first;
	}

	public IIAnimation build()
	{
		insertHoldFragments();
		if(animationFragments.isEmpty()||totalTime <= 0)
			return new IIAnimation(res, new IIAnimationGroup[0]);

		animationFragments.forEach(f -> f.ownerTotalTime = totalTime);
		animationFragments.sort(Comparator.comparingDouble(AnimationFragment::getTimestamp)
				.thenComparingInt(AnimationFragment::getLayer));

		Set<String> groupNames = new LinkedHashSet<>();
		animationFragments.forEach(f -> {
			for(IIAnimationGroup g : f.animation.groups) groupNames.add(g.groupName);
		});

		List<IIAnimationGroup> outGroups = new ArrayList<>();
		for(String name : groupNames)
		{
			List<FragmentGroup> contributors = new ArrayList<>();
			for(AnimationFragment f : animationFragments)
			{
				IIAnimationGroup g = findGroup(f.animation, name);
				if(g!=null) contributors.add(new FragmentGroup(f, g));
			}
			if(contributors.isEmpty()) continue;

			outGroups.add(new IIAnimationGroup(name,
					ensureLine(buildLine(contributors, g -> g.position, Vec3d.ZERO, Vec3d::add, v -> v.scale(1f))),
					ensureLine(buildLine(contributors, g -> g.scale, Vec3d.ZERO, Vec3d::add, v -> v.scale(1f))),
					ensureLine(buildLine(contributors, g -> g.rotation, Vec3d.ZERO, Vec3d::add, v -> v.scale(1f))),
					ensureLine(buildBooleanLine(contributors)),
					ensureLine(buildShaderLine(contributors)),
					ensureLine(buildFloatLine(contributors))
			));
		}
		return new IIAnimation(res, outGroups.toArray(new IIAnimationGroup[0]));
	}

	//--- Hold fragment insertion ---//
	private void insertHoldFragments()
	{
		Map<String, List<AnimationFragment>> layer0Frags = new HashMap<>();
		for(AnimationFragment f : animationFragments)
		{
			if(f.layer!=0) continue;
			for(IIAnimationGroup g : f.animation.groups)
				layer0Frags.computeIfAbsent(g.groupName, k -> new ArrayList<>()).add(f);
		}

		List<AnimationFragment> newFrags = new ArrayList<>();
		for(Map.Entry<String, List<AnimationFragment>> e : layer0Frags.entrySet())
		{
			List<AnimationFragment> frags = e.getValue();
			frags.sort(Comparator.comparingDouble(AnimationFragment::getTimestamp));
			String groupName = e.getKey();

			for(int i = 0; i < frags.size(); i++)
			{
				AnimationFragment curr = frags.get(i);
				float currEnd = curr.timestamp+curr.duration;
				float nextStart = (i+1 < frags.size())?frags.get(i+1).timestamp: totalTime;

				if(nextStart > currEnd+TIME_EPSILON)
				{
					IIAnimationGroup currGroup = findGroup(curr.animation, groupName);
					if(currGroup!=null)
					{
						IIAnimationGroup holdGroup = createStaticHoldGroup(currGroup);
						IIAnimation holdAnim = new IIAnimation(curr.animation.res, new IIAnimationGroup[]{holdGroup});
						newFrags.add(new AnimationFragment(holdAnim, currEnd, nextStart-currEnd, 0));
					}
				}
			}
		}
		animationFragments.addAll(newFrags);
		animationFragments.sort(Comparator.comparingDouble(AnimationFragment::getTimestamp)
				.thenComparingInt(AnimationFragment::getLayer));
	}

	private IIAnimationGroup createStaticHoldGroup(IIAnimationGroup source)
	{
		Vec3d finalPos = getFinal(source.position);
		Vec3d finalScale = getFinal(source.scale);
		Vec3d finalRot = getFinal(source.rotation);
		Boolean finalVis = getFinal(source.visibility);
		Float finalProp = getFinal(source.property);
		Float[] finalShader = getFinal(source.shader);

		IIVectorLine posLine = finalPos!=null?new IIVectorLine(new float[]{0f}, new Vec3d[]{finalPos}): null;
		IIVectorLine scaleLine = finalScale!=null?new IIVectorLine(new float[]{0f}, new Vec3d[]{finalScale}): null;
		IIVectorLine rotLine = finalRot!=null?new IIVectorLine(new float[]{0f}, new Vec3d[]{finalRot}): null;
		IIBooleanLine visLine = finalVis!=null?new IIBooleanLine(new float[]{0f}, new Boolean[]{finalVis}): null;
		IIFloatLine propLine = finalProp!=null?new IIFloatLine(new float[]{0f}, new Float[]{finalProp}): null;
		IIShaderLine shaderLine = null;
		if(finalShader!=null&&source.shader.getShader()!=null)
			shaderLine = new IIShaderLine(source.shader.getShader(), new float[]{0f}, new Float[][]{finalShader});

		return new IIAnimationGroup(source.groupName, posLine, scaleLine, rotLine, visLine, shaderLine, propLine);
	}

	@Nullable
	private <T> T getFinal(@Nullable IIAnimationLine<T> line)
	{
		return line!=null?line.getForTime(1f): null;
	}

	//--- Line builders ---//
	@Nullable
	private IIVectorLine buildLine(List<FragmentGroup> contributors,
								   Function<IIAnimationGroup, IIVectorLine> getter,
								   Vec3d zero, BiFunction<Vec3d, Vec3d, Vec3d> add,
								   Function<Vec3d, Vec3d> scaleFunc)
	{
		List<Float> globalTimes = collectGlobalTimes(contributors, getter::apply);
		if(globalTimes.isEmpty()) return null;

		float[] times = new float[globalTimes.size()];
		Vec3d[] values = new Vec3d[globalTimes.size()];

		for(int i = 0; i < globalTimes.size(); i++)
		{
			float tGlobal = globalTimes.get(i);
			times[i] = tGlobal;

			Map<Integer, Vec3d> layerSums = new HashMap<>();
			Map<Integer, Integer> layerCounts = new HashMap<>();

			for(FragmentGroup fg : contributors)
			{
				IIVectorLine line = getter.apply(fg.group);
				if(line==null) continue;
				float tLocal = toLocalTime(tGlobal, fg.fragment);
				if(tLocal < 0f||tLocal > 1f) continue;

				Vec3d val = line.getForTime(tLocal);
				int layer = fg.fragment.layer;
				layerSums.merge(layer, val, add);
				layerCounts.merge(layer, 1, Integer::sum);
			}

			Vec3d total = zero;
			if(layerCounts.containsKey(0))
			{
				int cnt = layerCounts.get(0);
				total = add.apply(total, scaleFunc.apply(layerSums.get(0).scale(1f/cnt)));
			}
			for(Map.Entry<Integer, Vec3d> e : layerSums.entrySet())
			{
				if(e.getKey()==0) continue;
				int cnt = layerCounts.get(e.getKey());
				total = add.apply(total, scaleFunc.apply(e.getValue().scale(1f/cnt)));
			}
			values[i] = total;
		}
		return new IIVectorLine(times, values);
	}

	@Nullable
	private IIFloatLine buildFloatLine(List<FragmentGroup> contributors)
	{
		List<Float> globalTimes = collectGlobalTimes(contributors, g -> g.property);
		if(globalTimes.isEmpty()) return null;

		float[] times = new float[globalTimes.size()];
		Float[] values = new Float[globalTimes.size()];
		for(int i = 0; i < globalTimes.size(); i++)
		{
			float tGlobal = globalTimes.get(i);
			times[i] = tGlobal;

			Map<Integer, Float> layerSums = new HashMap<>();
			Map<Integer, Integer> layerCounts = new HashMap<>();
			for(FragmentGroup fg : contributors)
			{
				IIFloatLine line = fg.group.property;
				if(line==null) continue;
				float tLocal = toLocalTime(tGlobal, fg.fragment);
				if(tLocal < 0f||tLocal > 1f) continue;
				Float v = line.getForTime(tLocal);
				if(v==null) continue;
				int layer = fg.fragment.layer;
				layerSums.merge(layer, v, Float::sum);
				layerCounts.merge(layer, 1, Integer::sum);
			}

			float total = 0f;
			for(Map.Entry<Integer, Float> e : layerSums.entrySet())
			{
				int cnt = layerCounts.get(e.getKey());
				total += e.getValue()/cnt;
			}
			values[i] = total;
		}
		return new IIFloatLine(times, values);
	}

	@Nullable
	private IIBooleanLine buildBooleanLine(List<FragmentGroup> contributors)
	{
		List<Float> globalTimes = collectGlobalTimes(contributors, g -> g.visibility);
		if(globalTimes.isEmpty()) return null;

		float[] times = new float[globalTimes.size()];
		Boolean[] values = new Boolean[globalTimes.size()];
		for(int i = 0; i < globalTimes.size(); i++)
		{
			float tGlobal = globalTimes.get(i);
			times[i] = tGlobal;
			boolean any = false, had = false;
			for(FragmentGroup fg : contributors)
			{
				IIBooleanLine line = fg.group.visibility;
				if(line==null) continue;
				float tLocal = toLocalTime(tGlobal, fg.fragment);
				if(tLocal < 0f||tLocal > 1f) continue;
				had = true;
				any |= Boolean.TRUE.equals(line.getForTime(tLocal));
			}
			values[i] = had&&any;
		}
		return new IIBooleanLine(times, values);
	}

	@Nullable
	private IIShaderLine buildShaderLine(List<FragmentGroup> contributors)
	{
		Shaders shaderType = null;
		for(FragmentGroup fg : contributors)
			if(fg.group.shader!=null) shaderType = fg.group.shader.getShader();
		if(shaderType==null) return null;

		List<Float> globalTimes = collectGlobalTimes(contributors, g -> g.shader);
		if(globalTimes.isEmpty()) return null;

		float[] times = new float[globalTimes.size()];
		Float[][] values = new Float[globalTimes.size()][];
		for(int i = 0; i < globalTimes.size(); i++)
		{
			float tGlobal = globalTimes.get(i);
			times[i] = tGlobal;
			for(int c = contributors.size()-1; c >= 0; c--)
			{
				FragmentGroup fg = contributors.get(c);
				IIShaderLine line = fg.group.shader;
				if(line==null) continue;
				float tLocal = toLocalTime(tGlobal, fg.fragment);
				if(tLocal < 0f||tLocal > 1f) continue;
				values[i] = line.getForTime(tLocal);
				break;
			}
		}
		return new IIShaderLine(shaderType, times, values);
	}

	//--- Time utilities ---//
	private List<Float> collectGlobalTimes(List<FragmentGroup> contributors,
										   Function<IIAnimationGroup, IIAnimationLine<?>> lineGetter)
	{
		List<Float> out = new ArrayList<>();
		for(FragmentGroup fg : contributors)
		{
			IIAnimationLine<?> line = lineGetter.apply(fg.group);
			if(line!=null&&line.timeframes.length > 0)
			{
				addGlobalTime(out, fg.fragment, 0f);
				for(float t : line.timeframes) addGlobalTime(out, fg.fragment, t);
				addGlobalTime(out, fg.fragment, 1f);
			}
		}
		return normalizeAndSortTimes(out);
	}

	private void addGlobalTime(List<Float> out, AnimationFragment frag, float tLocal)
	{
		float t = (frag.timestamp+tLocal*frag.duration)/totalTime;
		out.add(Math.max(0f, Math.min(1f, t)));
	}

	private float toLocalTime(float tGlobal, AnimationFragment frag)
	{
		return (tGlobal*frag.ownerTotalTime-frag.timestamp)/frag.duration;
	}

	private List<Float> normalizeAndSortTimes(List<Float> times)
	{
		if(times.isEmpty()) return Collections.emptyList();
		Collections.sort(times);
		List<Float> deduped = new ArrayList<>();
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
				deduped.set(deduped.size()-1, t);
			}
		}
		return deduped;
	}

	//--- Ensure lines have keyframes at 0 and 1 ---//
	@SuppressWarnings("SuspiciousSystemArraycopy")
	@Nullable
	private <L, V> L ensureLine(@Nullable L line, BiFunction<float[], V, L> creator,
								Function<L, float[]> timesExtractor, Function<L, V> valuesExtractor,
								IntFunction<V> arrayCreator)
	{
		if(line==null) return null;
		float[] times = timesExtractor.apply(line);
		if(times.length==0) return line;
		V values = valuesExtractor.apply(line);

		boolean has0 = Math.abs(times[0]) <= TIME_EPSILON;
		boolean has1 = Math.abs(times[times.length-1]-1f) <= TIME_EPSILON;
		if(has0&&has1) return line;

		int newLen = times.length+(has0?0: 1)+(has1?0: 1);
		float[] newTimes = new float[newLen];
		V newValues = arrayCreator.apply(newLen);

		int idx = 0;
		if(!has0)
		{
			newTimes[idx] = 0f;
			System.arraycopy(values, 0, newValues, idx, 1);
			idx++;
		}
		System.arraycopy(times, 0, newTimes, idx, times.length);
		System.arraycopy(values, 0, newValues, idx, times.length);
		idx += times.length;
		if(!has1)
		{
			newTimes[idx] = 1f;
			System.arraycopy(values, times.length-1, newValues, idx, 1);
		}
		return creator.apply(newTimes, newValues);
	}

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
		if(line==null) return null;
		float[] times = line.timeframes;
		Float[][] values = line.values;
		if(times.length==0) return line;

		boolean has0 = Math.abs(times[0]) <= TIME_EPSILON;
		boolean has1 = Math.abs(times[times.length-1]-1f) <= TIME_EPSILON;
		if(has0&&has1) return line;

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

	//--- Utility ---//
	@Nullable
	private static IIAnimationGroup findGroup(IIAnimation anim, String name)
	{
		for(IIAnimationGroup g : anim.groups)
			if(g.groupName.equals(name)) return g;
		return null;
	}

	public IIAnimationBuilder renameGroup(String oldName, String newName)
	{
		List<AnimationFragment> corrected = new ArrayList<>();
		Iterator<AnimationFragment> it = animationFragments.iterator();
		while(it.hasNext())
		{
			AnimationFragment f = it.next();
			for(IIAnimationGroup g : f.animation.groups)
			{
				if(g.groupName.equals(oldName))
				{
					it.remove();
					corrected.add(new AnimationFragment(f.animation.renameAnimationGroup(oldName, newName),
							f.timestamp, f.duration, f.layer));
					break;
				}
			}
		}
		animationFragments.addAll(corrected);
		return this;
	}

	//--- Inner classes ---//
	private static class AnimationFragment
	{
		final IIAnimation animation;
		final float timestamp, duration;
		final int layer;
		float ownerTotalTime;

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

		int getLayer()
		{
			return layer;
		}
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
}
