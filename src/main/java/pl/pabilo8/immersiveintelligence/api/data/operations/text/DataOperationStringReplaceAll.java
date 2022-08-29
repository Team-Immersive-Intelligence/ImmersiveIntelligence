package pl.pabilo8.immersiveintelligence.api.data.operations.text;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 21.01.2022
 */
public class DataOperationStringReplaceAll extends DataOperation
{
	public DataOperationStringReplaceAll()
	{
		name = "string_replace_all";
		allowedTypes = new Class[]{DataTypeString.class, DataTypeString.class, DataTypeString.class};
		params = new String[]{"text", "fragment", "with"};
		expectedResult = DataTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeString t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		DataTypeString t2 = packet.getVarInType(DataTypeString.class, data.getArgument(1));
		DataTypeString t3 = packet.getVarInType(DataTypeString.class, data.getArgument(2));

		return new DataTypeString(t1.value.replaceAll(t2.value, t3.value));
	}
}
