package pl.pabilo8.immersiveintelligence.api.utils.vehicles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleBlueprint;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehiclePart;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat.SeatInfo;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.VehicleOBB;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.07.2020
 */
public interface IVehicleMultiPart<T extends Entity & IVehicleMultiPart<T>> extends IEntityMultiPart
{
	boolean onInteractWithPart(EntityVehiclePart<T> part, EntityPlayer player, EnumHand hand);

	default String[] getOverlayTextOnPart(EntityVehiclePart<T> part, EntityPlayer player, RayTraceResult mop)
	{
		return null;
	}

	Entity[] getParts();

	EntityVehiclePart<T>[] getVehicleParts();

	VehicleBlueprint getVehicleBlueprint();

	SeatInfo<?> getSeatInfo(String seatID);

	void onSeatDismount(String seatID, Entity passenger);

	/**
	 * Ray-traces the vehicle's precise part OBBs and returns the closest hit part.
	 * This keeps interaction selection consistent with custom vehicle collision instead of vanilla enclosing AABBs.
	 */
	@Nullable
	default EntityVehiclePart<T> rayTracePart(Vec3d start, Vec3d end)
	{
		EntityVehiclePart<T> closest = null;
		double closestDistanceSq = Double.MAX_VALUE;
		for(EntityVehiclePart<T> part : getVehicleParts())
		{
			Vec3d hit = part.rayTraceOBB(start, end);
			if(hit==null)
				continue;

			double distanceSq = hit.squareDistanceTo(start);
			if(distanceSq < closestDistanceSq)
			{
				closestDistanceSq = distanceSq;
				closest = part;
			}
		}
		return closest;
	}

	/**
	 * Ray-traces from a player's eyes using normal Minecraft interaction reach.
	 */
	@Nullable
	default EntityVehiclePart<T> rayTracePart(EntityPlayer player)
	{
		double reach = player.capabilities.isCreativeMode?5.0D: 4.5D;
		Vec3d start = player.getPositionEyes(1.0F);
		Vec3d end = start.add(player.getLook(1.0F).scale(reach));
		return rayTracePart(start, end);
	}

	/**
	 * Delegates interaction to the vehicle's existing part-aware interaction method after precise OBB selection.
	 */
	default boolean interactRayTracedPart(EntityPlayer player, EnumHand hand)
	{
		EntityVehiclePart<T> part = rayTracePart(player);
		return part!=null&&onInteractWithPart(part, player, hand);
	}

	default void updateParts()
	{
		//noinspection unchecked
		T vehicle = ((T)this);
		boolean client = vehicle.world.isRemote;

		for(EntityVehiclePart<T> part : getVehicleParts())
		{
			// Vehicle-local convention: +X right, +Y up, -Z front.
			Vec3d offset = VehicleOBB.transformLocal(part.offset, vehicle.rotationYaw, vehicle.rotationPitch, getRotationRoll());

			part.setLocationAndAngles(
					vehicle.posX+offset.x,
					vehicle.posY+offset.y,
					vehicle.posZ+offset.z,
					0.0F, 0);

			if(client)
				IIEntityUtils.setEntityMotion(part, vehicle.motionX, vehicle.motionY, vehicle.motionZ);
			part.onUpdate();
		}
	}

	@Override
	default boolean attackEntityFromPart(@Nonnull MultiPartEntityPart part, @Nonnull DamageSource source, float amount)
	{
		return false;
	}

	@Override
	default World getWorld()
	{
		return ((Entity)this).getEntityWorld();
	}

	default double getAngularVelocity()
	{
		return 0;
	}

	Vec3d getVelocity();

	default float getRotationRoll()
	{
		return 0;
	}
}
