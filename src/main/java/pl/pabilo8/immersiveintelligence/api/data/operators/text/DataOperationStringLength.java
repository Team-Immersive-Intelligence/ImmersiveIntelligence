package pl.pabilo8.immersiveintelligence.api.data.operators.text;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationStringLength extends DataOperator
{
	public DataOperationStringLength()
	{
		//how many characters a string contains
		name = "string_length";
		sign = "";
		allowedType1 = DataPacketTypeString.class;
		allowedType2 = DataPacketTypeNull.class;
		expectedResult = DataPacketTypeInteger.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		IDataType t1 = getVarInType(DataPacketTypeString.class, data.getType1(), packet);
		return new DataPacketTypeInteger(t1.valueToString().length());
	}
}
