package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.propulsion;

import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleFuelTank;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 01.10.2025
 */
public class VehicleEngineFuelBased extends VehicleEngineBase<VehicleEngineFuelBased>
{
	protected VehicleFuelTank<?> fuelTank;

	public VehicleEngineFuelBased(VehicleFuelTank<?> fuelTank)
	{
		this.fuelTank = fuelTank;
	}

	@Override
	protected boolean canBeStarted()
	{
		//TODO: 02.12.2025 fuel condition
		return true;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		float newSpeed = acceleration*200;
		float newTorque = acceleration*50f;
		this.rotaryStorage.grow(newSpeed, newTorque, 0.15f);
	}
}
