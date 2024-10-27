package pl.pabilo8.immersiveintelligence.api.data.operations.entity;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeEntity;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "entity_get_y", allowedTypes = {DataTypeEntity.class}, params = {"entity"}, expectedResult = DataTypeInteger.class)
public class DataOperationGetEntityPosY extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		return new DataTypeInteger((int)packet.getVarInType(DataTypeEntity.class, data.getArgument(0)).lastPos.y);
	}
}
