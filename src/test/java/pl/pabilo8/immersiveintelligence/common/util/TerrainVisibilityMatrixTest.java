package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

	@Test
	void ignoredPositionsRemainTransparentAndFormPartOfTheConfiguration()
	{
		World world = mock(World.class);
		IBlockState opaque = mock(IBlockState.class);
		when(world.isBlockLoaded(any(BlockPos.class))).thenReturn(true);
		when(world.getBlockState(any(BlockPos.class))).thenReturn(opaque);
		when(opaque.isOpaqueCube()).thenReturn(true);

		TerrainVisibilityMatrix matrix = new TerrainVisibilityMatrix();
		Vec3d origin = new Vec3d(0.5d, 0.5d, 0.5d);
		AxisAlignedBB bounds = new AxisAlignedBB(-8, -8, -8, 8, 8, 8);
		BlockPos ignored = new BlockPos(1, 0, 0);
		matrix.update(world, origin, bounds, Collections.singleton(ignored));

		assertTrue(matrix.isVisible(new Vec3d(2.25d, 0.5d, 0.5d)));
		assertFalse(matrix.isVisible(new Vec3d(3.25d, 0.5d, 0.5d)),
				"opaque positions outside the ignore set must still block visibility");
		assertTrue(matrix.isConfiguredFor(world, origin, bounds, Collections.singleton(ignored)));
		assertFalse(matrix.isConfiguredFor(world, origin, bounds, Collections.emptySet()));
		verify(world, never()).getBlockState(ignored);
	}
}
