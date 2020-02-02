package pl.pabilo8.immersiveintelligence.api.camera;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.util.math.BlockPos;
import pl.pabilo8.immersiveintelligence.common.entity.EntityCamera;

/**
 * Created by Pabilo8 on 10-11-2019.
 */
public class CameraHandler
{
	public static CameraHandler INSTANCE = new CameraHandler();
	EntityCamera camera;
	boolean enabled = false;

	public void setCameraPos(BlockPos pos)
	{
		setCameraPos(pos.getX(), pos.getY(), pos.getZ());
	}

	public void setCameraPos(double x, double y, double z)
	{
		if(camera==null||camera.getEntityWorld()!=ClientUtils.mc().world)
		{
			camera = new EntityCamera(ClientUtils.mc().world);
			ClientUtils.mc().world.spawnEntity(camera);
		}
		camera.posX = x;
		camera.posY = y;
		camera.posZ = z;
	}

	public void setCameraAngle(float yaw, float pitch, float roll)
	{
		if(camera==null||camera.getEntityWorld()!=ClientUtils.mc().world)
		{
			camera = new EntityCamera(ClientUtils.mc().world);
			ClientUtils.mc().world.spawnEntity(camera);
		}
		camera.prevRotationYaw = yaw;
		camera.rotationYaw = yaw;
		camera.prevRotationPitch = pitch;
		camera.rotationPitch = pitch;
		camera.rotationRoll = roll;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
		if(enabled)
		{
			if(camera==null||camera.getEntityWorld()!=ClientUtils.mc().world)
			{
				camera = new EntityCamera(ClientUtils.mc().world);
				ClientUtils.mc().world.spawnEntity(camera);

			}
			ClientUtils.mc().setRenderViewEntity(camera);
		}
		else
			ClientUtils.mc().setRenderViewEntity(ClientUtils.mc().player);
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public float getYaw()
	{
		return camera.rotationYaw;
	}

	public float getPitch()
	{
		return camera.rotationPitch;
	}

	public float getRoll()
	{
		return camera.rotationRoll;
	}
}
