package pl.pabilo8.immersiveintelligence.api.bullets;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;

/**
 * @author Pabilo8
 * @since 13-09-2019
 */
public interface IAmmoCore
{
	//Gets the name of the component
	String getName();

	//Material used to create the component
	IngredientStack getMaterial();

	//Gets the density (density * amount = mass)
	float getDensity();

	//The damage modifier of this bullet core (1.5 - normal+0.5, 1.0 - normal, 0.5 - half)
	float getDamageModifier();

	//The explosion effect modifier of this bullet core (1 - full, 0.5 - half, 0 - no explosion)
	float getExplosionModifier();

	//The the hardness level this bullet core can penetrate (6-stone)
	float getPenetrationHardness();

	int getColour();
}
