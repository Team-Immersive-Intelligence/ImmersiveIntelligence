package pl.pabilo8.immersiveintelligence.api.data.operations.comparators;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation.DataOperationMeta;
import pl.pabilo8.immersiveintelligence.api.data.types.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.NumericDataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperationMeta(name = "equal", expression = "==",
		allowedTypes = {NumericDataType.class, NumericDataType.class}, params = {"number", "number"},
		expectedResult = DataTypeBoolean.class)
public class DataOperationEqual extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataType t1, t2;

		t1 = packet.getVarInType(NumericDataType.class, data.getArgument(0));
		t2 = packet.getVarInType(NumericDataType.class, data.getArgument(1));

		//Yes
		return new DataTypeBoolean(t1.equals(t2));
	}
}