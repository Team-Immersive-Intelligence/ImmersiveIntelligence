package pl.pabilo8.immersiveintelligence.api.data.operators.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationToBoolean extends DataOperator
{
	public DataOperationToBoolean()
	{
		name = "to_boolean";
		sign = "";
		allowedType1 = DataPacketTypeNull.class;
		allowedType2 = DataPacketTypeNull.class;
		expectedResult = DataPacketTypeBoolean.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		IDataType type = getVarInType(IDataType.class, data.getType1(), packet);
		if(type instanceof DataPacketTypeBoolean)
			return type;
		else if(type instanceof DataPacketTypeString)
			return new DataPacketTypeBoolean(((DataPacketTypeString)type).value.equalsIgnoreCase("TRUE"));
		else if(type instanceof DataPacketTypeInteger)
			return new DataPacketTypeBoolean(((DataPacketTypeInteger)type).value > 0);

		return new DataPacketTypeBoolean(false);
	}
}
