package pl.pabilo8.immersiveintelligence.api.protection.protection.capability;

/**
 * Capability exposed by equipment capable of shielding its wearer from radiation.
 *
 * @since 0.3.1
 */
@FunctionalInterface
public interface IRadiationProtection
{
	boolean protectsFromRadiation();
}
