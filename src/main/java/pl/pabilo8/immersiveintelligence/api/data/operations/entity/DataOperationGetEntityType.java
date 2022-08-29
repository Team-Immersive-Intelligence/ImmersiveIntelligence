package pl.pabilo8.immersiveintelligence.api.data.operations.entity;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeEntity;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationGetEntityType extends DataOperation
{
	public DataOperationGetEntityType()
	{
		name = "entity_get_type";
		allowedTypes = new Class[]{DataTypeEntity.class};
		params = new String[]{"entity"};
		expectedResult = DataTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		return new DataTypeString(packet.getVarInType(DataTypeEntity.class, data.getArgument(0)).entityClass);
	}
}
