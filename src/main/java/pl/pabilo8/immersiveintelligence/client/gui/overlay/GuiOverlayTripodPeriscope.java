package pl.pabilo8.immersiveintelligence.client.gui.overlay;

import blusunrize.immersiveengineering.api.tool.ZoomHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import pl.pabilo8.immersiveintelligence.client.util.CameraHandler;
import pl.pabilo8.immersiveintelligence.common.entity.mounted_weapon.EntityTripodPeriscope;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.09.2022
 */
public class GuiOverlayTripodPeriscope extends GuiOverlayBase
{
	@Override
	public boolean shouldDraw(@Nonnull EntityPlayer player, @Nullable RayTraceResult mouseOver)
	{
		return ZoomHandler.isZooming&&player.getRidingEntity() instanceof EntityTripodPeriscope;
	}

	@Override
	public void draw(@Nonnull EntityPlayer player, @Nullable RayTraceResult mouseOver, int width, int height)
	{
		ClientUtils.font().drawString(I18n.format(IIReference.INFO_KEY+"yaw", CameraHandler.getYaw()),
				(int)(width*0.5)+8, (int)(height*0.5)+8, 0xffffff, true);
		ClientUtils.font().drawString(I18n.format(IIReference.INFO_KEY+"pitch", CameraHandler.getPitch()),
				(int)(width*0.5)+8, (int)(height*0.5)+16, 0xffffff, true);

		RayTraceResult traceResult = CameraHandler.rayTrace(90, 0);
		BlockPos pos = ClientUtils.mc().player.getPosition();

		ClientUtils.font().drawString(I18n.format(IIReference.INFO_KEY+"distance", (traceResult==null||traceResult.typeOfHit==Type.MISS)?I18n.format(IIReference.INFO_KEY+"distance_unknown"): traceResult.getBlockPos().getDistance(pos.getX(), pos.getY(), pos.getZ())),
				(int)(width*0.5)+8, (int)(height*0.5)+24, 0xffffff, true);
	}
}
