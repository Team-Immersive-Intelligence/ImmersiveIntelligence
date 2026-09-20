package pl.pabilo8.immersiveintelligence.api.protection.protection.capability;

/**
 * Capability exposed by item stacks resistant to corrosion damage.
 *
 * @since 0.3.1
 */
@FunctionalInterface
public interface ICorrosionProtection
{
	boolean protectsFromCorrosion();
}
