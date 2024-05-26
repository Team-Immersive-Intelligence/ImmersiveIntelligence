package pl.pabilo8.immersiveintelligence.api.ammo.enums;

import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nonnull;
import java.util.Arrays;

public enum FuseTypes implements ISerializableEnum
{
	CONTACT('\u29b0'),
	TIMED('\u29b1'),
	PROXIMITY('\u29b2');

	public final char symbol;

	FuseTypes(char symbol)
	{
		this.symbol = symbol;
	}

	@Nonnull
	public static FuseTypes v(String s)
	{
		String ss = s.toUpperCase();
		return Arrays.stream(values()).filter(e -> e.name().equals(ss)).findFirst().orElse(CONTACT);
	}
}
