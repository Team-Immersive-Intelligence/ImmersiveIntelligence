package pl.pabilo8.immersiveintelligence.common.entity.tactile;

import com.google.gson.JsonParser;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimationCollisionMap;
import pl.pabilo8.immersiveintelligence.common.util.amt.TactileTransform;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.test.GameTestWorld;

import javax.annotation.Nullable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Covers tactile transforms, lookup fallbacks, and server-side entity spawning.
 */
class TactileManagerTest extends GameTestWorld
{
	@Test
	void resolvesPartsAndUsesNonNullFallbacks()
	{
		TactileManager manager = createManager(
				ResLoc.of(IIReference.RES_II, "aabb/multiblock/emplacement.json"), EnumFacing.NORTH, false);
		manager.defaultize();

		EntityAMTTactile platform = manager.getPart("platform");
		assertNotNull(platform);
		assertSame(platform, manager.getPart("platform"));
		assertEquals(platform.getPositionVector(), manager.getPosition("platform"));

		assertNull(manager.getPart("missing"));
		assertEquals(Vec3d.ZERO, manager.getPosition("missing"));
		Vec2f missingRotation = manager.getRotation("missing");
		assertEquals(0f, missingRotation.x);
		assertEquals(0f, missingRotation.y);
	}

	@Test
	void appliesAnAnimationTheFirstTimeItIsRequested()
	{
		TactileManager manager = createManager(
				ResLoc.of(IIReference.RES_II, "aabb/multiblock/emplacement.json"), EnumFacing.NORTH, false);
		manager.forceReload();
		manager.defaultize();
		double initialY = manager.getPosition("platform1").y;

		manager.update(ResLoc.of(IIReference.RES_II, "emplacement/open"), 0.61111f);

		assertNotEquals(initialY, manager.getPosition("platform1").y,
				"a newly loaded animation must be applied on its first update");
	}

	@Test
	void livingRootExposesPartsAndFactionOwnership()
	{
		World world = testManager.getWorld(0);
		TactileManager manager = createManager(
				ResLoc.of(IIReference.RES_II, "aabb/multiblock/emplacement.json"), EnumFacing.NORTH, false);
		manager.defaultize();
		EntitySheep ownerEntity = new EntitySheep(world);
		world.spawnEntity(ownerEntity);
		OwnerIdentity owner = new OwnerIdentity(ownerEntity);

		EntityTactileLivingBase root = manager.getOrCreateLivingEntity(owner, "entity.test.tactile");

		assertSame(owner, root.getOwnerIdentity());
		assertSame(owner, DiplomacyHandler.getInstance(false).getOwnerIdentityForEntity(root));
		assertArrayEquals(manager.getEntities().toArray(new EntityAMTTactile[0]), root.getParts());
		assertTrue(world.loadedEntityList.contains(root));
	}

	@Test
	void spawnsTactilesAtTheirCalculatedPositions()
	{
		TactileManager manager = createManager(
				ResLoc.of(IIReference.RES_II, "aabb/multiblock/emplacement.json"), EnumFacing.NORTH, false);
		manager.defaultize();

		assertFalse(manager.getEntities().isEmpty());
		manager.getEntities().forEach(entity -> {
			assertEquals(entity.posX, entity.prevPosX);
			assertEquals(entity.posY, entity.prevPosY);
			assertEquals(entity.posZ, entity.prevPosZ);
		});
	}

	@Test
	void directionTransformExplicitlyControlsHorizontalBoxSize()
	{
		ResLoc howitzer = ResLoc.of(IIReference.RES_II, "aabb/multiblock/artillery_howitzer.json");
		TactileManager south = createManager(howitzer, EnumFacing.SOUTH, false);
		south.defaultize();
		EntityAMTTactile southDoor = south.getPart("door_left_child0");
		assertNotNull(southDoor);

		TactileManager east = createManager(howitzer, EnumFacing.EAST, false);
		east.defaultize();
		EntityAMTTactile eastDoor = east.getPart("door_left_child0");
		assertNotNull(eastDoor);

		double southWidth = southDoor.aabb.maxX-southDoor.aabb.minX;
		double southDepth = southDoor.aabb.maxZ-southDoor.aabb.minZ;
		double eastWidth = eastDoor.aabb.maxX-eastDoor.aabb.minX;
		double eastDepth = eastDoor.aabb.maxZ-eastDoor.aabb.minZ;
		assertEquals(southWidth, eastDepth);
		assertEquals(southDepth, eastWidth);
		assertTrue(southDepth > southWidth, "south-facing hatch must not become a sideways stick");
	}

	@Test
	void animationTransformsRetainMirrorHandedness()
	{
		assertEquals(new Vec3d(-1, 0, 0), IIAnimationCollisionMap
				.transformTranslation(new Vec3d(0, 0, 1), EnumFacing.WEST, false));
		assertEquals(new Vec3d(1, 0, 0), IIAnimationCollisionMap
				.transformTranslation(new Vec3d(0, 0, 1), EnumFacing.EAST, false));
		Vec3d doorRotation = new Vec3d(0, 0, 185);
		assertEquals(185, IIAnimationCollisionMap
				.transformRotation(doorRotation, EnumFacing.SOUTH, false).z);
		assertEquals(-185, IIAnimationCollisionMap
				.transformRotation(doorRotation, EnumFacing.SOUTH, true).z);
	}

	@Test
	void resolvesGenericDirectionMirrorAndCombinedTransformSelectors()
	{
		TactileTransform transform = TactileTransform.resolve(new JsonParser().parse("{"
				+"\"all\":{\"offset\":[1,2,3]},"
				+"\"east\":{\"rotation\":[0,270,0],\"flip_xz_size\":true},"
				+"\"mirrored\":{\"offset\":[4,5,6],\"flip_xz_size\":false},"
				+"\"east_mirrored\":{\"flip_xz_size\":true}"
				+"}").getAsJsonObject(), EnumFacing.EAST, true);

		assertEquals(new Vec3d(4, 5, 6), transform.getOffset());
		assertEquals(new Vec3d(0, 270, 0), transform.getRotation());
		assertTrue(transform.isFlipXZSize());
	}

	private TactileManager createManager(ResLoc aabb, EnumFacing facing, boolean mirrored)
	{
		World world = testManager.getWorld(0);
		TestListener listener = new TestListener();
		TactileManager manager = new TactileManager(
				aabb,
				listener,
				() -> world,
				() -> BlockPos.ORIGIN,
				() -> facing,
				() -> mirrored
		);
		listener.manager = manager;
		return manager;
	}

	private static class TestListener implements TactileManager.ITactileListener
	{
		@Nullable
		private TactileManager manager;

		@Nullable
		@Override
		public TactileManager getTactileHandler()
		{
			return manager;
		}
	}
}
