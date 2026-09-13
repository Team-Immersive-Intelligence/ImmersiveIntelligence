package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Verifies lazy terrain line-of-sight caching.
 */
class TerrainVisibilityMatrixTest
{
	@Test
	void cachesStraightLineVisibilityUntilTheNextUpdate()
	{
		World world = mock(World.class);
		IBlockState air = mock(IBlockState.class);
		IBlockState opaque = mock(IBlockState.class);
		Set<BlockPos> blockers = new HashSet<>();
		when(world.isBlockLoaded(any(BlockPos.class))).thenReturn(true);
		when(world.getBlockState(any(BlockPos.class))).thenAnswer(invocation ->
				blockers.contains(invocation.getArgument(0))?opaque: air);
		when(air.isOpaqueCube()).thenReturn(false);
		when(opaque.isOpaqueCube()).thenReturn(true);

		TerrainVisibilityMatrix matrix = new TerrainVisibilityMatrix();
		Vec3d origin = new Vec3d(0.5d, 0.5d, 0.5d);
		AxisAlignedBB bounds = new AxisAlignedBB(-8, -8, -8, 8, 8, 8);
		Vec3d target = new Vec3d(5.25d, 0.5d, 0.5d);
		matrix.update(world, origin, bounds);
		assertTrue(matrix.isVisible(target));

		blockers.add(new BlockPos(3, 0, 0));
		assertTrue(matrix.isVisible(target), "the current matrix generation must remain cached");
		matrix.update(world, origin, bounds);
		assertFalse(matrix.isVisible(target));
		assertTrue(matrix.isVisible(new Vec3d(2.25d, 0.5d, 0.5d)));
	}
}
