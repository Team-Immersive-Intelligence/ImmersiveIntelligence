package pl.pabilo8.immersiveintelligence.api.data.operations.text;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.common.IILogger;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationStringContains extends DataOperation
{
	public DataOperationStringContains()
	{
		//A boolean version of the 'equals' operation
		name = "string_contains";
		allowedTypes = new Class[]{DataTypeString.class,DataTypeString.class};
		params = new String[]{"text","fragment"};
		expectedResult = DataTypeBoolean.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeString t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		DataTypeString t2 = packet.getVarInType(DataTypeString.class, data.getArgument(1));

		return new DataTypeBoolean(t1.value.contains(t2.value));
	}
}
