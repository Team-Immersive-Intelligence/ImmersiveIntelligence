package pl.pabilo8.immersiveintelligence.api.data.operators.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationToNull extends DataOperator
{
	public DataOperationToNull()
	{
		name = "to_null";
		sign = "";
		allowedType1 = DataPacketTypeNull.class;
		allowedType2 = DataPacketTypeNull.class;
		expectedResult = DataPacketTypeNull.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		//plain and simple, used to nullify values
		return new DataPacketTypeNull();
	}
}
