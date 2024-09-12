package pl.pabilo8.immersiveintelligence.api.ammo.enums;

import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

public enum ComponentRole implements ISerializableEnum
{
	GENERAL_PURPOSE(0xaaaaaa),
	SHRAPNEL(0x3f4f57),
	PIERCING(0xdc3939),
	SHAPED(0xdc8a46),
	EXPLOSIVE(0xdcb365),
	INCENDIARY(0x6b4344),
	TRACER(0x92b3db),
	FLARE(0xc1a8db),
	TERRAIN_DENIAL(0x578060),
	CHEMICAL(0x6a8258),
	EMP(0x434b69),
	SPECIAL(0x63dcc1);

	/**
	 * Used for item tooltips (if II font is enabled)
	 */
	private final IIColor color;

	ComponentRole(int color)
	{
		this.color = IIColor.fromPackedRGB(color);
	}

	public IIColor getColor()
	{
		return color;
	}
}
