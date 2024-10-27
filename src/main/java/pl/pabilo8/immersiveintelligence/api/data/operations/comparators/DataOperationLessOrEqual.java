package pl.pabilo8.immersiveintelligence.api.data.operations.comparators;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.NumericDataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "less_or_equal", expression = "<=",
		allowedTypes = {NumericDataType.class, NumericDataType.class}, params = {"number", "number"},
		expectedResult = DataTypeBoolean.class)
public class DataOperationLessOrEqual extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		NumericDataType t1 = packet.getVarInType(NumericDataType.class, data.getArgument(0));
		NumericDataType t2 = packet.getVarInType(NumericDataType.class, data.getArgument(1));

		return new DataTypeBoolean(t1.floatValue() <= t2.floatValue());
	}
}
