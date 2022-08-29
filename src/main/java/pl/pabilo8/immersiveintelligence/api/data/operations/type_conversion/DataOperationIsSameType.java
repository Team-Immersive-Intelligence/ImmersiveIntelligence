package pl.pabilo8.immersiveintelligence.api.data.operations.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationIsSameType extends DataOperation
{
	public DataOperationIsSameType()
	{
		name = "is_same_type";
		expression = "~~";
		allowedTypes = new Class[]{IDataType.class, IDataType.class};
		params = new String[]{"checked", "against"};
		expectedResult = DataTypeBoolean.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		return new DataTypeBoolean(data.getArgument(0).getClass()==data.getArgument(1).getClass());
	}
}
