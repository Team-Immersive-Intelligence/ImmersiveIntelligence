package pl.pabilo8.immersiveintelligence.api.data.types;

import pl.pabilo8.immersiveintelligence.api.data.types.DataType.IGenericDataType;

/**
 * @author Pabilo8
 * @since 16.01.2022
 */
@IGenericDataType(defaultType = DataTypeInteger.class)
public abstract class NumericDataType extends DataType
{
	public abstract DataTypeInteger asInt();

	public int intValue()
	{
		return asInt().value;
	}

	public abstract DataTypeFloat asFloat();

	public float floatValue()
	{
		return asFloat().value;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof NumericDataType&&((NumericDataType)obj).floatValue()==this.floatValue();
	}
}
