package pl.pabilo8.immersiveintelligence.common.util.raytracer;

import com.google.gson.JsonArray;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.function.Function;

/**
 * AABB wrapper handling their rotation
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.04.2023
 */
public class AxisAlignedFacingBB
{
	private final AxisAlignedBB[] facings = new AxisAlignedBB[EnumFacing.values().length];
	private final AxisAlignedBB[] facingsMirrored = new AxisAlignedBB[EnumFacing.values().length];

	public AxisAlignedFacingBB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
	{
		this(new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ));
	}

	public AxisAlignedFacingBB(AxisAlignedBB north)
	{
		//North
		setFacing(EnumFacing.NORTH, north, true);
		//South
		setFacing(EnumFacing.SOUTH, rotateClockwise(rotateClockwise(north)), true);
		//West
		setFacing(EnumFacing.WEST, rotateClockwise(north), false);
		//East
		setFacing(EnumFacing.EAST, rotateCounterClockwise(north), false);
		//Up
		setFacing(EnumFacing.UP, rotateUp(north), true);
		//Down
		setFacing(EnumFacing.DOWN, rotateDown(north), true);
	}

	public AxisAlignedFacingBB(JsonArray array)
	{
		this(new AxisAlignedBB(
				getParam(array, 0), getParam(array, 1), getParam(array, 2),
				getParam(array, 3), getParam(array, 4), getParam(array, 5))
		);
	}

	private static float getParam(JsonArray array, int id)
	{
		return array.get(id).getAsFloat()/16f;
	}

	//--- Transforms ---//
	private void setFacing(EnumFacing facing, AxisAlignedBB aabb, boolean mirrorAlongX)
	{
		facings[facing.ordinal()] = aabb;
		facingsMirrored[facing.ordinal()] = mirrorAlongX?mirrorX(aabb): mirrorZ(aabb);
	}

	private static AxisAlignedBB mirrorX(AxisAlignedBB aabb)
	{
		return new AxisAlignedBB(1-aabb.maxX, aabb.minY, aabb.minZ, 1-aabb.minX, aabb.maxY, aabb.maxZ);
	}

	private static AxisAlignedBB mirrorZ(AxisAlignedBB aabb)
	{
		return new AxisAlignedBB(aabb.minX, aabb.minY, 1-aabb.maxZ, aabb.maxX, aabb.maxY, 1-aabb.minZ);
	}

	private static AxisAlignedBB rotateClockwise(AxisAlignedBB aabb)
	{
		return new AxisAlignedBB(aabb.minZ, aabb.minY, 1-aabb.maxX, aabb.maxZ, aabb.maxY, 1-aabb.minX);
	}

	private static AxisAlignedBB rotateCounterClockwise(AxisAlignedBB aabb)
	{
		return new AxisAlignedBB(1-aabb.maxZ, aabb.minY, aabb.minX, 1-aabb.minZ, aabb.maxY, aabb.maxX);
	}

	private static AxisAlignedBB rotateUp(AxisAlignedBB aabb)
	{
		return new AxisAlignedBB(aabb.minX, 1-aabb.maxZ, aabb.minY, aabb.maxX, 1-aabb.minZ, aabb.maxY);
	}

	private static AxisAlignedBB rotateDown(AxisAlignedBB aabb)
	{
		return new AxisAlignedBB(aabb.minX, aabb.minZ, 1-aabb.maxY, aabb.maxX, aabb.maxZ, 1-aabb.minY);
	}

	//--- Outputs ---//

	public AxisAlignedFacingBB transform(Function<AxisAlignedBB, AxisAlignedBB> function)
	{
		return new AxisAlignedFacingBB(function.apply(facings[EnumFacing.NORTH.ordinal()]));
	}

	public AxisAlignedBB getFacing(EnumFacing facing, boolean mirrored)
	{
		return (mirrored?facingsMirrored: facings)[facing.ordinal()];
	}

	public float[] getFacingBounds(EnumFacing facing, boolean mirrored)
	{
		AxisAlignedBB aabb = getFacing(facing, mirrored);
		return new float[]{
				(float)aabb.minX, (float)aabb.minY, (float)aabb.minZ,
				(float)aabb.maxX, (float)aabb.maxY, (float)aabb.maxZ
		};
	}
}
