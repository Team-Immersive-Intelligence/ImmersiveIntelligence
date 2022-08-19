package pl.pabilo8.immersiveintelligence.api.data.operations.array;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeAccessor;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationPush extends DataOperation
{
	public DataOperationPush()
	{
		name = "array_push";
		allowedTypes = new Class[]{DataTypeArray.class, IDataType.class};
		params = new String[]{"array","inserted"};
		expectedResult = DataTypeArray.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeArray array;
		IDataType pushed;

		array = packet.getVarInType(DataTypeArray.class, data.getArgument(0));
		pushed = data.getArgument(1);
		//simple method for removing infinite accessor loop
		int tries = 0;
		while(pushed instanceof DataTypeAccessor&&tries < 32)
		{
			pushed = ((DataTypeAccessor)pushed).getRealValue(packet);
			tries++;
		}

		IDataType[] arr = array.value;
		ArrayList<IDataType> iDataTypes = new ArrayList<>(Arrays.asList(arr));
		iDataTypes.add(pushed);
		array.value = iDataTypes.toArray(new IDataType[0]);

		return array;
	}
}
