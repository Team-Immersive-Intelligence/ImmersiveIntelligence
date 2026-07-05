package pl.pabilo8.immersiveintelligence.api.data.operations.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.07.2019
 */
@DataOperation.DataOperationMeta(name = "is_same_type", expression = "~~",
		allowedTypes = {DataType.class, DataType.class}, params = {"first", "second"},
		expectedResult = DataTypeBoolean.class)
public class DataOperationIsSameType extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataType t1 = packet.evaluateVariable(data.getArgument(0), false);
		DataType t2 = packet.evaluateVariable(data.getArgument(1), false);
		return new DataTypeBoolean(t1.getClass()==t2.getClass());
	}
}
