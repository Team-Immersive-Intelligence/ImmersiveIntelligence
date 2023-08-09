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
public class DataOperationStringJoin extends DataOperation
{
	public DataOperationStringJoin()
	{
		//A string version of the 'add' operation
		name = "string_join";
		expression = "+";
		allowedTypes = new Class[]{DataTypeString.class, DataTypeString.class};
		params = new String[]{"text", "joined"};
		expectedResult = DataTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		IDataType t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		IDataType t2 = packet.getVarInType(DataTypeString.class, data.getArgument(1));

		return new DataTypeString(t1.valueToString()+t2.valueToString());
	}
}
