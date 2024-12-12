package pl.pabilo8.immersiveintelligence.api.data.operations.array;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "array_swap", allowedTypes = {DataTypeArray.class, DataTypeInteger.class, DataTypeInteger.class},
		params = {"array", "first", "second"}, expectedResult = DataTypeNull.class, resultMatters = false)
public class DataOperationArraySwap extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeArray array = packet.getVarInType(DataTypeArray.class, data.getArgument(0));
		int i1 = packet.getVarInType(DataTypeInteger.class, data.getArgument(1)).value;
		int i2 = packet.getVarInType(DataTypeInteger.class, data.getArgument(2)).value;

		if(i1 >= 0&&i2 >= 0&&i1 < array.value.length&&i2 < array.value.length)
		{
			DataType helper = array.value[i1];
			array.value[i1] = array.value[i2];
			array.value[i2] = helper;
		}

		return new DataTypeNull();
	}
}
