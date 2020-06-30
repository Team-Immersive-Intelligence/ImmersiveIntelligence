package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import pl.pabilo8.immersiveintelligence.common.entity.EntitySkyCrate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 08-06-2019
 */
public class SkyCrateSound implements ITickableSound
{
	private final EntitySkyCrate hook;
	private final ResourceLocation soundLoc;
	private Sound sound;
	private float speed = .01F;


	public SkyCrateSound(EntitySkyCrate hook, ResourceLocation soundLoc)
	{
		this.hook = hook;
		this.soundLoc = soundLoc;
	}

	@Override
	public boolean isDonePlaying()
	{
		return hook.isDead;
	}

	@Nonnull
	@Override
	public ResourceLocation getSoundLocation()
	{
		return soundLoc;
	}

	@Nullable
	@Override
	public SoundEventAccessor createAccessor(@Nonnull SoundHandler handler)
	{
		SoundEventAccessor soundEvent = handler.getAccessor(this.soundLoc);
		if(soundEvent==null)
			this.sound = SoundHandler.MISSING_SOUND;
		else
			this.sound = soundEvent.cloneEntry();
		return soundEvent;
	}

	@Nonnull
	@Override
	public Sound getSound()
	{
		return sound;
	}

	@Nonnull
	@Override
	public SoundCategory getCategory()
	{
		return SoundCategory.NEUTRAL;
	}

	@Override
	public boolean canRepeat()
	{
		return true;
	}

	@Override
	public int getRepeatDelay()
	{
		return 0;
	}

	@Override
	public float getVolume()
	{
		return Math.min(speed, .75F);
	}

	@Override
	public float getPitch()
	{
		return Math.min(.5F*speed, .75F);
	}

	@Override
	public float getXPosF()
	{
		return (float)hook.posX;
	}

	@Override
	public float getYPosF()
	{
		return (float)hook.posY;
	}

	@Override
	public float getZPosF()
	{
		return (float)hook.posZ;
	}

	@Nonnull
	@Override
	public AttenuationType getAttenuationType()
	{
		return AttenuationType.LINEAR;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		speed = (float)hook.getSpeed();
		if(speed < .01)
			speed = .01F;
	}
}