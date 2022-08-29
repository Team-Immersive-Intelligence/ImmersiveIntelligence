package pl.pabilo8.immersiveintelligence.api.data.operations.logic;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationNOT extends DataOperation
{
	public DataOperationNOT()
	{
		name = "not";
		expression = "!";
		allowedTypes = new Class[]{DataTypeBoolean.class};
		expectedResult = DataTypeBoolean.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		return new DataTypeBoolean(!packet.getVarInType(DataTypeBoolean.class, data.getArgument(0)).value);
	}
}