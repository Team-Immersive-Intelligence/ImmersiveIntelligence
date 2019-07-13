package pl.pabilo8.immersiveintelligence.api.data.operators.conditional_construct;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * Created by Pabilo8 on 05-07-2019.
 */
public class DataOperationConditionalConstructNull extends DataOperator
{
	public DataOperationConditionalConstructNull()
	{
		name = "construct_null";
		sign = "?->0";
		allowedType1 = DataPacketTypeBoolean.class;
		allowedType2 = DataPacketTypeNull.class;
		expectedResult = DataPacketTypeNull.class;

	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		DataPacketTypeBoolean t1;
		DataPacketTypeNull t2;
		boolean i1;

		t1 = ((DataPacketTypeBoolean)getVarInType(DataPacketTypeBoolean.class, data.getType1(), packet));
		t2 = ((DataPacketTypeNull)getVarInType(DataPacketTypeNull.class, data.getType2(), packet));
		i1 = t1.value;

		//Yes
		return i1?t2: new DataPacketTypeNull();
	}
}