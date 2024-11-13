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
@DataOperation.DataOperationMeta(name = "abs", allowedTypes = {NumericDataType.class}, params = {"value"}, expectedResult = NumericDataType.class)
public class DataOperationAbs extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		NumericDataType t1 = packet.getVarInType(NumericDataType.class, data.getArgument(0));

		if(t1 instanceof DataTypeInteger)
			return new DataTypeInteger(Math.abs(t1.intValue()));
		return new DataTypeFloat(Math.abs(t1.floatValue()));
	}
}
