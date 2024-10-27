package pl.pabilo8.immersiveintelligence.api.data.operations.text;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "string_split", allowedTypes = {DataTypeString.class, DataTypeString.class}, params = {"text", "separator"}, expectedResult = DataTypeArray.class)
public class DataOperationStringSplit extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataType t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		DataType t2 = packet.getVarInType(DataTypeString.class, data.getArgument(1));

		//Lambdas are love, Lambdas are life!
		return new DataTypeArray(Arrays.stream(t1.valueToString().split(t2.valueToString()))
				.map(DataTypeString::new)
				.toArray(DataTypeString[]::new));
	}
}
