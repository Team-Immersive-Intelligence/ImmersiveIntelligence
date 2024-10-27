package pl.pabilo8.immersiveintelligence.api.data.operations.text;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "string_length", allowedTypes = {DataTypeString.class}, params = {"text"}, expectedResult = DataTypeInteger.class)
public class DataOperationStringLength extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataType t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		return new DataTypeInteger(t1.valueToString().length());
	}
}
