package pl.pabilo8.immersiveintelligence.api.data.operations.array;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;
import java.util.ArrayList;

/**
 * Joins two arrays into a new array.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.09.2026
 */
@DataOperation.DataOperationMeta(name = "array_join", allowedTypes = {DataTypeArray.class, DataTypeArray.class},
		params = {"first", "second"}, expectedResult = DataTypeArray.class)
public class DataOperationArrayJoin extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeArray first = packet.getVarInType(DataTypeArray.class, data.getArgument(0));
		DataTypeArray second = packet.getVarInType(DataTypeArray.class, data.getArgument(1));
		ArrayList<DataType> values = new ArrayList<>();
		for(DataType dataType : first.value)
			values.add(dataType.clone());
		for(DataType dataType : second.value)
			values.add(dataType.clone());
		return new DataTypeArray(values);
	}
}
