package pl.pabilo8.immersiveintelligence.api.data.operations.map;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeMap;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.11.2024
 **/
@DataOperation.DataOperationMeta(name = "map_set",
		allowedTypes = {DataTypeMap.class, DataType.class, DataType.class}, params = {"map", "key", "value"},
		expectedResult = DataTypeMap.class)
public class DataOperationMapSet extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeMap map = packet.getVarInType(DataTypeMap.class, data.getArgument(0));
		DataType key = data.getArgument(1);
		DataType value = data.getArgument(2);
		map.put(key, value);
		return new DataTypeNull();
	}
}