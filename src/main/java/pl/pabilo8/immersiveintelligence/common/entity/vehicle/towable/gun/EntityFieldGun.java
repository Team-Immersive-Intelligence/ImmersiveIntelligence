package pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.gun;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.EntityVehicleTowable;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleBlueprint;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleType;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehiclePart;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 29.09.2025
 */
@VehicleBlueprint(id = "immersiveintelligence:towed/field_gun", mass = 10, type = VehicleType.TOWED_WEAPON)
public class EntityFieldGun extends EntityVehicleTowable<EntityFieldGun>
{
	public EntityFieldGun(World world)
	{
		super(world);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected EntityVehiclePart<EntityFieldGun>[] vehicleInit()
	{
		return new EntityVehiclePart[]{};
	}

	@Override
	protected void onVehicleUpdate()
	{

	}

	@Override
	public boolean onInteractWithPart(EntityVehiclePart<EntityFieldGun> part, EntityPlayer player, EnumHand hand)
	{
		return false;
	}

	@Override
	public String[] getOverlayTextOnPart(EntityVehiclePart<EntityFieldGun> part, EntityPlayer player, RayTraceResult mop)
	{
		return new String[0];
	}

	@Override
	public void getSeatRidingPosition(String seatID, Entity passenger)
	{

	}

	@Override
	public void getSeatRidingAngle(String seatID, Entity passenger)
	{

	}

	@Override
	public boolean shouldSeatPassengerSit(String seatID, Entity passenger)
	{
		return false;
	}

	@Override
	public void onSeatDismount(String seatID, Entity passenger)
	{

	}
}
