package pl.pabilo8.immersiveintelligence.api.ammo.parts;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;

/**
 * @author Pabilo8
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 13-09-2019
 */
public abstract class AmmoCore
{
	protected float density;
	protected float damageModifier, explosionModifier;
	protected PenetrationHardness penetrationHardness;
	protected final String name;
	protected final int colour;

	public AmmoCore(String name, float density, PenetrationHardness penetrationHardness, float explosionModifier, float damageModifier, int colour)
	{
		this.density = density;
		this.damageModifier = damageModifier;
		this.explosionModifier = explosionModifier;
		this.penetrationHardness = penetrationHardness;
		this.name = name;
		this.colour = colour;
	}

	/**
	 * Gets the name of the core material
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the ingredient used to create this ammo core
	 */
	public abstract IngredientStack getMaterial();

	/**
	 * @return the density of core material (density * amount = mass)
	 */
	public float getDensity()
	{
		return density;
	}

	/**
	 * @return the damage modifier of this ammo core (1.5 - normal+0.5, 1.0 - normal, 0.5 - half)
	 */
	public float getDamageModifier()
	{
		return damageModifier;
	}

	/**
	 * @return the explosion effect modifier of this ammo core (1 - full, 0.5 - half, 0 - no explosion)
	 */
	public float getExplosionModifier()
	{
		return explosionModifier;
	}

	/**
	 * @return the hardness level this ammo core can penetrate (6-stone)
	 */
	public PenetrationHardness getPenetrationHardness()
	{
		return penetrationHardness;
	}

	/**
	 * @return the colour of this ammo core in RGB int format
	 */
	public int getColour()
	{
		return colour;
	}
}
