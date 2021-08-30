package pl.pabilo8.immersiveintelligence.api.data.operators.entity;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationGetEntityName extends DataOperator
{
	public DataOperationGetEntityName()
	{
		//Whether the two itemstacks are compatible
		name = "entity_get_name";
		sign = "";
		allowedType1 = DataPacketTypeEntity.class;
		allowedType2 = DataPacketTypeNull.class;
		expectedResult = DataPacketTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		return new DataPacketTypeString(getVarInType(DataPacketTypeEntity.class, data.getType1(), packet).customName);
	}
}
