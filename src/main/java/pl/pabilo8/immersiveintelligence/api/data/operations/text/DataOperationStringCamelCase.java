package pl.pabilo8.immersiveintelligence.api.data.operations.text;

import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationStringCamelCase extends DataOperation
{
	public DataOperationStringCamelCase()
	{
		name = "string_camel_case";
		allowedTypes = new Class[]{DataTypeString.class,DataTypeBoolean.class};
		params = new String[]{"text","start_small"};
		expectedResult = DataTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeString t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		DataTypeBoolean t2 = packet.getVarInType(DataTypeBoolean.class, data.getArgument(1));
		return new DataTypeString(Utils.toCamelCase(t1.value,t2.value));
	}
}
