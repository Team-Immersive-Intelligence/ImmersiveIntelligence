package pl.pabilo8.immersiveintelligence.api.data.operations.advanced_arithmetic;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation.DataOperationMeta;
import pl.pabilo8.immersiveintelligence.api.data.types.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.api.data.types.NumericDataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperationMeta(name = "sin", allowedTypes = {NumericDataType.class}, params = {"number"}, expectedResult = NumericDataType.class)
public class DataOperationTan extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		NumericDataType t1 = packet.getVarInType(NumericDataType.class, data.getArgument(0));
		return new DataTypeFloat((float)Math.tan(t1.floatValue()));
	}
}
