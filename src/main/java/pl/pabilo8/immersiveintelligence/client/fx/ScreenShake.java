package pl.pabilo8.immersiveintelligence.client.fx;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 03.04.2024
 */
public class ScreenShake implements Comparable<ScreenShake>
{
	@SuppressWarnings("unused")
	private Vec3d position;
	/**
	 * The strength of the screen shake, used as a multiplier for gaussian noise.
	 */
	private final double strength;
	/**
	 * The duration of the screen shake in ticks.
	 */
	private double duration;

	public ScreenShake(float strength, float duration, Vec3d pos)
	{
		this.strength = strength*MathHelper.clamp(MathHelper.log2((int)pos.distanceTo(ClientUtils.mc().player.getPositionVector())*2), 0, 1.05);
		this.duration = duration;
	}

	/**
	 * Updates the screen shake, reducing the duration by the given amount.
	 *
	 * @param partialTicks current render ticks
	 * @return true if the effect is finished and should be removed
	 */
	public boolean tick(double partialTicks)
	{
		duration -= partialTicks;
		return duration <= 0;
	}

	@Override
	public int compareTo(ScreenShake o)
	{
		return Double.compare(o.strength, strength);
	}

	/**
	 * @return the strength of the screen shake
	 */
	public double getStrength()
	{
		return strength;
	}
}
