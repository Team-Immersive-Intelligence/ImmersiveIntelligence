package pl.pabilo8.immersiveintelligence.api.utils.vehicles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import pl.pabilo8.immersiveintelligence.common.entity.EntityVehicleMultiPart;

/**
 * @author Pabilo8
 * @since 09.07.2020
 */
public interface IVehicleMultiPart extends IEntityMultiPart
{
	boolean onInteractWithPart(EntityVehicleMultiPart part, EntityPlayer player, EnumHand hand);

	default boolean useNixieFontOnPart(EntityVehicleMultiPart part, EntityPlayer player, RayTraceResult mop)
	{
		return false;
	}

	String[] getOverlayTextOnPart(EntityVehicleMultiPart part, EntityPlayer player, RayTraceResult mop, boolean hammer);

	Entity[] getParts();

	void getSeatRidingPosition(int seatID, Entity passenger);

	void getSeatRidingAngle(int seatID, Entity passenger);

	boolean shouldSeatPassengerSit(int seatID, Entity passenger);
}
