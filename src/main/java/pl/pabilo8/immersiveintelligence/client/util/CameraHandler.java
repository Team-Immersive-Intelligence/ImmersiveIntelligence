package pl.pabilo8.immersiveintelligence.client.util;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.camera.IEntityZoomProvider;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedZoomTool;
import pl.pabilo8.immersiveintelligence.client.ClientEventHandler;
import pl.pabilo8.immersiveintelligence.common.entity.EntityCamera;

/**
 * @author Pabilo8
 * @updated 16.02.2023
 * @since 10-11-2019
 */
@SideOnly(Side.CLIENT)
public class CameraHandler
{
	//--- CameraHandler ---//
	private static EntityCamera camera;
	private static boolean enabled = false;

	//--- ZoomHandler ---//
	public static float fovZoom = 1;
	public static ZoomType type = null;
	public static IAdvancedZoomTool zoom;
	public static ItemStack stack;

	public static void setCameraPos(BlockPos pos)
	{
		setCameraPos(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
	}

	public static void setCameraPos(double x, double y, double z)
	{
		ensureExists();
		camera.posX = x;
		camera.posY = y;
		camera.posZ = z;
	}

	public static void setCameraAngle(float yaw, float pitch, float roll)
	{
		ensureExists();
		camera.prevRotationYaw = camera.rotationYaw;
		camera.rotationYaw = yaw;
		camera.prevRotationPitch = camera.rotationPitch;
		camera.rotationPitch = pitch;
		camera.rotationRoll = roll;
	}

	public static void setEnabled(boolean enabled)
	{
		CameraHandler.enabled = enabled;
		ensureExists();
		ClientUtils.mc().setRenderViewEntity(enabled?camera: ClientUtils.mc().player);
	}

	public static boolean isEnabled()
	{
		return enabled;
	}

	public static float getYaw()
	{
		return camera.rotationYaw;
	}

	public static float getPitch()
	{
		return camera.rotationPitch;
	}

	public static float getRoll()
	{
		return camera.rotationRoll;
	}

	public static boolean handleZoom()
	{
		if(handleZoomLogic())
			return true;
		type = null;
		zoom = null;
		return false;
	}

	private static boolean handleZoomLogic()
	{
		//Setup
		EntityPlayer player = ClientUtils.mc().player;
		Entity lowestRidden;

		//Zoom provider is an item in player's hand
		if((stack = player.getHeldItem(EnumHand.MAIN_HAND)).getItem() instanceof IAdvancedZoomTool)
		{
			type = ZoomType.ITEM_MAINHAND;
			zoom = (IAdvancedZoomTool)(stack.getItem());
		}
		else if((stack = player.getHeldItem(EnumHand.OFF_HAND)).getItem() instanceof IAdvancedZoomTool)
		{
			type = ZoomType.ITEM_OFFHAND;
			zoom = (IAdvancedZoomTool)(stack.getItem());
		}
		//Zoom provider is an entity ridden by player
		else if(ClientEventHandler.mgAiming&&(lowestRidden = player.getLowestRidingEntity()) instanceof IEntityZoomProvider)
		{
			type = ZoomType.RIDING;
			zoom = ((IEntityZoomProvider)lowestRidden).getZoom();
		}
		else
			return false;

		//Zoom conditions aren't met
		if(zoom==null||!zoom.shouldZoom(stack, player))
			return false;

		float[] steps = zoom.getZoomSteps(stack, player);
		if(steps!=null&&steps.length > 0)
		{
			int curStep = -1;
			float dist = 0;
			//When other zoom provider with different zoom was used
			for(int i = 0; i < steps.length; i++)
				if(curStep==-1||Math.abs(steps[i]-fovZoom) < dist)
				{
					curStep = i;
					dist = Math.abs(steps[i]-fovZoom);
				}
			fovZoom = steps[curStep];
		}

		return true;
	}

	private static void ensureExists()
	{
		if(camera==null||camera.getEntityWorld()!=ClientUtils.mc().world)
		{
			camera = new EntityCamera(ClientUtils.mc().world);
			ClientUtils.mc().world.spawnEntity(camera);
		}
	}

	public static RayTraceResult rayTrace(double blockReachDistance, float partialTicks)
	{
		return camera.rayTrace(blockReachDistance, partialTicks);
	}

	public enum ZoomType
	{
		ITEM_MAINHAND,
		ITEM_OFFHAND,
		RIDING
	}
}
