package pl.pabilo8.immersiveintelligence.common.util.amt;

import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.EntityAMTTactile;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIAnimationGroup;

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
	private Matrix4 mat;
	private EnumFacing facing;

	private IIAnimationCollisionMap()
	{
		super();
	}

	public static IIAnimationCollisionMap create(List<EntityAMTTactile> tactiles, IIAnimation animation, EnumFacing facing, boolean isMirrored)
	{
		IIAnimationCollisionMap map = new IIAnimationCollisionMap();
		map.mat = new Matrix4(facing);
		map.facing = facing;

		//iterate through all animation groups, if name matches, put the part into the animation map
		for(IIAnimationGroup group : animation.groups)
			for(EntityAMTTactile tactile : tactiles)
				if(isTactileGroup(group)&&group.groupName.equals(tactile.name))
				{
					map.put(tactile, group);
					break;
				}


		return map;
	}

	private static boolean isTactileGroup(IIAnimationGroup group)
	{
		return group.position!=null||group.rotation!=null||group.scale!=null||group.visibility!=null;
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
		//translation
		if(group.position!=null)
		{
			tactile.translation = group.position.getForTime(time);

			//swap axis depending on facing
			if(facing==EnumFacing.EAST)
				tactile.translation = new Vec3d(-tactile.translation.z, tactile.translation.y, tactile.translation.x);
			else if(facing==EnumFacing.WEST)
				tactile.translation = new Vec3d(tactile.translation.z, tactile.translation.y, tactile.translation.x);
			else if(facing==EnumFacing.NORTH)
				tactile.translation = new Vec3d(-tactile.translation.x, tactile.translation.y, tactile.translation.z);

		}
		//rotation
		if(group.rotation!=null)
		{
			tactile.rotation = group.rotation.getForTime(time);

			switch(facing)
			{
				case NORTH:
					break;
				case SOUTH:
					break;
				case WEST:
					break;
				case EAST:
					break;
			}

			if(facing==EnumFacing.EAST)
				tactile.rotation = new Vec3d(-tactile.rotation.z, tactile.rotation.y, tactile.rotation.x);
			else if(facing==EnumFacing.WEST)
				tactile.rotation = new Vec3d(-tactile.rotation.z, tactile.rotation.y, tactile.rotation.x);
			else if(facing==EnumFacing.SOUTH)
				tactile.rotation = new Vec3d(-tactile.rotation.x, tactile.rotation.y, -tactile.rotation.z);
		}
		if(group.scale!=null)
			tactile.scale = mat.apply(group.scale.getForTime(time));
		if(group.visibility!=null)
			tactile.visibility = group.visibility.getForTime(time);
	}
}
