package pl.pabilo8.immersiveintelligence.common.util.multiblock.util;

import net.minecraft.util.math.MathHelper;

/**
 * Represents an animated part of the multiblock, like a drawer
 */
public class MultiblockInteractablePart
{
	boolean opened = false;
	float progress = 0;
	final float maxProgress;

	public MultiblockInteractablePart(float maxProgress)
	{
		this.maxProgress = maxProgress;
	}

	public float getProgress(float partialTicks)
	{
		return MathHelper.clamp(progress+(opened?partialTicks: -partialTicks), 0, maxProgress)/maxProgress;
	}

	public void update()
	{
		this.progress = MathHelper.clamp(progress+(opened?1: -1), 0, maxProgress);
	}

	public void setState(boolean state)
	{
		this.opened = state;
	}
}
