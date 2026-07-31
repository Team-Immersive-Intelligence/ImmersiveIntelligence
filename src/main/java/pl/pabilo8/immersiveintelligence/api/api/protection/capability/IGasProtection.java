package pl.pabilo8.immersiveintelligence.api.api.protection.capability;

/**
 * Capability exposed by equipment capable of filtering harmful gases.
 *
 * @since 0.3.1
 */
@FunctionalInterface
public interface IGasProtection
{
	boolean protectsFromGases();
}
