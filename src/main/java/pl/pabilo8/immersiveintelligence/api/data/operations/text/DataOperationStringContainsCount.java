package pl.pabilo8.immersiveintelligence.api.data.operations.text;

import org.apache.commons.lang3.StringUtils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationStringContainsCount extends DataOperation
{
	public DataOperationStringContainsCount()
	{
		//A boolean version of the 'equals' operation
		name = "string_contains_count";
		allowedTypes = new Class[]{DataTypeString.class,DataTypeString.class};
		params = new String[]{"text","fragment"};
		expectedResult = DataTypeInteger.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeString t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		DataTypeString t2 = packet.getVarInType(DataTypeString.class, data.getArgument(1));

		return new DataTypeInteger(StringUtils.countMatches(t1.value, t2.value));
	}
}
