package pl.pabilo8.immersiveintelligence.api.data.operations.map;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeMap;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.11.2024
 **/
@DataOperation.DataOperationMeta(name = "map_create",
		allowedTypes = {DataType.class, DataType.class}, params = {"key", "value"},
		expectedResult = DataTypeMap.class)
public class DataOperationMapCreate extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataType key = packet.evaluateVariable(data.getArgument(0), false);
		DataType value = packet.evaluateVariable(data.getArgument(1), false);
		return new DataTypeMap(key, value);
	}
}