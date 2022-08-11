package pl.pabilo8.immersiveintelligence.api.data.operations.array;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationSet extends DataOperation
{
	public DataOperationSet()
	{
		name = "array_set";
		allowedTypes = new Class[]{DataTypeArray.class, DataTypeInteger.class, IDataType.class};
		params = new String[]{"array", "index", "value"};
		expectedResult = DataTypeNull.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeArray array = packet.getVarInType(DataTypeArray.class, data.getArgument(0));
		int index = packet.getVarInType(DataTypeInteger.class, data.getArgument(1)).value;
		IDataType t3 = data.getArgument(2);

		//simple method for removing infinite accessor loop
		int tries = 0;
		while(t3 instanceof DataTypeAccessor&&tries < 32)
		{
			t3 = ((DataTypeAccessor)t3).getRealValue(packet);
			tries++;
		}

		IDataType[] arr = array.value;
		ArrayList<IDataType> iDataTypes = new ArrayList<>(Arrays.asList(arr));
		iDataTypes.add(index, t3);
		array.value = iDataTypes.toArray(new IDataType[0]);

		return array;
	}
}
