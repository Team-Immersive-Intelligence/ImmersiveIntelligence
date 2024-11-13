package pl.pabilo8.immersiveintelligence.api.data.operations.array;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "array_length", allowedTypes = {DataTypeArray.class}, params = {"array"}, expectedResult = DataTypeInteger.class)
public class DataOperationArrayLength extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeArray array = packet.getVarInType(DataTypeArray.class, data.getArgument(0));
		return new DataTypeInteger(array.value.length);
	}
}
