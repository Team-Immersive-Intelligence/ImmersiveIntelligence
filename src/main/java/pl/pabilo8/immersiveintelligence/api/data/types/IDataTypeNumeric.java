package pl.pabilo8.immersiveintelligence.api.data.types;

import pl.pabilo8.immersiveintelligence.api.data.types.IDataType.IGenericDataType;

/**
 * @author Pabilo8
 * @since 16.01.2022
 */
@IGenericDataType(defaultType = DataTypeInteger.class)
public interface IDataTypeNumeric extends IDataType
{
	DataTypeInteger asInt();

	default int intValue()
	{
		return asInt().value;
	}

	DataTypeFloat asFloat();

	default float floatValue()
	{
		return asFloat().value;
	}
}
