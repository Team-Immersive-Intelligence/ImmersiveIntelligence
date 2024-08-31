package pl.pabilo8.immersiveintelligence.api.data.operations.advanced_arithmetic;

import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationRoot extends DataOperation
{
	public DataOperationRoot()
	{
		name = "root";
		allowedTypes = new Class[]{IDataTypeNumeric.class,IDataTypeNumeric.class};
		expectedResult = IDataTypeNumeric.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		IDataTypeNumeric t1, t2;

		t1 = packet.getVarInType(IDataTypeNumeric.class, data.getArgument(0));
		t2 = packet.getVarInType(IDataTypeNumeric.class, data.getArgument(1));

		if(t1 instanceof DataTypeFloat||t2 instanceof DataTypeFloat)
			return new DataTypeFloat((float)Math.round(IIMath.root(t1.floatValue(), t2.floatValue())));
		return new DataTypeInteger(((int)Math.round(IIMath.root(t1.intValue(), t2.intValue()))));
	}
}
