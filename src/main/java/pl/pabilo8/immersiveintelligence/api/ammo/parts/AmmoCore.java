package pl.pabilo8.immersiveintelligence.api.ammo.parts;

import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 13-09-2019
 */
public abstract class AmmoCore extends AmmoPart
{
	protected float damageModifier, explosionModifier;
	protected PenetrationHardness penetrationHardness;

	public AmmoCore(String name, float density, PenetrationHardness penetrationHardness, float explosionModifier, float damageModifier, IIColor color)
	{
		super(name, density, color);
		this.damageModifier = damageModifier;
		this.explosionModifier = explosionModifier;
		this.penetrationHardness = penetrationHardness;
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
}
