package pl.pabilo8.immersiveintelligence.api.data.operations.array;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeAccessor;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 09.10.2024
 */
public class DataOperationStart extends DataOperation
{
	public DataOperationStart()
	{
		name = "array_start";
		allowedTypes = new Class[]{DataTypeArray.class, IDataType.class};
		params = new String[]{"array", "first"};
		expectedResult = DataTypeArray.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		IDataType pushed = data.getArgument(1);
		//simple method for removing infinite accessor loop
		int tries = 0;
		while(pushed instanceof DataTypeAccessor&&tries < 32)
		{
			pushed = ((DataTypeAccessor)pushed).getRealValue(packet);
			tries++;
		}

		return new DataTypeArray(pushed);
	}
}
