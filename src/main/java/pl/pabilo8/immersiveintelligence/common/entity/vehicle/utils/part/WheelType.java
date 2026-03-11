package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part;

/**
 * Represents types of wheels in vehicles.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 24.10.2025
 */
public enum WheelType
{
	STEERABLE,
	DRIVE,
	STEERABLE_DRIVE,
	IDLER;

	public boolean isDriven()
	{
		return this==DRIVE||this==STEERABLE_DRIVE;
	}

	public boolean isSteerable()
	{
		return this==STEERABLE||this==STEERABLE_DRIVE;
	}

}
