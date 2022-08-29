package pl.pabilo8.immersiveintelligence.api.data.operations.text;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationStringCharAt extends DataOperation
{
	public DataOperationStringCharAt()
	{
		//Gets a character in string at given position
		name = "string_char_at";
		allowedTypes = new Class[]{DataTypeString.class, DataTypeInteger.class};
		params = new String[]{"text","index"};
		expectedResult = DataTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		IDataType t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		DataTypeInteger t2 = packet.getVarInType(DataTypeInteger.class, data.getArgument(1));

		return new DataTypeString(String.valueOf(t1.valueToString().charAt(t2.value)));
	}
}
