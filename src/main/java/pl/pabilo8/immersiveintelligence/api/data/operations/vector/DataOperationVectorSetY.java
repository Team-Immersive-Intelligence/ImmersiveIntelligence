package pl.pabilo8.immersiveintelligence.api.data.operations.vector;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeVector;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.11.2024
 **/
@DataOperation.DataOperationMeta(name = "vector_set_y",
		allowedTypes = {DataTypeVector.class, DataTypeFloat.class}, params = {"vector", "y"},
		expectedResult = DataTypeVector.class)
public class DataOperationVectorSetY extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeVector vector = packet.getVarInType(DataTypeVector.class, data.getArgument(0));
		float y = packet.getVarInType(DataTypeFloat.class, data.getArgument(1)).value;
		return new DataTypeVector(vector.x, y, vector.z);
	}
}