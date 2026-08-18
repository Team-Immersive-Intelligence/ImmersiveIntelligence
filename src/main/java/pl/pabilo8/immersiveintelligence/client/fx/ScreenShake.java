package pl.pabilo8.immersiveintelligence.client.fx;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.util.math.Vec3d;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 29.12.2024
 * @since 03.04.2024
 */
public class ScreenShake implements Comparable<ScreenShake>
{
	private static final double REFERENCE_FOV_TANGENT = Math.tan(Math.toRadians(70d)*0.5d);
	private static final double TWO_PI = Math.PI*2d;

	private final double strength;
	private final double duration;
	private final double phase;
	private double age;

	/**
	 * Creates a screen shake without a delay.
	 *
	 * @param strength the maximum angular strength
	 * @param duration the duration in ticks
	 * @param position the source position
	 */
	public ScreenShake(float strength, float duration, Vec3d position)
	{
		this(strength, duration, 0, position);
	}

	/**
	 * Creates a screen shake.
	 *
	 * @param strength the maximum angular strength
	 * @param duration the duration in ticks
	 * @param delay    the delay in ticks
	 * @param position the source position
	 */
	public ScreenShake(float strength, float duration, float delay, Vec3d position)
	{
		double distance = ClientUtils.mc().player==null?0:
				position.distanceTo(ClientUtils.mc().player.getPositionVector());
		this.strength = Math.max(0, strength)/(1d+distance/32d)*8;
		this.duration = Math.max(1, duration);
		this.age = -Math.max(0, delay);
		this.phase = getPhase(position);
	}

	/**
	 * Advances the effect by one client tick.
	 *
	 * @return true when the effect is complete
	 */
	public boolean tick()
	{
		age++;
		return age >= duration;
	}

	/**
	 * Gets the current smooth camera rotation.
	 *
	 * @param partialTicks render interpolation value
	 * @param fieldOfView  active field of view in degrees
	 * @return yaw, pitch, and roll in the X, Y, and Z components
	 */
	public Vec3d getRotation(double partialTicks, float fieldOfView)
	{
		double time = age+partialTicks;
		double amplitude = strength*getEnvelope(time)*getFovScale(fieldOfView);
		if(amplitude <= 0)
			return Vec3d.ZERO;

		double yaw = getWave(time, 1.85d, phase)*amplitude*0.65d;
		double pitch = getWave(time, 2.15d, phase+TWO_PI/3d)*amplitude*0.5d;
		double roll = getWave(time, 1.55d, phase+TWO_PI*2d/3d)*amplitude*0.2d;
		return new Vec3d(yaw, pitch, roll);
	}

	/**
	 * Gets the current effective strength.
	 *
	 * @param partialTicks render interpolation value
	 * @return current strength before FOV scaling
	 */
	public double getStrength(double partialTicks)
	{
		return strength*getEnvelope(age+partialTicks);
	}

	/**
	 * Gets the strength at the current client tick.
	 *
	 * @return current strength before FOV scaling
	 */
	public double getStrength()
	{
		return getStrength(0);
	}

	@Override
	public int compareTo(ScreenShake other)
	{
		return Double.compare(getStrength(), other.getStrength());
	}

	private double getEnvelope(double time)
	{
		if(time < 0||time >= duration)
			return 0;

		double attackDuration = Math.min(1d, duration*0.25d);
		double attack = smoothStep(time/attackDuration);
		double decay = smoothStep(1d-time/duration);
		return attack*decay;
	}

	private static double getWave(double time, double frequency, double phase)
	{
		return (Math.sin(time*frequency+phase)+
				0.35d*Math.sin(time*frequency*2.17d+phase*0.63d))/1.35d;
	}

	private static double getFovScale(float fieldOfView)
	{
		double safeFov = Math.max(5d, Math.min(170d, fieldOfView));
		double scale = Math.tan(Math.toRadians(safeFov)*0.5d)/REFERENCE_FOV_TANGENT;
		return Math.max(0.1d, Math.min(2.5d, scale));
	}

	private static double smoothStep(double value)
	{
		value = Math.max(0, Math.min(1, value));
		return value*value*(3d-2d*value);
	}

	private static double getPhase(Vec3d position)
	{
		double value = position.x*0.754877666d+position.y*0.569840296d+position.z*0.438289d;
		return value-Math.floor(value/TWO_PI)*TWO_PI;
	}
}
