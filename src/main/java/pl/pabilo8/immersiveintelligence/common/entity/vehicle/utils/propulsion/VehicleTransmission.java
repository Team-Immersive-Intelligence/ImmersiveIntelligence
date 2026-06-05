package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.propulsion;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleWheel;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.IVehicleComponent;
import pl.pabilo8.immersiveintelligence.common.util.entity.SyncedDurability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A combined semi-automatic gearbox/transmission class for vehicles. Links an
 * {@link VehicleEngineBase engine} to a {@link EntityVehicleWheel group of wheels} or another transmission.
 * <p>
 * Manual clutch control is intentionally absent. Gear requests are stored, and the actual shift animation
 * begins only when the input shaft reaches a safe speed. Unsafe downshifts shock the drivetrain, stall the
 * engine and may damage this gearbox.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 01.10.2025
 */
public class VehicleTransmission<V extends EntityVehicleBase<V>> implements IVehicleComponent, INBTSerializable<NBTTagCompound>, IRotaryEnergy
{
	@Nonnull
	protected final IRotaryEnergy[] sources;
	@Nonnull
	protected IRotaryEnergy[] receivers = new IRotaryEnergy[0];

	private SyncedDurability durability;
	private double[] ratios = new double[0];
	private int maxGearShiftTime = 20;
	private int currentGear = 0, nextGear = 0, requestedGear = -1, gearShiftDelay = 0;
	private int gearboxShockTicks = 0;
	private boolean semiAutomatic = true;
	private float minimumUpshiftSpeed = 120f;
	private float upshiftSpeedStep = 60f;
	private float maximumSafeInputSpeed = 380f;
	private float downshiftShockDamage = 12f;
	private RotaryStorage rotaryStorage = new RotaryStorage(0, 0);

	@ParametersAreNonnullByDefault
	public VehicleTransmission(IRotaryEnergy[] sources)
	{
		this.sources = sources;
	}

	@ParametersAreNonnullByDefault
	public VehicleTransmission(IRotaryEnergy source)
	{
		this.sources = new IRotaryEnergy[]{source};
	}

	public VehicleTransmission<V> withReceivers(@Nonnull IRotaryEnergy... receivers)
	{
		this.receivers = receivers;
		return this;
	}

	public VehicleTransmission<V> withDurability(SyncedDurability durability)
	{
		this.durability = durability;
		return this;
	}

	public VehicleTransmission<V> withRatios(int shiftTime, double... ratios)
	{
		this.ratios = ratios;
		this.maxGearShiftTime = shiftTime;
		this.currentGear = MathHelper.clamp(this.currentGear, 0, ratios.length-1);
		this.nextGear = MathHelper.clamp(this.nextGear, 0, ratios.length-1);
		this.requestedGear = -1;
		return this;
	}

	public VehicleTransmission<V> withCurrentGear(int currentGear)
	{
		this.requestedGear = -1;
		this.nextGear = this.currentGear = MathHelper.clamp(currentGear, 0, ratios.length-1);
		return this;
	}

	/**
	 * Configures semi-automatic shift behaviour. The player may request an upshift at any time, but the
	 * gearbox starts the shift only when the input shaft reaches the required D/t. Downshifts are accepted
	 * immediately if safe; otherwise the drivetrain is shocked and the engine stalls.
	 *
	 * @param minimumUpshiftSpeed   D/t required to shift from the first forward gear into the next forward gear
	 * @param upshiftSpeedStep      additional D/t required for each higher forward gear
	 * @param maximumSafeInputSpeed maximum safe input-shaft D/t after a downshift
	 * @return this transmission
	 */
	public VehicleTransmission<V> withSemiAutomaticShift(float minimumUpshiftSpeed, float upshiftSpeedStep, float maximumSafeInputSpeed)
	{
		this.semiAutomatic = true;
		this.minimumUpshiftSpeed = Math.max(0, minimumUpshiftSpeed);
		this.upshiftSpeedStep = Math.max(0, upshiftSpeedStep);
		this.maximumSafeInputSpeed = Math.max(1, maximumSafeInputSpeed);
		return this;
	}

	public VehicleTransmission<V> withManualInstantShift()
	{
		this.semiAutomatic = false;
		return this;
	}

