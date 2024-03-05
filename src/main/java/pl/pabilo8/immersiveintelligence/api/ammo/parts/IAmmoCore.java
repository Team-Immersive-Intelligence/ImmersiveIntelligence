package pl.pabilo8.immersiveintelligence.api.ammo.parts;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;

/**
 * @author Pabilo8
 * @updated 02.01.2024
 * @ii-approved 0.3.1
 * @since 13-09-2019
 */
public interface IAmmoCore
{
	/**
	 * Gets the name of the core material
	 */
	String getName();

	/**
	 * @return the ingredient used to create this ammo core
	 */
	IngredientStack getMaterial();

	/**
	 * @return the density of core material (density * amount = mass)
	 */
	float getDensity();

	/**
	 * @return the damage modifier of this ammo core (1.5 - normal+0.5, 1.0 - normal, 0.5 - half)
	 */
	float getDamageModifier();

	/**
	 * @return the explosion effect modifier of this ammo core (1 - full, 0.5 - half, 0 - no explosion)
	 */
	float getExplosionModifier();

	/**
	 * @return the hardness level this ammo core can penetrate (6-stone)
	 */
	float getPenetrationHardness();

	/**
	 * @return the colour of this ammo core in RGB int format
	 */
	int getColour();
}
