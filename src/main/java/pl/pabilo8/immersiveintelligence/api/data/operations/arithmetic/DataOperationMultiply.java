package pl.pabilo8.immersiveintelligence.api.data.operations.arithmetic;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.NumericDataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "multiply", expression = "*",
		allowedTypes = {NumericDataType.class, NumericDataType.class}, params = {"factor", "factor"},
		expectedResult = NumericDataType.class)
public class DataOperationMultiply extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		NumericDataType t1, t2;

		t1 = packet.getVarInType(NumericDataType.class, data.getArgument(0));
		t2 = packet.getVarInType(NumericDataType.class, data.getArgument(1));

		if(t1 instanceof DataTypeFloat||t2 instanceof DataTypeFloat)
			return new DataTypeFloat(t1.floatValue()*t2.floatValue());
		return new DataTypeInteger(t1.intValue()*t2.intValue());
	}
}

