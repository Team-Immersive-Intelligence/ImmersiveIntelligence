package pl.pabilo8.immersiveintelligence.api.data.operations.array;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeAccessor;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 09.10.2024
 */
@DataOperation.DataOperationMeta(name = "array_start", allowedTypes = {DataTypeArray.class, DataType.class}, params = {"array", "first"}, expectedResult = DataTypeArray.class)
public class DataOperationStart extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataType pushed = data.getArgument(1);
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
