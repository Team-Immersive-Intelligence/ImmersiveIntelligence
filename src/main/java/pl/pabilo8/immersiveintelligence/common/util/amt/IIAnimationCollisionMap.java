package pl.pabilo8.immersiveintelligence.common.util.amt;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.EntityAMTTactile;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIAnimationGroup;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * A map used for easily animating an array of AMTs
 * Use one per a single renderer per animation
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.04.2022
 */
public class IIAnimationCollisionMap extends HashMap<EntityAMTTactile, IIAnimationGroup>
{
	private Map<EntityAMTTactile, TactileTransform> transforms;

	private IIAnimationCollisionMap()
	{
		super();
	}

	public static IIAnimationCollisionMap create(List<EntityAMTTactile> tactiles, IIAnimation animation, EnumFacing facing, boolean isMirrored)
	{
		TactileTransform transform = TactileTransform.resolve(null, facing, isMirrored);
		Map<EntityAMTTactile, TactileTransform> transforms = new IdentityHashMap<>();
		tactiles.forEach(tactile -> transforms.put(tactile, transform));
		return create(tactiles, animation, transforms);
	}

	public static IIAnimationCollisionMap create(List<EntityAMTTactile> tactiles, IIAnimation animation,
												 Map<EntityAMTTactile, TactileTransform> transforms)
	{
		IIAnimationCollisionMap map = new IIAnimationCollisionMap();
		map.transforms = transforms;

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
		TactileTransform transform = transforms.get(tactile);
		if(transform==null)
			return;
		//translation
		if(group.position!=null)
			tactile.translation = transform.transformAnimationTranslation(group.position.getForTime(time));
		//rotation
		if(group.rotation!=null)
			tactile.rotation = transform.transformAnimationRotation(group.rotation.getForTime(time));
		if(group.scale!=null)
			tactile.scale = group.scale.getForTime(time);
		if(group.visibility!=null)
			tactile.visibility = group.visibility.getForTime(time);
	}

	/**
	 * Applies the same local mirror and horizontal facing rotation as the multiblock renderer.
	 */
	public static Vec3d transformTranslation(Vec3d translation, EnumFacing facing, boolean mirrored)
	{
		return TactileTransform.resolve(null, facing, mirrored).transformAnimationTranslation(translation);
	}

	/**
	 * Transforms a model-local direction without the translation channel's AMT X inversion.
	 */
	public static Vec3d transformModelDirection(Vec3d vector, EnumFacing facing, boolean mirrored)
	{
		Vec3d local = mirrored?new Vec3d(-vector.x, vector.y, vector.z): vector;
		return TactileTransform.resolve(null, facing, false).rotateDirection(local);
	}

	/**
	 * Converts Blockbench/AMT Euler axes to the collision hierarchy's axes, then treats the
	 * rotation as an axial vector. Reflection on X therefore reverses Y and Z rotation.
	 */
	public static Vec3d transformRotation(Vec3d rotation, EnumFacing facing, boolean mirrored)
	{
		return TactileTransform.resolve(null, facing, mirrored).transformAnimationRotation(rotation);
	}

	/**
	 * Rotates a direction around the renderer's -Y facing axis, without introducing the
	 * block-centre translation contained in an affine model matrix.
	 */
	public static Vec3d rotateHorizontal(Vec3d vector, EnumFacing facing)
	{
		switch(facing)
		{
			case WEST:
				return new Vec3d(-vector.z, vector.y, vector.x);
			case NORTH:
				return new Vec3d(-vector.x, vector.y, -vector.z);
			case EAST:
				return new Vec3d(vector.z, vector.y, -vector.x);
			case SOUTH:
			default:
				return vector;
		}
	}
}
