package pl.pabilo8.immersiveintelligence.api.data.operations.vector;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeVector;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.11.2024
 **/
@DataOperation.DataOperationMeta(name = "vector_normalize",
		allowedTypes = {DataTypeVector.class}, params = {"vector"},
		expectedResult = DataTypeVector.class)
public class DataOperationVectorNormalize extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeVector vector = packet.getVarInType(DataTypeVector.class, data.getArgument(0));
		float length = (float)Math.sqrt(vector.x*vector.x+vector.y*vector.y+vector.z*vector.z);
		return new DataTypeVector(vector.x/length, vector.y/length, vector.z/length);
	}
}