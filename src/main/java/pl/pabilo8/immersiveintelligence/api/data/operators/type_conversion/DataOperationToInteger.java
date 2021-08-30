package pl.pabilo8.immersiveintelligence.api.data.operators.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationToInteger extends DataOperator
{
	public DataOperationToInteger()
	{
		//A boolean version of the 'equals' operation
		name = "to_integer";
		sign = "";
		allowedType1 = DataPacketTypeNull.class;
		allowedType2 = DataPacketTypeNull.class;
		expectedResult = DataPacketTypeInteger.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		int e = 0;
		try
		{
			e = Integer.parseInt(getVarInType(IDataType.class, data.getType1(), packet).valueToString());
		}
		catch(NumberFormatException ignored)
		{

		}

		return new DataPacketTypeInteger(e);
	}
}
