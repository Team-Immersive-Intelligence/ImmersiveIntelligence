package pl.pabilo8.immersiveintelligence.api.data.operations.arithmetic;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationAbs extends DataOperation
{
	public DataOperationAbs()
	{
		name = "abs";
		allowedTypes = new Class[]{IDataTypeNumeric.class};
		expectedResult = IDataTypeNumeric.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		IDataTypeNumeric t1 = packet.getVarInType(IDataTypeNumeric.class, data.getArgument(0));

		if(t1 instanceof DataTypeInteger)
			return new DataTypeInteger(Math.abs(t1.intValue()));
		return new DataTypeFloat(Math.abs(t1.floatValue()));
	}
}
