package pl.pabilo8.immersiveintelligence.api.data.operations.text;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationStringReverse extends DataOperation
{
	public DataOperationStringReverse()
	{
		name = "string_reverse";
		allowedTypes = new Class[]{DataTypeString.class};
		params = new String[]{"text"};
		expectedResult = DataTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeString t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		return new DataTypeString(new StringBuilder(t1.value).reverse().toString());
	}
}
