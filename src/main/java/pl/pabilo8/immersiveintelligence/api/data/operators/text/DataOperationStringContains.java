package pl.pabilo8.immersiveintelligence.api.data.operators.text;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationStringContains extends DataOperator
{
	public DataOperationStringContains()
	{
		//A boolean version of the 'equals' operation
		name = "string_contains";
		sign = "";
		allowedType1 = DataPacketTypeString.class;
		allowedType2 = DataPacketTypeString.class;
		expectedResult = DataPacketTypeBoolean.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		DataPacketTypeString t1 = getVarInType(DataPacketTypeString.class, data.getType1(), packet);
		DataPacketTypeString t2 = getVarInType(DataPacketTypeString.class, data.getType2(), packet);

		return new DataPacketTypeBoolean(t1.value.contains(t2.value));
	}
}
