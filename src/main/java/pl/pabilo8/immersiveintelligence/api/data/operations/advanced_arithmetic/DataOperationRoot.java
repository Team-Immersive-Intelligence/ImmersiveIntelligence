package pl.pabilo8.immersiveintelligence.api.data.operations.advanced_arithmetic;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "root",
		allowedTypes = {NumericDataType.class, NumericDataType.class}, params = {"number", "root"},
		expectedResult = NumericDataType.class)
public class DataOperationRoot extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		NumericDataType t1, t2;

		t1 = packet.getVarInType(NumericDataType.class, data.getArgument(0));
		t2 = packet.getVarInType(NumericDataType.class, data.getArgument(1));

		if(t1 instanceof DataTypeFloat||t2 instanceof DataTypeFloat)
			return new DataTypeFloat((float)Math.round(IIMath.root(t1.floatValue(), t2.floatValue())));
		return new DataTypeInteger(((int)Math.round(IIMath.root(t1.intValue(), t2.intValue()))));
	}
}
