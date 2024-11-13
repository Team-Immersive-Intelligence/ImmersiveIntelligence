package pl.pabilo8.immersiveintelligence.api.data.operations.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "is_null", allowedTypes = {DataType.class}, params = {"checked"}, expectedResult = DataTypeBoolean.class)
public class DataOperationIsNull extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		return new DataTypeBoolean(packet.getVarInType(DataType.class, data.getArgument(0)) instanceof DataTypeNull);
	}
}
