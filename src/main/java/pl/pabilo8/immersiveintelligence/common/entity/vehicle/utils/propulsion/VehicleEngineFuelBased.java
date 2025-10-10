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
		return false;
	}

	@Override
	public void onUpdate()
	{

	}
}
