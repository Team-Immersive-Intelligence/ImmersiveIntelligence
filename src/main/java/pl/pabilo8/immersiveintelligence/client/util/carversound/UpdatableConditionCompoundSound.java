package pl.pabilo8.immersiveintelligence.client.util.carversound;

import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.MultiSound;

import java.util.function.Function;

/**
 * A repeated sound with a beginning and end which plays while a condition is met
 *
 * @author Pabilo8
 * @since 20.09.2022
 */
@SideOnly(Side.CLIENT)
public class UpdatableConditionCompoundSound<T> extends CompoundSound
{
	private boolean forceStop = false, initialTick = true;
	private final T controller;
	private final Function<T, Boolean> shouldPlay;

	public UpdatableConditionCompoundSound(MultiSound multiSound, SoundCategory category, Vec3d pos, float volume, float pitch, T controller, Function<T, Boolean> shouldPlay)
	{
		super(multiSound, category, pos, volume, pitch);
		this.controller = controller;
		this.shouldPlay = shouldPlay;
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
			playBeginSound();
			initialTick = false;
		}
		else if(forceStop||(!should&&repeat))
			playEndSound();

		repeat = should;
	}

	public void finishPlaying()
	{
		this.forceStop = true;
	}
}
