package pl.pabilo8.immersiveintelligence.api.data.operators.array;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationPop extends DataOperator
{
	public DataOperationPop()
	{
		name = "array_pop";
		sign = "[]>>";
		allowedType1 = DataPacketTypeArray.class;
		allowedType2 = DataPacketTypeNull.class;
		expectedResult = DataPacketTypeNull.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		DataPacketTypeArray t1;

		t1 = getVarInType(DataPacketTypeArray.class, data.getType1(), packet);

		IDataType[] arr = t1.value;
		return arr.length > 0?arr[arr.length-1]: new DataPacketTypeNull();
	}
}
