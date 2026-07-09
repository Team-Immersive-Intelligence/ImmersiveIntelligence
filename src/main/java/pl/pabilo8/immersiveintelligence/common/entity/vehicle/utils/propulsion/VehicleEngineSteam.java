package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.propulsion;

import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 01.10.2025
 */
public class VehicleEngineSteam<V extends EntityVehicleBase<V>> extends VehicleEngineBase<VehicleEngineSteam<V>, V>
{
	public VehicleEngineSteam(V vehicle)
	{
		super(vehicle);
	}

	@Override
	protected boolean canBeStarted()
	{
		return false;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
	}
}
