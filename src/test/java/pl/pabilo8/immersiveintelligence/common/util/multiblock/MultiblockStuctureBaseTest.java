package pl.pabilo8.immersiveintelligence.common.util.multiblock;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests typed POI storage and hierarchy resolution in multiblock definitions.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.08.2026
 */
class MultiblockStuctureBaseTest
{
	@Test
	void testParentPOIAppliesToChildren()
	{
		TestMultiblock multiblock = new TestMultiblock();
		multiblock.definePOI(MultiblockPOI.DATA, 6);

		assertArrayEquals(new int[]{6}, multiblock.getPointsOfInterest(MultiblockPOI.DATA));
		assertArrayEquals(new int[]{6}, multiblock.getPointsOfInterest(MultiblockPOI.DATA_INPUT));
		assertArrayEquals(new int[]{6}, multiblock.getPointsOfInterest(MultiblockPOI.DATA_OUTPUT));
	}

	@Test
	void testChildPOIAppliesToParentButNotSibling()
	{
		TestMultiblock multiblock = new TestMultiblock();
		multiblock.definePOI(MultiblockPOI.DATA_INPUT, 3);

		assertArrayEquals(new int[]{3}, multiblock.getPointsOfInterest(MultiblockPOI.DATA));
		assertArrayEquals(new int[]{3}, multiblock.getPointsOfInterest(MultiblockPOI.DATA_INPUT));
		assertArrayEquals(new int[0], multiblock.getPointsOfInterest(MultiblockPOI.DATA_OUTPUT));
	}

	@Test
	void testResolvedPOIsAreSortedAndDistinct()
	{
		TestMultiblock multiblock = new TestMultiblock();
		multiblock.definePOI(MultiblockPOI.DATA, 8, 2, 8);
		multiblock.definePOI(MultiblockPOI.DATA_INPUT, 5, 2);

		assertArrayEquals(new int[]{2, 5, 8}, multiblock.getPointsOfInterest(MultiblockPOI.DATA));
		assertArrayEquals(new int[]{2, 5, 8}, multiblock.getPointsOfInterest(MultiblockPOI.DATA_INPUT));
		assertArrayEquals(new int[]{2, 8}, multiblock.getPointsOfInterest(MultiblockPOI.DATA_OUTPUT));
	}

	@Test
	void testPOIHierarchyMatchesOnlyDirectBranches()
	{
		assertTrue(MultiblockPOI.DATA.matches(MultiblockPOI.DATA_INPUT));
		assertTrue(MultiblockPOI.DATA_INPUT.matches(MultiblockPOI.DATA));
		assertFalse(MultiblockPOI.DATA_INPUT.matches(MultiblockPOI.DATA_OUTPUT));

		assertTrue(MultiblockPOI.POWER.matches(MultiblockPOI.ENERGY_INPUT));
		assertFalse(MultiblockPOI.ENERGY_INPUT.matches(MultiblockPOI.ROTARY_INPUT));
	}

	private static class TestMultiblock extends MultiblockStuctureBase<TestTile>
	{
		private TestMultiblock()
		{
			super(new ResourceLocation("immersiveintelligence", "poi_test"));
		}

		private void definePOI(MultiblockPOI poi, int... positions)
		{
			addPOIPositions(poi, positions);
		}

		@Override
		protected BlockIIMultiblock<?> getBlock()
		{
			return null;
		}

		@Override
		protected int getMeta()
		{
			return 0;
		}

		@Override
		protected TestTile getMBInstance()
		{
			return new TestTile(this);
		}
	}

	private static class TestTile extends TileEntityMultiblockIIBase<TestTile>
	{
		private TestTile(MultiblockStuctureBase<TestTile> multiblock)
		{
			super(multiblock);
		}

		@Override
		protected void dummyCleanup()
		{

		}

		@Override
		protected void onUpdate()
		{

		}

		@Override
		public boolean isStackValid(int slot, ItemStack stack)
		{
			return false;
		}

		@Override
		public int getSlotLimit(int slot)
		{
			return 0;
		}

		@Override
		public void doGraphicalUpdates(int slot)
		{

		}
	}
}
