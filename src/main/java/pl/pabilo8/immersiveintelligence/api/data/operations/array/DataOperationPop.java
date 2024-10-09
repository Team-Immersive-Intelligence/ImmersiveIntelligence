package pl.pabilo8.immersiveintelligence.api.data.operations.array;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationPop extends DataOperation
{
	public DataOperationPop()
	{
		name = "array_pop";
		allowedTypes = new Class[]{DataTypeArray.class};
		params = new String[]{"array"};
		expectedResult = IDataType.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeArray array;
		array = packet.getVarInType(DataTypeArray.class, data.getArgument(0));
		IDataType[] arr = array.value;
		if(arr.length==0)
			return new DataTypeNull();

		IDataType[] arrNew = new IDataType[arr.length-1];
		System.arraycopy(arr, 0, arrNew, 0, arrNew.length);
		array.value = arrNew;

		return arr[arr.length-1];
	}
}
