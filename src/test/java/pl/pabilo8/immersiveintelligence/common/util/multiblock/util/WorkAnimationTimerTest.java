package pl.pabilo8.immersiveintelligence.common.util.multiblock.util;

import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests work animation ramps, loop timing and saved phase.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 15.10.2026
 */
class WorkAnimationTimerTest
{
	@Test
	void derivesRampDurationFromLoopTicksAndSteps()
	{
		WorkAnimationTimer timer = new WorkAnimationTimer(6, 15);
		assertTrue(timer.isFullyClosed());
		timer.setState(true);
		advance(timer, 45);
		assertEquals(0.5f, timer.getProgress(0));
		advance(timer, 44);
		assertFalse(timer.isFullyOpened());
		timer.update();
		assertTrue(timer.isFullyOpened());
		timer.setState(false);
		advance(timer, 90);
		assertTrue(timer.isFullyClosed());
	}

	@Test
	void acceleratesSmoothlyIntoSixTickWorkLoops()
	{
		WorkAnimationTimer timer = new WorkAnimationTimer(6, 15);
		timer.setState(true);
		assertPhase(0, timer.getAnimationProgress(0));
		advance(timer, 45);
		assertPhase(1.875, timer.getAnimationProgress(0));
		assertPhase(45.5*45.5/(2*90*6), timer.getAnimationProgress(0.5f));
		advance(timer, 45);
		assertPhase(7.5, timer.getAnimationProgress(0));
		assertPhase(7.5+0.5/6, timer.getAnimationProgress(0.5f));
		advance(timer, 6);
		assertPhase(8.5, timer.getAnimationProgress(0));
	}

	@Test
	void deceleratesAndRetainsItsStoppedPosition()
	{
		WorkAnimationTimer timer = runningTimer();
		timer.setState(false);
		advance(timer, 45);
		assertPhase(7.5+5.625, timer.getAnimationProgress(0));
		assertPhase(7.5+5.625+(0.5*0.5-0.5*0.5/(2*90))/6, timer.getAnimationProgress(0.5f));
		advance(timer, 45);
		float stopped = timer.getAnimationProgress(0);
		advance(timer, 100);
		assertTrue(timer.isFullyClosed());
		assertEquals(stopped, timer.getAnimationProgress(0.75f));
	}

	@Test
	void preservesPhaseWhenReversingTheRamp()
	{
		WorkAnimationTimer timer = new WorkAnimationTimer(6, 15);
		timer.setState(true);
		advance(timer, 45);
		float phase = timer.getAnimationProgress(0);
		timer.setState(false);
		assertEquals(phase, timer.getAnimationProgress(0));
		advance(timer, 15);
		assertPhase(1.875+6.25/6, timer.getAnimationProgress(0));
		timer.setState(true);
		assertPhase(1.875+6.25/6, timer.getAnimationProgress(0));
		advance(timer, 30);
		assertPhase(1.875+6.25/6+15.0/6, timer.getAnimationProgress(0));
	}

	@Test
	void integratesFractionalRampEndpoints()
	{
		WorkAnimationTimer timer = new WorkAnimationTimer(1.25, 3);
		timer.setState(true);
		advance(timer, 3);
		assertPhase(0.96, timer.getAnimationProgress(0));
		assertPhase(1.5, timer.getAnimationProgress(0.75f));
		assertPhase(1.7, timer.getAnimationProgress(1));
		timer.update();
		assertTrue(timer.isFullyOpened());
		assertPhase(1.7, timer.getAnimationProgress(0));
		timer.setState(false);
		advance(timer, 3);
		assertPhase(1.7+1.44, timer.getAnimationProgress(0));
		assertPhase(3.2, timer.getAnimationProgress(1));
		timer.update();
		assertTrue(timer.isFullyClosed());
		assertPhase(3.2, timer.getAnimationProgress(0.75f));
	}

	@Test
	void renderingDoesNotAdvanceOrChangeSavedState()
	{
		WorkAnimationTimer rendered = new WorkAnimationTimer(6, 15);
		WorkAnimationTimer hidden = new WorkAnimationTimer(6, 15);
		rendered.setState(true);
		hidden.setState(true);
		for(int tick = 0; tick < 100; tick++)
		{
			NBTTagCompound before = rendered.serializeNBT();
			for(float partial : new float[]{0, 0.25f, 0.5f, 0.75f, 1, 0.25f})
				rendered.getAnimationProgress(partial);
			assertEquals(before, rendered.serializeNBT());
			rendered.update();
			hidden.update();
		}
		assertEquals(hidden.serializeNBT(), rendered.serializeNBT());
	}

	@Test
	void keepsSeparateTimersIndependent()
	{
		WorkAnimationTimer first = runningTimer();
		WorkAnimationTimer second = new WorkAnimationTimer(6, 15);
		float firstPhase = first.getAnimationProgress(0);
		second.setState(true);
		advance(second, 45);
		assertEquals(firstPhase, first.getAnimationProgress(0));
		assertEquals(1, first.getProgress(0));
		assertEquals(0.5f, second.getProgress(0));
		assertPhase(1.875, second.getAnimationProgress(0));
	}

	@Test
	void restoresRampAndPhaseFromItsOwnNbt()
	{
		WorkAnimationTimer original = new WorkAnimationTimer(6, 15);
		original.setState(true);
		advance(original, 45);
		original.setState(false);
		advance(original, 7);
		WorkAnimationTimer loaded = new WorkAnimationTimer(6, 15);
		loaded.deserializeNBT(original.serializeNBT());
		assertFalse(loaded.getState());
		assertEquals(original.getProgress(0.5f), loaded.getProgress(0.5f));
		assertEquals(original.getAnimationProgress(0.5f), loaded.getAnimationProgress(0.5f));
		for(int tick = 0; tick < 100; tick++)
		{
			original.update();
			loaded.update();
		}
		assertEquals(original.serializeNBT(), loaded.serializeNBT());
	}

	@Test
	void readsExistingPartStateWithoutASavedPhase()
	{
		MultiblockInteractablePart part = new MultiblockInteractablePart(90);
		part.setState(true);
		for(int tick = 0; tick < 45; tick++)
			part.update();
		WorkAnimationTimer timer = runningTimer();
		timer.deserializeNBT(part.serializeNBT());
		assertTrue(timer.getState());
		assertEquals(0.5f, timer.getProgress(0));
		assertPhase(0, timer.getAnimationProgress(0));
		assertPhase((0.5*0.5+0.5*0.5/(2*90))/6, timer.getAnimationProgress(0.5f));
	}

	@Test
	void rejectsInvalidLoopAndRampDurations()
	{
		for(double loopTicks : new double[]{0, -1, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.MIN_VALUE, Double.MAX_VALUE})
			assertThrows(IllegalArgumentException.class, () -> new WorkAnimationTimer(loopTicks, 15));
		assertThrows(IllegalArgumentException.class, () -> new WorkAnimationTimer(6, 0));
		assertThrows(IllegalArgumentException.class, () -> new WorkAnimationTimer(6, -1));
	}

	private WorkAnimationTimer runningTimer()
	{
		WorkAnimationTimer timer = new WorkAnimationTimer(6, 15);
		timer.setState(true);
		advance(timer, 90);
		return timer;
	}

	private void advance(WorkAnimationTimer timer, int ticks)
	{
		for(int tick = 0; tick < ticks; tick++)
			timer.update();
	}

	private void assertPhase(double expectedCycles, float actual)
	{
		assertEquals(expectedCycles%1, actual, 0.000001);
	}
}
