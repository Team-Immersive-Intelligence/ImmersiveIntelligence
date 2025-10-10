package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.propulsion;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleDurability;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleWheel;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.IVehicleComponent;

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
public class VehicleTransmission<T extends EntityVehicleBase<T>> implements IVehicleComponent, INBTSerializable<NBTTagCompound>, IRotaryEnergy
{
	@Nonnull
	protected final IRotaryEnergy[] sources;
	@Nonnull
	protected final IRotaryEnergy[] receivers;

	private VehicleDurability durability;
	private double[] ratios = new double[0];
	private int maxGearShiftTime = 20;
	private int currentGear = 0, nextGear = 0, gearShiftDelay = 0;
	private RotaryStorage rotaryStorage = new RotaryStorage(0, 0);

	@ParametersAreNonnullByDefault
	public VehicleTransmission(IRotaryEnergy[] sources, IRotaryEnergy[] receivers)
	{
		this.sources = sources;
		this.receivers = receivers;
	}

	@ParametersAreNonnullByDefault
	public VehicleTransmission(IRotaryEnergy source, IRotaryEnergy... receivers)
	{
		this.sources = new IRotaryEnergy[]{source};
		this.receivers = receivers;
	}


	public VehicleTransmission<T> withDurability(VehicleDurability durability)
	{
		this.durability = durability;
		return this;
	}

	public VehicleTransmission<T> withRatios(int shiftTime, double... ratios)
	{
		this.ratios = ratios;
		this.maxGearShiftTime = shiftTime;
		this.currentGear = MathHelper.clamp(this.currentGear, 0, ratios.length-1);
		return this;
	}

	//--- Methods ---//

	public void onUpdate()
	{

	}

	public boolean shiftUp()
	{
		return false;
	}

	public boolean shiftDown()
	{
		return false;
	}

	//--- Getters ---//

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
		return currentGear==nextGear?0: Math.min(1f, (gearShiftDelay+partialTicks)/maxGearShiftTime);
	}

	public float getTotalShiftingProgress(float partialTicks)
	{
		if(ratios.length==0)
			return 0;

		if(currentGear==nextGear)
			return currentGear/(float)ratios.length;

		if(currentGear > nextGear)
			return (currentGear-1+Math.min(1f, (gearShiftDelay+partialTicks)/maxGearShiftTime))/(float)ratios.length;
		else
			return (currentGear+Math.min(1f, (gearShiftDelay+partialTicks)/maxGearShiftTime))/(float)ratios.length;
	}


	//--- INBTSerializable ---//

	@Override
	public NBTTagCompound serializeNBT()
	{
		return new NBTTagCompound();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{

	}

	//--- INBTSerializable ---//

	@Nullable
	@Override
	public VehicleDurability getDurability()
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
	public float getOutputTorque()
	{
		return rotaryStorage.getTorque()*(ratios.length==0?1: (float)ratios[currentGear]);
	}

	@Override
	public float getOutputRotationSpeed()
	{
		return rotaryStorage.getRotationSpeed()/(ratios.length==0?1: (float)ratios[currentGear]);
	}

	@Override
	public RotationSide getSide(@Nullable EnumFacing facing)
	{
		return null;
	}
}
