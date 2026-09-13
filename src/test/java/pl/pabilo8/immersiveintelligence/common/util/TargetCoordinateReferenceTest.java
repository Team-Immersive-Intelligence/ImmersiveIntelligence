package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;
import pl.pabilo8.immersiveintelligence.test.GameTestWorld;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies the lifetime rules shared by Fire Missions and autonomous targets.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.09.2026
 */
class TargetCoordinateReferenceTest extends GameTestWorld
{
	@Test
	void rejectsLivingTargetAsSoonAsItsHealthReachesZero()
	{
		World world = testManager.getWorld(0);
		EntitySheep target = new EntitySheep(world);
		world.spawnEntity(target);
		TargetCoordinateReference reference = new TargetCoordinateReference(() -> world).withEntity(target);

		assertTrue(reference.shouldBeExecuted(world));
		target.setHealth(0f);
		assertFalse(target.isDead);
		assertFalse(target.isEntityAlive());
		assertFalse(reference.shouldBeExecuted(world));
	}

	@Test
	void aimingOnlyPositionRemainsValidInAirAndRoundTrips()
	{
		World world = testManager.getWorld(0);
		BlockPos target = new BlockPos(8, 12, -4);
		TargetCoordinateReference reference = new TargetCoordinateReference(() -> world)
				.withPosition(target)
				.withAimingOnly(true);

		assertTrue(world.isAirBlock(target));
		assertTrue(reference.shouldBeExecuted(world));
		assertTrue(reference.isAimingOnly());
		assertFalse(reference.hasFiniteShots());

		NBTTagCompound serialized = reference.serializeNBT();
		TargetCoordinateReference copy = new TargetCoordinateReference(() -> world);
		copy.deserializeNBT(serialized);
		assertEquals(target, copy.getPosition());
		assertTrue(copy.isAimingOnly());
		assertTrue(copy.shouldBeExecuted(world));
		assertFalse(copy.hasFiniteShots());
	}

	@Test
	void fireModeOverrideRoundTripsAndFallsBackToTheDefault()
	{
		TargetCoordinateReference reference = new TargetCoordinateReference(() -> null)
				.withPosition(BlockPos.ORIGIN);
		assertFalse(reference.hasFireModeOverride());
		assertTrue(reference.isBallisticFire(true));

		reference.withBallisticFire(false);
		TargetCoordinateReference copy = new TargetCoordinateReference(() -> null);
		copy.deserializeNBT(reference.serializeNBT());
		assertTrue(copy.hasFireModeOverride());
		assertFalse(copy.isBallisticFire(true));

		copy.withDefaultFireMode();
		assertFalse(copy.hasFireModeOverride());
		assertTrue(copy.isBallisticFire(true));
	}
}
