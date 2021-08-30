package pl.pabilo8.immersiveintelligence.api.data.operators.text;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationStringCharAt extends DataOperator
{
	public DataOperationStringCharAt()
	{
		//Gets a character in string at given position
		name = "string_char_at";
		sign = "";
		allowedType1 = DataPacketTypeString.class;
		allowedType2 = DataPacketTypeInteger.class;
		expectedResult = DataPacketTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		IDataType t1 = getVarInType(DataPacketTypeString.class, data.getType1(), packet);
		DataPacketTypeInteger t2 = getVarInType(DataPacketTypeInteger.class, data.getType2(), packet);

		return new DataPacketTypeString(String.valueOf(t1.valueToString().charAt(t2.value)));
	}
}
