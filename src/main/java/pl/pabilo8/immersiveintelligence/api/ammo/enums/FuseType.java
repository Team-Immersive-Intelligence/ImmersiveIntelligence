package pl.pabilo8.immersiveintelligence.api.ammo.enums;

import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ILocalizedEnum;

public enum FuseType implements ILocalizedEnum
{
	CONTACT('\u29b0'),
	TIMED('\u29b1'),
	PROXIMITY('\u29b2');

	public final char symbol;

	FuseType(char symbol)
	{
		this.symbol = symbol;
	}

	@Override
	public String geLocaleKey()
	{
		return IIReference.DESCRIPTION_KEY+"bullet_fuse.";
	}
}
