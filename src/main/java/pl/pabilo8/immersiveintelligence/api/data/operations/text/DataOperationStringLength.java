package pl.pabilo8.immersiveintelligence.api.data.operations.text;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationStringLength extends DataOperation
{
	public DataOperationStringLength()
	{
		//how many characters a string contains
		name = "string_length";
		allowedTypes = new Class[]{DataTypeString.class};
		params = new String[]{"text"};
		expectedResult = DataTypeInteger.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		IDataType t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		return new DataTypeInteger(t1.valueToString().length());
	}
}
