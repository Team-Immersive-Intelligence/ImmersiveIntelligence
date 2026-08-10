package pl.pabilo8.immersiveintelligence.common.util.multiblock;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests static and dynamic POI lookup in the II multiblock tile base.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.08.2026
 */
class TileEntityMultiblockIIBaseTest
{
	@Test
	void testDynamicPOIsRemainLiveAndRespectHierarchy()
	{
		TestMultiblock multiblock = new TestMultiblock();
		multiblock.definePOI(MultiblockPOI.DATA, 2);
		TestTile tile = new TestTile(multiblock);
		tile.dynamicOutputPosition = 7;

		assertArrayEquals(new int[]{2, 7}, tile.getPOI(MultiblockPOI.DATA));
		assertArrayEquals(new int[]{2, 7}, tile.getPOI(MultiblockPOI.DATA_OUTPUT));
		assertArrayEquals(new int[]{2}, tile.getPOI(MultiblockPOI.DATA_INPUT));

		tile.dynamicOutputPosition = 9;
		assertArrayEquals(new int[]{2, 9}, tile.getPOI(MultiblockPOI.DATA_OUTPUT));
	}

	@Test
	void testIsPOIUsesResolvedParentPoints()
	{
		TestMultiblock multiblock = new TestMultiblock();
		multiblock.definePOI(MultiblockPOI.DATA, 5);

		TestTile tile = new TestTile(multiblock).at(5);

		assertTrue(tile.isPOI(MultiblockPOI.DATA));
		assertTrue(tile.isPOI(MultiblockPOI.DATA_INPUT));
		assertTrue(tile.isPOI(MultiblockPOI.DATA_OUTPUT));
	}

	private static class TestMultiblock extends MultiblockStuctureBase<TestTile>
	{
		private TestMultiblock()
		{
			super(new ResourceLocation("immersiveintelligence", "tile_poi_test"));
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
		private int dynamicOutputPosition = -1;

		private TestTile(MultiblockStuctureBase<TestTile> multiblock)
		{
			super(multiblock);
		}

		private TestTile at(int position)
		{
			this.pos = position;
			return this;
		}

		@Override
		protected int[] listDynamicPOI(MultiblockPOI poi)
		{
			return poi==MultiblockPOI.DATA_OUTPUT&&dynamicOutputPosition >= 0?
					new int[]{dynamicOutputPosition}: new int[0];
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
