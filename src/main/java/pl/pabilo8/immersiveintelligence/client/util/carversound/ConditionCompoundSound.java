package pl.pabilo8.immersiveintelligence.client.util.carversound;

import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.util.sound.AdvancedSounds.MultiSound;

import java.util.function.Function;

/**
 * A repeated sound with a beginning and end which plays while a condition is met
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 20.09.2022
 */
@SideOnly(Side.CLIENT)
public class ConditionCompoundSound<T> extends CompoundSound
{
	private final T controller;
	private final Function<T, Boolean> shouldPlay;
	private float playingVolume;
	private boolean forceStop = false, initialTick = true;

	public ConditionCompoundSound(MultiSound multiSound, SoundCategory category, Vec3d pos, float volume, float pitch, T controller, Function<T, Boolean> shouldPlay)
	{
		super(multiSound, category, pos, volume, pitch);
		this.playingVolume = volume;
		this.controller = controller;
		this.shouldPlay = shouldPlay;
	}

	public ConditionCompoundSound(MultiSound multiSound, Vec3d pos, T controller, Function<T, Boolean> shouldPlay)
	{
		this(multiSound, SoundCategory.BLOCKS, pos, 1f, 1f, controller, shouldPlay);
		start();
	}

	public ConditionCompoundSound(SoundEvent event, SoundCategory category, Vec3d pos, float volume, float pitch, T controller, Function<T, Boolean> shouldPlay)
	{
		super(event, category, pos, volume, pitch);
		this.playingVolume = volume;
		this.controller = controller;
		this.shouldPlay = shouldPlay;
	}

	public ConditionCompoundSound(SoundEvent multiSound, Vec3d pos, T controller, Function<T, Boolean> shouldPlay)
	{
		this(multiSound, SoundCategory.BLOCKS, pos, 1f, 1f, controller, shouldPlay);
		start();
	}

	@Override
	public boolean isDonePlaying()
	{
		//Safety check
		if(controller==null)
			forceStop = true;
		return forceStop;
	}

	@Override
	public void update()
	{
		boolean should = shouldPlay.apply(controller);

		if(initialTick)
		{
			if(!should)
			{
				setRepeat(false);
				return;
			}
			setRepeat(true);
			playBeginSound();
			initialTick = false;
		}
		else if(forceStop||(!should&&repeat))
		{
			initialTick = true;
			setRepeat(false);
			playEndSound();
		}
		else
			setRepeat(true);
	}

	public void forceStop()
	{
		this.forceStop = true;
	}

	public void setRepeat(boolean repeat)
	{
		this.repeat = repeat;
		setVolume(repeat?playingVolume: 0f);
	}
}
