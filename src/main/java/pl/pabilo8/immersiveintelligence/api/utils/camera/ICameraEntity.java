package pl.pabilo8.immersiveintelligence.api.utils.camera;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Pabilo8
 * @since 14.02.2023
 */
@SideOnly(Side.CLIENT)
public interface ICameraEntity
{
	/**
	 * @param cameraPlayer client player
	 * @param partialTicks partial ticks when rendering
	 * @return camera pitch angle
	 */
	float getCameraPitch(EntityPlayer cameraPlayer, float partialTicks);

	/**
	 * @param cameraPlayer client player
	 * @param partialTicks partial ticks when rendering
	 * @return camera yaw angle
	 */
	float getCameraYaw(EntityPlayer cameraPlayer, float partialTicks);

	/**
	 * @param cameraPlayer client player
	 * @param partialTicks partial ticks when rendering
	 * @return camera position (absolute)
	 */
	Vec3d getCameraPos(EntityPlayer cameraPlayer, float partialTicks);

	default void setCameraParameters(EntityPlayer cameraPlayer, float partialTicks)
	{

	}
}
