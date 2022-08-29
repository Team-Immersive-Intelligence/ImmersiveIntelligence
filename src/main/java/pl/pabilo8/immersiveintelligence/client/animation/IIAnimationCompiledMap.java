package pl.pabilo8.immersiveintelligence.client.animation;

import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.animation.IIAnimation.IIAnimationGroup;
import pl.pabilo8.immersiveintelligence.common.util.ArraylistJoinCollector;

import java.util.Arrays;
import java.util.HashMap;

/**
 * A map used for easily animating an array of AMTs
 * Use one per a single renderer per animation
 *
 * @author Pabilo8
 * @since 05.04.2022
 */
public class IIAnimationCompiledMap extends HashMap<AMT, IIAnimationGroup>
{
	private IIAnimationCompiledMap()
	{
		super();
	}

	public static IIAnimationCompiledMap create(AMT[] amts, IIAnimation animation)
	{
		IIAnimationCompiledMap map = new IIAnimationCompiledMap();
		//collect all child parts of the array
		AMT[] parts = Arrays.stream(amts)
				.map(AMT::getChildrenRecursive)
				.collect(new ArraylistJoinCollector<>())
				.toArray(new AMT[0]);

		//iterate through all animation groups, if name matches, put the part into the animation map
		for(IIAnimationGroup group : animation.groups)
			for(AMT amt : parts)
				if(group.groupName.equals(amt.name))
				{
					map.put(amt, group);
					break;
				}

		return map;
	}

	public static IIAnimationCompiledMap create(AMT[] AMTs, ResourceLocation res)
	{
		return create(AMTs, IIAnimationLoader.loadAnimation(res));
	}

	/**
	 * Applies the animation to a group of AMTs
	 *
	 * @param time time of the animation, 0.0-1.0
	 */
	public void apply(float time)
	{
		forEach((key, value) -> IIAnimationUtils.setModelAnimations(key, value, time));
	}
}
