package pl.pabilo8.immersiveintelligence.api.data.operations.vector;

import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeVector;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.NumericDataType;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.11.2024
 **/
@DataOperation.DataOperationMeta(name = "vector_create_angle", expression = "<vector_a>",
		allowedTypes = {NumericDataType.class, NumericDataType.class, NumericDataType.class}, params = {"offset", "yaw", "pitch"},
		expectedResult = DataTypeVector.class)
public class DataOperationVectorCreateAngle extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		float offset = packet.getVarInType(NumericDataType.class, data.getArgument(0)).floatValue();
		float yaw = packet.getVarInType(NumericDataType.class, data.getArgument(1)).floatValue();
		float pitch = packet.getVarInType(NumericDataType.class, data.getArgument(2)).floatValue();

		Vec3d vec3d = IIMath.offsetPosDirection(offset, yaw, pitch);
		return new DataTypeVector((float)vec3d.x, (float)vec3d.y, (float)vec3d.z);
	}
}
