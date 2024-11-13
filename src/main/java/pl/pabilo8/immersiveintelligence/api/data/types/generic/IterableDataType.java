package pl.pabilo8.immersiveintelligence.api.data.types.generic;

import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.IGenericDataType;

/**
 * @author Pabilo8
 * @since 26.08.2022
 */
@IGenericDataType(defaultType = DataTypeArray.class)
public abstract class IterableDataType extends DataType
{

}
