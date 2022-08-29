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
public class DataOperationStringLowerCase extends DataOperation
{
	public DataOperationStringLowerCase()
	{
		//how many characters a string contains
		name = "string_lowercase";
		allowedTypes = new Class[]{DataTypeString.class};
		params = new String[]{"text"};
		expectedResult = DataTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		IDataType t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		return new DataTypeString(t1.valueToString().toLowerCase());
	}
}
