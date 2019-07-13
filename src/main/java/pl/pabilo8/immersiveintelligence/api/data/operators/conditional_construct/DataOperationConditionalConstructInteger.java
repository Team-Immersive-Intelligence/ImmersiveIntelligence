package pl.pabilo8.immersiveintelligence.api.data.operators.conditional_construct;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * Created by Pabilo8 on 05-07-2019.
 */
public class DataOperationConditionalConstructInteger extends DataOperator
{
	public DataOperationConditionalConstructInteger()
	{
		name = "construct_integer";
		sign = "?->I";
		allowedType1 = DataPacketTypeBoolean.class;
		allowedType2 = DataPacketTypeInteger.class;
		expectedResult = DataPacketTypeInteger.class;

	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		DataPacketTypeBoolean t1;
		DataPacketTypeInteger t2;
		boolean i1;

		t1 = ((DataPacketTypeBoolean)getVarInType(DataPacketTypeBoolean.class, data.getType1(), packet));
		t2 = ((DataPacketTypeInteger)getVarInType(DataPacketTypeInteger.class, data.getType2(), packet));
		i1 = t1.value;

		//Yes
		return i1?t2: new DataPacketTypeNull();
	}
}