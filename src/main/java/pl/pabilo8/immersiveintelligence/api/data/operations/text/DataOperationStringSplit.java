package pl.pabilo8.immersiveintelligence.api.data.operations.text;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationStringSplit extends DataOperation
{
	public DataOperationStringSplit()
	{
		//A boolean version of the 'equals' operation
		name = "string_split";
		allowedTypes = new Class[]{DataTypeString.class, DataTypeString.class};
		params = new String[]{"text","separator"};
		expectedResult = DataTypeArray.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		IDataType t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		IDataType t2 = packet.getVarInType(DataTypeString.class, data.getArgument(1));

		//Lambdas are love, Lambdas are life!
		return new DataTypeArray(Arrays.stream(t1.valueToString().split(t2.valueToString()))
				.map(DataTypeString::new)
				.toArray(DataTypeString[]::new));
	}
}