	public VehicleTransmission<V> withDownshiftShockDamage(float damage)
	{
		this.downshiftShockDamage = Math.max(0, damage);
		return this;
	}

	//--- Methods ---//

	public void onUpdate()
	{
		if(gearboxShockTicks > 0)
			gearboxShockTicks--;

		float totalInputTorque = getTotalInputTorque();
		float totalInputSpeed = getTotalInputSpeed();

		// Start a queued shift only once the semi-automatic conditions are met. This is the clutch logic the
		// player does not have to control manually.
		if(gearShiftDelay <= 0&&requestedGear >= 0)
		{
			if(canStartRequestedShift(totalInputSpeed))
			{
				nextGear = requestedGear;
				requestedGear = -1;
				gearShiftDelay = maxGearShiftTime;
			}
		}

		if(gearShiftDelay > 0)
		{
			gearShiftDelay--;
			if(gearShiftDelay==0)
				currentGear = nextGear;
		}

		float currentRatio = ratios.length==0?1: (float)ratios[currentGear];
		if(gearboxShockTicks > 0||Math.abs(currentRatio) < 1.0E-5f)
		{
			rotaryStorage.setTorque(0);
			rotaryStorage.setRotationSpeed(0);
		}
		else
		{
			rotaryStorage.setTorque(totalInputTorque/currentRatio);
			rotaryStorage.setRotationSpeed(totalInputSpeed*currentRatio);
		}

		float outputTorque = getOutputTorque();
		float outputSpeed = getOutputRotationSpeed();
		for(IRotaryEnergy receiver : receivers)
		{
			receiver.setTorque(receivers.length==0?0: outputTorque/receivers.length);
			receiver.setRotationSpeed(outputSpeed);
		}
	}

	public boolean shiftUp()
	{
		if(currentGear >= ratios.length-1||gearShiftDelay > 0)
			return false;
		return requestGear(currentGear+1);
	}

	public boolean shiftDown()
	{
		if(currentGear <= 0||gearShiftDelay > 0)
			return false;

		int targetGear = currentGear-1;
		if(semiAutomatic&&!isDownshiftSafe(targetGear, getTotalInputSpeed()))
		{
			breakGearbox();
			return false;
		}
		return requestGear(targetGear);
	}

	public boolean requestGear(int gear)
	{
		if(gear < 0||gear >= ratios.length||gearShiftDelay > 0)
			return false;
		if(gear==currentGear)
		{
			requestedGear = -1;
			return false;
		}

		if(!semiAutomatic)
		{
			nextGear = gear;
			gearShiftDelay = maxGearShiftTime;
			requestedGear = -1;
			return true;
		}

		requestedGear = gear;
		return true;
	}

	private boolean canStartRequestedShift(float inputSpeed)
	{
		if(requestedGear < 0||requestedGear >= ratios.length)
			return false;
		if(!semiAutomatic)
			return true;

		double currentRatio = getRatio(currentGear);
		double targetRatio = getRatio(requestedGear);

		// Moving into or out of neutral/reverse is a selector action, not an engine-speed upshift gate.
		if(currentRatio <= 0||targetRatio <= 0)
			return true;

		if(requestedGear > currentGear)
			return Math.abs(inputSpeed) >= getRequiredUpshiftSpeed(requestedGear);

		return isDownshiftSafe(requestedGear, inputSpeed);
	}

	private float getRequiredUpshiftSpeed(int targetGear)
	{
		int forwardIndex = 0;
		for(int i = 0; i <= targetGear&&i < ratios.length; i++)
			if(ratios[i] > 0)
				forwardIndex++;
		return minimumUpshiftSpeed+Math.max(0, forwardIndex-2)*upshiftSpeedStep;
	}

	private boolean isDownshiftSafe(int targetGear, float inputSpeed)
	{
		double currentRatio = Math.abs(getRatio(currentGear));
		double targetRatio = Math.abs(getRatio(targetGear));

		// Neutral is always safe; reverse while moving quickly is not.
		if(targetRatio < 1.0E-5)
			return true;
		if(currentRatio < 1.0E-5)
			return true;

		double projectedInputSpeed = Math.abs(inputSpeed)*currentRatio/targetRatio;
		return projectedInputSpeed <= maximumSafeInputSpeed;
	}

