package pl.pabilo8.immersiveintelligence.api.data.operations.array;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeAccessor;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "array_push", allowedTypes = {DataTypeArray.class, DataType.class},
		params = {"array", "inserted"}, expectedResult = DataTypeNull.class, resultMatters = false)
public class DataOperationArrayPush extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeArray array;
		DataType pushed;

		array = packet.getVarInType(DataTypeArray.class, data.getArgument(0));
		pushed = data.getArgument(1);
		//simple method for removing infinite accessor loop
		int tries = 0;
		while(pushed instanceof DataTypeAccessor&&tries < 32)
		{
			pushed = ((DataTypeAccessor)pushed).getRealValue(packet);
			tries++;
		}

		DataType[] arr = array.value;
		ArrayList<DataType> dataTypes = new ArrayList<>(Arrays.asList(arr));
		dataTypes.add(pushed);
		array.value = dataTypes.toArray(new DataType[0]);

		return new DataTypeNull();
	}
}
