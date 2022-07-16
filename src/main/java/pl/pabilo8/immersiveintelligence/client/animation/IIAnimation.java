package pl.pabilo8.immersiveintelligence.client.animation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;

// TODO: 05.04.2022 separate animations and offsets using a new model format

/**
 * @author Pabilo8
 * @since 03.04.2022
 */
public class IIAnimation
{
	public final ResourceLocation res;
	@Nonnull
	public final IIAnimationGroup[] groups;

	public IIAnimation(ResourceLocation res, @Nonnull IIAnimationGroup[] groups)
	{
		this.res = res;
		this.groups = groups;
	}

	/**
	 * @param res  the resource location of this animation
	 * @param json a json AMT animation object, in most cases loaded from a file
	 */
	public IIAnimation(ResourceLocation res, JsonObject json)
	{
		this.res = res;

		//get the groups
		JsonObject groups = json.getAsJsonObject("groups");
		this.groups = groups.entrySet().stream()
				.map(e -> new IIAnimationGroup(e.getKey(), e.getValue().getAsJsonObject()))
				.toArray(IIAnimationGroup[]::new);
		//there is also a 'comment' tag, but it's left out intentionally
	}

	public static class IIAnimationGroup
	{
		final String groupName;
		@Nullable
		final IIVectorLine position, scale;
		@Nullable
		final IIVectorLine rotation;
		@Nullable
		final IIBooleanLine visibility;

		/**
		 * For internal testing
		 */
		@Deprecated
		public IIAnimationGroup(String groupName, @Nullable IIVectorLine position, @Nullable IIVectorLine scale, @Nullable IIVectorLine color, @Nullable IIVectorLine rotation, @Nullable IIBooleanLine visibility)
		{
			this.groupName = groupName;
			this.position = position;
			this.scale = scale;
			this.rotation = rotation;
			this.visibility = visibility;
		}

		public IIAnimationGroup(String groupName, JsonObject json)
		{
			this.groupName = groupName;

			//load lines
			position = json.has("position")?loadPositionLine(loadLine(json, "position")): null;
			scale = json.has("scale")?loadVectorLine(loadLine(json, "scale")): null;
			rotation = json.has("rotation")?loadVectorLine(loadLine(json, "rotation")): null;

			// TODO: 05.04.2022 implement visibility json loading
			visibility = null;
		}

		// TODO: 05.04.2022 attempt to streamline the code more

		private Tuple<ArrayList<Float>, ArrayList<Vec3d>> loadLine(JsonObject json, String id)
		{
			JsonArray array = json.getAsJsonArray(id);

			ArrayList<Float> timeframes = new ArrayList<>();
			ArrayList<Vec3d> vectors = new ArrayList<>();

			//parse json
			if(array!=null)
				for(JsonElement jsonElement : array)
				{
					JsonObject obj = jsonElement.getAsJsonObject();
					timeframes.add(obj.get("time").getAsFloat());
					vectors.add(IIAnimationUtils.jsonToVec3d(obj.get("transform").getAsJsonArray()));
				}

			return new Tuple<>(timeframes, vectors);
		}

		private IIVectorLine loadPositionLine(Tuple<ArrayList<Float>, ArrayList<Vec3d>> tuple)
		{
			//to array
			float[] arr = new float[tuple.getFirst().size()];
			for(int i = 0; i < tuple.getFirst().size(); i++)
				arr[i] = tuple.getFirst().get(i);

			//get line
			return new IIVectorLine(arr, tuple.getSecond().toArray(new Vec3d[0]), 0.0625f);
		}

		private IIVectorLine loadVectorLine(Tuple<ArrayList<Float>, ArrayList<Vec3d>> tuple)
		{
			//to array
			float[] arr = new float[tuple.getFirst().size()];
			for(int i = 0; i < tuple.getFirst().size(); i++)
				arr[i] = tuple.getFirst().get(i);

			//get line
			return new IIVectorLine(arr, tuple.getSecond().toArray(new Vec3d[0]));
		}
	}

	public static abstract class IIAnimationLine<T>
	{
		public final float[] timeframes;
		public final float[] durations;
		public final T[] values;

		public IIAnimationLine(float[] timeframes, T[] values)
		{
			this.timeframes = timeframes;
			if(timeframes.length==1)
				this.durations = new float[]{1};
			else
			{
				durations = new float[timeframes.length];

				for(int i = 0; i < timeframes.length-1; i++)
					durations[i] = timeframes[i+1]-timeframes[i];
				durations[durations.length-1] = 1f;
			}

			this.values = values;
		}

		public abstract T interpolate(T t1, T t2, float value);

		public T getForTime(float time)
		{
			for(int i = timeframes.length-1; i >= 0; i--)
				if(time >= timeframes[i])
				{
					if(time==timeframes[i])
						return values[i];
					if(i+1 < timeframes.length)
						return interpolate(values[i], values[i+1], (time-timeframes[i])/durations[i]);
				}
			return values[0];
		}
	}

	/**
	 * Used for vector values, such as position, scale, color
	 */
	public static class IIVectorLine extends IIAnimationLine<Vec3d>
	{
		public IIVectorLine(float[] timeframes, Vec3d[] values)
		{
			super(timeframes, values);
		}

		public IIVectorLine(float[] timeframes, Vec3d[] values, float scale)
		{
			this(timeframes, Arrays.stream(values).map(vec3d -> vec3d.scale(scale)).toArray(Vec3d[]::new));
		}

		@Override
		public Vec3d interpolate(Vec3d t1, Vec3d t2, float value)
		{
			return new Vec3d(
					MathHelper.clampedLerp(t1.x, t2.x, value),
					MathHelper.clampedLerp(t1.y, t2.y, value),
					MathHelper.clampedLerp(t1.z, t2.z, value)
			);
		}
	}

	/**
	 * Used for boolean values, such as visibility
	 */
	public static class IIBooleanLine extends IIAnimationLine<Boolean>
	{
		public IIBooleanLine(float[] timeframes, Boolean[] values)
		{
			super(timeframes, values);
		}

		@Override
		public Boolean interpolate(Boolean t1, Boolean t2, float value)
		{
			return t1;
		}

		@Override
		public Boolean getForTime(float time)
		{
			for(int i = 0; i < timeframes.length; i++)
				if(time > timeframes[i])
					return values[i];
			return values[0];
		}
	}
}
