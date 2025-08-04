package pl.pabilo8.immersiveintelligence.client.fx;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 29.12.2024
 * @since 03.04.2024
 */
public class ScreenShake implements Comparable<ScreenShake>
{
	/**
	 * The strength of the screen shake, used as a multiplier for gaussian noise.
	 */
	private final double strength;
	@SuppressWarnings("unused")
	private Vec3d position;
	/**
	 * The duration of the screen shake in ticks.
	 */
	private double duration;
	/**
	 * The delay of the screen shake in ticks.
	 */
	private double delay;

	/**
	 * Creates a new screen shake effect.
	 *
	 * @param strength the strength of the screen shake
	 * @param duration the duration of the screen shake in ticks
	 */
	public ScreenShake(float strength, float duration, Vec3d pos)
	{
		this(strength, duration, 0, pos);
	}

	/**
	 * Creates a new screen shake effect.
	 *
	 * @param strength the strength of the screen shake
	 * @param duration the duration of the screen shake in ticks
	 * @param delay    the delay of the screen shake in ticks
	 */
	public ScreenShake(float strength, float duration, float delay, Vec3d pos)
	{
		this.strength = strength*MathHelper.clamp(MathHelper.log2((int)pos.distanceTo(ClientUtils.mc().player.getPositionVector())*2), 0, 1.05);
		this.duration = duration;
		this.delay = delay;
	}

	/**
	 * Updates the screen shake, reducing the duration by the given amount.
	 *
	 * @param partialTicks current render ticks
	 * @return true if the effect is finished and should be removed
	 */
	public boolean tick(double partialTicks)
	{
		if(delay > 0)
		{
			delay -= partialTicks;
			return false;
		}

		duration -= partialTicks;
		return duration <= 0;
	}

	@Override
	public int compareTo(ScreenShake o)
	{
		if((int)delay==(int)o.delay)
			return Double.compare(o.strength, strength); //Pick the one with higher strength
		return Double.compare(delay, o.delay); //Pick the one with lower delay
	}

	/**
	 * @return the strength of the screen shake
	 */
	public double getStrength()
	{
		return delay > 0?0: strength;
	}
}
