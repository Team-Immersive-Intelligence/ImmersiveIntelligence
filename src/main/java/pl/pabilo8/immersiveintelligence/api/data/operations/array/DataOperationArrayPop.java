package pl.pabilo8.immersiveintelligence.api.data.operations.array;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "array_pop", allowedTypes = {DataTypeArray.class}, params = {"array"}, expectedResult = DataType.class)
public class DataOperationArrayPop extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeArray array;
		array = packet.getVarInType(DataTypeArray.class, data.getArgument(0));
		DataType[] arr = array.value;
		if(arr.length==0)
			return new DataTypeNull();

		DataType[] arrNew = new DataType[arr.length-1];
		System.arraycopy(arr, 0, arrNew, 0, arrNew.length);
		array.value = arrNew;

		return arr[arr.length-1];
	}
}
