package pl.pabilo8.immersiveintelligence.api.ammo.enums;

import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

/**
 * Defines the shape of {@link pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent} effects, specific for {@link CoreType}
 *
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 03.04.2024
 */
public enum ComponentEffectShape implements ISerializableEnum
{
	//default TNT explosion shape
	STAR,
	//ICBM mod style explosion orb shape
	ORB,
	//straight line
	LINE,
	//cone
	CONE
}
