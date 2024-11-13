package pl.pabilo8.immersiveintelligence.api.data.operations.comparators;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.IComparableDataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "less", expression = "<",
		allowedTypes = {DataType.class, DataType.class}, params = {"compared", "compared"},
		expectedResult = DataTypeBoolean.class)
public class DataOperationLess extends DataOperation
{
	@Nonnull
	@Override
	@SuppressWarnings({"raw", "unchecked"})
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataType t1 = packet.evaluateVariable(data.getArgument(0), false);
		DataType t2 = packet.evaluateVariable(data.getArgument(1), false);

		if(t1 instanceof IComparableDataType&&t2 instanceof IComparableDataType&&((IComparableDataType<?>)t1).canCompareWith(t2))
			return new DataTypeBoolean(((IComparableDataType)t1).lessThan(t2));
		else
			return new DataTypeBoolean(false);
	}
}