package pl.pabilo8.immersiveintelligence.api.data.operations.entity;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationGetEntityDimensionID extends DataOperation
{
	public DataOperationGetEntityDimensionID()
	{
		name = "entity_get_dimension_id";
		allowedTypes = new Class[]{DataTypeEntity.class};
		params = new String[]{"entity"};
		expectedResult = DataTypeInteger.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		return new DataTypeInteger(packet.getVarInType(DataTypeEntity.class, data.getArgument(0)).dimensionID);
	}
}
