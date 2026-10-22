package pl.pabilo8.immersiveintelligence.common.util.multiblock.util;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Combines a multiblock part's startup ramp with a continuous work animation.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.2
 * @since 15.10.2026
 */
public class WorkAnimationTimer extends MultiblockInteractablePart
{
	private final double loopTicks;
	private double phase;

	/**
	 * @param loopTicks ticks per work loop at full speed
	 * @param rampSteps number of loop durations in the startup and shutdown ramp
	 */
	public WorkAnimationTimer(double loopTicks, int rampSteps)
	{
		super((float)(rampSteps*loopTicks));
		if(!Double.isFinite(loopTicks)||loopTicks <= 0||rampSteps <= 0||!Float.isFinite(maxProgress)||maxProgress <= 0)
			throw new IllegalArgumentException("Loop and ramp durations must be positive and finite.");
		this.loopTicks = loopTicks;
	}

	@Override
	public void update()
	{
		phase = (phase+getPhaseAdvance(1))%1;
		super.update();
	}

	/**
	 * Gets the work loop phase without advancing the timer.
	 *
	 * @param partialTicks fraction of the next tick, from 0 to 1
	 * @return interpolated work loop phase
	 */
	public float getAnimationProgress(float partialTicks)
	{
		return (float)((phase+getPhaseAdvance(partialTicks))%1);
	}

	private double getPhaseAdvance(float partialTicks)
	{
		double speed = progress/(double)maxProgress;
		double rampTime = Math.min(partialTicks, opened?maxProgress-progress: progress/(double)closingSpeed);
		double acceleration = (opened?1: -closingSpeed)/(double)maxProgress;
		//Integrate the ramp and any remaining time at its clamped endpoint.
		double distance = speed*rampTime+0.5*acceleration*rampTime*rampTime+(opened?partialTicks-rampTime: 0);
		return distance/loopTicks;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = super.serializeNBT();
		nbt.setDouble("phase", phase);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		super.deserializeNBT(nbt);
		phase = nbt.getDouble("phase");
	}
}
