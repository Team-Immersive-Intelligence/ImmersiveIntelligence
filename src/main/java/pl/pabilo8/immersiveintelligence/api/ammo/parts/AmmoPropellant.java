package pl.pabilo8.immersiveintelligence.api.ammo.parts;

import pl.pabilo8.immersiveintelligence.api.ammo.enums.PropellantType;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 28.05.2024
 */
public abstract class AmmoPropellant extends AmmoPart
{
	private final PropellantType propellantType;
	private final float velocityModifier, failureRate;
	private final boolean worksInAtmosphere, worksInSpace;

	public AmmoPropellant(String name, float density, IIColor color, PropellantType propellantType, float velocityModifier, float failureRate, boolean worksInAtmosphere, boolean worksInSpace)
	{
		super(name, density, color);
		this.propellantType = propellantType;
		this.velocityModifier = velocityModifier;
		this.failureRate = failureRate;
		this.worksInAtmosphere = worksInAtmosphere;
		this.worksInSpace = worksInSpace;
	}

	/**
	 * @return The type of propellant
	 */
	public PropellantType getPropellantType()
	{
		return propellantType;
	}

	/**
	 * @return The velocity modifier of the propellant
	 */
	public float getVelocityModifier()
	{
		return velocityModifier;
	}

	/**
	 * @return The failure rate of the propellant
	 */
	public float getFailureRate()
	{
		return failureRate;
	}

	/**
	 * @return If the propellant works in the atmosphere
	 */
	public boolean isWorksInAtmosphere()
	{
		return worksInAtmosphere;
	}


	/**
	 * @return If the propellant works in space
	 */
	public boolean isWorksInSpace()
	{
		return worksInSpace;
	}
}
