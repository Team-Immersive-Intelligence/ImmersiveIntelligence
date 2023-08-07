package pl.pabilo8.immersiveintelligence.client.util.carversound;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityMotorbike;

/**
 * @author Pabilo8
 * @since 18.04.2021
 */
@SideOnly(Side.CLIENT)
public class MovingSoundMotorbikeEngine extends MovingSound
{
	private final EntityMotorbike motorbike;
	private float distance = 0.0F;

	public MovingSoundMotorbikeEngine(EntityMotorbike minecartIn)
	{
		super(IISounds.motorbikeEngine, SoundCategory.NEUTRAL);
		this.motorbike = minecartIn;
		this.repeat = true;
		this.repeatDelay = 0;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	public void update()
	{
		if (this.motorbike.isDead)
		{
			this.donePlaying = true;
		}
		else
		{
			//IILogger.info(new Vec3d(motorbike.motionX, motorbike.motionY, motorbike.motionZ).distanceTo(Vec3d.ZERO));
			this.xPosF = (float)this.motorbike.posX;
			this.yPosF = (float)this.motorbike.posY;
			this.zPosF = (float)this.motorbike.posZ;

			if (motorbike.engineProgress>10)
			{
				this.distance = Math.min(1f,distance+0.05f);
				this.pitch = motorbike.acceleration*1.25f;
			}
			else
			{
				this.pitch=0;
				this.distance = Math.max(0f,distance-0.07f);
			}
			this.volume=distance*0.95f;
		}
	}
}