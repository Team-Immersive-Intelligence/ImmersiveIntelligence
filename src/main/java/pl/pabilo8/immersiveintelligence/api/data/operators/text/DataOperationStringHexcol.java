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
public class DataOperationStringHexcol extends DataOperator
{
	public DataOperationStringHexcol()
	{
		//Gets a substring (from 0 to given integer)
		name = "string_hexcol";
		sign = "";
		allowedType1 = DataPacketTypeString.class;
		allowedType2 = DataPacketTypeInteger.class;
		expectedResult = DataPacketTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		DataPacketTypeString t1 = getVarInType(DataPacketTypeString.class, data.getType1(), packet);
		DataPacketTypeInteger t2 = getVarInType(DataPacketTypeInteger.class, data.getType2(), packet);

		return new DataPacketTypeString(String.format("<hexcol=%s:%s>", Integer.toHexString(t2.value), t1.value));
	}
}