	public void breakGearbox()
	{
		requestedGear = -1;
		nextGear = currentGear;
		gearShiftDelay = 0;
		gearboxShockTicks = Math.max(10, maxGearShiftTime);
		rotaryStorage.setTorque(0);
		rotaryStorage.setRotationSpeed(0);
		for(IRotaryEnergy receiver : receivers)
		{
			receiver.setTorque(0);
			receiver.setRotationSpeed(0);
		}
		if(durability!=null&&downshiftShockDamage > 0)
			durability.attackFrom(new DamageSource("iiGearboxShock"), downshiftShockDamage);
		stallSources();
	}

	private void stallSources()
	{
		for(IRotaryEnergy source : sources)
		{
			if(source instanceof VehicleEngineBase)
				((VehicleEngineBase<?, ?>)source).stall();
			else if(source instanceof VehicleTransmission)
				((VehicleTransmission<?>)source).breakGearbox();
			else
			{
				source.setTorque(0);
				source.setRotationSpeed(0);
			}
		}
	}

	private double getRatio(int gear)
	{
		return gear >= 0&&gear < ratios.length?ratios[gear]: 0;
	}

	private float getTotalInputTorque()
	{
		float total = 0;
		for(IRotaryEnergy source : sources)
			total += source.getOutputTorque();
		return total;
	}

	private float getTotalInputSpeed()
	{
		float total = 0;
		for(IRotaryEnergy source : sources)
			total += source.getOutputRotationSpeed();
		return total;
	}

	//--- Getters ---//

	@Nullable
	public AxisDirection getDirection()
	{
		if(currentGear < 0||currentGear >= ratios.length||ratios[currentGear]==0)
			return null;
		return ratios[currentGear] > 0?AxisDirection.POSITIVE: AxisDirection.NEGATIVE;
	}

	public double getCurrentGearRatio()
	{
		return getRatio(currentGear);
	}

	public int getCurrentGear()
	{
		return currentGear;
	}

	public int getNextGear()
	{
		return gearShiftDelay > 0?nextGear: requestedGear >= 0?requestedGear: currentGear;
	}

	public int getRequestedGear()
	{
		return requestedGear;
	}

	public boolean isShiftRequested()
	{
		return requestedGear >= 0;
	}

	public boolean isGearboxShocked()
	{
		return gearboxShockTicks > 0;
	}

	public float getShiftingProgress(float partialTicks)
	{
		if(currentGear==nextGear||gearShiftDelay <= 0)
			return 0;
		return (gearShiftDelay+partialTicks)/(float)maxGearShiftTime;
	}

	public float getTotalShiftingProgress(float partialTicks)
	{
		if(ratios.length==0)
			return 0;

		if(currentGear==nextGear||gearShiftDelay <= 0)
			return currentGear/((float)Math.max(1, ratios.length-1));

		if(currentGear > nextGear)
			return (currentGear-Math.min(1f, (gearShiftDelay+partialTicks)/maxGearShiftTime))/(float)Math.max(1, ratios.length-1);
		else
			return (currentGear+Math.min(1f, (gearShiftDelay+partialTicks)/maxGearShiftTime))/(float)Math.max(1, ratios.length-1);
	}


	//--- INBTSerializable ---//

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("currentGear", currentGear);
		nbt.setInteger("nextGear", nextGear);
		nbt.setInteger("requestedGear", requestedGear);
		nbt.setInteger("gearShiftDelay", gearShiftDelay);
		nbt.setInteger("gearboxShockTicks", gearboxShockTicks);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		currentGear = MathHelper.clamp(nbt.getInteger("currentGear"), 0, ratios.length-1);
		nextGear = MathHelper.clamp(nbt.getInteger("nextGear"), 0, ratios.length-1);
		requestedGear = nbt.hasKey("requestedGear")?nbt.getInteger("requestedGear"): -1;
		gearShiftDelay = nbt.getInteger("gearShiftDelay");
		gearboxShockTicks = nbt.getInteger("gearboxShockTicks");
	}

	//--- IVehicleComponent ---//

	@Nullable
	@Override
	public SyncedDurability getDurability()
	{
		return durability;
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
		return RotationSide.BOTH;
	}
}
