package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;
import pl.pabilo8.immersiveintelligence.common.util.entity.SyncedDurability;

import javax.annotation.Nonnull;

/**
 * Independently rotated vehicle layer.
 * <p>
 * The root segment represents the hull. Additional segments can be used for turrets, trailers,
 * folding ramps, gun cradles, suspension bogies, or any other layer that should rotate relative
 * to the hull while still participating in the vehicle OBB system.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.06.2026
 */
public class VehicleSegment<T extends EntityVehicleBase<T>> implements IVehicleComponent
{
	@Nonnull
	private final T vehicle;
	@Nonnull
	private final String name;
	@Nonnull
	private Vec3d offset;

	private float prevYaw = 0, yaw = 0;
	private float prevPitch = 0, pitch = 0;
	private float prevRoll = 0, roll = 0;

	public VehicleSegment(@Nonnull T vehicle, @Nonnull String name)
	{
		this(vehicle, name, Vec3d.ZERO);
	}

	public VehicleSegment(@Nonnull T vehicle, @Nonnull String name, @Nonnull Vec3d offset)
	{
		this.vehicle = vehicle;
		this.name = name;
		this.offset = offset;
	}

	@Override
	public void onUpdate()
	{
		this.prevYaw = this.yaw;
		this.prevPitch = this.pitch;
		this.prevRoll = this.roll;
	}

	//--- Setters ---//

	public VehicleSegment<T> withOffset(@Nonnull Vec3d offset)
	{
		this.offset = offset;
		return this;
	}

	public VehicleSegment<T> setRotation(float yaw, float pitch, float roll)
	{
		this.yaw = MathHelper.wrapDegrees(yaw);
		this.pitch = MathHelper.wrapDegrees(pitch);
		this.roll = MathHelper.wrapDegrees(roll);
		return this;
	}

	public VehicleSegment<T> rotate(float yaw, float pitch, float roll)
	{
		return setRotation(this.yaw+yaw, this.pitch+pitch, this.roll+roll);
	}

	//--- Transform ---//

	public Vec3d getWorldPosition(@Nonnull Vec3d vehiclePosition, float vehicleYaw, float vehiclePitch, float vehicleRoll)
	{
		return vehiclePosition.add(VehicleOBB.transformLocal(this.offset, vehicleYaw, vehiclePitch, vehicleRoll));
	}

	public Vec3d getPartWorldPosition(@Nonnull Vec3d vehiclePosition, @Nonnull Vec3d localPartOffset,
									  float vehicleYaw, float vehiclePitch, float vehicleRoll)
	{
		Vec3d segmentPosition = getWorldPosition(vehiclePosition, vehicleYaw, vehiclePitch, vehicleRoll);
		return segmentPosition.add(VehicleOBB.transformLocal(localPartOffset,
				getWorldYaw(vehicleYaw), getWorldPitch(vehiclePitch), getWorldRoll(vehicleRoll)));
	}

	public VehicleOBB createPartOBB(@Nonnull AxisAlignedBB localBox, @Nonnull Vec3d vehiclePosition,
									@Nonnull Vec3d localPartOffset, float vehicleYaw, float vehiclePitch, float vehicleRoll,
									float additionalYaw)
	{
		Vec3d partPosition = getPartWorldPosition(vehiclePosition, localPartOffset, vehicleYaw, vehiclePitch, vehicleRoll);
		return VehicleOBB.fromLocalAABB(localBox, partPosition,
				getWorldYaw(vehicleYaw)+additionalYaw,
				getWorldPitch(vehiclePitch),
				getWorldRoll(vehicleRoll));
	}

	public float getWorldYaw(float vehicleYaw)
	{
		return MathHelper.wrapDegrees(vehicleYaw+this.yaw);
	}

	public float getWorldPitch(float vehiclePitch)
	{
		return MathHelper.wrapDegrees(vehiclePitch+this.pitch);
	}

	public float getWorldRoll(float vehicleRoll)
	{
		return MathHelper.wrapDegrees(vehicleRoll+this.roll);
	}

	//--- Getters ---//

	@Nonnull
	public T getVehicle()
	{
		return vehicle;
	}

	@Nonnull
	public String getName()
	{
		return name;
	}

	@Nonnull
	public Vec3d getOffset()
	{
		return offset;
	}

	public float getYaw()
	{
		return yaw;
	}

	public float getPitch()
	{
		return pitch;
	}

	public float getRoll()
	{
		return roll;
	}

	public float getPrevYaw()
	{
		return prevYaw;
	}

	public float getPrevPitch()
	{
		return prevPitch;
	}

	public float getPrevRoll()
	{
		return prevRoll;
	}

	@Override
	public SyncedDurability getDurability()
	{
		return null;
	}
}
