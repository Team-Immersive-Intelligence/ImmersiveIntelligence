package pl.pabilo8.immersiveintelligence.client.animation;

import pl.pabilo8.immersiveintelligence.client.animation.IIAnimation.IIAnimationGroup;

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

	public static IIAnimationCompiledMap create(AMT[] AMTs, IIAnimation animation)
	{
		IIAnimationCompiledMap map = new IIAnimationCompiledMap();

		for(IIAnimationGroup group : animation.groups)
			for(AMT amt : AMTs)
				if(group.groupName.equals(amt.name))
				{
					map.put(amt, group);
					break;
				}

		return map;
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
