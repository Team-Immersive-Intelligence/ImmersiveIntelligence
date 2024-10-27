package pl.pabilo8.immersiveintelligence.api.data.operations.text;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 21.01.2022
 */
@DataOperation.DataOperationMeta(name = "string_replace_first", allowedTypes = {DataTypeString.class, DataTypeString.class, DataTypeString.class}, params = {"text", "fragment", "with"}, expectedResult = DataTypeString.class)
public class DataOperationStringReplaceFirst extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeString t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		DataTypeString t2 = packet.getVarInType(DataTypeString.class, data.getArgument(1));
		DataTypeString t3 = packet.getVarInType(DataTypeString.class, data.getArgument(2));

		return new DataTypeString(t1.value.replaceFirst(t2.value, t3.value));
	}
}
