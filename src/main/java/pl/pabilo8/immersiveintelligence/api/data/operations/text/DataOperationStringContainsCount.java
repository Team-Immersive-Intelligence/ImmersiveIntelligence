package pl.pabilo8.immersiveintelligence.api.data.operations.text;

import org.apache.commons.lang3.StringUtils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "string_contains_count", allowedTypes = {DataTypeString.class, DataTypeString.class}, params = {"text", "fragment"}, expectedResult = DataTypeInteger.class)
public class DataOperationStringContainsCount extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeString t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		DataTypeString t2 = packet.getVarInType(DataTypeString.class, data.getArgument(1));

		return new DataTypeInteger(StringUtils.countMatches(t1.value, t2.value));
	}
}
