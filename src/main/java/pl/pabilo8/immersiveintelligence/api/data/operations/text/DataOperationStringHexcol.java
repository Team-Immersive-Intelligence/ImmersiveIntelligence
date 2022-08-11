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
public class DataOperationStringHexcol extends DataOperation
{
	public DataOperationStringHexcol()
	{
		//Gets a substring (from 0 to given integer)
		name = "string_hexcol";
		allowedTypes = new Class[]{DataTypeString.class, DataTypeInteger.class};
		params = new String[]{"text","color"};
		expectedResult = DataTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeString t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		DataTypeInteger t2 = packet.getVarInType(DataTypeInteger.class, data.getArgument(1));

		return new DataTypeString(String.format("<hexcol=%s:%s>", Integer.toHexString(t2.value), t1.value));
	}
}
