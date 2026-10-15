package pl.pabilo8.immersiveintelligence.common.util.multiblock.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Tests cached vector orientation and mirroring around the block centre.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 15.10.2026
 */
class FacedVec3dTest
{
	@Test
	void orientsAllSixFacingsAndTheirMirrors()
	{
		FacedVec3d vector = new FacedVec3d(new Vec3d(2, 3, 4));
		assertFacing(vector, EnumFacing.SOUTH, new Vec3d(2, 3, 4), new Vec3d(-1, 3, 4));
		assertFacing(vector, EnumFacing.NORTH, new Vec3d(-1, 3, -3), new Vec3d(2, 3, -3));
		assertFacing(vector, EnumFacing.WEST, new Vec3d(-3, 3, 2), new Vec3d(-3, 3, -1));
		assertFacing(vector, EnumFacing.EAST, new Vec3d(4, 3, -1), new Vec3d(4, 3, 2));
		assertFacing(vector, EnumFacing.UP, new Vec3d(2, 4, -2), new Vec3d(-1, 4, -2));
		assertFacing(vector, EnumFacing.DOWN, new Vec3d(2, -3, 3), new Vec3d(-1, -3, 3));
	}

	@Test
	void preservesTheRotationPivotAndReturnsCachedInstances()
	{
		Vec3d centre = new Vec3d(0.5, 0.5, 0.5);
		FacedVec3d vector = new FacedVec3d(centre);
		Vec3d[] cached = new Vec3d[EnumFacing.VALUES.length*2];
		for(EnumFacing facing : EnumFacing.VALUES)
		{
			cached[facing.ordinal()*2] = vector.getFacing(facing, false);
			cached[facing.ordinal()*2+1] = vector.getFacing(facing, true);
		}
		for(EnumFacing facing : EnumFacing.VALUES)
			for(boolean mirrored : new boolean[]{false, true})
			{
				assertEquals(centre, vector.getFacing(facing, mirrored));
				assertSame(cached[facing.ordinal()*2+(mirrored?1: 0)], vector.getFacing(facing, mirrored));
			}
	}

	private void assertFacing(FacedVec3d vector, EnumFacing facing, Vec3d normal, Vec3d mirrored)
	{
		assertEquals(normal, vector.getFacing(facing, false));
		assertEquals(mirrored, vector.getFacing(facing, true));
	}
}
