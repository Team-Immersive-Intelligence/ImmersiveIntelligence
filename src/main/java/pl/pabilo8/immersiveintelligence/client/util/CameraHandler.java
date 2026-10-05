package pl.pabilo8.immersiveintelligence.client.util;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.camera.ICameraEntity;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedZoom;
import pl.pabilo8.immersiveintelligence.common.entity.EntityCamera;

/**
 * Controls the client camera and item or entity zoom.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 03.10.2026
 * @since 10.11.2019
 */
@SideOnly(Side.CLIENT)
public class CameraHandler
{
	//--- ZoomHandler ---//
	public static float fovZoom = 1;
	public static ZoomType type = null;
	public static IAdvancedZoom zoom;
	public static ItemStack stack = ItemStack.EMPTY;
	private static float previousZoomFactor = 1;
	private static boolean previousZooming = false;

	//--- CameraHandler ---//
	private static EntityCamera camera;
	private static boolean enabled = false;

	public static void setCameraPos(Vec3d pos)
	{
		setCameraPos(pos.x, pos.y, pos.z);
	}

	public static void setCameraPos(BlockPos pos)
	{
		setCameraPos(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
	}

	/**
	 * Sets the camera position for the current render frame.
	 */
	public static void setCameraPos(double x, double y, double z)
	{
		ensureExists();
		//The provider has already applied partial ticks to this position.
		camera.setPosition(x, y, z);
		camera.prevPosX = camera.lastTickPosX = x;
		camera.prevPosY = camera.lastTickPosY = y;
		camera.prevPosZ = camera.lastTickPosZ = z;
	}

	/**
	 * Sets the camera angles for the current render frame.
	 */
	public static void setCameraAngle(float yaw, float pitch, float roll)
	{
		ensureExists();
		//Do not interpolate the provider's render angles a second time.
		camera.prevRotationYaw = camera.rotationYaw = yaw;
		camera.prevRotationPitch = camera.rotationPitch = pitch;
		camera.rotationRoll = roll;
	}

	public static boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * Selects the custom camera or restores the player view.
	 */
	public static void setEnabled(boolean enabled)
	{
		Minecraft mc = ClientUtils.mc();
		if(enabled)
			ensureExists();

		Entity viewEntity = enabled?camera: mc.player;
		if((enabled||CameraHandler.enabled)&&mc.getRenderViewEntity()!=viewEntity)
		{
			mc.setRenderViewEntity(viewEntity);
			mc.renderGlobal.setDisplayListEntitiesDirty();
		}

		CameraHandler.enabled = enabled;
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

	/**
	 * Updates the zoom provider and refreshes terrain visibility when zoom changes.
	 */
	public static boolean handleZoom()
	{
		boolean zooming = handleZoomLogic();
		if(!zooming)
		{
			type = null;
			zoom = null;
			stack = ItemStack.EMPTY;
		}

		float zoomFactor = zooming?fovZoom: 1;
		if(zooming!=previousZooming||Float.compare(zoomFactor, previousZoomFactor)!=0)
		{
			//Terrain visibility does not update for an FOV change alone.
			ClientUtils.mc().renderGlobal.setDisplayListEntitiesDirty();
			previousZoomFactor = zoomFactor;
		}
		previousZooming = zooming;
		return zooming;
	}

	private static boolean handleZoomLogic()
	{
		EntityPlayer player = ClientUtils.mc().player;
		if(player==null)
			return false;

		//An active item zoom has priority over the ridden camera zoom.
		if(tryUseHeldZoom(player, EnumHand.MAIN_HAND, ZoomType.ITEM_MAINHAND)
				||tryUseHeldZoom(player, EnumHand.OFF_HAND, ZoomType.ITEM_OFFHAND))
			return true;

		Entity lowestRidden = player.getLowestRidingEntity();
		if(!(lowestRidden instanceof ICameraEntity camera)||!camera.isCameraEnabled(player))
			return false;

		return tryUseZoom(camera.getZoom(), camera.getZoomStack(), player, ZoomType.RIDING);
	}

	private static boolean tryUseHeldZoom(EntityPlayer player, EnumHand hand, ZoomType zoomType)
	{
		ItemStack heldStack = player.getHeldItem(hand);
		if(!(heldStack.getItem() instanceof IAdvancedZoom itemZoom))
			return false;

		return tryUseZoom(itemZoom, heldStack, player, zoomType);
	}

	private static boolean tryUseZoom(IAdvancedZoom newZoom, ItemStack newStack, EntityPlayer player, ZoomType newType)
	{
		if(newZoom==null||!newZoom.shouldZoom(newStack, player))
			return false;

		zoom = newZoom;
		stack = newStack;
		type = newType;

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
			//Keep the render camera outside the world tick and collision systems.
			camera = new EntityCamera(ClientUtils.mc().world);
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
