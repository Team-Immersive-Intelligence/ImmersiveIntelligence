package pl.pabilo8.immersiveintelligence.client.gui.overlay;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.09.2022
 */
public class GuiOverlayMotorbike extends GuiOverlayBase
{
	@Override
	public boolean shouldDraw(@Nonnull EntityPlayer player, @Nullable RayTraceResult mouseOver)
	{
		return player.getRidingEntity() instanceof EntityVehicleSeat;
	}

	@Override
	public void draw(@Nonnull EntityPlayer player, @Nullable RayTraceResult mouseOver, int width, int height)
	{
		EntityVehicleSeat seat = (EntityVehicleSeat)player.getRidingEntity();
	}
}
