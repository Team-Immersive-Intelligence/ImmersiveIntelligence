package pl.pabilo8.immersiveintelligence.api.data.operations.advanced_logic;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationXNOR extends DataOperation
{
	public DataOperationXNOR()
	{
		//A boolean version of the 'equals' operation
		name = "xnor";
		expression = "!^";
		allowedTypes = new Class[]{DataTypeBoolean.class,DataTypeBoolean.class};
		expectedResult = DataTypeBoolean.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeBoolean t1, t2;
		boolean i1, i2;

		t1 = packet.getVarInType(DataTypeBoolean.class, data.getArgument(0));
		t2 = packet.getVarInType(DataTypeBoolean.class, data.getArgument(1));
		i1 = t1.value;
		i2 = t2.value;

		//Yes
		return new DataTypeBoolean(i1==i2);
	}
}
