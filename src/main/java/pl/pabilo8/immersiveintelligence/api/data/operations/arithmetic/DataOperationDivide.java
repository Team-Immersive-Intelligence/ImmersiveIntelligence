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
@DataOperation.DataOperationMeta(name = "divide", expression = "/",
		allowedTypes = {NumericDataType.class, NumericDataType.class}, params = {"dividend", "divisor"},
		expectedResult = NumericDataType.class)
public class DataOperationDivide extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		NumericDataType t1, t2;

		t1 = packet.getVarInType(NumericDataType.class, data.getArgument(0));
		t2 = packet.getVarInType(NumericDataType.class, data.getArgument(1));

		if(t2.floatValue()==0)
			return t1 instanceof DataTypeInteger?new DataTypeInteger(Integer.MAX_VALUE): new DataTypeFloat(Float.MAX_VALUE);

		if(t1 instanceof DataTypeFloat||t2 instanceof DataTypeFloat)
			return new DataTypeFloat(t1.floatValue()/t2.floatValue());
		return new DataTypeInteger(t1.intValue()/t2.intValue());
	}
}
