package pl.pabilo8.immersiveintelligence.api.data.operations.array;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "array_set", allowedTypes = {DataTypeArray.class, DataTypeInteger.class, DataType.class},
		params = {"array", "index", "value"}, expectedResult = DataTypeNull.class, resultMatters = false)
public class DataOperationArraySet extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeArray array = packet.getVarInType(DataTypeArray.class, data.getArgument(0));
		int index = packet.getVarInType(DataTypeInteger.class, data.getArgument(1)).value;
		DataType t3 = data.getArgument(2);

		//simple method for removing infinite accessor loop
		int tries = 0;
		while(t3 instanceof DataTypeAccessor&&tries < 32)
		{
			t3 = ((DataTypeAccessor)t3).getRealValue(packet);
			tries++;
		}

		DataType[] arr = array.value;
		ArrayList<DataType> dataTypes = new ArrayList<>(Arrays.asList(arr));
		dataTypes.add(index, t3);
		array.value = dataTypes.toArray(new DataType[0]);

		return array;
	}
}
