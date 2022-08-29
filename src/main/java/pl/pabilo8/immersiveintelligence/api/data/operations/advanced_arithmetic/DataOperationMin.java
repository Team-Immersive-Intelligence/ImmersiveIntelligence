package pl.pabilo8.immersiveintelligence.api.data.operations.advanced_arithmetic;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationMin extends DataOperation
{
	public DataOperationMin()
	{
		name = "min";
		allowedTypes = new Class[]{IDataTypeNumeric.class, IDataTypeNumeric.class};
		expectedResult = IDataTypeNumeric.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		IDataTypeNumeric t1, t2;

		t1 = packet.getVarInType(IDataTypeNumeric.class, data.getArgument(0));
		t2 = packet.getVarInType(IDataTypeNumeric.class, data.getArgument(1));

		if(t1 instanceof DataTypeFloat||t2 instanceof DataTypeFloat)
			return new DataTypeFloat(Math.min(t1.floatValue(), t2.floatValue()));
		return new DataTypeInteger(Math.min(t1.intValue(), t2.intValue()));
	}
}