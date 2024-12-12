package pl.pabilo8.immersiveintelligence.api.data.operations.array;

import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "array_get", allowedTypes = {DataTypeArray.class, DataTypeInteger.class}, params = {"array", "index"}, expectedResult = DataType.class)
public class DataOperationArrayGet extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeArray array = packet.getVarInType(DataTypeArray.class, data.getArgument(0));
		DataTypeInteger index = packet.getVarInType(DataTypeInteger.class, data.getArgument(1));

		DataType[] arr = array.value;
		return arr.length > 0?arr[MathHelper.clamp(index.value, 0, arr.length-1)]: new DataTypeNull();
	}
}
