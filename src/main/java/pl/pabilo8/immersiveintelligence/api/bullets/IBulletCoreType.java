package pl.pabilo8.immersiveintelligence.api.bullets;

/**
 * Created by Pabilo8 on 13-09-2019.
 */
public interface IBulletCoreType extends IBulletComponent
{
	//The damage modifier of this bullet core (1 - full, 0.5 - half, 0 - no explosion)
	float getExplosionModifier();

	//The rgb color of the core (used in bullet stack rendering)
	int getColour();
}
