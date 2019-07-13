package pl.pabilo8.immersiveintelligence.api.data.operators.text;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * Created by Pabilo8 on 05-07-2019.
 */
public class DataOperationJoin extends DataOperator
{
	public DataOperationJoin()
	{
		//A boolean version of the 'equals' operation
		name = "join";
		sign = "+";
		allowedType1 = DataPacketTypeString.class;
		allowedType2 = DataPacketTypeString.class;
		expectedResult = DataPacketTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		DataPacketTypeString t1, t2;
		String i1, i2;

		t1 = ((DataPacketTypeString)getVarInType(DataPacketTypeString.class, data.getType1(), packet));
		t2 = ((DataPacketTypeString)getVarInType(DataPacketTypeString.class, data.getType2(), packet));
		i1 = t1.value;
		i2 = t2.value;

		//Yes
		return new DataPacketTypeString(i1+i2);
	}
}
