package pl.pabilo8.immersiveintelligence.api.data.operations.array;

import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationGet extends DataOperation
{
	public DataOperationGet()
	{
		name = "array_get";
		allowedTypes = new Class[]{DataTypeArray.class, DataTypeInteger.class};
		params = new String[]{"array","index"};
		expectedResult = IDataType.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeArray array;
		DataTypeInteger index;

		array = packet.getVarInType(DataTypeArray.class, data.getArgument(0));
		index = packet.getVarInType(DataTypeInteger.class, data.getArgument(1));

		IDataType[] arr = array.value;
		return arr.length > 0?arr[MathHelper.clamp(index.value, 0, arr.length-1)]: new DataTypeNull();
	}
}
