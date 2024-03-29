package pl.pabilo8.immersiveintelligence.api.ammo.enums;

import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nonnull;
import java.util.Arrays;

public enum ComponentRole implements ISerializableEnum
{
	GENERAL_PURPOSE(0xaaaaaa),
	SHRAPNEL(0x5592405),
	PIERCING(0xdc3939),
	SHAPED(0xdc3939),
	EXPLOSIVE(0xdcb365),
	INCENDIARY(0x762d2d),
	TRACER(0x6390dc),
	FLARE(0xb863dc),
	CHEMICAL(0x81dc64),
	SPECIAL(0x63dcc1);

	/**
	 * Used for item tooltips (if II font is enabled)
	 */
	private final int color;

	ComponentRole(int color)
	{
		this.color = color;
	}

	public int getColor()
	{
		return color;
	}

	@Nonnull
	public static ComponentRole v(String s)
	{
		String ss = s.toUpperCase();
		return Arrays.stream(values()).filter(e -> e.name().equals(ss)).findFirst().orElse(GENERAL_PURPOSE);
	}
}
