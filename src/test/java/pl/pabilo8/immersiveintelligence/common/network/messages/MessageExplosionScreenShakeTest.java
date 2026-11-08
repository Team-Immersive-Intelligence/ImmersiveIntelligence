package pl.pabilo8.immersiveintelligence.common.network.messages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks the finite range and distance scaling of explosion camera shake.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.10.2026
 */
class MessageExplosionScreenShakeTest
{
	@Test
	void smallEffectsDoNotShakeDistantCameras()
	{
		assertEquals(0f, MessageExplosion.getScreenShakeStrength(0.125f, 8f, 105d));
		assertEquals(0f, MessageExplosion.getScreenShakeStrength(0.125f, 8f, 8d));
		assertTrue(MessageExplosion.getScreenShakeStrength(0.125f, 8f, 2d) > 0);
	}

	@Test
	void largerEffectsReachFurtherAndFadeTowardsTheirBoundary()
	{
		float nearby = MessageExplosion.getScreenShakeStrength(3f, 128f, 32d);
		float distant = MessageExplosion.getScreenShakeStrength(3f, 128f, 105d);
		assertTrue(nearby > distant);
		assertTrue(distant > 0);
		assertEquals(0f, MessageExplosion.getScreenShakeStrength(3f, 128f, 128d));
	}

	@Test
	void effectsWithoutStrengthOrRangeDoNotShake()
	{
		assertEquals(0f, MessageExplosion.getScreenShakeStrength(0f, 8f, 0d));
		assertEquals(0f, MessageExplosion.getScreenShakeStrength(1f, 0f, 0d));
	}
}
