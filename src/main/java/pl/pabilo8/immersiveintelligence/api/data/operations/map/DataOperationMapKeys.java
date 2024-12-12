package pl.pabilo8.immersiveintelligence.api.data.operations.map;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeMap;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.11.2024
 **/
@DataOperation.DataOperationMeta(name = "map_keys",
		allowedTypes = {DataTypeMap.class}, params = {"map"},
		expectedResult = DataTypeArray.class)
public class DataOperationMapKeys extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeMap map = packet.getVarInType(DataTypeMap.class, data.getArgument(0));
		List<DataType> keys = new ArrayList<>(map.keySet());
		return new DataTypeArray(keys);
	}
}