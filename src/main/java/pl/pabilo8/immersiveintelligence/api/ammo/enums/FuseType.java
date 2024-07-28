package pl.pabilo8.immersiveintelligence.api.ammo.enums;

import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nonnull;
import java.util.Arrays;

public enum FuseType implements ISerializableEnum
{
	CONTACT('\u29b0'),
	TIMED('\u29b1'),
	PROXIMITY('\u29b2');

	public final char symbol;

	FuseType(char symbol)
	{
		this.symbol = symbol;
	}

	//TODO: 28.05.2024 replace with IIUtils.enumValue
	@Nonnull
	public static FuseType v(String s)
	{
		String ss = s.toUpperCase();
		return Arrays.stream(values()).filter(e -> e.name().equals(ss)).findFirst().orElse(CONTACT);
	}
}
