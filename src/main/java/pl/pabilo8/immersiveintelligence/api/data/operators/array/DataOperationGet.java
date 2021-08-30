package pl.pabilo8.immersiveintelligence.api.data.operators.array;

import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationGet extends DataOperator
{
	public DataOperationGet()
	{
		name = "array_get";
		sign = "[]>";
		allowedType1 = DataPacketTypeArray.class;
		allowedType2 = DataPacketTypeInteger.class;
		expectedResult = DataPacketTypeNull.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		DataPacketTypeArray t1;
		DataPacketTypeInteger t2;

		t1 = getVarInType(DataPacketTypeArray.class, data.getType1(), packet);
		t2 = getVarInType(DataPacketTypeInteger.class, data.getType2(), packet);

		IDataType[] arr = t1.value;
		return arr.length > 0?arr[MathHelper.clamp(t2.value, 0, arr.length-1)]: new DataPacketTypeNull();
	}
}
