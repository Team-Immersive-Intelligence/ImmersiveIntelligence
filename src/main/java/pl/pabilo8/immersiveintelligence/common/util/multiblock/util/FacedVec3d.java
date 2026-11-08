package pl.pabilo8.immersiveintelligence.common.util.multiblock.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

/**
 * Caches a block-local vector for all facings and mirroring states.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 15.10.2026
 */
public final class FacedVec3d
{
	private final Vec3d[] facings = new Vec3d[EnumFacing.VALUES.length];
	private final Vec3d[] mirroredFacings = new Vec3d[EnumFacing.VALUES.length];

	/**
	 * Uses the SOUTH-facing AMT coordinate system and the block centre as the rotation pivot.
	 * Mirroring reflects the local X coordinate before rotation.
	 */
	public FacedVec3d(Vec3d south)
	{
		cache(south, facings);
		cache(new Vec3d(1-south.x, south.y, south.z), mirroredFacings);
	}

	private void cache(Vec3d vector, Vec3d[] target)
	{
		target[EnumFacing.SOUTH.ordinal()] = vector;
		target[EnumFacing.NORTH.ordinal()] = new Vec3d(1-vector.x, vector.y, 1-vector.z);
		target[EnumFacing.WEST.ordinal()] = new Vec3d(1-vector.z, vector.y, vector.x);
		target[EnumFacing.EAST.ordinal()] = new Vec3d(vector.z, vector.y, 1-vector.x);
		target[EnumFacing.UP.ordinal()] = new Vec3d(vector.x, vector.z, 1-vector.y);
		target[EnumFacing.DOWN.ordinal()] = new Vec3d(vector.x, 1-vector.z, vector.y);
	}

	public Vec3d getFacing(EnumFacing facing, boolean mirrored)
	{
		return (mirrored?mirroredFacings: facings)[facing.ordinal()];
	}
}
