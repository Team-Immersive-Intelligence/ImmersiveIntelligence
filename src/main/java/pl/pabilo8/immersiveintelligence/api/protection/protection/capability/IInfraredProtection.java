package pl.pabilo8.immersiveintelligence.api.protection.protection.capability;

/**
 * Capability exposed by equipment which conceals its wearer from infrared sensors.
 *
 * @since 0.3.1
 */
@FunctionalInterface
public interface IInfraredProtection
{
	boolean isInvisibleToInfrared();
}
