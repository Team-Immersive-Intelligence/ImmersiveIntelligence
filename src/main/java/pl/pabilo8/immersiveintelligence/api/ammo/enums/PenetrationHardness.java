package pl.pabilo8.immersiveintelligence.api.ammo.enums;

import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 27.03.2024
 */
public enum PenetrationHardness implements ISerializableEnum
{
	FOLIAGE(false),
	//Copper
	FRAGILE(false),
	GROUND(false),
	//Brass
	WOOD(false),
	WEAK_METAL(true),
	//Lead
	ROCK(false),
	BRICKS(true),
	//Iron
	IRON(true),
	CONCRETE(false),
	//Steel
	STEEL(true),
	PANZERCONCRETE(true),
	//Tungsten
	TUNGSTEN(true),
	//Depleted Uranium
	UBERCONCRETE(true),
	//Pabilium
	PABILIUM(false),
	BEDROCK(false);

	final boolean canRicochet;

	PenetrationHardness(boolean canRicochet)
	{
		this.canRicochet = canRicochet;
	}

	public boolean canRicochet()
	{
		return canRicochet;
	}
}
