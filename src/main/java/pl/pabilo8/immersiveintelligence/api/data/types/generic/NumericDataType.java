package pl.pabilo8.immersiveintelligence.api.data.types.generic;

import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.IGenericDataType;

/**
 * @author Pabilo8
 * @since 16.01.2022
 */
@IGenericDataType(defaultType = DataTypeInteger.class)
public abstract class NumericDataType extends DataType implements IComparableDataType<NumericDataType>
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
	public boolean canCompareWith(DataType other)
	{
		return other instanceof NumericDataType;
	}

	@Override
	public int compareTo(NumericDataType other)
	{
		return Float.compare(this.floatValue(), other.floatValue());
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof NumericDataType&&((NumericDataType)obj).floatValue()==this.floatValue();
	}
}
