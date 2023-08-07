package pl.pabilo8.immersiveintelligence.client.util.carversound;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.MultiSound;

/**
 * A repeated sound with a beginning and end.
 *
 * @author Pabilo8
 * @since 19.09.2022
 */
@SideOnly(Side.CLIENT)
public abstract class CompoundSound extends PositionedSound implements ITickableSound
{
	private final SoundEvent soundBegin;
	private final SoundEvent soundEnd;

	public CompoundSound(MultiSound multiSound, SoundCategory category, Vec3d pos, float volume, float pitch)
	{
		super(multiSound.getSoundMid(), category);

		this.soundBegin = multiSound.getSoundBegin();
		this.soundEnd = multiSound.getSoundEnd();
		repeat = true;

		this.pitch = pitch;
		this.volume = volume;

		this.xPosF = (float)pos.x;
		this.yPosF = (float)pos.y;
		this.zPosF = (float)pos.z;

		this.repeatDelay = 0;
	}

	protected final void playBeginSound()
	{
		if(soundBegin!=null)
			ClientUtils.mc().getSoundHandler().playSound(new PositionedSoundRecord(soundBegin, category, volume*1.1f, pitch, xPosF, yPosF, zPosF));
	}

	protected final void playEndSound()
	{
		if(soundEnd!=null)
			ClientUtils.mc().getSoundHandler().playSound(new PositionedSoundRecord(soundEnd, category, volume*1.1f, pitch, xPosF, yPosF, zPosF));
	}

}
