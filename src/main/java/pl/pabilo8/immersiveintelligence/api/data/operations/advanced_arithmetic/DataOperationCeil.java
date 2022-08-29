package pl.pabilo8.immersiveintelligence.api.data.operations.advanced_arithmetic;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataTypeNumeric;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationCeil extends DataOperation
{
	public DataOperationCeil()
	{
		name = "ceil";
		allowedTypes = new Class[]{IDataTypeNumeric.class};
		expectedResult = DataTypeInteger.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		IDataTypeNumeric t1 = packet.getVarInType(IDataTypeNumeric.class, data.getArgument(0));
		return new DataTypeInteger((int)Math.ceil(t1.floatValue()));
	}
}
