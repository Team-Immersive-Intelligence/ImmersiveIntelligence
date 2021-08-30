package pl.pabilo8.immersiveintelligence.api.data.operators.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationIsNull extends DataOperator
{
	public DataOperationIsNull()
	{
		//A boolean version of the 'equals' operation
		name = "is_null";
		sign = "== null";
		allowedType1 = DataPacketTypeNull.class;
		allowedType2 = DataPacketTypeNull.class;
		expectedResult = DataPacketTypeBoolean.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		return new DataPacketTypeBoolean(getVarInType(IDataType.class, data.getType1(), packet) instanceof DataPacketTypeNull);
	}
}
