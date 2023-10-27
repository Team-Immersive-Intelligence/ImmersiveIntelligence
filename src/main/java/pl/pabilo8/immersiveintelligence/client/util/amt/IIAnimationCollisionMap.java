package pl.pabilo8.immersiveintelligence.client.util.amt;

import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimation.IIAnimationGroup;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.EntityAMTTactile;

import java.util.HashMap;
import java.util.List;

/**
 * A map used for easily animating an array of AMTs
 * Use one per a single renderer per animation
 *
 * @author Pabilo8
 * @since 05.04.2022
 */
public class IIAnimationCollisionMap extends HashMap<EntityAMTTactile, IIAnimationGroup>
{
	private IIAnimationCollisionMap()
	{
		super();
	}

	public static IIAnimationCollisionMap create(List<EntityAMTTactile> tactiles, IIAnimation animation)
	{
		IIAnimationCollisionMap map = new IIAnimationCollisionMap();
		//iterate through all animation groups, if name matches, put the part into the animation map
		for(IIAnimationGroup group : animation.groups)
			for(EntityAMTTactile tactile : tactiles)
				if(!isTactileGroup(group)&&group.groupName.equals(tactile.name))
				{
					map.put(tactile, group);
					break;
				}


		return map;
	}

	private static boolean isTactileGroup(IIAnimationGroup group)
	{
		return group.position!=null||group.rotation!=null||group.scale!=null;
	}

	/**
	 * Applies the animation to a group of AMTs
	 *
	 * @param time time of the animation, 0.0-1.0
	 */
	public void apply(float time)
	{
		forEach((key, value) -> setAnimationGroups(key, value, time));
	}

	private void setAnimationGroups(EntityAMTTactile tactile, IIAnimationGroup group, float time)
	{
		if(group.position!=null)
			tactile.translation = group.position.getForTime(time);
		if(group.rotation!=null)
			tactile.rotation = group.rotation.getForTime(time);
		if(group.scale!=null)
			tactile.scale = group.scale.getForTime(time);
	}
}
