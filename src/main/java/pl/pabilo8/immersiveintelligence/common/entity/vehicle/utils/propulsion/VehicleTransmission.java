package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.propulsion;

import net.minecraft.nbt.NBTTagCompound;
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
 * A combined gearbox-transmission class for vehicles. Links an {@link VehicleEngineBase engine} to a {@link EntityVehicleWheel group of wheels} or another transmission.
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
	private int currentGear = 0, nextGear = 0, gearShiftDelay = 0;
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
		return this;
	}

	public VehicleTransmission<V> withCurrentGear(int currentGear)
	{
		this.nextGear = this.currentGear = MathHelper.clamp(currentGear, 0, ratios.length-1);
		return this;
	}

	//--- Methods ---//

	public void onUpdate()
	{
		//Update gear shifting
		if(gearShiftDelay > 0)
		{
			gearShiftDelay--;
			if(gearShiftDelay==0)
				currentGear = nextGear;
		}

		//Update input rotary energy
		float totalInputTorque = 0;
		float totalInputSpeed = 0;
		for(IRotaryEnergy source : sources)
		{
			totalInputTorque += source.getOutputTorque();
			totalInputSpeed += source.getOutputRotationSpeed();
		}
		float currentRatio = ratios.length==0?1: (float)ratios[currentGear];
		rotaryStorage.setTorque(totalInputTorque/currentRatio);
		rotaryStorage.setRotationSpeed(totalInputSpeed*currentRatio);

		//Update output rotary energy
		float outputTorque = getOutputTorque();
		float outputSpeed = getOutputRotationSpeed();
		for(IRotaryEnergy receiver : receivers)
		{
			receiver.setTorque(outputTorque/receivers.length);
			receiver.setRotationSpeed(outputSpeed);
		}
	}

	public boolean shiftUp()
	{
		if(currentGear < ratios.length-1&&gearShiftDelay==0)
		{
			nextGear = currentGear+1;
			gearShiftDelay = maxGearShiftTime;
			return true;
		}
		return false;
	}

	public boolean shiftDown()
	{
		if(currentGear > 0&&gearShiftDelay==0)
		{
			nextGear = currentGear-1;
			gearShiftDelay = maxGearShiftTime;
			return true;
		}
		return false;
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
		if(currentGear < 0||currentGear >= ratios.length)
			return 0;
		return ratios[currentGear];
	}

	public int getCurrentGear()
	{
		return currentGear;
	}

	public int getNextGear()
	{
		return nextGear;
	}

	public float getShiftingProgress(float partialTicks)
	{
		if(currentGear==nextGear)
			return 0;
		return (gearShiftDelay+partialTicks)/(float)maxGearShiftTime;
	}

	public float getTotalShiftingProgress(float partialTicks)
	{
		if(ratios.length==0)
			return 0;

		if(currentGear==nextGear)
			return currentGear/((float)ratios.length-1);

		if(currentGear > nextGear)
			return (currentGear-Math.min(1f, (gearShiftDelay+partialTicks)/maxGearShiftTime))/(float)ratios.length;
		else
			return (currentGear+Math.min(1f, (gearShiftDelay+partialTicks)/maxGearShiftTime))/(float)ratios.length;
	}


	//--- INBTSerializable ---//

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("currentGear", currentGear);
		nbt.setInteger("nextGear", nextGear);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		currentGear = nbt.getInteger("currentGear");
		nextGear = nbt.getInteger("nextGear");
	}

	//--- INBTSerializable ---//

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
