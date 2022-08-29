package pl.pabilo8.immersiveintelligence.api.data.operations.array;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationSwap extends DataOperation
{
	public DataOperationSwap()
	{
		name = "array_swap";
		allowedTypes = new Class[]{DataTypeArray.class, DataTypeInteger.class, DataTypeInteger.class};
		params = new String[]{"array", "first", "second"};
		expectedResult = DataTypeNull.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeArray array = packet.getVarInType(DataTypeArray.class, data.getArgument(0));
		int i1 = packet.getVarInType(DataTypeInteger.class, data.getArgument(1)).value;
		int i2 = packet.getVarInType(DataTypeInteger.class, data.getArgument(2)).value;

		if(i1>=0&&i2>=0&&i1<array.value.length&&i2<array.value.length)
		{
			IDataType helper = array.value[i1];
			array.value[i1] = array.value[i2];
			array.value[i2] = helper;
		}

		return array;
	}
}
