package pl.pabilo8.immersiveintelligence.api.data.operators.array;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationPush extends DataOperator
{
	public DataOperationPush()
	{
		name = "array_push";
		sign = "[]<<";
		allowedType1 = DataPacketTypeArray.class;
		allowedType2 = DataPacketTypeAccessor.class;
		expectedResult = DataPacketTypeNull.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		DataPacketTypeArray t1;
		IDataType t2;

		t1 = getVarInType(DataPacketTypeArray.class, data.getType1(), packet);
		t2 = data.getType2();
		//simple method for removing infinite accessor loop
		int tries = 0;
		while(t2 instanceof DataPacketTypeAccessor&&tries < 32)
		{
			t2 = ((DataPacketTypeAccessor)t2).getRealValue(packet);
			tries++;
		}

		IDataType[] arr = t1.value;
		ArrayList<IDataType> iDataTypes = new ArrayList<>(Arrays.asList(arr));
		iDataTypes.add(t2);
		t1.value = iDataTypes.toArray(new IDataType[0]);

		return new DataPacketTypeNull();
	}
}
