package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleBlueprint;
import pl.pabilo8.immersiveintelligence.common.util.entity.SyncedDurability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Logical wheel receiver used by the vehicle physics layer.
 * <p>
 * Transmissions should normally feed these groups instead of individual wheels. The group stores
 * the drivetrain state once, applies shared steering/braking, and lets each wheel contribute contact
 * and suspension. That makes tracks, axles, and lone wheels behave like real vehicle subassemblies
 * instead of several unrelated engines bolted to the same hull.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.06.2026
 */
public class VehicleWheelGroup<T extends EntityVehicleBase<T>> implements IVehicleComponent, IRotaryEnergy
{
	public enum Mode
	{
		SINGLE,
		AXLE,
		TRACK
	}

	@Nonnull
	private final T vehicle;
	@Nonnull
	private final String name;
	@Nonnull
	private EntityVehicleWheel<T>[] wheels = new EntityVehicleWheel[0];
	@Nonnull
	private Mode mode = Mode.SINGLE;

	private final RotaryStorage rotaryStorage = new RotaryStorage(0, 0);
	private float steeringAngle = 0;
	private float brakeFactor = 0;
	private float driveScale = 1f;
	private float lateralScale = 1f;

	public VehicleWheelGroup(@Nonnull T vehicle, @Nonnull String name)
	{
		this.vehicle = vehicle;
		this.name = name;
	}

	//--- Setters ---//

	@SafeVarargs
	public final VehicleWheelGroup<T> withWheels(@Nonnull EntityVehicleWheel<T>... wheels)
	{
		this.wheels = wheels;
		for(EntityVehicleWheel<T> wheel : wheels)
			if(wheel!=null)
				wheel.withWheelGroup(this);
		return this;
	}

	public VehicleWheelGroup<T> withMode(@Nonnull Mode mode)
	{
		this.mode = mode;
		return this;
	}

	public VehicleWheelGroup<T> withDriveScale(float driveScale)
	{
		this.driveScale = driveScale;
		return this;
	}

	public VehicleWheelGroup<T> withLateralScale(float lateralScale)
	{
		this.lateralScale = lateralScale;
		return this;
	}

	public void setSteeringAngle(float angle)
	{
		this.steeringAngle = MathHelper.clamp(angle, -45.0f, 45.0f);
		for(EntityVehicleWheel<T> wheel : wheels)
			if(wheel!=null&&wheel.getType().isSteerable())
				wheel.setSteeringAngle(this.steeringAngle);
	}

	public void setBrakeFactor(float brakeFactor)
	{
		this.brakeFactor = MathHelper.clamp(brakeFactor, 0f, 1f);
		for(EntityVehicleWheel<T> wheel : wheels)
			if(wheel!=null)
				wheel.setBrakeFactor(this.brakeFactor);
	}

	//--- Behaviour used by wheels ---//

	public double getDriveShare(EntityVehicleWheel<T> wheel)
	{
		int count = Math.max(1, getSupportedWheelCount());
		switch(mode)
		{
			case TRACK:
				//A track spreads the drivetrain over the road wheels that are actually supporting the track.
				return 1.0/count;
			case AXLE:
				return 1.0/count;
			case SINGLE:
			default:
				return 1.0;
		}
	}

	public double getDriveForce(EntityVehicleWheel<T> wheel, VehicleBlueprint blueprint, double efficiency, double grip)
	{
		double targetSpeed = Math.max(1.0, blueprint.nominalWheelSpeed());
		double targetTorque = Math.max(1.0, blueprint.nominalWheelTorque());
		double speedIntent = MathHelper.clamp(Math.abs(getRotationSpeed())/targetSpeed, 0.0, 1.0);
		double torqueIntent = MathHelper.clamp(Math.abs(getTorque())/targetTorque, 0.0, 1.0);
		double sign = Math.signum(getRotationSpeed());
		if(sign==0)
			sign = Math.signum(getTorque());
		return sign*blueprint.driveForce()*speedIntent*torqueIntent*(1f-brakeFactor)*efficiency*grip*driveScale*getDriveShare(wheel);
	}

	public double getLateralFriction(VehicleBlueprint blueprint, EntityVehicleWheel<T> wheel)
	{
		double friction;
		switch(mode)
		{
			case TRACK:
				friction = blueprint.trackLateralFriction();
				break;
			case AXLE:
				friction = blueprint.axleLateralFriction();
				break;
			case SINGLE:
			default:
				friction = wheel.getType().isDriven()?blueprint.lateralFrictionDrive(): blueprint.lateralFrictionIdler();
				break;
		}
		if(Math.abs(wheel.getSteeringAngle()) > 0.1f)
			friction *= blueprint.steeringFrictionMultiplier();
		return friction*lateralScale;
	}

	private int getActiveWheelCount()
	{
		int count = 0;
		for(EntityVehicleWheel<T> wheel : wheels)
			if(wheel!=null)
				count++;
		return count;
	}

	private int getSupportedWheelCount()
	{
		int count = 0;
		for(EntityVehicleWheel<T> wheel : wheels)
			if(wheel!=null&&(wheel.isSuspensionSupported()||wheel.getLastVerticalForces().isGrounded))
				count++;
		return count > 0?count: getActiveWheelCount();
	}

	//--- IVehicleComponent ---//

	@Override
	public void onUpdate()
	{
		//Groups are passive receivers; wheel/contact simulation is run from EntityVehicleBase.
	}

	@Nullable
	@Override
	public SyncedDurability getDurability()
	{
		return null;
	}

	//--- IRotaryEnergy ---//

	@Override
	public float getTorque()
	{
		return rotaryStorage.getTorque();
	}

	@Override
	public void setTorque(float torque)
	{
		rotaryStorage.setTorque(torque);
	}

	@Override
	public float getRotationSpeed()
	{
		return rotaryStorage.getRotationSpeed();
	}

	@Override
	public void setRotationSpeed(float speed)
	{
		rotaryStorage.setRotationSpeed(speed);
	}

	@Override
	public RotationSide getSide(@Nullable EnumFacing facing)
	{
		return RotationSide.INPUT;
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
	public Mode getMode()
	{
		return mode;
	}

	@Nonnull
	public EntityVehicleWheel<T>[] getWheels()
	{
		return wheels;
	}

	public boolean hasDriveInput()
	{
		return Math.abs(getTorque()) > 1.0E-5f||Math.abs(getRotationSpeed()) > 1.0E-5f;
	}

	public float getBrakeFactor()
	{
		return brakeFactor;
	}

	public float getSteeringAngle()
	{
		return steeringAngle;
	}
}
