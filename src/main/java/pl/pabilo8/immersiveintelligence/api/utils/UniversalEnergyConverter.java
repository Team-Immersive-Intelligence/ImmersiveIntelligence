package pl.pabilo8.immersiveintelligence.api.utils;

/**
 * Created by Pabilo8 on 06-01-2020.
 * A utility / reference class for energy conversion
 */
public class UniversalEnergyConverter
{

	public enum EnergyUnits
	{
		IF("Immersive Flux", EnergyType.ELECTRICAL, 1),
		TESLA("Tesla", EnergyType.ELECTRICAL, 1),
		MJ("Minecraft Joules", EnergyType.ELECTRICAL, 10),
		EU("Energy Unit", EnergyType.ELECTRICAL, 4),

		RoF("Rotary Flux", EnergyType.ROTARY, 1),
		RU("Rotation Unit", EnergyType.ROTARY, 1),

		KU("Kinetic Unit", EnergyType.KINETIC, 1),

		HU("Heat Unit", EnergyType.HEAT, 1);

		String fullName;
		EnergyType type;
		/**
		 * Base value compared to the standard unit
		 */
		float value;

		EnergyUnits(String fullName, EnergyType type, float value)
		{
			this.fullName = fullName;
			this.type = type;
			this.value = value;
		}
	}

	public enum EnergyType
	{
		ELECTRICAL,
		ROTARY,
		KINETIC,
		HEAT
	}
}
