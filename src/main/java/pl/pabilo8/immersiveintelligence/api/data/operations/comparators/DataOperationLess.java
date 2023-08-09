package pl.pabilo8.immersiveintelligence.api.data.operations.comparators;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataTypeNumeric;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationLess extends DataOperation
{
	public DataOperationLess()
	{
		name = "less";
		expression = "<";
		allowedTypes = new Class[]{IDataTypeNumeric.class, IDataTypeNumeric.class};
		expectedResult = DataTypeBoolean.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		IDataTypeNumeric t1 = packet.getVarInType(IDataTypeNumeric.class, data.getArgument(0));
		IDataTypeNumeric t2 = packet.getVarInType(IDataTypeNumeric.class, data.getArgument(1));

		return new DataTypeBoolean(t1.floatValue() < t2.floatValue());
	}
}