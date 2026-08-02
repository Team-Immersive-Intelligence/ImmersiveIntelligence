package pl.pabilo8.immersiveintelligence.client.util.carversound;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.util.sound.AdvancedSounds.MultiSound;

/**
 * A repeated sound with a beginning and end.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 19.09.2022
 */
@SideOnly(Side.CLIENT)
public abstract class CompoundSound extends PositionedSound implements ITickableSound
{
	private final SoundEvent soundBegin;
	private final SoundEvent soundEnd;
	private float maxRange = 0;

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
		this.attenuationType = AttenuationType.LINEAR;

		this.repeatDelay = 0;
	}

	public CompoundSound(SoundEvent event, SoundCategory category, Vec3d pos, float volume, float pitch)
	{
		super(event, category);

		this.soundBegin = null;
		this.soundEnd = null;
		repeat = true;

		this.pitch = pitch;
		this.volume = volume;

		this.xPosF = (float)pos.x;
		this.yPosF = (float)pos.y;
		this.zPosF = (float)pos.z;
		this.attenuationType = AttenuationType.LINEAR;

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

	public void setPitch(float pitch)
	{
		this.pitch = pitch;
	}

	public void setVolume(float volume)
	{
		this.volume = volume;
	}

	public void setPosition(Vec3d position)
	{
		this.xPosF = (float)position.x;
		this.yPosF = (float)position.y;
		this.zPosF = (float)position.z;
	}

	public void setMaxRange(float maxRange)
	{
		this.maxRange = maxRange <= 0?0: maxRange;
		//When the range is undefined (0), attenuation is controlled by the sound system, otherwise it's calculated by the sound itself
		this.attenuationType = this.maxRange==0?AttenuationType.LINEAR: AttenuationType.NONE;
	}

	public float getMaxRange()
	{
		return maxRange;
	}

	public void start()
	{
		Minecraft.getMinecraft().getSoundHandler().playSound(this);
	}

	@Override
	public float getVolume()
	{
		if(this.maxRange==0)
			return super.getVolume();

		//Get camera entity
		Entity entity = ClientUtils.mc().getRenderViewEntity();
		if(entity==null)
			return super.getVolume();

		//Calculate inverse square law attenuation based on distance to the sound source
		float distance = (float)entity.getDistance(xPosF, yPosF, zPosF);
		float normalizedDistance = 1f-MathHelper.clamp(distance/maxRange, 0f, 1f);
		//Apply inverse square law
		return (float)(super.getVolume()*normalizedDistance);
	}

	@Override
	public float getXPosF()
	{
		if(maxRange!=0)
		{
			//Get camera entity
			Entity entity = ClientUtils.mc().getRenderViewEntity();
			if(entity==null)
				return super.getXPosF();
			//Clamp X to nearest
			return (float)(entity.posX+MathHelper.clamp(xPosF-(float)entity.posX, -1, 1));
		}
		return super.getXPosF();
	}

	@Override
	public float getYPosF()
	{
		if(maxRange!=0)
		{
			//Get camera entity
			Entity entity = ClientUtils.mc().getRenderViewEntity();
			if(entity==null)
				return super.getYPosF();
			//Clamp Y to nearest
			return (float)(entity.posY+MathHelper.clamp(yPosF-(float)entity.posY, -1, 1));
		}
		return super.getYPosF();
	}

	@Override
	public float getZPosF()
	{
		if(maxRange!=0)
		{
			//Get camera entity
			Entity entity = ClientUtils.mc().getRenderViewEntity();
			if(entity==null)
				return super.getZPosF();
			//Clamp Z to nearest
			return (float)(entity.posZ+MathHelper.clamp(zPosF-(float)entity.posZ, -1, 1));
		}
		return super.getZPosF();
	}
}
