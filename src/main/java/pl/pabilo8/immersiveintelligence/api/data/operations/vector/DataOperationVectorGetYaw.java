package pl.pabilo8.immersiveintelligence.api.data.operations.vector;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeVector;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.NumericDataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.11.2024
 **/
@DataOperation.DataOperationMeta(name = "vector_get_yaw",
		allowedTypes = {DataTypeVector.class}, params = {"vector"},
		expectedResult = NumericDataType.class)
public class DataOperationVectorGetYaw extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeVector vector = packet.getVarInType(DataTypeVector.class, data.getArgument(0));
		float yaw;

		if(vector.x < 0&&vector.z >= 0)
			yaw = (float)(Math.atan(Math.abs((double)vector.x/(double)vector.z))/Math.PI*180D);
		else if(vector.x <= 0&&vector.z <= 0)
			yaw = (float)(Math.atan(Math.abs((double)vector.z/(double)vector.x))/Math.PI*180D)+90;
		else if(vector.z < 0)
			yaw = (float)(Math.atan(Math.abs((double)vector.x/(double)vector.z))/Math.PI*180D)+180;
		else
			yaw = (float)(Math.atan(Math.abs((double)vector.z/(double)vector.x))/Math.PI*180D)+270;

		return new DataTypeFloat(yaw);
	}
}
