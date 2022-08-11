package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.util.SoundEvent;
import net.minecraft.util.Tuple;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * A class for storing sounds to be played during an animation
 *
 * @author Pabilo8
 * @since 10.08.2022
 */
// TODO: 11.08.2022 add loading from .json exported from blockbench
public class IISoundAnimation
{
	private final HashMap<Double, SoundEvent> sounds = new HashMap<>();
	private final HashMap<Integer, SoundEvent[]> compiledSounds = new HashMap<>();
	private final double animationDuration;

	// TODO: 11.08.2022 dispose of animationDuration when adding .json
	public IISoundAnimation(double animationDuration)
	{
		this.animationDuration = animationDuration;
	}

	/**
	 * Compiles the 0-1 double values to integer timeframes based on animation duration
	 *
	 * @param maxTime animation duration
	 */
	public void compile(int maxTime)
	{
		compiledSounds.clear();
		sounds.entrySet()
				.stream()
				.map(e -> new Tuple<>((int)(e.getKey()*maxTime), e.getValue()))
				.collect(Collectors.groupingBy(Tuple::getFirst))
				.forEach((time, sound) -> {
					SoundEvent[] sounds = sound.stream().map(Tuple::getSecond).toArray(SoundEvent[]::new);
					compiledSounds.put(time, sounds);
				});

	}

	/**
	 * Puts a sound at a given keyframe
	 *
	 * @return this
	 */
	public IISoundAnimation withSound(double time, SoundEvent sound)
	{
		sounds.put(time/animationDuration, sound);
		return this;
	}

	/**
	 * Gets sound(s) for time, requires compilation through {@link #compile(int)} first.
	 *
	 * @return the sounds for given time
	 */
	@Nullable
	public SoundEvent[] getSounds(int time)
	{
		return compiledSounds.get(time);
	}

}
