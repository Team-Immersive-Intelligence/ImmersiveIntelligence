package pl.pabilo8.immersiveintelligence.client.util.carversound;

import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.MultiSound;

import java.util.function.Supplier;

/**
 * A repeated sound with a beginning and end which plays while a condition is met
 *
 * @author Pabilo8
 * @since 20.09.2022
 */
@SideOnly(Side.CLIENT)
public class ConditionCompoundSound extends CompoundSound
{
	private boolean forceStop = false, initialTick = true;
	private final Supplier<Boolean> shouldPlay;

	public ConditionCompoundSound(MultiSound multiSound, SoundCategory category, Vec3d pos, float volume, float pitch, Supplier<Boolean> shouldPlay)
	{
		super(multiSound, category, pos, volume, pitch);
		this.shouldPlay = shouldPlay;
	}

	@Override
	public boolean isDonePlaying()
	{
		if(forceStop||!shouldPlay.get())
		{
			playEndSound();
			return true;
		}
		return false;
	}

	@Override
	public void update()
	{
		if(initialTick)
		{
			playBeginSound();
			initialTick = false;
		}
	}

	public void forceStop()
	{
		this.forceStop = true;
	}
}
