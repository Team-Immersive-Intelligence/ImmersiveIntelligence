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
public class DataOperationEqual extends DataOperation
{
	public DataOperationEqual()
	{
		name = "equal";
		expression = "==";
		allowedTypes = new Class[]{IDataTypeNumeric.class,IDataTypeNumeric.class};
		expectedResult = DataTypeBoolean.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		IDataType t1, t2;

		t1 = packet.getVarInType(IDataTypeNumeric.class, data.getArgument(0));
		t2 = packet.getVarInType(IDataTypeNumeric.class, data.getArgument(1));

		//Yes
		return new DataTypeBoolean(t1.equals(t2));
	}
}