package pl.pabilo8.immersiveintelligence.common.util.raytracer;

import com.google.gson.JsonArray;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.function.Function;

/**
 * AABB wrapper handling their rotation
 *
 * @author Pabilo8
 * @since 13.04.2023
 */
public class AxisAlignedFacingBB
{
	private final AxisAlignedBB[] facings = new AxisAlignedBB[4], facingsMirrored = new AxisAlignedBB[4];

	public AxisAlignedFacingBB(AxisAlignedBB north)
	{
		//North
		facings[0] = north;
		facingsMirrored[0] = mirrorX(north);
		//South
		facings[1] = new AxisAlignedBB(1-north.maxX, north.minY, 1-north.maxZ, 1-north.minX, north.maxY, 1-north.minZ);
		facingsMirrored[1] = mirrorX(facings[1]);
		//East
		facings[2] = new AxisAlignedBB(north.minZ, north.minY, 1-north.maxX, north.maxZ, north.maxY, 1-north.minX);
		facingsMirrored[2] = mirrorZ(facings[2]);
		//West
		facings[3] = new AxisAlignedBB(1-north.maxZ, north.minY, north.minX, 1-north.minZ, north.maxY, north.maxX);
		facingsMirrored[3] = mirrorZ(facings[3]);

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

	//--- Outputs ---//

	public AxisAlignedFacingBB transform(Function<AxisAlignedBB, AxisAlignedBB> function)
	{
		return new AxisAlignedFacingBB(function.apply(facings[0]));
	}

	public AxisAlignedBB getFacing(EnumFacing facing, boolean mirrored)
	{
		return (mirrored?facingsMirrored: facings)[(facing.ordinal()-2)%4];
	}
}
