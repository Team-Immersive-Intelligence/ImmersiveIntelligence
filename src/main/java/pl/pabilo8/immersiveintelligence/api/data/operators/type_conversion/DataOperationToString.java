package pl.pabilo8.immersiveintelligence.api.data.operators.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationToString extends DataOperator
{
	public DataOperationToString()
	{
		//A boolean version of the 'equals' operation
		name = "to_string";
		sign = "";
		allowedType1 = DataPacketTypeNull.class;
		allowedType2 = DataPacketTypeNull.class;
		expectedResult = DataPacketTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		return new DataPacketTypeString(getVarInType(IDataType.class, data.getType1(), packet).valueToString());
	}
}
