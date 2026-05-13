package pl.pabilo8.immersiveintelligence.client.util.carversound;

import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.util.sound.AdvancedSounds.MultiSound;

/**
 * A repeated sound with a beginning and end which plays for a set amount of ticks
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 20.09.2022
 */
@SideOnly(Side.CLIENT)
public class TimedCompoundSound extends CompoundSound
{
	private final int duration;
	private int timePlaying = 0;

	public TimedCompoundSound(MultiSound multiSound, SoundCategory category, Vec3d pos, int duration, float volume, float pitch)
	{
		super(multiSound, category, pos, volume, pitch);
		this.duration = duration;

	}

	@Override
	public boolean isDonePlaying()
	{
		return timePlaying > duration;
	}

	public void readjustTime(int timePLaying)
	{
		this.timePlaying = timePLaying;
	}

	@Override
	public void update()
	{
		if(timePlaying==0)
			playBeginSound();
		else if(timePlaying==duration-3)
			playEndSound();
		timePlaying++;

	}
}
