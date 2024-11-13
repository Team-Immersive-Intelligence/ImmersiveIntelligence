package pl.pabilo8.immersiveintelligence.api.data.operations.logic;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "not", expression = "!",
		allowedTypes = {DataTypeBoolean.class}, params = {"input"},
		expectedResult = DataTypeBoolean.class)
public class DataOperationNOT extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		return new DataTypeBoolean(!packet.getVarInType(DataTypeBoolean.class, data.getArgument(0)).value);
	}
}