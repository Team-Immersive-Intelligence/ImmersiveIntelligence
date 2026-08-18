package pl.pabilo8.immersiveintelligence.api.api.protection.capability;

/**
 * Capability exposed by equipment capable of shielding its wearer from acid.
 *
 * @since 0.3.1
 */
@FunctionalInterface
public interface IAcidProtection
{
	boolean protectsFromAcid();
}
