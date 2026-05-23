package pl.pabilo8.immersiveintelligence.api.utils.camera;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedZoom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
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
	 * @return camera roll angle
	 */
	default float getCameraRoll(EntityPlayer cameraPlayer, float partialTicks)
	{
		return 0;
	}

	/**
	 * @param cameraPlayer client player
	 * @param partialTicks partial ticks when rendering
	 * @return camera position (absolute)
	 */
	Vec3d getCameraPos(EntityPlayer cameraPlayer, float partialTicks);

	boolean isCameraEnabled(EntityPlayer player);

	default boolean isThirdPersonAllowed(EntityPlayer cameraPlayer)
	{
		return true;
	}

	//--- Zooming ---//

	@Nullable
	default IAdvancedZoom getZoom()
	{
		return null;
	}

	/**
	 * Allows to use a zoom-providing ItemStack. Used for vehicles, where the player can use inventory items like binoculars.
	 *
	 * @return the ItemStack providing the zoom, or an empty stack if none
	 */
	@Nonnull
	default ItemStack getZoomStack()
	{
		return ItemStack.EMPTY;
	}
}
