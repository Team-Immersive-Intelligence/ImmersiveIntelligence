package pl.pabilo8.immersiveintelligence.api.ammo.enums;

import java.util.Arrays;

public enum PenMaterialTypes
{
	//all the metals
	METAL(true),
	//dirt, sand, etc.
	GROUND(true),
	//stone, bricks
	SOLID(true),
	//unarmored mobs
	FLESH(false),
	//glass, wool, leaves, cloth
	LIGHT(false);

	private final boolean ricochet;

	PenMaterialTypes(boolean ricochet)
	{
		this.ricochet = ricochet;
	}

	public boolean canRicochetOff()
	{
		return ricochet;
	}

	public static PenMaterialTypes v(String s)
	{
		String ss = s.toUpperCase();
		return Arrays.stream(values()).filter(e -> e.name().equals(ss)).findFirst().orElse(METAL);
	}
}
