package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.propulsion;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.util.carversound.ConditionCompoundSound;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleFuelTank;
import pl.pabilo8.immersiveintelligence.common.util.sound.AdvancedSounds.MultiSound;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 01.10.2025
 */
public class VehicleEngineFuelBased<V extends EntityVehicleBase<V>> extends VehicleEngineBase<VehicleEngineFuelBased<V>, V>
{
	protected int idleUsage = 4, maxUsage = 40;
	protected int outputSpeed = 200, outputTorque = 50;
	protected VehicleFuelTank<?> fuelTank;

	@SideOnly(Side.CLIENT)
	private ConditionCompoundSound<VehicleEngineFuelBased<V>> engineNoise;
	protected MultiSound engineSound = null;
	protected float pitchIdle = 0.95f, pitchMax = 1.25f;

	public VehicleEngineFuelBased(V vehicle, VehicleFuelTank<?> fuelTank)
	{
		super(vehicle);
		this.fuelTank = fuelTank;
	}

	//--- Setters ---//

	public VehicleEngineFuelBased<V> withFuelUsage(int idleUsage, int maxUsage)
	{
		this.idleUsage = idleUsage;
		this.maxUsage = maxUsage;
		return this;
	}

	public VehicleEngineFuelBased<V> withSpeedTorque(int outputSpeed, int outputTorque)
	{
		this.outputSpeed = outputSpeed;
		this.outputTorque = outputTorque;
		return this;
	}

	public VehicleEngineFuelBased<V> withEngineSound(MultiSound engineSound, float pitchIdle, float pitchMax)
	{
		this.engineSound = engineSound;
		this.pitchIdle = pitchIdle;
		this.pitchMax = pitchMax;
		return this;
	}

	//--- Update ---//

	@Override
	protected boolean canBeStarted()
	{
		return fuelTank.getFuelPercentage() > 0;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		//Play engine noise on client
		if(vehicle.world.isRemote&&engineSound!=null&&isActive())
		{
			if(engineNoise==null||engineNoise.isDonePlaying())
				this.engineNoise = new ConditionCompoundSound<>(engineSound, vehicle.getPositionVector(), this,
						engine -> engine.isActive()&&!engine.vehicle.isDead);
			this.engineNoise.setPosition(vehicle.getPositionVector());
			this.engineNoise.setVolume(1f);
			this.engineNoise.setPitch(pitchIdle+(pitchMax-pitchIdle)*acceleration);
		}

		if(isActive())
		{
			FluidStack drained = fuelTank.drain((int)(idleUsage+acceleration*maxUsage), false);
			if(drained==null)
			{
				//Out of fuel, stop the engine
				this.active = this.nextState = false;
				this.activeTicks = this.activationTicks = 0;
				return;
			}
			this.rotaryStorage.grow(acceleration*outputSpeed, acceleration*outputTorque, 0.15f);
		}
		else
		{
			this.rotaryStorage.setTorque(0);
			this.rotaryStorage.setRotationSpeed(0);
		}
	}
}
