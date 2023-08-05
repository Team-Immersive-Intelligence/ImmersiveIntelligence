package pl.pabilo8.immersiveintelligence.client.util.amt;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimation.IIAnimationGroup;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimation.IIBooleanLine;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * A map used for easily animating an array of AMTs
 * Use one per a single renderer per animation
 *
 * @author Pabilo8
 * @since 05.04.2022
 */
public class IIAnimationCachedMap
{
	@Nonnull
	AMTModelCache<?> model;
	@Nonnull
	IIAnimation animation;
	@Nonnull
	HashMap<AMT[], IIAnimationCompiledMap> map = new HashMap<>();

	private IIAnimationCachedMap(@Nonnull AMTModelCache<?> model, @Nonnull IIAnimation animation)
	{
		this.model = model;
		this.animation = animation;
	}

	public static IIAnimationCachedMap create(@Nonnull AMTModelCache<?> model, @Nonnull IIAnimation animation)
	{
		return new IIAnimationCachedMap(model, animation);
	}

	public static IIAnimationCachedMap create(@Nonnull AMTModelCache<?> model, @Nonnull ResourceLocation res)
	{
		return create(model, IIAnimationLoader.loadAnimation(res));
	}

	/**
	 * Creates a "visibility animation", that makes the elements contained in header visible and other elements invisible<br>
	 * Used by items with upgrades
	 *
	 * @param model     model cache
	 * @param mainModel primary model of the cache
	 * @return visibility animation
	 */
	public static IIAnimationCachedMap createVisibilityAnimation(@Nonnull AMTModelCache<?> model, @Nonnull OBJModel mainModel)
	{
		Set<String> present = mainModel.getMatLib().getGroups().keySet();
		IIAnimationGroup[] groups = Arrays.stream(IIAnimationUtils.getChildrenRecursive(model.getBase()))
				.filter(amt -> amt instanceof AMTQuads)
				.filter(amt -> !present.contains(amt.name))
				.map(amt ->
						new IIAnimationGroup(amt.name, null, null, null,
								new IIBooleanLine(new float[]{0}, new Boolean[]{false}),
								null, null)
				)
				.toArray(IIAnimationGroup[]::new);


		return create(model, new IIAnimation(new ResourceLocation(ImmersiveIntelligence.MODID, "visibility"), groups));
	}

	/**
	 * Applies the animation to a group of AMTs
	 *
	 * @param time time of the animation, 0.0-1.0
	 */
	public void apply(float time)
	{
		AMT[] last = model.getLast();
		map.putIfAbsent(last, IIAnimationCompiledMap.create(last, animation));
		map.get(last).apply(time);
	}
}
