package pl.pabilo8.immersiveintelligence.api.utils.vehicles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleBlueprint;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehiclePart;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;

import javax.annotation.Nonnull;

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

	void getSeatRidingPosition(String seatID, Entity passenger);

	void getSeatRidingAngle(String seatID, Entity passenger);

	boolean shouldSeatPassengerSit(String seatID, Entity passenger);

	void onSeatDismount(String seatID, Entity passenger);

	default void updateParts()
	{
		Entity vehicle = ((Entity)this);
		boolean client = vehicle.world.isRemote;

		//create vectors
		Vec3d vecX = IIMath.offsetPosDirection(1f, Math.toRadians(MathHelper.wrapDegrees(-vehicle.rotationYaw)), 0);
		Vec3d vecZ = IIMath.offsetPosDirection(1f, Math.toRadians(MathHelper.wrapDegrees(-vehicle.rotationYaw-90)), 0);

		for(EntityVehiclePart<T> part : getVehicleParts())
		{
			//transform offset using on the rotated vectors
			Vec3d offsetX = vecX.scale(part.offset.x);
			Vec3d offsetZ = vecZ.scale(part.offset.z);

			part.setLocationAndAngles(
					vehicle.posX+offsetX.x+offsetZ.x,
					vehicle.posY+part.offset.y,
					vehicle.posZ+offsetX.z+offsetZ.z,
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

	VehicleBlueprint getVehicleBlueprint();
}
