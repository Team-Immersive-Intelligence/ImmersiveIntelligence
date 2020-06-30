package pl.pabilo8.immersiveintelligence.api.bullets;

/**
 * @author Pabilo8
 * @since 13-09-2019
 */
public interface IBulletCoreType extends IBulletComponent
{
	//The damage modifier of this bullet core (1 - full, 0.5 - half, 0 - no explosion)
	float getExplosionModifier();
}
