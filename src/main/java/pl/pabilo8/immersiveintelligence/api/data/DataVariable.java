package pl.pabilo8.immersiveintelligence.api.data;

import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 24.06.2025
 */
@ParametersAreNonnullByDefault
public class DataVariable
{
	final char name;
	final DataType value;

	DataVariable(Character c, DataType type)
	{
		this.name = c;
		this.value = type;
	}

	public char getName()
	{
		return name;
	}

	public DataType getValue()
	{
		return value;
	}
}
