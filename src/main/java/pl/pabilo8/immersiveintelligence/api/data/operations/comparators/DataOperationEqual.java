package pl.pabilo8.immersiveintelligence.api.data.operations.comparators;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation.DataOperationMeta;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * Used to compare two data types, and return true if they're equal
 *
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperationMeta(name = "equal", expression = "==",
		allowedTypes = {DataType.class, DataType.class}, params = {"compared", "compared"},
		expectedResult = DataTypeBoolean.class)
public class DataOperationEqual extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		return new DataTypeBoolean(data.getArgument(0).equals(data.getArgument(1)));
	}
}