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
@DataOperation.DataOperationMeta(name = "vector_dot",
		allowedTypes = {DataTypeVector.class, DataTypeVector.class}, params = {"first", "second"},
		expectedResult = DataTypeFloat.class)
public class DataOperationVectorDot extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeVector t1 = packet.getVarInType(DataTypeVector.class, data.getArgument(0));
		DataTypeVector t2 = packet.getVarInType(DataTypeVector.class, data.getArgument(1));
		return new DataTypeFloat(t1.x*t2.x+t1.y*t2.y+t1.z*t2.z);
	}
}
