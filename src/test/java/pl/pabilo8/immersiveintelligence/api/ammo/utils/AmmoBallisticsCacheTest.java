package pl.pabilo8.immersiveintelligence.api.ammo.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoBallistics.FlightState;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoBallisticsCache.BallisticSolution;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoBallisticsCache.CachedBallisticStats;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies shared trajectory calculation independently of Minecraft entities.
 */
class AmmoBallisticsCacheTest
{
	@BeforeEach
	void clearCache()
	{
		AmmoBallisticsCache.clear();
	}

	@Test
	void reusesEquivalentCustomModels()
	{
		AmmoBallistics first = AmmoBallistics.custom("straight", 1D, 40, FlightState::move);
		AmmoBallistics second = AmmoBallistics.custom("straight", 1D, 40, FlightState::move);

		assertSame(AmmoBallisticsCache.get(first), AmmoBallisticsCache.get(second));
	}

	@Test
	void resolvesAndCachesDirectAndArtilleryArcs()
	{
		CachedBallisticStats stats = AmmoBallisticsCache.get(
				AmmoBallistics.projectile("test_projectile", 2D, 0.1D, 1D, 100)
		);
		BallisticSolution direct = stats.getDirectSolution(10D, 0D);
		BallisticSolution artillery = stats.getArtillerySolution(10D, 0D);

		assertTrue(direct.isValid());
		assertTrue(artillery.isValid());
		assertTrue(direct.getElevation() < artillery.getElevation());
		assertTrue(direct.getImpactTime() < artillery.getImpactTime());
		assertSame(direct, stats.getDirectSolution(10D, 0D));
		assertSame(artillery, stats.getArtillerySolution(10D, 0D));
	}

	@Test
	void derivesRangeHeightVelocityAndImpactTime()
	{
		CachedBallisticStats stats = AmmoBallisticsCache.get(
				AmmoBallistics.projectile("test_stats", 2D, 0.1D, 1D, 100)
		);

		assertEquals(2D, stats.getVelocity(), 1e-6D);
		assertEquals(8D, stats.getMaxDirectRange(), 0.01D);
		assertTrue(stats.getMaxArtilleryRange() > stats.getMaxDirectRange());
		assertTrue(stats.getMaxHeightReached() > 10D);
		assertTrue(stats.getDirectImpactTime(10D, 0D) > 0);
	}

	@Test
	void boosterTimeChangesTheCachedTrajectory()
	{
		CachedBallisticStats unboosted = AmmoBallisticsCache.get(
				AmmoBallistics.projectile("rocket", 2D, 0.1D, 0.99D, 0, 100)
		);
		CachedBallisticStats boosted = AmmoBallisticsCache.get(
				AmmoBallistics.projectile("rocket", 2D, 0.1D, 0.99D, 5, 100)
		);

		assertNotSame(unboosted, boosted);
		assertTrue(boosted.getMaxDirectRange() > unboosted.getMaxDirectRange());
	}

	@Test
	void supportsMoveFirstNonAmmoModels()
	{
		CachedBallisticStats stats = AmmoBallisticsCache.get(
				AmmoBallistics.dragAfterMove("particle", 2D, 0.1D, 1D, 100)
		);

		assertEquals(10D, stats.getMaxDirectRange(), 0.01D);
		assertTrue(stats.getDirectSolution(10D, 0D).isValid());
	}

	@Test
	void velocityIsPartOfTheCacheKey()
	{
		CachedBallisticStats normal = AmmoBallisticsCache.get(
				AmmoBallistics.projectile("modifier", 1D, 0.1D, 0.99D, 100)
		);
		CachedBallisticStats faster = AmmoBallisticsCache.get(
				AmmoBallistics.projectile("modifier", 1.5D, 0.1D, 0.99D, 100)
		);

		assertNotSame(normal, faster);
		assertEquals(1.5D, faster.getVelocity(), 1e-6D);
	}
}
