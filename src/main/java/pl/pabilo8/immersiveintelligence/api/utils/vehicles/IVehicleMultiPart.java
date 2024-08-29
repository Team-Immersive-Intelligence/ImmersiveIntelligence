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
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehiclePart;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 09.07.2020
 */
public interface IVehicleMultiPart extends IEntityMultiPart
{
	boolean onInteractWithPart(EntityVehiclePart part, EntityPlayer player, EnumHand hand);

	default boolean useNixieFontOnPart(EntityVehiclePart part, EntityPlayer player, RayTraceResult mop)
	{
		return false;
	}

	String[] getOverlayTextOnPart(EntityVehiclePart part, EntityPlayer player, RayTraceResult mop);

	EntityVehiclePart[] getParts();

	void getSeatRidingPosition(int seatID, Entity passenger);

	void getSeatRidingAngle(int seatID, Entity passenger);

	boolean shouldSeatPassengerSit(int seatID, Entity passenger);

	void onSeatDismount(int seatID, Entity passenger);

	default void updateParts(Entity vehicle)
	{
		boolean client = vehicle.world.isRemote;

		//create vectors
		Vec3d vecX = IIMath.offsetPosDirection(1f, Math.toRadians(MathHelper.wrapDegrees(-vehicle.rotationYaw)), 0);
		Vec3d vecZ = IIMath.offsetPosDirection(1f, Math.toRadians(MathHelper.wrapDegrees(-vehicle.rotationYaw-90)), 0);

		for(EntityVehiclePart part : getParts())
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
}
