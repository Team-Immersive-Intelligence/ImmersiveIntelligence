package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.propulsion;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 01.10.2025
 */
public class VehicleEngineSteam extends VehicleEngineBase<VehicleEngineSteam>
{
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
