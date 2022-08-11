package pl.pabilo8.immersiveintelligence.api.data.operations.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationIsNull extends DataOperation
{
	public DataOperationIsNull()
	{
		name = "is_null";
		allowedTypes = new Class[]{IDataType.class};
		params = new String[]{"checked"};
		expectedResult = DataTypeBoolean.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		return new DataTypeBoolean(packet.getVarInType(IDataType.class, data.getArgument(0)) instanceof DataTypeNull);
	}
}
