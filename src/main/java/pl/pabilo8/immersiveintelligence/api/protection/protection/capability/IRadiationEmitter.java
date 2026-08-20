package pl.pabilo8.immersiveintelligence.api.protection.protection.capability;

/**
 * Capability exposed by entities and tile entities which emit radiation.
 * Block emitters are represented by their tile entity.
 *
 * @since 0.3.1
 */
public interface IRadiationEmitter
{
	float getRadiationRadius();

	float getRadiationStrength();

	boolean isRadiationActive();
}
