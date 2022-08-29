package pl.pabilo8.immersiveintelligence.api.data.operations.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationToNull extends DataOperation
{
	public DataOperationToNull()
	{
		name = "to_null";
		expression = "<null>";
		allowedTypes = new Class[]{DataTypeNull.class};
		params = new String[]{"nullified"};
		expectedResult = DataTypeNull.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		//plain and simple, used to nullify values
		return new DataTypeNull();
	}
}
