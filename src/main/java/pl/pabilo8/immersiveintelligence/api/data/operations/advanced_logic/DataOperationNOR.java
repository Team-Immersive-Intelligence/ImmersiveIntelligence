package pl.pabilo8.immersiveintelligence.api.data.operations.advanced_logic;

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
@DataOperation.DataOperationMeta(name = "nor", expression = "!|",
		allowedTypes = {DataTypeBoolean.class, DataTypeBoolean.class}, params = {"input", "input"},
		expectedResult = DataTypeBoolean.class)
public class DataOperationNOR extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeBoolean t1, t2;
		boolean i1, i2;

		t1 = packet.getVarInType(DataTypeBoolean.class, data.getArgument(0));
		t2 = packet.getVarInType(DataTypeBoolean.class, data.getArgument(1));
		i1 = t1.value;
		i2 = t2.value;

		//Yes
		return new DataTypeBoolean(!(i1||i2));
	}
}
